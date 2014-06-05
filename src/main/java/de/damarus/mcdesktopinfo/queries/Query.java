package de.damarus.mcdesktopinfo.queries;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import org.json.simple.JSONObject;

public abstract class Query {

    private final String query;
    private final boolean runOnRefresh;

    protected Query(String query, boolean runOnRefresh) {
        this.query = query;
        this.runOnRefresh = runOnRefresh;
    }

    public abstract JSONObject run(JSONObject params);

    public String getQueryString() {
        return query;
    }

    public boolean isDisabled() {
        return McDesktopInfo.getPluginInstance().getConfig().getStringList("disabledQueries").contains(getQueryString());
    }

    public boolean isUserExecutable() {
        return McDesktopInfo.getPluginInstance().getConfig().getStringList("userQueries").contains(getQueryString());
    }

    public boolean runOnRefresh() {
        return runOnRefresh;
    }

    public enum QueryEnum {
        KICK(new Kick("kick", false)),
        PLAYERCOUNT(new Playercount("playerCount", true)),
        MEM(new Mem("mem", true)),
        PLAYERLIST(new PlayerList("playerList", true)),
        SERVERNAME(new Servername("serverName", true)),
        SERVERVERSION(new ServerVersion("serverVersion", true)),
        PLUGINVERSION(new PluginVersion("pluginVersion", true)),
        TICKRATE(new Tickrate("tickrate", true)),
        REFRESH(new Refresh("refresh", false));

        private Query queryObj;

        QueryEnum(Query queryObj) {
            this.queryObj = queryObj;
        }

        public Query getQueryObj() {
            return queryObj;
        }
    }
}
