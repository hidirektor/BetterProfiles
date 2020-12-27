package com.alonsoaliaga.betterprofiles.utils;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import java.util.List;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
   private static ItemStack headItemSample;

   public static ItemStack getCleanHead() {
      return headItemSample.clone();
   }

   public static ItemStack buildHead(String texture) {
      NBTItem nbtItem = new NBTItem(headItemSample.clone());
      NBTCompound skullCompound = nbtItem.addCompound("SkullOwner");
      LocalUtils.setUUID(skullCompound, "Id", UUID.randomUUID());
      NBTListCompound backpackTextureCompound = skullCompound.addCompound("Properties").getCompoundList("textures").addCompound();
      backpackTextureCompound.setString("Signature", "XpRfRz6/vXE6ip7/vq+40H6W70GFB0yjG6k8hG4pmFdnJFR+VQhslE0gXX/i0OAGThcAVSIT+/W1685wUxNofAiy+EhcxGNxNSJkYfOgXEVHTCuugpr+EQCUBI6muHDKms3PqY8ECxdbFTUEuWxdeiJsGt9VjHZMmUukkGhk0IobjQS3hjQ44FiT1tXuUU86oAxqjlKFpXG/iXtpcoXa33IObSI1S3gCKzVPOkMGlHZqRqKKElB54I2Qo4g5CJ+noudIDTzxPFwEEM6XrbM0YBi+SOdRvTbmrlkWF+ndzVWEINoEf++2hkO0gfeCqFqSMHuklMSgeNr/YtFZC5ShJRRv7zbyNF33jZ5DYNVR+KAK9iLO6prZhCVUkZxb1/BjOze6aN7kyN01u3nurKX6n3yQsoQQ0anDW6gNLKzO/mCvoCEvgecjaOQarktl/xYtD4YvdTTlnAlv2bfcXUtc++3UPIUbzf/jpf2g2wf6BGomzFteyPDu4USjBdpeWMBz9PxVzlVpDAtBYClFH/PFEQHMDtL5Q+VxUPu52XlzlUreMHpLT9EL92xwCAwVBBhrarQQWuLjAQXkp3oBdw6hlX6Fj0AafMJuGkFrYzcD7nNr61l9ErZmTWnqTxkJWZfZxmYBsFgV35SKc8rkRSHBNjcdKJZVN4GA+ZQH5B55mi4=");
      backpackTextureCompound.setString("Value", texture);
      return nbtItem.getItem();
   }

   public static ItemStack buildHead(String displayname, List<String> lore, String texture) {
      ItemStack item = headItemSample.clone();
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(displayname);
      if (lore != null && !lore.isEmpty()) {
         meta.setLore(lore);
      }

      item.setItemMeta(meta);
      NBTItem nbtItem = new NBTItem(item);
      NBTCompound skullCompound = nbtItem.addCompound("SkullOwner");
      LocalUtils.setUUID(skullCompound, "Id", UUID.randomUUID());
      NBTListCompound backpackTextureCompound = skullCompound.addCompound("Properties").getCompoundList("textures").addCompound();
      backpackTextureCompound.setString("Signature", "XpRfRz6/vXE6ip7/vq+40H6W70GFB0yjG6k8hG4pmFdnJFR+VQhslE0gXX/i0OAGThcAVSIT+/W1685wUxNofAiy+EhcxGNxNSJkYfOgXEVHTCuugpr+EQCUBI6muHDKms3PqY8ECxdbFTUEuWxdeiJsGt9VjHZMmUukkGhk0IobjQS3hjQ44FiT1tXuUU86oAxqjlKFpXG/iXtpcoXa33IObSI1S3gCKzVPOkMGlHZqRqKKElB54I2Qo4g5CJ+noudIDTzxPFwEEM6XrbM0YBi+SOdRvTbmrlkWF+ndzVWEINoEf++2hkO0gfeCqFqSMHuklMSgeNr/YtFZC5ShJRRv7zbyNF33jZ5DYNVR+KAK9iLO6prZhCVUkZxb1/BjOze6aN7kyN01u3nurKX6n3yQsoQQ0anDW6gNLKzO/mCvoCEvgecjaOQarktl/xYtD4YvdTTlnAlv2bfcXUtc++3UPIUbzf/jpf2g2wf6BGomzFteyPDu4USjBdpeWMBz9PxVzlVpDAtBYClFH/PFEQHMDtL5Q+VxUPu52XlzlUreMHpLT9EL92xwCAwVBBhrarQQWuLjAQXkp3oBdw6hlX6Fj0AafMJuGkFrYzcD7nNr61l9ErZmTWnqTxkJWZfZxmYBsFgV35SKc8rkRSHBNjcdKJZVN4GA+ZQH5B55mi4=");
      backpackTextureCompound.setString("Value", texture);
      return nbtItem.getItem();
   }

   static {
      Material material = LocalUtils.findMaterial("PLAYER_HEAD", "SKULL_ITEM");
      if (material.name().equals("PLAYER_HEAD")) {
         headItemSample = new ItemStack(material);
      } else {
         headItemSample = new ItemStack(material, 1, (short)3);
      }

   }
}
