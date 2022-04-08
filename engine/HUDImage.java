package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class HUDImage implements HUDElement{

	boolean change = true;
	BufferedImage image;
	int x, y;
	HUD hud;
	Color color = new Color(0,1,0,0.5f);
	
	public HUDImage(BufferedImage image,int x,int y){
		this.image = image;
		this.x = x;
		this.y = y;
	}
	public void setColor(Color c){
		this.color = c;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	
	public void move(int newX, int newY){
		x = newX;
		y = newY;
		if(hud != null){
			hud.update = true;
		}
	}
	@Override
	public void addedToHUD(HUD hud) {		
		this.hud = hud;
		hud.update = true;
	}
	
	@Override
	public void render(Graphics g) {
		
		g.setColor(color);
		g.drawImage(image, x, y, null);
		
	}
	
	public void changeImage(BufferedImage newImage){
		image = newImage;
		if(hud != null){
			hud.update = true;
		}
	}
	
	public void update(){
		hud.update = true;
	}
	
}
