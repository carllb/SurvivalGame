package engine;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.shapes.CollisionShape;

public class Line implements VisibleObject {

	boolean visible = true;
	Vector3f endPoint1, endPoint2;
	
	public Line(Vector3f endPoint1, Vector3f endPoint2){
		this.endPoint1 = endPoint1;
		this.endPoint2 = endPoint2;
	}
	
	@Override
	public void render() {
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(endPoint1.x, endPoint1.y, endPoint1.z);
		GL11.glVertex3f(endPoint2.x, endPoint2.y, endPoint2.z);
		GL11.glEnd();
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
		
	}

	@Override
	public boolean getVisible() {
		return visible;
	}

	@Override
	public void addedToWorld(World w) {
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void move(Point3d newLocation) {}

	@Override
	public void cleanUp() {	}

	@Override
	public void removedFromWorld() {}

	@Override
	public void addToPhysWorld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToPhysWorld(float mass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromPhysWorld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToPhysWorld(CollisionShape cs, float mass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return null;
		
	}

	@Override
	public void rot(Vector3f rot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point3d getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3f getRot() {
		// TODO Auto-generated method stub
		return null;
	}
}
