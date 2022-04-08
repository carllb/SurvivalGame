package engine;



import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_RGBA;
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
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class HUD implements Runnable{
	boolean readyRenderUpdate = false;
	public boolean update = true;
	boolean firstTime = true;
	IntBuffer texture;
	BufferedImage image;
	int width = 1000;
	int height;
	int texID;
	Graphics graphics;
	ArrayList<HUDElement> hudelements = new ArrayList<HUDElement>();	
	public World world;
	float ratio;
	
	public HUD(float ratio){
		height = (int) (width*ratio);
		this.ratio = ratio;
		texture = (ByteBuffer.allocateDirect(width * height * 16)).asIntBuffer();			
		image = new BufferedImage(height, width,BufferedImage.TYPE_INT_ARGB);
		graphics = image.createGraphics();
	}
	
	public void addElement(HUDElement hudelement){
		hudelements.add(hudelement);
		hudelement.addedToHUD(this);
	}
	
	
	void updateByteBuffer(){		
		
		texture.clear();
		int[] pixels = new int[width*height];		
		PixelGrabber pg = new PixelGrabber(image,0,0,height,width,pixels,0,height);
		ColorModel cm = image.getColorModel();
		try{
			pg.grabPixels();
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i=0;i<pixels.length;i++){
			texture.put(cm.getRed(pixels[i]));
			texture.put(cm.getGreen(pixels[i]));
			texture.put(cm.getBlue(pixels[i]));
			texture.put(cm.getAlpha(pixels[i]));			
		}
		texture.flip();			
		readyRenderUpdate = true;
		while(readyRenderUpdate){
			try{
				Thread.sleep(10);
			}catch(Exception e){ 
				e.printStackTrace();
			}
		}
	}
	public void run(){
		while(BasicGame.isRunning && world.hud == this){
			if(update){		
				graphics.setColor(new Color(0,0,0,0));
		//		graphics.clearRect(0, 0,image.getHeight(), image.getWidth());
				int[] i2 = new int[width*height];
				for(int j=0;j<i2.length;j++){
					i2[j] = (new Color(0,0,0,0).getRGB());
				}
				image.setRGB(0, 0, height, width, i2, 0, height);							
				for(int i=0;i<hudelements.size();i++){
					hudelements.get(i).render(graphics);
				}
				updateByteBuffer();
				update = false;
			}else{
				try{
					Thread.sleep(10);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}	
	public void render(){
		
		if(firstTime){
			texID = glGenTextures();
			firstTime = false;
		}
		
		if(readyRenderUpdate){			
			glBindTexture(GL_TEXTURE_2D, texID);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);					
			//glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);	
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);		
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);	
			glTexImage2D(GL_TEXTURE_2D, 0,GL_RGBA, height,width, 0, GL_RGBA, GL_UNSIGNED_INT, texture);
			readyRenderUpdate = false;				
		}			
		glBindTexture(GL_TEXTURE_2D, texID);
	//	glPixelStorei(GL_UNPACK_ALIGNMENT, 1);					
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);	
		
		glPushMatrix();		
	//	glEnable(GL_TEXTURE_2D);
	//	if(glIsEnabled(GL_LIGHTING)){
	//		glDisable(GL_LIGHTING);
	//	}
		glTranslatef(0, 0f, -1f);
		glRotatef(180, 0, 0, 1);
		glRotatef(180, 0, 1, 0);		
		
		glColor4f(0, 0 ,0,0);
		glBegin(GL_QUADS);	
		
		glTexCoord2f(0, 0);
		glVertex2f(-1*ratio,-1f);	
		glNormal3f(0, 0, -1);
		
		glTexCoord2f(1, 0);
		glVertex2f(1*ratio,-1f);
		glNormal3f(1, 0, -1);
		
		glTexCoord2f(1, 1);		
		glVertex2f( 1*ratio,1);	
		glNormal3f(1, 1, -1);
		
		glTexCoord2f(0,1);
		glVertex2f(-1*ratio,1);
		glNormal3f(0, 1, -1);
		
		glEnd();
	//	if(glIsEnabled(GL_LIGHTING)){
	//		glEnable(GL_LIGHTING);
	//	}
	//	glEnable(GL_CULL_FACE);
		glPopMatrix();
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}

}
