package org.infy.profiler.visualization;

import org.infy.profiler.MethodMetrics;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodMetricsTable extends JPanel {
    private final JTable table;
    private final MethodMetricsTableModel tableModel;
    
    public MethodMetricsTable() {
        setLayout(new BorderLayout());
        
        tableModel = new MethodMetricsTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        
        // Configure table appearance
        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void updateData(Map<String, MethodMetrics> methodMetrics) {
        tableModel.updateData(methodMetrics);
    }
    
    private static class MethodMetricsTableModel extends AbstractTableModel {
        private final String[] columnNames = {
            "Method Name", 
            "Invocations", 
            "Avg Time (ms)", 
            "Max Time (ms)", 
            "Min Time (ms)"
        };
        private final List<MethodMetricsRow> data = new ArrayList<>();
        
        public void updateData(Map<String, MethodMetrics> methodMetrics) {
            data.clear();
            methodMetrics.forEach((name, metrics) -> 
                data.add(new MethodMetricsRow(
                    name,
                    metrics.getInvocationCount(),
                    metrics.getAverageExecutionTime() / 1_000_000.0,
                    metrics.getMaxExecutionTime() / 1_000_000.0,
                    metrics.getMinExecutionTime() / 1_000_000.0
                ))
            );
            fireTableDataChanged();
        }
        
        @Override
        public int getRowCount() {
            return data.size();
        }
        
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
        
        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            MethodMetricsRow row = data.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> row.methodName;
                case 1 -> row.invocations;
                case 2 -> String.format("%.2f", row.avgTime);
                case 3 -> String.format("%.2f", row.maxTime);
                case 4 -> String.format("%.2f", row.minTime);
                default -> null;
            };
        }
    }
    
    private record MethodMetricsRow(
        String methodName,
        long invocations,
        double avgTime,
        double maxTime,
        double minTime
    ) {}
} 