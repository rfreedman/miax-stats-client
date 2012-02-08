package statsclient

class StatsController {
    // these will be injected by Griffon
    def model
    def view
    def dataService

    void mvcGroupInit(Map args) {

        // todo - replace this with getting the column config from the server, and set it on the model
        model.generateFakeColumnConfig()

       dataService.getTabNames().each(createStatsTab)

       view.documents.selectedIndex = 0

       model.generateFakeData()
     }


     def createStatsTab = { statName ->
         String id = statName
         def document = buildMVCGroup('monitorTab', id,   tabs: view.documents, id: id, name: id)
         view.documents.addTab(id, document.view.root)
     }

     void mvcGroupDestroy() {
        // this method is called when the group is destroyed

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
