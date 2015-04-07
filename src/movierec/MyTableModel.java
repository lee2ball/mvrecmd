package movierec;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import javax.swing.border.*;
 public class MyTableModel extends AbstractTableModel {
  private String headName[];
  private Object obj[][];
  
  public MyTableModel() {
   super();
  }
  
  public MyTableModel(String[] headName, Object[][] obj) {
   this();
   this.headName = headName;
   this.obj = obj;
   
  }
 // public void setRowHeight(int rowHeight){
	  
 // }
  public int getColumnCount() {
   return headName.length;
  }

  public int getRowCount() {
   return obj.length;
  }

  public Object getValueAt(int r, int c) {
   return obj[r][c];
  }

  public String getColumnName(int c) {
   return headName[c];
  }

  public Class<?> getColumnClass(int columnIndex) {
   return obj[0][columnIndex].getClass();
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
	  return false;//使单元格能选中但不能编辑
  }

 }
 class ComboBoxCellRenderer implements TableCellRenderer {
	 protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	 private Color unselectedForeground;
	 private Color unselectedBackground;
	 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	  JButton cmb = (JButton) value;
	  if (isSelected) {
	   cmb.setForeground(table.getSelectionForeground());
	   cmb.setBackground(table.getSelectionBackground());
	  } else {
	   cmb.setForeground((unselectedForeground != null) ? unselectedForeground: table.getForeground());
	   cmb.setBackground((unselectedBackground != null) ? unselectedBackground: table.getBackground());
	  }
	  cmb.setFont(table.getFont());
	  if (hasFocus) {
	   cmb.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
	   if (!isSelected && table.isCellEditable(row, column)) {
	    Color col;
	    col = UIManager.getColor("Table.focusCellForeground");
	    if (col != null) {
	     cmb.setForeground(col);
	    }
	    col = UIManager.getColor("Table.focusCellBackground");
	    if (col != null) {
	     cmb.setBackground(col);
	    }
	   }
	  } else {
	   cmb.setBorder(noFocusBorder);
	  }
	  return cmb;
	 }
	}
