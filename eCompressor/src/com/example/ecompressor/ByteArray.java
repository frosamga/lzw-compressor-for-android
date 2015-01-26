package com.example.ecompressor;



import java.util.Arrays;

//esta clase la usamos para manejar los bytes

public class ByteArray {

	private byte[] array;

	public ByteArray() {
		this.array = new byte[0];
	}

	public ByteArray(ByteArray nuevo) {
		this.array = ((byte[]) nuevo.array.clone());
	}


	public ByteArray(byte b1) {
		this.array = new byte[1];
		this.array[0] = b1;
	}

	public int size() {
		return this.array.length;
	}

	public byte get(int index) {
		return this.array[index];
	}

	public void set(int index, byte value) {
		this.array[index] = value;
	}

	public ByteArray append(ByteArray another) {
		int size = size();
		int anotherSize = another.size();

		int newSize = size + anotherSize;
		byte[] newBuf = new byte[newSize];
		for (int i = 0; i < size; i++) {
			newBuf[i] = get(i);
		}
		for (int i = 0; i < anotherSize; i++) {
			newBuf[(i + size)] = another.get(i);
		}
		this.array = newBuf;
		return this;
	}

	public ByteArray append(byte b1) {
		return append(new ByteArray(b1));
	}

	public boolean equals(Object obj) {
		boolean ret = true;
		if (this.array == obj) {
			ret = true;
		}
		if (obj == null) {
			ret = false;
		}
		ByteArray other = (ByteArray) obj;
		if (!Arrays.equals(this.array, other.array)) {
			ret = false;
		}
		return ret;
	}

	public int hashCode() {
		int prime = 31;
		int res = 1;
		res = 31 * res + Arrays.hashCode(this.array);
		return res;
	}

	@Override
	public String toString() {
		return "ByteArray [array=" + Arrays.toString(array) + "]";
	}

	
}
