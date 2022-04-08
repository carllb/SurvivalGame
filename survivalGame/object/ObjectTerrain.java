
package survivalGame.object;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import survivalGame.Player;
import survivalGame.Biome.BiomeDesert;
import survivalGame.Biome.BiomeHills;
import survivalGame.Biome.BiomeSpawn;
import engine.Biome;
import engine.Point3d;
import engine.TerrainManager;

public class ObjectTerrain extends Object{

	TerrainManager manager;
	Player player;
	boolean inited = false;
	File imageA, imageB;
	int chunkCount = 0;
	Biome biome = new BiomeSpawn();
	Biome[] biomeTypes = new Biome[3];
	float[] chance = new float[biomeTypes.length];
	//Need to save this to file to preserve seed!
	ArrayList<Biome> biomeorder = new ArrayList<Biome>();
	int max = 40;
	Random random;
	int place = -1;
	
	public ObjectTerrain(Player player,File imageA,File imageB, Random random) {
		super(new TerrainManager(random));
		manager = (TerrainManager) vObject;
		this.player = player;
		this.imageA = imageA;
		this.imageB = imageB;
		this.random = random;
		fillBiomeTypes();
		genBiomeOrder();		
	}
	Point3d locA;
	public void tick(){
		if(!inited){
			manager.addToPhysWorld();
			manager.Initgen(6, imageA, imageB,biome,100);
			locA = player.getLocation();
			inited = true;
		}
		if(chunkCount >= 15) biome = chooseBiome();
		Point3d locB = player.getLocation();
		if(Math.sqrt(Math.pow(locA.x-locB.x,2) +  Math.pow(locA.y-locB.y,2) + Math.pow(locA.z-locB.z,2)) > 30);{
			chunkCount += manager.update((float)-locB.x,(float) -locB.z,biome);
			locA = player.getLocation();			
		}		
	}
	void fillBiomeTypes(){
		biomeTypes[0] = new BiomeSpawn();
		biomeTypes[1] = new BiomeDesert();
		biomeTypes[2] = new BiomeHills();
		chance[0] = .0f;
		chance[1] = 1.f;
		chance[2] = 0.0f;
	}
	void genBiomeOrder(){
		for(int i=0;i<max;i++){			
			for(int i2=0;i2<biomeTypes.length;i2++){
				if(((float)random.nextFloat()) <= chance[i2]){
					biomeorder.add(biomeTypes[i2]);
				}
			}
		}
	}
	Biome chooseBiome(){
		if(place >= (biomeorder.size()-1)){
			place = -1;
		}
		place ++;
		return biomeorder.get(place);
	}
	
	@Override
	public void addToPhys(){}
}
