package engine;

public class Vector3d {
	
	double x, y, z;
	
	public Vector3d(double x,double y,double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public Vector3d cross(Vector3d v){		
		double cx = 0,cy = 0,cz = 0;		
		cx = (y*v.z - z*v.y);		
		cy = (z*v.x - x*v.z);		
		cx = (x*v.y - y*v.x);		
		return (new Vector3d(cx, cy, cz));
	}
	

}
