package eu.union.dev.kits.rare;

import eu.union.dev.HG;
import eu.union.dev.KitManager;
import eu.union.dev.api.Icon;
import eu.union.dev.events.HGTimerSecondsEvent;
import eu.union.dev.storage.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Simba extends Kit implements Listener{

    /*
    * Created by Owen
    */
	public Simba() {//
		super("simba", false, Difficulty.MEDIUM, Rarity.RARE, new Icon(Material.DIAMOND_HELMET), 1000L);
	}

	@Override
	public void applyKit(Player player) {
		//ganha nada
	}
	@EventHandler
	public void onLook(HGTimerSecondsEvent e){
		for(Player p: Bukkit.getOnlinePlayers()){
			KitManager kit = new KitManager();
			if(kit.getKitAmIUsing(p, "simba")) {
				
				for(Entity around: p.getNearbyEntities(4.0D, 4.0D, 4.0D)){
					
					if(around instanceof Player){
						
						Player p2 = (Player)around;
						// Start Looking if someone is near....
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 900, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 900, 0));
						p.removePotionEffect(PotionEffectType.WEAKNESS);
						
					}else{
						
						p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 900 ,1));
					}
				}
			}
		}
	}
		
}