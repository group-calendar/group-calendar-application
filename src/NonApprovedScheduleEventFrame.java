import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jdbc.DBConnection;

class NonApprovedScheduleEvent_panel extends JPanel {

  private JButton bt_addSchedule, bt_cancel, bt_deleteSchedule, bt_modifySchedule;

  private JTextField tf_scheduleContent;

  private TableRowSorter<TableModel> sorter;

  private JTable table;

  private JScrollPane scrollPane;

  private int peopleCnt, insertUpdateDeleteDataResult, row;
  private static int year, group_id, k;

  private String query;
  private static String formatMonth, selectedDay;

  String header[] = { "번호", "내용", "수행 여부", "승인 여부" };
  private Object contents[][];

  private ResultSet result;

  private DBConnection dbc = new DBConnection();

  private DefaultTableModel model;

  SimpleCalendar sc = new SimpleCalendar(1);

  public NonApprovedScheduleEvent_panel(
    int group_id,
    int year,
    String formatMonth,
    String seleString
  ) {
    this.group_id = group_id;
    this.year = year;
    this.formatMonth = formatMonth;
    this.selectedDay = seleString;
  }

  public NonApprovedScheduleEvent_panel() {
    setLayout(null);
    setBackground(Color.WHITE);

    updatingtableData();

    tf_scheduleContent =
      new JTextField("새로운 이벤트") {
        @Override
        public void setBorder(Border border) {}
      };
    tf_scheduleContent.addFocusListener(
      new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
          if (tf_scheduleContent.getText().equals("새로운 이벤트")) {
            tf_scheduleContent.setText("");
            tf_scheduleContent.setForeground(Color.BLACK);
          }
        }

        @Override
        public void focusLost(FocusEvent e) {
          if (
            tf_scheduleContent.getText().equals("새로운 이벤트") ||
            tf_scheduleContent.getText().length() == 0
          ) {
            tf_scheduleContent.setText("새로운 이벤트");
            tf_scheduleContent.setForeground(Color.GRAY);
          } else {
            tf_scheduleContent.setForeground(Color.BLACK);
          }
        }
      }
    );
    tf_scheduleContent.setForeground(Color.GRAY);

    if (
      !tf_scheduleContent.getText().equals("새로운 이벤트")
    ) tf_scheduleContent.setText("새로운 이벤트");

    bt_addSchedule = new JButton("추가");
    bt_cancel = new JButton("취소");
    bt_cancel.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          JComponent comp = (JComponent) e.getSource();
          Window win = SwingUtilities.getWindowAncestor(comp);
          win.dispose();
        }
      }
    );
    bt_deleteSchedule = new JButton("삭제");
    bt_modifySchedule = new JButton("수정");

    model =
      new DefaultTableModel(contents, header) {
        @Override
        public Class<?> getColumnClass(int column) {
          return getValueAt(0, column).getClass();
        }
      };

    DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
    celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
    DefaultTableCellRenderer celAlignLeft = new DefaultTableCellRenderer();
    celAlignLeft.setHorizontalAlignment(JLabel.LEFT);
    table = new JTable(model);
    table.getTableHeader().setFont(new Font("arial", Font.PLAIN, 13));
    // 테이블 내 데이터 행 셀의 높이 지정
    table.setRowHeight(15);
    // table.setPreferredScrollableViewportSize(table.getPreferredSize());

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.getColumn("번호").setPreferredWidth(40);
    table.getColumn("번호").setCellRenderer(celAlignCenter);
    table.getColumn("내용").setPreferredWidth(220);
    table.getColumn("내용").setCellRenderer(celAlignLeft);
    table.getColumn("수행 여부").setPreferredWidth(75);
    // -------- true And false checkbox filter --------
    // RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
    //   public boolean include(Entry entry) {
    //     Boolean bol = (Boolean) entry.getValue(2);
    //     return bol.booleanValue() == true;
    //   }
    // };
    sorter = new TableRowSorter<TableModel>(model);
    // sorter.setRowFilter(filter);
    table.setRowSorter(sorter);
    scrollPane = new JScrollPane(table);

    tf_scheduleContent.setBounds(10, 6, 275, 30);
    bt_addSchedule.setBounds(342, 4, 80, 33);
    bt_cancel.setBounds(8, 221, 135, 35);
    bt_modifySchedule.setBounds(143, 221, 135, 35);
    bt_deleteSchedule.setBounds(277, 221, 135, 35);
    scrollPane.setBounds(10, 40, 410, 175);

    bt_addSchedule.addActionListener(new MyActionListener());
    bt_modifySchedule.addActionListener(new MyActionListener());
    bt_deleteSchedule.addActionListener(new MyActionListener());

    add(tf_scheduleContent);
    add(bt_addSchedule);
    add(bt_cancel);
    add(bt_deleteSchedule);
    add(bt_modifySchedule);
    add(scrollPane);

    setVisible(true);
  }

  class MyActionListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == bt_addSchedule) {
        if (
          tf_scheduleContent.getText().equals("") ||
          tf_scheduleContent.getText().equals("새로운 이벤트")
        ) {
          JOptionPane.showMessageDialog(
            null,
            "일정을 입력해 주세요.",
            "추가 실패",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }

        query =
          "INSERT INTO `simple_calendar`.`scheduleEvent` (`group_id`, `content`, `modify_time`, `completed`, `approved`) VALUES (" +
          group_id +
          ", '" +
          tf_scheduleContent.getText() +
          "', '" +
          year +
          "-" +
          formatMonth +
          "-" +
          selectedDay +
          "', 'false', 'false');";

        System.out.println(query);
        try {
          insertUpdateDeleteDataResult = dbc.insertUpdateDeleteData(query);
          System.out.println(insertUpdateDeleteDataResult);
          Object[] newRow = { ++k, tf_scheduleContent.getText(), false, false };
          model.addRow(newRow);
          updatingtableData();
          tf_scheduleContent.setText("");
        } catch (Exception error) {
          System.out.println("DB 쿼리 실행 실패");
          System.out.print("사유 : " + error.getMessage());
        }
      } else if (e.getSource() == bt_deleteSchedule) {
        row = table.getSelectedRow();
        if (row == -1) {
          JOptionPane.showMessageDialog(
            null,
            "삭제할 데이터의 행을 선택해 주세요.",
            "제거 실패",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }
        query =
          "DELETE FROM `simple_calendar`.`scheduleEvent` WHERE (`scheduleEvent_id` = " +
          contents[row][4] +
          ")";
        System.out.println(query);
        try {
          insertUpdateDeleteDataResult = dbc.insertUpdateDeleteData(query);
          System.out.println(insertUpdateDeleteDataResult);
          if (insertUpdateDeleteDataResult == 1) {
            model.removeRow(row);
            k--;
            updatingtableData();

            // 삭제했던 행을 강제로 다시 선택함
            if (peopleCnt > row) table.setRowSelectionInterval(
              row,
              row
            ); else if (
              peopleCnt == row && peopleCnt != 0
            ) table.setRowSelectionInterval(row - 1, row - 1);

            // 테이블 행 번호 칼럼 갱신
            for (int z = 0; z < table.getRowCount(); z++) table.setValueAt(
              z + 1,
              z,
              0
            );
          }
        } catch (Exception error) {
          System.out.println("DB 쿼리 실행 실패");
          System.out.print("사유 : " + error.getMessage());
        }
      } else if (e.getSource() == bt_modifySchedule) {
        row = table.getSelectedRow();
        if (row == -1) {
          JOptionPane.showMessageDialog(
            null,
            "수정할 데이터의 행을 선택해 주세요.",
            "업데이트 실패",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }

        // 미승인 일정 탭에서 승인 여부 체크 시 현재 권한이 어드민인지 확인
        if ((Boolean) table.getValueAt(row, 3) == true && !checkIsAdmin()) {
          table.setValueAt(false, row, 3);
          return;
        }

        query =
          "UPDATE `simple_calendar`.`scheduleEvent` SET `content` = '" +
          table.getValueAt(row, 1) +
          "', `completed` = '" +
          table.getValueAt(row, 2) +
          "', `approved` = '" +
          table.getValueAt(row, 3) +
          "' WHERE (`scheduleEvent_id` = " +
          contents[row][4] +
          ")";
        System.out.println(query);
        try {
          insertUpdateDeleteDataResult = dbc.insertUpdateDeleteData(query);
          System.out.println(insertUpdateDeleteDataResult);
          JOptionPane.showMessageDialog(
            null,
            "일정이 수정되었습니다.",
            "업데이트 완료",
            JOptionPane.PLAIN_MESSAGE
          );
        } catch (Exception error) {
          System.out.println("DB 쿼리 실행 실패");
          System.out.print("사유 : " + error.getMessage());
        }
      }
      for (int i = 0; i < 42; i++) {
        String str = Calendar_panel.bt_days[i].getText();
        if (str != null && str.endsWith(")")) {
          while (!str.endsWith("(")) {
            str = str.substring(0, str.length() - 1);
          }
          str = str.substring(0, str.length() - 2);
          Calendar_panel.bt_days[i].setText(str);
        }
      }
      Calendar_panel.setScheduledEventCnt();
      Calendar_panel.days_panel.revalidate();
      Calendar_panel.days_panel.repaint();
    }
  }

  boolean checkIsAdmin() {
    if (!sc.isAdmin) {
      JOptionPane.showMessageDialog(
        null,
        "제어 권한이 없습니다.\n관리자에게 문의해 주세요.",
        "추가 실패",
        JOptionPane.ERROR_MESSAGE
      );
      return false;
    }
    return true;
  }

  void updatingtableData() {
    try {
      query =
        "select count(*) from simple_calendar.scheduleEvent where group_id = " +
        group_id +
        " AND modify_time LIKE '" +
        year +
        "-" +
        formatMonth +
        "-" +
        selectedDay +
        "' AND approved LIKE 'false';";
      System.out.println(query);

      result = dbc.selectData(query);
      result.next();
      peopleCnt = Integer.parseInt(result.getString(1));
      contents = new Object[peopleCnt][5];
      query =
        "SELECT content, completed, approved, scheduleEvent_id FROM simple_calendar.scheduleEvent WHERE group_id = " +
        group_id +
        " AND modify_time LIKE '" +
        year +
        "-" +
        formatMonth +
        "-" +
        selectedDay +
        "' AND approved LIKE 'false';";

      System.out.println(query);
      result = dbc.selectData(query);
      for (k = 0; result.next(); k++) {
        contents[k][0] = (k + 1); // 테스크 순서 번호( 1 ~ result.next()까지 )
        contents[k][1] = result.getString(1); // 태스크 내용
        contents[k][2] = Boolean.parseBoolean(result.getString(2)); // 태스크 수행 여부
        contents[k][3] = Boolean.parseBoolean(result.getString(3)); // 태스크 승인 여부
        contents[k][4] = result.getString(4); // scheduleEvent_id (PK, 기본키)
      }
    } catch (Exception error) {
      System.out.println("DB 쿼리 실행 실패");
      System.out.print("사유 : " + error.getMessage());
      // JOptionPane.showMessageDialog(
      //   null,
      //   "로그인이 필요한 서비스입니다. \n로그인 후에 이용해 주세요",
      //   "로그인 실패",
      //   JOptionPane.ERROR_MESSAGE
      // );
    }
  }
}

public class NonApprovedScheduleEventFrame extends JFrame {

  public NonApprovedScheduleEventFrame() {
    setTitle("미승인된 일정 테이블 프레임");
    setSize(430, 290);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    add(new NonApprovedScheduleEvent_panel());
    setVisible(true);
  }

  public static void main(String args[]) {
    new NonApprovedScheduleEventFrame();
  }
}
