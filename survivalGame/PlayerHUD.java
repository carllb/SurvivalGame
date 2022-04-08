package survivalGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.HUD;
import engine.HUDElement;
import engine.HUDImage;

public class PlayerHUD {

	HUD hud;	
	HUDImage stamina;
	BufferedImage staminaImage;
	Graphics gStamina;
	
	HUDImage health;
	BufferedImage healthImage;
	Graphics gHealth;
	
	
	Player player;
	
	public PlayerHUD(Player player,float ratio){	
		hud = new HUD(ratio);
		this.player = player;		
		BufferedImage crossHairImage = null;
		try {
			crossHairImage = ImageIO.read(new File("SurvivalGame/res/crosshair.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		HUDElement crossHair = new HUDImage(crossHairImage, hud.getWidth()/2-30, hud.getHeight()/2-30);
		hud.addElement(crossHair);		
		staminaImage = new BufferedImage(300, 20, BufferedImage.TYPE_4BYTE_ABGR);		
		stamina = new HUDImage(staminaImage, 0, hud.getHeight() - (staminaImage.getHeight() + 10));
		gStamina = staminaImage.getGraphics();
		gStamina.setColor(new Color(0,255,255));
		gStamina.drawRect(1, 1, staminaImage.getWidth()-1,staminaImage.getHeight()-1);
		gStamina.setColor(new Color(0,225,0));
		gStamina.fillRect(4, 4,  staminaImage.getWidth()-4,staminaImage.getHeight()-4);
		hud.addElement(stamina);
		
		healthImage = new BufferedImage(300, 20, BufferedImage.TYPE_4BYTE_ABGR);		
		health = new HUDImage(healthImage, hud.getWidth() - (healthImage.getHeight() + 300), hud.getHeight() - (healthImage.getHeight() + 10));
		gHealth = healthImage.getGraphics();
		gHealth.setColor(new Color(0,255,255));
		gHealth.drawRect(1, 1, healthImage.getWidth()-1,healthImage.getHeight()-1);
		gHealth.setColor(new Color(255,0,0));
		gHealth.fillRect(4, 4,  healthImage.getWidth()-4,healthImage.getHeight()-4);
		hud.addElement(health);		
		player.level.renderWorld.setHUD(hud);
	}
	
	public void tick(){		
		int staminaI = player.stamina;
		BufferedImage temp = new BufferedImage(staminaImage.getWidth(), staminaImage.getWidth(),BufferedImage.TYPE_4BYTE_ABGR);
		int width = (int)(.000296f * ((float) staminaI));
		Graphics gTemp = temp.getGraphics();
		gTemp.setColor(new Color(0,255,255));
		gTemp.drawRect(1, 1, staminaImage.getWidth()-1,staminaImage.getHeight()-1);
		gTemp.setColor(new Color(0,225,0));
		gTemp.fillRect(4, 4,  width,staminaImage.getHeight()-4);
		gStamina.setColor(new Color(0,0,0,1));
		gStamina.fillRect(0, 0, 300,20);
		gStamina.drawImage(temp, 0, 0, null);	
		
		int healthI = player.health;
		BufferedImage temp2 = new BufferedImage(healthImage.getWidth(), healthImage.getWidth(),BufferedImage.TYPE_4BYTE_ABGR);
		int width2 = (int)(.296f * ((float) healthI));
		Graphics gTemp2 = temp2.getGraphics();
		gTemp2.setColor(new Color(0,255,255));
		gTemp2.drawRect(1, 1, healthImage.getWidth()-1,healthImage.getHeight()-1);
		gTemp2.setColor(new Color(255,0,0));
		gTemp2.fillRect(4, 4,  width2,healthImage.getHeight()-4);
		gHealth.setColor(new Color(0,0,0,1));
		gHealth.fillRect(0, 0, 300,20);
		gHealth.drawImage(temp2, 0, 0, null);		
		health.update();
	}
	
	
}
