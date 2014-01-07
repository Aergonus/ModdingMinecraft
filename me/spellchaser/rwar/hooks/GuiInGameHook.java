package me.spellchaser.rwar.hooks;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import me.spellchaser.rwar.Rwar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GuiInGameHook extends GuiIngame{

	public GuiInGameHook(Minecraft par1Minecraft) {
		super(par1Minecraft);
	}
	
	/**
     * Renders the specified item of the inventory slot at the specified location. Args: slot, x, y, partialTick
     
    private void renderInventorySlots(int slot, int xcor, int ycor, float partialTick)
    {
        ItemStack var5 = this.Rwar.getMinecraft().thePlayer.inventory.mainInventory[par1];

        if (var5 != null)
        {
            float var6 = (float)var5.animationsToGo - par4;

            if (var6 > 0.0F)
            {
                GL11.glPushMatrix();
                float var7 = 1.0F + var6 / 5.0F;
                GL11.glTranslatef((float)(par2 + 8), (float)(par3 + 12), 0.0F);
                GL11.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float)(-(par2 + 8)), (float)(-(par3 + 12)), 0.0F);
            }

            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), var5, par2, par3);

            if (var6 > 0.0F)
            {
                GL11.glPopMatrix();
            }

            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), var5, par2, par3);
        }
    }
    */
	public void renderGameOverlay(float par1, boolean par2, int par3, int par4)
    {	
		super.renderGameOverlay(par1, par2, par3, par4); 
        ScaledResolution scaledresolution = new ScaledResolution(Rwar.getMinecraft().gameSettings, Rwar.getMinecraft().displayWidth, Rwar.getMinecraft().displayHeight);
        int x = scaledresolution.getScaledWidth();
        int y = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = Rwar.getMinecraft().fontRenderer;
        Rwar.getMinecraft().entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_BLEND); 
        InventoryPlayer inventoryplayer = Rwar.getMinecraft().thePlayer.inventory;
        int y1 = (int)(30D - (Math.pow(inventoryplayer.currentItem - 4, 2D) / 3D) * 2D);
        
        
        
        fontrenderer.drawStringWithShadow(Rwar.clientName() + " " + Rwar.clientVersion(), 2, 2, 0xffffffff);
        //fontrenderer.drawStringWithShadow("iteminuseunlocalizedname:"+iteminuseunlocalizedname+", itemunlocalizedname:"+itemunlocalizedname, 2 , y- 10, 0xffffffff);
        me.spellchaser.rwar.tools.DrawShapes.drawWonkyFilledCircle(x / 2, y + 12, 125D, 40D, 0x90000000);
        me.spellchaser.rwar.tools.DrawShapes.drawWonkyCircle(x / 2, y + 12, 125.25D, 40.25D, 0x90ffffff);
        me.spellchaser.rwar.tools.DrawShapes.drawWonkyCircle(x / 2, y + 12, 125.75D, 40.75D, 0x90ffffff);
        me.spellchaser.rwar.tools.DrawShapes.drawWonkyCircle(x / 2, y + 12, 126D, 41D, 0x90ffffff);
        me.spellchaser.rwar.tools.DrawShapes.drawWonkyCircle((x / 2 - 91 - 1) + inventoryplayer.currentItem * 20 + 12, (y - y1) + 4, 12D, 12D, 0x99ffffff);
        me.spellchaser.rwar.tools.DrawShapes.drawWonkyCircle((x / 2 - 91 - 1) + inventoryplayer.currentItem * 20 + 12, (y - y1) + 4, 12.25D, 12.25D, 0x99ffffff);
        me.spellchaser.rwar.tools.DrawShapes.drawWonkyCircle((x / 2 - 91 - 1) + inventoryplayer.currentItem * 20 + 12, (y - y1) + 4, 12.75D, 12.75D, 0x99ffffff);
        me.spellchaser.rwar.tools.DrawShapes.drawWonkyFilledCircle((x / 2 - 91 - 1) + inventoryplayer.currentItem * 20 + 12, (y - y1) + 4, 12D, 12D, 0x60000000);
        //me.spellchaser.rwar.tools.DrawShapes.drawWonkyCircle(x, y/2, 10D, 200D, 0xffffffff);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();

        
        
        for (int l6 = 0; l6 < 9; l6++)
        {
            int l7 = (x / 2 - 90) + l6 * 20 + 2;
            int l8 = y - (int)(30D - (Math.pow(l6 - 4, 2D) / 3D) * 2D) - 4 ;
            renderInventorySlot(l6, l7, l8, par1);
        }

    }
}
