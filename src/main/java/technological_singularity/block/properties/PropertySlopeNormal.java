package technological_singularity.block.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.math.Vec3i;

public class PropertySlopeNormal implements IProperty<Vec3i> {

	private final Vec3i[] allowedValues = new Vec3i[] { new Vec3i(1, 1, 0), new Vec3i(1, -1, 0), new Vec3i(-1, -1, 0),
			new Vec3i(-1, 1, 0), new Vec3i(1, 0, 1), new Vec3i(1, 0, -1), new Vec3i(-1, 0, -1), new Vec3i(-1, 0, 1),
			new Vec3i(0, 1, 1), new Vec3i(0, 1, -1), new Vec3i(0, -1, -1), new Vec3i(0, -1, 1) };

	@Override
	public String getName() {
		return "slope_normal";
	}

	@Override
	public Collection<Vec3i> getAllowedValues() {
		List<Vec3i> allowedValuesCollection = new ArrayList<Vec3i>();
		for(Vec3i value:allowedValues)
			allowedValuesCollection.add(value);
		return allowedValuesCollection;
	}

	@Override
	public Class<Vec3i> getValueClass() {
		return Vec3i.class;
	}

	@Override
	public Optional<Vec3i> parseValue(String value) {
		int[] intValues = new int[3];
		int charFrom = value.indexOf("{");
		int charTo = value.indexOf("}");
		if (charTo != -1 && charFrom != -1) {
			value = value.substring(charFrom + 1, charTo);
		}
		String[] values = value.split(",");
		for (int i = 0; i < intValues.length; i++) {
			String val = values[i];
			if (val.contains("x")) {
				intValues[0] = Integer.valueOf(val.replace("x=", ""));
			} else if (val.contains("y")) {
				intValues[1] = Integer.valueOf(val.replace("y=", ""));
			} else if (val.contains("z")) {
				intValues[2] = Integer.valueOf(val.replace("z=", ""));
			} else {
				intValues[i] = Integer.valueOf(val);
			}
		}
		return Optional.fromNullable(new Vec3i(intValues[0], intValues[1], intValues[2]));
	}

	@Override
	public String getName(Vec3i value) {
		StringBuffer sb = new StringBuffer();
		if (value.getX() != 0) {
			sb.append(value.getX() > 0 ? "px_" : "nx_");
		}
		if (value.getY() != 0) {
			sb.append(value.getY() > 0 ? "py" : "ny");
			if (value.getZ() != 0) {
				sb.append("_");
			}
		}
		if (value.getZ() != 0) {
			sb.append(value.getZ() > 0 ? "pz" : "nz");
		}
		
		return sb.toString();
	}

	public int toMeta(Vec3i value) {
		int x = value.getX();
		int y = value.getY();
		int z = value.getZ();
		int bit1 = 0;
		int bit2 = 0;
		int p = z == 0 ? 0 : y == 0 ? 1 : 2;
		switch (p) {
		case 0:
			bit1 =  (x + 1) / 2;
			bit2 =  (y + 1) / 2;
			break;
		case 1:
			bit1 =  (x + 1) / 2;
			bit2 =  (z + 1) / 2;
			break;
		case 2:
			bit1 =  (y + 1) / 2;
			bit2 =  (z + 1) / 2;
			break;
		}
		return bit1 | bit2 << 1 | p << 2;
	}

	public Vec3i fromMeta(int value) {
		int x = 0;
		int y = 0;
		int z = 0;
		int bit1 = value & 1;
		int bit2 = (value >> 1) & 1;
		int p = value >> 2;
		switch (p) {
		case 0:
			x = bit1 * 2 - 1;
			y = bit2 * 2 - 1;
			break;
		case 1:
			x = bit1 * 2 - 1;
			z = bit2 * 2 - 1;
			break;
		case 2:
			y = bit1 * 2 - 1;
			z = bit2 * 2 - 1;
			break;
		}
		return new Vec3i(x, y, z);
	}
}
