function setupTabs() {
    var paneContainers = document.querySelectorAll(".paneContainer");

    for(i = 0; i < paneContainers.length; i++) {
        var tabList = document.createElement("ul");
        tabList.setAttribute("class", "tabList");

        var panes = paneContainers[i].querySelectorAll(".pane");
        for(p = 0; p < panes.length; p++) {
            var tab = document.createElement("li");
            tab.setAttribute("class", "tab");

            var link = document.createElement("a");
            link.href = "#";
            link.setAttribute("onclick", "showPane(this.parentNode.parentNode, " + p + "); this.blur(); return false;");
            link.innerHTML = panes[p].title;

            tab.appendChild(link);
            tabList.appendChild(tab);
        }

        document.getElementsByTagName("body")[0].insertBefore(tabList, paneContainers[i]);
        showPane(tabList, 0);
    }
}

function showPane(tabList, paneNo) {
    var paneContainer = tabList.nextSibling;
    var panes = paneContainer.querySelectorAll(".pane");

    for(i = 0; i < panes.length; i++) {
        panes[i].setAttribute("class", "pane");
        tabList.children[i].setAttribute("class", "tab");
    }

    panes[paneNo].setAttribute("class", "pane activepane");
    tabList.children[paneNo].setAttribute("class", "tab activetab");
}
