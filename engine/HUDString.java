package engine;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class HUDString implements HUDElement{

	boolean change = true;
	String string;
	int x, y;
	HUD hud;
	Color color = new Color(0,1,0,0.5f);
	
	public HUDString(String s,int x,int y){
		this.string = s;
		this.x = x;
		this.y = y;
	}
	public void setColor(Color c){	
		this.color = new Color(c.getRed()/255, c.getGreen()/255, c.getBlue()/255, 0.5f);		
		if(hud != null) hud.update = true;
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
		g.setFont(new Font("test", Font.PLAIN, 50));
		g.setColor(color);
		g.drawString(string, x+100, y+100);
	}
	
	public void changeString(String newString){
		string = newString;
		if(hud != null){
			hud.update = true;
		}
	}
	
}
