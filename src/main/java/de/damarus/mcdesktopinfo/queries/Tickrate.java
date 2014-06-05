package de.damarus.mcdesktopinfo.queries;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;

public class Tickrate extends Query {

    private Poller poller;

    protected Tickrate(String query, boolean runOnRefresh) {
        super(query, runOnRefresh);

        poller = new Poller();

        if(!isDisabled()) {
            McDesktopInfo.log("Starting TPS counter thread...");
            Bukkit.getScheduler().scheduleSyncRepeatingTask(McDesktopInfo.getPluginInstance(), poller, 0, Poller.INTERVAL);
        }
    }

    @Override
    public JSONObject run(JSONObject params) {
        JSONObject answer = new JSONObject();
        // Round to 2 decimal places
        answer.put(getQueryString(), Math.round(poller.getTps() * 100) / 100.0 + "");
        return answer;
    }

    class Poller implements Runnable {

        public static final int INTERVAL = 40;

        private long lastRun = System.currentTimeMillis() - INTERVAL / 20 * 1000;
        private double tps;

        @Override
        public void run() {
            long now = System.currentTimeMillis();
            double delta = (now - lastRun) / 1000.0;

            tps = 0;
            if(delta != 0) tps = INTERVAL / delta;

            lastRun = now;
        }

        public double getTps() {
            return tps;
        }
    }
}
