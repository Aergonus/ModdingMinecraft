package me.spellchaser.rwar.modules.combat;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import me.spellchaser.rwar.Rwar;
import me.spellchaser.rwar.modulebase.ModType;
import me.spellchaser.rwar.modulebase.ModuleBase;
import me.spellchaser.rwar.tools.GhostEntityProjectile;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.util.AxisAlignedBB;
//import net.minecraft.src.*;
//import net.minecraft.src.MyClient.Config;
//import net.minecraft.src.MyClient.Util.Util;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class LaserSight extends ModuleBase {

	public LaserSight() 
	{
		super("Projectile Predictor", Keyboard.KEY_P, "Shoot a laser showing the projectile path", 0, ModType.COMBAT);
	}
	
	@Override
	public void onRender()
	{
		EntityClientPlayerMP player = mc.thePlayer;
		if (player.getCurrentEquippedItem() != null && isEnabled()) 
		{ 		
			Item currentItem = player.getCurrentEquippedItem().getItem();
			if (this.isThrowable(currentItem, player)) 
			{
				GhostEntityProjectile projectile = new GhostEntityProjectile(player.worldObj);
				projectile.setupInitialThrowableValue(player, currentItem);
			
	            GL11.glPushMatrix();
	            enableDefaults();

	            MovingObjectPosition hasHit = null;
	            double gravity = this.getGravity(currentItem);

	            do {
	            	//Account for Minecraft's position interpolation. Minecraft interpolates render positions from "real" positions for lag compensation.
	            	hasHit = projectile.onUpdate(gravity); 
	            	double rx = projectile.posX * 1.0D - RenderManager.renderPosX;
	                double ry = projectile.posY * 1.0D - RenderManager.renderPosY;
	                double rz = projectile.posZ * 1.0D - RenderManager.renderPosZ;
	                //Render to given coordinates
	                GL11.glVertex3d(rx, ry, rz);
	                //Simulate projectile movement
	                if (player.getDistance(projectile.posX, projectile.posY, player.posZ) > 160) {break;} 
	            } while (hasHit == null);
	            
	            GL11.glEnd();
	            disableDefaults();
	            
	            GL11.glTranslated(- player.posX, - player.posY, - player.posZ);
	            if (player.getDistance(projectile.posX, projectile.posY, player.posZ) < 160) ESP(hasHit);
	            GL11.glTranslated(player.posX, player.posY, player.posZ);
	            GL11.glPopMatrix();
				}
			}
		}
		
		

      //float power = (float)(0x11940 - Rwar.getMinecraft().thePlayer.getItemInUseCount())/ 20F;
      //0x11940 is this.getMaxItemUseDuration(par1ItemStack); which is 72000 or how long it takes to use or consume an item
      
/**
    GL11.glEnd();

    AxisAlignedBB bbox = new AxisAlignedBB(cordX - 0.5 - RenderManager.renderPosX, cordY - 0.5 - RenderManager.renderPosY, cordZ - 0.5 - RenderManager.renderPosZ, (cordX - 0.5 - RenderManager.renderPosX) + 1, (cordY - 0.5 - RenderManager.renderPosY) + 1, (cordZ - 0.5 - RenderManager.renderPosZ) + 1);
    
    GL11.glTranslated(x  - RenderManager.renderPosX, y  - RenderManager.renderPosY, z  - RenderManager.renderPosZ);
    GL11.glRotatef(mc.thePlayer.rotationYaw, 0.0F, (float) (y  - RenderManager.renderPosY), 0.0F);
    GL11.glTranslated(-(x  - RenderManager.renderPosX), -(y  - RenderManager.renderPosY), -(z  - RenderManager.renderPosZ));
    Utils.drawESP(x - 0.35 - RenderManager.renderPosX, y - 0.5 - RenderManager.renderPosY, z - 0.5 - RenderManager.renderPosZ, r, b, g);
    disableDefaults();
    GL11.glPopMatrix();
    */


