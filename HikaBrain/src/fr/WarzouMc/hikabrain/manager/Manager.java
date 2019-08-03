package fr.WarzouMc.hikabrain.manager;

import fr.WarzouMc.hikabrain.gameLoop.NewPoint;
import fr.WarzouMc.hikabrain.gameLoop.StartLoop;
import fr.WarzouMc.hikabrain.gameLoop.WinLoop;
import fr.WarzouMc.hikabrain.graphic.bossBar.BossBar;
import fr.WarzouMc.hikabrain.graphic.scoreboard.Creater;
import fr.WarzouMc.hikabrain.main.Main;
import fr.WarzouMc.hikabrain.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Map;

public class Manager implements Listener {

    private Main main;
    public Manager(Main main) {this.main = main;}

    private Location spawn;

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    Team team = null;

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String playerName = player.getName();

        spawn = new Location(player.getLocation().getWorld(), 0.5, 70.0, 0.5);

        player.teleport(spawn);
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setHealth(20);
        player.setBedSpawnLocation(spawn);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        if(main.getPlayerInGame().size() > 1){
            for (int i = 0; i < main.getPlayerInGame().size(); i++) {
                Player pls = Bukkit.getPlayer(main.getPlayerInGame().get(i));
                pls.hidePlayer(player);
            }

            player.sendTitle("§cThe game is already", "§eStart");
            player.setGameMode(GameMode.SPECTATOR);
        }else{
            addPlayer(playerName);
            player.setGameMode(GameMode.ADVENTURE);
        }

