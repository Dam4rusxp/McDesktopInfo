package de.damarus.mcdesktopinfo.queries;

import org.json.simple.JSONObject;

public class Mem extends Query {

    protected Mem(String query, boolean runOnRefresh) {
        super(query, runOnRefresh);
    }

    @Override
    public JSONObject run(JSONObject params) {
        JSONObject answer = new JSONObject();
        Runtime runtime = Runtime.getRuntime();

        long used = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long max = runtime.maxMemory() / 1024 / 1024;

        answer.put(getQueryString(), used + "MB / " + (max == Long.MAX_VALUE ? "inf" : max + "MB"));

        return answer;
    }
}
