package engine;

import java.util.Random;

import javax.vecmath.Vector3f;

public interface Biome {
	public void setRandom(Random random);
	public void setMag(float mag);
	public float getNoise(float a, float c);
	public Color3f getColor(float a, float h, float c);
	public Model getModel(float a, float h, float c);
	public Random changeRandom(float a, float h, float c);
	public Vector3f changeVertex(float a, float h, float c);
	public int getSmoothTimes();
}
