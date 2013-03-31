package de.damarus.mcdesktopinfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bukkit.plugin.Plugin;

public class PasswordSystem {

    public static String generateMD5(String x) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] digestBytes = md.digest(x.getBytes());
            String digestString = "";
            int low, hi;

            for(int i = 0; i < digestBytes.length; i++) {
                low = (digestBytes[i] & 0x0f);
                hi = ((digestBytes[i] & 0xf0) >> 4);
                digestString += Integer.toHexString(hi);
                digestString += Integer.toHexString(low);
            }

            return digestString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean checkAdminPW(String pwHash) {
        Plugin plugin = McDesktopInfo.getPluginInstance();

        if(pwHash == null || pwHash.isEmpty()) return false;
        if(plugin.getConfig().getString("adminPw").isEmpty()) return false;

        // If password was delivered as clear text first create a MD5 from it
        if(pwHash.length() != 32) pwHash = generateMD5(pwHash);

        // Check if password is correct
        if(plugin.getConfig().getString("adminPw").equals(pwHash)) return true;

        return false;
    }

    public static void digestPWs() {
        Plugin plugin = McDesktopInfo.getPluginInstance();

        if(plugin.getConfig().getString("adminPw").length() != 32) {
            plugin.getConfig().set("adminPw", generateMD5(plugin.getConfig().getString("adminPw")));
            plugin.saveConfig();
        }
    }
}
