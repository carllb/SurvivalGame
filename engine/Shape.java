package engine;

import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
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
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDeleteLists;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.shapes.CollisionShape;

@SuppressWarnings("unused")
public class Shape implements VisibleObject,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 182L;

	public final double factor = (Math.PI/180);
	
	//Vertices and rotated verts
	public ArrayList<Point3d> vertices = new ArrayList<Point3d>();
	public ArrayList<Point2d> texCoords = new ArrayList<Point2d>();
	//public ArrayList<Point3d> rverts   = new ArrayList<Point3d>();
	transient private Image[] images;
	//public String[] imageURL;
	//planes and rotated planes
	public ArrayList<Plane> planes = new ArrayList<Plane>();
	private ArrayList<Plane> rplanes = new ArrayList<Plane>();
	
	
	public Point3d size;
	public Color3f color = new Color3f(1,0,0); 	
	public String name = "NULL";
	public Point3d location = new Point3d(0,0,0);
	private Point3d rotation = new Point3d(0,0,0);
	private IntBuffer[] imgbytebuffers;
	//private (int[])[] = new (int[])[20];
	private int[] texIds;
	private boolean byteBufferUpdated = false;
	private ByteBuffer bitmap;
	private int vertperplane; 
	private int listID;
	private boolean usingDiplayList = true;	
	boolean visable = true;
	
	
	public boolean getVisible(){
		return visable;
	}
	
	//FOR BRETT This is a constructor for the Shape class
	//the other one will set all the values that this one asks for but the other doesn't, to default;
	// for either one leave images null for no texture (it will use the color)	
	
	public Shape(Point3d[] verts,Point2d[] texCoords, String name,Image[] images,int vertperplane){
	
		this.vertperplane = vertperplane;
	
		for(int i = 0; i<verts.length;i++){
			vertices.add(verts[i]);
		}
		for(int i = 0; i<texCoords.length;i++){
			this.texCoords.add(texCoords[i]);
		}
		
		for(int i =0; i+(vertperplane-1)<vertices.size();i+=vertperplane){
			Point3d[] pverts = new Point3d[vertperplane];
			Point2d[] tverts = new Point2d[vertperplane];
			for(int j =0;j<vertperplane;j++){				
				pverts[j] = vertices.get(i+j);
				tverts[j] = this.texCoords.get(i+j);
			}
			planes.add(new Plane(pverts,tverts));
		}
		
		checkPlanesandImages();
		
		
		this.images = images;
		this.name = name;
		
	}
	public void remakePlanes(){
		planes.clear();	
		for(int i =0; i+(vertperplane-1)<vertices.size();i+=vertperplane){
			Point3d[] pverts = new Point3d[vertperplane];
			Point2d[] tverts = new Point2d[vertperplane];
			for(int j =0;j<vertperplane;j++){				
				pverts[j] = vertices.get(i+j);
				tverts[j] = this.texCoords.get(i+j);
			}
			planes.add(new Plane(pverts,tverts));
		}
	}
	
	public void setImage(Image image,int index){
		this.images[index] = image;
	}
	public void setImage(Image[] images){
		this.images = images;
	}
	public void loadedFromFile(World world){
		if(images != null){
			
		}
				
	}
	
	
	public void rotate(double x, double y, double z){
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}
	
	
	public double getDistanceBetweenShapes(Shape s){
		return Math.sqrt(Math.pow(s.location.x - location.x,2) + Math.pow(s.location.y - location.y,2) + Math.pow(s.location.z - location.z,2));		
	}
	
	public void setListID(int id){
		listID = id;
	}
	
	public void useDispList(boolean udl){
		usingDiplayList = udl;
	}
	
	//Texture stuff (not easy) it turns out that the image must have a special format
	//I will try label some stuff in this method
	public void loadTexture(){
		
		imgbytebuffers = new IntBuffer[images.length];
		texIds = new int[images.length];
		//Converts image into IntBuffer
		for(int i = 0;i<images.length;i++){
			Image image = images[i];
			int[] pixels = new int[image.getWidth(null) * image.getHeight(null)];
			int[] rgbpixels = new int[(image.getWidth(null) * image.getHeight(null))*3];
			PixelGrabber pg = new PixelGrabber(image, 0, 0, image.getWidth(null),image.getHeight(null),pixels,0,image.getWidth(null));
			try{
				pg.grabPixels();
			}catch(Exception e){
				e.printStackTrace();
				return;
			}
			
			for(int j=0;j<pixels.length;j++){
				int[] rgb = new int[3];
				ColorModel cm = pg.getColorModel();
				rgb[0] = cm.getRed(pixels[j]);
				rgb[1] = cm.getGreen(pixels[j]);
				rgb[2] = cm.getBlue(pixels[j]);
				//rgb[3] = cm.getAlpha(pixels[j]);				
				rgbpixels[j*3] = rgb[0];
				rgbpixels[j*3 + 1] = rgb[1];
				rgbpixels[j*3 + 2] = rgb[2];			
			}						
			IntBuffer bb = ( ByteBuffer.allocateDirect( (rgbpixels.length * 4)*3 ).asIntBuffer() );
			bb.put(rgbpixels);			
			bb.rewind();		
			imgbytebuffers[i] = bb;
			byteBufferUpdated = true;
		}
		IntBuffer tmp = (ByteBuffer.allocateDirect(imgbytebuffers.length * 4)).asIntBuffer();
		glGenTextures(tmp);
		for(int i=0;i<tmp.capacity();i++){
			texIds[i] = tmp.get(i);
		}
		for(int i = 0;i<imgbytebuffers.length;i++){
			glBindTexture(GL_TEXTURE_2D, texIds[i]);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);		
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);		
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);			
			glTexImage2D (GL_TEXTURE_2D, 0, GL_RGB, images[i].getWidth(null),images[i].getHeight(null),0,GL_RGB,GL_UNSIGNED_INT,imgbytebuffers[i]);		
		}
		GL11.glBindTexture(GL_TEXTURE_2D, 0);
	}
	public void move(Point3d newLocation){
		location = newLocation;
	}
	
	
	
	public void checkPlanesandImages(){
		if(images !=null){
			if(images.length != planes.size()){
				new RuntimeException("Planes don't Match up with Images " + "Images: " + images.length + " Planes: " + planes.size());
			}
		}
		
	}
	
	public void render(){
		if(listID == 0) genDisplayList();
		
		glCallList(listID);		
	}
	
	
	public void genDisplayList(){
		
		if(byteBufferUpdated == false && images != null){
			loadTexture();
		}		
		if(images != null && imgbytebuffers != null){			
			for(int i = 0;i<imgbytebuffers.length;i++){					
				glBindTexture(GL_TEXTURE_2D, texIds[i]);
				glTranslated(location.x, location.y, location.z);				
				glRotated(rotation.x,1,0,0);
				glRotated(rotation.y,0,1,0);
				glRotated(rotation.z,0,0,1);				
				glBegin(GL_POLYGON);
					for(int j=0;j<planes.get(i).verts.length;j++){						
						Point3d vert = planes.get(i).verts[j];
						Point2d tvert = planes.get(i).texCoords[j];						
						glTexCoord2d(tvert.x,tvert.y);					
						glVertex3d(vert.x,vert.y,vert.z);
					}
				glEnd();	
				
				glRotated(-rotation.z,0,0,1);
				glRotated(-rotation.y,0,1,0);		
				glRotated(-rotation.x,1,0,0);						
				glTranslated(-location.x, -location.y,-location.z);
			}	
			glBindTexture(GL_TEXTURE_2D, 0);
		}else{
			for(int i = 0;i<planes.size();i++){					
				glBindTexture(GL_TEXTURE_2D, 0);
				glTranslated(location.x, location.y, location.z);				
				glRotated(rotation.x,1,0,0);
				glRotated(rotation.y,0,1,0);
				glRotated(rotation.z,0,0,1);	
				//glColor3f(color.R, color.G, color.B);
				glColor3f(color.r, color.g, color.b);
				//System.out.println("hello");
				glBegin(GL_POLYGON);
					for(int j=0;j<planes.get(i).verts.length;j++){						
						Point3d vert = planes.get(i).verts[j];
					//	Point2d tvert = planes.get(i).texCoords[j];						
				//		glTexCoord2d(vert.x,vert.y);					
						glVertex3d(vert.x,vert.y,vert.z);						
					}
				glEnd();	
				
				glRotated(-rotation.z,0,0,1);
				glRotated(-rotation.y,0,1,0);		
				glRotated(-rotation.x,1,0,0);						
				glTranslated(-location.x, -location.y,-location.z);
			}	
		}		
	}
	@Override
	public void setVisible(boolean visible) {
		this.visable = visible;
	}
	
	public void tick(){
	}
	
	
	public double getDist(double x1,double y1, double x2, double y2){		
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));	
	}
	 
	public void addedToWorld(World world){
	}
	
	@Override
	public void cleanUp() {
		glDeleteLists(listID, 1);		
	}
	 
	
	

	public Shape duplicate(){
		Shape s = new Shape( (Point3d[]) vertices.toArray(), (Point2d[]) texCoords.toArray(),name,images,vertperplane);
		s.location = location;
		s.rotation = rotation;
		s.color = color;
		return s;
	}
	
	public class Plane implements Serializable{		
		public Point3d[] verts;	
		Point2d[] texCoords;
		public Plane(Point3d[] verts,Point2d[] texCoords){			
			this.verts = verts;
			if(verts == null) System.out.println("JSHDKSH");
			this.texCoords = texCoords;
		}	
		
		
		public boolean getVectors(Vector3d dest1,Vector3d dest2){
			if(verts.length < 3) return false;
			dest1.x = verts[0].x - verts[1].x;
			dest1.y = verts[0].y - verts[1].y;
			dest1.z = verts[0].z - verts[1].z;			
			dest2.x = verts[0].x - verts[2].x;
			dest2.y = verts[0].y - verts[2].y;
			dest2.z = verts[0].z - verts[2].z;			
			return true;
		}
		
		
		
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
	public void removeFromPhysWorld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToPhysWorld(CollisionShape cs, float mass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rot(javax.vecmath.Vector3f rot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point3d getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.vecmath.Vector3f getRot() {
		// TODO Auto-generated method stub
		return null;
	}


}
