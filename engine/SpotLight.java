package engine;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL11;

public class SpotLight extends Light{

	float cutoff = 15;
	Vector3f dir = new Vector3f();
	
	public SpotLight(float x, float y, float z, int lightID, float cutoff) {
		super(x, y, z, lightID);
		this.cutoff = cutoff;
	}
	
	
	@Override
	public void enable() {
		super.enable();
		GL11.glLightf(lightID, GL11.GL_SPOT_CUTOFF, cutoff);
		GL11.glLight(lightID, GL11.GL_SPOT_DIRECTION, Utils.asFloatBuffer(new Vector4f(dir.x,dir.y,dir.z,0)));
		//GL11.glLightf(lightID, GL11.GL_SPOT_EXPONENT, -10);
	}


	public void setCutoff(float cutoff2) {
		cutoff = cutoff2;
		ready = false;
	}
	
	public void serDirection(Vector3f dir){
		this.dir = dir;
		ready = false;
	}

}
