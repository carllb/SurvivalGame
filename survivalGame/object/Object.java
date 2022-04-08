package survivalGame.object;

import javax.vecmath.Vector3f;

import survivalGame.VisibleObjectHandler;

import com.bulletphysics.collision.shapes.CollisionShape;

import engine.Utils;
import engine.VisibleObject;

public class Object {
	
	public Object(int ModelID){
		vObject = VisibleObjectHandler.getModel(ModelID); 
	}
	
	public Object(VisibleObject m){
		vObject = m;
	}
	
	public VisibleObject vObject;

	public void setLocation(Vector3f newLocation){
		vObject.move(Utils.vector3fToPoint3d(newLocation));
	}
	
	public void addToPhys(CollisionShape cs){
		 vObject.addToPhysWorld(cs,0);
	}
	
	public void addToPhys(){
		vObject.addToPhysWorld();
	}
	public void addToPhys(float mass){
		vObject.addToPhysWorld(mass);
	}

	public void tick(){}
}
