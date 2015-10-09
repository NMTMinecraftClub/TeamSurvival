package nmt.minecraft.TeamSurvival.Session;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import nmt.minecraft.TeamSurvival.TeamSurvivalPlugin;
import nmt.minecraft.TeamSurvival.IO.ChatFormat;
import nmt.minecraft.TeamSurvival.Map.Map;
import nmt.minecraft.TeamSurvival.Player.SurvivalPlayer;
import nmt.minecraft.TeamSurvival.Player.Team;
import nmt.minecraft.TeamSurvival.Scheduling.Scheduler;
import nmt.minecraft.TeamSurvival.Scheduling.Tickable;
import nmt.minecraft.TeamSurvival.Shop.Shop;

/**
 * Holds a single game session.<br />
 * Game sessions will hold the current state of things, the teams involved, etc
 * @author Skyler
 * @author Stephanie
 *
 */
public class GameSession implements Listener, Tickable {
	
	
	public enum State {
		PREGAME,
		STARTINGPERIOD,
		INWAVE,
		INSHOP,
		FINISHED;
	}
	
	/**
	 * Defines standard messages that GameSessions will send out
	 * @author Skyler
	 *
	 */
	public static enum Messages {
		ONEMINUTE(ChatColor.GOLD + "One minute until waves begin!" + ChatColor.RESET),
		THIRTYSECONDS(ChatColor.DARK_RED + "30 seconds until waves begin!" + ChatColor.RESET);
		 
		private String message;
		
		private Messages(String msg) {
			this.message = msg;
		}
		
		@Override
		public String toString() {
			return message;
		}
		
		/**
		 * Returns the stirng equivalent of this predefined message.<br />
		 * For convenience, consider using {@link #toString()} instead
		 * @return
		 */
		public String getString() {
			return message;
		}
	}
	
	/**
	 * Holds the different types of time-based reminders we'd need
	 * @author Skyler
	 * @see {@link GameSession#tick(Object)}
	 */
	private enum Reminders {
		ONEMINUTE,
		THIRTYSECONDS,
		PUSHTOARENA,
		SHOPOVER;
	}
	
	private Collection<Team> teams;
	
	private State state;
	
	/**
	 * Keep a name so we can reference the game session
	 */
	private String name;
	
	/**
	 * The map with the arenas and the shop
	 */
	private Map map;
	
	public Map getMap() {
		return map;
	}

	/**
	 * The shop instance that's unique to this session
	 */
	private Shop sessionShop;
	
	public GameSession(String name, Map map) {
		this.name = name;
		this.map = map;
		this.state = State.PREGAME;
		this.teams = new LinkedList<Team>();
		//TODO fix this in case I accidently leave it in
		//this.sessionShop = new Shop(map.getShopButtonLocation(), null);
	}
	
	/**
	 * Starts the game, dealing with the teams and scores, etc
	 */
	public void start() {
		/*
		 * Teleport teams to their positions
		 * create waves for each player
		 * start the wave
		 */
		if (teams.size() == 0) {
			TeamSurvivalPlugin.plugin.getLogger().warning(
					ChatFormat.ERROR.wrap("Unable to start session, as there are no teams!"));
			return;
		}
		
		if (state != State.PREGAME) {
			TeamSurvivalPlugin.plugin.getLogger().warning(
					ChatFormat.ERROR.wrap("Unable to start session, as it's already been started!"));
		}
		
		state = State.STARTINGPERIOD;
		
	}
	
	/**
	 * Stops the game.<br />
	 * Games stop automatically, so this method is considered an emergency operation.
	 */
	public void stop() {
		HandlerList.unregisterAll(sessionShop);
		sessionShop = null;
	}
	
	/**
	 * @return The current state of the session
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Adds a team to the session
	 * @param team
	 */
	public void addTeam(Team team) {
		if (teams.contains(team)) {
			return;
		}
		
		if (teams.size() >= map.getArenaLocations().size()) {
			TeamSurvivalPlugin.plugin.getLogger().warning("Unable to add class: session is full!");
			return;
		}
		
		teams.add(team);
	}
	
