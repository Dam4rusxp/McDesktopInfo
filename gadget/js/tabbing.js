function setupTabs() {
    var tabList = document.getElementById("tabList");
    var panes = document.getElementById("paneContainer").getElementsByTagName("div");

    for(i = 0; i < panes.length; i++) {
        var paneId = panes[i].getAttribute("id");
        var paneName = panes[i].getAttribute("title");

        var tab = document.createElement("li");
        tab.innerHTML = "<a href=\"#" + paneId + "\" onfocus=\"this.blur();\" onclick=\"showTab(\'" + paneId + "\'); return false;\">" + paneName + "</a>";
        tabList.appendChild(tab);

        panes[i].removeAttribute("title");
    }

    // Refresh tabList and activate first tab
    tabList = document.getElementById("tabList");
    showTab(tabList.getElementsByTagName("a")[0].hash.substring(1));
}

function showTab(tabId) {
    // Display the requested tab
    var newTab = document.getElementById(tabId);
    newTab.style.display = "";

    // Hide all other tabs
    var tabList = document.getElementById("tabList");
    var tabs = tabList.getElementsByTagName("li");

    for(i = 0; i < tabs.length; i++) {
        var tabLink = tabs[i].getElementsByTagName("a")[0];
        var hash = tabLink.hash;

        if(hash.substring(1) != tabId) {
            tabs[i].setAttribute("className", "");
            document.getElementById(hash.substring(1)).style.display = "none";
        } else {
            tabs[i].setAttribute("className", "activetab");
        }
    }
}