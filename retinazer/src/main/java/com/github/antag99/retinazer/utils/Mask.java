/*******************************************************************************
 * Copyright (C) 2015 Anton Gustafsson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
        long[] words = this.words;
        for (int i = 0, n = words.length; i < n; i++)
            words[i] = 0L;
    }

    public void or(Mask other) {
        if (words.length < other.words.length)
            words = Arrays.copyOf(words, other.words.length);

        long[] words = this.words;
        long[] otherWords = other.words;
        int commonWords = Math.min(words.length, otherWords.length);

        for (int i = 0, n = commonWords; i < n; i++) {
            words[i] |= otherWords[i];
        }
    }

    public void xor(Mask other) {
        if (words.length < other.words.length)
            words = Arrays.copyOf(words, other.words.length);

        long[] words = this.words;
        long[] otherWords = other.words;
        int commonWords = Math.min(words.length, otherWords.length);

        for (int i = 0, n = commonWords; i < n; i++) {
            words[i] ^= otherWords[i];
        }
    }

    public void and(Mask other) {
        long[] words = this.words;
        long[] otherWords = other.words;
        int commonWords = Math.min(words.length, otherWords.length);

        for (int i = otherWords.length, n = words.length; i < n; i++) {
            words[i] = 0;
        }

        for (int i = 0, n = commonWords; i < n; i++) {
            words[i] &= otherWords[i];
        }
    }

    public void andNot(Mask other) {
        long[] words = this.words;
        long[] otherWords = other.words;
        int commonWords = Math.min(words.length, otherWords.length);

        for (int i = 0; i < commonWords; i++) {
            words[i] &= ~otherWords[i];
        }
    }

    public void set(int index) {
        int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            words = Arrays.copyOf(words, Bag.nextPowerOfTwo(wordIndex + 1));
        }
        // Note: index is truncated before shifting
        words[wordIndex] |= 1L << index;
    }

    public void set(int index, boolean value) {
        if (value)
            set(index);
        else
            clear(index);
    }

    public void clear(int index) {
        int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            return;
        }
        words[wordIndex] &= ~(1L << index);
    }

    public boolean get(int index) {
        int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            return false;
        }
        return (words[wordIndex] & (1L << index)) != 0L;
    }

    /**
     * Inserts a zero bit at the given index; this shifts up the whole mask
     *
     * @param index The index to insert the bit
     */
    public void push(int index) {
        int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            return;
        }
        long word = words[wordIndex];
        words[wordIndex] = (word & ((1L << index) - 1)) |
                ((word & ~((1L << index) - 1)) << 1);
        boolean carry = (word >>> 63) != 0;
        for (int i = wordIndex + 1, n = words.length; i < n; i++) {
            word = words[i];
            words[i] = (word << 1) | (carry ? 1 : 0);
            carry = (word >>> 63) != 0;
        }
        if (carry) {
            int wordCount = words.length;
            words = Arrays.copyOf(words, words.length * 2);
            words[wordCount] = 1;
        }
    }

    /**
     * Pops the bit at the given index; this shifts down the whole mask
     *
     * @param index The index of the bit to pop
     */
    public void pop(int index) {
        long[] words = this.words;
        int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            return;
        }
        long word = words[wordIndex];
        words[wordIndex] = (word & ((1L << index) - 1)) |
                (((word >> 1) & ~((1L << index) - 1)));
        for (int i = wordIndex + 1, n = words.length; i < n; i++) {
            // Carry bit
            if ((words[i] & 1) != 0)
                words[i - 1] |= 1L << 63;
            // Shift down
            words[i] >>>= 1;
        }
    }

    public int nextSetBit(int index) {
        long[] words = this.words;
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

    public int length() {
        final long[] words = this.words;
        for (int i = words.length - 1; i >= 0; i--) {
            if (words[i] != 0L) {
                return (i << 6) + (64 - Long.numberOfLeadingZeros(words[i]));
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        char[] value = new char[length()];
        for (int i = 0, n = value.length; i < n; i++) {
            value[n - 1 - i] = get(i) ? '1' : '0';
        }
        return new String(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Mask))
            return false;
        Mask mask = (Mask) obj;
        long[] shorterWords = this.words;
        long[] longerWords = mask.words;
        if (shorterWords.length > longerWords.length) {
            shorterWords = mask.words;
            longerWords = this.words;
        }

        // Check that exceeding words are zero
        for (int i = shorterWords.length, n = longerWords.length; i < n; i++) {
            if (longerWords[i] != 0L) {
                return false;
            }
        }

        for (int i = shorterWords.length - 1; i != 0; i--) {
            if (shorterWords[i] != longerWords[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        long[] words = this.words;
        int h = 0;
        // As trailing zero words do not count in the hash, this should result
        // in the same hash no matter the size of the buffer.
        for (int i = words.length - 1; i != 0; i--) {
            long word = words[i];
            h = h * 31 + (int) (word ^ (word >>> 32));
        }
        return h;
    }
}
