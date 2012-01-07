package de.damarus.mcdesktopinfo;

import java.util.HashMap;
import org.bukkit.Server;

public class Values {

    public Server server;
    private HashMap<String, String> values;

    public Values(Server server) {
        this.server = server;

        values = new HashMap<String, String>();

        updateValues();
    }

    public void updateValues() {
        values.put("playerCount", server.getOnlinePlayers().length + " / " + server.getMaxPlayers());
        values.put("serverName", server.getName()); // Always returns "craftbukkit"
        values.put("serverVersion", server.getVersion());
        values.put("pluginVersion", Config.PLUGIN_VERSION);
    }

    public String get(String variable) {
        // If the requested key exists return it...
        if(values.containsKey(variable)) return values.get(variable);

        // ...else return an empty string
        return "";
    }
}