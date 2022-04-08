package survivalGame;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import engine.World;
import engine.menu.MenuButton;

public class MenuLoading extends BaseMenu {
	public MenuLoading(World w){
		super(w);
		BufferedImage temp = new BufferedImage(1000,1000,BufferedImage.TYPE_3BYTE_BGR);
		char[] data = "Loading...".toCharArray();
		Graphics g = temp.getGraphics();
		g.setFont(new Font("rajkdsa",Font.BOLD,30));
		g.drawChars(data, 0, data.length, 425, 500);
		menu.addButton(new MenuButton("loading", temp, 0, 0));
	}

}
