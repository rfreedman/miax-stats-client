package statsclient

import javax.swing.table.AbstractTableModel
import groovy.util.logging.Log

@Log
class StatsTableModel extends AbstractTableModel {

    def columnNames = ["unset"] // todo - filter

    def data = []
    
    def beforeChangeListeners = []
    
    // the names of all of the available columns, in order
    // todo - filter
    def setAvailableColumnNames = { columnNames ->
        this.columnNames = columnNames
    }

    def registerBeforeChangeListener = { listener ->
        beforeChangeListeners << listener
    }

    def onUpdate = { data ->

        beforeChangeListeners.each {listener ->
            listener.beforeTableChanged()
        }

        this.data = data
        fireTableDataChanged()
    }
    
    int getRowCount() {
        return data == null ? 0 : data.size()
    }

    int getColumnCount() {
        return columnNames == null ? 0 : columnNames.size()
    }


    Object getValueAt(int row, int col) {
        return data == null ? null : data[row][col]
    }

    public String getColumnName(int col) {
        return columnNames == null ? null : columnNames[col];
    }

    public Class getColumnClass(int column) {
        Class returnValue;
        if ((column >= 0) && (column < getColumnCount() && data != null && data.size() > 0)) {
            def value = getValueAt(0, column)
            returnValue = value == null ? Object.class : value.getClass()
        } else {
            returnValue = Object.class;
        }
        return returnValue;
    }
}

