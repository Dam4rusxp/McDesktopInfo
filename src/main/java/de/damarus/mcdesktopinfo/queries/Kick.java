package de.damarus.mcdesktopinfo.queries;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class Kick extends Query {

    protected Kick(String query, boolean runOnRefresh) {
        super(query, runOnRefresh);
    }

    @Override
    public JSONObject run(JSONObject params) {
        JSONObject answer = new JSONObject();
        // TODO Do not use deprecated method getPlayer()
        Player player = McDesktopInfo.getPluginInstance().getServer().getPlayer((String)params.get("player"));
        if(player != null) {
            player.kickPlayer("Kicked with McDesktopInfo");

            answer.put(getQueryString(), params.get("player"));
        }
        return answer;
    }
}
