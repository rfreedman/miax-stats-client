application {
    title = 'GriffonStats'
    //startupGroups = ['griffonStats']
    startupGroups = ['mainMenu']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "monitorTab"
    'monitorTab' {
        model      = 'statsclient.MonitorTabModel'
        view       = 'statsclient.MonitorTabView'
        controller = 'statsclient.MonitorTabController'
    }

    // MVC Group for "mainMenu"
    'mainMenu' {
        model      = 'statsclient.MainMenuModel'
        view       = 'statsclient.MainMenuView'
        controller = 'statsclient.MainMenuController'
    }

    // MVC Group for "griffonStats"
    'griffonStats' {
        model      = 'statsclient.StatsModel'
        view       = 'statsclient.StatsView'
        controller = 'statsclient.StatsController'
    }

}
