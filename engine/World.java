package engine;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.dispatch.GhostPairCallback;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.extras.gimpact.GImpactCollisionAlgorithm;

import engine.menu.Menu;

public class World {
	
	
	public ArrayList<VisibleObject> visibleObjects = new ArrayList<VisibleObject>();	
	public ArrayList<Light> lights = new ArrayList<Light>();
	private ArrayList<Shader> shaders = new ArrayList<Shader>();
	public ArrayList<VisibleObject> visibleHUDObject = new ArrayList<VisibleObject>();
	ArrayList<RigidBody> rigidBodysToBeRemoved = new ArrayList<RigidBody>(); 
	HUD hud = null;
	Menu currentMenu = null;
	public DynamicsWorld physWorld;
	public CollisionConfiguration colCon;
	public CollisionDispatcher colDisp;
	public SequentialImpulseConstraintSolver sics;
	public BroadphaseInterface bI;
	public GhostPairCallback gpc;
	public Vector3f gravity = new Vector3f(0,-9.8f,0);
	public Render render;
	Vector3f worldMax, worldMin;
	public int renderTickNumber  = 0;
	public boolean ready = false;
	//public AxisSweep3 sweep3;
	
	Camera camera;	
	public World(Camera camera){
		this.camera = camera;
		setupPhysics();
		ready = true;
	}
	public void setHUD(HUD hud){
		hud.world = this;
		this.hud = hud;	
		Thread hudt = new Thread(hud);
		hudt.setName("HUD Thread");
		hudt.start();
		
	}
	
	public void setMenu(Menu m){
		currentMenu = m;
		if(m != null){
			(new Thread(m)).start();
		}
	}
	
	public void addObject(VisibleObject vo){
		visibleObjects.add(vo);		
		vo.addedToWorld(this);
	}
	
	
	public void removeObject(VisibleObject vo){		
		visibleObjects.remove(vo);			
	}
	
	public void addLight(Light l){
		lights.add(l);
	}
	
	public void removeLight(Light l) {
		lights.remove(l);
	}
	
	public void setGravity(Vector3f gravity) {
		this.gravity = gravity;
		if(physWorld != null){
			physWorld.setGravity(this.gravity);
		}
	}
	
	public void addShader(Shader s){
		shaders.add(s);
	}

	
	public void setupPhysics(){
		colCon = new DefaultCollisionConfiguration();
		colDisp = new CollisionDispatcher(colCon);
		sics = new SequentialImpulseConstraintSolver();
		bI = new DbvtBroadphase();
		GImpactCollisionAlgorithm.registerAlgorithm(colDisp);		
		physWorld = new DiscreteDynamicsWorld(colDisp,bI,sics,colCon);
//		physWorld.getPairCache().setInternalGhostPairCallback(gpc);
		physWorld.setGravity(gravity);
		physWorld.setInternalTickCallback(new ITCB(), null);
		
	//	physWorld.setDebugDrawer(new JBulletDebug());
	}
		
	public void tick(){		
		try{
			for(int i=0;i<visibleObjects.size();i++){
				visibleObjects.get(i).tick();		
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i=0;i<rigidBodysToBeRemoved.size();i++){
			physWorld.removeRigidBody(rigidBodysToBeRemoved.get(i));
			rigidBodysToBeRemoved.remove(i);
		}
	}
	public void addVisisbleHUDObject(VisibleObject v){
		visibleHUDObject.add(v);
	}
	
	public void removeVisibleHUDObject(VisibleObject v){
		visibleHUDObject.remove(v);		
	}
	
	public void removeVisibleHUDObjectByName(String name){
		for(int i=0;i<visibleHUDObject.size();i++){
			if(visibleHUDObject.get(i).getName().equals(name)){
				visibleHUDObject.remove(i);
			}
		}
	}
	
	public void removeRigidBody(RigidBody rb){
		rigidBodysToBeRemoved.add(rb);
	}
	
	public void render(){	
		GL11.glPushMatrix();
		
		if(renderTickNumber < 100);
			renderTickNumber++;
		for(Shader s : shaders){
			if(s.getLoaded()){
				if(s.getRun()){
					GL20.glUseProgram(s.getProgram());
			//		int location = glGetUniformLocation(s.programID, "my_float");
			//		glUniform1f(location, 1000);
				}
			}else{
				try {
					s.loadShaders();
				} catch (Exception e) {					
					e.printStackTrace();
					new RuntimeException("Shader Could not be compiled; Check file path.");
					System.exit(1);
				}
			}
		}
		for(int i=0;i<visibleHUDObject.size();i++){
			VisibleObject v = visibleHUDObject.get(i);
			if(v !=null && v.getVisible()){
				v.render();
			///	System.out.println(v.getName());
			}
		}
		
		GL11.glRotated(camera.rotx, 1,0, 0);		
		GL11.glRotated(camera.roty,0, 1,0);		
		GL11.glRotated(camera.rotz,0,0, 1);		
		GL11.glTranslated(camera.x, camera.y, camera.z);	
		
		for(int i = 0;i<visibleObjects.size();i++){	
			if(visibleObjects.get(i)!=null && visibleObjects.get(i).getVisible() || renderTickNumber < 5){
				visibleObjects.get(i).render();
			}
		}	
		for(int i =0;i<lights.size();i++){
			if(!lights.get(i).ready){
				lights.get(i).enable();
			}
		}
		GL11.glPopMatrix();
		
		GL11.glDisable(GL_DEPTH_TEST);
		if(hud !=null && currentMenu == null){
			hud.render();
		}
		
		if(currentMenu != null){
			currentMenu.render();
		}
		GL11.glEnable(GL_DEPTH_TEST);
	}
	public void cleanUp() {}
	
	public Menu getCurrentMenu(){
		return currentMenu;
	}
	
	public CollisionObject rayTest(Vector3f rayFromWorld, Vector3f rayToWorld){		
		ClosestRayResultCallback crrc = new ClosestRayResultCallback(rayFromWorld, rayToWorld);
		try{		
			physWorld.rayTest(rayFromWorld, rayToWorld, crrc);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return crrc.collisionObject;
	}
}
