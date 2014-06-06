var autoRefreshRunning = false;

function init() {
    System.Gadget.settingsUI = "settings.html";
    System.Gadget.onSettingsClosed = settingsClosed;

    System.Gadget.Flyout.file = "flyout.html";

    settingsChanged();
}

function refresh() {
    // Disable refresh button until we finish or run into timeout
    System.Gadget.document.getElementById("refreshBtn").disabled = true;

    sendQuery({"action": "refresh"}, function(response) {
        System.Gadget.document.getElementById("refreshBtn").disabled = false;

        // Clear the gadget if the server cannot be reached
        if (typeof response === "undefined") {
            response = {};
            var valueElements = settings.values["enabledQueries"].split(";");
            for (var i = 0; i < valueElements.length; i++) response[valueElements[i]] = "";
        }

        if (settings.values["useCustomName"] === true) response["serverName"] = settings.values["serverName"];

        // Server responds JSON object with key (ID) - value pairs, apply these to the gadget
        for (var key in response) {
            var element = System.Gadget.document.querySelector("#" + key + " .value");
            if(element != null) element.innerHTML = response[key];
        }
    });
}

function autoRefresh() {
    if(settings.values["useAutoRefresh"]) {
        refresh();
        setTimeout(autoRefresh, settings.values["refreshInterval"] * 1000);
    } else {
        autoRefreshRunning = false;
    }
}

function sendQuery(content, callback, callbackParam) {
    // If no host is specified, do not try to update
    if(typeof settings.values["host"] !== "undefined" && settings.values["host"] != "") {
        var xhr = new XMLHttpRequest();

        xhr.open("POST", "http://" + encodeURI(settings.values["host"]) + "?rnd=" + Math.random(), true);
        xhr.timeout = settings.values["connTimeout"] * 1000;
        xhr.setRequestHeader("Cache-Control", "no-cache");
        xhr.setRequestHeader("Content-Type", "text/plain; charset=utf-8");
        xhr.setRequestHeader("Connection", "close");

        // Add auth info to the request
        var additionalInfo = {};
        if(settings.values["adminPw"] !== "") additionalInfo["adminPw"] = settings.values["adminPw"];

        for(var key in additionalInfo) content[key] = additionalInfo[key];

        // Set the function that is executed when we get an answer
        xhr.onreadystatechange = function() {
            if(xhr.readyState == 4) {
                var jsonObj = {};
                if (xhr.responseText) jsonObj = JSON.parse(xhr.responseText);

                callback(jsonObj, callbackParam);
            }
        }

        xhr.send(JSON.stringify(content));
    } else {
        callback(undefined, callbackParam);
    }
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
    settings.loadSettings();

    // Set custom name
    if(settings.values["useCustomName"]) {
        System.Gadget.document.querySelector("#serverName .value").innerHTML = settings.values["serverName"];
    }

    // Set Background
    System.Gadget.background = "img/" + settings.values["bg"];

    // Hide all values with labels
    var liElements = System.Gadget.document.querySelectorAll("#infoList li");
    for(i = 0; i < liElements.length; i++) liElements[i].style.display = "none";

    // Display values for enabled queries
    var enabledQueries = settings.values["enabledQueries"].split(";");

    System.Gadget.document.getElementById("serverName").style.display = "block";
    for(i = 0; i < enabledQueries.length; i++) {
        System.Gadget.document.getElementById(enabledQueries[i]).style.display = "block";
        System.Gadget.document.getElementById("infoList").appendChild(System.Gadget.document.getElementById(enabledQueries[i]));
    }

    // Set text color
    System.Gadget.document.querySelector("body").style.color = settings.values["textColor"];

    refresh();

    // Start auto refreshing
    if(settings.values["useAutoRefresh"] && !autoRefreshRunning) {
        setTimeout(autoRefresh, settings.values["refreshInterval"] * 1000);
        autoRefreshRunning = true;
    }
}
