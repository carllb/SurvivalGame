package survivalGame.object;

import engine.FTree;

public class ObjectTree extends Object{

	public ObjectTree(int def, int branchPerJoint, int width,int x, int y,int z) {
		super(new FTree(def, branchPerJoint, width,x,y,z));
		
		
	}

}
