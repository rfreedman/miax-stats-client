package statsclient

class MainMenuController {
    // these will be injected by Griffon
    def model
    def view
    def windowNumber = 0;

    // void mvcGroupInit(Map args) {
    //    // this method is called after model and view are injected
    // }

    // void mvcGroupDestroy() {
    //    // this method is called when the group is destroyed
    // }

    /*
        Remember that actions will be called outside of the UI thread
        by default. You can change this setting of course.
        Please read chapter 9 of the Griffon Guide to know more.
       
    def action = { evt = null ->
    }
    */

    def openStatsWindow = { evt = null ->

        def (m, v, c) = createMVCGroup('griffonStats', "stats_${windowNumber}")
        v.statsFrame.show()
        windowNumber += 1
    }
}
