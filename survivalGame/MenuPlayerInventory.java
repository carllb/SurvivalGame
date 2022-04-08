package survivalGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import survivalGame.entity.EntityPlayer;
import survivalGame.item.Item;
import engine.Point2d;
import engine.World;
import engine.menu.MenuButton;

public class MenuPlayerInventory extends BaseMenu implements ActionListener{

	ArrayList<MenuButton> things = new ArrayList<MenuButton>();
	BufferedImage menuScrollButtonImage;
	MenuButton scroll;
	MenuButton[] itemButtons;
	InventoryItem[] items;
	EntityPlayer player;
	MenuButton equiped;
	public MenuPlayerInventory(World w, EntityPlayer player) {
		super(w);
		this.player = player;
		
		items = player.player.inventory.getArray();
		itemButtons = new MenuButton[items.length]; 
		menuScrollButtonImage = new BufferedImage(20,1000,BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage eq = new BufferedImage(20,20,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = eq.getGraphics();
		g.setColor(new Color(0,0,0,0));
		g.fillRect(0, 0, 20, 20);
		g.setColor(new Color(0,255,0,255));
		g.drawPolygon(new Polygon(new int[]{0,10,20}, new int[]{0,10,0}, 3));
		equiped = new MenuButton("q", eq, 0, 0);
		Graphics temp = menuScrollButtonImage.getGraphics();
		temp.setColor(new Color(255,0,0,0));
		temp.fillRect(0, 0, 20, 100);
		scroll = new MenuButton("Scroll",menuScrollButtonImage,980,0);
		menu.addButton(scroll);
		for(int i2=0;i2<items.length;i2++){
			Item item = items[i2].item;
			BufferedImage iTemp = new BufferedImage(300, 100, BufferedImage.TYPE_3BYTE_BGR);
			Graphics gTemp = iTemp.getGraphics();
			gTemp.setColor(new Color(255,255,255));
			gTemp.fillRect(0, 0, 300, 100);
			gTemp.setColor(new Color(255,0,0));
			gTemp.drawRect(1, 1, 298, 98);
			char[] cTemp = (items[i2].quantity + "  " + item.name).toCharArray();
			gTemp.setFont(new Font("temp", Font.TYPE1_FONT, 30));
			gTemp.drawChars(cTemp, 0, cTemp.length, 10, 50);
			MenuButton b = new MenuButton(item.name,iTemp,0, (i2*110));
			things.add(b);
			menu.addButton(b);
			b.setActionListener(this, MenuButton.BUTTON_CLICKED_LEFT);
			itemButtons[i2] = b;
		}
	}
	
	
	@Override
	public void tick() {
		if(!Keyboard.isCreated()) return;
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			for(int i=0;i<things.size();i++){
				MenuButton mb = things.get(i);
				Point2d loc = mb.getLocation();
				System.out.println(loc.y);
				loc.y = loc.y + 4;
				mb.setLocation((int)loc.x,(int) loc.y);				
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			for(int i=0;i<things.size();i++){
				MenuButton mb = things.get(i);
				Point2d loc = mb.getLocation();
				System.out.println(loc.y);
				loc.y = loc.y - 4;
				mb.setLocation((int)loc.x,(int) loc.y);				
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!(arg0.getSource() instanceof MenuButton)) return;
		MenuButton b =(MenuButton) arg0.getSource();
		for(int i=0;i<itemButtons.length;i++){
			if(b == itemButtons[i]){				
				items[i].item.equip(player);				
			}
		}
	}

	
}
