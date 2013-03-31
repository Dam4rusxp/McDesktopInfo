package de.damarus.mcdesktopinfo.queries;

import java.util.HashMap;

public class Mem extends Query {

    protected Mem(String query) {
        super(query, true);
    }

    @Override
    protected String exec(HashMap<String, String> params) {
        Runtime runtime = Runtime.getRuntime();

        long used = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long max = runtime.maxMemory() / 1024 / 1024;

        return used + "MB / " + (max == Long.MAX_VALUE ? "inf" : max + "MB");
    }
}
