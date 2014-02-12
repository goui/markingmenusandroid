package fr.enst.markingmenus.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MainGLSurfaceView extends GLSurfaceView {

	public MainGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		setRenderer(new MainRenderer());
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

}
