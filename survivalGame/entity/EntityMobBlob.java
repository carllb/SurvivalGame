package survivalGame.entity;

import survivalGame.Level;
import survivalGame.MobAIBlob;
import survivalGame.VisibleObjectHandler;

import com.bulletphysics.collision.shapes.SphereShape;

import engine.MobControler;

public class EntityMobBlob extends EntityMob{
	
	public EntityMobBlob(Level level) {
		super(VisibleObjectHandler.getVisableObject("Blob"), level,new MobAIBlob());
		controler = new MobControler(new SphereShape(4), level.renderWorld, 1f,this,"FRIEND",this);
	//	controler.setGravity(new Vector3f(0,10,0));
		mobAI.setControler(controler, level, 5);
		controler.setFriction(3);
	}	
}
