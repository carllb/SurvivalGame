package survivalGame;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import engine.MobControler;
import engine.Point3d;

public class MobAI {

	Point3d currentTarget;
	MobControler controler;
	Vector3f rot = new Vector3f();
	Level level;
	float speed;
	
	public void setControler(MobControler mc, Level level,float speed){
		controler = mc;
	//	controler.setGravity(new Vector3f(0,-98f,0));
		mc.setRot(rot);
		System.out.println("l:" + mc);
		this.level = level;
		this.speed = speed;
	}
	
	public void tick(){
		if(currentTarget == null){
			findTarget();			
		}
		double x = (level.player.getLocation().x-controler.getLocation().x);
		double z = (level.player.getLocation().z-controler.getLocation().z);
		double a = Math.atan2(z, x);
		controler.moveXZ( new Vector2f((float)Math.cos(a)*speed,(float)Math.sin(a)*speed));
	}
	
	public void findTarget(){
		currentTarget = level.player.getLocation();
	}
}
