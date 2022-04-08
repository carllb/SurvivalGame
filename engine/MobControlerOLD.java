package engine;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.GhostPairCallback;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.character.KinematicCharacterController;
import com.bulletphysics.linearmath.Transform;

public class MobControlerOLD {
	KinematicCharacterController kcc;
	PairCachingGhostObject pcgo;
	DynamicsWorld collWorld;
	/*
	 * ___________________________________________________________
	 *| 	  												      |
	 *| THIS CLASS IS A FALIER AND DOSE NOT WORK!!!!!!!!!!!!!!    |
	 *| 														  |
	 *|___________________________________________________________| 
	 *   
	 * 
	 * 
	 * 
	 */
	
	public MobControlerOLD(ConvexShape mobShape,float stepHeight, World world){
		collWorld = world.physWorld;
		pcgo = new PairCachingGhostObject();
		kcc = new KinematicCharacterController(pcgo, mobShape, stepHeight);	
		this.collWorld = world.physWorld;
		Transform t = new Transform();
		t.setIdentity();
		t.origin.set(0, 0, 0);
		pcgo.setWorldTransform(t);		
		pcgo.setCollisionShape(mobShape);
		pcgo.setCollisionFlags(CollisionFlags.CHARACTER_OBJECT);
		BroadphaseProxy bpp = new BroadphaseProxy(mobShape,CollisionFilterGroups.CHARACTER_FILTER,(short) (CollisionFilterGroups.STATIC_FILTER|CollisionFilterGroups.DEFAULT_FILTER)); 
		pcgo.setBroadphaseHandle(bpp);
		collWorld.addCollisionObject(pcgo,CollisionFilterGroups.CHARACTER_FILTER, (short)(CollisionFilterGroups.STATIC_FILTER | CollisionFilterGroups.DEFAULT_FILTER));
		collWorld.addAction(kcc);
		world.bI.getOverlappingPairCache().setInternalGhostPairCallback(new GhostPairCallback());		
		kcc.reset();		
	}
	
	public void jump(){
		kcc.jump();		
	}
	public void update(float dTime){
		kcc.playerStep(collWorld, dTime);
	}
	
	public void setMaxJumpHeight(float jumpHeight){
		kcc.setMaxJumpHeight(jumpHeight);
	}
	
	public void moveForTime(Vector3f dirSpeed,float time){
		kcc.setVelocityForTimeInterval(dirSpeed, time);			
	}
	
	public void warp(Vector3f location){
		kcc.warp(location);
	}
	
	public void setWalkDirection(Vector3f dir){
		kcc.setWalkDirection(dir);
	}
	public void setGravity(float g){
		kcc.setGravity(g);
	}
	
	public Point3d getLocation(){
		Transform t = new Transform();
		pcgo.getWorldTransform(t);
		
		return new Point3d(t.origin.x, t.origin.y, t.origin.z);
	}

}
