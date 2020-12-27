package com.alonsoaliaga.betterprofiles.libraries.armorequipevent;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArmorEquip implements Listener {
   private BetterProfiles plugin;

   public ArmorEquip(BetterProfiles plugin) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(new ArmorListener(plugin.getFiles().getArmorequip().get().getStringList("Blocked")), plugin);

      try {
         Class.forName("org.bukkit.event.block.BlockDispenseArmorEvent");
         plugin.getServer().getPluginManager().registerEvents(new DispenserArmorListener(), plugin);
      } catch (Exception var3) {
      }

   }

   public void test() {
      this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
   }

   @EventHandler
   public void equip(ArmorEquipEvent event) {
      System.out.println("ArmorEquipEvent - " + event.getMethod());
      System.out.println("Type: " + event.getType());
      System.out.println("New: " + (event.getNewArmorPiece() != null ? event.getNewArmorPiece().getType() : "null"));
      System.out.println("Old: " + (event.getOldArmorPiece() != null ? event.getOldArmorPiece().getType() : "null"));
      boolean test = true;
      if (test) {
         if (event.getOldArmorPiece() != null && event.getOldArmorPiece().getType().equals(Material.DIAMOND_HELMET)) {
            event.getPlayer().setGameMode(event.getPlayer().getGameMode().equals(GameMode.ADVENTURE) ? GameMode.SURVIVAL : GameMode.ADVENTURE);
         }

         if (event.getNewArmorPiece() != null && event.getNewArmorPiece().getType().equals(Material.DIAMOND_HELMET)) {
            event.getPlayer().setGameMode(event.getPlayer().getGameMode().equals(GameMode.ADVENTURE) ? GameMode.SURVIVAL : GameMode.ADVENTURE);
         }

         System.out.println("New Gamemode: " + event.getPlayer().getGameMode());
      }

   }
}
