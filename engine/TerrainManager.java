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
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

/**
 *
 * @author Brett Johnson
 *
 */


public class TerrainManager implements VisibleObject{

	float mag = 5;
	boolean visable=true;
	IntBuffer imageBuffer;
	BufferedImage image = null;
	BufferedImage image2 = null;
	ArrayList<Terrain> Tera = new ArrayList<Terrain>();
	float noise;
	public BasicGame game =BasicGame.game;
	int listID = 0;
	int textureID = 0;
	boolean vboUpdated = true;
	public FloatBuffer verts;
	public FloatBuffer texs;
	public FloatBuffer normals;
	int vertID,texID,normID;
	RigidBody rigidBody;
	World world;
	Terrain currentTerrain = null;
	boolean firstBuild = true;
	final int renderAtATime = 9;
	float[]test;
	int def = 0;//definition of terrain size-------------------------VERY IMPORTANT!!!!!!!!!!!
	//Gets set in the initGenMethead
	boolean phys = false;
	int defc = (int) (Math.pow(2, def)+1);
	Random random;

	//Camera cam;
	public TerrainManager(Random random){
		this.random = random;
	}
	public boolean getVisable(){
		return true;
	}

	void loadChunksToVramAndPhys(Terrain[] terrainsRenders){

		if(verts == null){
			verts = BufferUtils.createFloatBuffer(terrainsRenders.length*defc*defc*18);
			texs = BufferUtils.createFloatBuffer(terrainsRenders.length *defc*defc*12);
			normals = BufferUtils.createFloatBuffer(terrainsRenders.length *defc*defc*18);
		}

		verts.clear();
		texs.clear();
		normals.clear();
		//	System.out.println(size);
		CompoundShape collisionShape = new CompoundShape();

		for(int i=0;i<terrainsRenders.length;i++){			
			Terrain t = terrainsRenders[i];
			if(t == null) return;
			//System.out.println(t.startx + " :X LOC Z: " + t.starty);
			//potis
			verts.put(t.vertPart);
			texs.put(t.texPart);
			normals.put(t.normalPart);		
			//This code fixed the stupid bug			
			t.vertPart.position(0);
			t.texPart.position(0);
			t.normalPart.position(0);
			//	System.out.println(t.vertPart);
			Transform tr = new Transform();
			tr.origin.set(0,0,0);
			collisionShape.addChildShape(tr, t.collisionShape);			
		}
		verts.position(0);
		texs.position(0);
		normals.position(0);
		vboUpdated = false;
		/*	for(int i=0;i<verts.capacity();i++){
			System.out.println(verts.get(i));
		}*/				
	}

