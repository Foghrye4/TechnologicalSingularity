package technological_singularity.ship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class EquipmentRegistry {
	private final static EquipmentType[] REGISTRY = new EquipmentType[256];
	private final static Object2IntMap<String> NAME_TO_ID_LOOKUP_MAP = new Object2IntOpenHashMap<String>();
	private static int lastId = 0;

	public static void registerEquipment(EquipmentType equipment) throws IllegalAccessException {
		if (REGISTRY[equipment.getId()] != null)
			throw new IllegalAccessException("Cannot register " + equipment + " with id " + equipment.getId()
					+ ". Already registered for " + REGISTRY[equipment.getId()]);
		if (equipment.getId() != 0 && REGISTRY[equipment.getId() - 1] == null)
			throw new IllegalStateException("Cannot register " + equipment + " with id " + equipment.getId()
					+ ". Registry continuity shall be preserved.");
		REGISTRY[equipment.getId()] = equipment;
		NAME_TO_ID_LOOKUP_MAP.put(equipment.toString(), equipment.getId());
		if (equipment.getId() > lastId)
			lastId = equipment.getId();
	}

	public static int getLastRegisteredId() {
		return lastId;
	}

	public static EquipmentType get(int id) {
		if (REGISTRY[id] == null)
			throw new NullPointerException("No entry for id " + id);
		return REGISTRY[id];
	}

	public static EquipmentType fromName(String value) {
		return REGISTRY[NAME_TO_ID_LOOKUP_MAP.getInt(value)];
	}

	public static Collection<EquipmentType> getValues() {
		List<EquipmentType> types = new ArrayList<EquipmentType>();
		for (int i = 0; i <= lastId; i++) {
			types.add(REGISTRY[i]);
		}
		return types;
	}
}
