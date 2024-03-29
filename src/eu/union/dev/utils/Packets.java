package eu.union.dev.utils;

import eu.union.dev.HG;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBarAPI;

/**
 * Created by Fentis on 06/06/2016.
 */


public class Packets {

    private static Packets instance = new Packets();

    public void sendActionBar(Player player, String msg){
        BossBarAPI.setMessage(player,msg);
        Bukkit.getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable() {
            @Override
            public void run() {
                BossBarAPI.removeBar(player);
            }
        },3*20);
    }
   /* public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent titleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '" + title + "'}");
        IChatBaseComponent subtitleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '" + subtitle + "'}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJSON, fadeIn, stay, fadeOut);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleJSON);
        connection.sendPacket(titlePacket);
        connection.sendPacket(subtitlePacket);
    }

    public void sendActionBar(Player player, String msg){
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg +"\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc,(byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
    }

    public void setHeaderFooter(Player player, String header, String footer){
        if(header == null) header = "";
        if(footer == null) footer = "";

        header = ChatColor.translateAlternateColorCodes('&', header);
        footer = ChatColor.translateAlternateColorCodes('&', footer);

        IChatBaseComponent hder = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header + "\"}");
        IChatBaseComponent fter = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            a.set(packet, hder);

            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);
            b.set(packet, fter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }*/

    public static Packets getAPI() {
        return instance;
    }
}
