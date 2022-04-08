package survivalGame.weapon;

import java.awt.image.BufferedImage;

import javax.vecmath.Vector3f;

import survivalGame.Level;
import engine.Model;

public class Weapon {

	public Model weaponModel;
	public BufferedImage image;
	public void use(Vector3f dir, Vector3f location, Vector3f rot){}
	public Level level;
	public String name = "";
	public void tick() {}
	public Weapon(Model model, Level level){
		this.weaponModel = model;
		this.level = level;
		model.lighting = false;
	}
}
