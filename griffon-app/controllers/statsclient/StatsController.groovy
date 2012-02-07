package statsclient

class StatsController {
    // these will be injected by Griffon
    def model
    def view


    void mvcGroupInit(Map args) {

       // todo - get the stats from configuration
       ["Capacity", "Latency", "Custom1", "Custom2"].each(createStatsTab)
        
       view.documents.selectedIndex = 0
     }


     def createStatsTab = { statName ->
         String id = statName
         def document = buildMVCGroup('monitorTab', id,   tabs: view.documents, id: id, name: id)
         view.documents.addTab(id, document.view.root)
     }

     void mvcGroupDestroy() {
        // this method is called when the group is destroyed

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
