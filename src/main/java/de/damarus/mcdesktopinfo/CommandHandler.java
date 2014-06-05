package de.damarus.mcdesktopinfo;

import de.damarus.mcdesktopinfo.queries.Query.QueryEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandler implements CommandExecutor {

    JavaPlugin plugin;

    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            helpMessage(sender);
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("help")) {
                helpMessage(sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("reload")) {
                if(sender.hasPermission("mcdesktopinfo.admin")) {
                    respond(sender, "Reloading config...");
                    plugin.reloadConfig();
                    respond(sender, "Done!");
                    return true;
                }
            }

            if(args[0].equalsIgnoreCase("port")) {
                if(sender.hasPermission("mcdesktopinfo.getport")) {
                    respond(sender, plugin.getConfig().getInt("socket-port") + "");
                }
            }

            if(args[0].equalsIgnoreCase("queries")) {
                if(sender.hasPermission("mcdesktopinfo.listQueries")) {
                    QueryEnum[] queries = QueryEnum.values();
                    String cmds = "";
                    for(int i = 0; i < queries.length; i++) {
                        if(!queries[i].getQueryObj().isDisabled()) {
                            cmds += ", " + queries[i].getQueryObj().getQueryString();
                            if(!queries[i].getQueryObj().isUserExecutable()) cmds += "[A]";
                        }
                    }

                    respond(sender, cmds.length() > 2 ? cmds.substring(2) : "none");
                }
            }
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("setPassword")) {
                if(sender.hasPermission("mcdesktopinfo.admin")) {
                    respond(sender, "Changing the admin password...");

                    plugin.getConfig().set("adminPw", args[1]);
                    PasswordSystem.digestPWs();

                    respond(sender, "Done!");
                }
            }
        }
        return false;
    }

    public void helpMessage(CommandSender sender) {
        // @formatter:off
        respond(sender,
            "---McDesktopInfo help message---\n" +
            "<...> - required argument | [...] - optional argument\n" +
            "/mcdi help                       Display this message\n" +
            "/mcdi cmds                       List enabled queries\n" +
            "/mcdi port             Display the port of the socket\n" +
            "/mcdi reload                        Reload the config\n" +
            "/mcdi setPassword <pw>       Set a new admin password\n");
        // @formatter:on
    }

    public static void respond(CommandSender sender, String message) {
        sender.sendMessage("[" + McDesktopInfo.PLUGIN_NAME + "] " + message);
    }
}
