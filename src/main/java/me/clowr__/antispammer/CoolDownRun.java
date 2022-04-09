package me.clowr__.antispammer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class CoolDownRun  implements CommandExecutor , Listener {


    //Our plugin
    public AntiSpammer plugin;//We do this in order to make an instace of the polugin in the runnable state later in the code.
    private HashMap<UUID, Long> in_cooldown;
    private static final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
    private static int cooldown_time = 0;
    private static int storeTime = 0;
    private static boolean noChat = false;
    private static boolean continueCoolDown =false;
    private static boolean running = false;
    private static int kill = 0;


    //Construct the database
    public CoolDownRun(AntiSpammer plugin) {

        this.in_cooldown = new HashMap<UUID, Long>();
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            Player target = Bukkit.getPlayer(args[0]);
            //If the user is not a player, but omsehting else, then do not execute this command.
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Oops. Sorry. Only Players can run this command.");
                return false;
            }

            if(command.getName().equals("cEnable"))
            {
                Player player = (Player) sender;//Command sender

                if (playerOnline(args[0])) {




                    if (!in_cooldown.containsKey(target.getUniqueId())) {
                        this.cooldown_time = Integer.parseInt(args[1]);
                        storeTime = cooldown_time;
                    }

                    //If the player is already in the cooldown list and runs the command again while in the cooldown, the then give them the time left.
                    if (!in_cooldown.containsKey(target.getUniqueId()) || (int) ((System.currentTimeMillis() - in_cooldown.get(target.getUniqueId())) / 1000) > this.cooldown_time) {

                        noChat = true;
                        sender.sendMessage(ChatColor.GOLD + "Player " + target.getName().toString() + " has now cool down of " + ChatColor.AQUA + cooldown_time + " seconds. To deactivate, type /cDisable {player}");
                        this.in_cooldown.put(target.getUniqueId(), System.currentTimeMillis());//Add time when command was last ran to later do calculations

                              new BukkitRunnable(){

                                        //While running the timer and cooldown
                                      @Override
                                      public void run() {

                                          if(!running)
                                          {
                                              if(kill == 1)
                                              {
                                                  cancel();//Cancel the runnable
                                              }
                                              //Timer function to keep track of cooldown
                                              target.sendMessage(ChatColor.RED +  "Try chatting again");
                                              noChat = false;//After the cooldown interva; finishes, the user cna chat again
                                              running = true;//Pause

                                          }


                                    }
                                }.runTaskTimer(plugin , (long)cooldown_time * 20 , (long)cooldown_time * 20);//Time and delay time



                    } else {

                        //Pass
                    }

                } else {
                    sender.sendMessage(ChatColor.RED.BOLD + "Player must be online to run this command");
                }

               }
            else if (command.getName().equals("cDisable"))
            {
                //This will reset all the timers and variables controlling the cooldown

                cooldown_time = 0;
                kill =1;
                noChat = false;//
                in_cooldown.remove(target.getUniqueId());


            }

            return true;
    }




    //Helper method to check if the player trying to be cooled is a  player currently online.
    private static boolean playerOnline(String playerName) {
        for (Player p : onlinePlayers) {
            if (playerName.equals(p.getName())) {
                return true;
                //return the function because the player is online
            }
        }

        return false;
    }

//Event for user chatting in the server
    @EventHandler
    public void chatMessage(AsyncPlayerChatEvent e)
    {

        if(in_cooldown.containsKey(e.getPlayer().getUniqueId()) && running)
        {
            e.setCancelled(noChat);
            e.getPlayer().sendMessage("You must wait "  + (cooldown_time - (int) ((System.currentTimeMillis() - in_cooldown.get(e.getPlayer().getUniqueId())) / 1000) + " seconds "));
        }
        else {

            cooldown_time = storeTime;//Wait after the event fires and reset the time to new cooldown.
            running = false;//Caontinue running
            noChat = true;//Cnt chat because the cooldown is active again
        }


    }

}














