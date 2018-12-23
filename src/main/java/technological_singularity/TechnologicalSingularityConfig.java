package technological_singularity;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import technological_singularity.client.input.ClientControlInput;

import static technological_singularity.TechnologicalSingularity.*;

public class TechnologicalSingularityConfig {
	public TechnologicalSingularityConfig(Configuration configuration) {
		loadConfig(configuration);
		syncConfig();
}
	public static String getNicelyFormattedName(String name) {
		StringBuffer out = new StringBuffer();
		char char_ = '_';
		char prevchar = 0;
		for (char c : name.toCharArray()) {
			if (c != char_ && prevchar != char_) {
				out.append(String.valueOf(c).toLowerCase());
			} else if (c != char_) {
				out.append(String.valueOf(c));
			}
			prevchar = c;
		}
		return out.toString();
	}

	public Configuration configuration;

	void loadConfig(Configuration configuration) {
		this.configuration = configuration;
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.getModID().equals(MODID)) {
			TechnologicalSingularity.config.syncConfig();
		}
	}

	void syncConfig() {
		ClientControlInput.acradeRotationType = configuration.getBoolean("arcade_rotation_type",
				Configuration.CATEGORY_GENERAL, false, "Free camera and ship rotation - no inertia mode.");
		if (configuration.hasChanged()) {
			configuration.save();
		}
	}
}
