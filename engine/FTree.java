package engine;

import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;

public class FTree implements VisibleObject{
	ArrayList<Line> tree;
	
	public FTree(int def,int branches,int width, float x, float y, float z) {
		//System.out.println("j");
		tree=Gentree(def,branches,width,x,y,z);
		
		//System.out.println(width);
	}
	
	private ArrayList<Line> Gentree(int num, int branch, int width,float xp, float yp,float zp){
		ArrayList<Line> tree = new ArrayList<Line>();
		ArrayList<Vector3f> temptop = new ArrayList<Vector3f>();
		ArrayList<Vector3f> newtop = new ArrayList<Vector3f>();
		temptop.add(new Vector3f(0,0,0));
		for(int a = num;a>=1;a/=2){
			for(int b=0;b<temptop.size();b++){				
				Vector3f joint =temptop.get(b);
				joint.x+=xp;
				joint.y+=yp;
				joint.z+=zp;
				for(int s=0;s<branch;s++){
				Random e= new Random();
				float r =(e.nextFloat())*width;
				float m =(e.nextFloat())*width;
				float p =(e.nextFloat())*a;
				//System.out.println("r"+r);
				float rx = (float) (r-(float)width/2f);
				float rz = (float) (m-(float)width/2f);
				float ry = (float) (p-(float)width/2f);
				Vector3f delta =new Vector3f(joint.x+rx,(joint.y+a+p-a/2),joint.z+rz);
				newtop.add(delta);
				tree.add(new Line(joint, delta));
				}
				
			}
			for (int i = 0; i < newtop.size(); i++) {
				temptop.add(newtop.get(i));
			}
			
			newtop.clear();
			
		}
		
		return tree;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		for(int a=0;a<tree.size();a++){
			tree.get(a).render();
			//System.out.println("hh "+tree.get(a).endPoint1.y);
		}
		
		
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getVisible() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void addedToWorld(World w) {
		
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move(Point3d newLocation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removedFromWorld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToPhysWorld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToPhysWorld(float mass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToPhysWorld(CollisionShape cs, float mass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromPhysWorld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void rot(Vector3f rot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point3d getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3f getRot() {
		// TODO Auto-generated method stub
		return null;
	}
}
