package com.alonsoaliaga.betterprofiles.utils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ProtocolLibUtils {
   public static int getProtocolActionBarMethod() {
      ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
      PacketContainer chatPacket = protocolManager.createPacket(Server.CHAT);
      chatPacket.getChatComponents().write(0, WrappedChatComponent.fromText("Plugin made by AlonsoAliaga"));

      try {
         chatPacket.getChatTypes().write(0, ChatType.GAME_INFO);
         LocalUtils.logp("ProtocolLib available for action bars. Method: 'GAME_INFO'. Hooking..");
         return 1;
      } catch (FieldAccessException var5) {
         try {
            chatPacket.getBytes().write(0, (byte)2);
            LocalUtils.logp("ProtocolLib available for action bars. Method: '(byte)2' Hooking..");
            return 0;
         } catch (FieldAccessException var4) {
            LocalUtils.logp("ProtocolLib not available for action bars. Skipping..");
            return -1;
         }
      }
   }

   public static void playExplosion(Location location, int power, Collection<Player> players) {
      ProtocolManager pm = ProtocolLibrary.getProtocolManager();
      PacketContainer packet = pm.createPacket(Server.EXPLOSION);
      packet.getModifier().writeDefaults();
      packet.getDoubles().write(0, location.getX()).write(1, location.getY()).write(2, location.getZ());
      packet.getFloat().write(0, (float)power);
      players.forEach((online) -> {
         try {
            pm.sendServerPacket(online, packet);
         } catch (InvocationTargetException var4) {
            var4.printStackTrace();
         }

      });
   }

   public static void playExplosion(Location location, int power) {
      playExplosion(location, power, location.getWorld().getPlayers());
   }

   public static void playExplosion(Location location) {
      playExplosion(location, 4, location.getWorld().getPlayers());
   }

   public static void sendActionBar(int method, Player player, String text) {
      ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
      PacketContainer chatPacket = protocolManager.createPacket(Server.CHAT);
      chatPacket.getChatComponents().write(0, WrappedChatComponent.fromText(text));

      try {
         if (method == 0) {
            chatPacket.getBytes().write(0, (byte)2);
         } else {
            chatPacket.getChatTypes().write(0, ChatType.GAME_INFO);
         }

         protocolManager.sendServerPacket(player, chatPacket);
      } catch (Exception var6) {
         LocalUtils.logd("&c[ProtocolLib] Error sending Action Bar to player '" + player.getName() + "'.");
      }

   }

   public static void sendActionBar(int method, Collection<Player> players, String text) {
      ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
      PacketContainer chatPacket = protocolManager.createPacket(Server.CHAT);
      chatPacket.getChatComponents().write(0, WrappedChatComponent.fromText(text));

      try {
         if (method == 0) {
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
         LocalUtils.logd("&c[ProtocolLib] Error sending Action Bar to players.");
      }

   }
}
