package survivalGame.weapon;

import survivalGame.Level;
import survivalGame.VisibleObjectHandler;
import engine.Model;

public class WeaponAK47 extends WeaponRange{

	public WeaponAK47( Level level) {
		super((Model) VisibleObjectHandler.getVisableObject("Weapon_AK47"),level);
		name = "AK47";
	}                                                                                               

}
