package engine;

import java.io.Serializable;

public class Color3f implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 184L;
	public float r,g,b;
	
	
	public Color3f(){
		r = g = b = 0;
	}
	
	public Color3f(float R, float G, float B){
		this.r = R;
		this.g = G;
		this.b = B;	
	}
	
}
