package statsclient

import javax.swing.table.TableColumnModel
import groovy.util.logging.Log
import javax.swing.ListSelectionModel
import javax.swing.table.TableModel
import javax.swing.table.TableRowSorter

@Log
class MonitorTabController {
    def model
    def view
    def statsColumnConfig

    void mvcGroupInit(Map args) {

        def statsModel = args.statsModel
        statsColumnConfig = args.statsColumnConfig
        
        // firm-tab tables
        setupTable(view.firmTable, statsColumnConfig.firmColumns, MonitorTabModel.StatsDataView.FIRM, [0])
        setupTable(view.firmDetailTable, statsColumnConfig.instanceColumns, MonitorTabModel.StatsDataView.INSTANCE, [0, 1, 2])
        setupMasterDetailFiltering(view.firmTable, view.firmDetailTable, 0, 1)
        setInitialTableSort(view.firmTable, 0)

        // cloud-tab tables
        setupTable(view.cloudTable, statsColumnConfig.cloudColumns, MonitorTabModel.StatsDataView.CLOUD, [0])
        setupTable(view.cloudDetailTable, statsColumnConfig.instanceColumns, MonitorTabModel.StatsDataView.INSTANCE, [0, 1, 2])
        setupMasterDetailFiltering(view.cloudTable, view.cloudDetailTable, 0, 0)
        setInitialTableSort(view.cloudTable, 0)

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

    def setupMasterDetailFiltering = { masterTable, detailTable, selectionColumn, filterColumn ->
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(detailTable.model);
        detailTable.setRowSorter(sorter);
        sorter.setRowFilter(new TableRowFilter(masterTable, detailTable, selectionColumn, filterColumn))
    }

    def setInitialTableSort = { table, column ->
        def rowSorter = table.getRowSorter()
        if(rowSorter == null) {
            rowSorter = new TableRowSorter<TableModel>(table.model);
            table.setRowSorter(rowSorter)
        }
        rowSorter.toggleSortOrder(column)
    }
    
    // todo: currently fixed width - either use preferred size of header, or a configured value
    def setDefaultColumnWidths = { table ->
        TableColumnModel columns = table.getColumnModel();
        columns.each {column ->
            column.preferredWidth = 50
        }
    }
}
