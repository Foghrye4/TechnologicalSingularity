package technological_singularity.util;

import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class TSMathHelper {
	
	static final float PI = (float) Math.PI;

	public static Vec3i rotateVec3i(Axis a, Rotation rot, Vec3i value) {
		switch (a) {
		case Y:
			switch (rot) {
			case CLOCKWISE_90:
				return new Vec3i(-value.getZ(), value.getY(), value.getX());
			case CLOCKWISE_180:
				return new Vec3i(-value.getX(), value.getY(), -value.getZ());
			case COUNTERCLOCKWISE_90:
				return new Vec3i(value.getZ(), value.getY(), -value.getX());
			default:
				return new Vec3i(value.getX(), value.getY(), value.getZ());
			}
		case X:
			switch (rot) {
			case CLOCKWISE_90:
				return new Vec3i(value.getX(), -value.getZ(), value.getY());
			case CLOCKWISE_180:
				return new Vec3i(value.getX(), -value.getY(), -value.getZ());
			case COUNTERCLOCKWISE_90:
				return new Vec3i(value.getX(), value.getZ(), -value.getY());
			default:
				return new Vec3i(value.getX(), value.getY(), value.getZ());
			}
		case Z:
			switch (rot) {
			case CLOCKWISE_90:
				return new Vec3i(-value.getY(),value.getX(), value.getZ());
			case CLOCKWISE_180:
				return new Vec3i(-value.getX(), -value.getY(), value.getZ());
			case COUNTERCLOCKWISE_90:
				return new Vec3i(value.getY(), -value.getX(), value.getZ());
			default:
				return new Vec3i(value.getX(), value.getY(), value.getZ());
			}
		}
		return new Vec3i(value.getX(), value.getY(), value.getZ());
	}

	public static void add(float[] to, float[] from) {
		for (int i = 0; i < to.length; i++) {
			to[i] += from[i];
		}
	}

	public static void addWithScale(float[] to, float[] from, float scaleIn) {
		float scale = MathHelper.clamp(scaleIn, 0.0f, 1.0f);
		for (int i = 0; i < to.length; i++) {
			to[i] += from[i] * scale;
		}
	}

	public static Vec3d interpolateBetween(Vec3d from, Vec3d to, float interpolationShift) {
		double dx = to.x - from.x;
		double dy = to.y - from.y;
		double dz = to.z - from.z;
		return new Vec3d(from.x + dx * interpolationShift, from.y + dy * interpolationShift,
				from.z + dz * interpolationShift);
	}

	public static float[][] multiplyMatrixes(float[][] matrix1, float[][] matrix2) {
		int n = matrix1[0].length;
		int m = matrix2.length;
		int l = matrix1.length;
		float[][] result = new float[m][n];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				for (int k = 0; k < l; k++)
					result[i][j] += matrix2[i][k] * matrix1[k][j];
		return result;
	}
	
	public static float[] multiplyMatrix4fToVec3d(float[][] matrix1, Vec3d vec) {
		float resultX = 0f;
		float resultY = 0f;
		float resultZ = 0f;
		float resultW = 0f;
		for (int j = 0; j < 4; j++) {
			resultX += (float)vec.x * matrix1[0][j];
			resultY += (float)vec.y * matrix1[1][j];
			resultZ += (float)vec.z * matrix1[2][j];
			resultW += matrix1[3][j];
		}
		return new float[] {resultX / resultW, resultY / resultW, resultZ / resultW};
	}
	
	public static void multiplyMatrix4fToVec3f(float[][] matrix1, float[] vec) {
		float resultX = 0f;
		float resultY = 0f;
		float resultZ = 0f;
		float resultW = 0f;
		for (int j = 0; j < 4; j++) {
			resultX += vec[0] * matrix1[0][j];
			resultY += vec[1] * matrix1[1][j];
			resultZ += vec[2] * matrix1[2][j];
			resultW += matrix1[3][j];
		}
		vec[0] = resultX / resultW;
		vec[1] = resultY / resultW;
		vec[2] = resultZ / resultW;
	}

	public static float[][] transpose(float[] vector) {
		return transpose(new float[][] {vector});
	}
	
	public static float[][] transpose(float[][] matrix) {
		int m = matrix[0].length;
		int n = matrix.length;
		float[][] result = new float[m][n];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				result[i][j] = matrix[j][i];
		return result;
	}

	public static float[] rotateVec4f(float[] vector, float[] planeNormal, float angle) {
		float sina2 = (float) Math.sin(angle/2);
		float x = planeNormal[0]*sina2;
		float y = planeNormal[1]*sina2;
		float z = planeNormal[2]*sina2;
		float w = (float) Math.cos(angle/2);
        float[][] rotationMatrix = new float[][]{
				new float[] { 1 - 2 * (y * y + z * z), 	2 * (x * y - w * z), 		2 * (x * z + w * y), 		0 },
				new float[] { 2 * (x * y + w * z), 		1 - 2 * (x * x + z * z), 	2 * (y * z - w * x), 		0 },
				new float[] { 2 * (x * z - w * y), 		2 * (y * z + w * x), 		1 - 2 * (x * x + y * y), 	0 },
				new float[] { 0, 						0, 							0, 							1 }        };
		return multiplyMatrixes(rotationMatrix, new float[][] {vector})[0];
	}

	public static float[][] v3fTo4fM(float[] reaction, int zeroIndex) {
		return new float[][] {
				new float[] { reaction[zeroIndex], reaction[zeroIndex + 1], reaction[zeroIndex + 2], 1.0f } };
	}

	public static float[][] vec3dTo4fM(Vec3d vec) {
		return new float[][] { new float[] { (float) vec.x, (float) vec.y, (float) vec.z, 1.0f } };
	}
	
	public static float[] vec3dToV4f(Vec3d vec) {
		return new float[] { (float) vec.x, (float) vec.y, (float) vec.z, 1.0f };
	}
	
	public static Vec3d rotateVec3d(float[][] rotationMatrix, Vec3d vec) {
		float[] vecOutF = multiplyMatrixes(rotationMatrix,TSMathHelper.vec3dTo4fM(vec))[0];
		Vec3d vecOut = new Vec3d(vecOutF[0],vecOutF[1],vecOutF[2]);
		return vecOut;
	}
	
	public static void normalize(float[] vec) {
		float x = vec[0];
		float y = vec[1];
		float z = vec[2];
		float d = (float) Math.sqrt(x*x+y*y+z*z);
		if(d<0.01) {
			vec[0] = 0.0f;
			vec[1] = 0.0f;
			vec[2] = 1.0f;
		}
		else{
			vec[0] /= d;
			vec[1] /= d;
			vec[2] /= d;
			if (vec[0] > 1.0f)
				vec[0] = 1.0f;
			if (vec[1] > 1.0f)
				vec[1] = 1.0f;
			if (vec[2] > 1.0f)
				vec[2] = 1.0f;
			if (vec[0] < -1.0f)
				vec[0] = -1.0f;
			if (vec[1] < -1.0f)
				vec[1] = -1.0f;
			if (vec[2] < -1.0f)
				vec[2] = -1.0f;
		}
	}

	public static void toPitchYaw(float[] vec) {
		normalize(vec);
		float pitch = (float) Math.asin(vec[1]);
		pitch *= 180f / PI;
		float yaw = (float) Math.asin(vec[0]);
		if (vec[2] <= 0)
			yaw = PI - yaw;
		yaw += PI / 2;
		yaw *= 180f / PI;
		vec[0]=pitch;
		vec[1]=yaw;
	}

	public static float dotProduct(float[] vec4f1, float[] vec4f2) {
		return vec4f1[0]*vec4f2[0]+vec4f1[1]*vec4f2[1]+vec4f1[2]*vec4f2[2];
	}
	
	public static float dotProduct(float[] vec4f1, Vec3d vec2) {
		return vec4f1[0]*(float)vec2.x+vec4f1[1]*(float)vec2.y+vec4f1[2]*(float)vec2.z;
	}
	
	public static float[] crossProduct(Vec3d vec, float[] vec4f) {
		return new float[] { (float) vec.y * vec4f[2] - (float) vec.z * vec4f[1],
				(float) vec.z * vec4f[0] - (float) vec.x * vec4f[2],
				(float) vec.x * vec4f[1] - (float) vec.y * vec4f[0] };
	}
}
