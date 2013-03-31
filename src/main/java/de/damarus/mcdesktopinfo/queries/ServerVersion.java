package de.damarus.mcdesktopinfo.queries;

import java.util.HashMap;

import de.damarus.mcdesktopinfo.McDesktopInfo;

public class ServerVersion extends Query {

    protected ServerVersion(String query) {
        super(query, true);
    }

    @Override
    protected String exec(HashMap<String, String> params) {
        return McDesktopInfo.getPluginInstance().getServer().getBukkitVersion();
    }
}
