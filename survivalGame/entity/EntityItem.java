package survivalGame.entity;

import survivalGame.Level;
import survivalGame.item.Item;
import engine.Model;

public class EntityItem extends Entity{
	
	public EntityItem(Level level, Item item) {
		super(item.model.duplicate(), level);		
	}

	Item item;
	public void addedToLevel(){
		Model m = (Model) rendarbleObject;
		m.addToPhysWorld(0.5f);//, new BoxShape(new Vector3f(1f,0.3f,1f)));
		m.rb.setAngularFactor(0);
	}
}