        if(main.getPlayerInGame().size() == 2 && main.getGameState() == GameState.WAITING){
            main.setGameState(GameState.STARTING);
            StartLoop startLoop = new StartLoop(main);
            startLoop.runTaskTimer(main, 0, 1);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        String playerName = player.getName();

        if(main.getPlayerInGame().contains(playerName)){
            rvmPlayer(playerName);
            for (int i = 0; i < main.getPlayerInGame().size(); i++) {
                Player pls = Bukkit.getPlayer(main.getPlayerInGame().get(i));
                if(main.getGameState() == GameState.STARTING) {
                    pls.sendTitle("§cWaiting for", "§emore player !");
                    main.setGameState(GameState.WAITING);
                }else if (main.getGameState() != GameState.WINNING){
                    main.setGameState(GameState.WINNING);
                    win();
                    main.setWinner(main.getPlayerInGame().get(0));
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        String playerName = player.getName();

        player.setSaturation(20);
        player.setFoodLevel(20);

        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY()-1;
        int z = player.getLocation().getBlockZ();

        Map<String, Integer> point = main.getPoint();

        if(main.getGameState() == GameState.NEWPOINT){
            main.initPlayer(player);
        }

        Player bluePlayer = null;
        Player redPlayer = null;
        int bluePoint = 0;
        int redPoint = 0;
        if(main.getGameState() != GameState.WAITING && main.getGameState() != GameState.STARTING){
            bluePlayer = Bukkit.getPlayer(main.getPlayerInGame().get(0));
            redPlayer = Bukkit.getPlayer(main.getPlayerInGame().get(1));
            redPoint = main.getPoint().get(redPlayer.getName());
            bluePoint = main.getPoint().get(bluePlayer.getName());
        }

        if(main.getGameState() == GameState.PLAYING && player.getLocation().getBlockY() < 50){
            main.initPlayer(player);
        }else if(main.getGameState() == GameState.PLAYING && player.getLocation().getWorld().getBlockAt(x, y, z).getType() == Material.WOOL){
            if(main.getPlayerInGame().get(0).equalsIgnoreCase(playerName)){
                if(player.getLocation().getWorld().getBlockAt(x, y, z).getData() == 14){
                    point.replace(playerName, point.get(playerName) + 1);
                    main.setPoint(point);
                    if(main.getPoint().get(playerName) == 5){
                        main.setGameState(GameState.WINNING);
                        Bukkit.getPlayer(main.getPlayerInGame().get(1)).setGameMode(GameMode.SPECTATOR);
                        player.setAllowFlight(true);
                        player.getLocation().setY(player.getLocation().getY() + 0.1);
                        player.setFlying(true);
                        for (Player pls : Bukkit.getOnlinePlayers()){
                            pls.sendTitle("§1" + playerName + " has win", "§1" + 5 + " §6- §4" + redPoint);
                        }
                        main.setWinner(playerName);
                        win();
                    }else goal();
                }
            }else if(main.getPlayerInGame().get(1).equalsIgnoreCase(playerName)){
                if(player.getLocation().getWorld().getBlockAt(x, y, z).getData() == 11){
                    point.replace(playerName, point.get(playerName) + 1);
                    main.setPoint(point);
                    if(main.getPoint().get(playerName) == 5){
                        main.setGameState(GameState.WINNING);
                        player.setAllowFlight(true);
                        player.getLocation().setY(player.getLocation().getY() + 0.1);
                        player.setFlying(true);
                        Bukkit.getPlayer(main.getPlayerInGame().get(0)).setGameMode(GameMode.SPECTATOR);
                        for (Player pls : Bukkit.getOnlinePlayers()){
                            pls.sendTitle("§1" + playerName + " has win", "§4" + 5 + " §6- §1" + bluePoint);
                        }
                        main.setWinner(playerName);
                        win();
                    }else goal();
                }
            }
        }
    }

    private void goal() {
        main.setGameState(GameState.NEWPOINT);
        NewPoint newPoint = new NewPoint(main);
        newPoint.runTaskTimer(main, 0, 1);
        main.initPlayer(Bukkit.getPlayer(main.getPlayerInGame().get(0)));
        main.initPlayer(Bukkit.getPlayer(main.getPlayerInGame().get(1)));
        main.initMap();
    }

    private void win(){
        for (int x = -21; x < 22; x++) {
            for (int z = -7; z < 8; z++){
                Bukkit.getWorlds().get(0).getBlockAt(x, 69, z).setType(Material.AIR);
            }
        }
        WinLoop winLoop = new WinLoop(main);
        winLoop.runTaskTimer(main, 0, 20);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if(main.getGameState() != GameState.PLAYING) {
            event.setCancelled(true);
            return;
        }else if (entity instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent event){
        Entity entity = event.getEntity();

        if(entity instanceof Player){
            if((( Player ) entity).getHealth() <= event.getDamage()){
                main.initPlayer(( Player ) entity);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        if(block.getType() != Material.SANDSTONE || main.getGameState() != GameState.PLAYING){
            event.setCancelled(true);
            return;
        }
        if(blockLocation.getZ() >= -1 && blockLocation.getZ() <= 1 && blockLocation.getY() >= 59){
            event.setCancelled(blockLocation.getX() >= 19 || blockLocation.getX() <= -19);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        if(block.getType() != Material.SANDSTONE || main.getGameState() != GameState.PLAYING){
            event.setCancelled(true);
            return;
        }
        if(blockLocation.getZ() >= -1 && blockLocation.getZ() <= 1 && blockLocation.getY() >= 59){
            event.setCancelled(blockLocation.getX() >= 19 || blockLocation.getX() <= -19);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        event.setCancelled(true);
    }

    private void rvmPlayer(String playerName) {
        List<String> playerInGame = main.getPlayerInGame();
        Map<String, Integer> point = main.getPoint();

        Creater creater = new Creater(main);
        creater.destroy(Bukkit.getPlayer(playerName));

        playerInGame.remove(playerName);
        point.remove(playerName);
        main.setPlayerInGame(playerInGame);

        rvmBar(Bukkit.getPlayer(playerName));

    }

    private void addPlayer(String playerName) {
        List<String> playerInGame = main.getPlayerInGame();
        Map<String, Integer> point = main.getPoint();

        Creater creater = new Creater(main);
        creater.create(Bukkit.getPlayer(playerName));

        playerInGame.add(playerName);
        point.put(playerName, 0);
        main.setPlayerInGame(playerInGame);
        Bukkit.getPlayer(playerName).setPlayerListName("§k" + playerName);
        Bukkit.getPlayer(playerName).setDisplayName("§k" + playerName + "§r");

        if(scoreboard.getTeam(playerName) == null){
            team = scoreboard.registerNewTeam(playerName);
        }else{
            team = scoreboard.getTeam(playerName);
        }

        setBar(Bukkit.getPlayer(playerName));

        team.setPrefix("§k");
        team.addPlayer(Bukkit.getPlayer(playerName));
    }

    public void rvmBar(Player player) {
        Map<Player, BossBar> bar = main.getBar();
        if(bar.containsKey(player)){
            bar.remove(player);
            main.setBar(bar);
        }
    }

    public void setBar(Player player) {
        Map<Player, BossBar> bar = main.getBar();
        if(!bar.containsKey(player)){
            bar.put(player, new BossBar(player, "§cWaiting player", 100));
            main.setBar(bar);
        }
    }

}
