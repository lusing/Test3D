package yunos.test.test3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class MyGLSurfView extends GLSurfaceView implements
		GLSurfaceView.Renderer {

	// Save vectors and colors
	private FloatBuffer mVertBuf, mVertColorBuf;

	private ShortBuffer mIndexBuf;

	private float[] m3DObjVert = { -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0f,
			-0.5f, -0.5f, 0f, 0.5f, 0f, };

	private float[] m3DObjVertColor = { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.5f, 1.0f, };

	private short[] m3DObjVertIndex = { 0, 2, 1, 3, 2, 0, 3, 1, 2, 1, 3, 0, };

	private float backColorR = 0.3f, backColorG = 0.2f, backColorB = 0.1f,
			backColorA = 1.0f;
	private float mfRotaAng = 0f;

	private void setup() {
		// Create OpenGL specific vertex buffer
		ByteBuffer vertBuf = ByteBuffer.allocateDirect(4 * m3DObjVert.length);
		vertBuf.order(ByteOrder.nativeOrder());
		mVertBuf = vertBuf.asFloatBuffer();

		// Create OpenGL specific color buffer
		ByteBuffer vertColorBuf = ByteBuffer
				.allocateDirect(4 * m3DObjVertColor.length);
		vertColorBuf.order(ByteOrder.nativeOrder());
		mVertColorBuf = vertColorBuf.asFloatBuffer();

		// Create OpenGL specific index buffer
		ByteBuffer indexBuf = ByteBuffer
				.allocateDirect(2 * m3DObjVertIndex.length);
		indexBuf.order(ByteOrder.nativeOrder());
		mIndexBuf = indexBuf.asShortBuffer();

		mVertBuf.put(m3DObjVert);
		mVertColorBuf.put(m3DObjVertColor);
		mIndexBuf.put(m3DObjVertIndex);

		mVertBuf.position(0);
		mVertColorBuf.position(0);
		mIndexBuf.position(0);
	}

	public MyGLSurfView(Context context) {
		super(context);

		setRenderer(this);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// Clear and fill back color, clear z-buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Set Vertex
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertBuf);

		// Set Color
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, mVertColorBuf);

		gl.glLoadIdentity();
		gl.glTranslatef(0f, 0f, -3f);

		gl.glColorPointer(4, GL10.GL_FLOAT, 0, mVertColorBuf);

		gl.glLoadIdentity();
		gl.glTranslatef(0f, 0f, -3f);

		gl.glRotatef(mfRotaAng, 0f, 1f, 0f);
		mfRotaAng += 1f;

		// Draw
		gl.glDrawElements(GL10.GL_TRIANGLES, m3DObjVertIndex.length,
				GL10.GL_UNSIGNED_SHORT, mIndexBuf);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
		Log.d("Xulun","Extensions: "+extensions);
		final float fNEAREST = 0.01f, fFAREST = 100f, fVIEW_ANGLE = 45f;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		float fViewWidth = fNEAREST
				* (float) Math.tan(Math.toRadians(fVIEW_ANGLE / 2));
		float aspectRatio = (float) width / (float) height;
		gl.glFrustumf(-fViewWidth, fViewWidth, -fViewWidth / aspectRatio,
				fViewWidth / aspectRatio, fNEAREST, fFAREST);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		gl.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//Set background color
		gl.glClearColor(backColorR, backColorG, backColorB, backColorA);
		
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		setup();
	}

}
