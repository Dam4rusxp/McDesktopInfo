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

import de.damarus.mcdesktopinfo.McDesktopInfo;

public abstract class Query {

    private long lastExec = 0;
    private String lastValue = "";

    private final String query;
    private final boolean hasTimeout;

    protected Query(String query, boolean hasTimeout) {
        this.query = query;
        this.hasTimeout = hasTimeout;
    }

    protected abstract String exec(HashMap<String, String> params);

    public String execute(HashMap<String, String> params) {
        if(hasTimeout() && System.currentTimeMillis() - lastExec < McDesktopInfo.getPluginInstance().getConfig().getInt("valueTimeout")) return lastValue;
        return forceExecute(params);
    }

    public String forceExecute(HashMap<String, String> params) {
        lastExec = System.currentTimeMillis();
        return lastValue = exec(params);
    }

    public String getQuery() {
        return query;
    }

    public boolean hasTimeout() {
        return hasTimeout;
    }

    public void resetTimeout() {
        lastExec = 0;
    }

    public boolean isAdminOnly() {
        return McDesktopInfo.getPluginInstance().getConfig().getStringList("adminQueries").contains(getQuery());
    }

    public boolean isDisabled() {
        return McDesktopInfo.getPluginInstance().getConfig().getStringList("disabledQueries").contains(getQuery());
    }

    public boolean isUserExecutable() {
        return McDesktopInfo.getPluginInstance().getConfig().getStringList("userQueries").contains(getQuery());
    }

    public enum QueryEnum {
        KICK(new Kick("kick")),
        PLAYERCOUNT(new Playercount("playerCount")),
        MEM(new Mem("mem")),
        PLAYERLIST(new PlayerList("playerList")),
        SERVERNAME(new Servername("serverName")),
        SERVERVERSION(new ServerVersion("serverVersion")),
        PLUGINVERSION(new PluginVersion("pluginVersion")),
        TICKRATE(new Tickrate("tickrate"));

        private Query queryObj;

        QueryEnum(Query queryObj) {
            this.queryObj = queryObj;
        }

        public Query getQueryObj() {
            return queryObj;
        }
    }
}
