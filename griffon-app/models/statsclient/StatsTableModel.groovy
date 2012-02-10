package statsclient

import javax.swing.table.AbstractTableModel
import groovy.util.logging.Log

@Log
class StatsTableModel extends AbstractTableModel {
    def allColumnNames = []
    def columnNames = [] // todo - filter
    def columnMap = []
    
    def data = []
    
    def beforeChangeListeners = []
    
    // the names of all of the available columns, in the same order as the data
    def setAvailableColumnNames = { columnNames ->
        allColumnNames = columnNames
        this.columnNames = allColumnNames // default to showing all columns
    }

    // the columns that will actually be shown
    def setShownColumns = { columnNames ->
        if(columnNames.empty) {
            throw new RuntimeException("availableColumns must be set before shownColumns")
        }
        this.columnNames = columnNames
        buildColumnMap()
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
        if(data == null) {
            return null
        }
        def dataColumn = convertShownColumnIndexToAllColumnsIndex(col)
        return data[row][dataColumn]
        
    }

    public String getColumnName(int col) {
        return columnNames == null ? null : columnNames[col]
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

    def buildColumnMap = {
       columnNames.each { shownColumn ->
           def allColumnsIndex = allColumnNames.findIndexOf{name -> name == shownColumn}
           if(allColumnsIndex < 0) {
               throw new RuntimeException("shownColumn ${shownColumn} is not an available column")
           }
           columnMap << allColumnsIndex
       }
    }
    
    def convertShownColumnIndexToAllColumnsIndex = { shownColumnIndex ->
        columnMap.empty ? shownColumnIndex : columnMap[shownColumnIndex]
    }
}

