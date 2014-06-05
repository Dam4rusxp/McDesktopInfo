package de.damarus.mcdesktopinfo.queries;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import org.bukkit.Server;
import org.json.simple.JSONObject;

public class Playercount extends Query {

    protected Playercount(String query, boolean runOnRefresh) {
        super(query, runOnRefresh);
    }

    @Override
    public JSONObject run(JSONObject params) {
        JSONObject answer = new JSONObject();
        Server server = McDesktopInfo.getPluginInstance().getServer();
        answer.put(getQueryString(), server.getOnlinePlayers().length + " / " + server.getMaxPlayers());
        return answer;
    }
}
