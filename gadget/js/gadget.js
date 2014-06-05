var autoRefreshRunning = false;

function init() {
    System.Gadget.settingsUI = "settings.html";
    System.Gadget.onSettingsClosed = settingsClosed;

    System.Gadget.Flyout.file = "flyout.html";
    System.Gadget.Flyout.onShow = showFlyout;
    System.Gadget.Flyout.onHide = hideFlyout;

    settingsChanged();
}

function refresh() {
    // Disable refresh button until we finish or run into timeout
    System.Gadget.document.getElementById("refreshBtn").disabled = true;
    setTimeout(function() {
        System.Gadget.document.getElementById("refreshBtn").disabled = false;
    }, settings["connTimeout"] * 1000);

    sendQuery({"action": "refresh"}, function(response) {
        // Server responds JSON object with key (ID) - value pairs, apply these to the gadget
        for (var key in response) {
            var element = System.Gadget.document.querySelector("#" + key + " .value");
            if(element != null) element.innerHTML = response[key];
        }
    });
}

function autoRefresh() {
    if(settings["useAutoRefresh"]) {
        refresh();
        setTimeout(autoRefresh, settings["refreshInterval"] * 1000);
    } else {
        autoRefreshRunning = false;
    }
}

function sendQuery(content, callback, callbackParam) {
    // If no host is specified, do not try to update
    if(typeof settings["host"] !== "undefined" && settings["host"] != "") {
        var xhr = new XMLHttpRequest();

        xhr.open("GET", encodeURI(settings["host"]) + "&rnd=" + Math.random(), true);
        xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");

        // Add auth info to the request
        var additionalInfo = {
            "adminPw": settings["adminPw"]
        };
        content = additionalInfo.concat(content);

        // Set the function that is executed when we get an answer
        xhr.onreadystatechange = function() {
            if(xhr.readyState == 4) {
                var response = xhr.responseText;
                response = typeof response !== "undefined" ? response : "";

                // Interpret server response as JSON string
                response = JSON.parse(response);

                callback(response, callbackParam);
            }
        }

        xhr.send(content);
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
    settings.load();

    // Set custom name
    if(settings["useCustomName"]) {
        System.Gadget.document.querySelector("#serverName .value").innerHTML = settings["serverName"];
    }

    // Set Background
    System.Gadget.background = "img/" + settings["bg"];

    // Hide all values with labels
    var liElements = System.Gadget.document.querySelectorAll("#infoList li");
    for(i = 0; i < liElements.length; i++) liElements[i].style.display = "none";

    // Display values for enabled queries
    var enabledQueries = settings["enabledQueries"].split(";");

    System.Gadget.document.getElementById("serverName").style.display = "block";
    for(i = 0; i < enabledQueries.length; i++) {
        System.Gadget.document.getElementById(enabledQueries[i]).style.display = "block";
        System.Gadget.document.getElementById("infoList").appendChild(System.Gadget.document.getElementById(enabledQueries[i]));
    }

    // Set text color
    var spans = System.Gadget.document.getElementsByTagName("span");
    var as = System.Gadget.document.getElementsByTagName("a");

    for(i = 0; i < spans.length; i++) spans[i].style.color = settings["textColor"];
    for(i = 0; i < as.length; i++) as[i].style.color = settings["textColor"];

    refresh();

    // Start auto refreshing
    if(settings["useAutoRefresh"] && !autoRefreshRunning) {
        setTimeout(autoRefresh, settings["refreshInterval"] * 1000);
        autoRefreshRunning = true;
    }
}
