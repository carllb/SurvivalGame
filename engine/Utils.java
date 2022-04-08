package engine;

import java.nio.FloatBuffer;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.lwjgl.BufferUtils;

public class Utils {

	public static final double FACTOR_DEG_TO_RAD = Math.PI/180; 
	
	public static FloatBuffer asFloatBuffer(float[] floats) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(floats.length);
		fb.put(floats);
		fb.flip();
		return fb;	
	}
	
	public static FloatBuffer asFloatBuffer(Vector4f floats){
		FloatBuffer fb = asFloatBuffer(new float[]{floats.x,floats.y,floats.z,floats.w});
		return fb;	
	}
	
	public static FloatBuffer asFloatBuffer(Vector3f floats){
		FloatBuffer fb = asFloatBuffer(new float[]{floats.x,floats.y,floats.z});
		return fb;	
	}
	
	public static void QuaternionToEuler(Quat4f TQuat, Vector3f TEuler) {

		float W = TQuat.w;
		float X = TQuat.x;
		float Y = TQuat.y;
		float Z = TQuat.z;
		float WSquared = W * W;
		float XSquared = X * X;
		float YSquared = Y * Y;
		float ZSquared = Z * Z;

		float f = (float) (180/Math.PI);
		
		TEuler.x = (float) (Math.atan2(2.0f * (Y * Z + X * W), -XSquared - YSquared + ZSquared + WSquared));
		TEuler.y = (float) (Math.asin(-2.0f * (X * Z - Y * W)));
		TEuler.z = (float) (Math.atan2(2.0f * (X * Y + Z * W), XSquared - YSquared - ZSquared + WSquared));
		
		TEuler.x *= f;
		TEuler.y *= f;
		TEuler.z *= f;
	}
	public static Point3d vector3fToPoint3d(Vector3f v){
		Point3d p = new Point3d(v.x,v.y,v.z);
		return p;
	}
	
	
}
