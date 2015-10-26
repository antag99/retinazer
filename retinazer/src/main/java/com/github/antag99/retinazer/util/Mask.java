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
package com.github.antag99.retinazer.util;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Pool.Poolable;

public final class Mask implements Poolable {
    private long[] words = new long[0];

    public Mask() {
    }

    /**
     * Sets this mask to the value of the other mask.
     *
     * @param other The value to set.
     * @return {@code this} mask instance
     */
    public Mask set(Mask other) {
        return set(other.words);
    }

    public Mask set(long[] otherWords) {
        if (words.length < otherWords.length) {
            words = new long[otherWords.length];
        }
        System.arraycopy(otherWords, 0, words, 0, otherWords.length);
        for (int i = otherWords.length, n = words.length; i < n; i++)
            words[i] = 0;
        return this;
    }

    /**
     * Clears all bits in this mask.
     */
    public void clear() {
        long[] words = this.words;
        for (int i = 0, n = words.length; i < n; i++)
            words[i] = 0L;
    }

    /**
     * Sets all bits in this mask that are set in the other mask.
     *
     * @param other The other operand.
     */
    public void or(Mask other) {
        if (words.length < other.words.length) {
            long[] newWords = new long[other.words.length];
            System.arraycopy(words, 0, newWords, 0, words.length);
            this.words = newWords;
        }

        long[] words = this.words;
        long[] otherWords = other.words;
        int commonWords = Math.min(words.length, otherWords.length);

        for (int i = 0, n = commonWords; i < n; i++) {
            words[i] |= otherWords[i];
        }
    }

    /**
     * Clears all bits in this mask that are also in the other masks, and sets
     * all bits in this mask that are in the other mask but not in this mask.
     *
     * @param other The other operand.
     */
    public void xor(Mask other) {
        if (words.length < other.words.length) {
            long[] newWords = new long[other.words.length];
            System.arraycopy(words, 0, newWords, 0, words.length);
            this.words = newWords;
        }

        long[] words = this.words;
        long[] otherWords = other.words;
        int commonWords = Math.min(words.length, otherWords.length);

        for (int i = 0, n = commonWords; i < n; i++) {
            words[i] ^= otherWords[i];
        }
    }

    /**
     * Clears all bits in this mask that are not in the other mask.
     *
     * @param other The other operand.
     */
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

    /**
     * Clears all the bits in this mask contained in the other mask.
     *
     * @param other The other operand.
     */
    public void andNot(Mask other) {
        long[] words = this.words;
        long[] otherWords = other.words;
        int commonWords = Math.min(words.length, otherWords.length);

        for (int i = 0; i < commonWords; i++) {
            words[i] &= ~otherWords[i];
        }
    }

