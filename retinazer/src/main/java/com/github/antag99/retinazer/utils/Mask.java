package com.github.antag99.retinazer.utils;

import java.util.Arrays;

public final class Mask {
    private long[] words = new long[0];

    public Mask() {
    }

    public Mask set(Mask other) {
        if (words.length < other.words.length) {
            words = other.words.clone();
        } else {
            System.arraycopy(other.words, 0, words, 0, other.words.length);
            for (int i = other.words.length, n = words.length; i < n; i++)
                words[i] = 0;
        }
        return this;
    }

    public void clear() {
        final long[] words = this.words;
        for (int i = 0, n = words.length; i < n; i++)
            words[i] = 0L;
    }

    public void or(Mask other) {
        if (words.length < other.words.length)
            words = Arrays.copyOf(words, other.words.length);

        final long[] words = this.words;
        final long[] otherWords = other.words;
        final int commonWords = Math.min(words.length, otherWords.length);

        for (int i = 0, n = commonWords; i < n; i++) {
            words[i] |= otherWords[i];
        }
    }

    public void xor(Mask other) {
        if (words.length < other.words.length)
            words = Arrays.copyOf(words, other.words.length);

        final long[] words = this.words;
        final long[] otherWords = other.words;
        final int commonWords = Math.min(words.length, otherWords.length);

        for (int i = 0, n = commonWords; i < n; i++) {
            words[i] ^= otherWords[i];
        }
    }

    public void and(Mask other) {
        final long[] words = this.words;
        final long[] otherWords = other.words;
        final int commonWords = Math.min(words.length, otherWords.length);

        for (int i = otherWords.length, n = words.length; i < n; i++) {
            words[i] = 0;
        }

        for (int i = 0, n = commonWords; i < n; i++) {
            words[i] &= otherWords[i];
        }
    }

    public void set(int index) {
        final int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            words = Arrays.copyOf(words, Bag.nextPowerOfTwo(wordIndex + 1));
        }
        // Note: index is truncated before shifting
        words[wordIndex] |= 1L << index;
    }

    public void clear(int index) {
        final int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            return;
        }
        words[wordIndex] &= ~(1L << index);
    }

    public boolean get(int index) {
        final int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            return false;
        }
        return (words[wordIndex] & (1L << index)) != 0L;
    }

    public int nextSetBit(int index) {
        final long[] words = this.words;
        int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            return -1;
        }
        long word = words[wordIndex] & (-1L << index);
        while (true) {
            if (word != 0)
                return (wordIndex << 6) + Long.numberOfTrailingZeros(word);
            if (++wordIndex == words.length)
                return -1;
            word = words[wordIndex];
        }
    }

    public int nextClearBit(int index) {
        int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            return words.length >> 6;
        }
        long word = ~words[wordIndex] & (-1L << index);
        while (true) {
            if (word != 0)
                return (wordIndex << 6) + Long.numberOfTrailingZeros(word);
            if (++wordIndex == words.length)
                return words.length << 6;
            word = ~words[wordIndex];
        }
    }

    public boolean isSupersetOf(Mask other) {
        final long[] words = this.words;
        final long[] otherWords = other.words;
        final int commonWords = Math.min(words.length, otherWords.length);
        for (int i = commonWords, n = otherWords.length; i < n; i++)
            if (otherWords[i] != 0)
                return false;
        for (int i = 0, n = commonWords; i < n; i++)
            if ((words[i] & otherWords[i]) != otherWords[i])
                return false;
        return true;
    }

    public boolean isSubsetOf(Mask other) {
        return other.isSupersetOf(this);
    }

    public boolean intersects(Mask other) {
        final long[] words = this.words;
        final long[] otherWords = other.words;
        final int commonWords = Math.min(words.length, otherWords.length);
        for (int i = 0, n = commonWords; i < n; i++)
            if ((words[i] & otherWords[i]) != 0)
                return true;
        return false;
    }

    public int cardinality() {
        final long[] words = this.words;
        int cardinality = 0;
        for (int i = 0, n = words.length; i < n; i++)
            cardinality += Long.bitCount(words[i]);
        return cardinality;
    }
}
