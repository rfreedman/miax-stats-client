package statsclient

class MonitorTabController {
    def model
    def view
    Timer timer = new Timer();
    TimerTask timerTask = new ModelUpdateTimerTask();

     void mvcGroupInit(Map args) {

        def statsModel =  new StatsTableSorter(model.statsData)
        view.statsTable.model = statsModel
        statsModel.addMouseListenerToHeaderInTable(view.statsTable)


        def detailModel = new StatsTableSorter(model.detailData)
        view.detailTable.model = detailModel
        detailModel.addMouseListenerToHeaderInTable(view.detailTable)

        // todo - updates should come from CometD subscription in the model
        timer.scheduleAtFixedRate(timerTask, 2000, 2000)

     }

     void mvcGroupDestroy() {
        // this method is called when the group is destroyed
        timer.cancel()
     }


    def update = { evt = null ->
        model.update()
    }

    class ModelUpdateTimerTask extends TimerTask {
        @Override
        void run() {
            update();
        }

    }
}
