package engine;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.bulletphysics.linearmath.IDebugDraw;

public class JBulletDebug extends IDebugDraw{

	@Override
	public void drawLine(Vector3f from, Vector3f to, Vector3f color) {
		try {
			if(!Display.isCurrent()) return;
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor3f(color.x, color.y, color.z);
		GL11.glVertex3f(from.x, from.y, from.z);
		GL11.glVertex3f(to.x, to.y, to.z);
		GL11.glEnd();		
	}

	@Override
	public void drawContactPoint(Vector3f PointOnB, Vector3f normalOnB,
			float distance, int lifeTime, Vector3f color) {
		try {
			if(!Display.isCurrent()) return;
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
			GL11.glBegin(GL11.GL_POINTS);
			GL11.glColor3f(color.x, color.y, color.z);
			GL11.glVertex3f(PointOnB.x, PointOnB.y, PointOnB.z);
			GL11.glEnd();			
	}

	@Override
	public void reportErrorWarning(String warningString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw3dText(Vector3f location, String textString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDebugMode(int debugMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDebugMode() {
		// TODO Auto-generated method stub
		return 2;
	}

}
