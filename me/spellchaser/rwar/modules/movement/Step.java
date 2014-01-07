package me.spellchaser.rwar.modules.movement;

import org.lwjgl.input.Keyboard;

import me.spellchaser.rwar.Rwar;
import me.spellchaser.rwar.modulebase.ModType;
import me.spellchaser.rwar.modulebase.ModuleBase;

public class Step extends ModuleBase
{

	public Step() 
	{
		super("Step", Keyboard.KEY_B, "Automatically step up blocks", 0, ModType.MOVEMENT);
	}
	
	@Override
	public void onEnable()
	{
		//Simple automatic jump
		mc.thePlayer.stepHeight = 1.0F;

		// Bypass for NoCheat
		//if (mc.thePlayer.isCollided && block is not ladder or water or cobweb or half blocks) 
		//mc.thePlayer.motionY = 0.2D;
		//mc.thePlayer.setPosition(posX, posY = posY + 0.5D, posZ);
	}
	
	@Override
	public void onDisable()
	{
		mc.thePlayer.stepHeight = 0.5F;
	}
}
