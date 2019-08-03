package fr.WarzouMc.hikabrain.gameLoop;

import fr.WarzouMc.hikabrain.main.Main;
import fr.WarzouMc.hikabrain.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class NewPoint extends BukkitRunnable {

    private Main main;
    public NewPoint(Main main){this.main = main;}

    private int timer = 5;
    private int seconde = 0;

    @Override
    public void run() {
        if(seconde == 0){
            seconde = 20;
            main.setGameState(GameState.NEWPOINT);
            Player bluePlayer = Bukkit.getPlayer(main.getPlayerInGame().get(0));
            Player redPlayer = Bukkit.getPlayer(main.getPlayerInGame().get(1));

            int bluePoint = main.getPoint().get(bluePlayer.getName());
            int redPoint = main.getPoint().get(redPlayer.getName());

            bluePlayer.resetTitle();
            redPlayer.resetTitle();
            bluePlayer.sendTitle("§1" + bluePoint + " §6- §4" + redPoint, "§c" + timer);
            bluePlayer.playSound(bluePlayer.getLocation(), Sound.NOTE_PLING, 1, 100);

            redPlayer.sendTitle("§4" + redPoint + " §6- §1" + bluePoint, "§c" + timer);
            redPlayer.playSound(redPlayer.getLocation(), Sound.NOTE_PLING, 1, 100);
            if(timer == 0){
                main.setGameState(GameState.PLAYING);
                cancel();
            }
            timer--;
        }
        if (main.getPlayerInGame().size() == 1) {
            main.setGameState(GameState.WINNING);
            cancel();
        }
        seconde--;
    }
}
