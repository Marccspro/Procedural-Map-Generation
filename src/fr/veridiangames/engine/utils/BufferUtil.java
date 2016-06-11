package fr.veridiangames.engine.utils;

import fr.veridiangames.engine.maths.Mat4;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtil {
	
	public static ByteBuffer toByteBuffer(FloatBuffer buffer) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.capacity() * 4);
		byteBuffer.asFloatBuffer().put(buffer);
		
		return byteBuffer;
	}
	
	public static ByteBuffer toByteBuffer(IntBuffer buffer) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.capacity() * 4);
		byteBuffer.asIntBuffer().put(buffer);
		byteBuffer.flip();
		
		return byteBuffer;
	}
	
	public static FloatBuffer createBuffer(float[] v) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(v.length);
		buffer.put(v);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer createBuffer(int[] v) {
		IntBuffer buffer = BufferUtils.createIntBuffer(v.length);
		buffer.put(v);
		buffer.flip();
		return buffer;
	}
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
	public static FloatBuffer toMatrixBuffer(Mat4 v) {
		matrixBuffer.clear();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				matrixBuffer.put(v.matrix[x][y]);
			}	
		}
		matrixBuffer.flip();
		
		return matrixBuffer;
	}
}
