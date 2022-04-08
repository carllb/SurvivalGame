package engine;

import java.awt.Image;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class PhysObj extends Shape{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3821375280612781830L;
	transient public CollisionShape collisionShape;
	public float mass;
	public Vector3f inertia;
	transient public RigidBody  rb;
	
	public PhysObj(Point3d[] verts, Point2d[] textCoords, String name,  Image[] images,
			int vertperplane,CollisionShape colShape,float mass) {
		super(verts, textCoords, name, images, vertperplane);		
		this.collisionShape = colShape;
		this.mass = mass;
	}
	
	
	public void movePhysObj(double newX,double newY,double newZ){
		if(rb!=null){
			Transform t =new Transform();
			t.setIdentity();
			t.origin.x = (float) newX;			
			t.origin.y = (float) newY;
			t.origin.z = (float) newZ;
			rb.setWorldTransform(t);
		}
		System.out.println("hay");
		location.x = newX;
		location.y = newY;
		location.z = newZ;
	}
	
	public void moveReletive(double X,double Y,double Z){
		location.x += X;
		location.y += Y;
		location.z += Z;
		if(rb!=null){
			Transform t =new Transform();
			t.setIdentity();
			rb.getWorldTransform(t);
			t.origin.x += (float) X; 
			t.origin.y += (float) Y;
			t.origin.z += (float) Z;
			rb.setWorldTransform(t);
		}
	}

	
	public void addedToWorld(World world,boolean noPhys) {
		if(noPhys) return;
		collisionShape = new BoxShape(size.asVector3f());
		Transform t = new Transform();
		t.setIdentity();
		t.origin.set((float)location.x,(float)location.y,(float)location.z);
		inertia = new Vector3f();
		collisionShape.calculateLocalInertia(mass, inertia);
		MotionState ms = new DefaultMotionState(t);
		RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(mass, ms, collisionShape,inertia);
		RigidBody body = new RigidBody(rbci);
		world.physWorld.addRigidBody(body);
		rb = body;
	}
	
	@Override
	public void loadedFromFile(World world) {
		
	}
	
	
	@Override
	public void tick() {
		if(rb == null){
			return;
		}
		Transform t = new Transform();
		rb.getWorldTransform(t);
		location.x = -t.origin.x;
		location.y = t.origin.y;
		location.z = -t.origin.z;
		Quat4f q1 = new Quat4f();
		rb.getOrientation(q1);		
		/*
		rotX = (float) Math.atan(2*(2*out.y*out.w-2*out.x*out.z))
		rotY = (float) Math.asin(2*(out.x*out.z-out.w*out.y)) ;
		rotZ = (float) Math.atan((2*(out.x*out.w+out.y*out.z))/(1-2*(Math.pow(out.y, 2)+Math.pow(out.w,2))));
		rotate(rotX, rotY, rotZ);
		*/
		Vector3f rotE = new Vector3f();
		Utils.QuaternionToEuler(q1, rotE);
		rotate(rotE.x, rotE.y, rotE.z);
	}
}