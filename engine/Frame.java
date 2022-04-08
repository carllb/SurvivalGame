package engine;

public class Frame {
	public double x,y,z,rx,ry,rz;
	public int amFrame;
	public boolean aModel = false;
	public AnimatedModel am;
	public Model m;
	
	public Frame(Model m){
		this.m = m;
		x = m.location.x;
		y = m.location.y;
		z = m.location.z;
		rx = m.rot.x;
		ry = m.rot.y;
		rz = m.rot.z;			
	}
	public Frame(AnimatedModel m){
		this.am = m;
		x = m.location.x;
		y = m.location.y;
		z = m.location.z;
		rx = m.rot.x;
		ry = m.rot.y;
		rz = m.rot.z;
		aModel = true;
	}
	
	
}
