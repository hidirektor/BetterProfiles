package com.alonsoaliaga.betterprofiles;

import com.alonsoaliaga.betterprofiles.api.events.ProfileOpenEvent;
import com.alonsoaliaga.betterprofiles.commands.MainCommand;
import com.alonsoaliaga.betterprofiles.commands.ProfileCommand;
import com.alonsoaliaga.betterprofiles.enums.ColorType;
import com.alonsoaliaga.betterprofiles.enums.ItemType;
import com.alonsoaliaga.betterprofiles.libraries.armorequipevent.ArmorEquip;
import com.alonsoaliaga.betterprofiles.listeners.ArmorEquipListener;
import com.alonsoaliaga.betterprofiles.listeners.ClickListener;
import com.alonsoaliaga.betterprofiles.listeners.CloseListener;
import com.alonsoaliaga.betterprofiles.listeners.ConnectionListener;
import com.alonsoaliaga.betterprofiles.listeners.ConsumeListener;
import com.alonsoaliaga.betterprofiles.listeners.InteractListener;
import com.alonsoaliaga.betterprofiles.listeners.PlayerHeldListener;
import com.alonsoaliaga.betterprofiles.listeners.SwapHandListener;
import com.alonsoaliaga.betterprofiles.metrics.Metrics;
import com.alonsoaliaga.betterprofiles.others.Database;
import com.alonsoaliaga.betterprofiles.others.FileManager;
import com.alonsoaliaga.betterprofiles.others.ItemData;
import com.alonsoaliaga.betterprofiles.others.Messages;
import com.alonsoaliaga.betterprofiles.others.Permissions;
import com.alonsoaliaga.betterprofiles.others.PlayerData;
import com.alonsoaliaga.betterprofiles.others.ProfileHolder;
import com.alonsoaliaga.betterprofiles.utils.AlonsoUtils;
import com.alonsoaliaga.betterprofiles.utils.ItemUtils;
import com.alonsoaliaga.betterprofiles.utils.LocalUtils;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BetterProfiles extends JavaPlugin implements AlonsoUtils.AlonsoPlugin {
   private static BetterProfiles instance;
   private AlonsoUtils.PluginUtils pluginUtils;
   private FileManager fileManager;
   private AlonsoUtils.Updater updater = null;
   public MainCommand mainCommand;
   public ProfileCommand profileCommand;
   public Messages messages;
   public Permissions permissions;
   private int bStatsID = 8746;
   private String resourceID = "83529";
   public Database.Data database = null;
   public ArmorEquip armorEquip = null;
   private HashMap<UUID, HashMap<UUID, Inventory>> openedProfilesMap;
   private HashMap<Integer, LinkedList<ItemData>> itemsMap;
   private HashMap<UUID, PlayerData> dataMap;
   private ItemStack frameStainedPane;
   private ItemStack emptyStainedPane;
   private ItemStack hiddenStainedPane;
   public ClickListener clickListener;
   public CloseListener closeListener;
   public ConnectionListener connectionListener;
   public SwapHandListener swapHandListener = null;
   public ArmorEquipListener armorEquipListener = null;
   public PlayerHeldListener playerHeldListener = null;
   public ConsumeListener consumeListener = null;
   public InteractListener interactListener = null;
   public List<String> disabledWorlds;
   public boolean fillEmptySlots;
   public boolean profileOnInteract;
   public boolean profileOnCommand;
   public boolean checkVisibility;
   public boolean onlySneak;
   public boolean preventShield;
   public boolean preventInteract;
   public boolean debugMode = false;
   public int profileRows;
   public int closeSlot;
   public int helmetSlot;
   public int chestplateSlot;
   public int leggingsSlot;
   public int bootsSlot;
   public int mainHandSlot;
   public int offHandSlot;
   public int keepAliveInterval;
   public ItemStack closeItem;
   public ItemStack noHelmetItem;
   public ItemStack noChestplateItem;
   public ItemStack noLeggingsItem;
   public ItemStack noBootsItem;
   public ItemStack noMainHandItem;
   public ItemStack noOffHandItem;
   public String noHelmetItemPermission;
   public String noChestplateItemPermission;
   public String noLeggingsItemPermission;
   public String noBootsItemPermission;
   public String noMainHandItemPermission;
   public String noOffHandItemPermission;
   public ItemStack noHelmetHiddenItem;
   public ItemStack noChestplateHiddenItem;
   public ItemStack noLeggingsHiddenItem;
   public ItemStack noBootsHiddenItem;
   public ItemStack noMainHandHiddenItem;
   public ItemStack noOffHandHiddenItem;
   private Sound openSound;
   public boolean betterReviveHooked;
   public BukkitTask keepAliveTask = null;

   public void onEnable() {
      AlonsoUtils.sendEnableText(this);
      AlonsoUtils.isSupported();
      this.openedProfilesMap = new HashMap();
      this.itemsMap = new HashMap();
      this.dataMap = new HashMap();
      instance = this;
      this.fileManager = new FileManager(this);
      this.updateConfiguration();
      this.pluginUtils = new AlonsoUtils.PluginUtils(this);
      if (this.getFiles().getConfig().get().getString("Database.Type", "sqlite").equalsIgnoreCase("mysql")) {
         this.database = new Database().new MySQL(this);
      } else {
         this.database = new Database().new SQLite(this);
      }

      this.reloadMessages();
      this.messages = new Messages(this);
      this.permissions = new Permissions(this);
      List<String> mainAliases = (List)this.getFiles().getConfig().get().getStringList("Options.Main.Aliases").stream().map((s) -> {
         return s.replace(" ", "").trim();
      }).filter((s) -> {
         return !s.isEmpty();
      }).collect(Collectors.toList());
      this.mainCommand = new MainCommand(this, "betterprofiles", mainAliases);
      List<String> profileAliases = (List)this.getFiles().getConfig().get().getStringList("Options.Profile.Aliases").stream().map((s) -> {
         return s.replace(" ", "").trim();
      }).filter((s) -> {
         return !s.isEmpty();
      }).collect(Collectors.toList());
      this.profileCommand = new ProfileCommand(this, "profile", profileAliases);
      this.clickListener = new ClickListener(this);
      this.closeListener = new CloseListener(this);
      this.connectionListener = new ConnectionListener(this);
      if (this.getFiles().getConfig().get().getBoolean("Options.Update-equipment", true)) {
         LocalUtils.logp("Enabling real time update profile..");
         this.armorEquip = new ArmorEquip(this);
         this.armorEquipListener = new ArmorEquipListener(this);
         this.playerHeldListener = new PlayerHeldListener(this);
         this.consumeListener = new ConsumeListener(this);

         try {
            Class.forName("org.bukkit.event.player.PlayerSwapHandItemsEvent");
            this.swapHandListener = new SwapHandListener(this);
            LocalUtils.logp("PlayerSwapHandItemsEvent class found. Enabling..");
         } catch (Throwable var5) {
            LocalUtils.logp("PlayerSwapHandItemsEvent is not supported. Skipping..");
         }

         LocalUtils.logp("Real time profile update is enabled.");
      } else {
         LocalUtils.logp("Real time profile update is disabled.");
      }

      if (this.profileOnInteract) {
         this.interactListener = new InteractListener(this);
         LocalUtils.logp("Enabled profile on right click.");
      }

      if (this.bStatsID != 0) {
         Metrics metrics = new Metrics(this, this.bStatsID);
         metrics.addCustomChart(new Metrics.SimplePie("server_type", () -> {
            return LocalUtils.firstCase(AlonsoUtils.getServerType().toString());
         }));
         metrics.addCustomChart(new Metrics.SimplePie("protocollib_hooked", () -> {
            return this.pluginUtils.isProtocolLibSupported() ? "Yes" : "No";
         }));
         metrics.addCustomChart(new Metrics.SimplePie("placeholderapi_hooked", () -> {
            return this.pluginUtils.isPlaceholderApiSupported() ? "Yes" : "No";
         }));
         metrics.addCustomChart(new Metrics.SimplePie("nbtapi_hooked", () -> {
            return this.pluginUtils.isNbtApiSupported() ? "Yes" : "No";
         }));
         Bukkit.getScheduler().runTaskLater(this, () -> {
            metrics.addCustomChart(new Metrics.AdvancedPie("plugins_from_alonsoaliaga", () -> {
               Map<String, Integer> pluginsFromAlonsoAliaga = new HashMap();
               Plugin[] var2 = this.getServer().getPluginManager().getPlugins();
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  Plugin registeredPlugin = var2[var4];
                  if (registeredPlugin.getDescription().getAuthors().contains("AlonsoAliaga") && !registeredPlugin.getName().equalsIgnoreCase(this.getDescription().getName())) {
                     pluginsFromAlonsoAliaga.put(registeredPlugin.getName(), 1);
                  }
               }

               return pluginsFromAlonsoAliaga;
            }));
         }, 1200L);
      }

      if (this.getFiles().getConfig().get().getBoolean("Updates.Check-updates", true)) {
         this.updater = new AlonsoUtils.Updater(this, this.resourceID, this.getFiles().getConfig().get().getBoolean("Updates.Notify-updates", true), this.getFiles().getConfig().get().getString("Updates.Permission", (String)null), this.getFiles().getConfig().get().getString("Updates.Message", (String)null));
      }

      this.loadItems();
      Iterator var6 = this.getServer().getOnlinePlayers().iterator();

      while(var6.hasNext()) {
         Player onlinePlayer = (Player)var6.next();
         this.connectionListener.loadPlayer(onlinePlayer);
      }

   }

   public void loadFirstItems() {
      String frameColorString = this.getFiles().getConfig().get().getString("Options.Fill-empty.Color-frame", "BLACK");
      String emptySlotColorString = this.getFiles().getConfig().get().getString("Options.Empty-slot.Color-frame", "WHITE");
      String hiddenSlotColorString = this.getFiles().getConfig().get().getString("Options.Hidden-slot.Color-frame", "RED");
      ColorType frameColorType = ColorType.getColor(frameColorString, ColorType.BLACK);

      try {
         this.frameStainedPane = new ItemStack(Material.valueOf(frameColorString + "_STAINED_GLASS_PANE"));
      } catch (IllegalArgumentException var10) {
         this.frameStainedPane = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short)frameColorType.getData());
      }

      ColorType emptyColorType = ColorType.getColor(emptySlotColorString, ColorType.WHITE);

      try {
         this.emptyStainedPane = new ItemStack(Material.valueOf(emptySlotColorString + "_STAINED_GLASS_PANE"));
      } catch (IllegalArgumentException var9) {
         this.emptyStainedPane = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short)emptyColorType.getData());
      }

      ColorType hiddenColorType = ColorType.getColor(hiddenSlotColorString, ColorType.RED);

      try {
         this.hiddenStainedPane = new ItemStack(Material.valueOf(hiddenSlotColorString + "_STAINED_GLASS_PANE"));
      } catch (IllegalArgumentException var8) {
         this.hiddenStainedPane = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short)hiddenColorType.getData());
      }

      ItemMeta blackPaneMeta = this.frameStainedPane.getItemMeta();
      blackPaneMeta.setDisplayName("§r");
      this.frameStainedPane.setItemMeta(blackPaneMeta);
   }

   public void loadItems() {
      this.itemsMap = new HashMap();
      ConfigurationSection itemsSection = this.getFiles().getItems().get().getConfigurationSection("Items");
      Iterator var2 = itemsSection.getKeys(false).iterator();

      while(true) {
         while(var2.hasNext()) {
            String itemIdentifier = (String)var2.next();
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(itemIdentifier);
            int slot = Math.max(-1, itemSection.getInt("Slot", -1));
            if (slot == -1) {
               LocalUtils.logp("Slot for item '" + itemIdentifier + "' is negative. Skipping..");
            } else if (slot >= this.profileRows * 9) {
               LocalUtils.logp("Slot (" + slot + ") for item '" + itemIdentifier + "' is not valid for the amount of rows specified for profiles. Max: " + this.profileRows * 9);
            } else {
               String materialString = itemSection.getString("Material", "COBBLESTONE");
               short durability = (short)Math.max(-1, itemSection.getInt("Data", -1));
               ItemType itemType = ItemType.ITEM_STACK;
               String placeholder = "Developed by AlonsoAliaga";
               String accepted = "Developed by AlonsoAliaga";
               int dynamicSlot = 0;
               boolean placeholderRegex = false;
               ItemStack baseItem;
               Material material;
               int customModelData;
               ItemMeta baseMeta;
               if (materialString.toUpperCase().startsWith("PLACEHOLDER:")) {
                  if (!this.pluginUtils.isPlaceholderApiSupported()) {
                     LocalUtils.logp("Item '" + itemIdentifier + "' requires PlaceholderAPI, which is not installed. Skipping item..");
                     continue;
                  }

                  placeholder = itemSection.getString("Placeholder", "Developed by AlonsoAliaga");
                  accepted = itemSection.getString("Accepted", "Developed by AlonsoAliaga");
                  materialString = materialString.toUpperCase().replace("PLACEHOLDER:", "");
                  itemType = ItemType.PLACEHOLDER;
                  placeholderRegex = itemSection.getBoolean("Regex", false);
                  if (materialString.equalsIgnoreCase("CUSTOM_HEAD")) {
                     baseItem = ItemUtils.buildHead(itemSection.getString("Texture", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDQ0NjkyMDI5NiwKICAicHJvZmlsZUlkIiA6ICJlMWMxYTE5NDdlODY0MTRmODZiYjQyZDgyYTIxY2ZiOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbG9uc29BbGlhZ2EiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY4YWE3NGNjODg1NzgwNjBkMGZlM2JkOTA4YjM0ZjY5ZjEyZTA3ZjFmODljNWRkYzE1ZTZiZjYwZjA0Y2NjNiIKICAgIH0KICB9Cn0="));
                  } else {
                     material = LocalUtils.getMaterial(materialString);
                     if (material == null) {
                        LocalUtils.logp("Selected 'Material' for '" + itemIdentifier + "' item is not valid. Skipping item..");
                        continue;
                     }

                     if (durability != -1) {
                        baseItem = new ItemStack(material, 1, durability);
                     } else {
                        baseItem = new ItemStack(material);
                     }

                     customModelData = this.pluginUtils.isCustomModelSupported() ? Math.max(0, itemSection.getInt("Custom-model-data", 0)) : 0;
                     if (customModelData != 0) {
                        baseMeta = baseItem.getItemMeta();
                        baseMeta.setCustomModelData(customModelData);
                        baseItem.setItemMeta(baseMeta);
                     }
                  }
               } else if (materialString.toUpperCase().startsWith("ONLY_OWNER:")) {
                  materialString = materialString.toUpperCase().replace("ONLY_OWNER:", "");
                  itemType = ItemType.ONLY_OWNER;
                  if (this.debugMode) {
                     LocalUtils.logp("materialString is: " + materialString);
                  }

                  if (materialString.equalsIgnoreCase("CUSTOM_HEAD")) {
                     baseItem = ItemUtils.buildHead(itemSection.getString("Texture", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDQ0NjkyMDI5NiwKICAicHJvZmlsZUlkIiA6ICJlMWMxYTE5NDdlODY0MTRmODZiYjQyZDgyYTIxY2ZiOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbG9uc29BbGlhZ2EiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY4YWE3NGNjODg1NzgwNjBkMGZlM2JkOTA4YjM0ZjY5ZjEyZTA3ZjFmODljNWRkYzE1ZTZiZjYwZjA0Y2NjNiIKICAgIH0KICB9Cn0="));
                  } else {
                     material = LocalUtils.getMaterial(materialString);
                     if (material == null) {
                        LocalUtils.logp("Selected 'Material' for '" + itemIdentifier + "' item is not valid. Skipping item..");
                        continue;
                     }

                     if (durability != -1) {
                        baseItem = new ItemStack(material, 1, durability);
                     } else {
                        baseItem = new ItemStack(material);
                     }

                     customModelData = this.pluginUtils.isCustomModelSupported() ? Math.max(0, itemSection.getInt("Custom-model-data", 0)) : 0;
                     if (customModelData != 0) {
                        baseMeta = baseItem.getItemMeta();
                        baseMeta.setCustomModelData(customModelData);
                        baseItem.setItemMeta(baseMeta);
                     }
                  }
               } else if (materialString.toUpperCase().startsWith("ONLY_VIEWER:")) {
                  materialString = materialString.toUpperCase().replace("ONLY_VIEWER:", "");
                  itemType = ItemType.ONLY_VIEWER;
                  if (this.debugMode) {
                     LocalUtils.logp("materialString is: " + materialString);
                  }

                  if (materialString.equalsIgnoreCase("CUSTOM_HEAD")) {
                     baseItem = ItemUtils.buildHead(itemSection.getString("Texture", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDQ0NjkyMDI5NiwKICAicHJvZmlsZUlkIiA6ICJlMWMxYTE5NDdlODY0MTRmODZiYjQyZDgyYTIxY2ZiOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbG9uc29BbGlhZ2EiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY4YWE3NGNjODg1NzgwNjBkMGZlM2JkOTA4YjM0ZjY5ZjEyZTA3ZjFmODljNWRkYzE1ZTZiZjYwZjA0Y2NjNiIKICAgIH0KICB9Cn0="));
                  } else {
                     material = LocalUtils.getMaterial(materialString);
                     if (material == null) {
                        LocalUtils.logp("Selected 'Material' for '" + itemIdentifier + "' item is not valid. Skipping item..");
                        continue;
                     }

                     if (durability != -1) {
                        baseItem = new ItemStack(material, 1, durability);
                     } else {
                        baseItem = new ItemStack(material);
                     }

                     customModelData = this.pluginUtils.isCustomModelSupported() ? Math.max(0, itemSection.getInt("Custom-model-data", 0)) : 0;
                     if (customModelData != 0) {
                        baseMeta = baseItem.getItemMeta();
                        baseMeta.setCustomModelData(customModelData);
                        baseItem.setItemMeta(baseMeta);
                     }
                  }
               } else if (materialString.equalsIgnoreCase("DYNAMIC")) {
                  itemType = ItemType.DYNAMIC;
                  baseItem = this.emptyStainedPane.clone();
                  dynamicSlot = itemSection.getInt("Dynamic-slot", 0);
               } else if (materialString.equalsIgnoreCase("CUSTOM_HEAD")) {
                  itemType = ItemType.CUSTOM_HEAD;
                  baseItem = ItemUtils.buildHead(itemSection.getString("Texture", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDQ0NjkyMDI5NiwKICAicHJvZmlsZUlkIiA6ICJlMWMxYTE5NDdlODY0MTRmODZiYjQyZDgyYTIxY2ZiOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbG9uc29BbGlhZ2EiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY4YWE3NGNjODg1NzgwNjBkMGZlM2JkOTA4YjM0ZjY5ZjEyZTA3ZjFmODljNWRkYzE1ZTZiZjYwZjA0Y2NjNiIKICAgIH0KICB9Cn0="));
               } else if (materialString.equalsIgnoreCase("PROFILE_OWNER")) {
                  itemType = ItemType.PROFILE_OWNER;
                  baseItem = ItemUtils.getCleanHead();
               } else {
                  material = LocalUtils.getMaterial(materialString);
                  if (material == null) {
                     LocalUtils.logp("Selected material for '" + itemIdentifier + "' item is not valid. Skipping item..");
                     continue;
                  }

                  if (durability != -1) {
                     baseItem = new ItemStack(material, 1, durability);
                  } else {
                     baseItem = new ItemStack(material);
                  }

                  customModelData = this.pluginUtils.isCustomModelSupported() ? Math.max(0, itemSection.getInt("Custom-model-data", 0)) : 0;
                  if (customModelData != 0) {
                     baseMeta = baseItem.getItemMeta();
                     baseMeta.setCustomModelData(customModelData);
                     baseItem.setItemMeta(baseMeta);
                  }
               }

               String permission = itemSection.getString("Permission", "none");
               if (permission.equalsIgnoreCase("none")) {
                  permission = null;
               }

               String displayname = itemSection.getString("Displayname", "&cUnnamed Item");
               List<String> lore = itemSection.getStringList("Lore");
               boolean closeOnClick = itemSection.getBoolean("Close-on-click", true);
               List<String> commands = itemSection.getStringList("Commands");
               ItemData itemData = new ItemData(itemIdentifier, slot, dynamicSlot, placeholder, placeholderRegex, accepted, closeOnClick, itemType, permission, commands, baseItem, displayname, lore);
               if (!this.itemsMap.containsKey(slot)) {
                  this.itemsMap.put(slot, new LinkedList());
               }

               LinkedList<ItemData> itemList = (LinkedList)this.itemsMap.get(slot);
               itemList.add(itemData);
               if (this.debugMode) {
                  LocalUtils.logp("Successfully loaded " + itemIdentifier + "' item for slot " + slot + ". Enabling..");
               }
            }
         }

         LocalUtils.logp("Loaded " + this.itemsMap.size() + " items!");
         return;
      }
   }

   public void onDisable() {
      Iterator var1 = this.dataMap.values().iterator();

      while(var1.hasNext()) {
         PlayerData playerData = (PlayerData)var1.next();
         this.connectionListener.savePlayer(playerData);
      }

      this.openedProfilesMap.clear();
      this.closeInventories();
      if (this.database != null) {
         this.database.closeConnection();
      }

      AlonsoUtils.sendDisableText();
   }

   public void closeInventories() {
      Iterator var1 = this.getServer().getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player onlinePlayer = (Player)var1.next();

         try {
            if (onlinePlayer.getOpenInventory().getTopInventory().getHolder() instanceof ProfileHolder) {
               onlinePlayer.closeInventory();
            }
         } catch (Throwable var4) {
            onlinePlayer.closeInventory();
         }
      }

   }

   public BetterProfiles getMain() {
      return this;
   }

   public Database.Data getDatabase() {
      return this.database;
   }

   public void reloadMessages() {
      this.loadFirstItems();
      int blackMetaCustomModelData = this.pluginUtils.isCustomModelSupported() ? Math.max(0, this.getFiles().getConfig().get().getInt("Options.Fill-empty.Custom-model-data", 0)) : 0;
      if (blackMetaCustomModelData != 0) {
         ItemMeta blackMeta = this.frameStainedPane.getItemMeta();
         blackMeta.setCustomModelData(blackMetaCustomModelData);
         this.frameStainedPane.setItemMeta(blackMeta);
      }

      this.profileRows = Math.max(1, Math.min(6, this.getFiles().getConfig().get().getInt("Options.Profiles.Rows", 6)));
      this.betterReviveHooked = this.getServer().getPluginManager().getPlugin("BetterRevive") != null;
      this.disabledWorlds = this.getFiles().getConfig().get().getStringList("Options.Disabled-worlds");
      this.debugMode = this.getFiles().getConfig().get().getBoolean("Options.Debug", false);
      this.fillEmptySlots = this.getFiles().getConfig().get().getBoolean("Options.Fill-empty.Enabled", true);
      this.preventShield = this.getFiles().getConfig().get().getBoolean("Options.Prevent-shield", true);
      this.preventInteract = this.getFiles().getConfig().get().getBoolean("Options.Prevent-interact", true);
      this.profileOnInteract = this.getFiles().getConfig().get().getBoolean("Options.View.Interact", true);
      this.profileOnCommand = this.getFiles().getConfig().get().getBoolean("Options.View.Command", true);
      if (this.profileOnCommand) {
         LocalUtils.logp("Enabled profile on command.");
      }

      this.checkVisibility = this.getFiles().getConfig().get().getBoolean("Options.Check-visibility", true);
      this.onlySneak = this.getFiles().getConfig().get().getBoolean("Options.Only-sneak", true);
      this.closeSlot = Math.max(-1, this.getFiles().getConfig().get().getInt("Options.Slots.Close", 49));
      this.closeSlot = this.closeSlot < this.profileRows * 9 ? this.closeSlot : -1;
      this.helmetSlot = Math.max(-1, this.getFiles().getConfig().get().getInt("Options.Slots.Helmet", 10));
      this.helmetSlot = this.helmetSlot < this.profileRows * 9 ? this.helmetSlot : -1;
      this.chestplateSlot = this.getFiles().getConfig().get().getInt("Options.Slots.Chestplate", 19);
      this.chestplateSlot = this.chestplateSlot < this.profileRows * 9 ? this.chestplateSlot : -1;
      this.leggingsSlot = this.getFiles().getConfig().get().getInt("Options.Slots.Leggings", 28);
      this.leggingsSlot = this.leggingsSlot < this.profileRows * 9 ? this.leggingsSlot : -1;
      this.bootsSlot = this.getFiles().getConfig().get().getInt("Options.Slots.Boots", 37);
      this.bootsSlot = this.bootsSlot < this.profileRows * 9 ? this.bootsSlot : -1;
      this.mainHandSlot = this.getFiles().getConfig().get().getInt("Options.Slots.Main-hand", 20);
      this.mainHandSlot = this.mainHandSlot < this.profileRows * 9 ? this.mainHandSlot : -1;
      this.offHandSlot = this.getFiles().getConfig().get().getInt("Options.Slots.Off-hand", 29);
      this.offHandSlot = this.offHandSlot < this.profileRows * 9 ? this.offHandSlot : -1;
      this.keepAliveInterval = Math.max(5, this.getFiles().getConfig().get().getInt("Database.Keep-alive-interval", 30)) * 1200;
      this.closeItem = this.createSimpleItem(new ItemStack(Material.ARROW), this.getFiles().getConfig().get().getConfigurationSection("Items.Close"));
      this.noHelmetItem = this.createSimpleItem(this.emptyStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-helmet"));
      this.noHelmetHiddenItem = this.createSimpleItem(this.hiddenStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-helmet"), true);
      this.noHelmetItemPermission = this.getFiles().getConfig().get().getString("Items.No-helmet.Permission", "none");
      if (this.noHelmetItemPermission.equalsIgnoreCase("none")) {
         this.noHelmetItemPermission = null;
      }

      this.noChestplateItem = this.createSimpleItem(this.emptyStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-chestplate"));
      this.noChestplateHiddenItem = this.createSimpleItem(this.hiddenStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-chestplate"), true);
      this.noChestplateItemPermission = this.getFiles().getConfig().get().getString("Items.No-chestplate.Permission", "none");
      if (this.noChestplateItemPermission.equalsIgnoreCase("none")) {
         this.noChestplateItemPermission = null;
      }

      this.noLeggingsItem = this.createSimpleItem(this.emptyStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-leggings"));
      this.noLeggingsHiddenItem = this.createSimpleItem(this.hiddenStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-leggings"), true);
      this.noLeggingsItemPermission = this.getFiles().getConfig().get().getString("Items.No-leggings.Permission", "none");
      if (this.noLeggingsItemPermission.equalsIgnoreCase("none")) {
         this.noLeggingsItemPermission = null;
      }

      this.noBootsItem = this.createSimpleItem(this.emptyStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-boots"));
      this.noBootsHiddenItem = this.createSimpleItem(this.hiddenStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-boots"), true);
      this.noBootsItemPermission = this.getFiles().getConfig().get().getString("Items.No-boots.Permission", "none");
      if (this.noBootsItemPermission.equalsIgnoreCase("none")) {
         this.noBootsItemPermission = null;
      }

      this.noMainHandItem = this.createSimpleItem(this.emptyStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-main-hand"));
      this.noMainHandHiddenItem = this.createSimpleItem(this.hiddenStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-main-hand"), true);
      this.noMainHandItemPermission = this.getFiles().getConfig().get().getString("Items.No-main-hand.Permission", "none");
      if (this.noMainHandItemPermission.equalsIgnoreCase("none")) {
         this.noMainHandItemPermission = null;
      }

      this.noOffHandItem = this.createSimpleItem(this.emptyStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-off-hand"));
      this.noOffHandHiddenItem = this.createSimpleItem(this.hiddenStainedPane, this.getFiles().getConfig().get().getConfigurationSection("Items.No-off-hand"), true);
      this.noOffHandItemPermission = this.getFiles().getConfig().get().getString("Items.No-off-hand.Permission", "none");
      if (this.noOffHandItemPermission.equalsIgnoreCase("none")) {
         this.noOffHandItemPermission = null;
      }

      this.openSound = LocalUtils.getSound(this.getFiles().getConfig().get().getString("Options.Sounds.Open", "CHEST_OPEN"));
      if (this.keepAliveTask != null) {
         this.keepAliveTask.cancel();
      }

      this.keepAliveTask = (new BukkitRunnable() {
         public void run() {
            if (BetterProfiles.this.debugMode) {
               LocalUtils.logp("[DATABASE] Keeping connection alive..");
            }

            try {
               BetterProfiles.this.database.getConnection().prepareStatement("SELECT 1").executeQuery();
            } catch (SQLException var2) {
               if (BetterProfiles.this.debugMode) {
                  LocalUtils.loge("[DATABASE] Issue keeping connection alive..");
               }

               var2.printStackTrace();
            }

         }
      }).runTaskTimer(this, (long)this.keepAliveInterval, (long)this.keepAliveInterval);
   }

   private ItemStack createSimpleItem(ItemStack itemStack, ConfigurationSection section, boolean hidden) {
      if (!hidden) {
         return this.createSimpleItem(itemStack, section);
      } else {
         ItemStack cloned = itemStack.clone();
         ItemMeta clonedMeta = cloned.getItemMeta();
         clonedMeta.setDisplayName(LocalUtils.colorize(section.getString("Displayname")));
         clonedMeta.setLore(LocalUtils.colorize(section.getStringList("Lore-hidden")));
         clonedMeta.addItemFlags(ItemFlag.values());
         int customModelData = this.pluginUtils.isCustomModelSupported() ? Math.max(0, section.getInt("Custom-model-data-hidden", 0)) : 0;
         if (customModelData != 0) {
            clonedMeta.setCustomModelData(customModelData);
         }

         cloned.setItemMeta(clonedMeta);
         return cloned;
      }
   }

   private ItemStack createSimpleItem(ItemStack itemStack, ConfigurationSection section) {
      ItemStack cloned = itemStack.clone();
      ItemMeta clonedMeta = cloned.getItemMeta();
      clonedMeta.setDisplayName(LocalUtils.colorize(section.getString("Displayname")));
      clonedMeta.setLore(LocalUtils.colorize(section.getStringList("Lore")));
      clonedMeta.addItemFlags(ItemFlag.values());
      int customModelData = this.pluginUtils.isCustomModelSupported() ? Math.max(0, section.getInt("Custom-model-data", 0)) : 0;
      if (customModelData != 0) {
         clonedMeta.setCustomModelData(customModelData);
      }

      cloned.setItemMeta(clonedMeta);
      return cloned;
   }

   public static BetterProfiles getInstance() {
      return instance;
   }

   private void updateConfiguration() {
      if (this.getFiles().getConfig().get().getBoolean("Updates.Auto-update-configuration", false)) {
         boolean updated = false;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Updates.Auto-update-configuration", true) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Fill-empty.Enabled", true) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Fill-empty.Custom-model-data", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-main-hand.Custom-model-data", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-off-hand.Custom-model-data", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-helmet.Custom-model-data", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-chestplate.Custom-model-data", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-leggings.Custom-model-data", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-boots.Custom-model-data", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.Close.Custom-model-data", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Sounds.Open", "CHEST_OPEN") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Sounds.Close", "CHEST_CLOSE") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Prevent-shield", true) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Prevent-interact", true) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Fill-empty.Color-frame", "BLACK") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Empty-slot.Color-frame", "WHITE") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Hidden-slot.Color-frame", "GRAY") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-main-hand.Custom-model-data-hidden", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-main-hand.Permission", "none") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-main-hand.Lore-hidden", Collections.singletonList("&cYou can't see this slot.")) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-off-hand.Custom-model-data-hidden", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-off-hand.Permission", "none") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-off-hand.Lore-hidden", Collections.singletonList("&cYou can't see this slot.")) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-helmet.Custom-model-data-hidden", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-helmet.Permission", "none") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-helmet.Lore-hidden", Collections.singletonList("&cYou can't see this slot.")) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-chestplate.Custom-model-data-hidden", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-chestplate.Permission", "none") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-chestplate.Lore-hidden", Collections.singletonList("&cYou can't see this slot.")) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-leggings.Custom-model-data-hidden", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-leggings.Permission", "none") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-leggings.Lore-hidden", Collections.singletonList("&cYou can't see this slot.")) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-boots.Custom-model-data-hidden", 0) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-boots.Permission", "none") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Items.No-boots.Lore-hidden", Collections.singletonList("&cYou can't see this slot.")) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Permissions.Own-profile", "none") || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Messages.Help.Admin", Arrays.asList("&6 /betterprofiles &f- &eCheck your profile", "&6 /betterprofiles view <player> &f- &eOpen player's profile", "&6 /betterprofiles toggle <viewer/profile>&f- &eToggle viewing/showing profiles", "&6 /betterprofiles reload &f- &eReload configuration")) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Messages.Help.User", Arrays.asList("&6 /betterprofiles &f- &eCheck your profile", "&6 /betterprofiles view <player> &f- &eCheck player's profile", "&6 /betterprofiles toggle <viewer/profile>&f- &eToggle viewing/showing profiles")) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Database.Keep-alive-interval", 30) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Debug", false) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Profiles.Rows", 6) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Main.Aliases", Arrays.asList("betterprofile", "bprofiles", "bprofile")) || updated;
         updated = this.addConfig(this.getFiles().getConfig().get(), "Options.Profile.Aliases", Arrays.asList("perfil")) || updated;
         if (updated) {
            this.getFiles().getConfig().save();
         }

         LocalUtils.log(AlonsoUtils.second + "[Auto-update] §7Configuration is up-to-date!");
      } else {
         LocalUtils.logp("Configuration auto-update is not enabled!");
      }

   }

   private boolean addConfig(FileConfiguration configuration, String path, Object object) {
      if (!configuration.contains(path)) {
         configuration.set(path, object);
         LocalUtils.log(AlonsoUtils.second + "[Auto-update]§7 Adding default configuration in path: " + AlonsoUtils.first + path);
         return true;
      } else {
         return false;
      }
   }

   public HashMap<UUID, HashMap<UUID, Inventory>> getOpenedProfilesMap() {
      return this.openedProfilesMap;
   }

   public HashMap<Integer, LinkedList<ItemData>> getItemsMap() {
      return this.itemsMap;
   }

   public HashMap<UUID, PlayerData> getDataMap() {
      return this.dataMap;
   }

   public AlonsoUtils.PluginUtils getPluginUtils() {
      return this.pluginUtils;
   }

   public FileManager getFiles() {
      return this.fileManager;
   }

   public JavaPlugin getPlugin() {
      return this;
   }

   public Inventory openProfile(Player viewer, Player target) {
      if (!this.openedProfilesMap.containsKey(target.getUniqueId())) {
         this.openedProfilesMap.put(target.getUniqueId(), new HashMap());
      }

      HashMap<UUID, Inventory> profileMap = (HashMap)this.openedProfilesMap.get(target.getUniqueId());
      String title = this.messages.profileTitle.replace("{PLAYER}", target.getName());
      Inventory profileInventory = Bukkit.createInventory(new ProfileHolder(0, target.getUniqueId(), target.getName()), this.profileRows * 9, this.pluginUtils.getServerVersion().isOlderEqualThanV1_12() ? LocalUtils.limitString(title, 32) : title);
      if (this.helmetSlot != -1) {
         this.addItem(viewer, profileInventory, this.helmetSlot, target.getInventory().getHelmet(), this.noHelmetItem, this.noHelmetHiddenItem, this.noHelmetItemPermission);
      }

      if (this.chestplateSlot != -1) {
         this.addItem(viewer, profileInventory, this.chestplateSlot, target.getInventory().getChestplate(), this.noChestplateItem, this.noChestplateHiddenItem, this.noChestplateItemPermission);
      }

      if (this.leggingsSlot != -1) {
         this.addItem(viewer, profileInventory, this.leggingsSlot, target.getInventory().getLeggings(), this.noLeggingsItem, this.noLeggingsHiddenItem, this.noLeggingsItemPermission);
      }

      if (this.bootsSlot != -1) {
         this.addItem(viewer, profileInventory, this.bootsSlot, target.getInventory().getBoots(), this.noBootsItem, this.noBootsHiddenItem, this.noBootsItemPermission);
      }

      if (this.getPluginUtils().isV1_8()) {
         if (this.mainHandSlot != -1) {
            this.addItem(viewer, profileInventory, this.mainHandSlot, target.getInventory().getItemInHand(), this.noMainHandItem, this.noMainHandHiddenItem, this.noMainHandItemPermission);
         }
      } else {
         if (this.mainHandSlot != -1) {
            this.addItem(viewer, profileInventory, this.mainHandSlot, target.getInventory().getItemInMainHand(), this.noMainHandItem, this.noMainHandHiddenItem, this.noMainHandItemPermission);
         }

         if (this.offHandSlot != -1) {
            this.addItem(viewer, profileInventory, this.offHandSlot, target.getInventory().getItemInOffHand(), this.noOffHandItem, this.noOffHandHiddenItem, this.noOffHandItemPermission);
         }
      }

      if (this.closeSlot != -1) {
         profileInventory.setItem(this.closeSlot, this.closeItem);
      }

      Iterator var6 = this.itemsMap.values().iterator();

      while(true) {
         label124:
         while(var6.hasNext()) {
            LinkedList<ItemData> itemsList = (LinkedList)var6.next();
            Iterator var8 = itemsList.iterator();

            while(true) {
               while(true) {
                  ItemData itemData;
                  do {
                     if (!var8.hasNext()) {
                        continue label124;
                     }

                     itemData = (ItemData)var8.next();
                  } while(itemData.hasPermission() && !viewer.hasPermission(itemData.getPermission()));

                  if (itemData.hasDynamicSlot()) {
                     ItemStack itemInDynamicSlot = target.getInventory().getItem(itemData.getDynamicSlot());
                     profileInventory.setItem(itemData.getSlot(), itemInDynamicSlot != null && itemInDynamicSlot.getType() != Material.AIR ? itemInDynamicSlot : itemData.buildItem(viewer, target));
                  } else if (itemData.hasPlaceholderCondition()) {
                     if (itemData.checkPlaceholder(target)) {
                        profileInventory.setItem(itemData.getSlot(), itemData.buildItem(viewer, target));
                     }
                  } else if (itemData.isPrivateItem()) {
                     if (viewer.getUniqueId().equals(target.getUniqueId())) {
                        profileInventory.setItem(itemData.getSlot(), itemData.buildItem(viewer, viewer));
                        continue label124;
                     }
                  } else {
                     if (!itemData.isViewerItem()) {
                        profileInventory.setItem(itemData.getSlot(), itemData.buildItem(viewer, target));
                        continue label124;
                     }

                     if (!viewer.getUniqueId().equals(target.getUniqueId())) {
                        profileInventory.setItem(itemData.getSlot(), itemData.buildItem(viewer, target));
                        continue label124;
                     }
                  }
               }
            }
         }

         profileMap.put(viewer.getUniqueId(), profileInventory);
         ProfileOpenEvent profileOpenEvent = new ProfileOpenEvent(viewer, target, profileInventory, this.fillEmptySlots);
         this.getServer().getPluginManager().callEvent(profileOpenEvent);
         if (profileOpenEvent.isFillEmptySlots()) {
            for(int i = 0; i < profileInventory.getSize(); ++i) {
               ItemStack iStack = profileInventory.getItem(i);
               if (iStack == null || iStack.getType() == Material.AIR) {
                  profileInventory.setItem(i, this.frameStainedPane);
               }
            }
         }

         viewer.openInventory(profileInventory);
         if (this.openSound != null) {
            viewer.playSound(viewer.getLocation(), this.openSound, 1.0F, 1.0F);
         }

         return profileInventory;
      }
   }

   private void addItem(Player viewer, Inventory profileInventory, int slot, ItemStack checkItem, ItemStack defaultItem, ItemStack hiddenItem, String permission) {
      profileInventory.setItem(slot, permission != null && !viewer.hasPermission(permission) ? hiddenItem : (checkItem != null && checkItem.getType() != Material.AIR ? checkItem : defaultItem));
   }
}
