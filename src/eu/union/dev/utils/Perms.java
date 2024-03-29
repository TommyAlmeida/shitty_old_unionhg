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
    UNION_DEV("union.dev"),

    /**
     * Server perms
     */

    RESPAWN("union.respawn"),
    SPECTATOR("union.spectar"),
    KIT_POS_START("union.kitposstart"),
    START("union.start"),

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
                p.hasPermission(UNION_MOD.toString()) ||
                p.hasPermission(UNION_DEV.toString())) {
            return true;
        } else {
            //p.sendMessage(Messages.PREFIX.toString() + " §cYou don't have permission to use this.");
            return false;
        }
    }

    public static boolean isYoutuber(Player p) {
        if (p.hasPermission(UNION_YT.toString())) {
            return true;
        } else {
            //p.sendMessage(Messages.PREFIX.toString() + " §cYou don't have permission to use this.");
            return false;
        }
    }

    @Override
    public String toString() {
        return this.s;
    }
}