public void enableDefaults() {
GL11.glDisable(GL11.GL_LIGHTING);
GL11.glEnable(GL11.GL_LINE_SMOOTH);
GL11.glBlendFunc(770, 771);
GL11.glEnable(3042);
GL11.glDisable(3553);
GL11.glDisable(2929);
GL11.glEnable(GL13.GL_MULTISAMPLE);
GL11.glDepthMask(false);

GL11.glLineWidth(1.0F);
GL11.glEnable(GL11.GL_LINE_SMOOTH);
GL11.glDisable(GL11.GL_TEXTURE_2D);
GL11.glDisable(GL11.GL_LIGHTING);
GL11.glEnable(GL11.GL_BLEND);
GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
GL11.glColor4f(0f, 1f, 0f, 0.75f);
GL11.glBegin(GL11.GL_LINE_STRIP);

}

public void disableDefaults() {
GL11.glDisable(3042);
GL11.glEnable(3553);
GL11.glEnable(2929);
GL11.glDisable(GL13.GL_MULTISAMPLE);
GL11.glDepthMask(true);
GL11.glDisable(GL11.GL_LINE_SMOOTH);
GL11.glEnable(GL11.GL_LIGHTING);

GL11.glDisable(GL11.GL_BLEND);
GL11.glEnable(GL11.GL_TEXTURE_2D);
GL11.glDisable(GL11.GL_LINE_SMOOTH);
GL11.glEnable(GL11.GL_LIGHTING);
GL11.glColor4f(1f, 1f, 1f, 1f);


//AxisAlignedBB bbox = new AxisAlignedBB(projectile.posX - 0.5 - RenderManager.renderPosX, projectile.posY - 0.5 - RenderManager.renderPosY, projectile.posZ - 0.5 - RenderManager.renderPosZ, (projectile.posX - 0.5 - RenderManager.renderPosX) + 1, (projectile.posY - 0.5 - RenderManager.renderPosY) + 1, (projectile.posZ - 0.5 - RenderManager.renderPosZ) + 1);

//GL11.glTranslated(projectile.posX  - RenderManager.renderPosX, projectile.posY  - RenderManager.renderPosY, projectile.posZ  - RenderManager.renderPosZ);
//GL11.glRotatef(mc.thePlayer.rotationYaw, 0.0F, (float) (projectile.posY  - RenderManager.renderPosY), 0.0F);
//GL11.glTranslated(-(projectile.posX  - RenderManager.renderPosX), -(projectile.posY  - RenderManager.renderPosY), -(projectile.posZ  - RenderManager.renderPosZ));


}

	

	private boolean isThrowable(Item item, EntityClientPlayerMP player) 
	{
	    return item instanceof ItemBow 
    		|| item instanceof ItemSnowball
            || item instanceof ItemEgg 
            || item instanceof ItemEnderPearl
            || item instanceof ItemExpBottle
            || (item instanceof ItemPotion && ((ItemPotion)item).isSplash(player.getCurrentEquippedItem().getItemDamage()));
    }
	
    private double getGravity(Item item) {return item instanceof ItemBow ? 0.05D : 0.03D;}
	
    public static void ESP(MovingObjectPosition mop){
/**    	
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    
    	//Enabled to draw block
    	GL11.glEnable(3042);
    	GL11.glBlendFunc(770, 771);
    	*/
        GL11.glLineWidth(1.0F);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

    	 


    	//this will let us see all the way through the block
    	GL11.glDisable(2929); 
    	GL11.glDepthMask(false);
    	//end of code that will let us see through it
    	//*/

    	//draws the outside of the box
    	if (mop.entityHit == null) 
    	{
    		GL11.glColor4d(0, 255, 100, 1F);
    		drawOutlinedBoundingBox(new AxisAlignedBB(mop.blockX + 1.0, mop.blockY + 1.0, mop.blockZ + 1.0, mop.blockX, mop.blockY, mop.blockZ));
    		GL11.glColor4d(0F, 255F, 100F, .4F);
    		drawBoundingBox(new AxisAlignedBB(mop.blockX + 1.0, mop.blockY + 1.0, mop.blockZ + 1.0, mop.blockX, mop.blockY, mop.blockZ));
    	} else 
    	{
    		GL11.glColor4d(255, 0, 0, 1F);
    		drawOutlinedBoundingBox(mop.entityHit.boundingBox);
    		GL11.glColor4d(255, 0, 0, .4F);
    		drawBoundingBox(mop.entityHit.boundingBox);
    	}
    	
    	GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(2929); 
    	GL11.glDepthMask(true);
    	
    	/**
    	//enables all the stuff we disabled earlier
    	GL11.glDisable(GL11.GL_LINE_SMOOTH);
    	GL11.glEnable(GL11.GL_LIGHTING); 
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
    	//GL11.glEnable(2929); 
    	GL11.glDepthMask(true);
    	GL11.glDisable(3042);
	    GL11.glDisable(GL11.GL_BLEND);
    	*/


    	}
    

    /**
     * Draws lines for the edges of the bounding box.
     */
    private static void drawOutlinedBoundingBox(final AxisAlignedBB par1AxisAlignedBB) {
	Tessellator var2 = Tessellator.instance;
	var2.startDrawing(3);
	var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY,
		par1AxisAlignedBB.minZ);
	var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY,
		par1AxisAlignedBB.minZ);
	var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY,
		par1AxisAlignedBB.maxZ);
	var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY,
		par1AxisAlignedBB.maxZ);
	var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY,
		par1AxisAlignedBB.minZ);
	var2.draw();
	var2.startDrawing(3);
	var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY,
		par1AxisAlignedBB.minZ);
	var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY,
		par1AxisAlignedBB.minZ);
	var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY,
		par1AxisAlignedBB.maxZ);
	var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY,
		par1AxisAlignedBB.maxZ);
	var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY,
		par1AxisAlignedBB.minZ);
	var2.draw();
	var2.startDrawing(1);
	var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY,
		par1AxisAlignedBB.minZ);
	var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY,
		par1AxisAlignedBB.minZ);
	var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY,
		par1AxisAlignedBB.minZ);
	var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY,
		par1AxisAlignedBB.minZ);
	var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY,
		par1AxisAlignedBB.maxZ);
	var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY,
		par1AxisAlignedBB.maxZ);
	var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY,
		par1AxisAlignedBB.maxZ);
	var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY,
		par1AxisAlignedBB.maxZ);
	var2.draw();
    }
    private static void drawBoundingBox(AxisAlignedBB axisalignedbb)
    {

        Tessellator tessellator = Tessellator.instance;
  tessellator.startDrawingQuads(); //starts x
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ); 
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.draw();
  tessellator.startDrawingQuads();
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.draw(); //ends x
  tessellator.startDrawingQuads(); //starts y  
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);  
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.draw();
  tessellator.startDrawingQuads();  
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);  
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
  tessellator.draw(); //ends y
  tessellator.startDrawingQuads(); //starts z
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ); 
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.draw();
  tessellator.startDrawingQuads();
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
  tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
  tessellator.draw(); //ends z
    }


