package de.damarus.mcdesktopinfo;

import org.bukkit.plugin.java.JavaPlugin;
import de.damarus.mcdesktopinfo.socket.SocketListener;
import java.util.logging.Logger;

public class McDesktopInfo extends JavaPlugin {

    private static Logger logger;
    private Thread listenerThread;

    public void onEnable() {
        logger = getServer().getLogger();

        // TODO Write better script for writing/updating config
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        getConfig().set("VERSION", Config.PLUGIN_VERSION);

        PasswordSystem.setConfig(getConfig());
        PasswordSystem.digestPWs();
        saveConfig();
        reloadConfig();

        // That random looking number is the MD5 of "a"
        if(getConfig().getString("adminPw").equals("0cc175b9c0f1b6a831c399e269772661"))
            log("WARNING! The default password (\"a\") was not changed!");

        // Start the listener in a new thread to be able to do other things while listening
        listenerThread = new Thread(new SocketListener(getConfig().getInt("socket-port"), getServer()));
        listenerThread.start();

        log("Enabled!");
    }

    public void onDisable() {
        saveConfig();
        logger.info("Disabled!");
    }

    public static void log(String message) {
        logger.info("[" + Config.PLUGIN_NAME + "] " + message);
    }
}