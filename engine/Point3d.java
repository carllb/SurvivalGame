package engine;

import java.io.Serializable;

import javax.vecmath.Vector3f;

public class Point3d implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 183L;
	public double x,y,z;
	
	public Point3d(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3d(){
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public double getSum(){
		return x + y + z;
	}
	public Point3d duplicate(){
		return new Point3d(x,y,z);
	}
	public Vector3f asVector3f(){
		return new Vector3f((float)x,(float)y,(float)z);
	}
	public float[] asFloatArray(){
		return new float[]{(float)x,(float)y,(float)z};
	}
	public double[] asDoubleArray(){
		return new double[]{x,y,z};
	}
	public byte[] asByteArray(){
		return new byte[]{(byte)x,(byte)y,(byte)z};
	}
	
}