	public int update(float x, float z, Biome biome){
		int numGenerated = 0;
		//	System.out.println("Player: " + x + "," + z);
		for(int i=0;i<Tera.size();i++){
			Terrain t = Tera.get(i);
			//if((t.startx-x/mag<def&&t.startx-x/mag>=0)&&
			//		(t.starty-z/mag<def&&t.startx-z/mag>=0)){
			//	System.out.println("X: " + x + " Z: " + z) ;
			//	System.out.println("sX: " + t.startx + " sZ: " + t.starty) ;
			float xMag = x/mag, zMag = z/mag;


			if((xMag > t.startx && xMag < t.startx +(defc-1)) && (zMag > t.starty && zMag < t.starty + (defc-1))){			
				if(t != currentTerrain){
					currentTerrain=t;
					//	System.out.println(t.startx + " NEWCHUNK " + t.starty);

					ArrayList<Terrain> tempT = new ArrayList<Terrain>();
					boolean[][] exist = new boolean[3][3];
					for(int a=0;a<Tera.size();a++){						
						Terrain t2 = Tera.get(a);	
						//		System.out.println("Chunk at: " + t2.startx +"," + t2.starty);
						//System.out.println("Defc");
						float xDiff = (t.startx - t2.startx)/(defc-1);
						float yDiff = (t.starty - t2.starty)/(defc-1);						
						if(Math.abs(xDiff) <= 1){
							if(Math.abs(yDiff) <= 1){
								if(tempT.size() < 9){
									tempT.add(t2);
								}else{
									System.out.println("OverFlow!");
								}
								//if(exist[(int) xDiff+1][(int) yDiff+1]) System.out.println("notGood");
								exist[(int) xDiff+1][2-((int) yDiff+1)] = true;
								//			System.out.println(xDiff + " " + yDiff);								
							}else{
								//		System.out.println("Chunk at: " + t2.startx +"," + t2.starty + " Denied! by y diff & checking Chunk:" + t.startx + "," + t.starty);
							}
						}else{
							//		System.out.println("Chunk at: " + t2.startx +"," + t2.starty + " Denied! by x diff & checking Chunk:" + t.startx + "," + t.starty);
						}

					}

					//System.out.println("Count: " + tempT.size());
					int newCount = 0;
					if(tempT.size() <9){
						for(int b=0;b<3;b++){
							for(int c=0;c<3;c++){
								if(c==0 && b==0) continue;
								if(!exist[c][b]){
									//System.out.println(newCount);
									newCount++;
									//System.out.println("Gen new T At:  X: " + t.startx+((1-c)*(defc-1)) + " Z: " + t.starty+((b-1)*(defc-1)));
									Terrain newTerrain = new Terrain(t.startx+((1-c)*(defc-1)),t.starty+((b-1)*(defc-1)),noise);
									int[] adj=getAdjacentChunks(newTerrain);
									//	test = Tera.get(5).ntop;
									float[]top=new float[defc];
									if(adj[1]!=-1)top=Tera.get(adj[1]).nbottom;

									float[]left=new float[defc];
									if(adj[2]!=-1)left=Tera.get(adj[2]).nright;

									float[]right=new float[defc];
									if(adj[3]!=-1)right=Tera.get(adj[3]).nleft;

									float[]bottom=new float[defc];
									if(adj[4]!=-1)bottom=Tera.get(adj[4]).ntop;

									newTerrain.setSeed(top, left,right,bottom, true);
									//	newTerrain.setSeed(test,test,test,test,true);
									//	System.ut.println(right[1]);
									numGenerated++;
									newTerrain.genTerrain((defc), image,mag,random,biome);
									Tera.add(newTerrain);
									addTerrainToPhys(newTerrain);
									if(tempT.size()<9){
										tempT.add(newTerrain);
									}else{
										System.out.println("OverFlow At gen!d");
									}

								}
							}
						}
					}

					if(!exist[0][0]){
						//System.out.println(newCount);
						newCount++;
						//System.out.println("Gen new T At:  X: " + t.startx+((1-c)*(defc-1)) + " Z: " + t.starty+((b-1)*(defc-1)));
						Terrain newTerrain = new Terrain(t.startx+((1)*(defc-1)),t.starty+((-1)*(defc-1)),noise);
						int[] adj=getAdjacentChunks(newTerrain);
						//	test = Tera.get(5).ntop;
						float[]top=new float[defc];
						if(adj[1]!=-1)top=Tera.get(adj[1]).nbottom;

						float[]left=new float[defc];
						if(adj[2]!=-1)left=Tera.get(adj[2]).nright;

						float[]right=new float[defc];
						if(adj[3]!=-1)right=Tera.get(adj[3]).nleft;

						float[]bottom=new float[defc];
						if(adj[4]!=-1)bottom=Tera.get(adj[4]).ntop;

						newTerrain.setSeed(top, left,right,bottom, true);
						//	newTerrain.setSeed(test,test,test,test,true);
						//	System.out.println(right[1]);
						numGenerated++;
						newTerrain.genTerrain((defc), image,mag,random,biome);
						Tera.add(newTerrain);
						addTerrainToPhys(newTerrain);
						if(tempT.size()<9){
							tempT.add(newTerrain);
						}else{
							System.out.println("OverFlow At gen!d");
						}

					}
					Terrain[] terrainRenders = new Terrain[tempT.size()];
					for(int k=0;k<tempT.size();k++){
						terrainRenders[k] = tempT.get(k);
					}
					//Should be moved for better optimization
					loadChunksToVramAndPhys(terrainRenders);		
				}				
			}	
		}
		return numGenerated;
	}
	/**
	 * 
	 * 
	returns:
		1 is north; top
		3 is south; left
		2 is east; right
		4 is west; bottom
		0 is not used

	 **/
	int[] getAdjacentChunks(Terrain t){
		int[]tempT = new int[5];
		for (int a=0;a<tempT.length;a++)
			tempT[a]=-1;
		for (int a=0;a<Tera.size();a++){
			Terrain t2 = Tera.get(a);
			float xDiff = (t.startx - t2.startx)/(defc-1);
			float yDiff = (t.starty - t2.starty)/(defc-1);		
			if(Math.abs(xDiff) <= 1){
				if(Math.abs(yDiff) <= 1){					
					//	System.out.println(xDiff+" y "+yDiff);
					if(xDiff==1 && yDiff==0)
						tempT[1]=a;
					if(xDiff==-1 && yDiff==0){
						tempT[4]=a;
					}
					if(xDiff==0 && yDiff==-1)
						tempT[3]=a;
					if(xDiff==0 && yDiff==1)
						tempT[2]=a;
				}
			}
		}
		return tempT;
	}

	void addTerrainToPhys(Terrain t){	
		if(phys){
			RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(0,new DefaultMotionState(),t.collisionShape);
			RigidBody rigidBody = new RigidBody(rbci);
			world.physWorld.addRigidBody(rigidBody);
		}
	}

