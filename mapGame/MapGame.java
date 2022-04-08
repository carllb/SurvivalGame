package mapGame;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;
import javax.vecmath.Vector3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.WaveData;

import engine.BasicGame;
import engine.GameManager;
import engine.HUD;
import engine.HUDImage;
import engine.HUDString;
import engine.Shader;
import engine.Sound;

public class MapGame extends BasicGame{

	GameManager gm;
	final File mapFile = new File("res/MapGame/MapFile4.map");
	final File crossHairFile = new File("res/MapGame/CrossHair.png");
	Map map;
	Player player;
	HUD headsUpDisplay;
	HUDString fpsDisplay = new HUDString("FPS: 0", 10, 10);
	HUDString tpsDisplay = new HUDString("TPS: 0", 340, 10);
	HUDImage crossHair = null;
//	Light l = new Light(0, 0, 0, new Color3f(0.1f,0.5f,0.2f), 0);
	Sound par = null;
	Shader s = null;
	public MapGame() {
		super((int) (640*1.1f),(int) (480*1.1f),null);
		gm = new GameManager();
		gm.startGame(this, 60);
	}	
	
	public static void main(String[] args) {
		System.out.println("test");
		new MapGame();
	}
	
	@Override
	public void preInit() {
		 try {
			crossHair = new HUDImage(ImageIO.read(crossHairFile),(int) (640f/480f)*640, 480);
			File f = new File("res/MapGame/ding.wav");
			FileInputStream fis = new FileInputStream(f);
			WaveData wd = WaveData.create(new BufferedInputStream(fis));
			if(wd == null) System.out.println("why?");
			par = new Sound(wd);			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		s = new Shader(new File("Shaders/TextureLight.vert"), new File("Shaders/TextureLight.frag"));
	//	world.addShader(s);
		par.move(new Vector3f(10,3,10));
		par.playSound(true);
//		world.addLight(l);
		headsUpDisplay = new HUD((640f/480f));
		world.setHUD(headsUpDisplay);
		headsUpDisplay.addElement(fpsDisplay);
		headsUpDisplay.addElement(tpsDisplay);
		headsUpDisplay.addElement(crossHair);
		//crossHair.setColor(new Color(255,255,255));
		fpsDisplay.setColor(new Color(0,0,0));
		tpsDisplay.setColor(new Color(0,0,0));
		ObjectInputStream bis = null;
		world.setGravity(new Vector3f(0,-9.5f,0));
		player = new Player(world,10);
		player.warp(new Vector3f(0,20,0));
		try {
			bis = new ObjectInputStream(new FileInputStream(mapFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			map = (Map) bis.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		map.loadedFromFile(world,false);
		Mouse.setGrabbed(true);		
	}
	
	
	@Override
	public void tick(){
		
		Sound.setListenerLocation(player.getLocation().asVector3f());
	//	l.ready = false;
	//	l.x = (float) -camera.x;
	//	l.y = (float) camera.y;
	//	l.z = (float) -camera.z;
		
		tpsDisplay.changeString(String.valueOf("TPS: " + gm.tps));
	//	tpsDisplay.setColor(new Color(255,0,0));
		if(Keyboard.isKeyDown(Keyboard.KEY_9)){
			Mouse.setGrabbed(false);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_8)){
			Mouse.setGrabbed(true);
		}
		fpsDisplay.changeString("FPS: "+ render.fps);
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			gm.cleanupAndEndGame();
		}
		
		camera.x = player.getLocation().x;
		camera.y = (-player.getLocation().y) - 1;
		camera.z = player.getLocation().z;
		
		camera.roty += Mouse.getDX()/5;
		camera.rotx -= Mouse.getDY()/5;

	//	camera.rotx = 90;
		player.roty = camera.roty;
		player.rotx = camera.rotx;
		player.tick();
	}
}
