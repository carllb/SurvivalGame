package survivalGame.entity;

import javax.vecmath.Vector4f;

import survivalGame.Level;


public class EntityLightSun extends EntityLight{

	
	public EntityLightSun(Level level) {
		super(level,0);
	}
	
	public void setTime(float time){
		light.setDiffuse(new Vector4f(time,time,time,time));
		light.setAmbient(new Vector4f(time,time,time,time));
	}

}
