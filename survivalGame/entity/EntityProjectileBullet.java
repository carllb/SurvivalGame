package survivalGame.entity;

import survivalGame.Level;
import survivalGame.VisibleObjectHandler;

public class EntityProjectileBullet extends EntityProjectile{

	public EntityProjectileBullet(Level level) {
		super(VisibleObjectHandler.getVisableObject("Entity_Projectile_Bullet"), level);
		//	super(VisibleObjectHandler.getVisableObject("Battle_Axe"), level);
		damage = 5;
		initVel = 500;	
	}
	@Override
	public EntityProjectile duplicate() {
		return new EntityProjectileBullet(level);
	}
}
