package net.mysticcloud.bungee.core.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.mysticcloud.bungee.core.Main;
import net.mysticcloud.bungee.core.runnables.CheckServer;
import net.mysticcloud.bungee.core.utils.Utils;

public class EventListener implements Listener {
	
	

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerKick(ServerKickEvent e) {
		BungeeCord.getInstance().getConsole().sendMessage(Utils.colorize("&6Kicked &e" + e.getPlayer().getName() + " &6from&e " + e.getKickedFrom().getName() + "&6 for &e" + e.getKickReason()));
		if (e.getKickReason().contains("GRL")) {
			if(Utils.grlpl.containsKey(e.getKickedFrom().getName())) {
				if(Utils.grlpl.get(e.getKickedFrom().getName()).size() == 0) {
					BungeeCord.getInstance().getScheduler().schedule(Main.getPlugin(), new CheckServer(e.getKickedFrom().getName()), 3, TimeUnit.SECONDS);
				}
				Utils.grlpl.get(e.getKickedFrom().getName()).add(e.getPlayer().getUniqueId());
			} else {
				Utils.grlpl.put(e.getKickedFrom().getName(), new ArrayList<>());
				Utils.grlpl.get(e.getKickedFrom().getName()).add(e.getPlayer().getUniqueId());
				BungeeCord.getInstance().getScheduler().schedule(Main.getPlugin(), new CheckServer(e.getKickedFrom().getName()), 3, TimeUnit.SECONDS);
			}
			
			for(Entry<String, ServerInfo> en : BungeeCord.getInstance().getServers().entrySet()) {
				if(en.getKey() != e.getKickedFrom().getName()) {
					e.setCancelled(true);
					e.getPlayer().connect(BungeeCord.getInstance().getServerInfo(en.getKey()));
					e.getPlayer().sendMessage(Utils.colorize("&aThe server you were on is restarting so we moved you to &2" + en.getKey() + "&a. Please stay online, you will be reconnected to &2" + e.getKickedFrom().getName() + "&a as soon as it comes back online."));
				}
			}
			
			return;
			
		}
		if (e.getKickReason() == "Server Closed") {
			
			for(Entry<String, ServerInfo> en : BungeeCord.getInstance().getServers().entrySet()) {
				if(en.getKey() != e.getKickedFrom().getName()) {
					e.setCancelled(true);
					e.getPlayer().connect(BungeeCord.getInstance().getServerInfo(en.getKey()));
					e.getPlayer().sendMessage(Utils.colorize("&aThe server you were on went down so we moved you to &2" + en.getKey() + "&a."));
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerSwitchServer(ServerSwitchEvent e) {
		BungeeCord.getInstance().getConsole()
		.sendMessage(Utils.colorize("&e[SQL] &f>&7 Insert " + e.getPlayer().getName() + ": " + Utils.sendInsert(
				"INSERT INTO ServerStats (UUID,USERNAME,DATE,ACTION,SERVER) VALUES ('" + e.getPlayer().getUniqueId() + "','"+e.getPlayer().getName() + "','" + new Date().getTime() + "','switch','" + e.getPlayer().getServer().getInfo().getName() + "');")));
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerQuit(PlayerDisconnectEvent e) {
		BungeeCord.getInstance().getConsole()
				.sendMessage(Utils.colorize("&e[SQL] &f>&7 Update " + e.getPlayer().getName() + ": " + Utils.sendUpdate(
						"UPDATE PlayerStats SET ONLINE='no' WHERE UUID='" + e.getPlayer().getUniqueId() + "';")));
		BungeeCord.getInstance().getConsole()
				.sendMessage(Utils.colorize("&e[SQL] &f>&7 Insert " + e.getPlayer().getName() + ": "
						+ Utils.sendInsert("INSERT INTO ServerStats (UUID,USERNAME,DATE,ACTION,SERVER) VALUES('"
								+ e.getPlayer().getUniqueId() + "','"+e.getPlayer().getName() + "','" + new Date().getTime() + "','disconnect','" + e.getPlayer().getServer().getInfo().getName() + "');")));
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PostLoginEvent e) {
		BungeeCord.getInstance().getConsole().sendMessage(Utils.colorize("&eLogging in " + e.getPlayer().getName()));
		ResultSet rs = Utils.sendQuery("SELECT * FROM PlayerStats WHERE UUID='" + e.getPlayer().getUniqueId() + "';");
		try {
			BungeeCord.getInstance().getConsole()
					.sendMessage(Utils.colorize("&e[SQL] &f>&7 Updating ServerStats: &f"
							+ Utils.sendInsert("INSERT INTO ServerStats (UUID,USERNAME,DATE,ACTION,SERVER) VALUES('"
									+ e.getPlayer().getUniqueId() + "','"+e.getPlayer().getName() + "','" + new Date().getTime() + "','connect','NaN');")));
			if (rs.next()) {
				if (Utils.sendUpdate("UPDATE PlayerStats SET ONLINE='yes',IP='"
						+ e.getPlayer().getAddress().getHostString() + "',DATE='" + new Date().getTime()
						+ "' WHERE UUID='" + e.getPlayer().getUniqueId() + "';") == 1) {
					BungeeCord.getInstance().getConsole().sendMessage(
							Utils.colorize("&e[SQL] &7>&f Successfully updated info for " + e.getPlayer().getName()));

				}
			} else {
				BungeeCord.getInstance().getConsole().sendMessage(Utils
						.colorize("" + Utils.sendInsert("INSERT INTO PlayerStats (UUID,ONLINE,NAME,IP,DATE) VALUES('"
								+ e.getPlayer().getUniqueId() + "','yes','" + e.getPlayer().getName() + "','"
								+ e.getPlayer().getAddress().getHostString() + "','" + new Date().getTime() + "');")));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

//	@EventHandler
//	public void onPostLogin(PostLoginEvent e) {
////		if (SQLUtils.getSQL().update("") == -1) {
////			BungeeCord.getInstance().getConsole().sendMessage(
////					"Couldn't update player info for " + e.getPlayer().getName() + ". Trying to create new row.");
////			if (SQLUtils.getSQL().update("INSERT INTO OnlinePlayers (username, uuid) VALUES ('" + e.getPlayer().getName()
////					+ "', '" + e.getPlayer().getUniqueId() + "');") == -1) {
////				BungeeCord.getInstance().getConsole().sendMessage(
////						"Couldn't create player info! This is a severe warning, please report this to a developer.");
////			} else {
////				BungeeCord.getInstance().getConsole().sendMessage("Created info for player.");
////			}
////		} else {
////			BungeeCord.getInstance().getConsole()
////					.sendMessage("Updated player info for " + e.getPlayer().getName() + ".");
////		}
//	}

}