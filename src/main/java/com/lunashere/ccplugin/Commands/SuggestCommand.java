package com.lunashere.ccplugin.Commands;

import com.lunashere.ccplugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class SuggestCommand implements CommandExecutor {

    private HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
    private int cooldownTime = 60;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Get Player
        Player p = (Player) sender;
        // Check if player has a cooldown
        if(cooldown.containsKey(p.getUniqueId())){
            long timeLeft = (cooldown.get(p.getUniqueId()) / 1000) + cooldownTime - (System.currentTimeMillis() / 1000);
            // Check if there is still time on the cooldown
            if(timeLeft > 0){
                sender.sendMessage("§cPlease wait before sending another suggestion.");
                return true;
            }
        }
        // Ensures there is at least one arg
        if(args.length < 1){
            sender.sendMessage("§cPlease provide a suggestion.");
            return true;
        }

        try {

            URL url = new URL(Main.getPlugin(Main.class).URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"secret\":\""+ Main.getPlugin(Main.class).secret +"\",\"username\":\""+sender.getName()+"\",\"suggestion\":\""+String.join(" ", args)+"\"}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED && conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        sender.sendMessage("Suggestion should have been sent into our discord!  Thanks for the suggestion!");
        cooldown.put(p.getUniqueId(), System.currentTimeMillis());
        return true;
    }
}
