package com.alonsoaliaga.betterprofiles.others;

import java.util.UUID;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ProfileHolder implements InventoryHolder {
   private int type;
   private UUID ownerUUID;
   private String ownerName;

   public ProfileHolder(int type, UUID ownerUUID, String ownerName) {
      this.type = type;
      this.ownerUUID = ownerUUID;
      this.ownerName = ownerName;
   }

   public int getType() {
      return this.type;
   }

   public UUID getOwnerUUID() {
      return this.ownerUUID;
   }

   public String getOwnerName() {
      return this.ownerName;
   }

   public Inventory getInventory() {
      return null;
   }
}
