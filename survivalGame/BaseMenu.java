package survivalGame;

import engine.Color4f;
import engine.World;
import engine.menu.Menu;

public class BaseMenu {
	Menu menu;
	World world;
	
	
	public BaseMenu(World w){
		world = w;
		menu = new Menu(new Color4f(0,1,0,0),world);
	}
	public void tick(){}
	
}
