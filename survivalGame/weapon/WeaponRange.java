package survivalGame.weapon;


import javax.vecmath.Vector3f;

import survivalGame.Level;
import survivalGame.entity.EntityProjectile;
import survivalGame.entity.EntityProjectileBullet;
import engine.AnimationPlayer;
import engine.AnimationSet;
import engine.Frame;
import engine.Model;

public class WeaponRange extends Weapon{
	
	public WeaponRange(Model model, Level level){
		super(model,level);
		projectile = new EntityProjectileBullet(level);
	}
	
	
	public int clipSize;	
	public EntityProjectile projectile;
	AnimationSet reCoil;
	AnimationPlayer ap;
	int animationID;
	int ticksPerFire = 10;
	int deltaTicks = 0;
	//EntitySpawnManager esm;
	
	public void use(Vector3f dir, Vector3f location, Vector3f rot){
		if(!(deltaTicks>=ticksPerFire)) return;
		projectile = projectile.duplicate();
		deltaTicks = 0;
		ap.playAnimation(animationID, 100,false);	
		//dir.scale(3000);
	
		projectile.spawn(dir, location, rot);		
		
	//	CollisionObject co = level.renderWorld.rayTest(location, dir);	
	}
	
	public void creatRecoilAnimation(){
		reCoil = new AnimationSet(weaponModel, 12);
		for(int i=0;i<12;i++){
			Frame f = new Frame(weaponModel);
			f.rx += i;
			reCoil.setFrame(f, i);
		}
		/*for(int i=5;i<12;i++){
			Frame f = new Frame(weaponModel);
			f.rx -= i;
			reCoil.setFrame(f, i);
		}*/
		ap = new AnimationPlayer(reCoil);
		animationID = ap.defineAnimation(0, 12);
	}
	
	public void tick(){
		deltaTicks++;
		if(deltaTicks > 100000){
			deltaTicks = ticksPerFire + 1;
		}
	}

}
