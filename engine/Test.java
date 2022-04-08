package engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

public class Test {
	
	public static void main(String[] args) {
		try{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("SurvivalGame/Animated Models/Potato"));
		
		OBJLoader o = new OBJLoader();
		AnimatedModel m = o.loadAnimatedModel("C:/Users/Carl/Desktop/fail/cube", 7, ImageIO.read(new File("C:/Users/Carl/workspace/lwjgl-game-engine/SurvivalGame/res/ak_09.bmp")));
		oos.writeObject(m);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream("SurvivalGame/Animated Models/Potato"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println("Reading object...");
			AnimatedModel test = (AnimatedModel) ois.readObject();
			System.out.println("Done reading object...");
			test.lodedFromFile();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} 
		
			 
	}

}
