package org.example.template;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class HtmlTableTemplate {
    private String tableStyle;
    private String cellStyle;
    private Table<String, String, String> innerTable = HashBasedTable.create();

    public void addCell(String moduleName, String serviceName, String value) {
        innerTable.put(moduleName, serviceName, value);
    }

    public String toString() {
        StringBuilder result = new StringBuilder("<table style='"+ tableStyle +"'>\n\t<tr><th>module name</th><th>service/bean name</th></tr>\n");
        innerTable.cellSet().stream().forEach( cell -> {
            String td = cellStyle == null ? "<td>" : "<td style='"+cellStyle+"'>";
            if (cell.getValue().isEmpty()) {
                result.append("\t<tr>" + td + cell.getRowKey() + "</td>" + td + cell.getColumnKey() + "</td></tr>\n");
            } else {
                result.append("\t<tr>" + td + cell.getRowKey() + "</td>" + td + "<a href='" +cell.getValue()+"'>" + cell.getColumnKey() + "</a></td></tr>\n");
            }
        } );
        result.append("</table>\n");
        return result.toString();
    }

    public static void main(String[] args) {
        var table = new HtmlTableTemplate();
        table.addCell("simple-init-module", "kernel-holder", "/kernel/service/modules.simple-init-module.kernel-holder");
        table.addCell("simple-init-module", "simple-thread-service", "");
        table.addCell("simple-init-module", "other-objects-factory", "");
        table.addCell("simple-init-module", "ace2", "");
        table.addCell("simple-init-module", "ddos-attack-preventer", "");
        System.err.println(table);
    }

    public void setTableStyle(String tableStyle) {
        this.tableStyle = tableStyle;
    }

    public void setCellStyle(String cellStyle) {
        this.cellStyle = cellStyle;
    }
}
