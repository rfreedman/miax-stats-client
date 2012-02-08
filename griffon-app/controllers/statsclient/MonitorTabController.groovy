package statsclient

import javax.swing.table.TableColumnModel
import groovy.util.logging.Log

@Log
class MonitorTabController {
    def model
    def view


    void mvcGroupInit(Map args) {

        /*
        def statsModel = new StatsTableSorter(model.statsData)
        view.statsTable.model = statsModel
        view.statsTable.model.addMouseListenerToHeaderInTable(view.statsTable)
        setDefaultColumnWidths(view.statsTable)

        def detailModel = new StatsTableSorter(model.detailData)
        view.detailTable.model = detailModel
        view.detailTable.model.addMouseListenerToHeaderInTable(view.detailTable)
        setDefaultColumnWidths(view.detailTable)
        */

        StatsModel statsModel = args.statsModel
        def statsColumnConfig = args.statsColumnConfig
        
        // todo - the service-level and cloud-level tables
        
        // the 'firm' table
        StatsTableModel statsTableModel = new StatsTableModel()
        statsTableModel.setAvailableColumnNames(statsColumnConfig.firmColumns)

        view.statsTable.model = new StatsTableSorter(statsTableModel)
        view.statsTable.model.addMouseListenerToHeaderInTable(view.statsTable)
        setDefaultColumnWidths(view.statsTable)
        this.model.subscribe(MonitorTabModel.StatsDataView.FIRM, statsTableModel)


        view.rbFirm.actionPerformed = { evt ->
            //switchRollup(MonitorTabModel.StatsDataView.FIRM)
        };

        view.rbCloud.actionPerformed = { evt ->
            //switchRollup(MonitorTabModel.StatsDataView.CLOUD)
        };


        statsModel.subscribe(this.model) // subscribe the tab model to the global stats model for this window
    }

    void mvcGroupDestroy() {
        // this method is called when the group is destroyed
    }


    // todo: currently fixed width - either use preferred size of header, or a configured value
    def setDefaultColumnWidths = { table ->
        TableColumnModel columns = table.getColumnModel();
        columns.each {column ->
            column.preferredWidth = 50
        }
    }

/*
    void switchRollup(MonitorTabModel.StatsDataView value) {
        if (model.rollupMode != value) {
            model.rollupMode = value

            // todo - actually switch the mode
            System.out.println("switched to ${model.rollupMode}")
        }
    }
*/

}
