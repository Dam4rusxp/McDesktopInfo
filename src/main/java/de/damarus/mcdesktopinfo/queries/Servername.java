package de.damarus.mcdesktopinfo.queries;

import java.util.HashMap;

import de.damarus.mcdesktopinfo.McDesktopInfo;

public class Servername extends Query {

    protected Servername(String query) {
        super(query, true);
    }

    @Override
    protected String exec(HashMap<String, String> params) {
        return McDesktopInfo.getPluginInstance().getServer().getServerName();
    }

}
