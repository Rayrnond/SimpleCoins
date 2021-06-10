package me.raymonddev.coins.commands;

import me.raymonddev.coins.Coins;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (!((Player) sender).getPlayer().hasPermission("coins.use")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }
        }
        if(cmd.getName().equalsIgnoreCase("coins")) {
            if(args.length == 0) {
                sender.sendMessage(ChatColor.GOLD + "--- Coins ---");
                sender.sendMessage(ChatColor.GOLD + "/coins <add|remove> <player> <amount>");
                sender.sendMessage(ChatColor.GOLD + "/coins info <player>");
            } else {
                String sub = args[0];
                if(sub.equalsIgnoreCase("add") || sub.equalsIgnoreCase("remove") || sub.equalsIgnoreCase("info")) {
                    if(args.length == 1) {
                        sender.sendMessage(ChatColor.RED + "/coins " + sub + " <player>" + (!sub.equalsIgnoreCase("info") ? " <amount>" : ""));
                    } else {
                        Player target = Bukkit.getPlayerExact(args[1]);
                        if(target == null) {
                            sender.sendMessage(ChatColor.RED + "Target player is not online.");
                            return true;
                        }
                        if(sub.equalsIgnoreCase("info"))
                            sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.GOLD + " has a total of " + ChatColor.YELLOW + Coins.getInstance().getCoins(target) + ChatColor.GOLD + " coins.");
                        else {
                            if(args.length != 3) {
                                sender.sendMessage(ChatColor.RED + "/coins " + sub + " " + target.getName() + " <amount>");
                                return true;
                            }
                            if(!NumberUtils.isDigits(args[2])) {
                                sender.sendMessage(ChatColor.RED + "Invalid number.");
                                return true;
                            }
                            int amount = NumberUtils.toInt(args[2]);
                            boolean add = sub.equalsIgnoreCase("add");
                            Coins.getInstance().setCoins(target, Coins.getInstance().getCoins(target) + (add ? amount : amount*-1));
                            sender.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.GOLD + " now has " + ChatColor.YELLOW + Coins.getInstance().getCoins(target) + ChatColor.GOLD + " coins.");
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid sub-command. Type '/coins' for help.");
                }
            }
        }
        return true;
    }

}
