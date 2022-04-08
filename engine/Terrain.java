package engine;

import static org.lwjgl.opengl.GL11.glDeleteLists;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.collision.shapes.TriangleMeshShape;





public class Terrain{

	//Mag is the size of the terrain, actuality it is a multiplier that determines the size
	//when cords would normaly be x,y,z now are multiplied (ex: 1,2,2,--> mag = 10 --> 10,20,20
	int mag = 5;
	float noise= 1f;
	float smoothingfactor= 1f;
	float Chunk [][];
	int defp;
	boolean multiplechunks=false;
	int startx;
	int starty;
	int[] seed = new int[4];
	int displayListId;
	public ArrayList<Triangle> triangles = new ArrayList<Triangle>();	
	Render render;
	IntBuffer imageBuffer;
	int textid = 0;
	BufferedImage image;
	boolean texutreLoaded = false;
	boolean visable = true;
	public float[] points;
	public int[] tris;	
	float[]top;
	float[]left;
	float[]right;
	float[]bottom;
	float[]ntop;
	float[]nleft;
	float[]nright;
	float[]nbottom;
	CollisionShape collisionShape;
	public FloatBuffer vertPart;
	public FloatBuffer texPart;
	public FloatBuffer	normalPart;

	public String id = "null";


	public boolean getVisable(){
		return visable;
	}
	//public Terrain(Render render){
	//	this.render = render;	
	//	render.terrainNEEDbuild.add(this);
	//}

	public void cleanUp(){
		glDeleteLists(displayListId, 1);
	}
	//if the array of prev cordinates is all zer0, it ignores it 



	public Terrain(int x,int y,float noise, int[] seed){
		for(int a =0;a<seed.length;a++)
			this.seed[a]=seed[a];
		this.noise=noise;
		startx=x;
		starty=y;

	}
	public Terrain(int x,int y,float noise){
		for(int a =0;a<seed.length;a++)
			seed[a]=10;
		this.noise=noise;
		startx=x;
		starty=y;
	}
	public Terrain(int x,int y){
		for(int a =0;a<seed.length;a++)
			seed[a]=10;
		startx=x;
		starty=y;
	}
	public void setSeed(float[]top,float[]left,float[]right,float[]bottom,boolean mult){	
		this.top=top;
		this.left=left;
		this.right=right;
		this.bottom=bottom;

		//	for(int a=0;a<defp;a++){
		//		this.top[a]=top[a];
		//		this.left[a]=left[a];
		//		this.right[a]=right[a];
		//		this.bottom[a]=bottom[a];
		//	}
		multiplechunks=mult;
	}

