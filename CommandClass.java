package me.PimpDuck.DCRandomTP;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClass implements CommandExecutor
{
  Main plugin;

  public CommandClass(Main plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    Player player = (Player)sender;

    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.AQUA + "[DCRandomTP] " + ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Only in game players can perform this command!");
      return true;
    }

    if (!player.hasPermission("dcrandomtp.teleport")) {
        sender.sendMessage(ChatColor.AQUA + "[DCRandomTP] " + ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "You don't have permission to teleport!");
      return true;
    }

    if ((args.length > 1) || (args.length < 0)) {
      player.sendMessage(ChatColor.AQUA + "[DCRandomTP] " + ChatColor.DARK_RED + ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Invalid Command Syntax!");
      player.sendMessage(ChatColor.RED + "Usage: /randomtp");
      return true;
    }
    if ((TeleportHandler.isAntiCheat()) && (TeleportHandler.checkForCheating(player))) {
      player.sendMessage(ChatColor.AQUA + "[DCRandomTP] " + ChatColor.DARK_RED + "ERROR: " + ChatColor.DARK_RED + "You can't teleport from this location!");
      player.sendMessage(ChatColor.AQUA + "[DCRandomTP] " + ChatColor.RED + "Be sure you're not in water, in lava, or jumping!");
      return true;
    }

    if (TeleportHandler.isCoolDownEnabled()) {
      TeleportHandler.sendTeleport(player);
      TeleportHandler.setCoolDown(player);
    }

    if ((TeleportHandler.isCoolDownEnabled()) && (!TeleportHandler.isCoolDownFinished(player))) {
      player.sendMessage(ChatColor.RED + TeleportHandler.getCoolDownLeft(player));
    }

    if (TeleportHandler.isSafe()) {
      TeleportHandler.sendSafeTeleport(player);
    }

    if (!TeleportHandler.isSafe()) {
      TeleportHandler.sendTeleport(player);
    }
    return true;
  }
}
