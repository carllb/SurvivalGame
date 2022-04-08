package survivalGame.weapon;

import survivalGame.Level;
import survivalGame.VisibleObjectHandler;
import engine.Model;

public class WeaponPistol extends WeaponRange {

	public WeaponPistol(Level level) {
		super((Model)VisibleObjectHandler.getVisableObject("Weapon_Pistol_01"), level);
	//	super((Model)VisibleObjectHandler.getVisableObject("Entity_Projectile_Bullet"), level);
		name = "Pistol";
	}
	

}
