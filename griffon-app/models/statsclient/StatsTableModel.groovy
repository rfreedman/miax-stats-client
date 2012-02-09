package statsclient

import javax.swing.table.AbstractTableModel

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
        return data.size()
    }

    int getColumnCount() {
        return columnNames.size()
    }


    Object getValueAt(int row, int col) {
        return data[row][col]
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class getColumnClass(int column) {
        Class returnValue;
        if ((column >= 0) && (column < getColumnCount())) {
            returnValue = getValueAt(0, column).getClass();
        } else {
            returnValue = Object.class;
        }
        return returnValue;
    }
}

