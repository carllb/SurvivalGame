package engine;

import java.awt.Graphics;

public interface HUDElement {
	public void render(Graphics g);
	
	public void addedToHUD(HUD hud);
	
}
