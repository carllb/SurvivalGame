package survivalGame.entity;

import javax.vecmath.Vector3f;

import survivalGame.Level;
import engine.CollisionListener;
import engine.Point3d;
import engine.Utils;
import engine.VisibleObject;

public class Entity implements CollisionListener{
	
	Vector3f location;
	VisibleObject rendarbleObject;
	Level level;
	
	public Entity(VisibleObject vo, Level level){
	//	System.out.println(vo);
		rendarbleObject = vo;
		this.level = level;
		location = new Vector3f();
	}
	
	public VisibleObject getVisableObject(){
		return rendarbleObject;
	}
	
	public void tick(){}
	
	public Vector3f getLocation(){
		return location;
	}
	
	public void setLocation(Vector3f loc){
		location = loc;
		//System.out.println(location);
		if(rendarbleObject != null)
		rendarbleObject.move(Utils.vector3fToPoint3d(loc));
	}
	public void entityCollision(Object o,Point3d loc){
		
	}

	@Override
	public void collisionOccured(Object b, Object call, Vector3f loc) {
		if(call instanceof Entity ){
			//System.out.println("sdasdasdasdasdasdasdasdsdasdas");
			//if(b instanceof SpecialRigidBody) System.out.println("Collisiodasjfgasdjfgn: "  + ", " + ((SpecialRigidBody) b ).name);
			((Entity)call).entityCollision(b, Utils.vector3fToPoint3d(loc));
		}
	}	
}
