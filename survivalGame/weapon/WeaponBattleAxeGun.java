package survivalGame.weapon;

import survivalGame.Level;
import survivalGame.VisibleObjectHandler;
import survivalGame.entity.EntityProjectileBattleAxe;
import engine.Model;

public class WeaponBattleAxeGun extends WeaponRange{

	public WeaponBattleAxeGun(Level level) {
		super((Model) VisibleObjectHandler.getVisableObject("Test_Gun_01"), level);
		projectile = new EntityProjectileBattleAxe(level);
		ticksPerFire = 20;		
		name = "Debug Axe of Lag";
	}
}
