package engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class MobControler implements ActionListener {
	RigidBody rigidBody;
	DynamicsWorld collWorld;
	Timer t = new Timer(500, this);
	Vector3f velocity = new Vector3f(0,0,0);
	Vector3f rot = new Vector3f();
	GhostObject go = null;
	
	public MobControler(ConvexShape mobShape, World world, float mass){
		collWorld = world.physWorld;
		Transform t = new Transform();
		t.setIdentity();
		t.origin.set(0, 0, 0);
		Vector3f inertia = new Vector3f();
		mobShape.calculateLocalInertia(mass, inertia);
		MotionState ms = new DefaultMotionState(t);
		RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(mass, ms, mobShape,inertia);
		rigidBody = new RigidBody(rbci);
		rigidBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);		
		world.physWorld.addRigidBody(rigidBody);	
		rigidBody.setSleepingThresholds(0, 0);
		rigidBody.setAngularFactor(0);
		rigidBody.setFriction(0);
		rigidBody.setDamping(.7f, 0);		
	}
	
	public MobControler(ConvexShape mobShape, World world, float mass, Object o, String name, CollisionListener cl){
		collWorld = world.physWorld;
		Transform t = new Transform();
		t.setIdentity();
		t.origin.set(0, 0, 0);
		Vector3f inertia = new Vector3f();
		mobShape.calculateLocalInertia(mass, inertia);
		MotionState ms = new DefaultMotionState(t);
		RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(mass, ms, mobShape,inertia);
		rigidBody = new SpecialRigidBody(rbci, o, name, cl);
		rigidBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);		
		world.physWorld.addRigidBody(rigidBody);	
		rigidBody.setSleepingThresholds(0, 0);
		rigidBody.setAngularFactor(0);
		rigidBody.setFriction(0);
		rigidBody.setDamping(.7f, 0);		
	}
	public void setFriction(float friction){
		rigidBody.setFriction(friction);
	}
	
	public void jump(float force){
		Vector3f vel = new Vector3f();		
		getVelocity(vel);
		if((Math.abs(vel.y) < 1) && checkCollisionWithOtherObjectAndIsFromTop()){
			Vector3f jumpForce = new Vector3f(vel.x,force,vel.z);		
			rigidBody.applyCentralImpulse(jumpForce);
		}
	}
	
	public void jumpNOCheck(float force){
		Vector3f vel = new Vector3f();		
		getVelocity(vel);
		
			Vector3f jumpForce = new Vector3f(vel.x,force,vel.z);		
			rigidBody.applyCentralImpulse(jumpForce);
		
	}
	
	
	public void stop(){
		rigidBody.getLinearVelocity(velocity);
		velocity.x = 0;
		velocity.z = 0;	
		rigidBody.setLinearVelocity(velocity);
	}
	
	public void moveXZ(Vector2f force){
		rigidBody.getLinearVelocity(velocity);
		velocity.x = -force.x;
		velocity.z = -force.y;
		rigidBody.setLinearVelocity(velocity);
	}
	
	public void warp(Vector3f location){
		Transform t = new Transform();
		t.origin.set(location);
		rigidBody.setWorldTransform(t);
	}
	
	
	public void setGravity(Vector3f grav){
		rigidBody.setGravity(grav);
	}
	
	public Vector3f getGravity(){
		Vector3f out = new Vector3f();
		rigidBody.getGravity(out);
		return out;
	}
	
	public Point3d getLocation(){
		Transform t = new Transform();
		rigidBody.getWorldTransform(t);
		return new Point3d(-t.origin.x, -t.origin.y, -t.origin.z);
	}
	
	public void getVelocity(Vector3f vOut){
		rigidBody.getLinearVelocity(vOut);
	}
	
	public void setRot(Vector3f rot){
		this.rot = rot;
	}
	
	public Vector3f getRot(){
		return rot;
	}
	public boolean checkCollisionWithOtherObject(){
		int numManifolds = collWorld.getDispatcher().getNumManifolds();
		for(int i=0;i<numManifolds;i++){
			PersistentManifold contactManifold = collWorld.getDispatcher().getManifoldByIndexInternal(i);
			if(contactManifold.getBody0() == rigidBody || contactManifold.getBody1() == rigidBody){
				int numContacts = contactManifold.getNumContacts();
				for(int j=0;j<numContacts;j++){
					ManifoldPoint pt = contactManifold.getContactPoint(j);
					if(pt.getDistance()<0){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkCollisionWithOtherObjectAndIsFromTop(){
		boolean one = false;
		int numManifolds = collWorld.getDispatcher().getNumManifolds();
		for(int i=0;i<numManifolds;i++){
			PersistentManifold contactManifold = collWorld.getDispatcher().getManifoldByIndexInternal(i);
			if(contactManifold.getBody0() == rigidBody || contactManifold.getBody1() == rigidBody){
				if(contactManifold.getBody1() == rigidBody) one = true;
				int numContacts = contactManifold.getNumContacts();
				for(int j=0;j<numContacts;j++){
					ManifoldPoint pt = contactManifold.getContactPoint(j);
					
					if(pt.getDistance()<0){
						Vector3f loc = new Vector3f();
						if(one){
							pt.getPositionWorldOnA(loc);
						}else{
							pt.getPositionWorldOnB(loc);
						}
						Transform t = new Transform();
						rigidBody.getWorldTransform(t);
						if(loc.y<t.origin.y-1f){
							return true;
						}
						return false;
					}
				}
			}
		}
		return false;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		rigidBody.setFriction(1000);	
		t.stop();
	}

}
