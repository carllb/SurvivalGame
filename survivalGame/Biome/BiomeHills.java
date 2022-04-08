package survivalGame.Biome;

import java.util.Random;

import javax.vecmath.Vector3f;

import engine.Biome;
import engine.Color3f;
import engine.Model;

public class BiomeHills implements Biome {

	@Override
	public void setRandom(Random random) {}

	@Override
	public void setMag(float mag) {}

	@Override
	public float getNoise(float a, float c) {
		return 4f;
	}

	@Override
	public Color3f getColor(float a, float h, float c) {
		return null;
	}

	@Override
	public Model getModel(float a, float h, float c) {
		return null;
	}

	@Override
	public Random changeRandom(float a, float h, float c) {
		return null;
	}

	@Override
	public Vector3f changeVertex(float a, float h, float c) {
		return null;
	}

	@Override
	public int getSmoothTimes() {
		return 4;
	}

}
