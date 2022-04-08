package engine;

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
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;

public class AnimatedModel implements VisibleObject{

	int currentFrame = 0;
	transient DoubleBuffer vertexBuffer;
	transient DoubleBuffer normalBuffer;
	transient DoubleBuffer textureCoordBuffer;      
	public Point3d location = new Point3d(0, 0, 0);
	ArrayList<Point3d> tempVertBuff = new ArrayList<Point3d>();
	ArrayList<Point3d> tempNormBuff = new ArrayList<Point3d>();
	ArrayList<Point2d> tempCoordBuff = new ArrayList<Point2d>();
	public boolean visable = true;
	ArrayList<Integer> arrayVertSize = new ArrayList<Integer>();
	ArrayList<Integer> arrayNormSize = new ArrayList<Integer>();
	ArrayList<Integer> arrayCoordSize = new ArrayList<Integer>();
	ArrayList<Integer> frameStart = new ArrayList<Integer>();
	boolean built = false;
	public Vector3f rot = new Vector3f(0, 0, 0);
	int totalFrames = 0;
	public BufferedImage texture = null;
	transient IntBuffer textureBuffer;
	int texID = 0;
	transient World world;
	transient RigidBody rigidBody;
	int width, height;
	int[] textureData;
	transient boolean readyToWrite = false;
	public String name = null;

	public AnimatedModel(){

	}
	public void lodedFromFile(){
		built = false;
		textureBuffer = ByteBuffer.allocateDirect(width*height*12).asIntBuffer();
		textureBuffer.put(textureData);
		textureBuffer.rewind();
	}

	public void imageToArray(){
		width = texture.getWidth();
		height = texture.getHeight();
		textureData = new int[width*height*12];
		for(int i=0;i<textureData.length;i++){
			textureData[i] = textureBuffer.get(i);
		}
		readyToWrite = true;
	}

	public void addFrame(ArrayList<Point3d> verts, ArrayList<Point3d> norms, ArrayList<Point2d> texCoords, ArrayList<Face> faces){
		totalFrames += 1;
		frameStart.add(tempVertBuff.size());
		for(Face f: faces){
			tempVertBuff.add(verts.get((int) f.vertex.x-1));
			tempVertBuff.add(verts.get((int) f.vertex.y-1));
			tempVertBuff.add(verts.get((int) f.vertex.z-1));

			tempNormBuff.add(norms.get((int) f.normal.x-1));
			tempNormBuff.add(norms.get((int) f.normal.y-1));
			tempNormBuff.add(norms.get((int) f.normal.z-1));

			tempCoordBuff.add(texCoords.get((int) f.texture.x-1));
			tempCoordBuff.add(texCoords.get((int) f.texture.y-1));
			tempCoordBuff.add(texCoords.get((int) f.texture.z-1));
		}
		arrayVertSize.add(faces.size()*9);
		arrayNormSize.add(faces.size()*9);
		arrayCoordSize.add(faces.size()*6);
	}

	public void doneAddingFrames(){
		int vS = 0;
		int nS = 0;
		int tS = 0;

		for(Integer i: arrayVertSize){
			vS += i;
		}
		for(Integer i: arrayNormSize){
			nS += i;
		}
		for(Integer i: arrayCoordSize){
			tS += i;
		}

		vertexBuffer = BufferUtils.createDoubleBuffer(vS);
		normalBuffer = BufferUtils.createDoubleBuffer(nS);
		textureCoordBuffer = BufferUtils.createDoubleBuffer(tS);

		for(Point3d p: tempVertBuff){
			vertexBuffer.put(p.asDoubleArray());
		}
		for(Point3d p: tempNormBuff){
			normalBuffer.put(p.asDoubleArray());
		}
		for(Point2d p: tempCoordBuff){
			textureCoordBuffer.put(p.asDoubleArray());
		}

	}

