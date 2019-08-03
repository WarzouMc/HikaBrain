package fr.WarzouMc.hikabrain.graphic.scoreboard;

import fr.WarzouMc.hikabrain.main.Main;
import org.bukkit.entity.Player;

import java.util.Map;

public class Creater {

    private Main main;

    public Creater(Main main){this.main = main;}

    public void create(Player player){
        ScoreboardSign scoreboardSign = new ScoreboardSign(player, "§6Hikabrain");

        Map<Player, ScoreboardSign> board = main.getBoard();

        scoreboardSign.create();
        scoreboardSign.setLine(0, "");

        board.put(player, scoreboardSign);
        main.setBoard(board);
    }

    public void destroy(Player player){
        Map<Player, ScoreboardSign> board = main.getBoard();
        if(!board.containsKey(player))
            return;
        board.remove(player);
        main.setBoard(board);
    }

}
