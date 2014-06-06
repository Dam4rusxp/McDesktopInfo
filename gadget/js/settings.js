var settings = {
    values: {
        useCustomName: false,
        useAutoRefresh: false,
        refreshInterval: 30,
        connTimeout: 5,

        serverName: "A Bukkit Server",
        host: "",
        bg: "bg_grass.png",
        adminPw: "",
        textColor: "#FFFFFF",

        enabledQueries: "playerCount;pluginVersion;serverVersion;mem;tickrate",
        disabledQueries: ""
    },

    initSettings: function() {
        System.Gadget.onSettingsClosing = settingsClosing;

        this.loadSettings();
        setupTabs();
        applySettings();
    },

    loadSettings: function() {
        // Load from disk
        if(System.Gadget.Settings.read("settingsExist") === true) {
            for (var key in this.values) this.values[key] = System.Gadget.Settings.read(key);
        }
    },


    saveSettings: function() {
        // Save to disk
        for (var key in this.values) {
            if (typeof this.values[key] === "string") System.Gadget.Settings.writeString(key, this.values[key]);
            else System.Gadget.Settings.write(key, this.values[key]);
        }
        System.Gadget.Settings.write("settingsExist", true);
    }
};

// Apply Settings to settings dialog
function applySettings() {
    for (var key in settings.values) {
        var sElement = document.getElementById(key);
        var value = settings.values[key];

        // Decide how the value should be applied by analyzing the target element
        if (sElement.tagName === "INPUT" && sElement.type === "checkbox") sElement.checked = value;
        else if ((sElement.tagName === "INPUT" && (sElement.type === "text" || sElement.type === "password")) || (sElement.tagName === "SELECT" && sElement.size < 2)) sElement.value = value;
        else if (sElement.tagName === "SELECT" && sElement.size >= 2 && value !== "") {
            value = value.split(";");
            for (var i = 0; i < value.length; i++) {
                var option = document.createElement("option");
                option.value = option.innerHTML = value[i];
                sElement.appendChild(option);
            }
        }
    }

    nameBoxChanged();
    refreshBoxChanged();
}

// Read all settings from the document
function settingsClosing(event) {
    if(event.closeAction == event.Action.commit) {
        for(var key in settings.values) {
            var sElement = document.getElementById(key);
            var value = "";
            if (sElement.tagName === "INPUT" && sElement.type === "checkbox") value = sElement.checked;
            else if ((sElement.tagName === "INPUT" && (sElement.type === "text" || sElement.type === "password")) || (sElement.tagName === "SELECT" && sElement.size < 2)) value = sElement.value;
            else if (sElement.tagName === "SELECT" && sElement.size >= 2) {
                var temp = "";
                for (var i = 0; i < sElement.options.length; i++) {
                    temp += ";" + sElement.options[i].value;
                }
                value = temp.substr(1);
            }

            settings.values[key] = value;
        }

        settings.saveSettings();
    }
}

function nameBoxChanged() {
    document.getElementById("serverName").disabled = !useCustomName.checked;
}

function refreshBoxChanged() {
    document.getElementById("refreshInterval").disabled = !useAutoRefresh.checked;
}

function addPortNumber(ip) {
    // If IP is not undefined, not empty and doesn't contain a port number, add the default one
    if(typeof ip !== "undefined" && ip !== "" && !ip.match(".*:[0-9]{1,5}")) ip = ip.concat(":6868");
    return ip;
}

function up() {
    var enabledQueries = document.getElementById("enabledQueries");
    var disabledQueries = document.getElementById("disabledQueries");

    if(enabledQueries.selectedIndex != -1) {
        var option = enabledQueries.children[enabledQueries.selectedIndex];
        enabledQueries.insertBefore(option, option.previousSibling);
    } else if(disabledQueries.selectedIndex != -1) {
        var option = disabledQueries.children[disabledQueries.selectedIndex];
        disabledQueries.insertBefore(option, option.previousSibling);
    }
}

function down() {
    var enabledQueries = document.getElementById("enabledQueries");
    var disabledQueries = document.getElementById("disabledQueries");

    if(enabledQueries.selectedIndex != -1) {
        var option = enabledQueries.children[enabledQueries.selectedIndex];
        if(option.nextSibling) {
            enabledQueries.insertBefore(option, option.nextSibling.nextSibling);
        } else {
            enabledQueries.insertBefore(option, enabledQueries.firstChild);
        }
    } else if(disabledQueries.selectedIndex != -1) {
        var option = disabledQueries.children[disabledQueries.selectedIndex];
        if(option.nextSibling) {
            disabledQueries.insertBefore(option, option.nextSibling.nextSibling);
        } else {
            disabledQueries.insertBefore(option, disabledQueries.firstChild);
        }
    }
}

function right() {
    var enabledQueries = document.getElementById("enabledQueries");
    var disabledQueries = document.getElementById("disabledQueries");

    if(enabledQueries.selectedIndex != -1) {
        var option = enabledQueries.children[enabledQueries.selectedIndex];
        if(--enabledQueries.selectedIndex < 0) enabledQueries.selectedIndex = 1;
        disabledQueries.appendChild(option);
        option.selected = false;
    }
}

function left() {
    var enabledQueries = document.getElementById("enabledQueries");
    var disabledQueries = document.getElementById("disabledQueries");

    if(disabledQueries.selectedIndex != -1) {
        var option = disabledQueries.children[disabledQueries.selectedIndex];
        if(--disabledQueries.selectedIndex < 0) disabledQueries.selectedIndex = 1;
        enabledQueries.appendChild(option);
        option.selected = false;
    }
}
