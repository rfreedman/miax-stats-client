package statsclient;

import groovy.inspect.swingui.TableSorter;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class StatsTableSorter extends TableSorter {
    boolean ascending = true;
    int lastSortedColumn = -1;
    final TableSorter sorter = this;

    public StatsTableSorter(TableModel model) {
        setModel(model);
    }

    /* override to provide provide proper sorting when table data changes after sort
     * and to provide correct ascending/descending sort when sorting a new column and the previous sort was descending
     */
    public void addMouseListenerToHeaderInTable(JTable table) {

        final JTable tableView = table;
        tableView.setColumnSelectionAllowed(false);
        MouseAdapter listMouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);
                if (e.getClickCount() == 1 && column != -1) {
                    if (lastSortedColumn == column) {
                        ascending = !ascending;
                    } else {
                        ascending = true;
                    }
                    sorter.sortByColumn(column, ascending);
                    lastSortedColumn = column;
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);
    }

    public void reSort() {
        sorter.sortByColumn(lastSortedColumn, ascending);
    }

    public void tableChanged(TableModelEvent e) {
       //super.tableChanged(e);

       // bypass super.tableChanged because it prints annoying stuff to the console
       // see http://jira.codehaus.org/browse/GROOVY-5270 - should be fixed for 1.8.6 and 2.0-beta-3
       reallocateIndexes();
       fireTableChanged(e);

       // re-apply the current sort
       reSort();
    }
}
