package statsclient

import java.awt.BorderLayout
import javax.swing.JTable
import javax.swing.WindowConstants
import net.miginfocom.swing.MigLayout

frame(id: 'statsFrame',
        title: 'GriffonStats',
        layout: new BorderLayout(),
        preferredSize: [1024, 1000],
        pack: true,
        locationByPlatform: true,
        iconImage: imageIcon('/griffon-icon-48x48.png').image,
        iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                imageIcon('/griffon-icon-32x32.png').image,
                imageIcon('/griffon-icon-16x16.png').image],
        defaultCloseOperation: WindowConstants.DO_NOTHING_ON_CLOSE,
        windowClosing: { evt ->
            controller.shutdown()
        }
) {


    panel(constraints: "North") {
        label('Application Name')
    }

    panel(layout: new BorderLayout(), constraints: "Center") {
        panel(layout: new MigLayout(), constraints: "North") {
            table(id: 'serviceLevelTable', constraints: 'w 100%, h 100%', autoResizeMode: JTable.AUTO_RESIZE_OFF) {
            }
        }

        tabbedPane(id: 'documents', constraints: "Center", stateChanged: { evt ->
            controller.activeDocumentChanged()
        }) {
        }
    }

}