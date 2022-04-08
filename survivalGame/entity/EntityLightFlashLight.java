package survivalGame.entity;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import survivalGame.Level;
import engine.SpotLight;

public class EntityLightFlashLight extends EntityLight{

	public EntityLightFlashLight(Level level) {
		super(level, 1);
		light = new SpotLight(0, 0, 0, 1, 15);
		light.setAmbient(new Vector4f(0.3f, 0.3f, 0.3f, 0.3f));
		light.setDiffuse(new Vector4f(1, 1, 1, 1));
	}
	
	public void setCutoff(float cutoff){
		((SpotLight) light).setCutoff(cutoff);
	}
	
	
	public void setDirection(Vector3f dir){
		((SpotLight) light).serDirection(dir);
	}

}
