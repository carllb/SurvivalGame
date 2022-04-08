package survivalGame;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import survivalGame.entity.EntityLightFlashLight;
import survivalGame.item.Item;
import survivalGame.item.ItemWeapon;
import survivalGame.weapon.WeaponRange;

import com.bulletphysics.collision.shapes.CapsuleShape;

import engine.Camera;
import engine.MobControler;
import engine.Model;
import engine.Point3d;
import engine.Utils;
import engine.World;

public class Player {
	Item item;
	MobControler mobControler;
	Model wModel = null;
	public Inventory inventory;
	World world;
	Level level;
	double speed;
	double oSpeed;
	double runFactor;
	boolean waitRun = false;
	float jumpForce;
	public int maxWeight = 1000;
	public int stamina = 1000000;
	public int health = 100;
	EntityLightFlashLight flashLight;
	Camera camera;
	PlayerHUD hud;

	public Player(Level level, double speed, double runFactor, float jumpForce,Camera camera){
		world = level.renderWorld;
		this.level = level;
		oSpeed = speed;
		this.runFactor = runFactor;
		this.jumpForce = jumpForce;
		mobControler = new MobControler(new CapsuleShape(1,3f),world,20);
		mobControler.warp(new Vector3f(0,0,0));
		inventory = new Inventory(maxWeight);
		flashLight = new EntityLightFlashLight(level);		
		level.addEntity(flashLight);
		this.camera = camera;
		level.setPlayer(this);
		hud = new PlayerHUD(this,640/480);
	}

	float roty;
	float rotx;
	boolean gPressed = false;

	public void tick(){		
		camera.rotx -= ((double) Mouse.getDY())/4;
		camera.roty += ((double) Mouse.getDX())/4;		
		boolean moving = false;
		if(!(camera.rotx<-89 || camera.rotx > 89)){
			rotx = (float) camera.rotx;
		}else{
			camera.rotx = rotx;
		}
		roty = (float) camera.roty;
		speed = oSpeed;
		Vector3f loc = camera.locAsVecotr3f();
		//		loc.negate();
		flashLight.setLocation(loc);		
		flashLight.setDirection(camera.camRotAsTrueVector());
		if(stamina < 1000 && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			stamina = 0;
			waitRun = true;
		}else if (stamina < 1000){
			waitRun = true;
		}else{
			waitRun = false;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !waitRun) {
			speed *=runFactor;
			stamina -= 1000;
		}		

		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			mobControler.jump(jumpForce);
		}		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			mobControler.moveXZ(new Vector2f((float)  (-Math.sin(roty*Utils.FACTOR_DEG_TO_RAD)*speed),(float) (Math.cos(roty*Utils.FACTOR_DEG_TO_RAD)*speed)));
			moving = true;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			mobControler.moveXZ(new Vector2f((float) (Math.sin(roty*Utils.FACTOR_DEG_TO_RAD)*speed),(float) (-Math.cos(roty*Utils.FACTOR_DEG_TO_RAD)*speed)));
			moving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
			stamina = 1000000;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			double x,z;
			x = -Math.sin((roty-90)*Utils.FACTOR_DEG_TO_RAD)*speed;
			z = +Math.cos((roty-90)*Utils.FACTOR_DEG_TO_RAD)*speed;
			mobControler.moveXZ(new Vector2f((float)x,(float)z));
			moving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			double x,z;
			x = +Math.sin((roty-90)*Utils.FACTOR_DEG_TO_RAD)*speed;
			z = -Math.cos((roty-90)*Utils.FACTOR_DEG_TO_RAD)*speed;
			mobControler.moveXZ(new Vector2f((float)x,(float)z));
			moving = true;
		}	
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			//	Vector3f newLoc = mobControler.getLocation().asVector3f();
			//	newLoc.y += 10;
			//	mobControler.warp(newLoc);
			mobControler.jumpNOCheck(100);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			//	Vector3f newLoc = mobControler.getLocation().asVector3f();
			//	newLoc.y += 10;
			//	mobControler.warp(newLoc);
			mobControler.jumpNOCheck(-100);
		}
		if(Mouse.isButtonDown(0)) {
			if(item != null) {
				//		System.out.println(rotx);
				item.use(camera.camRotAsTrueVector(), camera.locAsVecotr3f(), camera.rotAsVecotr3f());
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_G)){
			if(!gPressed){
				gPressed = true;
				if(Math.abs(mobControler.getGravity().y)>0){
					mobControler.setGravity(new Vector3f());
				}else{
					Vector3f out = new Vector3f();
					world.physWorld.getGravity(out);
					mobControler.setGravity(out);
				}
			}
		}else{
			gPressed = false;
		}

		if(!moving){
			mobControler.moveXZ(new Vector2f(0,0));
		}


		Point3d mLoc = mobControler.getLocation();
		camera.x = mLoc.x;
		camera.y = mLoc.y-1;
		camera.z = mLoc.z;
		hud.tick();
		//System.out.println(mobControler.checkCollisionWithOtherObject());
		if(item != null){
			item.tick();
		}
		if(stamina < 1000000){
			stamina += 100;
		}else{
			stamina -= 1;
		}
		if(health < 1000){
			health += 1;
		}else{
			health -=1;
		}
	}


	public void equip(Item item){
		if(this.item != null) world.removeVisibleHUDObject(this.item.model);
		this.item = item;
		wModel = item.model;
		world.addVisisbleHUDObject(wModel);
		wModel.setVisable(true);
		wModel.move(new Point3d(0.2f,-0.5f,0.1f));
		wModel.rot(0, 0, 0);
		if(item instanceof ItemWeapon){
			if(((ItemWeapon) item).weapon instanceof WeaponRange){
				((WeaponRange)((ItemWeapon) item).weapon).creatRecoilAnimation();
			}
		}
	}
	public Point3d getLocation(){
		return mobControler.getLocation().duplicate();
	}
	public void setLocation(Point3d newLoc){
		mobControler.warp(newLoc.asVector3f());
	}
}
