package survivalGame.entity;

import javax.vecmath.Vector3f;

import survivalGame.Level;
import survivalGame.MobAI;
import survivalGame.item.Item;
import engine.AnimatedModel;
import engine.AnimationPlayer;
import engine.MobControler;
import engine.Point3d;
import engine.VisibleObject;

public class EntityMob extends Entity {

	
	MobControler controler;
	MobAI mobAI;
	float health;
	int maxHealth;
	int defence = 0;
	AnimationPlayer ap;
	int walkingAnimation = 0;
	
	
	public EntityMob(VisibleObject vo, Level level,MobAI mobAI) {
		super(vo, level);
		this.mobAI = mobAI;
		createWalkingAnimation();
	}
	
	public float getHealth(){
		return health;
	}
	public int getMaxHealth(){
		return maxHealth;
	}
	@Override
	public void tick() {
	//	System.out.println(controler);
		if(controler == null) return;
		if(mobAI == null) return;
		Point3d p = controler.getLocation();
		p.x*=-1;
		p.y*=-1;
		p.z*=-1;
		location = p.asVector3f();
		rendarbleObject.move(p);
		rendarbleObject.rot(controler.getRot());
		mobAI.tick();
		walk();
	}
	
	public void createWalkingAnimation(){
		if(!(rendarbleObject instanceof AnimatedModel)) return;
		AnimatedModel am = (AnimatedModel)rendarbleObject;
		ap = new AnimationPlayer(am);
		walkingAnimation = ap.defineAnimation(0, 50);
	}
	
	public void walk(){
		if(ap == null) return;
		Vector3f temp = new Vector3f();
		controler.getVelocity(temp);
		if((Math.abs(temp.x))>0.2 || (Math.abs(temp.x)>0.2)){
			ap.playAnimation(walkingAnimation, 30, true);
		}else{
			ap.stopAnimation();
		}
	}
	
	
	public void equip(Item item){
		//TODO: Write general equip code
	}
	public void kill(){
		//TODO: death animation here and despawn
		dropItem();
	}
	public void dropItem(){
		
	}
	public void damage(float damage){
		if(damage > defence){
			health -= (damage - defence);			
		}
		if(health <= 0){
			kill();
		}
	}
	@Override
	public void setLocation(Vector3f loc) {
		controler.warp(loc);
	}
	public void entityCollision(Object o, Point3d loc) {
		if(controler!= null) {
			Point3d p = controler.getLocation();
			if(p.y > 20)
				System.out.println("Impact Vel: " + p.asVector3f());
		}
	}
}
