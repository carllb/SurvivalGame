package engine;

public class AnimationSet {
	
	boolean animatedmodel = false;
	Model m;
	AnimatedModel am;
	int length;
	double[] xVals;
	double[] yVals;
	double[] zVals;
	double[] rxVals;
	double[] ryVals;
	double[] rzVals;
	int[] amFrame = null;	
	
	
	public AnimationSet(AnimatedModel am,int length){
		animatedmodel = true;
		this.am = am;
		this.length = length;
	    xVals = new double[length];
		yVals = new double[length];
		zVals = new double[length];
		rxVals = new double[length];
		ryVals= new double[length];
		rzVals = new double[length];
		amFrame = new int[length];
	}
	
	public AnimationSet(Model m,int length){
		animatedmodel = false;
		this.m = m;
		this.length = length;
		xVals = new double[length];
		yVals = new double[length];
		zVals = new double[length];
		rxVals = new double[length];
		ryVals= new double[length];
		rzVals = new double[length];
	}	
	
	public void setFrame(Frame f,int frameID){
		xVals[frameID] = f.x;
		yVals[frameID] = f.y;
		zVals[frameID] = f.z;
		rxVals[frameID] = f.rx;
		ryVals[frameID] = f.ry;
		rzVals[frameID] = f.rz;
		if(amFrame != null){			
			amFrame[frameID] = f.amFrame;
		}	
				
	}
	
	public Frame getFrame(int frame){
		Frame f = new Frame(m);
		f.x = xVals[frame];
		f.y = yVals[frame];
		f.z = zVals[frame];
		f.rx = rxVals[frame];
		f.ry = ryVals[frame];
		f.rz = rzVals[frame];
		if(amFrame != null){
			f.aModel = true;
			f.am = am;
			f.amFrame = amFrame[frame];
		}else{
			f.aModel = false;
			f.m = m;
		}		
		return f;		
	}
}
