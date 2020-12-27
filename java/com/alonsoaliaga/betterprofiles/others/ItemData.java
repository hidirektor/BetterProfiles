package com.alonsoaliaga.betterprofiles.others;

import com.alonsoaliaga.betterprofiles.enums.ItemType;
import com.alonsoaliaga.betterprofiles.utils.AlonsoUtils;
import com.alonsoaliaga.betterprofiles.utils.LocalUtils;
import java.util.List;
import java.util.stream.Collectors;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemData {
   private String identifier;
   private int slot;
   private int dynamicSlot;
   private String placeholder;
   private boolean placeholderRegex;
   private String accepted;
   private boolean closeOnClick;
   private ItemType itemType;
   private String permission;
   private List<String> commands;
   private ItemStack baseItem;
   private String displayname;
   private List<String> lore;

   public ItemData(String identifier, int slot, int dynamicSlot, String placeholder, boolean placeholderRegex, String accepted, boolean closeOnClick, ItemType itemType, String permission, List<String> commands, ItemStack baseItem, String displayname, List<String> lore) {
      this.identifier = identifier;
      this.slot = slot;
      this.dynamicSlot = dynamicSlot;
      this.placeholder = placeholder;
      this.placeholderRegex = placeholderRegex;
      this.accepted = accepted;
      this.closeOnClick = closeOnClick;
      this.itemType = itemType;
      this.permission = permission;
      this.commands = commands;
      this.baseItem = baseItem;
      this.displayname = displayname;
      this.lore = lore;
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public int getSlot() {
      return this.slot;
   }

   public ItemType getItemType() {
      return this.itemType;
   }

   public boolean hasCloseOnClick() {
      return this.closeOnClick;
   }

   public boolean hasCommands() {
      return !this.commands.isEmpty();
   }

   public List<String> getCommands() {
      return this.commands;
   }

   public boolean hasPermission() {
      return this.permission != null;
   }

   public String getPermission() {
      return this.permission;
   }

   public int getDynamicSlot() {
      return this.dynamicSlot;
   }

   public boolean isPrivateItem() {
      return this.itemType == ItemType.ONLY_OWNER;
   }

   public boolean isViewerItem() {
      return this.itemType == ItemType.ONLY_VIEWER;
   }

   public boolean hasDynamicSlot() {
      return this.itemType == ItemType.DYNAMIC;
   }

   public boolean hasPlaceholderCondition() {
      return this.itemType == ItemType.PLACEHOLDER;
   }

   public boolean checkPlaceholder(Player player) {
      return this.placeholderRegex ? PlaceholderAPI.setPlaceholders(player, this.placeholder).matches(this.accepted) : PlaceholderAPI.setPlaceholders(player, this.placeholder).equals(this.accepted);
   }

   public String getPlaceholder() {
      return this.placeholder;
   }

   public String getAccepted() {
      return this.accepted;
   }

   public ItemStack buildItem(Player viewer, Player owner) {
      ItemStack cloned = this.baseItem.clone();
      if (this.itemType == ItemType.PROFILE_OWNER) {
         SkullMeta clonedMeta = (SkullMeta)cloned.getItemMeta();
         if (AlonsoUtils.serverVersion.isOlderEqualThan(AlonsoUtils.ServerVersion.v1_12)) {
            clonedMeta.setOwner(owner.getName());
         } else {
            clonedMeta.setOwningPlayer(owner);
         }

         cloned.setItemMeta(clonedMeta);
      }

      ItemMeta clonedMeta = cloned.getItemMeta();
      if (AlonsoUtils.PluginUtils.hasPlaceholderApiSupport()) {
         clonedMeta.setDisplayName(LocalUtils.colorize(PlaceholderAPI.setPlaceholders(owner, this.displayname.replace("{PLAYER}", viewer.getName()).replace("{OWNER}", owner.getName()))));
         clonedMeta.setLore((List)this.lore.stream().map((line) -> {
            return PlaceholderAPI.setPlaceholders(owner, line.replace("{PLAYER}", viewer.getName()).replace("{OWNER}", owner.getName()));
         }).collect(Collectors.toList()));
      } else {
         clonedMeta.setDisplayName(LocalUtils.colorize(this.displayname.replace("{PLAYER}", viewer.getName()).replace("{OWNER}", owner.getName())));
         clonedMeta.setLore((List)this.lore.stream().map((line) -> {
            return LocalUtils.colorize(PlaceholderAPI.setPlaceholders(owner, line.replace("{PLAYER}", viewer.getName()).replace("{OWNER}", owner.getName())));
         }).collect(Collectors.toList()));
      }

      cloned.setItemMeta(clonedMeta);
      return cloned;
   }
}
