package com.alonsoaliaga.betterprofiles.utils;

import com.alonsoaliaga.betterprofiles.others.Materials;
import com.alonsoaliaga.betterprofiles.others.Sounds;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;
import de.tr7zw.nbtapi.NBTCompound;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocalUtils {
   public static long DAY_START = 22750L;
   public static long DAY_END = 12750L;
   public static boolean hexColorSupport;

   public static String translateAlternateHexColorCodes(Character c, String string) {
      string = ChatColor.translateAlternateColorCodes(c, string);
      if (string.length() >= 7 && string.contains("#")) {
         String match;
         for(Matcher matcher = Pattern.compile("#[A-Fa-f0-9]{6}").matcher(string); matcher.find(); string = string.replace(match, net.md_5.bungee.api.ChatColor.of(match).toString())) {
            match = matcher.group(0);
         }

         return string;
      } else {
         return string;
      }
   }

   public static String colorize(String string) {
      return hexColorSupport ? translateAlternateHexColorCodes('&', string) : ChatColor.translateAlternateColorCodes('&', string);
   }

   public static List<String> colorize(List<String> strings) {
      return (List)strings.stream().map(LocalUtils::colorize).collect(Collectors.toList());
   }

   public static void logp(String string) {
      Bukkit.getConsoleSender().sendMessage(colorize(AlonsoUtils.second + "[" + "BetterProfiles" + "] &7" + string));
   }

   public static void loge(String string) {
      Bukkit.getConsoleSender().sendMessage(colorize("&c[BetterProfiles] " + string));
   }

   public static void logd(String string) {
      Bukkit.getConsoleSender().sendMessage(colorize(AlonsoUtils.second + "[" + "BetterProfiles" + "]" + AlonsoUtils.first + "&l[Debug] &7" + string));
   }

   public static void log(String string) {
      Bukkit.getConsoleSender().sendMessage(colorize(string));
   }

   public static Material findMaterial(String... names) {
      String[] var1 = names;
      int var2 = names.length;
      int var3 = 0;

      while(var3 < var2) {
         String name = var1[var3];

         try {
            return Material.valueOf(name.toUpperCase());
         } catch (IllegalArgumentException var6) {
            ++var3;
         }
      }

      return Material.COBBLESTONE;
   }

   public static Sound findSound(String... names) {
      String[] var1 = names;
      int var2 = names.length;
      int var3 = 0;

      while(var3 < var2) {
         String name = var1[var3];

         try {
            return Sound.valueOf(name.toUpperCase());
         } catch (IllegalArgumentException var7) {
            ++var3;
         }
      }

      try {
         return Sound.valueOf("CLICK");
      } catch (IllegalArgumentException var6) {
         return Sound.valueOf("UI_BUTTON_CLICK");
      }
   }

   public static Sound getSound(String name) {
      try {
         return Sounds.valueOf(name.toUpperCase()).getSound();
      } catch (Exception var4) {
         try {
            return Sound.valueOf(name.toUpperCase());
         } catch (IllegalArgumentException var3) {
            return null;
         }
      }
   }

   public static Sound getSoundException(String name) {
      try {
         return Sounds.valueOf(name.toUpperCase()).getSound();
      } catch (Exception var2) {
         return Sound.valueOf(name.toUpperCase());
      }
   }

   public static Material getMaterial(String name) {
      try {
         return Materials.valueOf(name).getMaterial();
      } catch (IllegalArgumentException var4) {
         try {
            return Material.valueOf(name);
         } catch (IllegalArgumentException var3) {
            return null;
         }
      }
   }

   public static Material getMaterialException(String name) {
      try {
         return Materials.valueOf(name).getMaterial();
      } catch (IllegalArgumentException var2) {
         return Material.valueOf(name);
      }
   }

   public static List<EntityType> getEntityTypes(String... names) {
      List<EntityType> entityTypes = new ArrayList();
      String[] var2 = names;
      int var3 = names.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String name = var2[var4];

         try {
            entityTypes.add(EntityType.valueOf(name.toUpperCase()));
         } catch (IllegalArgumentException var7) {
         }
      }

      return entityTypes;
   }

   public static List<EntityType> getEntityTypes(List<String> names) {
      List<EntityType> entityTypes = new ArrayList();
      Iterator var2 = names.iterator();

      while(var2.hasNext()) {
         String name = (String)var2.next();

         try {
            entityTypes.add(EntityType.valueOf(name.toUpperCase()));
         } catch (IllegalArgumentException var5) {
            logp("&cInvalid entity type: " + name.toUpperCase());
         }
      }

      return entityTypes;
   }

   public static String limitString(String text, int length) {
      return text.length() <= length ? text : text.substring(0, length);
   }

   public static void setUUID(NBTCompound nbtCompound, String name, UUID uuid) {
      if (AlonsoUtils.serverVersion.isOlderThan(AlonsoUtils.ServerVersion.v1_16)) {
         nbtCompound.setString(name, uuid.toString());
      } else {
         nbtCompound.setUUID(name, uuid);
      }

   }

   public static String pascalCase(String string) {
      return pascalCase(string, false);
   }

   public static String pascalCase(String string, boolean fancy) {
      if (string == null) {
         return "";
      } else if (string.length() <= 1) {
         return string.toUpperCase();
      } else {
         String toCase = fancy ? string.trim().replace("_", " ").replace("-", " ") : string.trim();
         List<String> list = new ArrayList();
         String[] var4 = toCase.split(" ");
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String s = var4[var6];
            if (!s.isEmpty()) {
               list.add(s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase());
            } else {
               list.add("");
            }
         }

         return String.join(" ", list);
      }
   }

   public static String firstCase(String string) {
      if (string == null) {
         return "";
      } else {
         String toCase = string.trim();
         return string.length() <= 1 ? toCase.toUpperCase() : toCase.substring(0, 1).toUpperCase() + toCase.substring(1).toLowerCase();
      }
   }

   public static String firstCase(String string, boolean fancy) {
      if (string == null) {
         return "";
      } else if (!fancy) {
         return firstCase(string);
      } else {
         String toCase = string.trim();
         return string.length() <= 1 ? toCase.toUpperCase() : (toCase.substring(0, 1).toUpperCase() + toCase.substring(1).toLowerCase()).replace("_", " ").replace("-", " ");
      }
   }

   public static boolean isDay(long dayTime) {
      if (dayTime >= 24000L) {
         dayTime %= 24000L;
      }

      return dayTime > DAY_START || dayTime < DAY_END;
   }

   public static boolean isNight(long dayTime) {
      return !isDay(dayTime);
   }

   @Nullable
   public static Location decodeLocation(String locationString) {
      if (locationString == null) {
         return null;
      } else {
         try {
            String[] parts = locationString.split("\\|");
            World world = Bukkit.getWorld(parts[0]);
            return world == null ? null : new Location(world, Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Float.parseFloat(parts[4]), Float.parseFloat(parts[5]));
         } catch (Exception var3) {
            return null;
         }
      }
   }

   public static void sendProtocolActionBar(AlonsoUtils.AlonsoPlugin plugin, Player player, String message) {
      ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
      PacketContainer chatPacket = protocolManager.createPacket(Server.CHAT);
      chatPacket.getChatComponents().write(0, WrappedChatComponent.fromText(message));

      try {
         if (plugin.getPluginUtils().getActionBarProtocolMethod() == 0) {
            chatPacket.getBytes().write(0, (byte)2);
         } else {
            chatPacket.getChatTypes().write(0, ChatType.GAME_INFO);
         }

         protocolManager.sendServerPacket(player, chatPacket);
      } catch (Exception var6) {
         log(String.format("&c%s[ProtocolLib] Error sending Action Bar to %s", "[BetterProfiles] ", player.getName()));
      }

   }

   public static void sendProtocolActionBar(AlonsoUtils.AlonsoPlugin plugin, Collection<Player> players, String message) {
      ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
      PacketContainer chatPacket = protocolManager.createPacket(Server.CHAT);
      chatPacket.getChatComponents().write(0, WrappedChatComponent.fromText(message));

      try {
         if (plugin.getPluginUtils().getActionBarProtocolMethod() == 0) {
            chatPacket.getBytes().write(0, (byte)2);
         } else {
            chatPacket.getChatTypes().write(0, ChatType.GAME_INFO);
         }

         Iterator var5 = players.iterator();

         while(var5.hasNext()) {
            Player player = (Player)var5.next();
            protocolManager.sendServerPacket(player, chatPacket);
         }
      } catch (Exception var7) {
         log(String.format("&c%s[ProtocolLib] Error sending Action Bar to players.", "[BetterProfiles] "));
      }

   }

   public static void sendBungeeActionBar(Player player, String message) {
      player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
   }

   public static void sendActionBar(AlonsoUtils.AlonsoPlugin plugin, Player player, String message) {
      if (plugin.getPluginUtils().getActionBarType() == AlonsoUtils.ActionBarType.BUNGEE) {
         sendBungeeActionBar(player, message);
      } else if (plugin.getPluginUtils().getActionBarType() == AlonsoUtils.ActionBarType.PROTOCOL) {
         sendProtocolActionBar(plugin, player, message);
      } else {
         player.sendMessage(message);
      }

   }

   public static void sendActionBar(AlonsoUtils.AlonsoPlugin plugin, Collection<Player> players, String message) {
      Iterator var3;
      Player player;
      if (plugin.getPluginUtils().getActionBarType() == AlonsoUtils.ActionBarType.BUNGEE) {
         var3 = players.iterator();

         while(var3.hasNext()) {
            player = (Player)var3.next();
            sendBungeeActionBar(player, message);
         }
      } else if (plugin.getPluginUtils().getActionBarType() == AlonsoUtils.ActionBarType.PROTOCOL) {
         sendProtocolActionBar(plugin, players, message);
      } else {
         var3 = players.iterator();

         while(var3.hasNext()) {
            player = (Player)var3.next();
            player.sendMessage(message);
         }
      }

   }

   @Nullable
   public static String encodeLocation(Location location) {
      if (location == null) {
         return null;
      } else {
         try {
            String locationString = "%s|%s|%s|%s|%s|%s";
            return String.format(locationString, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
         } catch (Exception var2) {
            return null;
         }
      }
   }

   public static void send(CommandSender sender, String message) {
      sender.sendMessage(colorize(message));
   }

   public static Collection<Material> getMaterials(String... names) {
      List<String> possibleNames = Arrays.asList(names);
      return (Collection)Stream.of(Material.values()).filter((material) -> {
         return possibleNames.contains(material.name());
      }).collect(Collectors.toList());
   }

   public static List<Location> getLocationsInLine(Location start, Location end) {
      Vector target = end.toVector();
      start.setDirection(target.subtract(start.toVector()));
      Vector toIncrease = start.getDirection();
      double distance = start.distance(end);
      List<Location> locations = new ArrayList();
      locations.add(start.clone());

      for(int i = 0; (double)i < distance; ++i) {
         Location newLoc = start.add(toIncrease);
         locations.add(newLoc.clone());
      }

      return locations;
   }

   public static List<Location> getLocationsInLine(Location start, Location end, double interval) {
      Vector target = end.toVector();
      start.setDirection(target.subtract(start.toVector()));
      Vector toIncrease = start.getDirection();
      double distance = start.distance(end);
      List<Location> locations = new ArrayList();
      locations.add(start.clone());

      for(double i = 0.0D; i < distance; i += interval) {
         Location newLoc = start.add(toIncrease);
         locations.add(newLoc.clone());
      }

      return locations;
   }

   public static String getDuration(long milliseconds, boolean fancy) {
      if (!fancy) {
         return getDuration(milliseconds);
      } else {
         long seconds = milliseconds / 1000L;
         long minutes = seconds / 60L;
         long hours = minutes / 60L;
         long days = hours / 24L;
         if (milliseconds > 86399999L) {
            return Math.abs(days) + "d " + Math.abs(hours % 24L) + "h " + Math.abs(minutes) % 60L + "m " + Math.abs(seconds) % 60L + "s";
         } else if (milliseconds > 1439999L) {
            return Math.abs(hours % 24L) + "h " + Math.abs(minutes) % 60L + "m " + Math.abs(seconds) % 60L + "s";
         } else if (milliseconds > 59999L) {
            return Math.abs(minutes) % 60L + "m " + Math.abs(seconds) % 60L + "s";
         } else {
            long amount = Math.abs(seconds) % 60L;
            return (amount <= 0L ? 1L : amount) + "s";
         }
      }
   }

   public static String getDuration(long milliseconds) {
      long seconds = milliseconds / 1000L;
      long minutes = seconds / 60L;
      long hours = minutes / 60L;
      long days = hours / 24L;
      if (milliseconds > 86399999L) {
         return Math.abs(days) + "d " + Math.abs(hours % 24L) + "h " + Math.abs(minutes) % 60L + "m " + Math.abs(seconds) % 60L + "s";
      } else {
         return milliseconds > 3599999L ? Math.abs(hours % 24L) + "h " + Math.abs(minutes) % 60L + "m " + Math.abs(seconds) % 60L + "s" : Math.abs(minutes) % 60L + "m " + Math.abs(seconds) % 60L + "s";
      }
   }

   public static String getWatchDuration(long milliseconds) {
      long seconds = milliseconds / 1000L;
      long minutes = seconds / 60L;
      long hours = minutes / 60L;
      long days = hours / 24L;
      if (milliseconds > 86399999L) {
         return Math.abs(days) + ":" + Math.abs(hours % 24L) + ":" + Math.abs(minutes) % 60L + ":" + Math.abs(seconds) % 60L;
      } else {
         return milliseconds > 1439999L ? Math.abs(hours % 24L) + ":" + Math.abs(minutes) % 60L + ":" + Math.abs(seconds) % 60L : Math.abs(minutes) % 60L + ":" + Math.abs(seconds) % 60L;
      }
   }

   public static String random(List<String> list) {
      return (String)list.get(ThreadLocalRandom.current().nextInt(list.size()));
   }

   public static <E> E randomNbt(List<E> list) {
      return list.get(ThreadLocalRandom.current().nextInt(list.size()));
   }

   public static boolean isInteger(String string) {
      try {
         Integer.parseInt(string);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static LivingEntity getLivingEntityAround(Player player, UUID uuid, int id, int radius) {
      Optional<Entity> entity = player.getNearbyEntities((double)radius, (double)radius, (double)radius).stream().filter((l) -> {
         return l instanceof LivingEntity && l.getEntityId() == id;
      }).findFirst();
      return (LivingEntity)entity.orElse(null);
   }

   public static TextComponent createClickable(@Nonnull String text, Action hoverAction, @Nullable String hover, net.md_5.bungee.api.chat.ClickEvent.Action clickAction, String click) {
      TextComponent message = new TextComponent(colorize(text));
      if (hover != null && hoverAction != null) {
         message.setHoverEvent(new HoverEvent(hoverAction, (new ComponentBuilder(colorize(hover))).create()));
      }

      if (click != null && clickAction != null) {
         message.setClickEvent(new ClickEvent(clickAction, click));
      }

      return message;
   }

   public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      if (AlonsoUtils.serverVersion.isOlderEqualThan(AlonsoUtils.ServerVersion.v1_12)) {
         player.sendTitle(colorize(title), colorize(subtitle));
      } else {
         player.sendTitle(colorize(title), colorize(subtitle), fadeIn, stay, fadeOut);
      }

   }

   public static void sendTitle(Player player, String title, String subtitle) {
      player.sendTitle(colorize(title), colorize(subtitle), 15, 30, 15);
   }

   static {
      try {
         net.md_5.bungee.api.ChatColor.of("#03fc30");
         hexColorSupport = true;
         logp("Hex colors are available!");
      } catch (NoSuchMethodError var1) {
         hexColorSupport = false;
         logp("Hex colors are not available!");
      }

   }
}
