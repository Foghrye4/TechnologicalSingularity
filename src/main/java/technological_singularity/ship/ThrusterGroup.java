package technological_singularity.ship;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.network.ClientNetworkHandler;
import technological_singularity.util.TSConstants;
import technological_singularity.util.TSMathHelper;
import static technological_singularity.util.TSMathHelper.*;

public class ThrusterGroup {
//	private float[] reaction = new float[6];
	private final List<Thruster> thrustersList;
	private final static Vec3d ROLL = new Vec3d(0.0, 0.0, 1.0);
	private final static Vec3d PITCH = new Vec3d(1.0, 0.0, 0.0);
	private final static Vec3d YAW = new Vec3d(0.0, 1.0, 0.0);
	
	public ThrusterGroup(List<Thruster> list) {
		thrustersList = list;
/*		for(Thruster t:list){
			reaction[0] -= (float)(t.direction.x * t.traction);
			reaction[1] -= (float)(t.direction.y * t.traction);
			reaction[2] -= (float)(t.direction.z * t.traction);
			Vec3d pitch = t.direction.crossProduct(PITCH);
			reaction[3] += (float)(pitch.dotProduct(t.comOffset) * t.traction);
			Vec3d yaw = t.direction.crossProduct(YAW);
			reaction[4] += (float)(yaw.dotProduct(t.comOffset) * t.traction);
			Vec3d roll = t.direction.crossProduct(ROLL);
			reaction[5] += (float)(roll.dotProduct(t.comOffset) * t.traction);
		}*/
	}

	/* Reaction: 
	 * forward-back: 0
	 * left-right: 1
	 * up-down: 2
	 * pitch: 3
	 * yaw: 4
	 * roll: 5
	 * */
	public void getReaction(Ship ship, float[] reaction, float[][] rotationMatrix, float scale) {
		for(Thruster t:thrustersList){
			float[] dir = multiplyMatrix4fToVec3d(rotationMatrix,t.direction);
			reaction[0] -= dir[0] * t.traction * scale / ship.mass;
			reaction[1] -= dir[1] * t.traction * scale / ship.mass;
			reaction[2] -= dir[2] * t.traction * scale / ship.mass;
			reaction[3] += getMoment(t, PITCH, scale);
			reaction[4] += getMoment(t, YAW, scale);
			reaction[5] += getMoment(t, ROLL, scale);
		}
	}
	
	private float getMoment(Thruster t, Vec3d forward, float scale) {
		float fx = (float)forward.x;
		float fy = (float)forward.y;
		float fz = (float)forward.z;
		
		float cx = (float)t.comOffset.x;
		float cy = (float)t.comOffset.y;
		float cz = (float)t.comOffset.z;
		
		float dirx = (float)t.direction.x;
		float diry = (float)t.direction.y;
		float dirz = (float)t.direction.z;			
		
		float nx = fy*cz-fz*cy;
		float ny = fz*cx-fx*cz;
		float nz = fx*cy-fy*cx;
		return t.traction * scale * (nx*dirx+ny*diry+nz*dirz);
	}
	
	public void spawnTrailParticles(float[][] rotationMatrix, Vec3d comPos, Vec3d shipVelocity, float startAge){
		for(Thruster t:thrustersList){
			ClientNetworkHandler clientNetwork = (ClientNetworkHandler) TechnologicalSingularity.network;
			Vec3d vel = TSMathHelper.rotateVec3d(rotationMatrix, t.direction).scale(0.5).add(shipVelocity);
			Vec3d pos = TSMathHelper.rotateVec3d(rotationMatrix, t.comOffset).add(comPos);
			clientNetwork.sendCommandSpawnParticles(TSConstants.PARTICLE_TYPE_TRAIL, pos, vel, startAge);
		}
	}
}
