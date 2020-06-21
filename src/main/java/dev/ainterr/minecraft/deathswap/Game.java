package dev.ainterr.minecraft.deathswap;

import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Random;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;


public final class Game extends JavaPlugin {
    public static boolean ALIVE = true;
    public static boolean DEAD = false;

    public HashMap<Player, Boolean> players = new HashMap<Player, Boolean>();

    private int interval = 300;

    private boolean running = false;

    public void startRound() {
        Bukkit.broadcastMessage("DeathSwap in " + this.interval + "s");

        getServer().getPluginManager().registerEvents(
            new DeathListener(this), this
        );

        new Countdown(this).runTaskTimer(this, this.interval * 20, 20);

        this.running = true;
    }

    public void endRound(boolean grace) {
        if(!grace) {
            Player[] players = (Player[]) this.players.keySet().toArray();

            if(players.length > 1) {
                Location first = players[0].getLocation();

                for(int i = 0; i < players.length; i++) {
                    if(i < players.length - 1) {
                        players[i].teleport(players[i+1].getLocation());
                    }
                    else {
                        players[i].teleport(first);
                    }
                }
            }
        }

        PlayerDeathEvent.getHandlerList().unregister(this);
        Bukkit.getScheduler().cancelTasks(this);

        this.running = false;
    }

    public void endRound() {
        this.endRound(false);
    }

    private void startGame() {
        for(Player player: Bukkit.getServer().getOnlinePlayers()) {
            this.players.put(player, this.ALIVE);
        }

        if(this.players.size() < 2) {
            Bukkit.broadcastMessage("you need at least two players for DeathSwap");
            return;
        }

        Bukkit.broadcastMessage("welcome to DeathSwap");

        Bukkit.broadcastMessage("players:");

        for(Player player: this.players.keySet()) {
            Bukkit.broadcastMessage("    " + player.getName());
        }

        this.startRound();
    }

    public void stopGame() {
        this.endRound(true);

        this.players.clear();

        Bukkit.broadcastMessage("DeathSwap game over");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("death-swap-start")) {
            if(this.running) {
                sender.sendMessage("DeathSwap has already started");
                return true;
            }

            this.startGame();

            return true;
        }
        else if(command.getName().equalsIgnoreCase("death-swap-stop")) {
            if(!this.running) {
                sender.sendMessage("you're not currently playing DeathSwap");
                return true;
            }

            this.stopGame();

            return true;
        }
        else if(command.getName().equalsIgnoreCase("death-swap-configure")) {
            if(this.running) {
                sender.sendMessage("a game of DeathSwap is already in progress - stop the game to change configuration values");
                return true;
            }

            if(args.length < 1) {
                return false;
            }

            switch (args[0]) {
                case "interval":
                    if(args.length != 2) {
                        return false;
                    }
                    
                    try {
                        this.interval = Integer.parseInt(args[1]);
                        sender.sendMessage("DeathSwap interval set to " + this.interval + "s");
                    }
                    catch(NumberFormatException error) {
                        sender.sendMessage("invalid interval value '"+ args[1] + "'");
                    }
                    break;
                default:
                    sender.sendMessage("unknown configuration parameter '" + args[0] + "'");
            }

            return true;
        }

        return false;
    }
}

class DeathListener implements Listener {
    private Game plugin;

    public DeathListener(Game plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        Player player = event.getEntity();

        this.plugin.players.put(player, this.plugin.DEAD);

        Bukkit.broadcastMessage(
            ChatColor.RED
            + player.getName() + " is out"
        );

        int alive = 0;
        for(Player p: this.plugin.players.keySet()) {
            if(this.plugin.players.get(p) == this.plugin.ALIVE) {
                alive++;
                player = p;
            }
        }

        if(alive < 2) {
            Bukkit.broadcastMessage(
                ChatColor.GOLD
                + player.getName() + " is the last one standing!"
            );

            this.plugin.stopGame();
        }
    }
}

class Countdown extends BukkitRunnable {
    private Game plugin;
    private int countdown;

    public Countdown(Game plugin) {
        this.plugin = plugin;
        this.countdown = 10;
    }

    @Override
    public void run() {
        if(this.countdown > 0) {
            Bukkit.broadcastMessage(
                "DeathSwap in " + countdown-- + " ..."
            );
        }
        else {
            new Swap(this.plugin).runTaskLater(this.plugin, 1 * 20);
            this.cancel();
        }
    }
}

class Swap extends BukkitRunnable {
    private Game plugin;

    public Swap(Game plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.endRound();
        this.plugin.startRound();
    }
}
