package nmt.minecraft.TeamSurvival;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin class.<br />
 * Creates everything and all things and everything.
 * @author Skyler
 *
 */
public class TeamSurvivalPlugin extends JavaPlugin {
	
	public static JavaPlugin plugin;
	
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void onLoad() {
		TeamSurvivalPlugin.plugin = this;
	}
}
