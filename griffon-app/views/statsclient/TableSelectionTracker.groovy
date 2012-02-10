package statsclient

import groovy.util.logging.Log
import javax.swing.JTable
import javax.swing.RepaintManager

@Log
class TableSelectionTracker {

    JTable table;
    java.util.List<Integer> keys
    String lastSelectedCompoundKey = null

    public TableSelectionTracker(JTable table, java.util.List<Integer> keys) {
        this.table = table;
        this.keys = keys
        table.model.registerBeforeChangeListener(this)
    }

    def beforeTableChanged = {

        def selectedViewRowIndex = table.getSelectedRow()

        if (selectedViewRowIndex > -1) {
            def selectedModelRowIndex = table.convertRowIndexToModel(selectedViewRowIndex)

            StringBuilder buff = new StringBuilder()
            keys.each {key ->
                buff.append("|").append(table.model.getValueAt(selectedModelRowIndex, key))
            }
            lastSelectedCompoundKey = buff.toString()
        }
    }


    public void reselect() {

        if (lastSelectedCompoundKey != null) {

            // find the previously selected row by its compound key
            def selectedModelRowIndex = -1

            for (i in 0..(table.model.rowCount - 1)) {

                StringBuilder buff = new StringBuilder()
                keys.each {key ->
                    buff.append("|").append(table.model.getValueAt(i, key))
                }
                if(buff.toString() == lastSelectedCompoundKey) {
                    selectedModelRowIndex = i
                    break
                }
            }

            if (selectedModelRowIndex > -1) {
                def selectedViewRowIndex = table.convertRowIndexToView(selectedModelRowIndex)
                table.getSelectionModel().setSelectionInterval(selectedViewRowIndex, selectedViewRowIndex)
            }
        }
    }
}
