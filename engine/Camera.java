package engine;

import javax.vecmath.Vector3f;

public class Camera {
	
	public double x = 0,y = 0,z = 0, rotx = 0, roty = 0, rotz = 0;
	
	public Vector3f locAsVecotr3f(){
		return new Vector3f((float) -x,(float) -y,(float) -z);
	}
	
	public Vector3f rotAsVecotr3f(){
		return new Vector3f((float) rotx,(float) roty,(float) rotz);
	}
	
	
	public Vector3f camRotAsTrueVector(){
		Vector3f out = new Vector3f();
		out.x = (float) Math.sin(roty*Utils.FACTOR_DEG_TO_RAD);
		out.y = (float) Math.tan(-rotx*Utils.FACTOR_DEG_TO_RAD);
		out.z = (float) -Math.cos(roty*Utils.FACTOR_DEG_TO_RAD);
		return out;
	}
}

