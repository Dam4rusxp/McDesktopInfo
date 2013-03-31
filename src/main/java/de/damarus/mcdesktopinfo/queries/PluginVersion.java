package de.damarus.mcdesktopinfo.queries;

import java.util.HashMap;

import de.damarus.mcdesktopinfo.McDesktopInfo;

public class PluginVersion extends Query {

    protected PluginVersion(String query) {
        super(query, true);
    }

    @Override
    protected String exec(HashMap<String, String> params) {
        return McDesktopInfo.getPluginInstance().getDescription().getVersion();
    }
}
