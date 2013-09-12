package me.PimpDuck.DCRandomTP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClass implements CommandExecutor
{	
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
		String prefix = Main.plugin.getConfig().getString("DCRandomTP.Chat-Prefix");
		prefix = ChatColor.translateAlternateColorCodes('&', prefix);  
	Player player = (Player) sender;
    if (!(sender instanceof Player)) {
      sender.sendMessage(prefix + ChatColor.RED + "Command must be executed by a player.");
      return true;
    }
    
    if (Main.playerDelayed(player))
	{
		if(Main.getRemainingTime(player) < 1)
		{
            Main.removeDelayedPlayer(player);
		}else
		{
			int remaining = Main.getRemainingTime(player);
			String hours = remaining == 1 ? " hour" : " hours";

			player.sendMessage(prefix + ChatColor.GOLD + ("You must wait " + remaining + hours + " before using /random again!"));
			return false;
		}
	}
    
    if(cmd.getName().equalsIgnoreCase("random")){
    	if(args.length == 0){
    player.sendMessage(prefix + Main.plugin.tp_message.replaceAll("&", "§"));
    Main.addDelayedPlayer(player);
    getRandomLoc(player, Main.plugin.minX, Main.plugin.maxX, Main.plugin.minZ, Main.plugin.maxZ, Main.plugin.world);
    	}
    	if(args.length >= 1){
    		player.sendMessage(prefix + ChatColor.DARK_RED + "Too many arguments!");
    	}
  }
    return true;
  }

  public void getRandomLoc(Player p, int minX, int maxX, int minZ, int maxZ, String world)
  {
    Random rand = new Random();

    int x = rand.nextInt(maxX - minX + 1) + minX;
    int z = rand.nextInt(maxZ - minZ + 1) + minZ;

    double y = getValidHighestY(Bukkit.getWorld(world), x, z);
    if (y == -1.0D) {
      getRandomLoc(p, minX, maxX, minZ, maxZ, world);
      return;
    }
    Location loc = new Location(Bukkit.getWorld(world), x, y, z);
    sendGround(p, loc);
    p.teleport(loc.add(0.5D, 1.0D, 0.5D));
  }

  private double getValidHighestY(World world, int x, int z) {
    @SuppressWarnings("rawtypes")
	List blacklist = new ArrayList();
    blacklist = Arrays.asList(new Integer[] { Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(18), Integer.valueOf(51), Integer.valueOf(81) });
    world.getChunkAt(new Location(world, x, 0.0D, z)).load();

    double y = 0.0D;
    int blockid = 0;

    y = 257.0D;
    while ((y >= 0.0D) && (blockid == 0)) {
      y -= 1.0D;
      blockid = world.getBlockTypeIdAt(x, (int)y, z);
    }
    if (y == 0.0D) return -1.0D;

    if (blacklist.contains(Integer.valueOf(blockid))) return -1.0D;
    if ((blacklist.contains(Integer.valueOf(81))) && (world.getBlockTypeIdAt(x, (int)(y + 1.0D), z) == 81)) return -1.0D;

    return y;
  }

  public void sendGround(Player player, Location location)
  {
    location.getChunk().load();

    World world = location.getWorld();

    for (int y = 0; y <= location.getBlockY() + 2; y++) {
      Block block = world.getBlockAt(location.getBlockX(), y, location.getBlockZ());
      player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
    }
  }
}
