package statsclient

import javax.swing.table.TableColumnModel

class MonitorTabController {
    def model
    def view
    Timer timer = new Timer();
    TimerTask timerTask = new ModelUpdateTimerTask();

    void mvcGroupInit(Map args) {

        def statsModel = new StatsTableSorter(model.statsData)
        view.statsTable.model = statsModel
        view.statsTable.model.addMouseListenerToHeaderInTable(view.statsTable)
        setDefaultColumnWidths(view.statsTable)

        def detailModel = new StatsTableSorter(model.detailData)
        view.detailTable.model = detailModel
        view.detailTable.model.addMouseListenerToHeaderInTable(view.detailTable)
        setDefaultColumnWidths(view.detailTable)

        view.rbFirm.actionPerformed = { evt ->
            switchRollup(MonitorTabModel.FIRM)
        };

        view.rbCloud.actionPerformed = { evt ->
            switchRollup(MonitorTabModel.CLOUD)
        };

        // todo - updates should come from CometD subscription in the model
        timer.scheduleAtFixedRate(timerTask, 2000, 2000)

    }

    void mvcGroupDestroy() {
        // this method is called when the group is destroyed
        timer.cancel()
    }


    // todo: currently fixed width - either use preferred size of header, or a configured value
    def setDefaultColumnWidths = { table ->
        TableColumnModel columns = table.getColumnModel();
        columns.each {column ->
            column.preferredWidth = 50
        }
    }

    void switchRollup(String value) {
        if (model.rollupMode != value) {
            model.rollupMode = value

            // todo - actually switch the mode
            System.out.println("switched to ${model.rollupMode}")
        }
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
