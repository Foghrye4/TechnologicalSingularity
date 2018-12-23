package technological_singularity.client.gui;

import java.io.IOException;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.block.ShipEquipmentBlock;
import technological_singularity.network.ClientNetworkHandler;
import technological_singularity.util.TSConstants;

import static technological_singularity.TechnologicalSingularity.*;

public class GuiEngineEquipmentSetup extends GuiEquipmentSetup {

	private final ContainerEquipmentSetup container;
	private final int thrustersAmount;
	private final Vec3d[] directions;
	private final static int BUTTON_WIDTH = 20;
	private final static int BUTTON_HEIGHT = 19;
	private final static int THRUSTER_ROW_HEIGHT = 60;

	public GuiEngineEquipmentSetup(ContainerEquipmentSetup containerIn) {
		super(containerIn);
		container = containerIn;
		thrustersAmount = container.tile.getThrustersAmount();
		World world = container.tile.getWorld();
		BlockPos pos = container.tile.getPos();
		IBlockState state = world.getBlockState(pos);
		ShipEquipmentBlock shEB = (ShipEquipmentBlock) state.getBlock();
		directions = shEB.getDirections(state, pos);
	}

	public void initGui() {
		super.initGui();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		for (int i = 0; i < thrustersAmount; i++) {
			GuiLabel thrusterNumberlabel = new GuiLabel(fontRenderer, i, x + 10, y + THRUSTER_ROW_HEIGHT * i+4, 150, 20, 0xffaa00);
			thrusterNumberlabel.addLine(I18n.format("ts.thruster_N", i + 1) + directions[i]);
			this.labelList.add(thrusterNumberlabel);
		}
		for (int thruster = 0; thruster < thrustersAmount; thruster++) {
			for (int row = 0; row < 6; row++) {
				this.buttonList.add(new GuiTexturedButton(row + 1 + thruster * TSConstants.CONTROLS_AMOUNT,
						x + 15 + BUTTON_WIDTH * row, y + THRUSTER_ROW_HEIGHT * thruster + 20, BUTTON_WIDTH, BUTTON_HEIGHT, "",
						108 + BUTTON_WIDTH * row, 128 + BUTTON_WIDTH * row, 166, 185, 185, 185, 204, 204));
			}
			for (int row = 0; row < 6; row++) {
				this.buttonList.add(new GuiTexturedButton(row + 7 + thruster * TSConstants.CONTROLS_AMOUNT,
						x + 15 + BUTTON_WIDTH * row, y + THRUSTER_ROW_HEIGHT * thruster + BUTTON_WIDTH + 20, BUTTON_WIDTH, BUTTON_HEIGHT, "",
						108 + BUTTON_WIDTH * row, 128 + BUTTON_WIDTH * row, 204, 223, 223, 223, 242, 242));
			}
		}
		for (GuiButton button : buttonList) {
			if (button.id == 0)
				continue;
			int id = button.id - 1;
			int control = id % TSConstants.CONTROLS_AMOUNT;
			int thruster = id / TSConstants.CONTROLS_AMOUNT;
			GuiTexturedButton buttonA = (GuiTexturedButton) button;
			buttonA.activated = (container.tile.getThrustersControls()[thruster] >> control & 1) != 0;
		}
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 0)
			return;
		int id = button.id - 1;
		int control = id % TSConstants.CONTROLS_AMOUNT;
		int thruster = id / TSConstants.CONTROLS_AMOUNT;
    	ClientNetworkHandler clientNetwork = (ClientNetworkHandler) network;
    	clientNetwork.switchThrusterControl(container.getPos(), thruster, control);
		
	}

}
