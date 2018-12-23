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
import technological_singularity.block.BlockEarthStructure;
import technological_singularity.block.BlockEarthStructureSlope;
import technological_singularity.block.BlockSpaceStationHull;
import technological_singularity.block.BlockSpaceStationLightPanel;
import technological_singularity.block.TechnologicalSingularityBlockBase;
import technological_singularity.ship.EngineEquipmentType;
import technological_singularity.ship.EquipmentRegistry;
import technological_singularity.ship.EquipmentType;

public class TSEquipmentTypes {
	private static List<EquipmentType> types = new ArrayList<EquipmentType>();
	public static EquipmentType CORE;
	public static EquipmentType BASIC_ARMOR;
	public static EquipmentType BASIC_ENGINE;
	public static EquipmentType BASIC_REACTOR;
	public static EquipmentType BASIC_DUAL_THRUSTER_ENGINE;

	public static void init() throws IllegalAccessException {
		CORE = new EquipmentType(0, "core", types);
		BASIC_ARMOR = new EquipmentType(1, "basic_armor", types);
		BASIC_ENGINE = new EngineEquipmentType(2, "basic_engine", 1, 100, types);
		BASIC_REACTOR = new EquipmentType(3, "basic_reactor", types);
		BASIC_DUAL_THRUSTER_ENGINE = new EngineEquipmentType(4, "basic_dual_engine", 2, 100, types);
		
		for (EquipmentType type : types) {
			TechnologicalSingularity.log.info("Registering equipment type " + type + " for id " + type.getId());
			EquipmentRegistry.registerEquipment(type);
		}
	}
}
