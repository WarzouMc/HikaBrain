package fr.WarzouMc.hikabrain.graphic.bossBar;

import fr.WarzouMc.hikabrain.gameLoop.StartLoop;
import fr.WarzouMc.hikabrain.main.Main;
import fr.WarzouMc.hikabrain.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class BossBarUpdater extends BukkitRunnable {

    private Main main;
    public BossBarUpdater(Main main){this.main = main;}

    @Override
    public void run(){
        if (main.getGameState() == GameState.STARTING || main.getGameState() == GameState.PLAYING || main.getGameState() == GameState.NEWPOINT || main.getGameState() == GameState.WINNING){
            if(main.getPlayerInGame().size() == 2){
                Map<Player, BossBar> bossBarMap = main.getBar();
                Player bluePlayer = Bukkit.getPlayer(main.getPlayerInGame().get(0));
                Player redPlayer = Bukkit.getPlayer(main.getPlayerInGame().get(1));

                float bluePoint = main.getPoint().get(bluePlayer.getName());
                float redPoint = main.getPoint().get(redPlayer.getName());

                float bluePerCent = ((50 + (redPoint * 10)) * bluePoint / 5) - ((50 + (bluePoint * 2 * 10)) * redPoint / 5) + 50;
                float redPerCent = ((50 + (bluePoint * 2 * 10)) * redPoint / 5) - ((50 + (redPoint * 10)) * bluePoint / 5) + 50;

                if (bluePoint > redPoint){
                    bluePerCent = ((50 + (redPoint * 2 * 10)) * bluePoint / 5) - ((50 + (bluePoint * 10)) * redPoint / 5) + 50;
                    redPerCent = ((50 + (bluePoint * 10)) * redPoint / 5) - ((50 + (redPoint * 2 * 10)) * bluePoint / 5) + 50;
                }else if (bluePoint == redPoint){
                    bluePerCent = ((50 + (redPoint * 2 * 10)) * bluePoint / 5) - ((50 + (bluePoint * 2 * 10)) * redPoint / 5) + 50;
                    redPerCent = ((50 + (bluePoint * 2 * 10)) * redPoint / 5) - ((50 + (redPoint * 2 * 10)) * bluePoint / 5) + 50;
                }

                System.out.println(bluePerCent + " " + redPerCent);

                for (Map.Entry<Player, BossBar> bar : bossBarMap.entrySet()) {
                    if (main.getGameState() == GameState.STARTING){
                        float timer = main.getTimer();
                        bar.getValue().update(bar.getKey(), "§eGame start in : §c" + (int) ((timer / 20) + 1) + "s", 100 * timer / (10 * 20));
                    }else {
                        if (bar.getKey() == bluePlayer){
                            bar.getValue().update(bar.getKey(), "§escore : §1" + (int) bluePoint + " §f/ §4" + (int) redPoint, bluePerCent);
                        }else {
                            bar.getValue().update(bar.getKey(), "§escore : §4" + (int) redPoint + " §f/ §1" + (int) bluePoint, redPerCent);
                        }
                    }
                }
            }
        }else if (main.getGameState() == GameState.WAITING){
            Map<Player, BossBar> bossBarMap = main.getBar();
            for (Map.Entry<Player, BossBar> bar : bossBarMap.entrySet()) {
                bar.getValue().update(bar.getKey(), "§cWaiting player", 100);
            }
        }
    }

}
