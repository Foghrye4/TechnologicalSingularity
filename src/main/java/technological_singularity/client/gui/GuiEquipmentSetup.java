package technological_singularity.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.network.ClientNetworkHandler;

import static technological_singularity.TechnologicalSingularity.*;

public class GuiEquipmentSetup extends GuiContainer {

	protected final static ResourceLocation TEXTURE = new ResourceLocation(TechnologicalSingularity.MODID, "textures/gui/background.png");
    
	public GuiEquipmentSetup(ContainerEquipmentSetup inventorySlotsIn) {
		super(inventorySlotsIn);
		xSize = 256;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
	
    public void initGui()
    {
    	super.initGui();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.labelList.clear();
        this.buttonList.clear();
        this.buttonList.add(new GuiTexturedButton(0, x + this.xSize/2 - 75, y + this.ySize - 28, 150, 20, I18n.format("ts.launch"), 0, 88, 166, 166, 206, 206, 206, 246));
    }
	
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
            	ClientNetworkHandler clientNetwork = (ClientNetworkHandler) network;
            	clientNetwork.sendCommandAssembleShip(((ContainerEquipmentSetup)this.inventorySlots).getPos());
            }
        }
    }

}
