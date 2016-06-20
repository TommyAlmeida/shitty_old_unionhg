package eu.union.dev.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class SoupListener implements Listener {
    public SoupListener() {
        ShapelessRecipe cocoaSoup = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        cocoaSoup.addIngredient(1, Material.INK_SACK, 3);
        cocoaSoup.addIngredient(1, Material.BOWL);
        Bukkit.addRecipe(cocoaSoup);
        ShapelessRecipe cacSoup = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        cacSoup.addIngredient(1, Material.CACTUS);
        cacSoup.addIngredient(1, Material.BOWL);
        Bukkit.addRecipe(cacSoup);
        ShapelessRecipe abobSoup = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        abobSoup.addIngredient(2, Material.PUMPKIN_SEEDS);
        abobSoup.addIngredient(1, Material.BOWL);
        Bukkit.addRecipe(abobSoup);
        ShapelessRecipe florSoup = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        florSoup.addIngredient(1, Material.RED_ROSE);
        florSoup.addIngredient(1, Material.YELLOW_FLOWER);
        florSoup.addIngredient(1, Material.BOWL);
        Bukkit.addRecipe(florSoup);
        ShapelessRecipe melSoup = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        melSoup.addIngredient(2, Material.MELON_SEEDS);
        melSoup.addIngredient(1, Material.BOWL);
        Bukkit.addRecipe(melSoup);
    }
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (p.getHealth() < p.getMaxHealth() && p.getItemInHand().getType() == Material.MUSHROOM_SOUP){
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR){
                p.setItemInHand(new ItemStack(Material.BOWL,p.getItemInHand().getAmount()));
                double soup = 7.0;
                if ((p.getHealth()+soup) <= p.getMaxHealth()){
                    p.setHealth(p.getHealth()+soup);
                }else{
                    p.setHealth(p.getMaxHealth());
                }
            }
        }
    }
}
