package me.PimpDuck.DCRandomTP;


import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportHandler
{
  public static Main plugin;

  public TeleportHandler(Main plugin)
  {
    plugin = plugin;
  }

  public static boolean isSafe()
  {
    Boolean safe = Boolean.valueOf(plugin.getConfig().getBoolean("DCRandomTP.Teleport.SafeTeleport"));
    return safe != null ? safe.booleanValue() : false;
  }

  public static int minHealth() {
    return plugin.getConfig().getInt("DCRandomTP.AntiCheat.MinimumHealthLevel");
  }

  public static boolean isAntiCheat() {
    Boolean anti = Boolean.valueOf(plugin.getConfig().getBoolean("DCRandomTP.AntiCheat.Enable"));
    return anti != null ? anti.booleanValue() : false;
  }

  public static long getCooldownTime() {
    return plugin.getConfig().getLong("DCRandomTP.Cooldown.CooldownTime");
  }

  public static boolean isCoolDownEnabled() {
    Boolean enabled = Boolean.valueOf(plugin.getConfig().getBoolean("DCRandomTP.Cooldown.Enable"));
    return enabled != null ? enabled.booleanValue() : false;
  }

  public static int getMaxX() {
    return plugin.getConfig().getInt("DCRandomTP.Teleport.CoordsX");
  }

  public static int getMaxZ() {
    return plugin.getConfig().getInt("DCRandomTP.Teleport.CoordsZ");
  }

  public static String getWorld()
  {
    return plugin.getConfig().getString("DCRandomTP.Teleport.World");
  }
  public static boolean checkForCheating(Player player)
  {
    boolean cheating;
    if ((player.getLocation().getBlock().getType() == Material.WATER) || 
      (player.getLocation().getBlock().getType() == Material.LAVA) || 
      (player.getLocation().getBlock().getType() == Material.AIR)) {
      cheating = false;
    }
    else {
      cheating = true;
    }

    return cheating;
  }
  public static void setCoolDown(Player player)
  {
    Cooldown.addCooldown("DCRandomTP-Cooldown", player.getName(), getCooldownTime());
  }

  public static String getCoolDownLeft(Player player) {
    PlayerCooldown pc = Cooldown.getCooldown("DCRandomTP-Cooldown", player.getName(), getCooldownTime());
    Cooldown.getCooldown("DCRandomTP-Cooldown", player.getName(), pc.getTimeLeft());
    if (pc.isOver()) {
      pc.reset();
      return "You can teleport again!";
    }
    return "You have to wait " + pc.getTimeLeft() + " seconds before you can teleport again.";
  }

  public static boolean isCoolDownFinished(Player player)
  {
    PlayerCooldown pc = Cooldown.getCooldown("DCRandomTP-Cooldown", player.getName(), getCooldownTime());

    return pc.isOver();
  }

  public static void sendTeleport(Player player)
  {
    World world = Bukkit.getWorld(getWorld());
    Random random = new Random();
    int X = getMaxX();
    int Z = getMaxZ();
    int x = 0xFFFFFFFF ^ random.nextInt(X);
    int y = 63;
    int z = random.nextInt(Z);
    Location loc = new Location(world, x, y, z);
    loc.setY(loc.getWorld().getHighestBlockYAt(loc));
    player.teleport(loc);
    player.sendMessage(ChatColor.AQUA + "Diamcraft" + ChatColor.RED + " has teleported you to: ");
    player.sendMessage(ChatColor.RED + "X: " + ChatColor.AQUA + x);
    player.sendMessage(ChatColor.RED + "Y: " + ChatColor.AQUA + y);
    player.sendMessage(ChatColor.RED + "Z: " + ChatColor.AQUA + z);
    player.sendMessage(ChatColor.RED + "World: " + ChatColor.AQUA + player.getWorld().getName());
  }

  public static void sendSafeTeleport(Player player) {
    World world = Bukkit.getWorld(getWorld());
    Random random = new Random();
    int X = getMaxX();
    int Z = getMaxZ();
    int x = random.nextInt(X);
    int y = 63;
    int z = random.nextInt(Z);
    Location loc = new Location(world, x, y, z);
    Location glass = new Location(world, x - 1, y - 1, z - 1);
    loc.setY(loc.getWorld().getHighestBlockYAt(loc));
    if ((loc.getBlock().getType() == Material.AIR) || (loc.getBlock().getType() == Material.WATER) || (loc.getBlock().getType() == Material.LAVA)) {
      glass.getBlock().setType(Material.GLASS);
    }

    player.teleport(loc);
    player.sendMessage(ChatColor.AQUA + "Diamcraft" + ChatColor.RED + " has safe teleported to: ");
    player.sendMessage(ChatColor.RED + "X: " + ChatColor.AQUA + x);
    player.sendMessage(ChatColor.RED + "Y: " + ChatColor.AQUA + y);
    player.sendMessage(ChatColor.RED + "Z: " + ChatColor.AQUA + z);
    player.sendMessage(ChatColor.RED + "World: " + ChatColor.AQUA + player.getWorld().getName());
  }
}
