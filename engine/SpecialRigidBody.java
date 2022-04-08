package engine;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

public class SpecialRigidBody extends RigidBody{

	public int bodyType;	
	public String name;
	public Object object;
	public CollisionListener cl;
	
	public SpecialRigidBody(RigidBodyConstructionInfo constructionInfo,Object object, String name, CollisionListener cl) {
		super(constructionInfo);
		this.object = object;
		this.name = name;
		this.cl = cl;
	}

}
