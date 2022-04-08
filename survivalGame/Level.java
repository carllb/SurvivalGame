package survivalGame;

import java.util.ArrayList;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import survivalGame.entity.Entity;
import survivalGame.entity.EntityLight;
import survivalGame.entity.EntityLightSun;
import survivalGame.entity.EntitySpawnManager;
import survivalGame.object.Object;
import engine.Color3f;
import engine.Effect;
import engine.World;

public class Level {

	ArrayList<Entity> entitys = new ArrayList<Entity>();
	ArrayList<Object> objects = new ArrayList<Object>();
	EntityLightSun sun = new EntityLightSun(this);
	public World renderWorld;
	public EntitySpawnManager esm;
	float time = 1;
	boolean night = false;
	BaseMenu menu = null;
	public Player player;

	public Level(World world){
		renderWorld = world;
		world.addLight(sun.light);	
		sun.setLocation(new Vector3f(0,1000,0));
		sun.light.setAmbient(new Vector4f(1,1,1,1));
		esm = new EntitySpawnManager(this);
	}

	public void addEntity(Entity e){
		entitys.add(e);
		if(e instanceof EntityLight){
			renderWorld.addLight(((EntityLight)e).light);
			return;
		}
		renderWorld.addObject(e.getVisableObject());
		e.getVisableObject().setVisible(true);
		//System.out.println("skldhjaslkdjsalkdjasolkdjs");
	} 
	
	public void addEffect(Effect e){
		renderWorld.addObject(e);
	}

	public void removeEntity(Entity e){
		entitys.remove(e);
		e.getVisableObject().removeFromPhysWorld();
		if(e instanceof EntityLight){
			renderWorld.removeLight(((EntityLight)e).light);
			return;
		}
		renderWorld.removeObject(e.getVisableObject());
	}

	public void addObject(Object o){
		objects.add(o);
		renderWorld.addObject(o.vObject);
		o.vObject.setVisible(true);
	}

	public void removeObject(Object o){
		objects.remove(o);
		renderWorld.removeObject(o.vObject);
	}
	public void setMenu(BaseMenu menu){
		this.menu = menu;
		if(menu != null){
			renderWorld.setMenu(menu.menu);
		}else{
			renderWorld.setMenu(null);
		}

	}
	public void setPlayer(Player player){
		this.player = player;
	}
	final int day = 144000; //ticks  
	final int second = 100; //ticks this line might not do anything MABEY
	int elapsed = 0;
	int currentTime = day/4;	
	public void tick(){
		esm.tick();
		if(elapsed>=100){ 
			elapsed = 0;
			if(night && currentTime >= day/2){ 
				night = false;
			}else if(!night && currentTime <= 0){
				night = true;
			}
			time = (float) currentTime/((float)day/2f);
		}
		elapsed++;

		//	System.out.println(time);
		//	System.out.println(currentTime);
		if(night) currentTime += 1;
		else      currentTime -= 1;
		sun.setTime(time);
		renderWorld.render.setClearColor(new Color3f(time/2, time/2, time));
		try{
			for(int i=0;i<objects.size();i++){
				objects.get(i).tick();
			}
			for(int i=0;i<entitys.size();i++){
				entitys.get(i).tick();
			}
		}catch(Exception e){}
	}

	public void menuTick(){
		if(menu != null){
			menu.tick();
		}
	}
}
