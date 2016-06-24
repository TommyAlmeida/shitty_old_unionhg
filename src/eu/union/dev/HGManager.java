package eu.union.dev;

import eu.union.dev.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Fentis on 06/06/2016.
 */
public class HGManager {

    private static HGManager instance = new HGManager();
    public static HGManager getInstance() {
        return instance;
    }
    private Status status;
    Location feast,minifeast1,minifeast2,minifeast3,coliseu,deathmatch = null;
    int bordsize = 600;//480
    int camadalimite = 135;
    private List<UUID> playersvivos = new ArrayList<>();
    private ArrayList<Player> build = new ArrayList<>();
    private List<Player> specs = new ArrayList<>();
    private ArrayList<Player> noscore = new ArrayList<>();
    private HashMap<Player,Integer> kills = new HashMap<>();
    private ArrayList<UUID> nodamage = new ArrayList<>();
    private ArrayList<String> reconect = new ArrayList<>();
    private ArrayList<UUID> admin = new ArrayList<>();
    public enum Status {
        LOBBY("Lobby"),
        INVINCIBILITY("Invencibility"),
        POS_INVINCIBILITY("PosInvencibility"),
        FEAST_ANNOUNCEMENT("FeastAnnouncement"),
        FEAST("Feast"),
        DEATH_MATCH("DeathMatch"),
        ENDGAME("EndGame");

        String d;

        Status(String d) {
            this.d = d;
        }

        public String value() {
            return this.d;
        }
    }
    public Status getStatus() {
        return status;
    }

    public int getBordSize() {
        return bordsize;
    }

    public int getCamadalimite(){return camadalimite;}

    public void setStatus(Status status) {
        this.status = status;
    }
    public StructureCreator scc;
    public void setup(){
        coliseu = new Location(Bukkit.getWorlds().get(0),0,150,0);
        feast = RandomLocation(150);
        minifeast1 = RandomLocation(500);
        minifeast2 = RandomLocation(500);
        minifeast3 = RandomLocation(500);
        deathmatch = new Location(feast.getWorld(),feast.getX(),110,feast.getZ());
        scc = new StructureCreator(coliseu, StructureCreator.Structure.COLISEU);
        scc.createStrucure();
        Bukkit.getWorlds().get(0).setSpawnLocation(0,155,0);
    }
    public Location RandomLocation(int raio)
    {
        Random random = new Random();
        Location startFrom = new Location(Bukkit.getWorlds().get(0),0,0,0);

        Location loc = startFrom.clone();
        loc.add((random.nextBoolean() ? 1 : -1) * random.nextInt(raio), raio,
                (random.nextBoolean() ? 1 : -1) * random.nextInt(raio));
        int newY = Bukkit.getWorlds().get(0).getHighestBlockYAt(loc);
        loc.setY(newY);
        return loc;
    }

    public Location getFeastLoc() {
        return feast;
    }

    public Location getColiseuLoc() {
        return coliseu;
    }

    public Location getDeathMatchLoc() { return deathmatch;}

    public Location getMiniFeastLoc(int n) {
        if (n == 1){
            return minifeast1;
        }
        if (n == 2){
            return minifeast2;
        }
        if (n == 3){
            return minifeast3;
        }
        return null;
    }

    public List<Player> getPlayersVivos() {
        List<Player> players = new ArrayList<>();
        for (UUID uuid : playersvivos){
            Player p = Bukkit.getPlayer(uuid);
            if (p != null){
                players.add(p);
            }
        }
        return players;
    }
    public void addPlayersVivos(Player p){
        if (!playersvivos.contains(p.getUniqueId())){
            playersvivos.add(p.getUniqueId());
        }
    }
    public void removePlayersVivos(Player p){
        if (playersvivos.contains(p.getUniqueId())){
            playersvivos.remove(p.getUniqueId());
        }
    }
    public boolean isAlive(Player p){
        return playersvivos.contains(p.getUniqueId());
    }

    public boolean inBuild(Player p){
        return build.contains(p);
    }
    public void addBuild(Player p){
        if (!inBuild(p)){
            build.add(p);
        }
    }
    public void removeBuild(Player p){
        if (inBuild(p)){
            build.remove(p);
        }
    }

    public List<Player> getSpecs() {
        return specs;
    }
    public void addSpec(Player p){
        if (!specs.contains(p)){
            specs.add(p);
        }
    }
    public void removeSpec(Player p){
        if (specs.contains(p)){
            specs.remove(p);
        }
    }
    public boolean isSpec(Player p){
        return specs.contains(p);
    }

    public ArrayList<Player> getNoScore() {
        return noscore;
    }

    public void addNoScore(Player p){
        if (!noscore.contains(p)){
            noscore.add(p);
        }
    }
    public void removeNoScore(Player p){
        if (noscore.contains(p)){
            noscore.remove(p);
        }
    }
    public void addKills(Player p){
        if (kills.containsKey(p)){
            kills.put(p,kills.get(p)+1);
        }else{
            kills.put(p,1);
        }
    }
    public int getKills(Player p){
        if (kills.containsKey(p)){
            return kills.get(p);
        }
        return 0;
    }

    public boolean isNoDamage(Player p){ return nodamage.contains(p.getUniqueId());}

    public void addNoDamage(Player p){
        if (!isNoDamage(p)){
            nodamage.add(p.getUniqueId());
        }
    }

    public void removeNoDamage(Player p){
        if (isNoDamage(p)){
            nodamage.remove(p.getUniqueId());
        }
    }

    public boolean inReconect(Player p){ return reconect.contains(p.getName()); }

    public void addReconect(Player p){
        if (!inReconect(p)){
            reconect.add(p.getName());
        }
    }

    public void removeReconect(Player p){
        if (inReconect(p)){
            reconect.remove(p.getName());
        }
    }

    public boolean inAdminMode(Player p){ return admin.contains(p.getUniqueId()); }

    public void addAdminMode(Player p){
        if (!inAdminMode(p)){
            admin.add(p.getUniqueId());
        }
    }

    public void removeAdminMode(Player p){
        if (inAdminMode(p)){
            admin.remove(p.getUniqueId());
        }
    }
}