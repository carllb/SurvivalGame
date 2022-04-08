package engine;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_NOTEQUAL;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glAlphaFunc;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;



public class Render implements Runnable{

	public void run(){
		startRender();
	}


	public World world;
	public DisplyListManager dlm = new DisplyListManager();
	//	public ArrayList<TerrainManager> terrainNEEDbuild = new ArrayList<TerrainManager>();
	public boolean wireFrame = false;
	public boolean fullscreen = false;
	private Color3f color = new Color3f();
	private boolean colorChange = false;
	public static int width;
	public static int height;
	public int fps = 0;
	Canvas canvas = null;
	DisplayMode dm;	

	public Render(World world,int width, int height, Canvas canvas){
		this.world = world;
		Render.width = width;
		Render.height = height;
		this.canvas = canvas;
	}
	
	public Render(World world,DisplayMode dm, Canvas canvas){
		this.dm = dm;
		width = dm.getWidth();
		height = dm.getHeight();
		this.world = world;
	}

	public void setFullScreen(boolean fscreen){
		fullscreen = fscreen;
	}

	public void startRender(){		

		world.render = this;
		//Init OpenGL
		try {			
			if(canvas != null) Display.setParent(canvas);
			if(dm != null){
				
				Display.setDisplayMode(dm);
			}else{
				Display.setDisplayMode(new DisplayMode(width, height));
			}
			Display.create();		
			glShadeModel(GL_SMOOTH);
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_ALPHA_TEST);
			glEnable(GL_BLEND);
		//	glEnable(GL_NORMALIZE);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glEnable(GL_LIGHTING);
			//	glEnable(GL_STENCIL);
			//		glEnable(GL_CULL_FACE);		

			//	glEnable(GL_COLOR_MATERIAL);
			//	glColorMaterial(GL_FRONT, GL_DIFFUSE);
			glAlphaFunc(GL_NOTEQUAL, 0);

			glClearColor(color.r,color.g,color.b, 1);
			glClearDepth(1.0);
			glEnable(GL_DEPTH_TEST);
			glDepthFunc(GL_LEQUAL);

			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();

			GLU.gluPerspective(/*Field of view*/ 90, /* Aspect ratio */((float) Display.getWidth()/(float)Display.getHeight()), 0.01f, 200000);

			glMatrixMode(GL_MODELVIEW);

			glLoadIdentity();		
			glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
			glHint(GL_POLYGON_SMOOTH_HINT,GL_NICEST);
			glHint(GL_LINE_SMOOTH_HINT,GL_NICEST);
			glHint(GL_POINT_SMOOTH_HINT,GL_NICEST);
			glLineWidth(.5f);

			//	Display.setVSyncEnabled(true);

			long starttime = System.currentTimeMillis();
			long endtime = System.currentTimeMillis();			
			//Main render loop			
			while(BasicGame.isRunning){
				if(wireFrame){
					glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
				}else{
					glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
				}

				if(colorChange){
					colorChange = false;
					glClearColor(color.r,color.g,color.b, 1);
				}
				//Display.sync(400);


				//FPS Calculating code
				endtime = System.currentTimeMillis();
				if((endtime-starttime) == 0){					
					//	System.out.println("FPS: infinity");					
				}else{					
					fps = (int) (1000f/(endtime-starttime));			
				}							
				starttime = System.currentTimeMillis();				
				//End Calculating FPS
				

				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glLoadIdentity();			
				try{
					world.render();
				}catch(Exception e){
					e.printStackTrace();
				}
				Display.update();
				
				if(Display.isFullscreen() != fullscreen){
					Display.setFullscreen(fullscreen);
				}
			}
			BasicGame.isRunning = false;

			Display.destroy();		
			try{
				Thread.sleep(20);
			}catch(Exception e){
				
			}
			System.exit(0);

		} catch (LWJGLException e) {
			System.out.println("Could Not init OpenGL! Aborting!!!");
			e.printStackTrace();
			System.exit(-1);
		}


	}
	//	private void buildTerrains(){
	//		
	//		for(int i=0;i<terrainNEEDbuild.size();i++){
	//			terrainNEEDbuild.get(i).buildList();
	//			terrainNEEDbuild.remove(terrainNEEDbuild.get(i));
	//		}

	//	}
	public void cleanup(){
		Display.destroy();	
	}

	public void setClearColor(Color3f color){
		this.color = color;
		colorChange = true;
	}

}
