package engine;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;

public interface VisibleObject {
	
	public void render();	
	public void setVisible(boolean visible);
	public boolean getVisible();
	public void addedToWorld(World w);
	public void tick();
	public void move(Point3d newLocation);
	public void rot(Vector3f rot);
	public Point3d getLocation();
	public Vector3f getRot();
	public void cleanUp();
	public void removedFromWorld();
	public void addToPhysWorld();
	public void addToPhysWorld(float mass);	
	public void addToPhysWorld(CollisionShape cs,float mass);
	public void removeFromPhysWorld();
	public String getName();
}
