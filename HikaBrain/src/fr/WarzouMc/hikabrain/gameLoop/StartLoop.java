package fr.WarzouMc.hikabrain.gameLoop;

import fr.WarzouMc.hikabrain.main.Main;
import fr.WarzouMc.hikabrain.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartLoop extends BukkitRunnable {

    private Main main;
    public StartLoop(Main main) {this.main = main;}

    private int second = 0;
    private int timer = 10;
    private int all = 10 * 20;
    @Override
    public void run() {
        main.setTimer(all);
        if(second == 0){
            second = 20;
            for (int i = 0; i < main.getPlayerInGame().size(); i++){
                Player player = Bukkit.getPlayer(main.getPlayerInGame().get(i));
                switch (timer){
                    case 10:
                        player.resetTitle();
                        player.sendTitle("§cGame start in :", "§e" + timer + "s");
                        player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 100);
                        break;
                    case 5:
                        player.resetTitle();
                        player.sendTitle("§cGame start in :", "§e" + timer + "s");
                        player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 100);
                        break;
                    case 3:
                        player.resetTitle();
                        player.sendTitle("§cGame start in :", "§e" + timer + "s");
                        player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 100);
                        break;
                    case 2:
                        player.resetTitle();
                        player.sendTitle("§cGame start in :", "§e" + timer + "s");
                        player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 100);
                        break;
                    case 1:
                        player.resetTitle();
                        player.sendTitle("§cGame start in :", "§e" + timer + "s");
                        player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 100);
                        break;
                    case 0:
                        main.initPlayer(player);
                        break;
                }
            }
            timer--;
        }
        if(main.getGameState() == GameState.WAITING) cancel();
        if(timer == -1){
            if(main.getGameState() == GameState.STARTING){
                main.setGameState(GameState.PLAYING);
                cancel();
            }
        }
        second--;
        all--;
    }

    public int getTimer(){return timer;}

}
