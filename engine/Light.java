package engine;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL11;

public class Light {
	
	public float x,y,z;
	Color3f color;
	int lightID;
	public boolean ready = false;
	Vector4f diffuse = new Vector4f();
	Vector4f ambient = new Vector4f();
	
	public Light(float x, float y, float z, int lightID){
		this.x = x;
		this.y = y;
		this.z = z;
		this.lightID = (lightID + 16384);
		if(lightID > 7 || lightID > 0) new RuntimeException("light id must be 0 - 7 inclusive");
		ready = false;
	}
	
	public void enable(){
		ready = true;
		GL11.glEnable(lightID);
		GL11.glLight(lightID, GL11.GL_POSITION, Utils.asFloatBuffer(new float[]{ x,y,z,1}));
		GL11.glLight(lightID, GL11.GL_DIFFUSE, Utils.asFloatBuffer(diffuse));
		GL11.glLight(lightID, GL11.GL_AMBIENT, Utils.asFloatBuffer(ambient));
	}
	
	public void move(Vector3f newLocation){
		this.x = newLocation.x;
		this.y = newLocation.y;
		this.z = newLocation.z;
		ready = false;
	}
	
	public void setDiffuse(Vector4f dif){
		diffuse = dif;
		ready = false;
	}
	
	public void setAmbient(Vector4f ambi){
		ambient = ambi;
		ready = false;
	}
	
	

}
