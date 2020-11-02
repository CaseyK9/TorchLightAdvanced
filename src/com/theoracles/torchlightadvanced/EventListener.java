package com.theoracles.torchlightadvanced;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventListener implements Listener {
  HashMap<String, Location> locs = new HashMap<>();
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Material[] items = { Material.JACK_O_LANTERN, Material.TORCH, Material.GLOWSTONE, Material.GLOWSTONE_DUST, 
        Material.REDSTONE_BLOCK, Material.REDSTONE_TORCH, Material.LAVA_BUCKET, Material.LAVA, Material.NETHER_STAR };
    boolean makeLight = false;
    byte b;
    int i;
    Material[] arrayOfMaterial1;
    for (i = (arrayOfMaterial1 = items).length, b = 0; b < i; ) {
      Material m = arrayOfMaterial1[b];
      if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(m)) {
          makeLight = true;
          break; 
      }
      b++;
    } 
    if (makeLight) {
      setLight(event.getPlayer());
    } else {
      disableLight(event.getPlayer());
    } 
  }
  
  public void setLight(Player p) {
    Location prevLoc = this.locs.get(p.getName());
    Location newLoc = getBlockUnderPlayer(p);
    if (prevLoc != null && prevLoc.getWorld().equals(p.getWorld()))
      disableLight(p); 
    if (newLoc.getY() >= 0.0D && p.getWorld().getBlockAt(newLoc).getType().isSolid()) {
      setBlock(p, newLoc, Material.GLOWSTONE, p.getWorld().getBlockAt(newLoc).getBlockData());
      this.locs.put(p.getName(), newLoc);
    } else {
      this.locs.put(p.getName(), null);
    } 
  }
  
  public void disableLight(Player p) {
    if (this.locs.get(p.getName()) != null) {
      Location loc = this.locs.get(p.getName());
      Block realBlock = loc.getWorld().getBlockAt(loc);
      setBlock(p, loc, realBlock.getType(), realBlock.getBlockData());
    } 
  }
  
  public void setBlock(Player p, Location l, Material m, BlockData blockData) {
	p.sendBlockChange(l, m.createBlockData());
  }
  
  public Location getBlockUnderPlayer(Player p) {
    for (int y = (int)p.getLocation().getY() - 1; y >= 0; y--) {
      Location l = p.getLocation();
      l.setY(y);
      Block b = p.getWorld().getBlockAt(l);
      if (b.getType().isSolid())
        return l; 
    } 
    return null;
  }
}
