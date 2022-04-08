package engine;

import java.io.Serializable;

public class Color4f implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 219837L;
	public float R,G,B,A;
	
	
	public Color4f(){
		R = G = B = A = 1;
	}
	
	public Color4f(float R, float G, float B, float A){
		this.R = R;
		this.G = G;
		this.B = B;	
		this.A = A;
	}
	
}
