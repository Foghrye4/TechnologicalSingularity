package technological_singularity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import technological_singularity.client.ClientCameraEventHandler;
import technological_singularity.client.HUDHandler;
import technological_singularity.client.SkyRenderer;
import technological_singularity.client.input.ClientControlInput;
import technological_singularity.client.renderer.PlayerRenderer;
import technological_singularity.init.TSBlocks;
import technological_singularity.init.TSItems;
import technological_singularity.world.TechnologicalSingularityWorldProvider;
import static technological_singularity.world.TechnologicalSingularityWorldProvider.*;

public class ClientProxy extends ServerProxy {

	private SkyRenderer skyRenderer;

	@Override
	public File getMinecraftDir() {
		return Minecraft.getMinecraft().mcDataDir;
	}
	
	public void setSkyRenderer(TechnologicalSingularityWorldProvider provider) {
		skyRenderer = new SkyRenderer();
		MinecraftForge.EVENT_BUS.register(skyRenderer);
		provider.setSkyRenderer(skyRenderer);
	}
	
	public void setCloudRenderer(TechnologicalSingularityWorldProvider provider) {
		provider.setCloudRenderer(new IRenderHandler(){
			@Override
			public void render(float partialTicks, WorldClient world, Minecraft mc) {
			}});
	}
	
	@Override
	public void load() throws IOException {
		OBJLoader.INSTANCE.addDomain(TechnologicalSingularity.MODID);
		MinecraftForge.EVENT_BUS.register(new ClientControlInput());
		MinecraftForge.EVENT_BUS.register(new ClientCameraEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerRenderer());
		MinecraftForge.EVENT_BUS.register(new HUDHandler());
	}
	
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		
	}
	
	@Override
	public void registerRenders() {
		TSBlocks.registerRenders();
		TSItems.registerRenders();
	}
	
	@Override
	public InputStream getResourceInputStream(ResourceLocation location) throws IOException {
		return Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
	}
	
	@Override
	public float getElevationAboveEarthSurfaceNormalized() {
		double posY = Minecraft.getMinecraft().player.posY;
		return MathHelper.clamp((float) ((posY-SEA_LEVEL_Y)/SEA_LEVEL_Y), 0f,1f);
	}
}
