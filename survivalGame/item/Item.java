package survivalGame.item;

import java.awt.image.BufferedImage;

import javax.vecmath.Vector3f;

import survivalGame.entity.EntityMob;
import engine.Model;

public class Item {
	

	public Model model;
	BufferedImage image;
	public String name = "";
	public int weight;
	
	public Item(BufferedImage image, Model model,int weight){
		
		this.model = model;
		this.image= image;
		this.weight = weight;
	}
	
	public void equip(EntityMob entity){
		entity.equip(this);
	}

	public void tick() {}

	public void use(Vector3f dir, Vector3f loc,
			Vector3f rot) {}
	
}
