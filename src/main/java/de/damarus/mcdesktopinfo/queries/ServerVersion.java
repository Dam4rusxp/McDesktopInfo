package de.damarus.mcdesktopinfo.queries;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import org.json.simple.JSONObject;

public class ServerVersion extends Query {

    protected ServerVersion(String query, boolean runOnRefresh) {
        super(query, runOnRefresh);
    }

    @Override
    public JSONObject run(JSONObject params) {
        JSONObject answer = new JSONObject();
        answer.put(getQueryString(), McDesktopInfo.getPluginInstance().getServer().getBukkitVersion());
        return answer;
    }
}
