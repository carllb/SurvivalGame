package engine.menu;

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
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
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

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import engine.BasicGame;
import engine.Color4f;
import engine.Render;
import engine.World;


public class Menu implements Runnable{
	
	boolean firstTime = true;
	boolean NeedRenderUpdate = true;
	boolean needUpdate = true;
	int texID = 0;
	int height = 1000;
	int width = 1000;
	int pWidth = width;
	int pHeight = height;
	float ratio = 0;
	IntBuffer texture;
	Graphics graphics;
	BufferedImage image;
	ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();
	ArrayList<MenuTextField> menuTextFields = new ArrayList<MenuTextField>();
	Color4f backGround;
	World world;
	
	public Menu( Color4f backGround, World world){
		this.world = world;
		NeedRenderUpdate = false;
		ratio = ((float) Render.width/ (float) Render.height);
		texture = (ByteBuffer.allocateDirect(width * height * 16)).asIntBuffer();			
		image = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
		graphics = image.createGraphics();
		this.backGround = backGround;
		redrawMenu();		
	}
	public void changeBackGoundColor(Color4f newColor){
		backGround = newColor;
		redrawMenu();
	}
	
	void redrawMenu(){
		int[] i2 = new int[width*height];
		for(int j=0;j<i2.length;j++){
			i2[j] = (new Color(backGround.R,backGround.G,backGround.B,backGround.A).getRGB());
		}
		image.setRGB(0, 0, pHeight, pWidth, i2, 0, pHeight);	
		needUpdate = true;
	}

	public void addButton(MenuButton b){
		menuButtons.add(b);
		menuButtons.get(menuButtons.size()-1).addedToMenu(this);		
		redrawMenu();
	}
	
	public void removeButton(MenuButton b){
		menuButtons.remove(b);
		redrawMenu();
	}
	
	public void drawElements(){
		System.out.println("test");
		for(int i = 0;i<menuButtons.size();i++){
			menuButtons.get(i).draw(graphics);
		}
		for(int i = 0;i<menuTextFields.size();i++){
			menuTextFields.get(i).draw(graphics);
		}
	}	
	
	public void render(){
		if(firstTime){
			texID = glGenTextures();
			firstTime = false;
		}		
		if(NeedRenderUpdate){					
			glBindTexture(GL_TEXTURE_2D, texID);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);					
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);		
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);	
			texture.rewind();
	//		System.out.println(texture.capacity());
			glTexImage2D(GL_TEXTURE_2D, 0,GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_INT, texture);
			NeedRenderUpdate = false;				
		}	
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
		
		glPushMatrix();
		glBindTexture(GL_TEXTURE_2D, texID);
		glTranslatef(0, 0f, -1);
		glColor4f(0, 0, 0,0);
		
		glBegin(GL_QUADS);
		
		glTexCoord2f(0, 1);		
		glVertex2f(-1*ratio, -1);		
		
		glTexCoord2f(0, 0);		
		glVertex2f(-1*ratio, 1);		
		
		glTexCoord2f(1, 0);		
		glVertex2f(1*ratio, 1);		
		
		glTexCoord2f(1, 1);		
		glVertex2f(1*ratio, -1);		
		
		glEnd();
		glPopMatrix();
		
		
	/*  glPushMatrix();		
	//	glDisable(GL_CULL_FACE);
		glDisable(GL_LIGHTING);
		glTranslatef(0, 0f, -0.11f);
		glRotatef(270, 0, 0, 1);
		glBindTexture(GL_TEXTURE_2D, texID);		
		
		glBegin(GL_QUADS);		
		
		glTexCoord2f(0, 0);
		glVertex2f(-(1f/9f),-height/10500f);	
		glNormal3f(0, 0, -1);
		
		glTexCoord2f(1, 0);
		glVertex2f((1f/9f),-height/10500f);
		glNormal3f(1, 0, -1);
		
		glTexCoord2f(1, 1);		
		glVertex2f( (1f/9f),height/10500f);	
		glNormal3f(1, 1, -1);
		
		glTexCoord2f(0,1);
		glVertex2f(-(1f/9f),height/10500f);
		glNormal3f(0, 1, -1);
		
		
		glEnd();
	//	glEnable(GL_LIGHTING);
	//	glEnable(GL_CULL_FACE);
		glPopMatrix();		
		*/
	}
	
	void updateByteBuffer(){			
		texture.clear();
		int[] pixels = new int[width*height];		
		PixelGrabber pg = new PixelGrabber(image,0,0,width,height,pixels,0,height);
		ColorModel cm = pg.getColorModel();
		try{
			pg.grabPixels();
		}catch(Exception e){
			e.printStackTrace();
		}
		int temp = 0;
		for(int i=0;i<pixels.length;i++){
			texture.put(cm.getRed(pixels[i]));
			texture.put(cm.getGreen(pixels[i]));
			texture.put(cm.getBlue(pixels[i]));
			texture.put(cm.getAlpha(pixels[i]));
			temp = i;
		}
		texture.flip();			
		NeedRenderUpdate = true;
		while(NeedRenderUpdate){
			try{
				Thread.sleep(10);
			}catch(Exception e){ 
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run(){
		while(BasicGame.isRunning && world.getCurrentMenu() == this){	
			if(needUpdate){
				redrawMenu();
				drawElements();
				updateByteBuffer();
				needUpdate = false;		
			}			
			for(int i=0;i<menuButtons.size();i++){
				if(Keyboard.isCreated())
				menuButtons.get(i).tick(Mouse.getX(), Mouse.getY());
			}
		}
	}
	
	
	
}
