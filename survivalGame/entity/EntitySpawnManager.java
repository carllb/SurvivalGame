package survivalGame.entity;

import java.util.ArrayList;

import survivalGame.Level;

public class EntitySpawnManager {
	private ArrayList<SpawnedEntity>  spawnedEntitys= new ArrayList<SpawnedEntity>();
	ArrayList<Entity> entitiesToBeRemoved = new ArrayList<Entity>();
	Level l;
	
	public EntitySpawnManager(Level l){
		this.l = l;
		l.esm = this;
	}
	
	public void tick(){
		for(int i=0;i<spawnedEntitys.size();i++){
		SpawnedEntity s = spawnedEntitys.get(i);
			if(s.oneSecondTick()){
				l.removeEntity(s.e);
				despawnEntity(s.e);
			}
		}
		for(int i =0;i<entitiesToBeRemoved.size();i++){
			Entity e = entitiesToBeRemoved.get(i);
			for(int i2=0;i2< spawnedEntitys.size();i2++){
				SpawnedEntity s = spawnedEntitys.get(i2);
				if(s.e == e){
					l.removeEntity(e);
					spawnedEntitys.remove(s);
					entitiesToBeRemoved.remove(e);
				}
			}
		}
	}
	
	public void spawnEntity(Entity e, int life){
		SpawnedEntity se = new SpawnedEntity(e, life);
		spawnedEntitys.add(se);
		l.addEntity(e);
	} 
	
	public void despawnEntity(Entity e){
		entitiesToBeRemoved.add(e);
	}
}

class SpawnedEntity{
	Entity e;
	int life;
	int eTime = 0;
	
	public SpawnedEntity(Entity e,int life) {
		this.e = e;
		this.life = life;
	}
	
	public boolean oneSecondTick(){
		if(eTime >= life && life != 0){
			return true;
		}
		eTime++;
		
		return false;
	}
}
