package engine;


import java.awt.Canvas;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.DisplayMode;

public class BasicGame {

	public static boolean isRunning = false;
	
	public Render render;
	
	public Camera camera;
	public World world;
	public static BasicGame game;
	public Thread rendert;
	int w;
	int h;
	DisplayMode dm;
	Vector3f worldMax, worldMin;
	Canvas canvas;
	
	public BasicGame(int width, int height,Canvas canvas){
		game = this;
		w = width;
		h = height;
		this.canvas = canvas;
	}
	public BasicGame(DisplayMode dm,Canvas canvas){
		this.dm = dm;
		this.canvas = canvas;
		game = this;
	}
	public void initGameObjects(){
		camera = new Camera();
		world = new World(camera);
		world.setupPhysics();
		if(dm != null){
			render = new Render(world,dm,canvas);
		}else{
			render = new Render(world,w,h,canvas);
		}
		rendert = new Thread(render,"Render");
		isRunning = true;			
	}
	
	public void preInit(){}

	public void midInit(){}
	
	public void postInit(){}

	public void tick(){}
	
	public void nonPauseTick(){}
	
	public void setGravity(Vector3f gravity){
		world.setGravity(gravity);
	}
}
