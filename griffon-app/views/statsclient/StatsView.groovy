package statsclient

import java.awt.BorderLayout

frame(id: 'statsFrame',
        title: 'GriffonStats',
        layout: new BorderLayout(),
        preferredSize: [1024, 768],
        pack: true,
        locationByPlatform: true,
        iconImage: imageIcon('/griffon-icon-48x48.png').image,
        iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                imageIcon('/griffon-icon-32x32.png').image,
                imageIcon('/griffon-icon-16x16.png').image]) {


    panel(constraints: "North") {
        label('Application Name')
    }


    tabbedPane(id: 'documents', constraints: "Center", stateChanged: { evt ->
        controller.activeDocumentChanged()
    }) {
    }


}