	public float[][] genTerrain(int def,BufferedImage image,float mag,Random random,Biome biome){
		this.image = image;
		Chunk = new float[def][def];
		defp= def;
		biome.setRandom(random);
		biome.setMag(mag);
		
		int histp = 0;
		//int rem = a;
		double height = 2;
		//determines flatness of terrain
		double spacing =0;
		//	float maxHeight = biome.getMaxHeight();
		//	float minHeight = biome.getMinHeight();
		for(int a=0;a<Chunk.length;a++){
			for (int b=0;b<Chunk.length;b++){
				Chunk[a][b]=0;	
			}
		}

		Chunk[0][(Chunk.length-1)]= (float) (random.nextFloat()*seed[1]);
		Chunk[0][0]= (float) (random.nextFloat()*seed[0]);
		Chunk[(Chunk.length-1)][0]= (float) (random.nextFloat()*seed[2]);
		Chunk[Chunk.length-1][(Chunk.length-1)]= (float) (random.nextFloat()*seed[3]);
		if (multiplechunks){
			for(int a=0;a<Chunk.length;a++){
				if(top[a]!=0)
					Chunk[0][a]=top[a];
				//		else
				//			Chunk[0][a] = (random.nextFloat()*(maxHeight-minHeight))+minHeight;

				if(bottom[a]!=0)
					Chunk[defp-1][a]=bottom[a];
				//	else
				//		Chunk[defp-1][a] = (random.nextFloat()*(maxHeight-minHeight))+minHeight;

				if(left[a]!=0)
					Chunk[a][0]=left[a];
				//	else
				//		Chunk[a][0] = (random.nextFloat()*(maxHeight-minHeight))+minHeight;

				if(right[a]!=0)
					Chunk[a][defp-1]=right[a];
				//	else
				//		Chunk[a][defp-1] = (random.nextFloat()*(maxHeight-minHeight))+minHeight;
			}
		}

		for(int a=(def-1);a>=2;a=a/2){
			//	System.out.println(a);
			int size=Chunk.length-1;
			int half=a/2;
			int div = (int) Math.pow(2, a);
			//int div2 = (int) Math.pow(2, b);
			for(int x=0;x<Chunk.length-a;x+=a){
				for(int y=0;y<Chunk.length-a;y+=a){
					noise = biome.getNoise(x,y);
					//if (x==0) avg=Chunk[x][y+half]+Chunk[x+half][y+half]+Chunk[x+half][y-half]+Chunk[x][y-half];
					//else if (y==0) avg=Chunk[x-half][y+half]+Chunk[x+half][y+half]+Chunk[x+half][y]+Chunk[x-half][y];
					//else if (y==256)avg=Chunk[x-half][y]+Chunk[x+half][y]+Chunk[x+half][y-half]+Chunk[x-half][y-half];
					//else if (x==256)avg=Chunk[x-half][y+half]+Chunk[x+half][y+half]+Chunk[x+half][y-half]+Chunk[x-half][y-half];
					//if (Chunk[x][y]==0){
					float avg=0;
					//avg=Chunk[(x+half)][y]+Chunk[(x-half)][y]+Chunk[x][(y-half)]+Chunk[x][(y+half)];
					//if ((x-half)>=0) avg+= Chunk[(x-half)][y];
					//if ((x+half)<def)avg+=Chunk[(x+half)][y];
					//if ((y-half)>=0) avg+=Chunk[x][(y-half)];
					//if ((y+half)<def)avg+=Chunk[x][(y+half)];
					avg=(Chunk[x][y]+Chunk[x+a][y]+Chunk[x+a][y+a]+Chunk[x][y+a])/4;
					float pd=(noise/2);
					float ab = (float) (Math.sqrt(a/2));
					float av2=(float)(avg+(((random.nextFloat()*noise)-pd)*(half/2)));
					if(Chunk[x+half][y+half]==0)Chunk[x+half][y+half]=av2;
					avg=(Chunk[x][y]+Chunk[x+a][y]+Chunk[x+a][y+a]+Chunk[x][y+a]+Chunk[x+half][y+half])/5;

					avg=(Chunk[x+a][y]+Chunk[x][y]+Chunk[x+half][y+half])/3;
					av2=(float)(avg+(((random.nextFloat()*noise)-pd)*(half/2)));
					if(Chunk[x+half][y]==0)Chunk[x+half][y]=av2;//(float) ( (((random.nextFloat()*(avg*ab)/noise)-pd))/smoothingfactor);
					avg=(Chunk[x][y+a]+Chunk[x][y]+Chunk[x+half][y+half])/3;
					av2=(float)(avg+(((random.nextFloat()*noise)-pd)*(half/2)));					
					if(Chunk[x][y+half]==0)Chunk[x][y+half]=av2;//(float) ( (((random.nextFloat()*(avg*ab)/noise)-pd))/smoothingfactor);
					avg=(Chunk[x][y+a]+Chunk[x+a][y+a]+Chunk[x+half][y+half])/3;
					av2=(float)(avg+(((random.nextFloat()*noise)-pd)*(half/2)));
					if(Chunk[x+half][y+a]==0)Chunk[x+half][y+a]=av2;//(float) ((((random.nextFloat()*(avg*ab)/noise)-pd))/smoothingfactor);
					avg=(Chunk[x+a][y]+Chunk[x+a][y+a]+Chunk[x+half][y+half])/3;
					av2=(float)(avg+(((random.nextFloat()*noise)-pd)*(half/2)));
					if(Chunk[x+a][y+half]==0)Chunk[x+a][y+half]=av2;///avg(float) ((((random.nextFloat()*(avg*ab)/noise)-pd))/smoothingfactor);
				}
			}
		}
		for(int i=0;i<biome.getSmoothTimes();i++){
			Chunk = smooth(Chunk);
		}
		ntop=new float[defp];
		nbottom=new float[defp];
		nleft=new float[defp];
		nright=new float[defp];
		for(int a=defp-1;a>=0;a--){

			ntop[a]=Chunk[0][a];

			nbottom[a]=Chunk[def-1][a];

			nleft[a]=Chunk[a][0];

			nright[a]=Chunk[a][def-1];

			//	ntop[a]=Chunk[0][(def-1) - a];

			//			nbottom[a]=Chunk[def-1][(def-1) - a];

			//		nleft[a]=Chunk[(def-1) - a][0];

			//	nright[a]=Chunk[(def-1) - a][def-1];
		}

		points=new float[def*def*2*3*3];                          
		ByteBuffer verts = ByteBuffer.allocate(points.length*4);
		ByteBuffer indes = ByteBuffer.allocate((points.length/3)*4);
		int i2 = 0;
		int i3 = 0;
		for(int a=0;a<Chunk.length-1;a++){
			for (int b=0;b<Chunk.length-1;b++){
				indes.putInt(i3);
				indes.putInt(i3+1);
				indes.putInt(i3+2);

				indes.putInt(i3+3);
				indes.putInt(i3+4);
				indes.putInt(i3+5);

				points[i2] = a + startx;   
				points[i2+1]=Chunk[a][b];				
				points[i2+2] = b + starty;				

				points[i2+3] = a + 1 + startx;
				points[i2+4]=Chunk[a+1][b];				
				points[i2+5] = b + starty;

				points[i2+6] = a + startx;
				points[i2+7]=Chunk[a][b+1];				
				points[i2+8] = b + 1 + starty;
				//----------------------------------	
				points[i2+9] = a + 1 + startx;
				points[i2+10]=Chunk[a + 1][b + 1];				
				points[i2+11] = b + starty + 1;

				points[i2+12] = a + 1 + startx;
				points[i2+13]=Chunk[a + 1][b];				
				points[i2+14] = b + starty;

				points[i2+15] = a + startx;
				points[i2+16]=Chunk[a][b + 1];				
				points[i2+17] = b +  1 + starty;

				i2+=18;
				i3+=6;
			}
		}
		//System.out.println(i3);
		//	ObjectArrayList<Vector3f> tris = new ObjectArrayList<Vector3f>(points.length/3);

		for(int a=0;a<points.length;a+=1){
			//		System.out.println(points[a]);	
			verts.putFloat(points[a]);
			points[a] *= mag;	

		} 

		///	for(int i=0;i<verts.capacity();i+=4){
		///	//	System.out.println(verts.getFloat(i));
		//	verts.putFloat(i, verts.getFloat(i)*mag);
		//	
		//	}

		int startz = starty;
		vertPart = BufferUtils.createFloatBuffer(points.length);
		vertPart.put(points);
		vertPart.rewind();

		float defp2 =Chunk.length/4;//size of the terrain
		texPart = BufferUtils.createFloatBuffer(Chunk.length*Chunk.length*12);
		float test = (float) (defp2*2/defp2);
		for (int f=0;f<Chunk.length-1;f++){
			for (int g=0;g<Chunk.length-1;g++){
				texPart.put((startx*mag+(f+1)*mag/defp2)*test);
				texPart.put((startz*mag+g*mag/defp2)*test);

				texPart.put((startx*mag+(f*mag)/defp2)*test);
				texPart.put((startz*mag+(g*mag)/defp2)*test);	

				texPart.put((startx*mag+f*mag/defp2)*test);
				texPart.put((startz*mag+(g+1)*mag/defp2)*test);


				texPart.put((startx*mag+(f+1)*mag/defp2)*test);
				texPart.put((startz*mag+g*mag/defp2)*test);

				texPart.put((startx*mag+(f+1)*mag/defp2)*test);
				texPart.put((startz*mag+((g+1)*mag)/defp2)*test);

				texPart.put((startx*mag+f*mag/defp2)*test);
				texPart.put((startz*mag+(g+1)*mag/defp2)*test);

			}
		}
		texPart.rewind();
		normalPart = BufferUtils.createFloatBuffer(Chunk.length*Chunk.length*6*3);
		/*
		for (int a=0;a<Chunk.length-1;a++){
			for (int b=0;b<Chunk.length-1;b++){
				Vector3f p1 =new Vector3f(a + startx,Chunk[a][b],b + starty);				
				Vector3f p2 =new Vector3f(a + 1 + startx,Chunk[a+1][b],b + starty);
				Vector3f p3 =new Vector3f(a + startx,Chunk[a][b+1],b + 1 + starty);
				normalPart.put(Utils.asFloatBuffer(calcTriNorm(p1, p2, p3)));
				normalPart.put(Utils.asFloatBuffer(calcTriNorm(p2, p1, p3)));
				normalPart.put(Utils.asFloatBuffer(calcTriNorm(p3, p2, p1)));
				//----------------------------------
				p1 = new Vector3f(a + 1 + startx,Chunk[a+1][b+1],b + starty + 1);
				p2 = new Vector3f(a + 1 + startx,Chunk[a+1][b],b + starty);
				p3 = new Vector3f(a + startx,Chunk[a][b+1],b + starty + 1);
				normalPart.put(Utils.asFloatBuffer(calcTriNorm(p1, p2, p3)));
				normalPart.put(Utils.asFloatBuffer(calcTriNorm(p2, p1, p3)));
				normalPart.put(Utils.asFloatBuffer(calcTriNorm(p3, p2, p1)));
			}
		}

		 */

		for(int i=0;i<points.length;i+=9){	
			if((i & 1) == 1) {
				Vector3f p1 =new Vector3f(points[i],points[i+1],points[i+2]);				
				Vector3f p2 =new Vector3f(points[i+3],points[i+4],points[i+5]);
				Vector3f p3 =new Vector3f(points[i+6],points[i+7],points[i+8]);
				normalPart.put(Utils.asFloatBuffer(calcTriNorm(p1,p2,p3)));
				normalPart.put(Utils.asFloatBuffer(calcTriNorm(p3,p1,p2)));
				normalPart.put(Utils.asFloatBuffer(calcTriNorm(p2,p3,p1)));
			}else{
				Vector3f p1 =new Vector3f(points[i],points[i+1],points[i+2]);				
				Vector3f p2 =new Vector3f(points[i+3],points[i+4],points[i+5]);
				Vector3f p3 =new Vector3f(points[i+6],points[i+7],points[i+8]);
				normalPart.put(Utils.asFloatBuffer(calcTriNormO(p1,p2,p3)));
				normalPart.put(Utils.asFloatBuffer(calcTriNormO(p3,p1,p2)));
				normalPart.put(Utils.asFloatBuffer(calcTriNormO(p2,p3,p1)));
			}
			//	normalPart.put(Utils.asFloatBuffer(calcTriNorm(p2,p3,p1)));
			//	normalPart.put(Utils.asFloatBuffer(calcTriNorm(p2,p3,p1)));
			//	normalPart.put(Utils.asFloatBuffer(calcTriNorm(p2,p3,p1)));
			//	normalPart.put(Utils.asFloatBuffer(calcTriNorm(p3,p1,p2)));
			//	normalPart.put(Utils.asFloatBuffer(calcTriNorm(p3,p1,p2)));
			//	normalPart.put(Utils.asFloatBuffer(calcTriNorm(p3,p1,p2)));
		}
		

		/*
		for(int i=0;i<points.length;i+=3){
			if(i >= points.length-3){
				Vector3f current = new Vector3f(points[i],points[i+1],points[i+2]);
				Vector3f next = new Vector3f(points[i-3],points[i-2],points[i-1]);
				Vector3f normal = new Vector3f();
				normal.x = (current.y*next.z) - (current.z*current.y);
				normal.y = (current.z*next.x) - (current.x*current.z);
				normal.z = (current.x*next.y) - (current.y*current.x);
				normalPart.put(Utils.asFloatBuffer(normal));				
			}else{

		}
		 */
		// 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 
		normalPart.rewind();

		TriangleIndexVertexArray tiva = new TriangleIndexVertexArray(points.length/9, indes, 3*4, points.length/3, verts, 4*3);
		tiva.setScaling(new Vector3f(mag,mag,mag));
		TriangleMeshShape tms = new BvhTriangleMeshShape(tiva, true);



		collisionShape = tms;
		return Chunk;
	}
	Vector3f calcTriNorm(Vector3f p1, Vector3f p2, Vector3f p3){	
		Vector3f U = new Vector3f(p2);
		Vector3f V = new Vector3f(p3);
		U.sub(p1);
		V.sub(p1);
		Vector3f normal = new Vector3f();
		//	normal.x = (U.y*V.z) - (U.z*U.y);
		//	normal.y = (U.z*V.x) - (U.x*U.z);
		//	normal.z = (U.x*V.y) - (U.y*U.x);
		normal.cross(U, V);
		return normal;		
	}
	Vector3f calcTriNormO(Vector3f p1, Vector3f p2, Vector3f p3){	
		Vector3f U = new Vector3f(p2);
		Vector3f V = new Vector3f(p3);
		U.sub(p1);
		V.sub(p1);
		Vector3f normal = new Vector3f();
		//	normal.x = (U.y*V.z) - (U.z*U.y);
		//	normal.y = (U.z*V.x) - (U.x*U.z);
		//	normal.z = (U.x*V.y) - (U.y*U.x);
		normal.cross(V, U);
		return normal;		
	}

