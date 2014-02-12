package fr.enst.markingmenus.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Rectangle {

	private FloatBuffer vertexBuffer;
	private ShortBuffer drawListBuffer;

	private static final int COORDS_PER_VERTEX = 3;
	private static float[] rectCoords = { -0.5f, 0.5f, 0.0f, // top left
			-0.5f, -0.5f, 0.0f, // bottom left
			0.5f, -0.5f, 0.0f, // bottom right
			0.5f, 0.5f, 0.0f // top right
	};

	private short[] drawOrder = { 0, 1, 2, 0, 2, 3 };

	public Rectangle() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(rectCoords.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());

		vertexBuffer = byteBuffer.asFloatBuffer();
		vertexBuffer.put(rectCoords);
		vertexBuffer.position(0);

		ByteBuffer dlByteBuffer = ByteBuffer.allocateDirect(drawOrder.length * 2);
		dlByteBuffer.order(ByteOrder.nativeOrder());

		drawListBuffer = dlByteBuffer.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);
	}
}
