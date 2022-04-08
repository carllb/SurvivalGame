package engine.menu;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import org.lwjgl.input.Mouse;

import engine.Point2d;
import engine.Render;

public class MenuButton {
	ActionListener al0;
	ActionListener al1;
	ActionListener al2;
	ActionListener al3;
	String name;
	BufferedImage image;	
	int x, y, width, height;
	Menu menu = null;
	boolean pressedRight = false;
	boolean pressedLeft = false;
	
	
	public static final int BUTTON_CLICKED_LEFT = 0;
	public static final int BUTTON_CLICKED_RIGHT = 1;
	public static final int MOUSE_OVER = 2;
	public static final int MOUSE_LEAVE = 3;
	
	
	public MenuButton(String name,BufferedImage image,int x, int y){
		this.name = name;
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
		this.x = x;
		this.y = y;
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
		menu.needUpdate = true;
	}
	
	public Point2d getLocation(){
		return new Point2d(this.x,this.y);
	}
	
	public void setImage(BufferedImage image){
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
		menu.needUpdate = true;
	}
	
	public void setImage(BufferedImage image,int width,int height){
		this.image = image;
		this.width = width;
		this.height = height;
		menu.needUpdate = true;
	}
	
	public void addedToMenu(Menu menu){
		this.menu = menu;
		menu.needUpdate = true;
	}
	
	public void setActionListener(ActionListener eventHandler,int EventType){
		switch (EventType) {
		case 0:
			al0 = eventHandler;			
			break;
		case 1:
			al1 = eventHandler;
			break;
		case 2:
			al2 = eventHandler;
			break;
		case 3:
			al3 = eventHandler;
			break;
		default:
			break;
		} 		
	}
	
	public void draw(Graphics g){
		g.drawImage(image, x, y, width, height, null);	
	}
	
	public void tick(int mouseX, int mouseY){
		int mx = (Mouse.getX()*1000)/Render.width;				
		int my =(Render.height-Mouse.getY())*1000/Render.height;
		
	//	System.out.println("mX: " + mx + " mY: " + my);
		
		if(al0 != null){
			if(Mouse.isButtonDown(0)){				
				if(!pressedLeft){
					pressedLeft = true;
					if(mx > x && mx < (x+width)){
						if(my > y && my < (y+height)){
							al0.actionPerformed(new MenuButtonClickedEvent(this, 0, name));
						}
					}
				}				
			}else{
				pressedLeft = false;
			}
		}
		if(al1 != null){
			if(Mouse.isButtonDown(1)){	
				if(!pressedRight){
					pressedRight = true;
					if(mx > x && mx < (x+width)){
						if(my > y && my < (y+height)){
							al1.actionPerformed(new MenuButtonClickedEvent(this, 1, name));
						}
					}
				}			
			}else{
				pressedRight = false;
			}
		}
		if(al2 != null){
			if(mx > x && mx < (x+width)){
				if(my > y && my < (y+height)){
					al0.actionPerformed(new MenuButtonClickedEvent(this, 0, name));
				}
			}
		}
		if(al3 != null){
			if(!(mx > x) && !(mx < (x+width))){
				if(!(my > y) && !(my < (y+height))){
					al0.actionPerformed(new MenuButtonClickedEvent(this, 0, name));
				}
			}
		}
	}

}
