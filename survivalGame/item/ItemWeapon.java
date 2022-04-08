package survivalGame.item;

import javax.vecmath.Vector3f;

import survivalGame.weapon.Weapon;


public class ItemWeapon extends Item{

	public ItemWeapon(Weapon w,int weight) {
		super(w.image,w.weaponModel, weight);
		weapon = w;
		name = w.name;
	}
	public Weapon weapon;
	
	@Override
	public void tick() {
		weapon.tick();
	}
	@Override
	public void use(Vector3f dir, Vector3f loc,
			Vector3f rot) {
		weapon.use(dir, loc, rot);
	}	
}
