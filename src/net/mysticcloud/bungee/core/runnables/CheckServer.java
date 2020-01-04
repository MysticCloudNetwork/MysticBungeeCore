package net.mysticcloud.bungee.core.runnables;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.mysticcloud.bungee.core.Main;
import net.mysticcloud.bungee.core.utils.Utils;

public class CheckServer implements Runnable {
	
	final String server;
	
	public CheckServer(String server) {
		this.server = server;
	}

	@Override
	public void run() {
		BungeeCord.getInstance().getConsole().sendMessage(Utils.colorize("&6Trying to reconnect to &e" + server + "&6."));
		ProxyServer.getInstance().getServers().get(server).ping(new Callback<ServerPing>() {
	         
            @SuppressWarnings("deprecation")
			@Override
            public void done(ServerPing result, Throwable error) {
                if(error!=null){
                	BungeeCord.getInstance().getConsole().sendMessage(Utils.colorize("&eError connecting to " + server));
                	BungeeCord.getInstance().getScheduler().schedule(Main.getPlugin(), new CheckServer(server), 3, TimeUnit.SECONDS);
                } else {
                	BungeeCord.getInstance().getConsole().sendMessage(Utils.colorize("&aConnected to " + server));
                	for(UUID uid : Utils.grlpl.get(server)) {
                		if(BungeeCord.getInstance().getPlayer(uid) == null) continue;
                		BungeeCord.getInstance().getPlayer(uid).connect(BungeeCord.getInstance().getServerInfo(server));
                		BungeeCord.getInstance().getPlayer(uid).sendMessage(Utils.colorize("&aYou've been reconnected to &2" + server + "&a."));
                	}
                	Utils.grlpl.get(server).clear();
                }
             
            }
        });
	}

}
