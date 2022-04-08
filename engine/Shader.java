package engine;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Shader {

	public int programID;
	private boolean loaded = false;
	private boolean run = true;
	private boolean firstLoad = false;
	File vertShader, fragShader;
	ArrayList<float[]> floatsToPass = new ArrayList<float[]>();
	ArrayList<String> varNames = new ArrayList<String>();


	public Shader(File vertShader, File fragShader){
		this.vertShader = vertShader;
		this.fragShader = fragShader;
	}

	public void loadShaders() throws Exception{
		loaded = true;
		if(!firstLoad){
			int shaderProgram = glCreateProgram();
			int vertexShader = glCreateShader(GL_VERTEX_SHADER);
			int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
			programID = shaderProgram;
			StringBuilder vertShadeSource = new StringBuilder();
			StringBuilder fragShadeSource = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(vertShader));
			String line;
			while ((line = reader.readLine()) != null){
				if(!line.contains("//"));
					vertShadeSource.append(line).append('\n');
			}
			reader = new BufferedReader(new FileReader(fragShader));
			while ((line = reader.readLine()) != null){
				if(!line.contains("//"));
					fragShadeSource.append(line).append('\n');
			}
			glShaderSource(vertexShader, vertShadeSource);
			glCompileShader(vertexShader);
			if(glGetShader(vertexShader, GL_COMPILE_STATUS) == GL_FALSE){
				System.err.println("VERTEX SHADER NOT COMPILED PROPERLY!");		
				System.err.println(glGetShaderInfoLog(vertexShader, 10000));
			}
			glShaderSource(fragmentShader, fragShadeSource);
			glCompileShader(fragmentShader);
			if(glGetShader(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE){
				System.err.println("FRAGMENT SHADER NOT COMPILED PROPERLY!");		
				System.err.println(glGetShaderInfoLog(fragmentShader, 10000));
			}
			glAttachShader(shaderProgram, vertexShader);
			glAttachShader(shaderProgram, fragmentShader);
			glLinkProgram(shaderProgram);
			glValidateProgram(shaderProgram);
			reader.close();
			firstLoad = true;
		}
		for(int i =0; i<floatsToPass.size();i++){
			int location = glGetUniformLocation(programID, varNames.get(i));
			glUniform1(location, Utils.asFloatBuffer(floatsToPass.get(i)));
		}				
		
	}
	public boolean getLoaded(){
		return loaded;
	}

	public void setVisable(boolean run){
		this.run = run;
	}

	public boolean getRun(){
		return run;
	}

	public int getProgram(){
		return programID;
	}

	public void passUniform(float[] floats,String varName){
		floatsToPass.add(floats);
	}

}
