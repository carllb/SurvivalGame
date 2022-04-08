package engine;

import javax.swing.Timer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

public class GameManager implements Runnable {

	World world;
	public BasicGame game;
	public Timer timer;
	float gameSpeed = 0;
	public int tps = 0;
	Thread logic = new Thread();
	Thread phys = new Thread();
	public boolean grabMouse = false;
	public static int ptps = 0;
	public static boolean paused = false;


	public void startGame(BasicGame game,int gameSpeed){
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.err.println("FATAL ERROR: Could not creat AL audio device.");
			cleanupAndEndGame();			
		}
		this.game = game;
		this.game.initGameObjects();
		this.game.preInit();	
		game.rendert.start();			
		world = game.world;
		this.gameSpeed = (float) gameSpeed;
		while(!Keyboard.isCreated() || game.world.renderTickNumber < 3){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};	
		game.midInit();
		game.world.renderTickNumber = 0;
		while(game.world.renderTickNumber < 7){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};		
		game.postInit();
		logic = new Thread(this,"Logic");
		logic.start();		 
	//	phys = new Thread(physClass);
	//	phys.start();
	}



	long Stime = 0;
	long Etime = 0;
	boolean first = true;

	long Start = 0;
	long Finish = 0;
	long start = 0, finish;	

	@Override
	public void run() {	
		while(BasicGame.isRunning){
			if(Display.isCloseRequested()) cleanupAndEndGame();
			game.nonPauseTick();

			Etime = System.currentTimeMillis();
			int timeE = (int) ((Etime - Stime)/1000f);			
			float dps = 1f/gameSpeed;		
			float diff = dps-timeE;
			//System.out.println(diff*1000);
			if(diff > 0 && !first){
				try{		
					Thread.sleep((long) (diff*1000));
					//		System.out.println("Delay");
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				System.out.println("NOTDelay");
			}
			Stime = System.currentTimeMillis();
			first = false;

			Finish = System.currentTimeMillis();
			if(Finish - Start == 0){
				tps = -1;
			}else{				
				tps = (int) (1000f/(Finish - Start));
			}

			if(game.world.currentMenu != null){
				paused = true;
				Mouse.setGrabbed(false);
				continue;
			}else{
				paused = false;
				if(grabMouse && !Mouse.isGrabbed()){
					Mouse.setGrabbed(true);
				}
			}	

			if(Keyboard.isCreated()){
				game.tick();
			}
				
			finish = System.currentTimeMillis();
			float timeStep = finish-start;
			
			timeStep /= 1000f;
			game.world.physWorld.stepSimulation(timeStep,20,0.001f);
			start = System.currentTimeMillis();
			if(world != null) world.tick();

			if(!game.rendert.isAlive()){
				System.out.println("Thread render is alive: " + game.rendert.isAlive());
				cleanupAndEndGame();
			}

			Start = System.currentTimeMillis();
			//	System.out.println("tick");
		}
		while(!game.rendert.isAlive()){
			System.out.println("Thread render is alive: " + game.rendert.isAlive());
			try{
				Thread.sleep(100);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
	

	public void cleanupAndEndGame(){		
		BasicGame.isRunning = false;
		world.cleanUp();	
		AL.destroy();
		System.out.println("Ending game...");		
	}
}