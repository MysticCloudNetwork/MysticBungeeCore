package net.mysticcloud.bungee.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.mysticcloud.bungee.core.utils.Utils;

public class SQLCommand extends Command {

	public SQLCommand(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("mysticcoud.bungee.cmd.rsql")) {
			sender.sendMessage(Utils.colorize("&e[SQL] &f> &7Error: You don't have permission to use that command."));
			return;
		}
		
		if (args.length == 0) {
			sender.sendMessage(Utils.colorize("&e[SQL] &f> &7Error. Usage: /sql [query]"));
			return;
		}
		
		if(args[0].equalsIgnoreCase("SELECT")) {
			ResultSet rs = Utils.sendQuery(Utils.toString(args));
			int a = 0;
			try {
				while (rs.next()) {
					a = a + 1;
					sender.sendMessage("");
					sender.sendMessage(Utils.colorize("&6-------Row " + a + "-------"));
					for (int i = 1; i != rs.getMetaData().getColumnCount() + 1; i++) {
						sender.sendMessage(Utils.colorize("&6" + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i)));
					}
					sender.sendMessage(Utils.colorize("&6--------------------"));
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if(args[0].equalsIgnoreCase("INSERT")) {
			sender.sendMessage(Utils.colorize("&6Result: " + Utils.sendInsert(Utils.toString(args))));
		}
		if(args[0].equalsIgnoreCase("UPDATE") || args[0].equalsIgnoreCase("DELETE")) {
			sender.sendMessage(Utils.colorize("&6Result: " + Utils.sendUpdate(Utils.toString(args))));
		}
		
		return;
	}

}
