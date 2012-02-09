package statsclient

import java.awt.Color
import java.awt.ComponentOrientation
import javax.swing.JSplitPane
import javax.swing.JTable
import net.miginfocom.swing.MigLayout

panel(id: 'root', layout: new MigLayout(), constraints: 'w 100%, h 100%') {

    tabbedPane(id: 'documents', constraints: 'w 100%, h 100%', componentOrientation: ComponentOrientation.RIGHT_TO_LEFT, selectedIndex:1) {

        // Cloud tab
        splitPane(id: "Cloud",
                constraints: 'newLine, w 100%, h 100%',
                orientation: JSplitPane.VERTICAL_SPLIT,
                resizeWeight: 0.75,
                oneTouchExpandable: true,
                continuousLayout: true,
                border: lineBorder(color: Color.LIGHT_GRAY, thickness: 1)) {

            panel(layout: new MigLayout(), constraints: 'w 100%, h 100%') {
                scrollPane(constraints: 'w 100%, h 100%') {
                    table(id: 'cloudTable', constraints: 'w 100%, h 100%', autoResizeMode: JTable.AUTO_RESIZE_OFF) {
                    }
                }
            }

            panel(layout: new MigLayout(), constraints: 'w 100%, h 100%') {
                scrollPane(constraints: 'w 100%, h 100%') {
                    table(id: 'cloudDetailTable', constraints: 'w 100%, h 100%', autoResizeMode: JTable.AUTO_RESIZE_OFF) {
                    }
                }
            }
        }

        // Firm tab
        splitPane(id: "Firm",
                constraints: 'newLine, w 100%, h 100%',
                orientation: JSplitPane.VERTICAL_SPLIT,
                resizeWeight: 0.75,
                oneTouchExpandable: true,
                continuousLayout: true,
                border: lineBorder(color: Color.LIGHT_GRAY, thickness: 1)) {

            panel(layout: new MigLayout(), constraints: 'w 100%, h 100%') {
                scrollPane(constraints: 'w 100%, h 100%') {
                    table(id: 'firmTable', constraints: 'w 100%, h 100%', autoResizeMode: JTable.AUTO_RESIZE_OFF) {
                    }
                }
            }

            panel(layout: new MigLayout(), constraints: 'w 100%, h 100%') {
                scrollPane(constraints: 'w 100%, h 100%') {
                    table(id: 'firmDetailTable', constraints: 'w 100%, h 100%', autoResizeMode: JTable.AUTO_RESIZE_OFF) {
                    }
                }
            }
        }

    }


}