	public void Initgen(int def,File imageA, File imageB,Biome startBiome,float sizeFactor){

		this.def = def;
		defc = (int) (Math.pow(2, def)+1);
		//	this.noise=noise;
		test=new float[defc];
		for(int a=0;a<defc;a++)test[a]=(float)Math.random()*5;

		try {
			image = ImageIO.read(imageA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			image2 = ImageIO.read(imageB);
		} catch (IOException e) {
			e.printStackTrace();
		}	

		
		float a = (float) (1/(defc));
		mag = (sizeFactor/(defc-1f));

		System.out.println("mag"+mag);
		int numofchunks=1;
		int count = 0;
		Terrain[] terrainRenders = new Terrain[9];
		for(int aa=0;aa<numofchunks;aa++){
			for(int b=0;b<numofchunks;b++){
				Terrain t = new Terrain(aa*(int)defc,b*(int)defc,noise);//,new int[]{100,0,0,0});
				int[] adj=getAdjacentChunks(t);
				float[]top=new float[defc];
				if(adj[1]!=-1)top=Tera.get(adj[1]).nleft;
				float[]left=new float[defc];
				if(adj[4]!=-1)left=Tera.get(adj[4]).nbottom;
				float[]right=new float[defc];
				if(adj[2]!=-1)right=Tera.get(adj[2]).ntop;
				float[]bottom=new float[defc];
				if(adj[3]!=-1)bottom=Tera.get(adj[3]).nright;
				//		t.setSeed(top, left, right, bottom, true);
				t.genTerrain(defc,image,mag,random,startBiome);
				Tera.add(t);
				addTerrainToPhys(t);
				t.id = count + "original";				
				if(count < terrainRenders.length){
					terrainRenders[count] = t;
					count++;
				}
			}
		}

		//	update(0,0);
		loadChunksToVramAndPhys(terrainRenders);
		System.out.println("people");
	}
	public void render(){
		//	GL11.glDisable(GL11.GL_LIGHTING);
		if(!vboUpdated){ 
			buildVBO();
			vboUpdated = true;
			textureID =	loadTexture(image);
		}
		if(vertID == 0) return;
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
		glBindTexture(GL_TEXTURE_2D, textureID);
		GL11.glColor3f(255,0, 0);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);			
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertID);		
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0,0L);

		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, texID);			
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT,0, 0L);

		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normID);			
		GL11.glNormalPointer(GL11.GL_FLOAT, 0,0);			

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, defc*defc*6*renderAtATime);			


		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
	}
	public int loadTexture(BufferedImage image){
		int textid = 0;
		int il = image.getWidth()*image.getHeight();
		int[] pixles = new int[il];
		PixelGrabber pg = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixles,0,image.getWidth());
		ColorModel cm = pg.getColorModel();		
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
		imageBuffer = (ByteBuffer.allocateDirect(il*4*3)).asIntBuffer();

		for(int i=0;i<pixles.length;i++){
			imageBuffer.put(cm.getRed(pixles[i]));
			imageBuffer.put(cm.getGreen(pixles[i]));
			imageBuffer.put(cm.getBlue(pixles[i]));			
		}
		imageBuffer.rewind();
		IntBuffer tmp = (ByteBuffer.allocateDirect(4).asIntBuffer());
		GL11.glGenTextures(tmp);
		textid = tmp.get(0);
		glBindTexture(GL_TEXTURE_2D, textid);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);		
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);			
		glTexImage2D (GL_TEXTURE_2D, 0, GL_RGB, image.getWidth(null),image.getHeight(null),0,GL_RGB,GL_UNSIGNED_INT,imageBuffer);
		PixelFormat f = new PixelFormat();
		return(textid);
	}

	void buildVBO(){
		if(firstBuild){
			//System.out.println("Seting to 0000000");
			firstBuild = false;
			if(verts == null){
				verts = BufferUtils.createFloatBuffer(renderAtATime*defc*defc*18);
				texs = BufferUtils.createFloatBuffer(renderAtATime *defc*defc*12);
				normals = BufferUtils.createFloatBuffer(renderAtATime*defc*defc*18);
			}
			vertID = ARBVertexBufferObject.glGenBuffersARB();
			texID = ARBVertexBufferObject.glGenBuffersARB();
			normID = ARBVertexBufferObject.glGenBuffersARB();
		}

		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertID);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, verts, ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB);

		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, texID);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, texs, ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB);

		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normID);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, normals, ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB);
		//	System.out.println("ajhd");
		//	System.exit(1);
	}	
	/*
		int textid=loadTexture(image);
		int textid2=loadTexture(image2);
		listID = glGenLists(1);
		glNewList(listID, GL_COMPILE);

		for(int i =0;i<Tera.size();i++){
			Terrain ter = Tera.get(i);
			float[][]Chunk=ter.Chunk;
			float defp =Chunk.length/4;//size of the terrain	
			float defp2 =defp;
			int def = Chunk.length;
			int alt=0;
			int startx=ter.startx-5;
			int startz=ter.starty-5;
			int low=600000;

			float snowheightoffset=(float) (Math.random()*10);
			for (int f=0;f<Chunk.length-1;f++){
				for (int g=0;g<Chunk.length-1;g++){
					if(Chunk[f][g]>alt)alt=(int) Chunk[f][g];
					if(Chunk[f][g]<low)low=(int) Chunk[f][g];
				}
			}
			int avg=(int) ((low+alt)/3);


			for (int f=0;f<Chunk.length-1;f++){
				for (int g=0;g<Chunk.length-1;g++){
					if(f%2==0&&g%2==0)
						if(Chunk[f][g]*mag<(avg*mag)+((alt*mag-avg*mag)/2)+snowheightoffset*.5)
							glColor3f((Chunk[f][g]-avg)/300, (Chunk[f][g]-avg)/200+((float)Math.random()/30f+.1f),(Chunk[f][g]-avg)/300);
						else{
							glColor3f(1-(float)Math.random()/5f, 1-(float)Math.random()/5f ,1-(float)Math.random()/5f);
						}

					if (Chunk[f][g]*mag<(avg*mag)+((alt*mag-avg*mag)/2)+snowheightoffset*.5)					 
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, textid);
					else
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, textid2);

					glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);	
					GL11.glBegin(GL11.GL_TRIANGLES);				 

					GL11.glTexCoord2f(startx*mag+(f*mag)/defp,startz*mag+(g*mag)/defp);
					GL11.glVertex3f(startx*mag+f*mag,Chunk[f][g]*mag,startz*mag+g*mag);


					GL11.glTexCoord2f(startx*mag+(f+1)*mag/defp,startz*mag+g*mag/defp);
					GL11.glVertex3f(startx*mag+(f+1)*mag,Chunk[f+1][g]*mag,startz*mag+g*mag);

					GL11.glTexCoord2f(startx*mag+f*mag/defp,startz*mag+(g+1)*mag/defp);
					GL11.glVertex3f(startx*mag+f*mag,Chunk[f][g+1]*mag,startz*mag+(g+1)*mag);

					GL11.glEnd();			 

					GL11.glBegin(GL11.GL_TRIANGLES);
					GL11.glTexCoord2f(startx*mag+(f+1)*mag/defp,startz*mag+((g+1)*mag)/defp);
					GL11.glVertex3f(startx*mag+(f+1)*mag,Chunk[f+1][g+1]*mag,startz*mag+(g+1)*mag);

					GL11.glTexCoord2f(startx*mag+(f+1)*mag/defp,startz*mag+g*mag/defp);
					GL11.glVertex3f(startx*mag+(f+1)*mag,Chunk[f+1][g]*mag,startz*mag+g*mag);

					GL11.glTexCoord2f(startx*mag+f*mag/defp,startz*mag+(g+1)*mag/defp);
					GL11.glVertex3f(startx*mag+f*mag,Chunk[f][g+1]*mag,startz*mag+(g+1)*mag);

					GL11.glEnd();

					if(f==-10&&g==0){//makes a big blue plane
						glColor3f(0, 0,1);
					GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex3f(startx*mag,avg*mag,startz*mag+0);
					GL11.glVertex3f(startx*mag+(def-1)*mag,avg*mag,startz*mag+0);
					GL11.glVertex3f(startx*mag+(def-1)*mag,avg*mag,startz*mag+((def-1)*mag));
					GL11.glVertex3f(startx*mag,avg*mag,startz*mag+(def-1)*mag);
					GL11.glEnd();
					}

				}
			}

		}
		glEndList();

	 */

	@Override
	public void addedToWorld(World w) {
		this.world = w;
	}
	@Override
	public void cleanUp() {
		//TODO DELETE THE BUFFERS!!! ------- Very Important 

	}
	@Override
	public void move(Point3d newLocation) {
		// TODO Auto-generated method stub

	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub

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
	public void removedFromWorld() {
		// TODO Auto-generated method stub
	}
	public void addToPhysWorld(){
		phys = true;		
	}
	@Override
	public void addToPhysWorld(float mass) {
		phys = true;
		for(int i=0;i<Tera.size();i++){
			addTerrainToPhys(Tera.get(i));
		}
	}
	@Override
	public void addToPhysWorld(CollisionShape cs, float mass) {	
		System.err.println("Terrain currently can not have custom collision shape!");
	}
	@Override
	public void removeFromPhysWorld() {
		phys = false;	
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
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
