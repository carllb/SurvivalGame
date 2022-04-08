package survivalGame;

import java.util.ArrayList;

import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.dispatch.CollisionWorld.LocalRayResult;
import com.bulletphysics.collision.dispatch.CollisionWorld.RayResultCallback;

public class Callback extends RayResultCallback{
	
	CollisionWorld collWorld;
	ArrayList<LocalRayResult> results = new ArrayList<LocalRayResult>();
	
	public Callback(CollisionWorld cw){
		collWorld = cw;
	}
	
	@Override
	public float addSingleResult(LocalRayResult rayResult,
			boolean normalInWorldSpace) {
		results.add(rayResult);
		return 1;
	}
	
	public ArrayList<LocalRayResult> getResults(){
		return results;
		
	}

}
