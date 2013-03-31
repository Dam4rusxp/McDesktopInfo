package de.damarus.mcdesktopinfo.queries;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.bukkit.entity.Player;

import de.damarus.mcdesktopinfo.McDesktopInfo;

public class PlayerList extends Query {

    protected PlayerList(String query) {
        super(query, true);
    }

    @Override
    protected String exec(HashMap<String, String> params) {
        Player[] players = McDesktopInfo.getPluginInstance().getServer().getOnlinePlayers();

        String playerList = "";
        for(Player p : players) {
            try {
                playerList += "+" + URLEncoder.encode(p.getName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        playerList = playerList.replaceFirst("[+]", "");
        return playerList;
    }
}
