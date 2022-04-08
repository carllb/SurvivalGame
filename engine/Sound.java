package engine;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_LOOPING;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alListener3f;
import static org.lwjgl.openal.AL10.alSource3f;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcei;

import javax.vecmath.Vector3f;

import org.lwjgl.util.WaveData;

public class Sound {

	WaveData wd;
	int soundBufferID = 0;
	int sourceID = 0;
	
	public Sound(WaveData wd){
		
		this.wd = wd;
		sourceID = alGenSources();
		soundBufferID = alGenBuffers();
		alBufferData(soundBufferID, wd.format, wd.data, wd.samplerate);
		alSourcei(sourceID, AL_BUFFER, soundBufferID);  		
	}
	
	public void move(Vector3f newLocation){
		alSource3f(sourceID, AL_POSITION, newLocation.x, newLocation.y, newLocation.z);
	}
	public void setVelocity(Vector3f velocity){
		alSource3f(sourceID, AL_VELOCITY, velocity.x, velocity.y, velocity.z);
	}
	
	public void playSound(boolean loop){		
		int l = 0;
		if(loop){
			l = 1;
		}
		alSourcei(sourceID, AL_LOOPING, l);
		alSourcePlay(sourceID);
	}
	public void stopSound(){
		alSourceStop(sourceID);
	}
	
	public static void setListenerLocation(Vector3f listenerLocation){ 
		alListener3f(AL_POSITION, -listenerLocation.x, listenerLocation.y, -listenerLocation.z);
	}
	
	public static void setListenerVelocity(Vector3f listenerVelocity){ 
		alListener3f(AL_POSITION, listenerVelocity.x, listenerVelocity.y, listenerVelocity.z);
	}
}
