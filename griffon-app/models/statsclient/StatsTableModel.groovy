package statsclient

import javax.swing.table.AbstractTableModel
import groovy.util.logging.Log

@Log
class StatsTableModel extends AbstractTableModel {

    def random = new Random(new Date().getTime())
    def columnNames = [] // todo - filter

    def data = []


    /*
    final int COL_COUNT = 25;
    final int ROW_COUNT = 300;


    public StatsTableModel() {
        for(int n = 0; n < COL_COUNT; n++) {
            columnNames.push("Col${n}")
            data = buildModel()
        }
    }


    def buildModel = {
        def d = []

        for(int i = 0; i < ROW_COUNT; i++) {
            def row = []
            for(int j = 0; j < columnNames.size(); j++) {
                row.push(random.nextInt(101))
            }
            d.push(row)
        }

        return d
    }


    def update = {
        data = buildModel()
        fireTableDataChanged()
    }
    */   
    
    
    // the names of all of the available columns, in order
    // todo - filter
    def setAvailableColumnNames = { columnNames ->
        this.columnNames = columnNames
    }
    
    def onUpdate = { data ->
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

