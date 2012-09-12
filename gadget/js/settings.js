var mySettings = new mcSettings();

// Is fired when the settings dialogue is closed
function settingsClosing(event) {
	if (event.closeAction == event.Action.commit) {
		saveToMemory();
	}
}

function mcSettings() {
    // Functions that load and save this element
    this.save = saveToDisk;
    this.load = loadFromDisk;

    this.useCustomName   = false;
    this.serverName      = "A Bukkit Server"
    this.host            = "";
    this.bg              = "bg_grass.png"
    this.useAutoRefresh  = false;
    this.refreshInterval = 30 * 1000; // 30 seconds
    this.adminPw         = "";
}

function saveToMemory() {
    // Read settings from settings dialogue and save them in mySettings
    mySettings.useCustomName   = useCustomName.checked;
    mySettings.serverName      = serverNameInp.value;
    mySettings.host            = hostInp.value;
    mySettings.bg              = bgSel.value;
    mySettings.useAutoRefresh  = useAutoRefresh.checked;
    mySettings.refreshInterval = refreshIntervalInp.value * 1000;
    mySettings.adminPw         = adminPwInp.value;
    
    // Save settings to disk
    mySettings.save();
}

function loadSettings() {
    // Load settings from disk
    mySettings.load();
    
    // Apply loaded settings to HTML elements
    useCustomName.checked    = mySettings.useCustomName;
    serverNameInp.value      = mySettings.serverName;
    hostInp.value            = mySettings.host;
    bgSel.value              = mySettings.bg;
    useAutoRefresh.checked   = mySettings.useAutoRefresh;
    refreshIntervalInp.value = mySettings.refreshInterval / 1000;
    adminPwInp.value         = mySettings.adminPw;
    
    nameBoxChanged();
    refreshBoxChanged();
    
    System.Gadget.onSettingsClosing = settingsClosing;
}

function saveToDisk() {
    System.Gadget.Settings.write      ("settingsExist"  , true);
    System.Gadget.Settings.write      ("useCustomName"  , this.useCustomName);
    System.Gadget.Settings.writeString("serverName"     , this.serverName);
    System.Gadget.Settings.writeString("host"           , this.host);
    System.Gadget.Settings.writeString("bg"             , this.bg);
    System.Gadget.Settings.write      ("useAutoRefresh" , this.useAutoRefresh);
    System.Gadget.Settings.write      ("refreshInterval", this.refreshInterval);
    System.Gadget.Settings.writeString("adminPw"        , this.adminPw);
}

function loadFromDisk() {
    if(System.Gadget.Settings.read("settingsExist")) {
        this.useCustomName   = System.Gadget.Settings.read      ("useCustomName");
        this.serverName      = System.Gadget.Settings.readString("serverName");
        this.host            = System.Gadget.Settings.readString("host");
        this.bg              = System.Gadget.Settings.readString("bg");
        this.useAutoRefresh  = System.Gadget.Settings.read      ("useAutoRefresh");
        this.refreshInterval = System.Gadget.Settings.read      ("refreshInterval");
        this.adminPw         = System.Gadget.Settings.readString("adminPw");
    }
}

function nameBoxChanged() {
    serverNameInp.disabled = !useCustomName.checked;
}

function refreshBoxChanged() {
    refreshIntervalInp.disabled = !useAutoRefresh.checked;
}

function addPortNumber(ip) {
	// If IP is not undefined, not empty and doesn't contain a port number, add the default one
	if(ip != undefined && ip != "" && !ip.match(".*:[0-9]{1,5}")) return ip.concat(":6868");
	return ip;
}