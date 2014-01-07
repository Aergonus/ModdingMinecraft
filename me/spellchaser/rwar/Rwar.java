package me.spellchaser.rwar;

import me.spellchaser.rwar.modulebase.ModHooks;
import me.spellchaser.rwar.modulebase.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Rwar
{
	private static Minecraft mc = Minecraft.getMinecraft();
	private static ModuleManager modules;
	private static ModHooks modhooks;
	
	public static String clientName(){return "Rwar";}
	public static String clientVersion(){return "0.0";}
	
	public static void startUp()
	{	
		// If playing on a singleplayer world, sets the username to multiplayer username
		if (getMinecraft().getSession().getUsername().startsWith("Player"))
		   {getMinecraft().getSession().username = "Spellchaser";}
		try
		{
			modules = new ModuleManager(); 
			modhooks = new ModHooks();
		}
			catch (Exception exception){exception.printStackTrace();}
	}

    /**
     * @return Returns a instance of Minecraft.java
     */
	public static Minecraft getMinecraft() 
	{
		if(mc == null){mc = Minecraft.getMinecraft();}
		return mc;
	}
	
    /**
     * @return Returns the client's module handler
     */
	public static ModuleManager getModuleManager()
	{
		if( modules == null) {modules = new ModuleManager();}
		return modules;
	}
    /**
     * @return Returns the client's hooks into the original source code
     */	
	public static ModHooks getModHooks()
	{
		if( modhooks == null){modhooks = new ModHooks();}
		return modhooks;
	}
	
    /**
     * @return Return's the client's ScaledResolution to get the screen bounds
     */
    public ScaledResolution getResolution() {
	return new ScaledResolution(getMinecraft().gameSettings,
		getMinecraft().displayWidth, getMinecraft().displayHeight);
    }
}