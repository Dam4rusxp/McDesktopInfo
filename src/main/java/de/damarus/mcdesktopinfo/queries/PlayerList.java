package de.damarus.mcdesktopinfo.queries;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PlayerList extends Query {

    protected PlayerList(String query, boolean runOnRefresh) {
        super(query, runOnRefresh);
    }

    @Override
    public JSONObject run(JSONObject params) {
        JSONObject answer = new JSONObject();
        Player[] players = McDesktopInfo.getPluginInstance().getServer().getOnlinePlayers();

        JSONArray playerList = new JSONArray();
        for (Player p : players) playerList.add(p.getName());
        answer.put("playerList", playerList);

        return answer;
    }
}
