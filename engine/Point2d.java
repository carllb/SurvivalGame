package engine;

import java.io.Serializable;



public class Point2d implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1910023718871039074L;
	public double x,y;
	
	public Point2d(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Point2d() {
		x = 0;
		y = 0;
	}

	public double getSum(){
		return x + y;
	}
	public Point2d duplicate(){
		return new Point2d(x,y);
	}
	public double[] asDoubleArray(){
		return new double[]{x,y};
	}
	
	
}
