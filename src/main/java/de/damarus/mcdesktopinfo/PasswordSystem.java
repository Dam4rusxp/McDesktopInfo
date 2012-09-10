/*
 *  McDesktopInfo - A Bukkit plugin + Windows Sidebar Gadget
 *  Copyright (C) 2012  Damarus
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.damarus.mcdesktopinfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bukkit.configuration.file.FileConfiguration;

public class PasswordSystem {

    private static FileConfiguration config;

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
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean checkAdminPW(String pwHash) {
        if(pwHash == null) throw new NullPointerException();

        // If password was delivered as clear text first create a MD5 from it
        if(pwHash.length() != 32) pwHash = generateMD5(pwHash);

        // Check if password is correct
        if(config.getString("adminPw").equals(pwHash)) return true;

        return false;
    }

    public static void digestPWs() {
        if(config.getString("adminPw").length() != 32)
            config.set("adminPw", generateMD5(config.getString("adminPw")));
    }

    public static void setConfig(FileConfiguration fc) {
        config = fc;
    }
}
