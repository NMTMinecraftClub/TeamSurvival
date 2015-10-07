package nmt.minecraft.TeamSurvival;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.OfflinePlayer;

import nmt.minecraft.TeamSurvival.Player.SurvivalPlayer;
import nmt.minecraft.TeamSurvival.Player.Team;
import nmt.minecraft.TeamSurvival.Session.GameSession;

/**
 * Manager class for all sessions.<br />
 * Static class that gives access to global information
 * @author Skyler
 * @note Classified as a session job
 */
public class TeamSurvivalManager {
	
	private static Collection<GameSession> sessions = new HashSet<GameSession>();
	
	/**
	 * Looks up a player using an ofline player.
	 * @param player
	 * @return
	 */
	public static SurvivalPlayer getPlayer(OfflinePlayer player) {
		for(GameSession s : sessions){
			SurvivalPlayer play = s.getPlayer(player);
			if(play != null){
				return play;
			}
		}
		return null;
	}
	
	/**
	 * Looks up a team by a player.<br />
	 * In other words, this method returns the team a player belongs to
	 * @param player
	 * @return
	 */
	public static Team getTeam(SurvivalPlayer player) {
		for(GameSession s : sessions){
			Team team = s.getTeam(player);
			if(team != null){
				return team;
			}
		}
		return null;
	}
	
	/**
	 * Returns the underlying collection of sessions.<br />
	 * Does NOT clone! This collection is the same collection this thing holds
	 * @return
	 */
	public static Collection<GameSession> getSessions() {
		return sessions;
	}
	
	/**
	 * Registers a game session, if it's not already registered
	 * @param session The game session to add.
	 * @return true if the session was added
	 */
	public static boolean register(GameSession session) {
		boolean check = sessions.add(session);
		if (check) {
			System.out.println("Successfully added session: " + session.getName());
		} else {
			System.out.println("WARNING! Session: " + session.getName() + " was not added!");
		}
		return check;
	}
	
	/**
	 * Attempts to unregister a game session
	 * @param session The game session to remove.
	 * @return true if the session was found and unregistered
	 * TODO Make sure that these messages are put into the LOG
	 */
	public static boolean unregister(GameSession session) {
		if (session.getState() != GameSession.State.FINISHED) {
			System.out.println("ERROR! Session: " + session.getName() + " is not finished! Please wait!");
			return false;
		}
		boolean check = sessions.remove(session);
		if (check) {
			System.out.println("Successfully removed session: " + session.getName());
		} else {
			System.out.println("WARNING! Session: " + session.getName() + " was not added!");
		}
		return false;
	}
	
	public static GameSession getSession(String sessionName){
		for(GameSession s : TeamSurvivalManager.getSessions()){
			if(s.getName().equalsIgnoreCase(sessionName)){
				return s;
			}
		}
		return null;
	}
	
}
