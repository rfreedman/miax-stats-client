package statsclient

import javax.swing.table.TableColumnModel
import groovy.util.logging.Log

@Log
class MonitorTabController {
    def model
    def view
    def statsColumnConfig

    void mvcGroupInit(Map args) {

        def statsModel = args.statsModel
        statsColumnConfig = args.statsColumnConfig
        
        // todo - the service-level and cloud-level tables
        setupTable(view.statsTable, statsColumnConfig.firmColumns, MonitorTabModel.StatsDataView.FIRM)
        setupTable(view.detailTable, statsColumnConfig.instanceColumns, MonitorTabModel.StatsDataView.INSTANCE)

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
    
    def setupTable = {viewTable, columnConfig, dataView ->
        StatsTableModel statsTableModel = new StatsTableModel()
        statsTableModel.setAvailableColumnNames(columnConfig) //statsColumnConfig.firmColumns)
        viewTable.model = new StatsTableSorter(statsTableModel)
        viewTable.model.addMouseListenerToHeaderInTable(viewTable)
        setDefaultColumnWidths(viewTable)
        this.model.subscribe(dataView, statsTableModel)
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
