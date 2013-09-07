package me.PimpDuck.DCRandomTP;

class PlayerCooldown
{
  private long startTime;
  private String playerName;
  private String cooldownName;
  private long lengthInMillis;
  private long endTime;
  private boolean overFirst = false;

  PlayerCooldown(String cooldownName, String player, long lengthInMillis) {
    this.cooldownName = cooldownName;
    this.startTime = System.currentTimeMillis();
    this.playerName = player;
    this.lengthInMillis = lengthInMillis;
    this.endTime = (this.startTime + this.lengthInMillis);
  }
  public PlayerCooldown(String cooldownName, String playerName, long length, boolean over) {
    this(cooldownName, playerName, length);
    this.overFirst = true;
  }

  public boolean isOver()
  {
    if (this.overFirst) {
      this.overFirst = false;
      return true;
    }
    return this.endTime < System.currentTimeMillis();
  }

  public int getTimeLeft() {
    return (int)(this.endTime - System.currentTimeMillis());
  }

  public String getPlayerName() {
    return this.playerName;
  }

  public String getCooldownName() {
    return this.cooldownName;
  }

  public void reset() {
    this.startTime = System.currentTimeMillis();
    this.endTime = (this.startTime + this.lengthInMillis);
  }
}
