package eu.union.dev;

import eu.union.dev.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Fentis on 06/06/2016.
 */
public class HGManager {

    private static HGManager instance = new HGManager();
    public static HGManager getInstance() {
        return instance;
    }
    private Status status;
    Location feast,minifeast1,minifeast2,minifeast3,coliseu = null;
    int bordsize = 600;//480
    private List<UUID> playersvivos = new ArrayList<>();
    private ArrayList<Player> build = new ArrayList<>();
    private List<Player> specs = new ArrayList<>();
    private ArrayList<Player> noscore = new ArrayList<>();
    public enum Status {
        LOBBY("Lobby"),
        INVINCIBILITY("Invencibility"),
        POS_INVINCIBILITY("PosInvencibility"),
        FEAST_ANNOUNCEMENT("FeastAnnouncement"),
        FEAST("Feast"),
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

    public void setStatus(Status status) {
        this.status = status;
    }
    StructureCreator scc;
    public void setup(){
        coliseu = new Location(Bukkit.getWorlds().get(0),0,150,0);
        feast = RandomLocation(100);
        minifeast1 = RandomLocation(500);
        minifeast2 = RandomLocation(500);
        minifeast3 = RandomLocation(500);
        scc = new StructureCreator(coliseu, StructureCreator.Structure.COLISEU);
        scc.createStrucure();
        Bukkit.getWorlds().get(0).setSpawnLocation(0,155,0);
    }
    public static Location RandomLocation(int raio)
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
}