package dev.sejtam.gui.utils;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MinecraftVersion {
    v1_8( "1_8", 0 ),
    v1_9( "1_9", 1 ),
    v1_10( "1_10", 2 ),
    v1_11( "1_11", 3 ),
    v1_12( "1_12", 4 ),
    v1_13( "1_13", 5 ),
    v1_14( "1_14", 6 ),
    v1_15( "1_15", 7 ),
    v1_16( "1_16", 8 ),
    v1_17( "1_17", 9 ),
    v1_18( "1_18", 10 ),
    v1_19( "1_19", 11 );

    private int order;
    private String key;

    @Getter(AccessLevel.PUBLIC)
    private static String version;
    private static MinecraftVersion minecraftVersion;

    static {
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        minecraftVersion = get(getVersion());
    }

    MinecraftVersion(String key, int v ) {
        this.key = key;
        this.order = v;
    }

    @Contract(pure = true)
    public boolean greaterThanOrEqualTo(@NotNull MinecraftVersion other ) {
        return this.order >= other.order;
    }

    @Contract(pure = true)
    public boolean lessThanOrEqualTo(@NotNull MinecraftVersion other ) {
        return this.order <= other.order;
    }

    @Nullable
    public static MinecraftVersion get(String v ) {
        for ( MinecraftVersion k : MinecraftVersion.values() ) {
            if ( v.contains( k.key ) ) {
                return k;
            }
        }
        return null;
    }

    @Contract(pure = true)
    public static MinecraftVersion get() {
        return minecraftVersion;
    }
}
