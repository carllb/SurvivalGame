package engine;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.vehicle.DefaultVehicleRaycaster;
import com.bulletphysics.dynamics.vehicle.RaycastVehicle;
import com.bulletphysics.dynamics.vehicle.VehicleRaycaster;
import com.bulletphysics.dynamics.vehicle.VehicleTuning;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class Vehicle {
	
	VisibleObject body;
	VisibleObject[] wheels;
	CollisionShape bodyShape;
	CollisionShape[] wheelShapes;
	RaycastVehicle rcv;
	RigidBody rigidBody;
	VehicleTuning vt = new VehicleTuning();
	Vector3f location = new Vector3f(0,0,0);
	/** Constructor for A vehicle
	 * Make sure that the visible objects are not already in the world or phys world
	 */	
	public Vehicle(VisibleObject body,CollisionShape bodyShape,VisibleObject[] wheels,CollisionShape[] wheelShapes,World world,float mass){
		this.body = body;
		this.wheels = wheels;
		this.bodyShape = bodyShape;
		this.wheelShapes = wheelShapes;
		VehicleRaycaster vrc = new DefaultVehicleRaycaster(world.physWorld);
		Transform t = new Transform();
		t.origin.set(location);
		MotionState ms = new DefaultMotionState(t);
		Vector3f inertia = new Vector3f();
		bodyShape.calculateLocalInertia(mass, inertia);
		RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(mass, ms, bodyShape,inertia);
		RigidBody rb = new RigidBody(rbci);
		rigidBody = rb;
		rcv = new RaycastVehicle(vt, rb, vrc);
	}
	/**
	 * Call this method for each wheel that is in the wheel array in.
	 * @param connectionPointCS
	 * @param wheelDir
	 * @param AxleCS
	 * @param radius
	 * @param isFrontWheel
	 * @param suspension
	 */
	public void placeWheel(Vector3f connectionPointCS,Vector3f wheelDir,Vector3f AxleCS,float radius,boolean isFrontWheel,float suspension){
		rcv.addWheel(connectionPointCS, wheelDir, AxleCS, suspension, radius, vt, isFrontWheel);
	}	
	public void render(){
		//Render Body
		body.render();
		
		//Render wheels
		for(int i=0;i<wheels.length;i++){
			wheels[i].render();
		}
	}
	public void tick(){
		Transform t = new Transform();
		rigidBody.getWorldTransform(t);
		body.move(Utils.vector3fToPoint3d(t.origin));		
	}
	public void drive(float steering, float engienForce){
		
	}

}
