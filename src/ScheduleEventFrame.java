import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jdbc.DBConnection;
import org.w3c.dom.css.RGBColor;

class ScheduleEventFrame extends JFrame {

  private static int year, group_id, k;

  private static String formatMonth, selectedDay;

  private boolean isAdmin;

  JTabbedPane tab;

  private DefaultTableModel model;

  public ScheduleEventFrame(int temp) {}

  public ScheduleEventFrame(
    int group_id,
    int year,
    String formatMonth,
    String selectedDay,
    boolean isAdmin
  ) {
    this.group_id = group_id;
    this.year = year;
    this.formatMonth = formatMonth;
    this.selectedDay = selectedDay;
    this.isAdmin = isAdmin;
  }

  public ScheduleEventFrame() {
    setTitle("일정 등록");
    setSize(450, 340);
    setLocationRelativeTo(null);

    tab = new JTabbedPane();

    new ApprovedScheduleEvent_panel(group_id, year, formatMonth, selectedDay);

    new NonApprovedScheduleEvent_panel(
      group_id,
      year,
      formatMonth,
      selectedDay
    );

    tab.add("승인된 일정", new ApprovedScheduleEvent_panel());
    tab.setBackgroundAt(0, new Color(150, 150, 255));
    tab.setForegroundAt(0, Color.BLACK);
    tab.add("미승인된 일정", new NonApprovedScheduleEvent_panel());
    tab.setBackgroundAt(1, new Color(255, 150, 150));
    tab.setForegroundAt(1, new Color(100, 100, 100));

    tab.addChangeListener(
      new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
          if (tab.getSelectedIndex() == 0) {
            tab.setForegroundAt(0, Color.BLACK);
            tab.setForegroundAt(1, new Color(100, 100, 100));
          } else if (tab.getSelectedIndex() == 1) {
            tab.setForegroundAt(1, Color.BLACK);
            tab.setForegroundAt(0, new Color(100, 100, 100));
          }
        }
      }
    );

    add(tab);

    setVisible(true);
  }

  public static void main(String args[]) {
    new ScheduleEventFrame();
  }
}