	public float[][] smooth(float[][] Chunk){
		for(int a=1;a<Chunk.length-1;a++){
			for(int b=1;b<Chunk.length-1;b++){
				float avg = Chunk[a][b];
				if(a>0){
					if(a<Chunk.length-1){
						if(b>0){
							if(b<Chunk.length-1){
								avg = (Chunk[a-1][b]+Chunk[a+1][b]+Chunk[a][b-1]+Chunk[a][b+1])/4;
							}
							else avg = (Chunk[a-1][b]+Chunk[a+1][b]+Chunk[a][b-1])/3;
						}
						else avg = (Chunk[a-1][b]+Chunk[a+1][b]+Chunk[a][b+1])/3;
					}
					else if(b>0){
						if(b<Chunk.length-1){
							avg = (Chunk[a-1][b]+Chunk[a][b-1]+Chunk[a][b+1])/3;
						}
						else avg = (Chunk[a-1][b]+Chunk[a][b-1])/2;
					}
					else avg = (Chunk[a][b+1]+Chunk[a-1][b])/3;

				}
				else if(b>0){
					if(b<Chunk.length-1){
						avg = (Chunk[a+1][b]+Chunk[a][b-1]+Chunk[a][b+1])/3;
					}
					else avg = (Chunk[a+1][b]+Chunk[a][b-1])/2;
				}
				else avg = (Chunk[a+1][b]+Chunk[a][b+1])/3;

				float dif = avg-Chunk[a][b];
				Chunk[a][b]+=dif;
			} 
		}

		return Chunk;
	}


	public CollisionShape getCollisionShape(){
		return collisionShape;
	}

}