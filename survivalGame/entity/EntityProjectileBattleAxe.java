package survivalGame.entity;

import javax.vecmath.Vector3f;

import survivalGame.Level;
import survivalGame.VisibleObjectHandler;

import com.bulletphysics.collision.shapes.BoxShape;

import engine.Point3d;

public class EntityProjectileBattleAxe extends EntityProjectile{

	public EntityProjectileBattleAxe(Level level) {
		super(VisibleObjectHandler.getVisableObject("Battle_Axe"), level);
		damage = 5;
		initVel = 50;
		life = 1000;
	}

	@Override
	public EntityProjectile duplicate() {
		return new EntityProjectileBattleAxe(level);
	}
	
	@Override
	public void spawn(Vector3f dir, Vector3f location, Vector3f rot) {
		level.esm.spawnEntity(this, life);
		rendarbleObject.setVisible(true);
		projectileModel.addToPhysWorldSRB(new BoxShape(new Vector3f(.5f,.5f,10)),3f, rot, "Battle Axe", this);
		projectileModel.move(new Point3d((dir.x*3 + location.x),dir.y*3 + location.y,dir.z*3 + location.z));
		projectileModel.rot(0, -rot.y, 0);
		projectileModel.rb.applyForce(new Vector3f(dir.x*initVel,dir.y*initVel,dir.z*initVel), new Vector3f(0,0,0));
	}	
}
