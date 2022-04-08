package engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;


public class AnimationPlayer implements ActionListener{
	
	ArrayList<Integer> animations = new ArrayList<Integer>();
	boolean aModel = false;
	AnimatedModel am;
	AnimationSet as;
	boolean playing = false;
	Timer t; 
	int currentFrame = 0;
	int currentAnimation;
	boolean loop;
	
	
	public AnimationPlayer(AnimatedModel am){
		this.am = am;
		aModel = true;
	}
	
	public AnimationPlayer(AnimationSet as){
		this.as = as;
	}
	
	public int defineAnimation(int start,int finish){		
		int s = animations.size();
		animations.add(start);
		animations.add(finish);
		return s;
	}
	
	public void playAnimation(int id,int fps,boolean loop){		
		if(playing) return;
		playing = true;
		currentFrame = animations.get(currentAnimation);
		playing = true;
		currentAnimation = id;
		t = new Timer((int)((1f/fps)*1000f), this);;
		t.start();
		this.loop = loop;
	}
	
	public void stopAnimation(){
		if(t != null){
			t.stop();
		}
		playing = false;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Timer temp = (Timer) arg0.getSource();
		if(currentFrame >= animations.get((currentAnimation*2)+1)){
			if(!loop){
				temp.stop();
				playing = false;
			}else{
				currentFrame = animations.get(currentAnimation*2);
			}			
		}
		if(aModel){			
			am.currentFrame = currentFrame;
		}else{
			if(currentFrame >= animations.get((currentAnimation*2)+1)){
				if(!loop){
					currentFrame = animations.get(currentAnimation*2);
					temp.stop();
					playing = false;	
				}else{
					currentFrame = animations.get(currentAnimation*2);
				}
			}
			Frame f = as.getFrame(currentFrame);
			if(f.aModel){				
				AnimatedModel am = f.am;
				am.currentFrame = f.amFrame;
				am.location.x = f.x;
				am.location.y = f.y;
				am.location.z = f.z;
				am.rot.x = (float) f.rx;
				am.rot.y = (float) f.ry;
				am.rot.z = (float) f.rz;				
			}else{
				Model m = f.m;
				m.location.x = f.x;
				m.location.y = f.y;
				m.location.z = f.z;
				m.rot.x = (float) f.rx;
				m.rot.y = (float) f.ry;
				m.rot.z = (float) f.rz;	
			}
		}
		currentFrame++;
	}

}
