package fr.WarzouMc.hikabrain.main;

import fr.WarzouMc.hikabrain.graphic.bossBar.BossBar;
import fr.WarzouMc.hikabrain.graphic.bossBar.BossBarUpdater;
import fr.WarzouMc.hikabrain.graphic.scoreboard.Creater;
import fr.WarzouMc.hikabrain.graphic.scoreboard.ScoreBoardUpdater;
import fr.WarzouMc.hikabrain.graphic.scoreboard.ScoreboardSign;
import fr.WarzouMc.hikabrain.manager.Manager;
import fr.WarzouMc.hikabrain.state.GameState;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {

    private GameState gameState;

    private List<String> playerInGame = new ArrayList<>();
    private List<String> playerOutGame = new ArrayList<>();
    private Map<String, Integer> point = new HashMap<>();
    private Map<Player, ScoreboardSign> board = new HashMap<>();
    private Map<Player, BossBar> bar = new HashMap<>();

    private int timer = 10;

    public String winner = null;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§2#########################\n" +
                "                 §2##§6Hikabrain by §1War§fzo§4uMc§2##\n" +
                "                 §2#########################");

        Bukkit.getWorlds().get(0).setGameRuleValue("doDaylightCycle", "false");
        Bukkit.getWorlds().get(0).setGameRuleValue("doMobSpawning", "false");
        Bukkit.getWorlds().get(0).setGameRuleValue("keepInventory", "true");

        setGameState(GameState.WAITING);

        getServer().getPluginManager().registerEvents(new Manager(this), this);

        ScoreBoardUpdater scoreBoardUpdater = new ScoreBoardUpdater(this);
        scoreBoardUpdater.runTaskTimer(this, 0, 1);

        BossBarUpdater bossBarUpdater = new BossBarUpdater(this);
        bossBarUpdater.runTaskTimer(this, 0, 1);
        initMap();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§4#########################\n" +
                "                 §4##§6Hikabrain by §1War§fzo§4uMc§4##\n" +
                "                 §4#########################");

        for (Player pls : Bukkit.getOnlinePlayers()) {
            Creater creater = new Creater(this);
            creater.destroy(pls);
            pls.kickPlayer("§4End of the game !");
        }
    }

    public void initPlayer(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = null;
        float x = 20.5f;
        float yaw = 0;
        if(getPlayerInGame().get(0).equalsIgnoreCase(player.getName())){
            x = 20.5f;
            yaw = 90.0f;
            player.setPlayerListName("§1" + player.getName());
            player.setDisplayName("§1" + player.getName() + "§r");


            if (getGameState() != GameState.NEWPOINT && getGameState() != GameState.STARTING) {
                FireworkEffect fireworkEffect = FireworkEffect.builder()
                        .with(FireworkEffect.Type.STAR)
                        .withColor(Color.BLUE)
                        .build();

                Firework firework = (Firework)player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                firework.detonate();
                FireworkMeta fireworkMeta = firework.getFireworkMeta();

                fireworkMeta.setPower(0);
                fireworkMeta.addEffect(fireworkEffect);
                firework.setFireworkMeta(fireworkMeta);
            }


            if(scoreboard.getTeam(player.getName()) == null){
                team = scoreboard.registerNewTeam(player.getName());
            }else{
                team = scoreboard.getTeam(player.getName());
            }

            team.setPrefix("§1");
            team.addPlayer(Bukkit.getPlayer(player.getName()));
        }else {
            x = -20.5f;
            yaw = 270.0f;
            player.setPlayerListName("§4" + player.getName());
            player.setDisplayName("§4" + player.getName() + "§r");


            if (getGameState() != GameState.NEWPOINT && getGameState() != GameState.STARTING) {
                FireworkEffect fireworkEffect = FireworkEffect.builder()
                        .with(FireworkEffect.Type.STAR)
                        .withColor(Color.RED)
                        .build();

                Firework firework = (Firework)player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                firework.detonate();
                FireworkMeta fireworkMeta = firework.getFireworkMeta();

                fireworkMeta.setPower(0);
                fireworkMeta.addEffect(fireworkEffect);
                firework.setFireworkMeta(fireworkMeta);
            }


            if(scoreboard.getTeam(player.getName()) == null){
                team = scoreboard.registerNewTeam(player.getName());
            }else{
                team = scoreboard.getTeam(player.getName());
            }

            team.setPrefix("§4");
            team.addPlayer(Bukkit.getPlayer(player.getName()));
        }
        Location tp = new Location(player.getLocation().getWorld(), x, 64, 0.5f, yaw, 0.0f);
        player.teleport(tp);
        player.setHealth(20);
        player.setSaturation(20);
        player.setFoodLevel(20);
        player.getInventory().clear();
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemStack pickAxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemStack gApple = new ItemStack(Material.GOLDEN_APPLE, 64);
        ItemStack sandStonne = new ItemStack(Material.SANDSTONE, 64);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack leggins = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS);

        ItemMeta swordM = sword.getItemMeta();
        ItemMeta pickAxeM = pickAxe.getItemMeta();

        swordM.addEnchant(Enchantment.DURABILITY, 1000, true);
        swordM.addEnchant(Enchantment.KNOCKBACK, 1, true);
        sword.setItemMeta(swordM);

        pickAxeM.addEnchant(Enchantment.DURABILITY, 1000, true);
        pickAxe.setItemMeta(pickAxeM);

        player.getInventory().addItem(sword);
        player.getInventory().addItem(pickAxe);
        player.getInventory().addItem(gApple);

        player.setNoDamageTicks(20*2);

        for (int i = 0; i < 34; i++) {
            player.getInventory().addItem(sandStonne);
        }

        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);

        player.setGameMode(GameMode.SURVIVAL);
    }

    public void initMap(){
        World world = Bukkit.getWorlds().get(0);

        for (int x = -21; x < 22; x++) {
            for (int y = 50; y < 73; y++) {
                for (int z = -7; z < 8; z++){
                    world.getBlockAt(x, y, z).setType(Material.AIR);
                    world.getBlockAt(x, 69, z).setType(Material.STAINED_GLASS);
                }
            }
        }

        for (int y = 50; y < 73; y++){
            for (int x = -22; x < 23; x++){
                for (int z = -8; z < 9; z++){
                    if(x == -22 || x == 22 || z == -8 || z == 8){
                        if (y < 70){
                            world.getBlockAt(x, y, z).setType(Material.OBSIDIAN);
                        }else if (y > 70){
                            world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                        }else {
                            world.getBlockAt(x, y, z).setType(Material.AIR);
                        }
                    }
                }
            }
        }

        for (int x = -21; x < 22; x++) {
            for (int y = 50; y < 60; y++) {
                if(x == 0 && y == 59){
                    world.getBlockAt(x, y, 0).setType(Material.REDSTONE_BLOCK);
                }else if((x == 21 || x == 20) && y == 59){
                    world.getBlockAt(x, y, 0).setType(Material.WOOL);
                    world.getBlockAt(x, y, 0).setData(( byte ) 11);
                }else if((x == -21 || x == -20) && y == 59){
                    world.getBlockAt(x, y, 0).setType(Material.WOOL);
                    world.getBlockAt(x, y, 0).setData(( byte ) 14);
                }else{
                    world.getBlockAt(x, y, 0).setType(Material.SANDSTONE);
                }
            }
        }

        for (int x = 19; x < 22; x++) {
            for (int z = -1; z < 2; z++) {
                world.getBlockAt(x, 63, z).setType(Material.STAINED_GLASS);
                world.getBlockAt(x, 63, z).setData(( byte ) 11);
            }
        }

        for (int x = -21; x < -18; x++) {
            for (int z = -1; z < 2; z++) {
                world.getBlockAt(x, 63, z).setType(Material.STAINED_GLASS);
                world.getBlockAt(x, 63, z).setData(( byte ) 14);
            }
        }
    }

    public int getTimer(){return timer;}

    public void setTimer(int timer){this.timer = timer;}

    public Map<String, Integer> getPoint() {
        return point;
    }

    public void setPoint(Map<String, Integer> point) {
        this.point = point;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public List<String> getPlayerInGame() {
        return playerInGame;
    }

    public void setPlayerInGame(List<String> playerInGame) {
        this.playerInGame = playerInGame;
    }

    public List<String> getPlayerOutGame() {
        return playerOutGame;
    }

    public void setPlayerOutGame(List<String> playerOutGame) {
        this.playerOutGame = playerOutGame;
    }

    public Map<Player, ScoreboardSign> getBoard(){return board;}

    public void setBoard(Map<Player, ScoreboardSign> board){this.board = board;}

    public String getWinner(){return winner;}

    public void setWinner(String winner){this.winner = winner;}

    public Map<Player, BossBar> getBar() {
        return bar;
    }

    public void setBar(Map<Player, BossBar> bar) {
        this.bar = bar;
    }

}
