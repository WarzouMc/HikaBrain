package fr.WarzouMc.hikabrain.graphic.scoreboard;

import fr.WarzouMc.hikabrain.main.Main;
import fr.WarzouMc.hikabrain.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.*;

public class ScoreBoardUpdater extends BukkitRunnable {

    private Main main;
    public ScoreBoardUpdater(Main main){this.main = main;}

    private List<String> name = new ArrayList<>();

    private int second = 0;
    private int showName = 0;

    @Override
    public void run() {
        Map<Player, ScoreboardSign> board = main.getBoard();
        List<String> playerInGame = main.getPlayerInGame();

        name.clear();
        name.add("§6Hikabrain");
        name.add("§4H§6ikabrain");
        name.add("§6H§4i§6kabrain");
        name.add("§6Hi§4k§6abrain");
        name.add("§6Hik§4a§6brain");
        name.add("§6Hika§4b§6rain");
        name.add("§6Hikab§4r§6ain");
        name.add("§6Hikabr§4a§6in");
        name.add("§6Hikabra§4i§6n");
        name.add("§6Hikabrai§4n");

        name.add("§6Hikabrain");

        name.add("§6Hikabrai§4n");
        name.add("§6Hikabra§4i§6n");
        name.add("§6Hikabr§4a§6in");
        name.add("§6Hikab§4r§6ain");
        name.add("§6Hika§4b§6rain");
        name.add("§6Hik§4a§6brain");
        name.add("§6Hi§4k§6abrain");
        name.add("§6H§4i§6kabrain");
        name.add("§4H§6ikabrain");

        if(second == 5){
            second = 0;
            showName++;
        }

        if(showName == name.size()){
            showName = 0;
        }

        for (Entry<Player, ScoreboardSign> entry : board.entrySet()){
            entry.getValue().setObjectiveName(name.get(showName));
            if (main.getGameState() == GameState.WAITING){
                entry.getValue().setLine(0, "");
                entry.getValue().setLine(1, "§cGame statue : §4" + main.getGameState());
                entry.getValue().removeLine(2);
                entry.getValue().removeLine(3);
                entry.getValue().removeLine(4);
                entry.getValue().removeLine(5);
            }else if (main.getGameState() == GameState.STARTING){
                Player bluePlayer = Bukkit.getPlayer(playerInGame.get(0));
                Player redPlayer = Bukkit.getPlayer(playerInGame.get(1));
                entry.getValue().setLine(0, "");
                entry.getValue().setLine(1, "§cGame statue §f: §4" + main.getGameState());
                entry.getValue().setLine(2, "§0");
                if (entry.getKey() == bluePlayer){
                    entry.getValue().setLine(3, "§eYour opponent §f: §8" + redPlayer.getDisplayName());
                }else {
                    entry.getValue().setLine(3, "§eYour opponent §f: §8" + bluePlayer.getDisplayName());
                }
            }else if (main.getGameState() != GameState.WINNING){
                Player bluePlayer = Bukkit.getPlayer(playerInGame.get(0));
                Player redPlayer = Bukkit.getPlayer(playerInGame.get(1));
                int bluePoint = main.getPoint().get(bluePlayer.getName());
                int redPoint = main.getPoint().get(redPlayer.getName());
                entry.getValue().setLine(0, "");
                entry.getValue().setLine(1, "§cObjectif §f: §45 points");
                entry.getValue().setLine(2, "§0");
                entry.getValue().setLine(4, "§7");
                if (entry.getKey() == bluePlayer){
                    entry.getValue().setLine(3, "§eYour opponent §f: §8" + redPlayer.getDisplayName());
                    entry.getValue().setLine(5, "§2Score §f: §1" + bluePoint + " §f/ §4" + redPoint);
                }else {
                    entry.getValue().setLine(3, "§eYour opponent §f: §8" + bluePlayer.getDisplayName());
                    entry.getValue().setLine(5, "§2Score §f: §4" + redPoint + " §f/ §1" + bluePoint);
                }
            }
        }
        second ++;
    }
}
