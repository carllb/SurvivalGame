package engine;

import static org.lwjgl.opengl.GL11.GL_DECAL;
import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.collision.shapes.TriangleShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;

public class Model implements VisibleObject, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 180L;
	public ArrayList<Point3d> vertices = new ArrayList<Point3d>();
	public ArrayList<Point2d> texVerts = new ArrayList<Point2d>();
	public ArrayList<Point3d> normals = new ArrayList<Point3d>();
	public ArrayList<Face> faces = new ArrayList<Face>();
	boolean visable = true;
	transient public int vertexBufferID = 0;
	transient public int normalBufferID = 0;
	transient public int textureCoordBufferID = 0;
	transient public int texID = 0;
	transient IntBuffer textureBuffer = null;
	public transient RigidBody rb = null;
	transient public CollisionShape collisonShape;
	public float mass = 0;
	public transient World world;
	public boolean built = false;
	public Color3f color = new Color3f();
	public Point3d location = new Point3d(0, 0, 0);
	public Vector3f rot = new Vector3f(0, 0, 0);	
	transient public BufferedImage image = null; 
	public boolean dynamic = false;
	int id = (int) (Math.random()*100);
	transient boolean builtTexture = false;
	int[] imageData;	
	int width;
	int height;
	transient boolean readyToWrite = false;
	public String name = null;
	public boolean lighting = true;

	public void setVisable(boolean visable){
		this.visable = visable;
	}
	public void phys(){}

	public void setColor(Color3f color){
		this.color = color;		
	}
	public CollisionShape getCollisonShape(){
		return collisonShape;
	}
	public void loadedFromFile(){
		textureBuffer = ByteBuffer.allocateDirect(width*height*12).asIntBuffer();
		textureBuffer.put(imageData);
		textureBuffer.rewind();	
		built = false;
		builtTexture = false;
		System.out.println("Width: " + width + "Height: " + height);
	//	for(int i=0;i<textureBuffer.capacity();i++){
	//		System.out.println(textureBuffer.get(i));
	//	}
	}
	public void imageToArray(){
		width = image.getWidth();
		height = image.getHeight();
		imageData = new int[textureBuffer.capacity()];		
		for(int i=0;i<textureBuffer.capacity();i++){
			imageData[i] = textureBuffer.get(i);
		}
		System.out.println("imageReady");
		readyToWrite = true;
		
	}
	@Override
	public void removeFromPhysWorld(){
		if(rb!=null)
		world.removeRigidBody(rb);
	}
	public SpecialRigidBody addToPhysWorldSRB(CollisionShape cs,float mass,Object object, String name, CollisionListener cl){
		if(mass == 0){
			this.mass = mass;
			collisonShape = cs;
			dynamic = false;
			Transform t = new Transform();
			t.setIdentity();
			t.origin.set((float)location.x,(float)location.y,(float)location.z);
			MotionState ms = new DefaultMotionState(t);
			RigidBodyConstructionInfo rbci = null;	
			rbci = new RigidBodyConstructionInfo(mass, ms, cs);		
			rb = new SpecialRigidBody(rbci, object, name, cl);
			if(world == null) new RuntimeException("Add to render world first befor adding to phys world");		
			world.physWorld.addRigidBody(rb);
		}else{	
			this.mass = mass;
			collisonShape = cs;
			dynamic = true;
			Transform t2 = new Transform();
			t2.setIdentity();
			t2.origin.set(0,0,0);			
			Vector3f inertia = new Vector3f();
			cs.calculateLocalInertia(mass, inertia);
			MotionState ms = new DefaultMotionState(t2);
			RigidBodyConstructionInfo rbci = null;	
			rbci = new RigidBodyConstructionInfo(mass, ms, cs,inertia);		
			rb = new SpecialRigidBody(rbci, object, name, cl);
			if(world == null) new RuntimeException("Add to render world first befor adding to phys world");		
			world.physWorld.addRigidBody(rb);
		}
		return (SpecialRigidBody) rb;
	}

	
	public void addToPhysWorld(CollisionShape cs,float mass){
		if(mass == 0){
			this.mass = mass;
			collisonShape = cs;
			dynamic = false;
			Transform t = new Transform();
			t.setIdentity();
			t.origin.set((float)location.x,(float)location.y,(float)location.z);
			MotionState ms = new DefaultMotionState(t);
			RigidBodyConstructionInfo rbci = null;	
			rbci = new RigidBodyConstructionInfo(mass, ms, cs);		
			rb = new RigidBody(rbci);
			if(world == null) new RuntimeException("Add to render world first befor adding to phys world");		
			world.physWorld.addRigidBody(rb);
		}else{	
			this.mass = mass;
			collisonShape = cs;
			dynamic = true;
			Transform t2 = new Transform();
			t2.setIdentity();
			t2.origin.set(0,0,0);			
			Vector3f inertia = new Vector3f();
			cs.calculateLocalInertia(mass, inertia);
			MotionState ms = new DefaultMotionState(t2);
			RigidBodyConstructionInfo rbci = null;	
			rbci = new RigidBodyConstructionInfo(mass, ms, cs,inertia);		
			rb = new RigidBody(rbci);
			if(world == null) new RuntimeException("Add to render world first befor adding to phys world");		
			world.physWorld.addRigidBody(rb);
		}
	}

	public void addToPhysWorld(){		
		this.mass = 0;
		dynamic = false;
		ByteBuffer verts = ByteBuffer.allocateDirect(faces.size()*3*4);
		verts.order(ByteOrder.nativeOrder());
		ByteBuffer indes = ByteBuffer.allocateDirect(faces.size()*3*4*3);
		indes.order(ByteOrder.nativeOrder());
		verts.clear();
		indes.clear();	    
		for(Point3d p: vertices){
			verts.putFloat((float) p.x);	
			verts.putFloat((float) p.y);	
			verts.putFloat((float) p.z);	    	
		}	   
		for(Face f: faces){		
			indes.putInt((int) f.vertex.x-1);
			indes.putInt((int) f.vertex.x-1);
			indes.putInt((int) f.vertex.x-1);

			indes.putInt((int) f.vertex.y-1);
			indes.putInt((int) f.vertex.y-1);
			indes.putInt((int) f.vertex.y-1);

			indes.putInt((int) f.vertex.z-1);
			indes.putInt((int) f.vertex.z-1);
			indes.putInt((int) f.vertex.z-1);
		}
	//	for(int i=0;i<indes.capacity();i+=4){
	//		System.out.println(indes.getInt(i));
	//	}
		verts.flip();
		indes.flip();		
		TriangleIndexVertexArray smi = new TriangleIndexVertexArray(faces.size(),indes,3*4*3,faces.size()*3,verts,3*4);		
		BvhTriangleMeshShape cs = new BvhTriangleMeshShape(smi,true);	

		Transform t = new Transform();
		t.setIdentity();
		t.origin.set((float)location.x,(float)location.y,(float)location.z);
		MotionState ms = new DefaultMotionState(t);
		RigidBodyConstructionInfo rbci = null;	
		collisonShape = cs;
		rbci = new RigidBodyConstructionInfo(mass, ms, cs);		
		rb = new RigidBody(rbci);
		if(world == null) new RuntimeException("Add to render world first befor adding to phys world");		
		world.physWorld.addRigidBody(rb);
	}
	public void addToPhysWorldBVH(float mass){		
		this.mass = mass;
		dynamic = true;
		ByteBuffer verts = ByteBuffer.allocateDirect(faces.size()*3*4);
		verts.order(ByteOrder.nativeOrder());
		ByteBuffer indes = ByteBuffer.allocateDirect(faces.size()*3*4*3);
		indes.order(ByteOrder.nativeOrder());
		verts.clear();
		indes.clear();	    
		for(Point3d p: vertices){
			verts.putFloat((float) p.x);	
			verts.putFloat((float) p.y);	
			verts.putFloat((float) p.z);	    	
		}	   
		for(Face f: faces){		
			indes.putInt((int) f.vertex.x-1);
			indes.putInt((int) f.vertex.x-1);
			indes.putInt((int) f.vertex.x-1);

			indes.putInt((int) f.vertex.y-1);
			indes.putInt((int) f.vertex.y-1);
			indes.putInt((int) f.vertex.y-1);

			indes.putInt((int) f.vertex.z-1);
			indes.putInt((int) f.vertex.z-1);
			indes.putInt((int) f.vertex.z-1);
		}
	//	for(int i=0;i<indes.capacity();i+=4){
	//		System.out.println(indes.getInt(i));
	//	}
		verts.flip();
		indes.flip();		
		TriangleIndexVertexArray smi = new TriangleIndexVertexArray(faces.size(),indes,3*4*3,faces.size()*3,verts,3*4);		
		BvhTriangleMeshShape cs = new BvhTriangleMeshShape(smi,true);	

		Transform t = new Transform();
		t.setIdentity();
		t.origin.set((float)location.x,(float)location.y,(float)location.z);
		MotionState ms = new DefaultMotionState(t);
		RigidBodyConstructionInfo rbci = null;	
		collisonShape = cs;
		rbci = new RigidBodyConstructionInfo(mass, ms, cs);		
		rb = new RigidBody(rbci);
		if(world == null) new RuntimeException("Add to render world first befor adding to phys world");		
		world.physWorld.addRigidBody(rb);

	}
	public void addToPhysWorld(float mass){
		this.mass = mass;
		dynamic = false;
		Transform t2 = new Transform();
		t2.setIdentity();
		t2.origin.set((float)location.x,(float)location.y,(float)location.z);
		Transform t3 = new Transform();
		t3.setIdentity();
		t3.origin.set(0,0,0);
		CompoundShape cs = new CompoundShape();
		for(Face f: faces){
			CollisionShape tri = new TriangleShape((vertices.get((int) f.vertex.x -1)).asVector3f(),(vertices.get((int) f.vertex.y -1)).asVector3f(),(vertices.get((int) f.vertex.z -1)).asVector3f());
			cs.addChildShape(t3, tri);			
		}
		Vector3f inertia = new Vector3f();
		cs.calculateLocalInertia(mass, inertia);
		MotionState ms = new DefaultMotionState(t2);
		RigidBodyConstructionInfo rbci = null;	
		collisonShape = cs;
		rbci = new RigidBodyConstructionInfo(mass, ms, cs,inertia);		
		rb = new RigidBody(rbci);
		if(world == null) new RuntimeException("Add to render world first befor adding to phys world");		
		world.physWorld.addRigidBody(rb);
	}

	public void move(Point3d newLocation){
		double newX = newLocation.x;
		double newY = newLocation.y;
		double newZ = newLocation.z;
		if(rb !=null){
			Transform t = new Transform();
			t.origin.x = (float) newX;
			t.origin.y = (float) newY;
			t.origin.z = (float) newZ;
			rb.setWorldTransform(t);
		}else{
			location.x = newX;
			location.y = newY;
			location.z = newZ;
		}
	}
	public boolean readyToWrite(){
		return readyToWrite;
	}

	public void rot(double newRX, double newRY, double newRZ){	
		if(rb !=null){
			Quat4f r = new Quat4f();
			rb.getOrientation(r);
			QuaternionUtil.setEuler(r, (float) newRY, (float) newRX, (float) newRZ);
			Transform t = new Transform();
			rb.getWorldTransform(t);
			t.setRotation(r);
			rb.setWorldTransform(t);
		}
		rot.x =(float) newRX;
		rot.y =(float) newRY;
		rot.z =(float) newRZ;
	}

	void loadTexture(){
		
		if(textureBuffer != null){			
			texID = GL11.glGenTextures();		
			glBindTexture(GL_TEXTURE_2D, texID);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);		
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE,GL11.GL_MODULATE);		
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);		
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);					
			glTexImage2D (GL_TEXTURE_2D, 0, GL_RGB, width,height,0,GL_RGB,GL_UNSIGNED_INT,textureBuffer);	
			glBindTexture(GL_TEXTURE_2D, 0);	
			System.out.println("things");
			return;
		}
		if(image == null) return;
		int il = image.getWidth()*image.getHeight();		
		//	Graphics2D g = image.createGraphics();
		//	g.rotate(Math.toRadians(180), image.getHeight()/2, image.getWidth()/2);
		//AffineTransform xform = AffineTransform.getTranslateInstance(0.5*image.getHeight(),0.5*image.getWidth());
		//	xform.rotate(Math.toRadians(180));
		//	xform.translate(-0.5*image.getWidth(), -0.5*image.getHeight());
		//	g.drawImage(image,0,0,null);
		int[] pixles = new int[il];
		PixelGrabber pg = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixles,0,image.getWidth());
		ColorModel cm = pg.getColorModel();		

		try {
			pg.grabPixels();
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
		textureBuffer = (ByteBuffer.allocateDirect(il*4*3)).asIntBuffer();

		for(int i=0; i<pixles.length;i++){
			//System.out.println("hay");
			textureBuffer.put(cm.getRed(pixles[i]));
			textureBuffer.put(cm.getGreen(pixles[i]));
			textureBuffer.put(cm.getBlue(pixles[i]));
		}
		textureBuffer.rewind();
		imageToArray();
		texID = GL11.glGenTextures();		
		glBindTexture(GL_TEXTURE_2D, texID);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);		
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE,GL11.GL_MODULATE);		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);		
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);					
		glTexImage2D (GL_TEXTURE_2D, 0, GL_RGB, image.getWidth(null),image.getHeight(null),0,GL_RGB,GL_UNSIGNED_INT,textureBuffer);	
		glBindTexture(GL_TEXTURE_2D, 0);		
	}	



	@Override
	public void render() {
	//	System.out.println(vertexBufferID + " " + getName());
		if(!builtTexture){
			builtTexture = true;			
			loadTexture();
		}
		if(lighting){
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
		}else{
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
		}
		if(built == false){
			if(texID == 0){
				if(vertexBufferID < 1) vertexBufferID = ARBVertexBufferObject.glGenBuffersARB();
				if(normalBufferID < 1) normalBufferID = ARBVertexBufferObject.glGenBuffersARB();
				DoubleBuffer verts = BufferUtils.createDoubleBuffer(faces.size() * 9);
				DoubleBuffer norms = BufferUtils.createDoubleBuffer(faces.size() * 9);
				for(Face f: faces){					
					Point3d n1 = normals.get((int)f.normal.x-1);
					norms.put(point3dtoDoubleArray(n1));

					Point3d v1 = vertices.get((int)f.vertex.x-1);
					verts.put(point3dtoDoubleArray(v1));

					Point3d n2 = normals.get((int)f.normal.y-1);
					norms.put(point3dtoDoubleArray(n2));

					Point3d v2 = vertices.get((int)f.vertex.y-1);
					verts.put(point3dtoDoubleArray(v2));

					Point3d n3 = normals.get((int)f.normal.z-1);
					norms.put(point3dtoDoubleArray(n3));

					Point3d v3 = vertices.get((int)f.vertex.z-1);
					verts.put(point3dtoDoubleArray(v3));

				}	
				verts.flip();
				norms.flip();
				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBufferID);
				ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, verts, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normalBufferID);
				ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, norms, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
				built = true;
			}else{
				if(vertexBufferID < 1) vertexBufferID = ARBVertexBufferObject.glGenBuffersARB();
				if(normalBufferID < 1) normalBufferID = ARBVertexBufferObject.glGenBuffersARB();
				if(textureCoordBufferID < 1) textureCoordBufferID = ARBVertexBufferObject.glGenBuffersARB();
				DoubleBuffer verts = BufferUtils.createDoubleBuffer(faces.size() * 9);
				DoubleBuffer norms = BufferUtils.createDoubleBuffer(faces.size() * 9);
				DoubleBuffer texVr = BufferUtils.createDoubleBuffer(faces.size() * 6);
				for(Face f: faces){					
					Point3d n1 = normals.get((int)f.normal.x-1);
					norms.put(point3dtoDoubleArray(n1));

					Point3d v1 = vertices.get((int)f.vertex.x-1);
					verts.put(point3dtoDoubleArray(v1));

					Point2d t1 = texVerts.get((int)f.texture.x-1);
					Point2d out1 = new Point2d();
					out1.x = t1.y;
					out1.y = t1.x;
					texVr.put(point2dtoDoubleArray(t1));


					Point3d n2 = normals.get((int)f.normal.y-1);
					norms.put(point3dtoDoubleArray(n2));

					Point3d v2 = vertices.get((int)f.vertex.y-1);
					verts.put(point3dtoDoubleArray(v2));

					Point2d t2 = texVerts.get((int)f.texture.y-1);
					Point2d out2 = new Point2d();
					out2.x = t2.y;
					out2.y = t2.x;
					texVr.put(point2dtoDoubleArray(t2));


					Point3d n3 = normals.get((int)f.normal.z-1);
					norms.put(point3dtoDoubleArray(n3));

					Point3d v3 = vertices.get((int)f.vertex.z-1);
					verts.put(point3dtoDoubleArray(v3));

					Point2d t3 = texVerts.get((int)f.texture.z-1);
					Point2d out3 = new Point2d();
					out3.x = t3.y;
					out3.y = t3.x;
					texVr.put(point2dtoDoubleArray(t3));
				}	
				verts.rewind();
				norms.rewind();
				texVr.rewind();
				//Flip the texVr the coordinates are in backwards order.
				//		DoubleBuffer temp = BufferUtils.createDoubleBuffer(texVr.capacity());
				//		for(int i=0;i<texVr.capacity();i++){
				//			temp.put((texVr.capacity()-1)-i, texVr.get(i));
				//		}
				//		temp.rewind();
				//		texVr = temp;
				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBufferID);
				ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, verts, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normalBufferID);
				ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, norms, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, textureCoordBufferID);
				ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, texVr, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
				built = true;
				//	System.out.println(id);				
				//loadTexture();
			}
		}else{
			if(texID == 0){
				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);			
				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBufferID);		
				GL11.glVertexPointer(3, GL11.GL_DOUBLE, 0,0L);			

				GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normalBufferID);			
				GL11.glNormalPointer( GL11.GL_DOUBLE, 0,0);			

				GL11.glPushMatrix();			
				GL11.glTranslated(location.x, location.y, location.z);
				GL11.glRotated(rot.x, 1, 0, 0);
				GL11.glRotated(rot.y, 0, 1, 0);
				GL11.glRotated(rot.z, 0, 0, 1);    

				GL11.glColor3f(color.r, color.g, color.b);
				//	GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 10f);
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, faces.size() * 3);			
				GL11.glPopMatrix();			

				GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
				return;
			}else{
				glBindTexture(GL_TEXTURE_2D, texID);

				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);			
				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, textureCoordBufferID);		
				GL11.glTexCoordPointer(2, GL11.GL_DOUBLE, 0,0L);

				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);			
				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBufferID);		
				GL11.glVertexPointer(3, GL11.GL_DOUBLE, 0,0L);			

				GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normalBufferID);			
				GL11.glNormalPointer( GL11.GL_DOUBLE, 0,0);			

				GL11.glPushMatrix();			
				GL11.glTranslated(location.x, location.y, location.z);
				GL11.glRotated(rot.x, 1, 0, 0);
				GL11.glRotated(rot.y, 0, 1, 0);
				GL11.glRotated(rot.z, 0, 0, 1);    

				GL11.glColor3f(color.r, color.g, color.b);
				//	GL11.glColor3f(color.R, color.G, color.B);
				//	GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 10f);
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, faces.size() * 3);			
				GL11.glPopMatrix();			

				GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);


				ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);	
				glBindTexture(GL_TEXTURE_2D, 0);
			}
		}
	}
	public double[] point3dtoDoubleArray(Point3d p){		
		double[] d = new double[3];
		d[0] =(double) p.x;
		d[1] =(double) p.y;
		d[2] =(double) p.z;
		return d;
	}

	public double[] point2dtoDoubleArray(Point2d p){		
		double[] d = new double[2];
		d[0] =(double) p.x;
		d[1] =(double) p.y;		
		return d;
	}

	@Override
	public void cleanUp() {
		glDeleteTextures(texID);
		ARBVertexBufferObject.glDeleteBuffersARB(normalBufferID);
		ARBVertexBufferObject.glDeleteBuffersARB(vertexBufferID);
		ARBVertexBufferObject.glDeleteBuffersARB(textureCoordBufferID);	
	}


	@Override
	public boolean getVisible() {
		return visable;
	}
	@Override
	public void addedToWorld(World w) {
		world = w;
	}
	@Override
	public void tick() {
		if(rb == null) return;
		if(dynamic){
			Transform t = new Transform();
			rb.getWorldTransform(t);
			location.x = t.origin.x;
			location.y = t.origin.y;
			location.z = t.origin.z;
			Quat4f q = new Quat4f();
			rb.getOrientation(q);
			Vector3f v = new Vector3f();
			Utils.QuaternionToEuler(q, v);
			rot.x = v.x;
			rot.y = v.y;
			rot.z = -v.z;
		}
	}

	public Model duplicate(){	
		Model m = new Model();
		m.location = location.duplicate();
		m.image = image;
		m.normals = normals;
		m.rot = new Vector3f(rot.x,rot.y,rot.z);
		m.vertices = vertices;		
		m.visable = visable;
		m.vertexBufferID = vertexBufferID;
		m.normalBufferID = normalBufferID;
		m.textureCoordBufferID = textureCoordBufferID;
		m.texID = texID;
		m.built = true;
		m.faces = faces;
		m.name = name;
		if(dynamic){		
			m.dynamic = true;
			m.addToPhysWorld(collisonShape, mass);
		}else{
			m.dynamic = false;
		}		
		return m;
	}
	@Override
	public void setVisible(boolean visible) {
		this.visable = visible;
	}

	@Override
	public void removedFromWorld() {
		if(rb != null) world.removeRigidBody(rb);
	}
	@Override
	public String getName() {
		return name;		
	}
	@Override
	public void rot(Vector3f rot) {
		rot(rot.x,rot.y,rot.z);
		
	}
	@Override
	public Point3d getLocation() {
		return location;
	}
	@Override
	public Vector3f getRot() {
		return rot;
	}	

}
