package survivalGame.entity;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import survivalGame.Level;
import engine.Light;

public class EntityLight extends Entity{

	
	public Light light;
	public EntityLight( Level level, int id) {
		super(null, level);
		light = new Light(0, 0, 0, id);
		light.setDiffuse(new Vector4f(1,1,1,1));
		light.setAmbient(new Vector4f(0.1f,0.1f,0.1f,0.3f));
	}
	
	@Override
	public void setLocation(Vector3f loc) {
		light.move(loc);
		super.setLocation(loc);
	}
	
}
