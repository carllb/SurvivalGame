package engine;

import java.io.Serializable;

public class Face implements Serializable{

	public Point3d vertex, texture, normal;
	
	
	public Face(Point3d vertex,Point3d texture, Point3d normal){
		this.vertex = vertex;
		this.texture = texture;
		this.normal = normal;
	}
	
}
