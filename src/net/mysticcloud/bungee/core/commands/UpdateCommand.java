package net.mysticcloud.bungee.core.commands;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.mysticcloud.bungee.core.utils.StreamGobbler;
import net.mysticcloud.bungee.core.utils.Utils;

public class UpdateCommand extends Command {

	public UpdateCommand(String name) {
		super(name);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		long time = new Date().getTime();

		
		try {
			Process updatespigot = Runtime.getRuntime().exec("/home/Minecraft/BuildTools/update.sh");
			StreamGobbler streamGobbler1 = new StreamGobbler(updatespigot.getInputStream(), BungeeCord.getInstance().getConsole()::sendMessage);
			Executors.newSingleThreadExecutor().submit(streamGobbler1);
			int exitCode1 = updatespigot.waitFor();
			assert exitCode1 == 0;
			
			sender.sendMessage(Utils.colorize("&aSpigot files Downloaded..."));
			
			Process movespigot = Runtime.getRuntime().exec("/home/Minecraft/BuildTools/movefiles.sh");
			StreamGobbler streamGobbler2 = new StreamGobbler(movespigot.getInputStream(), sender::sendMessage);
			Executors.newSingleThreadExecutor().submit(streamGobbler2);
			int exitCode2 = movespigot.waitFor();
			assert exitCode2 == 0;
			
			sender.sendMessage(Utils.colorize("&aSpigot files Moved..."));
			
			Process updatebungee = Runtime.getRuntime().exec("/home/Minecraft/BuildTools/updatebungee.sh");
			StreamGobbler streamGobbler3 = new StreamGobbler(updatebungee.getInputStream(), sender::sendMessage);
			Executors.newSingleThreadExecutor().submit(streamGobbler3);
			int exitCode3 = updatebungee.waitFor();
			assert exitCode3 == 0;
			
			sender.sendMessage(Utils.colorize("&aBungee Updated..."));
			sender.sendMessage(Utils.colorize("&aCompleted in " + (new Date().getTime()-time) + " ms!"));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
