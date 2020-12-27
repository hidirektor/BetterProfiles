package com.alonsoaliaga.betterprofiles.others;

import org.bukkit.Material;

public enum Materials {
   GUNPOWDER(new String[]{"GUNPOWDER", "SULPHUR"}),
   SULPHUR(new String[]{"GUNPOWDER", "SULPHUR"}),
   ENDER_EYE(new String[]{"ENDER_EYE", "EYE_OF_ENDER"}),
   EYE_OF_ENDER(new String[]{"ENDER_EYE", "EYE_OF_ENDER"}),
   PORK(new String[]{"PORK", "PORKCHOP"}),
   PORKCHOP(new String[]{"PORK", "PORKCHOP"}),
   GRILLED_PORK(new String[]{"GRILLED_PORK", "COOKED_PORKCHOP"}),
   COOKED_PORKCHOP(new String[]{"GRILLED_PORK", "COOKED_PORKCHOP"}),
   CLOCK(new String[]{"CLOCK", "WATCH"}),
   WATCH(new String[]{"CLOCK", "WATCH"}),
   MELON_BLOCK(new String[]{"MELON", "MELON_BLOCK"}),
   MOB_SPAWNER(new String[]{"MOB_SPAWNER", "SPAWNER"}),
   SPAWNER(new String[]{"MOB_SPAWNER", "SPAWNER"}),
   BREWING_STAND_ITEM(new String[]{"BREWING_STAND_ITEM", "BREWING_STAND"}),
   CAULDRON_ITEM(new String[]{"CAULDRON_ITEM", "CAULDRON"}),
   SPECKLED_MELON(new String[]{"SPECKLED_MELON", "GLISTERING_MELON_SLICE"}),
   GLISTERING_MELON_SLICE(new String[]{"SPECKLED_MELON", "GLISTERING_MELON_SLICE"}),
   SUGAR_CANE_BLOCK(new String[]{"SUGAR_CANE_BLOCK", "SUGAR_CANE"}),
   WOOL(new String[]{"WOOL", "WHITE_WOOL"}),
   WHITE_WOOL(new String[]{"WOOL", "WHITE_WOOL"}),
   WOOD(new String[]{"WOOD", "OAK_PLANKS"}),
   OAK_PLANKS(new String[]{"WOOD", "OAK_PLANKS"}),
   WORKBENCH(new String[]{"WORKBENCH", "CRAFTING_TABLE"}),
   CRAFTING_TABLE(new String[]{"WORKBENCH", "CRAFTING_TABLE"}),
   ENCHANTING_TABLE(new String[]{"ENCHANTMENT_TABLE", "ENCHANTING_TABLE"}),
   ENCHANTMENT_TABLE(new String[]{"ENCHANTMENT_TABLE", "ENCHANTING_TABLE"}),
   WITHER_SKELETON_SKULL(new String[]{"WITHER_SKELETON_SKULL", "SKULL_ITEM"}),
   SKELETON_SKULL(new String[]{"SKELETON_SKULL", "SKULL_ITEM"}),
   ZOMBIE_HEAD(new String[]{"ZOMBIE_HEAD", "SKULL_ITEM"}),
   CREEPER_HEAD(new String[]{"CREEPER_HEAD", "SKULL_ITEM"}),
   DRAGON_HEAD(new String[]{"DRAGON_HEAD", "SKULL_ITEM"}),
   PLAYER_HEAD(new String[]{"PLAYER_HEAD", "SKULL_ITEM"}),
   SEEDS(new String[]{"SEEDS", "WHEAT_SEEDS"}),
   WHEAT_SEEDS(new String[]{"SEEDS", "WHEAT_SEEDS"}),
   RAW_FISH(new String[]{"RAW_FISH", "COD"}),
   RAW_CHICKEN(new String[]{"RAW_CHICKEN", "CHICKEN"}),
   CHICKEN(new String[]{"RAW_CHICKEN", "CHICKEN"}),
   RAW_BEEF(new String[]{"RAW_BEEF", "BEEF"}),
   BEEF(new String[]{"RAW_BEEF", "BEEF"}),
   SALMON(new String[]{"RAW_FISH", "SALMON"}),
   COD(new String[]{"RAW_FISH", "COD"}),
   COOKED_FISH(new String[]{"COOKED_FISH", "COOKED_COD"}),
   COOKED_COD(new String[]{"COOKED_FISH", "COOKED_COD"}),
   SMOOTH_BRICK(new String[]{"SMOOTH_BRICK", "STONE_BRICKS"}),
   STONE_BRICKS(new String[]{"SMOOTH_BRICK", "STONE_BRICKS"}),
   MUSHROOM_SOUP(new String[]{"MUSHROOM_SOUP", "MUSHROOM_STEW"}),
   SUSPICIOUS_STEW(new String[]{"SUSPICIOUS_STEW", "MUSHROOM_SOUP"}),
   BEETROOT_SOUP(new String[]{"BEETROOT_SOUP", "MUSHROOM_SOUP"}),
   MUSHROOM_STEW(new String[]{"MUSHROOM_SOUP", "MUSHROOM_STEW"}),
   TOTEM(new String[]{"TOTEM", "TOTEM_OF_UNDYING", "NETHER_STAR"}),
   TOTEM_OF_UNDYING(new String[]{"TOTEM", "TOTEM_OF_UNDYING", "NETHER_STAR"}),
   CARROT_ITEM(new String[]{"CARROT_ITEM", "CARROT"});

   private Material material = null;

   private Materials(String... materials) {
      String[] var4 = materials;
      int var5 = materials.length;
      int var6 = 0;

      while(var6 < var5) {
         String name = var4[var6];

         try {
            this.material = Material.valueOf(name);
            break;
         } catch (Exception var9) {
            ++var6;
         }
      }

   }

   public Material getMaterial() {
      return this.material;
   }

   public static Materials valueFrom(String string) {
      Materials[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Materials value = var1[var3];
         if (value.name().equals(string)) {
            return value;
         }
      }

      return null;
   }
}
