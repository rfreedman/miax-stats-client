package statsclient

import java.awt.BorderLayout
import java.awt.Color
import javax.swing.JSplitPane
import net.miginfocom.swing.MigLayout

panel(id: 'root', layout: new MigLayout(), constraints: 'w 100%, h 100%') {


    panel(layout: new MigLayout(), constraints: 'w 100%, h 100px') {
        label('Service Level Rollup')
    }


    splitPane(id: "splitter",
            constraints: 'newLine, w 100%, h 100%',
            orientation: JSplitPane.VERTICAL_SPLIT,
            resizeWeight: 0.5,
            oneTouchExpandable: true,
            continuousLayout: true,
            border: lineBorder(color: Color.BLACK, thickness: 1)) {


        panel(layout: new MigLayout(), constraints: 'w 100%, h 100%') {
            panel(layout: new BorderLayout(), constraints: 'w 100%, h 100%') {
                panel(layout: new MigLayout("align right"), constraints: "North") {
                    buttonGroup(id: 'firm-cloud').with { group ->
                        radioButton(id: 'rbFirm', text: 'Firm', buttonGroup: group, selected: bind {model.rollupMode == MonitorTabModel.FIRM})
                        radioButton(id: 'rbCloud', text: 'Cloud', buttonGroup: group, selected: bind {model.rollupMode == MonitorTabModel.CLOUD})
                    }
                }
                scrollPane(constraints: 'Center') {
                    table(id: 'statsTable', constraints: 'w 100%, h 100%') {
                    }
                }
            }
        }

        panel(layout: new MigLayout(), constraints: 'w 100%, h 100%') {
            scrollPane(constraints: 'w 100%, h 100%') {
                table(id: 'detailTable', constraints: 'w 100%, h 100%') {
                }
            }
        }
    }


}
