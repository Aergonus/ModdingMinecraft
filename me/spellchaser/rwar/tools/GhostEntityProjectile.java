package me.spellchaser.rwar.tools;

import java.util.List;

import org.lwjgl.opengl.GL11;

import me.spellchaser.rwar.Rwar;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class GhostEntityProjectile extends EntityArrow implements IProjectile
{	
	//START OF ORIGINAL FUNCTION
	public int refX = -1;
    public int refY = -1;
    public int refZ = -1;
    private Block refBlock;
    private int inData;
    private boolean inGround;

    /** The owner of this arrow. */ 
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private int ticksInWater;
    private int ticksInFlight;
    
    //Original Constructor: On Release of Bow
    public GhostEntityProjectile(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = Rwar.getMinecraft().thePlayer;
    	// These assignments setup the initial values
        this.setSize(0.5F, 0.5F);
    	float powerModifier = (float) ( 72000 //Rwar.getMinecraft().thePlayer.getItemInUse().getMaxItemUseDuration() 
    						  		  - Rwar.getMinecraft().thePlayer.getItemInUseCount())
    						  		  / 20.0F;
    	powerModifier = (powerModifier * powerModifier + powerModifier * 2.0F) * 2.0F / 3.0F;
    	
    	if ((double)powerModifier < 0.2D) {return;}
    	else {if (powerModifier > 2.0F) {powerModifier = 2.0F;}}

        this.setLocationAndAngles(Rwar.getMinecraft().thePlayer.posX, 
        						  Rwar.getMinecraft().thePlayer.posY + (double)Rwar.getMinecraft().thePlayer.getEyeHeight(), 
        						  Rwar.getMinecraft().thePlayer.posZ, 
        						  Rwar.getMinecraft().thePlayer.rotationYaw, 
        						  Rwar.getMinecraft().thePlayer.rotationPitch);
        this.yOffset = 0.0F;
        double randMotionX = this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        double randMotionY = this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        double randMotionZ = this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setPerfectThrowableHeading(this.motionX, this.motionY, this.motionZ, powerModifier * 1.5F, 1.0F);
        //this.setRandomThrowableHeading(this.randMotionX, this.randMotionY, this.randMotionZ, powerModifier * 1.5F, 1.0F);
    	this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
    	this.posY -= 0.10000000149011612D;
    	this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
    	this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    public GhostEntityProjectile(World par1World)
    {
    	   super(par1World);
    }
	
	@Override
	protected void entityInit() 
	{
		//Used to create a Datawatcher. We don't want to have any server interaction.
	}
    
    /**
     * Called to update the entity's position/logic.
     */
    public MovingObjectPosition onUpdate(double gravity)
    {
        //Returns the Block that is at offset -1, -1, -1 
        Block hitBlock = this.worldObj.func_147439_a(this.refX, this.refY, this.refZ);

        //If the block has the quality of AIR, this is ignored
        if (hitBlock.func_149688_o() != Material.field_151579_a)
        {
            hitBlock.func_149719_a(this.worldObj, this.refX, this.refY, this.refZ);
            AxisAlignedBB var2 = hitBlock.func_149668_a(this.worldObj, this.refX, this.refY, this.refZ);

            if (var2 != null && var2.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ)))
            {
                return null;
            }
        }
        
        //If done in ground 
        if (this.inGround)
        {	
        	//Seems to check if it's actually the same block
            int refMetaData = this.worldObj.getBlockMetadata(this.refX, this.refY, this.refZ);

            if (hitBlock == this.refBlock && refMetaData == this.inData)
            {
            	// It's already in the ground therefore we won't use this
            }
            	// If it's not really in the ground, move a little more  
            else
            {
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        }
        
        //Not in Ground
        else
        {

        	//One more tick in the Air
            ++this.ticksInAir;
            Vec3 curPOS = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
            Vec3 nextPOS = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            // Initial detection, is overwritten is there is an entity
            MovingObjectPosition hitMOP = this.worldObj.func_147447_a(curPOS, nextPOS, false, true, false);
            curPOS = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
            nextPOS = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            
            //Sets nextPOS vector to the hitVectorCoords
            if (hitMOP != null)
            {
                nextPOS = this.worldObj.getWorldVec3Pool().getVecFromPool(hitMOP.hitVec.xCoord, hitMOP.hitVec.yCoord, hitMOP.hitVec.zCoord);
            }

            // Sets the hitEntity if the entity collides with the intercept vector
            // Start of Hit detection
            Entity hitEntity = null;
            List listOfEntities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            //Loop through List of Entities within the arrow's bounding box expanded to include next movement
            for (int eCount = 0; eCount < listOfEntities.size(); ++eCount)
            {
                Entity curEntityCheck = (Entity)listOfEntities.get(eCount);

                if (curEntityCheck.canBeCollidedWith() && (curEntityCheck != this.shootingEntity || this.ticksInAir >= 5))
                {
                    float arrowBBExpansion = 0.3F;
                    AxisAlignedBB var12 = curEntityCheck.boundingBox.expand((double)arrowBBExpansion, (double)arrowBBExpansion, (double)arrowBBExpansion);
                    MovingObjectPosition interceptVector = var12.calculateIntercept(curPOS, nextPOS);
                    // If exists an intercept vector run this
                    if (interceptVector != null)
                    {
                    	hitEntity = curEntityCheck;
                    }
                }
            }
            // If there is an entity => MovingObjectPosition is moved to the entity position?
            // hitEntity is an entity in the looped list
            // hitMOP is set here
            if (hitEntity != null)
            {
                hitMOP = new MovingObjectPosition(hitEntity);
                //Sets hitMOP.entityHit
            }
            // Cases where it doesn't hit and should continue
            if (hitMOP != null && hitMOP.entityHit != null && hitMOP.entityHit instanceof EntityPlayer)
            {
                EntityPlayer var20 = (EntityPlayer)hitMOP.entityHit;

                if (var20.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(var20))
                {
                    hitMOP = null;
                }
            }

// If hitMOP is null, runs Post movement calculations
// All Hit calculations are finally done here
            if (hitMOP != null)
            {
            	// If it hits an entity at all do this
                if (hitMOP.entityHit != null)
                {	
                	//Yadayada used to check if hit an entity 
                	// Tech don't need if don't want to check invulnerability
                    DamageSource var21 = DamageSource.causeArrowDamage(this, this.shootingEntity);

                    if (hitMOP.entityHit.attackEntityFrom(var21, 0.0F))
                    {
                    	return hitMOP;
                        //this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    }
                    else
                    {
                    	//Invulnerable reverse direction 
                        this.motionX *= -0.10000000149011612D;
                        this.motionY *= -0.10000000149011612D;
                        this.motionZ *= -0.10000000149011612D;
                        this.ticksInAir = 0;
                        return hitMOP;
                    }
                }
                else
                // Hits a block and executes this!
                {
                    this.refX = hitMOP.blockX;
                    this.refY = hitMOP.blockY;
                    this.refZ = hitMOP.blockZ;
                    this.refBlock = hitBlock;
                    this.inData = this.worldObj.getBlockMetadata(this.refX, this.refY, this.refZ);
                    this.motionX = (double)((float)(hitMOP.hitVec.xCoord - this.posX));
                    this.motionY = (double)((float)(hitMOP.hitVec.yCoord - this.posY));
                    this.motionZ = (double)((float)(hitMOP.hitVec.zCoord - this.posZ));
                    float var19 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double)var19 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double)var19 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double)var19 * 0.05000000074505806D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    if (this.refBlock.func_149688_o() != Material.field_151579_a)
                    {
                        this.refBlock.func_149670_a(this.worldObj, this.refX, this.refY, this.refZ, this);
                    }
                    
                    
                    return hitMOP;
                    
                    
                    
                }
            }
            
          //These calculations are called every tick
          //Update movement
          this.posX += this.motionX;
          this.posY += this.motionY;
          this.posZ += this.motionZ;

          if (this.isInWater()) {++ticksInWater;} else {++ticksInFlight;}
          float dragforce = isInWater() ? 0.8F : 0.99F;

          this.motionX *= (double)dragforce;
          this.motionY *= (double)dragforce;
          this.motionZ *= (double)dragforce;
          this.motionY -= (double)gravity;
          this.setPosition(this.posX, this.posY, this.posZ);
          this.func_145775_I(); //Updates boundingBox

          
        }
        return null;
    }

	public void setupInitialThrowableValue(EntityClientPlayerMP player, Item currentItem)
	{
        this.posX = player.lastTickPosX 
        		+ (player.posX - player.lastTickPosX) * (double) Rwar.getMinecraft().timer.renderPartialTicks
                - (double) (MathHelper.cos((float) Math.toRadians((double) player.rotationYaw)) * 0.16F);
        this.posY = player.lastTickPosY
                + (player.posY - player.lastTickPosY) * (double) Rwar.getMinecraft().timer.renderPartialTicks
                + (double) player.getEyeHeight() - 0.100149011612D;
        this.posZ = player.lastTickPosZ
                + (player.posZ - player.lastTickPosZ) * (double) Rwar.getMinecraft().timer.renderPartialTicks
                - (double) (MathHelper.sin((float) Math.toRadians((double) player.rotationYaw)) * 0.16F);
        float constant = currentItem instanceof ItemBow ? 1.0F : 0.4F;
        this.motionX = (double) (-MathHelper.sin((float) Math.toRadians((double) player.rotationYaw))
                * MathHelper.cos((float) Math.toRadians((double) player.rotationPitch)) * constant);
        this.motionZ = (double) (MathHelper.cos((float) Math.toRadians((double) player.rotationYaw))
                * MathHelper.cos((float) Math.toRadians((double) player.rotationPitch)) * constant);
        this.motionY = (double) (-MathHelper.sin((float) Math.toRadians((double) player.rotationPitch)) * constant);
        
        double motionMagnitude = Math.sqrt(this.motionX * this.motionX
                						   + this.motionY * this.motionY
                						   + this.motionZ * this.motionZ);
        
        this.motionX /= motionMagnitude;
        this.motionY /= motionMagnitude;
        this.motionZ /= motionMagnitude;
		
        if (currentItem instanceof ItemBow) 
        {
            float powerModifier = (float) (72000 - player.getItemInUseCount()) / 20.0F;
            powerModifier = (powerModifier * powerModifier + powerModifier * 2.0F) ;
            if ((double)powerModifier < 0.3D || powerModifier > 3.0F) {powerModifier = 3.0F;}

            this.motionX *= (double) powerModifier;
            this.motionY *= (double) powerModifier;
            this.motionZ *= (double) powerModifier;
        } else 
        {
            this.motionX *= 1.5D;
            this.motionY *= 1.5D;
            this.motionZ *= 1.5D;
        }

	}

	/**
	 * Points the throwable entity in a x, y, z direction
	 */
	public void setPerfectThrowableHeading(double xCord, double yCord, double zCord, float calculatedPowerMultiplier, float constantMultiplier)
	{
	    float distance = MathHelper.sqrt_double(xCord * xCord + yCord * yCord + zCord * zCord);
	    xCord /= (double)distance;
	    yCord /= (double)distance;
	    zCord /= (double)distance;
	    xCord *= (double)calculatedPowerMultiplier;
	    yCord *= (double)calculatedPowerMultiplier;
	    zCord *= (double)calculatedPowerMultiplier;
	    this.motionX = xCord;
	    this.motionY = yCord;
	    this.motionZ = zCord;
	    float xzDistance = MathHelper.sqrt_double(xCord * xCord + zCord * zCord);
	    this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(xCord, zCord) * 180.0D / Math.PI);
	    this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(yCord, (double)xzDistance) * 180.0D / Math.PI);
	    this.ticksInGround = 0;
	}

	/**
	 * Points the throwable entity in a x, y, z direction
	 */
	public void setRandomThrowableHeading(double xCord, double yCord, double zCord, float calculatedPowerMultiplier, float constantMultiplier)
	{
	    float distance = MathHelper.sqrt_double(xCord * xCord + yCord * yCord + zCord * zCord);
	    xCord /= (double)distance;
	    yCord /= (double)distance;
	    zCord /= (double)distance;
	    xCord += 0.007499999832361937D * (double)constantMultiplier;
	    yCord += 0.007499999832361937D * (double)constantMultiplier;
	    zCord += 0.007499999832361937D * (double)constantMultiplier;
	    xCord *= (double)calculatedPowerMultiplier;
	    yCord *= (double)calculatedPowerMultiplier;
	    zCord *= (double)calculatedPowerMultiplier;
	    //this.randMotionX = xCord;
	    //this.randMotionY = yCord;
	    //this.randMotionZ = zCord;
	}

	/**
	 * Sets the Location and Yaw/Pitch of an entity in the world
	 */
	public void setLocationAndAngles(double par1, double par3, double par5, float par7, float par8)
	{
	    this.lastTickPosX = this.prevPosX = this.posX = par1;
	    this.lastTickPosY = this.prevPosY = this.posY = par3 + 0.0D;
	    this.lastTickPosZ = this.prevPosZ = this.posZ = par5;
	    this.rotationYaw = par7;
	    this.rotationPitch = par8;
	    this.setPosition(this.posX, this.posY, this.posZ);
	}
    
	/**
	 * Sets the x,y,z of the entity from the given parameters and then sets up the bounding box.
	 */
	public void setPosition(double xPos, double yPos, double zPos)
	{
	    this.posX = xPos;
	    this.posY = yPos;
	    this.posZ = zPos;
	    float widthCalculation = this.width / 2.0F;
	    float heightCalculation = this.height;
	    this.boundingBox.setBounds(xPos - (double)widthCalculation, 
	    						   yPos - (double)this.yOffset + (double)this.ySize, 
	    						   zPos - (double)widthCalculation, xPos + (double)widthCalculation, 
	    						   yPos - (double)this.yOffset + (double)this.ySize + (double)heightCalculation, 
	    						   zPos + (double)widthCalculation);
	}

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction. Original Minecraft Method
     */
	@Override
	public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
    {
        float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= (double)var9;
        par3 /= (double)var9;
        par5 /= (double)var9;
        par1 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)par8;
        par3 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)par8;
        par5 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)par8;
        par1 *= (double)par7;
        par3 *= (double)par7;
        par5 *= (double)par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)var10) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    /**
     * NBT Information, we won't write this entity to be saved
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        this.refX = par1NBTTagCompound.getShort("xTile");
        this.refY = par1NBTTagCompound.getShort("yTile");
        this.refZ = par1NBTTagCompound.getShort("zTile");
        this.ticksInGround = par1NBTTagCompound.getShort("life");
        this.refBlock = Block.func_149729_e(par1NBTTagCompound.getByte("inTile") & 255);
        this.inData = par1NBTTagCompound.getByte("inData") & 255;
        this.inGround = par1NBTTagCompound.getByte("inGround") == 1;
    }	

	/**
	 * NBT Information, we won't write this entity to be saved
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.setShort("xTile", (short)this.refX);
        par1NBTTagCompound.setShort("yTile", (short)this.refY);
        par1NBTTagCompound.setShort("zTile", (short)this.refZ);
        par1NBTTagCompound.setShort("life", (short)this.ticksInGround);
        par1NBTTagCompound.setByte("inTile", (byte)Block.func_149682_b(this.refBlock));
        par1NBTTagCompound.setByte("inData", (byte)this.inData);
    }
	/**
	 *xTile: X coordinate of the item's position in the chunk.
	 *yTile: Y coordinate of the item's position in the chunk.
	 *zTile: Z coordinate of the item's position in the chunk.
	 *inTile: ID of tile projectile is in.
	 *inGround: 1 or 0 (true/false) - If the Projectile is in the ground or hit the ground already (For arrow pickup; you cannot pickup arrows in the air)
	 *EntityArrow has these additional fields:
	 *inData: Metadata of tile arrow is in.
	 *pickup: 0 = cannot be picked up by players. 1 = can be picked up by players in survival or creative. 2 = can only be picked up by players in creative.
	 *player: 1 or 0 (true/false) - If pickup is not used, and this is true, the arrow can be picked up by players.
	 *life: Increments each tick when an arrow is not moving; resets to 0 if it moves. When it ticks to 1200, the arrow despawns.
	 *damage: Unknown how this affects actual damage inflicted by the arrow. May not be a whole number. 2.0 for normal arrows, and increased 0.5 per level of Power enchantment on the firing bow. If the Power enchantment is present, an additional 0.5 is added on (so Power I gives a bonus of 1.0, while Power II gives 1.5).
 	 */
}

