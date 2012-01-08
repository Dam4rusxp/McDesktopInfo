var fields = new Array("playerCount", "serverName", "pluginVersion");

// Simple for-each script
Array.prototype.foreach = function(callback) {
  for(var x=0; x < this.length; x++) {
    callback(this[x]);
  }
}

function init() {
    System.Gadget.settingsUI = "settings.html";
    System.Gadget.onSettingsClosed = settingsClosed;
    
    System.Gadget.Flyout.file = "flyout.html";
    System.Gadget.Flyout.onShow = showFlyout;
    System.Gadget.Flyout.onHide = hideFlyout;
    
    settingsChanged();
}

function changeBg(newBg) {
    System.Gadget.background = "img/" + newBg;
}

function loadInfo() {
    // Load info for each existing field
    fields.foreach(loadInfo2);
    
    if(mySettings.useCustomName) {
        serverName.innerHTML = mySettings.serverName;
    }
    
    if(mySettings.useAutoRefresh) {
        setTimeout("loadInfo()", mySettings.refreshInterval);
    }
}

function loadInfo2(key) {
    var value = getInfo(key)
    System.Gadget.document.getElementById(key).innerHTML = value;
}

function getInfo(key) {
    // If no host is specified, do not try to update
    if(mySettings.host != "") {
        var xhr = new XMLHttpRequest();
        var wait = true;
        var response;
        
        xhr.open("GET", "http://" + mySettings.host + "/" + key + "?rnd=" + Math.random(), false);
        
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4) {
                response = xhr.responseText;
                wait = false;
            }
        }
        
        xhr.send(null);
        
        while(wait) {}
        if(response == undefined) response = "";
        return response;
    }
    return "";
}

function showFlyout() {
    System.Gadget.Flyout.show = true;
}

function hideFlyout() {
    System.Gadget.Flyout.show = false;
}

function settingsClosed(event) {
    if(event.closeAction == event.Action.commit) {  
        settingsChanged();
    }
}

function settingsChanged() {
    mySettings.load();
    
    changeBg(mySettings.bg);
    loadInfo();
}