/**
		

	switch (itemID){
	case 261:
		
	}
	@Override
    public String call(Minecraft mc) {
        if(!enabled)return null;
        if(Config.instance.getPlayer().getCurrentEquippedItem() == null) return null;
        Item i = Config.instance.getPlayer().getCurrentEquippedItem().getItem();
        if(i instanceof ItemBow){

            
        }else if(i instanceof ItemSnowball || i instanceof ItemEgg || i instanceof ItemEnderPearl || i instanceof ItemExpBottle || 
        		(i instanceof ItemPotion && ((ItemPotion)i).isSplash(Config.instance.getPlayer().getCurrentEquippedItem().getItemDamage()))){
            traceThrowable(mc);
        }
        return null;
    }
**/

}

//private ArrayList<Vector3f> LaserDots = new ArrayList<Vector3f>();//Arraylist with datatype Vector3f
//    LaserDots.add(new Vector3f(this.posX, this.posY, this.posZ);//Add a dot at the current arrow location

/**
glEnable(GL_BLEND);//Blend in the lines into the background to make them look pretty
glDisable(GL_TEXTURE_2D);//Disable texture rendering so we can render plain colors
glEnable(GL_LINE_SMOOTH);//Smooth the lines
glColor4f(1, 1, 1, 1);//Set the color to white
glLineWidth(1);//Set the line width to 1
glBegin(gl_line_strip);//Begin drawing lines
for(Vertex3f v: BreadCrumbs)//For every crumb in BreadCrumbs
{
    glVertex3D(v.x, v.y, v.z);//Add a vertex to draw a line to
}
glEnd();//End drawing lines
glDisable(GL_LINE_SMOOTH);//Change anything we changed earlier back so It doesn't mess up rendering later on.
glEnable(GL_TEXTURE_2D);
glDisable(GL_BLEND);

*/