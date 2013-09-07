package me.PimpDuck.DCRandomTP;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Cooldown
{
  private static Set<PlayerCooldown> cooldowns = new HashSet();

  public static void addCooldown(String cooldownName, String player, long lengthInMillis) {
    PlayerCooldown pc = new PlayerCooldown(cooldownName, player, lengthInMillis);
    Iterator it = cooldowns.iterator();

    while (it.hasNext()) {
      PlayerCooldown iterated = (PlayerCooldown)it.next();
      if ((!iterated.getPlayerName().equalsIgnoreCase(pc.getPlayerName())) || 
        (!iterated.getCooldownName().equalsIgnoreCase(pc.getCooldownName()))) continue;
      it.remove();
    }

    cooldowns.add(pc);
  }

  public static PlayerCooldown getCooldown(String cooldownName, String playerName, long length) {
    Iterator it = cooldowns.iterator();
    while (it.hasNext()) {
      PlayerCooldown pc = (PlayerCooldown)it.next();
      if ((pc.getCooldownName().equalsIgnoreCase(cooldownName)) && 
        (pc.getPlayerName().equalsIgnoreCase(playerName))) {
        return pc;
      }
    }

    return new PlayerCooldown(cooldownName, playerName, length, true);
  }
}
