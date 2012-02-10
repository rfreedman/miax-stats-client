package statsclient

import groovy.util.logging.Log
import griffon.core.MVCGroup
import javax.swing.ListSelectionModel
import javax.swing.table.TableColumnModel
import static statsclient.MonitorTabModel.StatsDataView.*

@Log
class StatsController {
    // these will be injected by Griffon
    def model
    def view
    def dataService

    List<MVCGroup> tabMVCGroups = new ArrayList<MVCGroup>()

    String mvcName
    def statsColumnConfig

    void mvcGroupInit(Map args) {
        mvcName = args.mvcName

        statsColumnConfig = dataService.getGlobalColumnConfig()

        setupTable(view.serviceLevelTable, statsColumnConfig.serviceColumns, SERVICE )

        dataService.getTabNames().each(createStatsTab)

        view.documents.selectedIndex = 0

        dataService.subscribe(model.onUpdate)
    }

    def setupTable = {viewTable, columnConfig, dataView  ->
        StatsTableModel statsTableModel = new StatsTableModel()
        statsTableModel.setAvailableColumnNames(columnConfig)
        statsTableModel.setShownColumns(dataService.getTableColumnConfig(null, dataView))

        viewTable.model = new StatsTableSorter(statsTableModel)
        viewTable.model.addMouseListenerToHeaderInTable(viewTable)
        viewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        setDefaultColumnWidths(viewTable)
        this.model.subscribeToServiceData(statsTableModel)
    }


    // todo: currently fixed width - either use preferred size of header, or a configured value
    def setDefaultColumnWidths = { table ->
        TableColumnModel columns = table.getColumnModel();
        columns.each {column ->
            column.preferredWidth = 50
        }
    }

    def createStatsTab = { statName ->
        String id = statName
        def tabMVCGroup = buildMVCGroup('monitorTab', id, tabs: view.documents, id: id, name: id, statsColumnConfig:statsColumnConfig, statsModel: model)
        tabMVCGroups.add(tabMVCGroup)
        view.documents.addTab(id, tabMVCGroup.view.root)
    }

    def shutdown = {
        destroyMVCGroup(mvcName)
    }

    void mvcGroupDestroy() {
        // this method is called when the group is destroyed
        log.info("window closing, so unsubscribing")
        dataService.unsubscribe()
        tabMVCGroups.each {mvcGroup ->
            mvcGroup.destroy()
        }
    }

    void activeDocumentChanged() {
        /*
        // de-activate the existing active document
        if (model.activeDocument) {
            model.activeDocument.controller.deactivate()
        }

        // figure out the new active document
        int index = view.documents.selectedIndex
        model.activeDocument = index == -1 ? null : model.openDocuments[index]
        if (model.activeDocument) {
            model.activeDocument.controller.activate(model.state)
        }
        */
    }
}
