package net.mysticcloud.bungee.core.listeners;

import java.util.Map.Entry;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChannelListener implements Listener {

	@EventHandler
	public void onPluginMessage(PluginMessageEvent e) {
		BungeeCord.getInstance().getConsole().sendMessage("Channel: " + e.getTag());
		if (e.getTag().equalsIgnoreCase("mystic:mystic")) {
			for(Entry<String,ServerInfo> entry : BungeeCord.getInstance().getServers().entrySet()){
				if(entry.getValue().getPlayers().size() > 0){
					entry.getValue().sendData(e.getTag(), e.getData());
					BungeeCord.getInstance().getConsole().sendMessage("Channel: " + e.getTag());
					BungeeCord.getInstance().getConsole().sendMessage("Sending to: " + entry.getKey());
				} else {
					BungeeCord.getInstance().getConsole().sendMessage("Channel: " + e.getTag());
					BungeeCord.getInstance().getConsole().sendMessage("Skipping " + entry.getKey() + " because playercount is at 0");
				}
				
			}
		}

	}

}