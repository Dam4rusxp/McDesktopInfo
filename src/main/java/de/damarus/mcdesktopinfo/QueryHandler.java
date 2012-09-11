/*
 *  McDesktopInfo - A Bukkit plugin + Windows Sidebar Gadget
 *  Copyright (C) 2012  Damarus
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.damarus.mcdesktopinfo;

import java.util.HashMap;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class QueryHandler {

    private FileConfiguration       config;
    private Server                  server;
    private HashMap<String, String> values;

    public QueryHandler(Server server) {
        this.config = server.getPluginManager().getPlugin(Config.PLUGIN_NAME).getConfig();
        this.server = server;

        values = new HashMap<String, String>();

        updateValues();
    }

    public void updateValues() {
        values.put("playerCount", server.getOnlinePlayers().length + " / " + server.getMaxPlayers());
        values.put("serverName", server.getServerName());
        values.put("serverVersion", server.getVersion());
        values.put("pluginVersion", server.getPluginManager().getPlugin(Config.PLUGIN_NAME).getDescription().getVersion());
    }

    public String get(String query, HashMap<String, String> params) {
        // Return nothing if a password is required but not given with the query or is wrong
        if(config.getBoolean("enforcePassword") && !params.containsKey("adminPw") || (params.containsKey("adminPw") && !PasswordSystem.checkAdminPW(params.get("adminPw")))) return "";

        // Handle more complex querys
        if(query.equals("kick")) doKick(params);
        if(query.equals("playerList")) return getPlayerList(params);

        // Handle simpler querys
        if(values.containsKey(query)) return values.get(query);

        // ...else return an empty string
        return "";
    }

    public boolean doKick(HashMap<String, String> params) {
        // Report to serverlog that a kick was queryed
        McDesktopInfo.log("The IP " + params.get("gadgetIp") + " sent a query to kick the player " + params.get("player") + " using the password " + params.get("adminPw"));
        if(!params.containsKey("adminPw")) {
            McDesktopInfo.log("No adminPw specified in the config! Skipping kick.");
        } else if(PasswordSystem.checkAdminPW(params.get("adminPw"))) {
            Player player = server.getPlayer(params.get("player"));
            if(player != null) {
                player.kickPlayer("Kicked with McDesktopInfo");
                return true;
            }
        }
        return false;
    }

    public String getPlayerList(HashMap<String, String> params) {
        Player[] players = server.getOnlinePlayers();

        String output = "";
        for(Player p : players) {
            output += "+" + p.getName();
        }

        output = output.replaceFirst("[+]", "");
        return output;
    }
}