/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.util;

/**
 * A list of bits.
 */
//�±궼�Ǵ�0��ʼ�����ģ�data[0]��ʾ0��63��data[1]��ʾ64��127������
public final class BitField {

    private static final int ADDRESS_BITS = 6; //��ʾ2��6�η����պõ���64, i >> ADDRESS_BITSʱ��ȷ������data���Ǹ��±�
    private static final int BITS = 64;
    private static final int ADDRESS_MASK = BITS - 1;
    private long[] data;
    private int maxLength;

    public BitField() {
        this(64);
    }

	// �Ҽ��ϵ�
	public String toString() {
		return stringArray2(data);
	}

	// �Ҽ��ϵ�
	public static String stringArray2(long[] array) {
		if (array == null) {
			return "null";
		}
		StatementBuilder buff = new StatementBuilder("[");
		for (Object a : array) {
			buff.appendExceptFirst(", ");
			buff.append(a.toString());
		}

		buff.append("]");
		return buff.toString();
	}
    
    //capacity��ʾ��λ��
    public BitField(int capacity) {
    	//���capacityС��8(2�����η�)����ô��Ԥ�ȷ���data
    	//capacity>8ʱ������capacity >>> 3��long,һ��long�ܱ�ʾ64����
    	//����capacity>8ʱԤ�����data�ܱ�ʾ��λ����: (capacity >>> 3) *64 Լ���� (capacity/8)*64 = capacity*8
    	//����ԭ��capacity��8��(Լ����)
    	
        data = new long[capacity >>> 3];
    }

    /**
     * Get the index of the next bit that is not set.
     *
     * @param fromIndex where to start searching
     * @return the index of the next disabled bit
     */
    public int nextClearBit(int fromIndex) {
        int i = fromIndex >> ADDRESS_BITS;
        int max = data.length;
        for (; i < max; i++) {
            if (data[i] == -1) { //long������λ����1ʱ��ֵʱ-1
                continue;
            }
            int j = Math.max(fromIndex, i << ADDRESS_BITS);
            for (int end = j + 64; j < end; j++) {
                if (!get(j)) {
                    return j;
                }
            }
        }
        return max << ADDRESS_BITS;
    }

    /**
     * Get the bit at the given index.
     *
     * @param i the index
     * @return true if the bit is enabled
     */
    public boolean get(int i) {
        int addr = i >> ADDRESS_BITS;
        if (addr >= data.length) {
            return false;
        }
        return (data[addr] & getBitMask(i)) != 0;
    }

    /**
     * Get the next 8 bits at the given index.
     * The index must be a multiple of 8.
     *
     * @param i the index
     * @return the next 8 bits
     */
    public int getByte(int i) {
        int addr = i >> ADDRESS_BITS;
        if (addr >= data.length) {
            return 0;
        }
        return (int) (data[addr] >>> (i & (7 << 3)) & 255);
    }

    /**
     * Combine the next 8 bits at the given index with OR.
     * The index must be a multiple of 8.
     *
     * @param i the index
     * @param x the next 8 bits (0 - 255)
     */
    public void setByte(int i, int x) {
        int addr = i >> ADDRESS_BITS;
        checkCapacity(addr);
        data[addr] |= ((long) x) << (i & (7 << 3));
        if (maxLength < i && x != 0) {
            maxLength = i + 7;
        }
    }

    /**
     * Set bit at the given index to 'true'.
     *
     * @param i the index
     */
    public void set(int i) {
        int addr = i >> ADDRESS_BITS;
        checkCapacity(addr);
        data[addr] |= getBitMask(i);
        if (maxLength < i) {
            maxLength = i;
        }
    }

    /**
     * Set bit at the given index to 'false'.
     *
     * @param i the index
     */
    public void clear(int i) {
        int addr = i >> ADDRESS_BITS;
        if (addr >= data.length) {
            return;
        }
        data[addr] &= ~getBitMask(i);
    }
    
    //���磬���i��63�����ص�longֵ�ĵ�64(��Ϊi��0��ʼ��������i+1)λ��1��
    //���i��65����63ȡģ����1�����ص�longֵ�ĵ�2λ��1��
    private static long getBitMask(int i) {
        return 1L << (i & ADDRESS_MASK); //(i & ADDRESS_MASK)�൱�ڰ�63ȡģ(�õ���ֵ��0��63)
    }

    private void checkCapacity(int size) {
        if (size >= data.length) {
            expandCapacity(size);
        }
    }

    private void expandCapacity(int size) {
        while (size >= data.length) {
            int newSize = data.length == 0 ? 1 : data.length * 2;
            long[] d = new long[newSize];
            System.arraycopy(data, 0, d, 0, data.length);
            data = d;
        }
    }

    /**
     * Enable or disable a number of bits.
     *
     * @param fromIndex the index of the first bit to enable or disable
     * @param toIndex one plus the index of the last bit to enable or disable
     * @param value the new value
     */
    public void set(int fromIndex, int toIndex, boolean value) {
        // go backwards so that OutOfMemory happens
        // before some bytes are modified
        for (int i = toIndex - 1; i >= fromIndex; i--) {
            set(i, value);
        }
        if (value) {
            if (toIndex > maxLength) {
                maxLength = toIndex;
            }
        } else {
            if (toIndex >= maxLength) {
                maxLength = fromIndex;
            }
        }
    }

    private void set(int i, boolean value) {
        if (value) {
            set(i);
        } else {
            clear(i);
        }
    }

    /**
     * Get the index of the highest set bit plus one, or 0 if no bits are set.
     *
     * @return the length of the bit field
     */
    public int length() {
        int m = maxLength >> ADDRESS_BITS;
        while (m > 0 && data[m] == 0) {
            m--;
        }
        maxLength = (m << ADDRESS_BITS) +
            (64 - Long.numberOfLeadingZeros(data[m]));
        return maxLength;
    }

}
