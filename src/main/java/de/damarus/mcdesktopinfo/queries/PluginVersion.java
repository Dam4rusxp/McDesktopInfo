package de.damarus.mcdesktopinfo.queries;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import org.json.simple.JSONObject;

public class PluginVersion extends Query {

    protected PluginVersion(String query, boolean runOnRefresh) {
        super(query, runOnRefresh);
    }

    @Override
    public JSONObject run(JSONObject params) {
        JSONObject answer = new JSONObject();
        answer.put(getQueryString(), McDesktopInfo.getPluginInstance().getDescription().getVersion());
        return answer;
    }
}
