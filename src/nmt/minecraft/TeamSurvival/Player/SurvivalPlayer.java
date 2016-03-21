package nmt.minecraft.TeamSurvival.Player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.potion.PotionEffect;

import nmt.minecraft.TeamSurvival.TeamSurvivalManager;
import nmt.minecraft.TeamSurvival.TeamSurvivalPlugin;
import nmt.minecraft.TeamSurvival.Session.GameSession.State;

/**
 * A wrapper class for someone playing the game.<br />
 * @author Skyler
 * @author William
 */
public class SurvivalPlayer implements Listener {
	
	/**
	 * The UUID of the player we're following
	 */
	private UUID playerID;
	
	/**
	 * This stores the location of the player before the game begins.
	 * At the end of a game, the player is returned to this location.
	 */
	private Location preGameLocation;
	
	
	/**
	 * Creates a new wrapper survival player.<br />
	 * Since we only want one survival player per player, we make this private and use static getter
	 * @param player The player to wrap around
	 */
	public SurvivalPlayer(OfflinePlayer player) {
		//Only need the Player User ID
		this.playerID = player.getUniqueId();
		this.preGameLocation = null;
		Bukkit.getPluginManager().registerEvents(this, TeamSurvivalPlugin.plugin);
	}
	
	/**
	 * This method sets the Pre-Game location of the Player<br />
	 * Remember that this is where the player is returned to after a 
	 * game session.
	 * @param location The Pre-Game location.
	 */
	protected void setPreGameLocation(Location location) {
		this.preGameLocation = location;
	}
	
	/**
	 * This method teleports the player to the set PreGame Location<br />
	 */
	private void returnToPreGame() {
		Player p = this.getPlayer();

		//Check to ensure that the player's pregame location was set.
		if (this.preGameLocation == null) {
			System.out.println("ERROR: " + this.getOfflinePlayer().getName() + "'s location was not set!");
			return;
		}
		//Only teleport the player if they are actively in the game.
		if (p != null) {
			p.teleport(this.preGameLocation);
			p.setLevel(0);
			p.setGameMode(GameMode.SURVIVAL);
			
			//clear effects
			for(PotionEffect e : p.getActivePotionEffects()){
				p.removePotionEffect(e.getType());
			}
			//heal the player
			this.healPlayer();
		}
	}
	
	/**
	 * Heals the player to max health
	 */
	public void healPlayer(){
		Player p = this.getPlayer();
		if(p!=null){
			p.setHealth(p.getMaxHealth());
		}
	}
	
	public void setGamemode(GameMode gamemode){
		Player p = this.getPlayer();
		if(p!=null){
			p.setGameMode(gamemode);
		}
	}
	
	/**
	 * @see Team#win()
	 * TODO Add more 'winning' content
	 */
	protected void win() {
		//Send the Player a winning message
		this.sendMessage(ChatColor.BLUE + "Congratulations! Your team won!" + ChatColor.RESET);
		Player p = getPlayer();
		this.returnToPreGame(); //Send then back to their pregame location
		if (p != null) {
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0);
			p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1, 0);
		}
	}
	
	/**
	 * @see Team#lose()
	 * TODO Add more 'losing' content
	 */
	protected void lose() {
		//Send the Player a winning message
		this.sendMessage(ChatColor.DARK_RED + "Your team lost!" + ChatColor.RESET);
		Player p = getPlayer();
		this.returnToPreGame(); //Send them back to their pregame location
		if (p != null) {
			p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_STARE, 1, 0);
		}
	}
	
	/**
	 * This method sends a message to the player.<br />
	 * If no player exists for this class then nothing is performed.
	 * @param message The string message to send to the player.
	 */
	protected void sendMessage(String message) {
		Player p = this.getPlayer();
		if (p != null) {
			p.sendMessage(message);
		}
	}
	
	/**
	 * This method plays a sound to the player using default pitch and volume.<br />
	 * If no player exists for this class then nothing is performed.
	 * @param sound The sound to be played.
	 */
	protected void playSound(Sound sound) {
		Player p = this.getPlayer();
		if (p != null) {
			p.playSound(p.getLocation(), sound, 1, 0);
		}
	}
	
	/**
	 * This method plays a sound to the player using the specified pitch and volume.<br />
	 * If no player exists for this class then nothing is performed.
	 * @param sound The sound to be played.
	 * @param volume The volume of the sound (default value is 1).
	 * @param pitch The pitch of the sound (default value is 0).
	 */
	protected void playSound(Sound sound, float volume, float pitch) {
		Player p = this.getPlayer();
		if (p != null) {
			p.playSound(p.getLocation(), sound, volume, pitch);
		}
	}
	
	/**
	 * This method teleports a player to a given location.<br />
	 * If no player exists for this class then nothing is performed.
	 * @param location The location to send the SurvivalPlayer to.
	 */
	protected void teleportPlayer(Location location) {
		Player p = this.getPlayer();
		if (p != null) {
			p.teleport(location);
		}
	}
	
	/**
	 * Looks up and returns the player wrapped by this object.<br />
	 * We have to do it this way in case someone logs off, then back in; their 'Player' id/reference to their Player is now broken.
	 * @return The OfflinePlayer associated with this player
	 * @note don't forget to make sure the player's online
	 * @see OfflinePlayer
	 */
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(this.playerID);
	}
	
	/**
	 * This method performs a UUID lookup with bukkit to return a Player class.<br />
	 * <b>WARNING</b>: This method returns <i>null</i> if no Player class could be returned.
	 * @return A the Player class or null if no player could be found.
	 */
	public Player getPlayer() {
		return Bukkit.getPlayer(this.playerID);
	}
	
	//Made it so the player receives no EXP, because the money/EXP is now given elsewhere
	@EventHandler
	public void onExpPickup(PlayerExpChangeEvent e) {
		if (e.getPlayer().getUniqueId().equals(playerID)
				&& TeamSurvivalManager.getSession(TeamSurvivalManager.getTeam(TeamSurvivalManager.getPlayer(e.getPlayer()))) != null
				&& TeamSurvivalManager.getSession(TeamSurvivalManager.getTeam(TeamSurvivalManager.getPlayer(e.getPlayer()))).getState() == State.INWAVE) {
			
			//int amount = e.getAmount();
			e.setAmount(0);
			//e.getPlayer().setLevel(e.getPlayer().getLevel() + amount);
		}
	}
	
	
}
