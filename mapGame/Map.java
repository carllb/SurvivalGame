package mapGame;

import java.io.Serializable;
import java.util.ArrayList;

import engine.Model;
import engine.PhysObj;
import engine.Shape;
import engine.World;

public class Map implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 181L;
//	public Model player;
	public ArrayList<Model> mapModels = new ArrayList<Model>();
	public ArrayList<Shape> mapShapes = new ArrayList<Shape>();
	//public ArrayList<PhysObj> mapPhysShapes = new ArrayList<PhysObj>();	
	public transient World world;
	
	public Map(World world){
		this.world = world;
	}
	public void loadedFromFile(World world,boolean noPhys){
		this.world = world;
		for(Model m : mapModels){
			m.loadedFromFile();
			world.addObject(m);
		}
		for(Shape p : mapShapes){
			if(p instanceof PhysObj){
				PhysObj s = (PhysObj) p;
				s.addedToWorld(world, noPhys);
			}else{
				p.loadedFromFile(world);
			}
			world.addObject(p);
		}
		
	}
	public void addObject(Shape s){
		mapShapes.add(s);
		world.addObject(s);
	}
	public void addModel(Model m){
		mapModels.add(m);
		world.addObject(m);
	}
	public void removeObject(Shape s){
		mapShapes.remove(s);
		world.removeObject(s);
	}
	public void removeModel(Model m){
		mapModels.remove(m);
		world.removeObject(m);
	}
	
	
}
