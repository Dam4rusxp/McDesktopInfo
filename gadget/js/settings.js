var settings = new settingsObject();

var checkSettings = new Array("useCustomName", "useAutoRefresh")
var varSettings = new Array("refreshInterval", "connTimeout");
var stringSettings = new Array("serverName", "host", "bg", "adminPw", "textColor");

function initSettings() {
    System.Gadget.onSettingsClosing = settingsClosing;
    
    setupTabs();
    applySettings();
    
    nameBoxChanged();
    refreshBoxChanged();
}

// Is fired when the settings dialogue is closed
function settingsClosing(event) {
     if (event.closeAction == event.Action.commit) {
         for(i = 0; i < checkSettings.length; i++) settings[checkSettings[i]] = document.getElementById(checkSettings[i]).checked;
         for(i = 0; i < varSettings.length; i++) settings[varSettings[i]] = document.getElementById(varSettings[i]).value;
         for(i = 0; i < stringSettings.length; i++) settings[stringSettings[i]] = document.getElementById(stringSettings[i]).value;
         saveSettings();
     }
}

function settingsObject() {
    this.load = loadSettings;
    this.save = saveSettings;
    
    this["useCustomName"]   = false;
    this["useAutoRefresh"]  = false;
    this["refreshInterval"] = 30;
    this["connTimeout"]     = 5;
    
    this["serverName"] = "A Bukkit Server";
    this["host"]       = "";
    this["bg"]         = "bg_grass.png";
    this["adminPw"]    = "";
    this["textColor"]  = "#FFFFFF";
}

function loadSettings() {
    // Load from disk
    if(System.Gadget.Settings.read("settingsExist")) {
        for(i = 0; i < checkSettings.length; i++) settings[checkSettings[i]] = System.Gadget.Settings.read(checkSettings[i]);
        for(i = 0; i < varSettings.length; i++) settings[varSettings[i]] = System.Gadget.Settings.read(varSettings[i]);
        for(i = 0; i < stringSettings.length; i++) settings[stringSettings[i]] = System.Gadget.Settings.readString(stringSettings[i]);
    }
}

function saveSettings() {
    // Save to disk
    for(i = 0; i < checkSettings.length; i++) System.Gadget.Settings.write(checkSettings[i], settings[checkSettings[i]]);
    for(i = 0; i < varSettings.length; i++) System.Gadget.Settings.write(varSettings[i], settings[varSettings[i]]);
    for(i = 0; i < stringSettings.length; i++) System.Gadget.Settings.writeString(stringSettings[i], settings[stringSettings[i]]);
    System.Gadget.Settings.write("settingsExist", true);
}

function applySettings() {
    // Apply Settings to settings dialog
    loadSettings();
    for(i = 0; i < checkSettings.length; i++) document.getElementById(checkSettings[i]).checked = settings[checkSettings[i]];
    for(i = 0; i < varSettings.length; i++) document.getElementById(varSettings[i]).value = settings[varSettings[i]];
    for(i = 0; i < stringSettings.length; i++) document.getElementById(stringSettings[i]).value = settings[stringSettings[i]];
}

function nameBoxChanged() {
    document.getElementById("serverName").disabled = !useCustomName.checked;
}

function refreshBoxChanged() {
    document.getElementById("refreshInterval").disabled = !useAutoRefresh.checked;
}

function addPortNumber(ip) {
    // If IP is not undefined, not empty and doesn't contain a port number, add the default one
    if(ip != undefined && ip != "" && !ip.match(".*:[0-9]{1,5}")) return ip.concat(":6868");
    return ip;
}