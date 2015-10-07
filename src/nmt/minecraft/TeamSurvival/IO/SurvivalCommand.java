package nmt.minecraft.TeamSurvival.IO;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nmt.minecraft.TeamSurvival.TeamSurvivalManager;
import nmt.minecraft.TeamSurvival.Map.Map;
import nmt.minecraft.TeamSurvival.Session.GameSession;

/**
 * 
 * @author Stephanie
 */
public class SurvivalCommand implements CommandExecutor {

	/**
	 * A list of commands handled by the 'team survival' wrapping command
	 */
	private static final String[] teamSurvivalCommandList = {"session", "team"};

	private static final String[] sessionCommandList = {"list", "create", "info", "dispatch"};
	
	private static final String[] teamCommandList = {"list", "create", "info", "dispatch"};
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) {
			//print usage
			sender.sendMessage("/ts [session|team] {args}");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("session")) {
			onSessionCommand(sender, args);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("team")) {
			onTeamCommand(sender, args);
			return true;
		}
		
		return false;
	}
	
	protected static List<String> getTeamsurvivalcommandlist() {
		return Arrays.asList(teamSurvivalCommandList);
	}

	protected static List<String> getSessioncommandlist() {
		return Arrays.asList(sessionCommandList);
	}

	protected static List<String> getTeamcommandlist() {
		return Arrays.asList(teamCommandList);
	}
	
	/**
	 * Handles the admin 'session' command
	 * @param sender
	 * @param args
	 */
	private void onSessionCommand(CommandSender sender, String[] args) {
		//[list | create | start | stop | remove | info] ?
		//this is what EDFs has, and it feels pretty similar in terms of 'sessions'
		if (args.length < 2) {
			return;
		}
		
		if (args[1].equalsIgnoreCase("list")) {
			onSessionListCommand(sender, args);
			return;
		}
		
		if (args[1].equalsIgnoreCase("info")) {
			onSessionInfoCommand(sender, args);
			return;
		}
		
		if (args[1].equalsIgnoreCase("create")) {
			onSessionCreateCommand(sender, args);
			return;
		}
		
		if (args[1].equalsIgnoreCase("start")) {
			onSessionStartCommand(sender, args);
			return;
		}
		
		if (args[1].equalsIgnoreCase("stop")) {
			onSessionStopCommand(sender, args);
			return;
		}
		
		if (args[1].equalsIgnoreCase("remove")) {
			onSessionRemoveCommand(sender, args);
			return;
		}
	}
	
	/**
	 * Handles the 'list' argument for the admin 'session' command
	 * @param sender
	 * @param args
	 */
	private void onSessionListCommand(CommandSender sender, String[] args) {
		if (args.length > 2) {
			sender.sendMessage("/ts session list");
			return;
		}
		
		Collection<GameSession> sessions = TeamSurvivalManager.getSessions();
		
		if (sessions == null) {
			sender.sendMessage(ChatFormat.ERROR.wrap("Session list is null!"));
			return;
		}
		
		if (sessions.isEmpty()) {
			sender.sendMessage(ChatFormat.IMPORTANT.wrap("There are no sessions"));
			return;
		}
		
		sender.sendMessage("There are currently " + ChatColor.GREEN + sessions.size() + ChatColor.RESET + " sessions:");
		
		for (GameSession s : sessions) {
			sender.sendMessage(ChatFormat.SESSION.wrap(s.getName()) + "  " 
					+ ChatFormat.IMPORTANT.wrap("[" + s.getState().toString() + "]"));
		}
	}
	
	/**
	 * Handles the 'create' argument for the admin 'session' command
	 * @param sender
	 * @param args
	 */
	private boolean onSessionCreateCommand(CommandSender sender, String[] args) {
		// /ts session create [sessionName] [mapName]
		if(args.length != 4){
			sender.sendMessage(ChatFormat.ERROR.wrap("Incorrect number of arguments! ")
					+ ChatFormat.IMPORTANT.wrap("usage: /teamsurvival session create [sessionName] [mapName]"));
			return false;
		}
		
		if(TeamSurvivalManager.getSession(args[2])!=null){
			sender.sendMessage(ChatFormat.ERROR.wrap("There already exists an active session with that name"));
			return false;
		}
		
		//check for map matching the given name
		if(!Map.listConfigs().contains(args[3])){
			sender.sendMessage(ChatFormat.ERROR.wrap("Could not find config for the given map"));
			return false;
		}
		
		Map tmpMap = Map.loadConfig(args[3]);
		if(tmpMap == null){
			sender.sendMessage(ChatFormat.ERROR.wrap("Could not load the config file for the map"));
			return false;
		}
		
		GameSession session = new GameSession(args[2], tmpMap);
		
		if(!TeamSurvivalManager.register(session)){
			sender.sendMessage(ChatFormat.ERROR.wrap("Could not register the session with the TSManager"));
			return false;
		}
		
		sender.sendMessage(ChatFormat.SESSION.wrap("Session successfully created"));
		return true;
		
		
	}
	
	/**
	 * Handles the 'start' argument for the admin 'session' command
	 * @param sender
	 * @param args
	 */
	private boolean onSessionStartCommand(CommandSender sender, String[] args) {
		// /ts session start [session]
		if(args.length != 3){
			sender.sendMessage(ChatFormat.ERROR.wrap("Incorrect number of arguments! ")
					+ ChatFormat.IMPORTANT.wrap("usage: /teamsurvival session start [sessionName]"));
			return false;
		}
		
		GameSession session = TeamSurvivalManager.getSession(args[2]);
		if(session == null){
			sender.sendMessage(ChatFormat.ERROR.wrap("Could not find session"));
			return false;
		}
		
		sender.sendMessage(ChatFormat.SESSION.wrap("Starting session..."));
		session.start();
		
		return true;
	}
	
	/**
	 * Handles the 'stop' argument for the admin 'session' command
	 * @param sender
	 * @param args
	 */
	private boolean onSessionStopCommand(CommandSender sender, String[] args) {
		// /ts session stop [session]
			if(args.length != 3){
				sender.sendMessage(ChatFormat.ERROR.wrap("Incorrect number of arguments! ")
						+ ChatFormat.IMPORTANT.wrap("usage: /teamsurvival session stop [sessionName]"));
				return false;
			}
			
			GameSession session = TeamSurvivalManager.getSession(args[2]);
			if(session == null){
				sender.sendMessage(ChatFormat.ERROR.wrap("Could not find session"));
				return false;
			}
			
			sender.sendMessage(ChatFormat.SESSION.wrap("Stoping session..."));
			session.stop();
			
			return true;
	}
	
	/**
	 * Handles the 'remove' argument for the admin 'session' command
	 * @param sender
	 * @param args
	 */
	private boolean onSessionRemoveCommand(CommandSender sender, String[] args) {
		// /ts session remove [sessionName]
		if(args.length != 3){
			sender.sendMessage(ChatFormat.ERROR.wrap("Incorrect number of arguments! ")
					+ ChatFormat.IMPORTANT.wrap("usage: /teamsurvival session remove [sessionName]"));
			return false;
		}
		
		GameSession session = TeamSurvivalManager.getSession(args[2]);
		if(session == null){
			sender.sendMessage(ChatFormat.ERROR.wrap("Could not find session"));
			return false;
		}
		
		if(!TeamSurvivalManager.unregister(session)){
			sender.sendMessage(ChatFormat.ERROR.wrap("Could not remove session."));
			return false;
		}
		
		sender.sendMessage(ChatFormat.SESSION.wrap("Session removed."));
		return true;
	}
	
	/**
	 * Handles the 'info' argument for the admin 'session' command
	 * @param sender
	 * @param args
	 */
	private void onSessionInfoCommand(CommandSender sender, String[] args) {
		if (args.length < 2 || args.length > 4) {
			sender.sendMessage("/ts session info " + ChatFormat.SESSION.wrap("[sessionName] {verbose}"));
			return;
		}
		
		String sessionName = args[2];
		GameSession gameSession = null;
		
		for (GameSession session : TeamSurvivalManager.getSessions()) {
			if (session.getName().equals(sessionName)) {
				gameSession = session;
				break;
			}
		}
		
		if (gameSession == null) {
			sender.sendMessage(ChatFormat.ERROR.wrap("Unable to find session ") + ChatFormat.SESSION.wrap(sessionName));
			return;
		}
		
		boolean verbose = false;
		if (args.length == 4) {
			if (args[3].equalsIgnoreCase("true") || args[3].equals("verbose")) {
				verbose = true;
			}
		}
		
		sender.sendMessage(gameSession.getInfo(verbose));
	}
	
	/**
	 * Handles the admin 'team' command
	 * @param sender
	 * @param args
	 */
	private void onTeamCommand(CommandSender sender, String[] args) {
		//[list | create | info | disband] ?
	}
}
