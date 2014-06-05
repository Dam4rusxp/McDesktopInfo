package de.damarus.mcdesktopinfo.queries;

import org.json.simple.JSONObject;

public class Refresh extends Query {

    protected Refresh(String query, boolean runOnRefresh) {
        super(query, runOnRefresh);
    }

    @Override
    public JSONObject run(JSONObject params) {
        JSONObject answer = new JSONObject();
        for(QueryEnum e : QueryEnum.values()) {
            Query q = e.getQueryObj();
            if (q.runOnRefresh()) answer.putAll(q.runSecure(params));
        }
        return answer;
    }
}
