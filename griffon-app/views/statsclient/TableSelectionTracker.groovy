package statsclient

import groovy.util.logging.Log
import javax.swing.JTable

@Log
class TableSelectionTracker { // implements ListSelectionListener {

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

            lastSelectedCompoundKey = ""
            keys.each {key ->
                lastSelectedCompoundKey += "|" + table.model.getValueAt(selectedModelRowIndex, key)
            }
        }
    }


    public void reselect() {

        if (lastSelectedCompoundKey != null) {
            // find the previously selected row by its compound key
            def selectedModelRowIndex = -1

            for (i in 0..(table.model.rowCount - 1)) {
                def compoundKey = ""
                keys.each {key ->
                    compoundKey += "|" + table.model.getValueAt(i, key)
                }
                if (compoundKey == lastSelectedCompoundKey) {
                    selectedModelRowIndex = i
                    break;
                }
            }

            if (selectedModelRowIndex > -1) {
                def selectedViewRowIndex = table.convertRowIndexToView(selectedModelRowIndex)
                table.getSelectionModel().setSelectionInterval(selectedViewRowIndex, selectedViewRowIndex)
            }
        }

    }
}
