package technological_singularity.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.block.BlockArmorEquipmentSlope;
import technological_singularity.block.BlockEarthStructure;
import technological_singularity.block.BlockEarthStructureSlope;
import technological_singularity.block.BlockSpaceStationHull;
import technological_singularity.block.BlockSpaceStationLightPanel;
import technological_singularity.block.ShipEquipmentBlock;
import technological_singularity.block.TechnologicalSingularityBlockBase;

public class TSBlocks {
	private static List<TechnologicalSingularityBlockBase> blocks = new ArrayList<TechnologicalSingularityBlockBase>();
	public static TechnologicalSingularityBlockBase EARTH_STRUCTURE;
	public static TechnologicalSingularityBlockBase EARTH_STRUCTURE_SLOPE;
	public static TechnologicalSingularityBlockBase SPACE_STATION_HULL;
	public static TechnologicalSingularityBlockBase SPACE_STATION_LIGHT_PANEL;
	public static TechnologicalSingularityBlockBase SHIP_CORE;
	public static TechnologicalSingularityBlockBase BASIC_ARMOR;
	public static TechnologicalSingularityBlockBase BASIC_ARMOR_SLOPE;
	public static TechnologicalSingularityBlockBase BASIC_DUAL_THRUSTER_ENGINE;

	public static void init() {
		EARTH_STRUCTURE = new BlockEarthStructure(Material.IRON, MapColor.WHITE_STAINED_HARDENED_CLAY).registerAs("earth_structure", blocks);
		EARTH_STRUCTURE_SLOPE = new BlockEarthStructureSlope(Material.IRON, MapColor.WHITE_STAINED_HARDENED_CLAY).registerAs("earth_structure_slope", blocks);
		SPACE_STATION_HULL = new BlockSpaceStationHull(Material.IRON, MapColor.LIGHT_BLUE)
				.registerAs("space_station_hull", blocks);
		SPACE_STATION_LIGHT_PANEL = new BlockSpaceStationLightPanel(Material.IRON, MapColor.LIGHT_BLUE)
				.registerAs("space_station_light_panel", blocks);
		SHIP_CORE = new ShipEquipmentBlock(TSEquipmentTypes.CORE, Material.IRON, MapColor.BLUE_STAINED_HARDENED_CLAY)
				.registerAs("equipment_core", blocks);
		BASIC_ARMOR = new ShipEquipmentBlock(TSEquipmentTypes.BASIC_ARMOR, Material.IRON, MapColor.BLUE_STAINED_HARDENED_CLAY)
				.registerAs("equipment_basic_armor", blocks);
		BASIC_ARMOR_SLOPE = new BlockArmorEquipmentSlope(TSEquipmentTypes.BASIC_ARMOR, Material.IRON, MapColor.BLUE_STAINED_HARDENED_CLAY)
				.registerAs("equipment_basic_armor_slope", blocks);
		BASIC_DUAL_THRUSTER_ENGINE = new BlockArmorEquipmentSlope(TSEquipmentTypes.BASIC_DUAL_THRUSTER_ENGINE, Material.IRON, MapColor.BLUE_STAINED_HARDENED_CLAY)
				.registerAs("equipment_basic_dual_thruster_engine", blocks);
	}

	public static void register(IForgeRegistry<Block> registry) {
		for (Block block : blocks) {
			registry.register(block);
		}
	}
	
	public static void registerAsItem(IForgeRegistry<Item> registry) {
		for (TechnologicalSingularityBlockBase block : blocks) {
			Item blockItem = block.generateItemFromBlock();
			if(blockItem!=null)
				registry.register(blockItem);
		}
	}
	
	public static void registerRenders() {
		for (TechnologicalSingularityBlockBase block : blocks) {
			for(IBlockState state:block.getBlockState().getValidStates()){
				int meta = block.getMetaFromState(state);
				String stateString = block.stateToString(state);
				registerRender(Item.getItemFromBlock(block), meta, block.getRegistryName(), stateString);
			}
		}
	}

	private static void registerRender(Item item, int metadata, ResourceLocation modelResourceLocation, String variantIn) {
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(modelResourceLocation, variantIn));
	}
}
