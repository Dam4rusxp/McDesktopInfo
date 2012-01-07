var host;
var fields = new Array("playerCount", "serverName", "pluginVersion");

// Simple for-each script
Array.prototype.foreach = function(callback) {
  for(var x=0; x < this.length; x++) {
    callback(this[x]);
  }
}

function init() {
    settingsChanged();
    
    System.Gadget.settingsUI = "settings.html";
    System.Gadget.onSettingsClosed = settingsClosed;
}

function changeBg(newBg) {
    System.Gadget.background = "img/" + newBg;
}

function loadInfo() {
    // Load info for each existing field
    fields.foreach(getInfo);
    
    if(mySettings.useCustomName) {
        serverName.innerHTML = mySettings.serverName;
    }
    
    if(mySettings.useAutoRefresh) {
        setTimeout("loadInfo()", mySettings.refreshInterval);
    }
}

function getInfo(key) {
    // If no host is specified, do not try to update
    if(host != "") {
        var xhr = new XMLHttpRequest();
        
        xhr.open("GET", "http://" + host + "/" + key + "?rnd=" + Math.random(), false);
        
        xhr.onreadystatechange = function() {
            if ( xhr.readyState == 4 ) {
                document.getElementById(key).innerHTML = xhr.responseText;
            }
        }
        
        xhr.send(null);
    }
}

function settingsClosed(event) {
    if(event.closeAction == event.Action.commit) {  
        settingsChanged();
    }
}

function settingsChanged() {
    mySettings.load();
    
    host = mySettings.host;
    
    changeBg(mySettings.bg);
    loadInfo();
}