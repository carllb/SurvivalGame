package survivalGame.entity;

import survivalGame.Level;
import survivalGame.Player;
import survivalGame.item.Item;
import engine.VisibleObject;

public class EntityPlayer extends EntityMob{

	public Player player;
	
	public EntityPlayer(VisibleObject vo, Level level,Player player) {
		super(null, level, null);		
		this.player = player;
	}
	@Override
	public void equip(Item item) {
		player.equip(item);
	}
}
