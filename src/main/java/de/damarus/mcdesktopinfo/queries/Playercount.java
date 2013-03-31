package de.damarus.mcdesktopinfo.queries;

import java.util.HashMap;

import org.bukkit.Server;

import de.damarus.mcdesktopinfo.McDesktopInfo;

public class Playercount extends Query {

    protected Playercount(String query) {
        super(query, true);
    }

    @Override
    protected String exec(HashMap<String, String> params) {
        Server server = McDesktopInfo.getPluginInstance().getServer();
        return server.getOnlinePlayers().length + " / " + server.getMaxPlayers();
    }
}
