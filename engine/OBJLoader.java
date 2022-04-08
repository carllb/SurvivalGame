package engine;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OBJLoader {
	
	
	public Model loadOBJ(File f) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		Model m = new Model();
		String line;				
		while((line = reader.readLine()) != null){
		
			if(line.startsWith("v ")){
				double x = Double.parseDouble(line.split(" ")[1]);
				double y = Double.parseDouble(line.split(" ")[2]);
				double z = Double.parseDouble(line.split(" ")[3]);
				m.vertices.add(new Point3d(x, y, z));				
			}else if(line.startsWith("vt ")){				
				double x = Double.parseDouble(line.split(" ")[1]);
				double y = Double.parseDouble(line.split(" ")[2]);			
				m.texVerts.add(new Point2d(x, y));
			}else if(line.startsWith("vn ")){
				double x = Double.parseDouble(line.split(" ")[1]);
				double y = Double.parseDouble(line.split(" ")[2]);
				double z = Double.parseDouble(line.split(" ")[3]);
				m.normals.add(new Point3d(x, y, z));
			}else if(line.startsWith("f ")){
				double xv = Double.parseDouble(line.split(" ")[1].split("/")[0]);
				double yv = Double.parseDouble(line.split(" ")[2].split("/")[0]);
				double zv = Double.parseDouble(line.split(" ")[3].split("/")[0]);				
				
				double xn = Double.parseDouble(line.split(" ")[1].split("/")[2]);
				double yn = Double.parseDouble(line.split(" ")[2].split("/")[2]);
				double zn = Double.parseDouble(line.split(" ")[3].split("/")[2]);		
				
				m.faces.add(new Face(new Point3d(xv, yv, zv), new Point3d(01,01,01),new Point3d(xn, yn, zn)));
			}
		}	
		reader.close();	
		return m;
	}
	public Model loadOBJ(File f,BufferedImage image) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		Model m = new Model();
		String line;				
		while((line = reader.readLine()) != null){
		try{	
			if(line.startsWith("v ")){
				double x = Double.parseDouble(line.split(" ")[1]);
				double y = Double.parseDouble(line.split(" ")[2]);
				double z = Double.parseDouble(line.split(" ")[3]);
				m.vertices.add(new Point3d(x, y, z));
			}else if(line.startsWith("vt ")){
				double x = Double.parseDouble(line.split(" ")[1]);
				double y = Double.parseDouble(line.split(" ")[2]);					
				m.texVerts.add(new Point2d(x, y));				
			}else if(line.startsWith("vn ")){
				double x = Double.parseDouble(line.split(" ")[1]);
				double y = Double.parseDouble(line.split(" ")[2]);
				double z = Double.parseDouble(line.split(" ")[3]);
				m.normals.add(new Point3d(x, y, z));
			}else if(line.startsWith("f ")){
				double xv = Double.parseDouble(line.split(" ")[1].split("/")[0]);
				double yv = Double.parseDouble(line.split(" ")[2].split("/")[0]);
				double zv = Double.parseDouble(line.split(" ")[3].split("/")[0]);
				
				double xt = Double.parseDouble(line.split(" ")[1].split("/")[1]);
				double yt = Double.parseDouble(line.split(" ")[2].split("/")[1]);
				double zt = Double.parseDouble(line.split(" ")[3].split("/")[1]);		
				
				double xn = Double.parseDouble(line.split(" ")[1].split("/")[2]);
				double yn = Double.parseDouble(line.split(" ")[2].split("/")[2]);
				double zn = Double.parseDouble(line.split(" ")[3].split("/")[2]);						
				m.faces.add(new Face(new Point3d(xv, yv, zv), new Point3d(xt,yt,zt),new Point3d(xn, yn, zn)));			
			}
		}catch(Exception e){
	//		System.err.println("Warning faild to read a line");
		//	System.err.println();
		//	e.printStackTrace();
		}
		}	
		reader.close();	
		m.image = image;	
		return m;
	}
	/*
	 * Filename is without the _00001 and without the .obj for example just "res/Cube"
	 */
	public AnimatedModel loadAnimatedModel(String fileName,int frames,BufferedImage texture) throws IOException{
		AnimatedModel m = new AnimatedModel();
		System.out.println("Loading Animated Model: " + fileName);
		for(int i=0;i<frames;i++){			
			String frn;
			if((i+1)<10){
				frn = fileName + "_00000" + (i+1) + ".obj";
			}else if((i+1)<100){
				frn = fileName + "_0000" + (i+1) + ".obj";
			}else if((i+1)<1000){
				frn = fileName + "_000" + (i+1) + ".obj";
			}else if((i+1)<10000){
				frn = fileName + "_00" + (i+1) + ".obj";
			}else if((i+1)<100000){
				frn = fileName + "_0" + (i+1) + ".obj";
			}else{
				frn = fileName + "_" + (i+1) + ".obj";
			}
			Model m2 = loadOBJ(new File(frn),texture);
			m.addFrame(m2.vertices, m2.normals, m2.texVerts,m2.faces);
			System.out.println("%: " + i*100/frames);
		}
		m.doneAddingFrames();
		m.texture = texture;
		return m;
	}
	public AnimatedModel loadAnimatedModel(String fileName,int frames) throws IOException{
		AnimatedModel m = new AnimatedModel();
		System.out.println("Loading Animated Model: " + fileName);
		for(int i=0;i<frames;i++){			
			String frn;
			if((i+1)<10){
				frn = fileName + "_00000" + (i+1) + ".obj";
			}else if((i+1)<100){
				frn = fileName + "_0000" + (i+1) + ".obj";
			}else if((i+1)<1000){
				frn = fileName + "_000" + (i+1) + ".obj";
			}else if((i+1)<10000){
				frn = fileName + "_00" + (i+1) + ".obj";
			}else if((i+1)<100000){
				frn = fileName + "_0" + (i+1) + ".obj";
			}else{
				frn = fileName + "_" + (i+1) + ".obj";
			}
			Model m2 = loadOBJ(new File(frn));
			m.addFrame(m2.vertices, m2.normals, m2.texVerts,m2.faces);
			System.out.println("%: " + i*100/frames);
		}
		m.doneAddingFrames();		
		return m;
	}
	
	

}
