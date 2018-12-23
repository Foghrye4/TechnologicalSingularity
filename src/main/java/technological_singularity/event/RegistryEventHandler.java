package technological_singularity.event;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.init.TSBiomes;
import technological_singularity.init.TSBlocks;
import technological_singularity.init.TSItems;

public class RegistryEventHandler {
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		TSBlocks.register(event.getRegistry());
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		TSBlocks.registerAsItem(event.getRegistry());
		TSItems.register(event.getRegistry());
		TechnologicalSingularity.proxy.registerRenders();
	}
	
	@SubscribeEvent
	public void registerBiomes(RegistryEvent.Register<Biome> event) {
		TSBiomes.register(event.getRegistry());
	}
}