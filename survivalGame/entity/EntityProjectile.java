package survivalGame.entity;

import javax.vecmath.Vector3f;

import survivalGame.EffectBullet;
import survivalGame.Level;

import com.bulletphysics.collision.shapes.SphereShape;

import engine.Model;
import engine.Point3d;
import engine.SpecialRigidBody;
import engine.VisibleObject;

public class EntityProjectile extends Entity {



	public EntityProjectile(VisibleObject vo,Level level){
		super(vo, level);
		projectileModel = ((Model) vo);
		rendarbleObject = projectileModel;

	}

	Model projectileModel;
	Point3d location;
	int damage = 0;
	int initVel = 0;	
	int life = 400;


	public int getDamege(){
		return damage;
	}

	public int getInitVel(){
		return initVel;
	}
	@Override
	public void entityCollision(Object o, Point3d loc) {
		if(o instanceof SpecialRigidBody){
			Object obj = ((SpecialRigidBody)o).object;
			if(obj instanceof EntityMob){
				EntityMob mob = (EntityMob) obj;
				mob.damage(damage);
			}
		}
		level.removeEntity(this);
	}

	public void spawn(Vector3f dir, Vector3f location, Vector3f rot){
		level.esm.spawnEntity(this, life);
		rendarbleObject.setVisible(true);
		projectileModel.addToPhysWorldSRB(new SphereShape(0.3f),0.02f,this,"Projectile",this);
		setLocation(new Point3d((dir.x*1.5 + location.x),dir.y*1.5 + location.y,dir.z*1.5 + location.z).asVector3f());
		projectileModel.rot(0, -rot.y, 0);
		projectileModel.rb.setGravity(new Vector3f(0,-3,0));
		projectileModel.rb.setFriction(1000000);
		projectileModel.rb.applyCentralForce(new Vector3f(dir.x*initVel,dir.y*initVel,dir.z*initVel));//, new Vector3f(0,0,0));
	}
	public EntityProjectile duplicate(){
		return new EntityProjectile(rendarbleObject, level);
	}
	Vector3f temp = null;
	int ticks = 0;
	@Override
	public void tick() {
		
		//System.out.println("asdksjadlksajdlk" + location );
		
		if(ticks > 10){		
			level.addEffect(new EffectBullet(100, location.asVector3f(), temp));
			temp = location.asVector3f();
			ticks = 0;
		}else{
			ticks++;
		}
	}
}
