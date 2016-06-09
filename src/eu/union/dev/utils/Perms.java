package eu.union.dev.utils;

/**
 * Created by Fentis on 06/06/2016.
 */
import org.bukkit.entity.Player;

public enum Perms {

    UNION_ADMIN("union.admin"),
    UNION_HELPER("union.helper"),
    UNION_OWNER("union.owner"),
    UNION_TMOD("union.owner"),
    UNION_MOD("union.mod"),
    UNION_YT("union.yt"),
    UNION_YT_PLUS("union.yt+"),

    /**
     * Server perms
     */

    RESPAWN("union.respawn"),
    SPECTATOR("union.spectar"),

    /**
     * KITS
     */
    KIT_FREE("unkit.free");


    String s;

    Perms(String s) {
        this.s = s;
    }

    public static boolean isStaff(Player p) {
        if (p.hasPermission(UNION_ADMIN.toString()) ||
                p.hasPermission(UNION_OWNER.toString()) ||
                p.hasPermission(UNION_TMOD.toString()) ||
                p.hasPermission(UNION_MOD.toString())) {
            return true;
        } else {
            p.sendMessage(Messages.PREFIX.toString() + " §cYou dont have permission to use this.");
            return false;
        }
    }

    public static boolean isYoutuber(Player p) {
        if (p.hasPermission(UNION_YT.toString())) {
            return true;
        } else {
            p.sendMessage(Messages.PREFIX.toString() + " §cYou dont have permission to use this.");
            return false;
        }
    }

    @Override
    public String toString() {
        return this.s;
    }
}