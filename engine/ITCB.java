package engine;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.InternalTickCallback;

public class ITCB extends InternalTickCallback{

	@Override
	public void internalTick(DynamicsWorld world, float timeStep) {
		checkCollisions(world);
	}	

	public void checkCollisions(DynamicsWorld world){
		int numManifolds = world.getDispatcher().getNumManifolds();
		for(int i=0;i<numManifolds;i++){
			PersistentManifold contactManifold = world.getDispatcher().getManifoldByIndexInternal(i);
			int numContacts = contactManifold.getNumContacts();
			for(int j=0;j<numContacts;j++){
				ManifoldPoint pt = contactManifold.getContactPoint(j);
				if(pt.getDistance()<0){
					Object a = contactManifold.getBody0();
					Object b = contactManifold.getBody1();
					if(b instanceof SpecialRigidBody && a instanceof SpecialRigidBody) System.out.println("Collision: " + ((SpecialRigidBody)a).name + ", " + ((SpecialRigidBody) b ).name);
					if(a instanceof SpecialRigidBody){
						if(((SpecialRigidBody) a).cl != null){
							Vector3f loc = new Vector3f();
							pt.getPositionWorldOnB(loc);
							((SpecialRigidBody) a).cl.collisionOccured(b,((SpecialRigidBody)a).object,loc);
						}
					}
					if(b instanceof SpecialRigidBody){
						if(((SpecialRigidBody) b).cl == null) return;
						Vector3f loc = new Vector3f();
						pt.getPositionWorldOnA(loc);
						((SpecialRigidBody) b).cl.collisionOccured(a,((SpecialRigidBody)b).object,loc);
					}
				}
			}
		}
	}
}
