package fr.WarzouMc.hikabrain.gameLoop;

import fr.WarzouMc.hikabrain.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WinLoop extends BukkitRunnable {

    private Main main;
    public WinLoop(Main main){this.main = main;}

    private int timer = 10;

    @Override
    public void run() {
        String winner = main.getWinner();

        int power = new Random().nextInt(2);

        int x = new Random().nextInt(20);
        int z = new Random().nextInt(8);

        int signe = new Random().nextInt(2);

        int X = x;
        int Z = z;

        if (signe == 1){
            X = -x;
        }

        signe = new Random().nextInt(2);

        if (signe == 1){
            Z = -z;
        }

        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(winner)) && Bukkit.getPlayer(winner).getDisplayName().startsWith("§1")){
            FireworkEffect fireworkEffect = FireworkEffect.builder()
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(Color.BLUE)
                    .flicker(true)
                    .trail(true)
                    .build();

            Firework firework = (Firework)Bukkit.getWorlds().get(0).spawnEntity(new Location(Bukkit.getWorlds().get(0), X, 50, Z), EntityType.FIREWORK);
            firework.detonate();
            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            fireworkMeta.setPower(power);
            fireworkMeta.addEffect(fireworkEffect);
            firework.setFireworkMeta(fireworkMeta);
        }else {
            FireworkEffect fireworkEffect = FireworkEffect.builder()
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(Color.RED)
                    .flicker(true)
                    .trail(true)
                    .build();

            Firework firework = (Firework)Bukkit.getWorlds().get(0).spawnEntity(new Location(Bukkit.getWorlds().get(0), X, 50, Z), EntityType.FIREWORK);
            firework.detonate();
            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            fireworkMeta.setPower(power);
            fireworkMeta.addEffect(fireworkEffect);
            firework.setFireworkMeta(fireworkMeta);
        }

        if (timer == 0){
           Bukkit.getServer().reload();
           cancel();
        }
        timer--;
    }
}
