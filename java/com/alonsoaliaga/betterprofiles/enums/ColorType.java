package com.alonsoaliaga.betterprofiles.enums;

public enum ColorType {
   WHITE((byte)0),
   ORANGE((byte)1),
   MAGENTA((byte)2),
   LIGHT_BLUE((byte)3),
   YELLOW((byte)4),
   LIME((byte)5),
   PINK((byte)6),
   GRAY((byte)7),
   LIGHT_GRAY((byte)8),
   CYAN((byte)9),
   PURPLE((byte)10),
   BLUE((byte)11),
   BROWN((byte)12),
   GREEN((byte)13),
   RED((byte)14),
   BLACK((byte)15);

   private byte data;

   private ColorType(byte data) {
      this.data = data;
   }

   public byte getData() {
      return this.data;
   }

   public static ColorType getColor(String name) {
      if (name == null) {
         return null;
      } else {
         ColorType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ColorType colorType = var1[var3];
            if (colorType.name().equalsIgnoreCase(name)) {
               return colorType;
            }
         }

         return null;
      }
   }

   public static ColorType getColor(String name, ColorType defaultColorType) {
      if (name == null) {
         return defaultColorType;
      } else {
         ColorType[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ColorType colorType = var2[var4];
            if (colorType.name().equalsIgnoreCase(name)) {
               return colorType;
            }
         }

         return defaultColorType;
      }
   }
}