	public void loadTexture(){
		if(textureBuffer != null){
			texID = GL11.glGenTextures();           
			glBindTexture(GL_TEXTURE_2D, texID);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);          
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE,GL11. GL_MODULATE);               
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);          
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);                                     
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width,height,0,GL_RGB,GL_UNSIGNED_INT,textureBuffer);    
			glBindTexture(GL_TEXTURE_2D, 0);        
			return;
		}

		int il = texture.getWidth()*texture.getHeight();
		int[] pixles = new int[il];
		PixelGrabber pg = new PixelGrabber(texture, 0, 0, texture.getWidth(), texture.getHeight(), pixles,0,texture.getWidth());
		ColorModel cm = pg.getColorModel();             
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {              
			e.printStackTrace();
		}
		textureBuffer = (ByteBuffer.allocateDirect(il*4*3)).asIntBuffer();

		for(int i=0;i<pixles.length;i++){
			textureBuffer.put(cm.getRed(pixles[i]));
			textureBuffer.put(cm.getGreen(pixles[i]));
			textureBuffer.put(cm.getBlue(pixles[i]));                         
		}
		textureBuffer.flip();
		texID = GL11.glGenTextures();           
		glBindTexture(GL_TEXTURE_2D, texID);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);          
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE,GL11. GL_MODULATE);               
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);          
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);                                     
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, texture.getWidth(null),texture.getHeight(null),0,GL_RGB,GL_UNSIGNED_INT,textureBuffer);  
		glBindTexture(GL_TEXTURE_2D, 0);                
	}

	public void stepAnimation(int steps){
		if(currentFrame >= (totalFrames-1)){
			currentFrame = 0;                       
			return;
		}
		currentFrame += steps;
	}
	public void gotoFrame(int frame){
		currentFrame = frame;
	}

	int vertexBufferID = 0;
	int normalBufferID = 0;
	int textureCoordBufferID = 0;

	public boolean readyToWrite(){
		return readyToWrite;
	}


	@Override
	public void render() {                  
		if(built == false){

			if(vertexBufferID < 1) vertexBufferID = ARBVertexBufferObject.glGenBuffersARB();
			if(normalBufferID < 1) normalBufferID = ARBVertexBufferObject.glGenBuffersARB();
			if(textureCoordBufferID < 1) textureCoordBufferID = ARBVertexBufferObject.glGenBuffersARB();

			vertexBuffer.flip();
			normalBuffer.flip();
			textureCoordBuffer.flip();
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBufferID);
			ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBuffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normalBufferID);
			ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normalBuffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, textureCoordBufferID);
			ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, textureCoordBuffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
			built = true;
			if(texture != null || textureBuffer != null) {
				loadTexture();

			}

		}else{
			if(texture != null || textureBuffer != null){
				glBindTexture(GL_TEXTURE_2D, texID);            

			}

			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);                  
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, textureCoordBufferID);         
			GL11.glTexCoordPointer(2, GL11.GL_DOUBLE, 0,0);

			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);                 
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBufferID);               
			GL11.glVertexPointer(3, GL11.GL_DOUBLE,0,0);                    

			GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normalBufferID);                       
			GL11.glNormalPointer(GL11.GL_DOUBLE, 0,0);



			GL11.glPushMatrix();

			GL11.glTranslated(location.x, location.y, location.z);
			GL11.glRotated(rot.x, 1, 0, 0);
			GL11.glRotated(rot.y, 0, 1, 0);
			GL11.glRotated(rot.z, 0, 0, 1);    

			//              GL11.glColor3f(1, 0, 0);
			//      GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 10f);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, frameStart.get(currentFrame),arrayVertSize.get(currentFrame)/3); 
			GL11.glPopMatrix();

			/*
                        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
                        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			 */

			 ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);    
			 glBindTexture(GL_TEXTURE_2D, 0);
		}

	}

	@Override
	public void cleanUp() {
		GL11.glDeleteTextures(texID);
		ARBVertexBufferObject.glDeleteBuffersARB(normalBufferID);
		ARBVertexBufferObject.glDeleteBuffersARB(vertexBufferID);
		ARBVertexBufferObject.glDeleteBuffersARB(textureCoordBufferID);
	}       

	public AnimatedModel duplicate(){               
		AnimatedModel m = new AnimatedModel();
		m.location = location.duplicate();
		m.frameStart = frameStart;
		m.arrayCoordSize = m.arrayCoordSize;
		m.arrayNormSize = m.arrayNormSize;
		m.arrayVertSize = m.arrayVertSize;
		m.texture = texture;
		m.currentFrame = currentFrame;
		m.rot = new Vector3f(rot.x,rot.y,rot.z);
		m.visable = visable;
		m.vertexBufferID = vertexBufferID;
		m.normalBufferID = normalBufferID;
		m.textureCoordBufferID = textureCoordBufferID;
		m.texID = texID;
		m.built = true;
		if(world != null){
			world.addObject(m);
		}

		return m;
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
		if(rigidBody == null) return;
		Transform t = new Transform();
		rigidBody.getWorldTransform(t);
		location.x = t.origin.x;
		location.y = t.origin.y;
		location.z = t.origin.z;
		Quat4f q = new Quat4f();
		rigidBody.getOrientation(q);
		Vector3f v = new Vector3f();
		Utils.QuaternionToEuler(q, v);
		rot.x = v.x;
		rot.y = v.y;
		rot.z = -v.z;
	}
	public void move(Point3d newLocation){
		if(rigidBody != null){
			location = newLocation;
			Transform worldTransform = new Transform();
			worldTransform.origin.set(newLocation.asVector3f());
			rigidBody.setWorldTransform(worldTransform);
			location = newLocation;
		}else{
			location = newLocation;
		}

	}
	public void addToPhysWorld(CollisionShape cs,float mass){
		if(world == null){
			System.exit(-1);
		}
		Transform t = new Transform();
		t.setIdentity();
		t.origin.set(location.asVector3f());
		Vector3f i = new Vector3f();
		RigidBody rb = null;
		if(mass > 0){
			cs.calculateLocalInertia(mass, i);
			MotionState ms = new DefaultMotionState(t);
			RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(mass, ms, cs,i);
			rb = new RigidBody(rbci);
		}else{          
			MotionState ms = new DefaultMotionState(t);
			RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(mass, ms, cs);
			rb = new RigidBody(rbci);
		}
		rigidBody = rb;
		world.physWorld.addRigidBody(rb);

	}
	@Override
	public void addToPhysWorld() {
		new RuntimeErrorException(new Error("Animated Model must be supplyed with a Collision Box"));
	}
	@Override
	public void addToPhysWorld(float mass) {
		new RuntimeErrorException(new Error("Animated Model must be supplyed with a Collision Box"));            
	}
	@Override
	public void removeFromPhysWorld() {
		if(rigidBody != null){          
			world.physWorld.removeRigidBody(rigidBody);
		}
	}


	@Override
	public void setVisible(boolean visible) {
		this.visable = visible;
	}


	@Override
	public void removedFromWorld() {
		if(rigidBody != null) world.physWorld.removeRigidBody(rigidBody);
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public void rot(Vector3f rot) {
		if(rigidBody == null){
			this.rot = rot;
		}
		Transform temp = new Transform();
		rigidBody.getWorldTransform(temp);
		Quat4f temp2 = new Quat4f();
		QuaternionUtil.setEuler(temp2, rot.y, rot.x, rot.z);
		temp.setRotation(temp2);
		rigidBody.setWorldTransform(temp);
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