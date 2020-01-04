package net.mysticcloud.bungee.core;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.mysticcloud.bungee.core.commands.SQLCommand;
import net.mysticcloud.bungee.core.commands.UpdateCommand;
import net.mysticcloud.bungee.core.listeners.ChannelListener;
import net.mysticcloud.bungee.core.listeners.EventListener;
import net.mysticcloud.bungee.core.utils.Utils;

public class Main extends Plugin {
	
	static Plugin plugin;
	
	@Override
    public void onEnable(){
		plugin = this;
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new SQLCommand("gsql"));
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new UpdateCommand("update"));
        BungeeCord.getInstance().getPluginManager().registerListener(this, new ChannelListener());
        BungeeCord.getInstance().getPluginManager().registerListener(this, new EventListener());
        BungeeCord.getInstance().getConsole().sendMessage("Enabled");
        BungeeCord.getInstance().registerChannel("mystic:mystic");
        Utils.start();
        
    }
	
	public static Plugin getPlugin() {
		return plugin;
	}

}
