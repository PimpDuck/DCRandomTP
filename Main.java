package me.PimpDuck.DCRandomTP;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin
{
  public YamlHelper yamlHandler;
  public int minX;
  public int maxX;
  public int minZ;
  public int maxZ;
  public String world;
  public static Main plugin;
  public String tp_message;


  Logger log = Logger.getLogger("Minecraft");
  
  public static FileManager configurationFile;
  public static HashMap<String, Long> delayedPlayers;
  private static int delay;
  
  public void onEnable()
  {
	saveDefaultConfig();
    configurationFile = new FileManager(this, "config.yml");
    delayedPlayers = new HashMap<String, Long>();
    delay = getConfig().getInt("DCRandomTP.Options.Cooldown");
    plugin = this;
    this.yamlHandler = new YamlHelper(this);
    this.minX = this.yamlHandler.config.getInt("DCRandomTP.Min-x", 1);
    this.maxX = this.yamlHandler.config.getInt("DCRandomTP.Max-x", 100);
    this.minZ = this.yamlHandler.config.getInt("DCRandomTP.Min-z", 1);
    this.maxZ = this.yamlHandler.config.getInt("DCRandomTP.Max-z", 100);
    this.world = this.yamlHandler.config.getString("DCRandomTP.World", "world");
    this.tp_message = this.yamlHandler.config.getString("DCRandomTP.Message", "&aPlease wait while the plugin finds you a safe location.");
    log.info("[DCRandomTP] has been enabled!");
    getCommand("random").setExecutor(new CommandClass());
  }
	@Override
	public void onDisable(){
		log.info("[DCRandomTP] has been disabled!");
	}
	
	  public static void addDelayedPlayer(Player player) {
		    delayedPlayers.put(player.getName(), System.currentTimeMillis());
		  }

		  public static void removeDelayedPlayer(Player player) {
		    delayedPlayers.remove(player.getName());
		  }

		  public static boolean playerDelayed(Player player) {
		    return delayedPlayers.containsKey(player.getName());
		  }

		  public static Long getPlayerDelay(Player player) {
		    return (Long)delayedPlayers.get(player.getName());
		  }

		  public static int getDelay(int multiplier) {
		    return delay * multiplier;
		  }

		  public static int getRemainingTime(Player player) {
		    return (int)(getDelay(1) - (System.currentTimeMillis() - getPlayerDelay(player).longValue()) / 1000L / 60 / 60);
		  }
}
