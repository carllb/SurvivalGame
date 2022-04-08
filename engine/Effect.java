package engine;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;

public abstract class Effect implements VisibleObject{

	World world;
	boolean visible = true;
	Vector3f location;
	int life;
	
	
	public Effect(int life,Vector3f location){
		this.location = location;
		this.life = life;				
	}
	
	@Override
	public abstract void render();

	@Override
	public void setVisible(boolean visible) {
		this.visible =  visible;	
	}

	@Override
	public boolean getVisible() {
		return visible;
	}

	@Override
	public void addedToWorld(World w) {
		world = w;		
	}

	@Override
	public void tick() {		
		if(life <= 0){
			world.removeObject(this);
		}
		life--;
	}

	@Override
	public void move(Point3d newLocation) {
		this.location = newLocation.asVector3f();		
	}

	@Override
	public void rot(Vector3f rot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point3d getLocation() {
		return Utils.vector3fToPoint3d(location);
	}

	@Override
	public Vector3f getRot() {
		return null;
	}

	@Override
	public void cleanUp() {
		
	}

	@Override
	public void removedFromWorld() {
		
	}

	@Override
	public void addToPhysWorld() {
		
	}

	@Override
	public void addToPhysWorld(float mass) {
		
	}

	@Override
	public void addToPhysWorld(CollisionShape cs, float mass) {
		
	}

	@Override
	public void removeFromPhysWorld() {
		
	}

	@Override
	public String getName() {
		return null;
	}

}