    /**
     * Sets the bit at the given index in this mask.
     *
     * @param index The index of the bit.
     */
    public void set(int index) {
        int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            int newCapacity = Bag.nextPowerOfTwo(wordIndex + 1);
            long[] newWords = new long[newCapacity];
            System.arraycopy(words, 0, newWords, 0, words.length);
            this.words = newWords;
        }
        // Note: index is truncated before shifting
        words[wordIndex] |= 1L << index;
    }

    /**
     * Sets the bit at the given index in this mask to the given value.
     *
     * @param index The index of the bit.
     * @param value The value of the bit.
     */
    public void set(int index, boolean value) {
        if (value)
            set(index);
        else
            clear(index);
    }

    /**
     * Clears the bit at the given index in this mask.
     *
     * @param index The index of the bit.
     */
    public void clear(int index) {
        int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            return;
        }
        words[wordIndex] &= ~(1L << index);
    }

    /**
     * Gets the value of the bit at the given index in this mask.
     *
     * @param index The index of the bit.
     * @return The value of the bit.
     */
    public boolean get(int index) {
        int wordIndex = index >> 6;
        if (wordIndex >= words.length) {
            return false;
        }
        return (words[wordIndex] & (1L << index)) != 0L;
    }

    /**
     * Returns the index of the set bit that is higher than or equal to the
     * given index. Returns -1 in case no such bit exists.
     *
     * @param index The index to start looking from.
     * @return The index of the next set bit.
     */
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

    /**
     * Returns the index of the clear bit that is higher than or equal to the
     * given index.
     *
     * @param index The index to start looking from.
     * @return The index of the next clear bit.
     */
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

    /**
     * Returns whether all bits of the other mask are also contained in this mask.
     *
     * @param other The other mask.
     * @return Whether this mask is a superset of the other mask.
     */
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

    /**
     * Returns whether all bits of this mask are also contained in the other mask.
     *
     * @param other The other mask
     * @return Whether this mask is a subset of the other mask.
     */
    public boolean isSubsetOf(Mask other) {
        return other.isSupersetOf(this);
    }

    /**
     * Returns whether any bits of this mask are also contained in the other mask.
     *
     * @param other The other mask.
     * @return Whether this mask intersects the other mask.
     */
    public boolean intersects(Mask other) {
        final long[] words = this.words;
        final long[] otherWords = other.words;
        final int commonWords = Math.min(words.length, otherWords.length);
        for (int i = 0, n = commonWords; i < n; i++)
            if ((words[i] & otherWords[i]) != 0)
                return true;
        return false;
    }

    /**
     * Returns the number of set bits in this mask.
     *
     * @return The number of set bits in this mask.
     */
    public int cardinality() {
        final long[] words = this.words;
        int cardinality = 0;
        for (int i = 0, n = words.length; i < n; i++)
            cardinality += Long.bitCount(words[i]);
        return cardinality;
    }

    /**
     * Returns the index of the highest set bit in this mask plus one.
     *
     * @return The length of this mask.
     */
    public int length() {
        final long[] words = this.words;
        for (int i = words.length - 1; i >= 0; i--) {
            if (words[i] != 0L) {
                return (i << 6) + (64 - Long.numberOfLeadingZeros(words[i]));
            }
        }
        return 0;
    }

    /**
     * Returns the indices of the set bits in this mask.
     *
     * @return The indices of the set bits in this mask.
     */
    public int[] getIndices() {
        int[] indices = new int[cardinality()];
        for (int i = 0, b = nextSetBit(0), n = indices.length; i < n; i++, b = nextSetBit(b + 1)) {
            indices[i] = b;
        }
        return indices;
    }

    public void getIndices(IntArray out) {
        int offset = out.size;
        int count = cardinality();
        out.ensureCapacity(count);
        int[] items = out.items;
        for (int i = 0, b = nextSetBit(0), n = count; i < n; i++, b = nextSetBit(b + 1)) {
            items[offset + i] = b;
        }
        out.size += count;
    }

    public long getWord(int index) {
        return index < words.length ? words[index] : 0L;
    }

    public void setWord(int index, long word) {
        if (index >= words.length) {
            long[] newWords = new long[Bag.nextPowerOfTwo(index + 1)];
            System.arraycopy(words, 0, newWords, 0, words.length);
            this.words = newWords;
        }
        words[index] = word;
    }

    /**
     * Gets the amount of necessary words in this mask. Excludes trailing zero
     * words.
     */
    public int getWordCount() {
        final long[] words = this.words;
        for (int i = words.length - 1; i > 0; i--) {
            if (words[i] != 0L) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Gets the backing buffer of this mask. Does not exclude trailing zero
     * words.
     */
    public long[] getWords() {
        return words;
    }

    public boolean isEmpty() {
        final long[] words = this.words;
        for (int i = 0; i < words.length; i++) {
            if (words[i] != 0L) {
                return false;
            }
        }
        return true;
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

        for (int i = shorterWords.length - 1; i >= 0; i--) {
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
        for (int i = words.length - 1; i >= 0; i--) {
            long word = words[i];
            h = h * 31 + (int) (word ^ (word >>> 32));
        }
        return h;
    }

    @Override
    public void reset() {
        clear();
    }
}
