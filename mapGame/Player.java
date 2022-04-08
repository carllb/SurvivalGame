package mapGame;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.lwjgl.input.Keyboard;

import com.bulletphysics.collision.shapes.CapsuleShape;

import engine.MobControler;
import engine.Point3d;
import engine.World;

public class Player {
	MobControler mc;
	double roty, rotx;
	double factor = Math.PI/180;
	double rotSpeed = 10;
	float jumpForce = 10;
	double dSpeed = 4;
	double speed = dSpeed;
	boolean jumping = false;
	public Player(World world,float speed){
		mc = new MobControler(new CapsuleShape(0.6f, 1),world,1);
		this.dSpeed = speed;
		speed = (float) (dSpeed/10);		
	}
	public void warp(Vector3f location){
		mc.warp(location);
	}
	
	public Point3d getLocation(){
		return mc.getLocation();
	}
	
	public void tick(){
		boolean moving = false;	
		if(!Keyboard.isCreated()) return;

		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){// && !jumping){
			jumping = true;
			mc.jump(jumpForce);			
		}else{
			if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				jumping = false;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			mc.moveXZ(new Vector2f((float)  (-Math.sin(roty*factor)*speed),(float) (Math.cos(roty*factor)*speed)));
			moving = true;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			mc.moveXZ(new Vector2f((float) (Math.sin(roty*factor)*speed),(float) (-Math.cos(roty*factor)*speed)));
			moving = true;
		}
			
		
		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
		//	camera.y -= 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_T)){
	//		camera.y += 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			double x,z;
			x = -Math.sin((roty-90)*factor)*speed;
			z = +Math.cos((roty-90)*factor)*speed;
			mc.moveXZ(new Vector2f((float)x,(float)z));
			moving = true;
		}
				
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			double x,z;
			x = +Math.sin((roty-90)*factor)*speed;
			z = -Math.cos((roty-90)*factor)*speed;
			mc.moveXZ(new Vector2f((float)x,(float)z));
			moving = true;
		}	
		if(!moving){
			mc.moveXZ(new Vector2f(0,0));
		}
	}

}