	/**
	 * Removes a team from the game session
	 * @param team the team to kick
	 * @return true if the team was successfully removed
	 */
	public boolean removeTeam(Team team){
		if(!teams.contains(team)){
			return false;
		}
		
		return this.teams.remove(team);
	}
	
	
	/**
	 * Tries to look up a team
	 * @param name The name to look up
	 * @return The team with the given name, null if it cannot be found
	 */
	public Team getTeam(String name) {
		if (teams.isEmpty()) {
			return null;
		}
		
		for(Team t : teams){
			if(t.getName().equals(name)){
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Tries to look up a team
	 * @param player the player to look up
	 * @return The team the player is on, null if the player is not on a team.
	 */
	public Team getTeam(SurvivalPlayer player) {
		if (teams.isEmpty()) {
			return null;
		}
		
		for(Team t : teams){
			if(t.hasPlayer(player)){
				return t;
			}
		}
		
		return null;
	}
	
	/**
	 * Tries to look up a team
	 * @param player the player to look up
	 * @return The team the player is on, null if the player is not on a team.
	 */
	public Team getTeam(OfflinePlayer player) {
		if (teams.isEmpty()) {
			return null;
		}
		
		for(Team t: teams){
			if(t.hasPlayer(player) != null){
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Looks for the provided player, returning their wrapper
	 * @param player
	 * @return The survival player of the given player, or null if not found
	 */
	public SurvivalPlayer getPlayer(OfflinePlayer player) {
		if (teams.isEmpty()) {
			return null;
		}
		
		for(Team t : teams){
			SurvivalPlayer tmp=t.hasPlayer(player);
			if(tmp != null){
				return tmp;
			}
		}
		return null;
	}
	
	/**
	 * Returns summary information about this session.
	 * @param verbose Should this also give extensive information?
	 * @return a string with the name of the session, it's current state, and 
	 * the name of the map it is running. On verbose mode it also includes a 
	 * list of teams in the session as well as the current number of teams and 
	 * the max number of teams.  
	 */
	public String getInfo(boolean verbose) {
		String str = "Session Name: "+ChatFormat.SESSION.wrap(this.name)+"\n"; 
		str += "Map Name: "+ ChatFormat.IMPORTANT.wrap(this.map.getName())+"\n";
		str += "Session State: "+ChatFormat.IMPORTANT.wrap(this.state + "\n");
		
		if(verbose){
			str += "Team size: "+this.teams.size() + "/" + this.map.getMaxTeams();
			str += "\n";
			for(Team t : teams){
				str += ChatFormat.TEAM.wrap(t.getName()) + "   ";
			}
		}
		return str;
	}
	
	/**
	 * Get the name associated with this session
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "GameSession[" + getName() + "]";
	}
	
	public Collection<Team> getTeams(){
		return teams;
	}
	
	@Override
	public void tick(Object reference) {
		if (!(reference instanceof Reminders)) {
			//what the heck is this?
			return; //error
		}
		
		Reminders reminder = (Reminders) reference;
		
		switch (reminder) {
		case ONEMINUTE:
			for (Team team : teams) {
				team.sendTeamMessage(Messages.ONEMINUTE.toString());
			}
			Scheduler.getScheduler().schedule(this, Reminders.THIRTYSECONDS, 30);
			break;
		case THIRTYSECONDS:
			for (Team team : teams) {
				team.sendTeamMessage(Messages.THIRTYSECONDS.toString());
			}
			Scheduler.getScheduler().schedule(this, Reminders.PUSHTOARENA, 30);
			break;
		case PUSHTOARENA:
			moveToArena(); //TODO
			break;
		case SHOPOVER:
			moveToArena();
			break;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		return o.toString().equals(toString());
	}
	
	private void moveToArena() {
		Iterator<Location> arenaIt = map.getArenaLocations().iterator();
		for (Team team : teams) {
			team.moveTo(arenaIt.next());
		}
		
	}
}
