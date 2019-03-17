package technological_singularity;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.util.JsonException;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import technological_singularity.client.gui.ServerGuiHandler;
import technological_singularity.command.TSCommands;
import technological_singularity.creativetab.TSCreativeTab;
import technological_singularity.event.PlayerEventHandler;
import technological_singularity.event.RegistryEventHandler;
import technological_singularity.init.TSBiomes;
import technological_singularity.init.TSBlocks;
import technological_singularity.init.TSEquipmentTypes;
import technological_singularity.init.TSItems;
import technological_singularity.network.ServerNetworkHandler;
import technological_singularity.player.ITSPlayer;
import technological_singularity.player.TSPlayerCapabilityImplementation;
import technological_singularity.player.TSPlayerCapabilityStorage;
import technological_singularity.ship.Ship;
import technological_singularity.tileentity.ShipEquipmentTileEntity;
import technological_singularity.world.TechnologicalSingularityWorldProvider;
import technological_singularity.world.TechnologicalSingularityWorldType;
import technological_singularity.world.biome.TSBiome;
import technological_singularity.worldgen.TSCubePrimer;

@Mod(modid = TechnologicalSingularity.MODID, version = TechnologicalSingularity.VERSION, guiFactory = TechnologicalSingularity.GUI_FACTORY, name = TechnologicalSingularity.NAME)
public class TechnologicalSingularity {
	@SidedProxy(clientSide = "technological_singularity.ClientProxy", serverSide = "technological_singularity.ServerProxy")
	public static ServerProxy proxy;

	@SidedProxy(clientSide = "technological_singularity.network.ClientNetworkHandler", serverSide = "technological_singularity.network.ServerNetworkHandler")
	public static ServerNetworkHandler network;

	@SidedProxy(clientSide = "technological_singularity.client.gui.ClientGuiHandler", serverSide = "technological_singularity.client.gui.ServerGuiHandler")
	public static ServerGuiHandler guiHandler;

	public static final String MODID = "technological_singularity";
	public static final String VERSION = "0.001";
	public static final String NAME = "Technological Singularity";
	public static final String GUI_FACTORY = "technological_singularity.client.gui.TSGuiFactory";

	public static final int TECHNOLOGICAL_SINGULARITY_DIMENSION_ID = 4;
	public static DimensionType TECHNOLOGICAL_SINGULARITY_DIMENSION_TYPE;
	public static WorldType TECHNOLOGICAL_SINGULARITY_WORLD_TYPE;

	public static TSCreativeTab tab;
	public static Logger log;

	public static TechnologicalSingularityConfig config;
	public static TechnologicalSingularity instance;
	
	@CapabilityInject(ITSPlayer.class)
	public static final Capability<ITSPlayer> TSPLAYER_CAPABILITY = null;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) throws Exception {
		instance = this;
		config = new TechnologicalSingularityConfig(new Configuration(event.getSuggestedConfigurationFile()));
		MinecraftForge.EVENT_BUS.register(config);
		tab = new TSCreativeTab(MODID);
		log = event.getModLog();
		TSEquipmentTypes.init();
		TSItems.init();
		TSBlocks.init();
		TSBiomes.init();
		TSCubePrimer.initMapping();
		MinecraftForge.EVENT_BUS.register(proxy);
		proxy.load();
		network.load();
		MinecraftForge.EVENT_BUS.register(new RegistryEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
		TECHNOLOGICAL_SINGULARITY_DIMENSION_TYPE = DimensionType.register(MODID, "_" + MODID,
				TECHNOLOGICAL_SINGULARITY_DIMENSION_ID, TechnologicalSingularityWorldProvider.class, true);
		DimensionManager.registerDimension(TECHNOLOGICAL_SINGULARITY_DIMENSION_ID,
				TECHNOLOGICAL_SINGULARITY_DIMENSION_TYPE);
		TECHNOLOGICAL_SINGULARITY_WORLD_TYPE = new TechnologicalSingularityWorldType("ts.worldType");
		GameRegistry.registerTileEntity(ShipEquipmentTileEntity.class, MODID + ":equipment");
		CapabilityManager.INSTANCE.register(ITSPlayer.class, new TSPlayerCapabilityStorage(), TSPlayerCapabilityImplementation.class);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		TechnologicalSingularity.proxy.registerRenders();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new TSCommands());
		network.setServer(event.getServer());
	}
}
