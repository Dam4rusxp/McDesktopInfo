package de.damarus.mcdesktopinfo.queries;

import java.util.HashMap;

import org.bukkit.entity.Player;

import de.damarus.mcdesktopinfo.McDesktopInfo;

public class Kick extends Query {

    protected Kick(String query) {
        super(query, false);
    }

    @Override
    public String exec(HashMap<String, String> params) {
        Player player = McDesktopInfo.getPluginInstance().getServer().getPlayer(params.get("player"));
        if(player != null) {
            player.kickPlayer("Kicked with McDesktopInfo");

            QueryEnum.PLAYERLIST.getQueryObj().resetTimeout();
            QueryEnum.PLAYERCOUNT.getQueryObj().resetTimeout();

            return params.get("player");
        }
        return "";
    }
}
