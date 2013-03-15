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

package de.damarus.mcdesktopinfo.queries;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

import de.damarus.mcdesktopinfo.McDesktopInfo;

public abstract class Query {

    private long lastExec = 0;
    private String lastValue = "";

    private final Plugin plugin = McDesktopInfo.getPluginInstance();
    private final String query;
    private final boolean hasTimeout;

    protected Query(String query, boolean hasTimeout) {
        this.query = query;
        this.hasTimeout = hasTimeout;

        if(!(getConfig().getStringList("userQueries").contains(query) || getConfig().getStringList("adminQueries").contains(query) || getConfig().getStringList("disabledQueries").contains(query))) {
            List<String> disabled = getConfig().getStringList("disabledQueries");
            disabled.add(query);
            getConfig().set("disabledQueries", disabled);
        }
    }

    protected abstract String exec(HashMap<String, String> params);

    public String execute(HashMap<String, String> params) {
        if(hasTimeout() && System.currentTimeMillis() - lastExec < getPlugin().getConfig().getInt("valueTimeout")) return lastValue;
        return forceExecute(params);
    }

    public String forceExecute(HashMap<String, String> params) {
        lastExec = System.currentTimeMillis();
        return lastValue = exec(params);
    }

    public Configuration getConfig() {
        return getPlugin().getConfig();
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getQuery() {
        return query;
    }

    public Server getServer() {
        return getPlugin().getServer();
    }

    public boolean hasTimeout() {
        return hasTimeout;
    }

    public void resetTimeout() {
        lastExec = 0;
    }

    public boolean isAdminOnly() {
        return getPlugin().getConfig().getStringList("adminQueries").contains(getQuery());
    }

    public boolean isDisabled() {
        return getPlugin().getConfig().getStringList("disabledQueries").contains(getQuery());
    }

    public boolean isUserExecutable() {
        return getPlugin().getConfig().getStringList("userQueries").contains(getQuery());
    }
}
