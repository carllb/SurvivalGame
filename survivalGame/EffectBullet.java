package survivalGame;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import engine.Effect;
import engine.Point3d;

public class EffectBullet extends Effect{

	Vector3f end;	
	
	public EffectBullet(int life, Vector3f location, Vector3f end) {
		super(life, location);
		this.end = end;
	}

	@Override
	public void render() {
		Point3d temp = super.getLocation();
		double x = temp.x, ex = end.x;
		double y = temp.y, ey = end.y;
		double z = temp.z, ez = end.z;
		GL11.glColor3f(0, 255, 0);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(x, y, z);
		GL11.glVertex3d(ex, ey, ez);
		GL11.glEnd();		
	}

}
