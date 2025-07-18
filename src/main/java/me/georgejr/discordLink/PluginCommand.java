package me.georgejr.discordLink;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PluginCommand implements CommandExecutor, TabCompleter {

    //get main class
    private final DiscordLink main = DiscordLink.getPlugin(DiscordLink.class);

    //get cooldown manager
    private final CooldownManager cM = new CooldownManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //check args length
        if (args.length == 0) {
            //sender is running /discord
            if (!(sender instanceof Player) || (sender.hasPermission("discordlink.admin") && main.getConfig().getBoolean("cooldown.admin-bypass"))) {
                sender.sendMessage(main.formatString(main.getConfig().getString("link")));
                return true;
            }

            Player p = (Player) sender;
            //get the amount of milliseconds that have passed since the command was last executed
            if (cM.cooldowns.get(p.getUniqueId()) == null || TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cM.cooldowns.get(p.getUniqueId())) >= main.getConfig().getInt("cooldown.time")) {
                //send link
                p.sendMessage(main.formatString(main.getConfig().getString("link")));
                //set cooldown
                cM.setCooldown(p.getUniqueId(), System.currentTimeMillis());
            } else {
                //send remaining time
                p.sendMessage(main.formatString(main.getConfig().getString("cooldown.message").replace("{COOLDOWN}", String.valueOf(main.getConfig().getInt("cooldown.time") - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cM.cooldowns.get(p.getUniqueId()))))));
            }

        } else {
            //check if args is admin command
            if (!args[0].equalsIgnoreCase("cooldown") && !args[0].equalsIgnoreCase("reload"))
                return true;

            //check if player is admin
            if (!sender.hasPermission("discord.admin")) {
                sender.sendMessage("§cYou do not have permission to execute this command. If this is an error, please contact admin!");
                return true;
            }

            //check if reload command is being run
            if (args[0].equalsIgnoreCase("reload")) {
                //send reload message and reload config.yml
                main.reloadConfig();

                sender.sendMessage("§bPlugin has been reloaded!");
                return true;
            }

            //check if cooldown command is being run
            if (args[0].equalsIgnoreCase("cooldown")) {
                //check args length
                if (args.length != 2) {
                    sender.sendMessage("§cUsage: /discordlink cooldown (time)");
                    return true;
                }
                //get cooldown time
                int time = Integer.parseInt(args[1]);
                //set new cooldown time
                main.getConfig().set("cooldown.time", time);
                main.saveConfig();

                //send confirmation message
                sender.sendMessage("§bCooldown has been changed to §d" + time + "§b seconds!");
            }

        }

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        //check if player is admin
        if (!sender.hasPermission("discordlink.admin"))
            return List.of();

        if (args.length == 1)
            return List.of("reload", "cooldown");

        if (args.length == 2)
            return List.of("(Current: " + main.getConfig().getInt("cooldown.time") + "s)");

        return List.of();
    }

}
