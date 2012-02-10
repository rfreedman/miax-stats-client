package statsclient

import javax.swing.RowFilter
import javax.swing.JTable
import javax.swing.event.ListSelectionListener


class TableRowFilter extends RowFilter implements ListSelectionListener {

    JTable selectionTable
    JTable filteredTable
    def selectionColumn
    def filterColumn
    def currentFilterValue = null

    public TableRowFilter(JTable selectionTable, JTable filteredTable, int selectionColumn, int filterColumn) {
        super()
        this.selectionTable = selectionTable
        this.filteredTable = filteredTable
        this.selectionColumn = selectionColumn
        this.filterColumn = filterColumn

        selectionTable.getSelectionModel().addListSelectionListener(this)
    }

    @Override
    boolean include(RowFilter.Entry entry) {

        if(currentFilterValue == null) {
            return false // if no selection, don't show any rows
        }

        if(entry.model == null) {
            return false
        }

        if(filterColumn == null) {
            return false
        }

        def cellValue = entry.model.getValueAt(entry.identifier, filterColumn)
        def retval = (cellValue == currentFilterValue)
        return retval
    }

    void valueChanged(javax.swing.event.ListSelectionEvent event) {
        if(!event.valueIsAdjusting) {
           def selectedViewRow = event.lastIndex
           def selectedModelRow = selectionTable.convertRowIndexToModel(selectedViewRow)
           currentFilterValue = selectionTable.model.getValueAt(selectedModelRow, selectionColumn)
           filteredTable.model.fireTableDataChanged()
        }
    }
}
