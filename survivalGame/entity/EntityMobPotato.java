package survivalGame.entity;

import survivalGame.Level;
import survivalGame.MobAI;
import survivalGame.VisibleObjectHandler;

public class EntityMobPotato extends EntityMob{

	public EntityMobPotato(Level level) {
		super(VisibleObjectHandler.getVisableObject("fail"), level, new MobAI());
		
	}
}
