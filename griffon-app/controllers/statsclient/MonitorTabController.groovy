package statsclient

import javax.swing.table.TableColumnModel
import groovy.util.logging.Log
import javax.swing.ListSelectionModel

@Log
class MonitorTabController {
    def model
    def view
    def statsColumnConfig

    void mvcGroupInit(Map args) {

        def statsModel = args.statsModel
        statsColumnConfig = args.statsColumnConfig
        
        // todo - the cloud-level table
        setupTable(view.statsTable, statsColumnConfig.firmColumns, MonitorTabModel.StatsDataView.FIRM, [0])
        setupTable(view.detailTable, statsColumnConfig.instanceColumns, MonitorTabModel.StatsDataView.INSTANCE, [0, 1, 2])

        view.rbFirm.actionPerformed = { evt ->
            switchRollup(MonitorTabModel.StatsDataView.FIRM)
        };

        view.rbCloud.actionPerformed = { evt ->
            switchRollup(MonitorTabModel.StatsDataView.CLOUD)
        };


        statsModel.subscribe(this.model) // subscribe the tab model to the global stats model for this window
    }

    void mvcGroupDestroy() {
        // this method is called when the group is destroyed
    }
    
    def setupTable = {viewTable, columnConfig, dataView, keys ->
        StatsTableModel statsTableModel = new StatsTableModel()
        statsTableModel.setAvailableColumnNames(columnConfig)
        viewTable.model = new StatsTableSorter(statsTableModel)
        viewTable.model.addMouseListenerToHeaderInTable(viewTable)
        viewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        setDefaultColumnWidths(viewTable)
        this.model.subscribe(dataView, statsTableModel)

        TableSelectionTracker selectionTracker = new TableSelectionTracker(viewTable, keys)
        viewTable.model.selectionTracker = selectionTracker
    }
    

    // todo: currently fixed width - either use preferred size of header, or a configured value
    def setDefaultColumnWidths = { table ->
        TableColumnModel columns = table.getColumnModel();
        columns.each {column ->
            column.preferredWidth = 50
        }
    }


    void switchRollup(MonitorTabModel.StatsDataView value) {
        if (model.rollupMode != value) {
            model.rollupMode = value

            // todo - actually switch the mode
            String msg = "switched to ${model.rollupMode}"
            System.out.println(msg)
        }
    }
}
