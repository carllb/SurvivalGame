package engine;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;

public abstract class ShapeGenarator {
	
	public static Shape genBoxShape(Vector3f halfExtents){
		Shape s;
		int side = 0;
		Point3d[] verts = new Point3d[24];
		Point2d[] textCoords = new Point2d[24];
		//rendering the faces backwards remember to flip!!!!!!
		for(int i=0;i+4<=24;i+=4){
			switch (side) {
			case 0:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);
				break;
			case 1:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);				
				break;
			case 2:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);				
				break;				
			case 3:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);					
				break;
			case 4:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);					
				break;
			case 5:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);		
				break;			
			default:					
				break;			
			}
			side ++;
		}
		s = new Shape(verts, textCoords, null, null, 4);
		s.size = Utils.vector3fToPoint3d(halfExtents);
		s.location = new Point3d(0, 0, 0);
		return s;
	}
	public static PhysObj genPhysShape(Vector3f halfExtents,CollisionShape c,float mass){
		PhysObj s;
		int side = 0;
		Point3d[] verts = new Point3d[24];
		Point2d[] textCoords = new Point2d[24];
		//rendering the faces backwards remember to flip!!!!!!
		for(int i=0;i+4<=24;i+=4){
			switch (side) {
			case 0:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);
				break;
			case 1:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);				
				break;
			case 2:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);				
				break;				
			case 3:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);					
				break;
			case 4:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);					
				break;
			case 5:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);		
				break;			
			default:					
				break;			
			}
			side ++;
		}
		s = new PhysObj(verts, textCoords, null, null, 4, c, mass);
		s.size = Utils.vector3fToPoint3d(halfExtents);
		s.location = new Point3d(0, 0, 0);
		return s;
	}
	public static void resizePhysShape(PhysObj s, Vector3f halfExtents){
		Point2d[] textCoords = new Point2d[24];
		Point3d[] verts = new Point3d[24];
		int side = 0;
		for(int i=0;i+4<=24;i+=4){
			switch (side) {
			case 0:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);
				break;
			case 1:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);				
				break;
			case 2:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);				
				break;				
			case 3:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);					
				break;
			case 4:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);					
				break;
			case 5:
				textCoords[i] = new Point2d(1, 1);
				textCoords[i+1] = new Point2d(0, 1);
				textCoords[i+2] = new Point2d(0, 0);
				textCoords[i+3] = new Point2d(1, 0);
				verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
				verts[i+1] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);
				verts[i+2] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);
				verts[i+3] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);		
				break;			
			default:					
				break;			
			}
			side ++;
		}
		for(int i=0;i<verts.length;i++){
			s.vertices.set(i, verts[i]);
			s.texCoords.set(i, textCoords[i]);
		}
		
		s.size = Utils.vector3fToPoint3d(halfExtents);
		s.remakePlanes();
		}
		public static void resizeShape(Shape s, Vector3f halfExtents){
			Point2d[] textCoords = new Point2d[24];
			Point3d[] verts = new Point3d[24];
			int side = 0;
			for(int i=0;i+4<=24;i+=4){
				switch (side) {
				case 0:
					textCoords[i] = new Point2d(1, 1);
					textCoords[i+1] = new Point2d(0, 1);
					textCoords[i+2] = new Point2d(0, 0);
					textCoords[i+3] = new Point2d(1, 0);
					verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);
					verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);
					verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
					verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);
					break;
				case 1:
					textCoords[i] = new Point2d(1, 1);
					textCoords[i+1] = new Point2d(0, 1);
					textCoords[i+2] = new Point2d(0, 0);
					textCoords[i+3] = new Point2d(1, 0);
					verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
					verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
					verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
					verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);				
					break;
				case 2:
					textCoords[i] = new Point2d(1, 1);
					textCoords[i+1] = new Point2d(0, 1);
					textCoords[i+2] = new Point2d(0, 0);
					textCoords[i+3] = new Point2d(1, 0);
					verts[i] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);
					verts[i+1] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
					verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
					verts[i+3] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);				
					break;				
				case 3:
					textCoords[i] = new Point2d(1, 1);
					textCoords[i+1] = new Point2d(0, 1);
					textCoords[i+2] = new Point2d(0, 0);
					textCoords[i+3] = new Point2d(1, 0);
					verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
					verts[i+1] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
					verts[i+2] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);
					verts[i+3] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);					
					break;
				case 4:
					textCoords[i] = new Point2d(1, 1);
					textCoords[i+1] = new Point2d(0, 1);
					textCoords[i+2] = new Point2d(0, 0);
					textCoords[i+3] = new Point2d(1, 0);
					verts[i] = new Point3d(halfExtents.x,-halfExtents.y,-halfExtents.z);
					verts[i+1] = new Point3d(halfExtents.x,halfExtents.y,-halfExtents.z);
					verts[i+2] = new Point3d(halfExtents.x,halfExtents.y,halfExtents.z);
					verts[i+3] = new Point3d(halfExtents.x,-halfExtents.y,halfExtents.z);					
					break;
				case 5:
					textCoords[i] = new Point2d(1, 1);
					textCoords[i+1] = new Point2d(0, 1);
					textCoords[i+2] = new Point2d(0, 0);
					textCoords[i+3] = new Point2d(1, 0);
					verts[i] = new Point3d(-halfExtents.x,-halfExtents.y,-halfExtents.z);
					verts[i+1] = new Point3d(-halfExtents.x,halfExtents.y,-halfExtents.z);
					verts[i+2] = new Point3d(-halfExtents.x,halfExtents.y,halfExtents.z);
					verts[i+3] = new Point3d(-halfExtents.x,-halfExtents.y,halfExtents.z);		
					break;			
				default:					
					break;			
				}
				side ++;
			}
		for(int i=0;i<verts.length;i++){
			s.vertices.set(i, verts[i]);
			s.texCoords.set(i, textCoords[i]);
		}
		
		s.size = Utils.vector3fToPoint3d(halfExtents);
		s.remakePlanes();

	}
}
