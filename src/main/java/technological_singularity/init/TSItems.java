package technological_singularity.init;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;
import technological_singularity.item.ItemBlockFiller;
import technological_singularity.item.ItemEraser;
import technological_singularity.item.ItemRotator;
import technological_singularity.item.TechnologicalSingularityItemBase;

public class TSItems {
	private static List<TechnologicalSingularityItemBase> items = new ArrayList<TechnologicalSingularityItemBase>();
	public static TechnologicalSingularityItemBase ERASER;
	public static TechnologicalSingularityItemBase FILLER;
	public static TechnologicalSingularityItemBase ROTATOR_X;
	public static TechnologicalSingularityItemBase ROTATOR_Y;
	public static TechnologicalSingularityItemBase ROTATOR_Z;

	public static void init() {
		ERASER = new ItemEraser().registerAs("eraser", items);
		FILLER = new ItemBlockFiller().registerAs("filler", items);
		ROTATOR_X = new ItemRotator(Axis.X).registerAs("rotator_x", items);
		ROTATOR_Y = new ItemRotator(Axis.Y).registerAs("rotator_y", items);
		ROTATOR_Z = new ItemRotator(Axis.Z).registerAs("rotator_z", items);
	}

	public static void register(IForgeRegistry<Item> registry) {
		for (Item item : items) {
			registry.register(item);
		}
	}

	public static void registerRenders() {
		for (TechnologicalSingularityItemBase item : items) {
			for (int meta = 0; meta <= item.getLastMeta(); meta++) {
				registerRender(item, meta, item.getRegistryName(), item.getModelLocation(meta));
			}
		}
	}

	private static void registerRender(Item item, int metadata, ResourceLocation modelResourceLocation,
			String modelLocation) {
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(modelResourceLocation, modelLocation));
	}
}
