package net.mysticcloud.bungee.core.utils;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.md_5.bungee.BungeeCord;

public class Utils {
	private static IDatabase db = new IDatabase("localhost", "Minecraft", 3306, "mysql", "v4pob8LW");
	private static boolean connected = false;
	
	public static Map<String, List<UUID>> grlpl = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	public static void start() {
		if (!connected) {
			if (testSQLConnection()) {
				connected = true;
				BungeeCord.getInstance().getConsole().sendMessage(colorize("&e[SQL] &7>&f Successfully connected to SQL."));
			} else {
				connected = false;
				BungeeCord.getInstance().getConsole().sendMessage(colorize("&e[SQL] &7>&f Error connecting to SQL."));
			}
		}
	}
	private static boolean testSQLConnection() {
		return (db.init());

	}

	public static String colorize(String message) {
		return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
	}
	public static ResultSet sendQuery(String query) {
		if (connected) {
			return db.query(query);
		} else {
			if (testSQLConnection()) {
				return db.query(query);
			}
			return null;
		}
	}

	public static Integer sendUpdate(String query) {
		if (connected) {
			return db.update(query);
		} else {
			if (testSQLConnection()) {
				return db.update(query);
			}
			return null;
		}
	}

	public static boolean sendInsert(String query) {
		if (connected) {
			return db.input(query);
		} else {
			if (testSQLConnection()) {
				return db.input(query);
			}
			return false;
		}
	}
	
	public static String toString(String[] args, String seporator) {
		String s = "";
		for(String arg : args) 
			s = (s=="") ? arg : s + seporator + arg;
		return s;
		
	}
	
	public static String toString(String[] args) {
		return toString(args, " ");
	}

}
