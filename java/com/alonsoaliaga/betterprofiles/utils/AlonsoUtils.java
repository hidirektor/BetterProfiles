package com.alonsoaliaga.betterprofiles.utils;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import com.alonsoaliaga.betterprofiles.others.FileManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AlonsoUtils {
   public static final String PLUGIN = "BetterProfiles";
   public static final boolean DEBUG = true;
   public static String first = "&e";
   public static String second = "&6";
   public static final String PREFIX;
   public static final String PREFIXC = "[BetterProfiles] ";
   private static List<String> spigotList;
   private static List<String> paperList;
   public static AlonsoUtils.ServerType serverType;
   public static AlonsoUtils.ServerVersion serverVersion;

   public static void sendEnableText(JavaPlugin plugin) {
      LocalUtils.log(first + "  ___      _   _           ___          __ _ _         ");
      LocalUtils.log(first + " | _ ) ___| |_| |_ ___ _ _| _ \\_ _ ___ / _(_) |___ ___ ");
      LocalUtils.log(first + " | _ \\/ -_)  _|  _/ -_) '_|  _/ '_/ _ \\  _| | / -_|_-< ");
      LocalUtils.log(second + " |___/\\___|\\__|\\__\\___|_| |_| |_| \\___/_| |_|_\\___/__/ ");
      LocalUtils.log(second + "");
      LocalUtils.log(first + "   Running plugin " + second + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion());
      LocalUtils.log(first + "   Server running " + second + Bukkit.getServer().getName() + first + " version " + second + Bukkit.getVersion());
      LocalUtils.log(first + "   (Implementing API version " + second + Bukkit.getVersion() + first + ")");
      LocalUtils.log(first + "   (Developed by " + second + " AlonsoAliaga" + first + " - Thanks for using my plugin " + second + "❤" + first + ")");
      LocalUtils.log(first + "   (If you loved the plugin consider leaving a review and mentioning your server IP in it!");
      LocalUtils.log("");
   }

   public static boolean isSupported() {
      if (Bukkit.getVersion().toLowerCase().contains("paper")) {
         serverType = AlonsoUtils.ServerType.PAPER;
         LocalUtils.log("");
         LocalUtils.log("&c===================================================================================");
         LocalUtils.log("&c[BetterProfiles] You are using a Spigot fork (PAPER). Plugin might have errors");
         LocalUtils.log("&c[BetterProfiles] and not work properly. Consider using SPIGOT!");
         LocalUtils.log("&c===================================================================================");
         LocalUtils.log("");
      } else if (Bukkit.getVersion().toLowerCase().contains("spigot")) {
         serverType = AlonsoUtils.ServerType.SPIGOT;
      } else if (!Bukkit.getName().toLowerCase().contains("paper") && !paperList.contains(Bukkit.getName().toLowerCase())) {
         if (!Bukkit.getName().toLowerCase().contains("spigot") && !spigotList.contains(Bukkit.getName().toLowerCase())) {
            serverType = AlonsoUtils.ServerType.CRAFTBUKKIT;
            LocalUtils.log("");
            LocalUtils.log("&c=====================================================================================");
            LocalUtils.log("&c[BetterProfiles] You are using an unknown for (" + Bukkit.getName() + "). Plugin might have");
            LocalUtils.log("&c[BetterProfiles] errors and not work properly. Consider using SPIGOT!");
            LocalUtils.log("&c=====================================================================================");
            LocalUtils.log("");
         } else {
            serverType = AlonsoUtils.ServerType.SPIGOT;
            LocalUtils.log("");
            LocalUtils.log("&c===================================================================================");
            LocalUtils.log("&c[BetterProfiles] You are using a Spigot fork (" + Bukkit.getVersion() + "). Plugin might have errors");
            LocalUtils.log("&c[BetterProfiles] and not work properly. Consider using SPIGOT!");
            LocalUtils.log("&c===================================================================================");
            LocalUtils.log("");
         }
      } else {
         serverType = AlonsoUtils.ServerType.SPIGOT;
         LocalUtils.log("");
         LocalUtils.log("&c===================================================================================");
         LocalUtils.log("&c[BetterProfiles] You are using a Paper fork (" + Bukkit.getVersion() + "). Plugin might have errors");
         LocalUtils.log("&c[BetterProfiles] and not work properly. Consider using SPIGOT!");
         LocalUtils.log("&c===================================================================================");
         LocalUtils.log("");
      }

      return true;
   }

   public static AlonsoUtils.ServerVersion getServerVersion() {
      String version = Bukkit.getBukkitVersion().trim();
      if (version.contains("1.7")) {
         return AlonsoUtils.ServerVersion.v1_7;
      } else if (version.contains("1.8")) {
         return AlonsoUtils.ServerVersion.v1_8;
      } else if (version.contains("1.9")) {
         return AlonsoUtils.ServerVersion.v1_9;
      } else if (version.contains("1.10")) {
         return AlonsoUtils.ServerVersion.v1_10;
      } else if (version.contains("1.11")) {
         return AlonsoUtils.ServerVersion.v1_11;
      } else if (version.contains("1.12")) {
         return AlonsoUtils.ServerVersion.v1_12;
      } else if (version.contains("1.13")) {
         return AlonsoUtils.ServerVersion.v1_13;
      } else if (version.contains("1.14")) {
         return AlonsoUtils.ServerVersion.v1_14;
      } else if (version.contains("1.15")) {
         return AlonsoUtils.ServerVersion.v1_15;
      } else if (version.contains("1.16")) {
         return AlonsoUtils.ServerVersion.v1_16;
      } else {
         return version.contains("1.17") ? AlonsoUtils.ServerVersion.v1_17 : AlonsoUtils.ServerVersion.v1_18;
      }
   }

   public static AlonsoUtils.ServerType getServerType() {
      if (Bukkit.getVersion().toLowerCase().contains("paper")) {
         return AlonsoUtils.ServerType.PAPER;
      } else if (Bukkit.getVersion().toLowerCase().contains("spigot")) {
         return AlonsoUtils.ServerType.SPIGOT;
      } else if (Bukkit.getName().toLowerCase().contains("paper")) {
         return AlonsoUtils.ServerType.PAPER;
      } else {
         return Bukkit.getName().toLowerCase().contains("spigot") ? AlonsoUtils.ServerType.SPIGOT : AlonsoUtils.ServerType.CRAFTBUKKIT;
      }
   }

   public static void sendDisableText() {
      LocalUtils.log(" ");
      LocalUtils.log(String.format("&c%sPlugin has been disabled!", "[BetterProfiles] "));
      LocalUtils.log(String.format("&c%sThank you for using my plugin!", "[BetterProfiles] "));
      LocalUtils.log(" ");
   }

   static {
      PREFIX = second + "[" + "BetterProfiles" + "] &7";
      spigotList = Arrays.asList();
      paperList = Arrays.asList("paperspigot", "tacospigot");
      serverType = getServerType();
      serverVersion = getServerVersion();
   }

   public static class Updater {
      private String resourceID;
      private JavaPlugin plugin;
      private int updateCheckCounter = 0;
      private AlonsoUtils.Updater.UpdateFound updateFound = null;
      private String notificationMessage = null;

      public Updater(JavaPlugin plugin, @Nullable String resourceID, boolean notify, @Nullable String notifyPermission, String notificationMessage) {
         this.plugin = plugin;
         this.resourceID = resourceID;
         if (plugin.isEnabled() && resourceID != null && !resourceID.isEmpty()) {
            this.startChecking();
            if (notify) {
               new AlonsoUtils.Updater.UpdateJoinListener(plugin, notifyPermission);
            }

            this.notificationMessage = LocalUtils.colorize(notificationMessage == null ? String.format("%s&eA new update has been found! Download it here &c{LINK}", AlonsoUtils.PREFIX) : notificationMessage);
         }

      }

      private void checkUpdate() {
         if (this.resourceID != null && !this.resourceID.isEmpty()) {
            HttpsURLConnection connection = null;

            try {
               connection = (HttpsURLConnection)(new URL(String.format("https://api.spigotmc.org/legacy/update.php?resource=%s", this.resourceID))).openConnection();
               int timed_out = 1250;
               connection.setConnectTimeout(timed_out);
               connection.setReadTimeout(timed_out);
               String localPluginVersion = this.plugin.getDescription().getVersion();
               String spigotPluginVersion = (new BufferedReader(new InputStreamReader(connection.getInputStream()))).readLine();
               if (!spigotPluginVersion.equals(localPluginVersion)) {
                  this.updateFound = new AlonsoUtils.Updater.UpdateFound(spigotPluginVersion, this.resourceID);
                  LocalUtils.log(LocalUtils.colorize(String.format("&6%s&fChecking for updates...", "[BetterProfiles] ")));
                  LocalUtils.log(LocalUtils.colorize(String.format("&6%s&aNew version available: %s", "[BetterProfiles] ", spigotPluginVersion)));
                  LocalUtils.log(LocalUtils.colorize(String.format("&6%s&aPlease download the latest version to get support!", "[BetterProfiles] ")));
                  LocalUtils.log(LocalUtils.colorize(String.format("&6%s&eDownload: https://www.spigotmc.org/resources/%s/", "[BetterProfiles] ", this.resourceID)));
               } else if (this.updateCheckCounter % 3 == 0) {
                  LocalUtils.log(LocalUtils.colorize(String.format("&6%s&fChecking for updates...", "[BetterProfiles] ")));
                  LocalUtils.log(LocalUtils.colorize(String.format("&6%s&ePlugin up-to-date! You have the latest version!", "[BetterProfiles] ")));
               }

               connection.disconnect();
               ++this.updateCheckCounter;
            } catch (Exception var5) {
               LocalUtils.log(String.format("&c%sFailed to check for an update on SpigotMC.org!", "[BetterProfiles] "));
               var5.printStackTrace();
               ++this.updateCheckCounter;
               if (connection != null) {
                  connection.disconnect();
               }
            }

         }
      }

      private void startChecking() {
         (new BukkitRunnable() {
            public void run() {
               if (!Updater.this.plugin.isEnabled()) {
                  this.cancel();
               } else {
                  Updater.this.checkUpdate();
               }
            }
         }).runTaskTimer(this.plugin, 0L, 86400000L);
      }

      public class UpdateJoinListener implements Listener {
         private JavaPlugin plugin;
         private String notifyPermission;

         public UpdateJoinListener(JavaPlugin plugin, @Nullable String notifyPermission) {
            this.plugin = plugin;
            this.notifyPermission = notifyPermission != null && !notifyPermission.equalsIgnoreCase("none") ? notifyPermission : null;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
         }

         @EventHandler
         public void onJoinUpdate(PlayerJoinEvent e) {
            if (Updater.this.updateFound != null && (e.getPlayer().isOp() || this.notifyPermission != null && e.getPlayer().hasPermission(this.notifyPermission))) {
               e.getPlayer().sendMessage(Updater.this.notificationMessage.replace("{CURRENT}", this.plugin.getDescription().getVersion()).replace("{NEW}", Updater.this.updateFound.getNewVersion()).replace("{LINK}", Updater.this.updateFound.getDownloadLink()));
            }

         }
      }

      public class UpdateFound {
         private String newVersion;
         private String resourceID;

         public UpdateFound(String newVersion, String resourceID) {
            this.newVersion = newVersion;
            this.resourceID = resourceID;
         }

         public String getNewVersion() {
            return this.newVersion;
         }

         public String getDownloadLink() {
            return String.format("https://www.spigotmc.org/resources/%s/history", this.resourceID);
         }
      }
   }

   public static class PluginUtils {
      private JavaPlugin plugin;
      private AlonsoUtils.ActionBarType actionBarType;
      private AlonsoUtils.ServerType serverType;
      private AlonsoUtils.ServerVersion serverVersion;
      private boolean protocolLibSupport;
      private boolean customModelSupport;
      private int actionBarProtocolMethod;
      private boolean betterHeadsSupported;
      private static boolean betterHeadsSupport = Bukkit.getServer().getPluginManager().getPlugin("BetterHeads") != null;
      private boolean nbtApiSupported;
      private static boolean nbtApiSupport = Bukkit.getServer().getPluginManager().getPlugin("NBTAPI") != null;
      private boolean placeholderApiSupported;
      private static boolean placeholderApiSupport = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

      public PluginUtils(JavaPlugin plugin) {
         this.plugin = plugin;
         this.load();
      }

      private void load() {
         this.actionBarType = AlonsoUtils.ActionBarType.UNSUPPORTED;
         this.serverType = AlonsoUtils.ServerType.SPIGOT;
         this.serverVersion = AlonsoUtils.ServerVersion.v1_8;
         this.protocolLibSupport = false;
         this.customModelSupport = false;
         this.betterHeadsSupported = Bukkit.getServer().getPluginManager().getPlugin("BetterHeads") != null;
         this.nbtApiSupported = Bukkit.getServer().getPluginManager().getPlugin("NBTAPI") != null;
         this.placeholderApiSupported = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
         this.actionBarProtocolMethod = -1;

         try {
            Class.forName("org.spigotmc.SpigotConfig");
            Class.forName("net.md_5.bungee.api.chat.BaseComponent");
            Class.forName("net.md_5.bungee.api.ChatMessageType");
            LocalUtils.logp("BungeeCord action bar available. Hooking..");
            this.actionBarType = AlonsoUtils.ActionBarType.BUNGEE;
         } catch (ClassNotFoundException | NoClassDefFoundError var2) {
         }

         if (this.plugin.getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            LocalUtils.logp("ProtocolLib found! Hooking..");
            this.protocolLibSupport = true;
            if (this.actionBarType != AlonsoUtils.ActionBarType.BUNGEE) {
               this.actionBarProtocolMethod = ProtocolLibUtils.getProtocolActionBarMethod();
               if (this.actionBarProtocolMethod != -1) {
                  this.actionBarType = AlonsoUtils.ActionBarType.PROTOCOL;
               }
            }
         } else {
            LocalUtils.logp("ProtocolLib not found! Skipping..");
         }

         String version = Bukkit.getBukkitVersion().trim();
         if (version.contains("1.7")) {
            if (this.actionBarType == AlonsoUtils.ActionBarType.BUNGEE) {
               LocalUtils.logp("BungeeCord action bar not available in 1.7.x! Skipping..");
            }

            this.serverVersion = AlonsoUtils.ServerVersion.v1_7;
         } else if (version.contains("1.8")) {
            if (this.actionBarType == AlonsoUtils.ActionBarType.BUNGEE) {
               LocalUtils.logp("BungeeCord action bar not available in 1.8.x! Skipping..");
            }

            this.serverVersion = AlonsoUtils.ServerVersion.v1_8;
         } else if (version.contains("1.9")) {
            this.serverVersion = AlonsoUtils.ServerVersion.v1_9;
         } else if (version.contains("1.10")) {
            this.serverVersion = AlonsoUtils.ServerVersion.v1_10;
         } else if (version.contains("1.11")) {
            this.serverVersion = AlonsoUtils.ServerVersion.v1_11;
         } else if (version.contains("1.12")) {
            this.serverVersion = AlonsoUtils.ServerVersion.v1_12;
         } else if (version.contains("1.13")) {
            this.serverVersion = AlonsoUtils.ServerVersion.v1_13;
         } else if (version.contains("1.14")) {
            this.serverVersion = AlonsoUtils.ServerVersion.v1_14;
         } else if (version.contains("1.15")) {
            this.serverVersion = AlonsoUtils.ServerVersion.v1_15;
         } else if (version.contains("1.16")) {
            this.serverVersion = AlonsoUtils.ServerVersion.v1_16;
         } else if (version.contains("1.17")) {
            this.serverVersion = AlonsoUtils.ServerVersion.v1_17;
         } else {
            this.serverVersion = AlonsoUtils.ServerVersion.v1_18;
         }

         this.serverType = AlonsoUtils.getServerType();
         this.customModelSupport = this.serverVersion.getWeight() >= AlonsoUtils.ServerVersion.v1_14.getWeight();
      }

      public AlonsoUtils.ServerType getServerType() {
         return this.serverType;
      }

      public AlonsoUtils.ServerVersion getServerVersion() {
         return this.serverVersion;
      }

      public AlonsoUtils.ActionBarType getActionBarType() {
         return this.actionBarType;
      }

      public boolean isProtocolLibSupported() {
         return this.protocolLibSupport;
      }

      public boolean isCustomModelSupported() {
         return this.customModelSupport;
      }

      public boolean isV1_8() {
         return this.serverVersion == AlonsoUtils.ServerVersion.v1_8;
      }

      public int getActionBarProtocolMethod() {
         return this.actionBarProtocolMethod;
      }

      public boolean isBetterHeadsSupported() {
         return this.betterHeadsSupported;
      }

      public static boolean hasBetterHeadsSupport() {
         return betterHeadsSupport;
      }

      public boolean isNbtApiSupported() {
         return this.nbtApiSupported;
      }

      public static boolean hasNbtApiSupport() {
         return nbtApiSupport;
      }

      public boolean isPlaceholderApiSupported() {
         return this.placeholderApiSupported;
      }

      public static boolean hasPlaceholderApiSupport() {
         return placeholderApiSupport;
      }
   }

   public interface AlonsoPlugin {
      AlonsoUtils.PluginUtils getPluginUtils();

      FileManager getFiles();

      JavaPlugin getPlugin();

      BetterProfiles getMain();
   }

   public static enum ActionBarType {
      PROTOCOL,
      BUNGEE,
      UNSUPPORTED;
   }

   public static enum ServerVersion {
      v1_7(0),
      v1_8(1),
      v1_9(2),
      v1_10(3),
      v1_11(4),
      v1_12(5),
      v1_13(6),
      v1_14(7),
      v1_15(8),
      v1_16(9),
      v1_17(10),
      v1_18(11);

      int weight;

      private ServerVersion(int weight) {
         this.weight = weight;
      }

      public int getWeight() {
         return this.weight;
      }

      public boolean isOlderEqualThan(AlonsoUtils.ServerVersion version) {
         return this.getWeight() <= version.getWeight();
      }

      public boolean isOlderThan(AlonsoUtils.ServerVersion version) {
         return this.getWeight() < version.getWeight();
      }

      public boolean isNewerEqualThan(AlonsoUtils.ServerVersion version) {
         return this.getWeight() >= version.getWeight();
      }

      public boolean isNewerThan(AlonsoUtils.ServerVersion version) {
         return this.getWeight() > version.getWeight();
      }

      public boolean isEqualThan(AlonsoUtils.ServerVersion version) {
         return this.getWeight() == version.getWeight();
      }

      public boolean isOlderEqualThanV1_12() {
         return this.getWeight() <= v1_12.getWeight();
      }
   }

   public static enum ServerType {
      CRAFTBUKKIT,
      SPIGOT,
      PAPER;
   }
}
