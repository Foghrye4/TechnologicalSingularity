package technological_singularity.client.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import technological_singularity.TechnologicalSingularity;

public class GuiTexturedButton extends GuiButton {
	protected final static ResourceLocation TEXTURE = new ResourceLocation(TechnologicalSingularity.MODID, "textures/gui/background.png");
	int u1, u2, v1Disabled, v1Activated, v1Hovered, v2Disabled, v2Activated, v2Hovered;
	public boolean activated = false;	
	public GuiTexturedButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, 
			int u1In, int u2In, int v1DisabledIn,int v1ActivatedIn,int v1HoveredIn, int v2DisabledIn,int v2ActivatedIn,int v2HoveredIn) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		u1 = u1In;
		u2 = u2In; 
		v1Disabled = v1DisabledIn;
		v1Activated = v1ActivatedIn;
		v1Hovered = v1HoveredIn;
		v2Disabled = v2DisabledIn;
		v2Activated = v2ActivatedIn;
		v2Hovered = v2HoveredIn;
	}
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int v1 = v1Disabled;
            int v2 = v2Disabled;
            if(this.hovered) {
            	v1 = v1Hovered;
                v2 = v2Hovered;
            }
            else if(this.activated) {
            	v1 = v1Activated;
                v2 = v2Activated;
            }
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            
            this.drawTexturedModalRect(this.x, this.y, u1, v1, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, u2 - this.width / 2, v1, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x, this.y + this.height / 2, u1, v2 - this.height / 2, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y + this.height / 2, u2 - this.width / 2, v2 - this.height / 2, this.width / 2, this.height / 2);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
    }


}
