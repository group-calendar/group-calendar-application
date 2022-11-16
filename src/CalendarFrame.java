import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

class Calendar_panel extends JPanel {

  JFrame jf_scheduleEvent;

  JPanel days_panel, scheduleEvent_panel;

  JLabel lb_dateTitle;
  JLabel lb_week[] = new JLabel[7];

  JButton bt_prevMonth, bt_today, bt_nextMonth;
  JButton bt_days[] = new JButton[42];
  JButton bt_addSchedule;

  JTextField tf_scheduleContent;

  String str_week[] = { "일", "월", "화", "수", "목", "금", "토" };

  private static TableRowSorter<TableModel> sorter;

  int xPos = 0;
  int year, month, week, day;
  int dayCnt = 1, nextMonthDayCnt = 1;
  int monthSet[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
  int currentYear, currentMonth, currentDay, nowMonth;

  String header[] = { "번호", "내용", "수행 여부" };
  Object contents[][] = {
    {
      "1",
      "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요",
      true,
    },
    { "2", "방가방가", false },
    { "2", "방가방가", true },
    { "2", "방가방가", false },
    { "2", "방가방가", false },
    { "2", "방가방가", false },
    { "2", "방가방가", false },
    { "2", "방가방가", false },
    { "2", "방가방가", false },
    { "2", "방가방가", false },
    { "2", "방가방가", false },
  };

  public Calendar_panel() {
    setLayout(null);
    setBackground(Color.WHITE);

    // ---------------- 일정 등록 관련 JFrame ---------------
    jf_scheduleEvent = new JFrame();
    jf_scheduleEvent.setSize(350, 250);
    jf_scheduleEvent.setLocationRelativeTo(null);
    jf_scheduleEvent.setTitle("일정 등록");
    // -----------------------------------------------
    scheduleEvent_panel = new JPanel(null);
    scheduleEvent_panel.setBackground(Color.WHITE);

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

    bt_addSchedule = new JButton("추가");

    TableModel model = new DefaultTableModel(contents, header) {
      @Override
      public Class<?> getColumnClass(int column) {
        return getValueAt(0, column).getClass();
      }
    };
    DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
    celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
    DefaultTableCellRenderer celAlignLeft = new DefaultTableCellRenderer();
    celAlignLeft.setHorizontalAlignment(JLabel.LEFT);
    JTable table = new JTable(model);
    // table.setPreferredScrollableViewportSize(table.getPreferredSize());

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.getColumn("번호").setPreferredWidth(40);
    table.getColumn("번호").setCellRenderer(celAlignCenter);
    table.getColumn("내용").setPreferredWidth(220);
    table.getColumn("내용").setCellRenderer(celAlignLeft);
    table.getColumn("수행 여부").setPreferredWidth(50);
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
    JScrollPane scrollPane = new JScrollPane(table);

    tf_scheduleContent.setBounds(10, 6, 275, 30);
    bt_addSchedule.setBounds(292, 4, 50, 33);
    scrollPane.setBounds(10, 40, 330, 175);

    scheduleEvent_panel.add(tf_scheduleContent);
    scheduleEvent_panel.add(bt_addSchedule);
    scheduleEvent_panel.add(scrollPane);

    jf_scheduleEvent.add(scheduleEvent_panel);

    // ------------------ 달력을 담는 패널 정의 --------------------

    days_panel = new JPanel(new GridLayout(0, 7));
    days_panel.setBounds(0, 70, 1000, 480);

    // ----------------------- 날짜 및 캘린더 정의 -----------------------

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");
    SimpleDateFormat formatterMonth = new SimpleDateFormat("MM");
    SimpleDateFormat fomatterDay = new SimpleDateFormat("dd");
    currentYear = Integer.parseInt(formatterYear.format(calendar.getTime()));
    currentMonth = Integer.parseInt(formatterMonth.format(calendar.getTime()));
    currentDay = Integer.parseInt(fomatterDay.format(calendar.getTime()));
    lb_dateTitle = new JLabel(currentYear + "년 " + currentMonth + "월");
    lb_dateTitle.setFont(new Font("arial", Font.BOLD, 27));
    lb_dateTitle.setBounds(10, 10, 230, 30);

    for (int k = 0; k < 7; k++) {
      lb_week[k] = new JLabel(str_week[k]);
      // 밑 조건식은 일요일 월요일일 때 텍스트를 회색으로 표시하기 위한 조건
      if (k == 0 || k == 6) lb_week[k].setForeground(Color.GRAY);
      lb_week[k].setBounds(125 * (k + 1) + xPos, 10, 100, 100);
      xPos += 16.5;
      add(lb_week[k]);
    }

    //
    year = currentYear;
    month = currentMonth;
    nowMonth = month;

    if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) monthSet[1] =
      29; else monthSet[1] = 28; // 윤년의 조건.
    day =
      (year - 1) * 365 + (year - 1) / 4 - (year - 1) / 100 + (year - 1) / 400;
    for (int k = 0; k < month - 1; k++) {
      day += monthSet[k];
    }

    week = day % 7 + 1;

    for (int k = 0; k < month - 1; k++) {
      day += monthSet[k];
    }

    for (int k = 0; k < 42; k++) {
      // 이전 달일 때의 조건
      if (k == 0) {
        for (int z = 0; z < week; z++) {
          if (week == 7) break;
          bt_days[k] = new JButton();
          if (month - 2 < 0) bt_days[k].setText(
              "" + (monthSet[11] - week + k + 1) + "일"
            ); else bt_days[k].setText(
              "" + (monthSet[month - 2] - week + k + 1) + "일"
            );

          bt_days[k].setFont(new Font("arial", Font.PLAIN, 15));
          bt_days[k].setHorizontalAlignment(SwingConstants.RIGHT);
          bt_days[k].setVerticalAlignment(SwingConstants.TOP);
          bt_days[k].setEnabled(false);
          bt_days[k].addActionListener(
              new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  // String toDos = JOptionPane.showInputDialog(
                  //   null,
                  //   "메모할 내용을 입력해 주세요",
                  //   "할 일 추가",
                  //   JOptionPane.INFORMATION_MESSAGE
                  // );
                  // if (!toDos.equals(""))
                }
              }
            );
          days_panel.add(bt_days[k++]);
        }
      }

      // 현재 달일 때의 조건
      if (dayCnt == 1) bt_days[k] =
        new JButton(month + "월 " + dayCnt + "일"); else bt_days[k] =
        new JButton(dayCnt + "일");
      bt_days[k].setFont(new Font("arial", Font.PLAIN, 15));
      bt_days[k].setHorizontalAlignment(SwingConstants.RIGHT);
      bt_days[k].setVerticalAlignment(SwingConstants.TOP);
      if (k % 7 == 0 || k % 7 == 6) bt_days[k].setForeground(
          new Color(0, 0, 190)
        );

      // 현재 달일 때의 조건
      if (dayCnt <= monthSet[month - 1]) {
        if (bt_days[k].getText().equals(currentDay + "일")) {
          // bt_days[k].setForeground(Color.BLUE);
          bt_days[k].setText("📌 " + bt_days[k].getText());
        }
      }
      // 다음 달일 때의 조건
      if (dayCnt > monthSet[month - 1]) {
        if (nextMonthDayCnt == 1) if (month + 1 == 13) bt_days[k].setText(
            "1월 " + nextMonthDayCnt + "일"
          ); else bt_days[k].setText(
            month + 1 + "월 " + nextMonthDayCnt + "일"
          ); else bt_days[k].setText(nextMonthDayCnt + "일");
        bt_days[k].setEnabled(false);
        nextMonthDayCnt++;
      }

      bt_days[k].addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              // String toDos = JOptionPane.showInputDialog(
              //   null,
              //   "메모할 내용을 입력해 주세요",
              //   "할 일 추가",
              //   JOptionPane.INFORMATION_MESSAGE
              // );
              // System.out.println(toDos);
              // tf_scheduleContent.setText("");
              if (
                !tf_scheduleContent.getText().equals("새로운 이벤트")
              ) tf_scheduleContent.setText("새로운 이벤트");
              jf_scheduleEvent.setVisible(true);
              jf_scheduleEvent.requestFocus();
              // bt_temp.requestFocus();
              // tf_scheduleContent.setText("새로운 이벤트");
              // tf_scheduleContent.requestFocus(true);
            }
          }
        );
      days_panel.add(bt_days[k]);
      dayCnt++;
      week++;
    }
    // -----------------------------------------------------
    bt_prevMonth = new JButton("<");
    bt_prevMonth.setBounds(830, 10, 33, 33);
    bt_prevMonth.addActionListener(new MyActionListener());

    bt_today = new JButton("오늘");
    bt_today.setBounds(860, 10, 100, 33);
    bt_today.addActionListener(new MyActionListener());

    bt_nextMonth = new JButton(">");
    bt_nextMonth.setBounds(957, 10, 33, 33);
    bt_nextMonth.addActionListener(new MyActionListener());

    add(lb_dateTitle);
    add(bt_prevMonth);
    add(bt_today);
    add(bt_nextMonth);
    add(days_panel);
  }

  // --------------------- ActionListener --------------------------
  class MyActionListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == bt_prevMonth) {
        month--;
        if (month == 0) {
          year--;
          month = 12;
        }
        lb_dateTitle.setText((year + "년 " + month + "월"));

        dayCnt = 1;
        nextMonthDayCnt = 1;

        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) monthSet[1] =
          29; else monthSet[1] = 28; // 윤년의 조건.
        day =
          (year - 1) *
          365 +
          (year - 1) /
          4 -
          (year - 1) /
          100 +
          (year - 1) /
          400;
        for (int k = 0; k < month - 1; k++) {
          day += monthSet[k];
        }

        week = day % 7 + 1;

        for (int k = 0; k < month - 1; k++) {
          day += monthSet[k];
        }

        for (int k = 0; k < 42; k++) {
          if (k == 0) {
            for (int z = 0; z < week; z++) {
              if (week == 7) break;
              if (month - 2 < 0) bt_days[k].setText(
                  "" + (monthSet[11] - week + k + 1) + "일"
                ); else bt_days[k].setText(
                  "" + (monthSet[month - 2] - week + k + 1) + "일"
                );
              bt_days[k].setEnabled(false);
              k++;
            }
          }

          if (dayCnt == 1) bt_days[k].setText(
              month + "월 " + dayCnt + "일"
            ); else bt_days[k].setText(dayCnt + "일");
          bt_days[k].setEnabled(true);

          // 현재 달일 때의 조건
          if (dayCnt <= monthSet[month - 1] && month == nowMonth) {
            if (bt_days[k].getText().equals(currentDay + "일")) {
              bt_days[k].setText("📌 " + bt_days[k].getText());
            }
          }

          if (dayCnt > monthSet[month - 1]) {
            if (nextMonthDayCnt == 1) if (month + 1 == 13) bt_days[k].setText(
                "1월 " + nextMonthDayCnt + "일"
              ); else bt_days[k].setText(
                month + 1 + "월 " + nextMonthDayCnt + "일"
              ); else bt_days[k].setText(nextMonthDayCnt + "일");
            bt_days[k].setEnabled(false);
            nextMonthDayCnt++;
          }
          dayCnt++;
          week++;
        }
      } else if (e.getSource() == bt_today) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatterMonth = new SimpleDateFormat("MM");
        currentYear =
          Integer.parseInt(formatterYear.format(calendar.getTime()));
        currentMonth =
          Integer.parseInt(formatterMonth.format(calendar.getTime()));
        lb_dateTitle.setText((currentYear + "년 " + currentMonth + "월"));

        year = currentYear;
        month = currentMonth;
        dayCnt = 1;
        nextMonthDayCnt = 1;

        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) monthSet[1] =
          29; else monthSet[1] = 28; // 윤년의 조건.
        day =
          (year - 1) *
          365 +
          (year - 1) /
          4 -
          (year - 1) /
          100 +
          (year - 1) /
          400;
        for (int k = 0; k < month - 1; k++) {
          day += monthSet[k];
        }

        week = day % 7 + 1;

        for (int k = 0; k < month - 1; k++) {
          day += monthSet[k];
        }

        for (int k = 0; k < 42; k++) {
          if (k == 0) {
            for (int z = 0; z < week; z++) {
              if (week == 7) break;
              if (month - 2 < 0) bt_days[k].setText(
                  "" + (monthSet[11] - week + k + 1) + "일"
                ); else bt_days[k].setText(
                  "" + (monthSet[month - 2] - week + k + 1) + "일"
                );
              bt_days[k].setEnabled(false);
              k++;
            }
          }

          if (dayCnt == 1) bt_days[k].setText(
              month + "월 " + dayCnt + "일"
            ); else bt_days[k].setText(dayCnt + "일");
          bt_days[k].setEnabled(true);

          // 현재 달일 때의 조건
          if (dayCnt <= monthSet[month - 1]) {
            if (bt_days[k].getText().equals(currentDay + "일")) {
              bt_days[k].setText("📌 " + bt_days[k].getText());
            }
          }

          if (dayCnt > monthSet[month - 1]) {
            if (nextMonthDayCnt == 1) if (month + 1 == 13) bt_days[k].setText(
                "1월 " + nextMonthDayCnt + "일"
              ); else bt_days[k].setText(
                month + 1 + "월 " + nextMonthDayCnt + "일"
              ); else bt_days[k].setText(nextMonthDayCnt + "일");
            bt_days[k].setEnabled(false);
            nextMonthDayCnt++;
          }
          dayCnt++;
          week++;
        }
      } else if (e.getSource() == bt_nextMonth) {
        month++;
        if (month == 13) {
          year++;
          month = 1;
        }
        lb_dateTitle.setText((year + "년 " + month + "월"));

        dayCnt = 1;
        nextMonthDayCnt = 1;

        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) monthSet[1] =
          29; else monthSet[1] = 28; // 윤년의 조건.
        day =
          (year - 1) *
          365 +
          (year - 1) /
          4 -
          (year - 1) /
          100 +
          (year - 1) /
          400;
        for (int k = 0; k < month - 1; k++) {
          day += monthSet[k];
        }

        week = day % 7 + 1;

        for (int k = 0; k < month - 1; k++) {
          day += monthSet[k];
        }

        for (int k = 0; k < 42; k++) {
          if (k == 0) {
            for (int z = 0; z < week; z++) {
              if (week == 7) break;
              if (month - 2 < 0) bt_days[k].setText(
                  "" + (monthSet[11] - week + k + 1) + "일"
                ); else bt_days[k].setText(
                  "" + (monthSet[month - 2] - week + k + 1) + "일"
                );
              bt_days[k].setEnabled(false);
              k++;
            }
          }

          if (dayCnt == 1) bt_days[k].setText(
              month + "월 " + dayCnt + "일"
            ); else bt_days[k].setText(dayCnt + "일");
          bt_days[k].setEnabled(true);

          // 현재 달일 때의 조건
          if (dayCnt <= monthSet[month - 1] && month == nowMonth) {
            if (bt_days[k].getText().equals(currentDay + "일")) {
              bt_days[k].setText("📌 " + bt_days[k].getText());
            }
          }

          System.out.println(dayCnt + ", " + monthSet[month - 1]);

          if (dayCnt > monthSet[month - 1]) { // 정해진 날짜를 벗어날 때의 조건
            if (nextMonthDayCnt == 1) if (month + 1 == 13) bt_days[k].setText(
                "1월 " + nextMonthDayCnt + "일"
              ); else bt_days[k].setText(
                month + 1 + "월 " + nextMonthDayCnt + "일"
              ); else bt_days[k].setText(nextMonthDayCnt + "일");
            bt_days[k].setEnabled(false);
            nextMonthDayCnt++;
          }
          dayCnt++;
          week++;
        }
      }
      revalidate();
      repaint();
    }
  }
}

public class CalendarFrame extends JFrame {

  public CalendarFrame() {
    setTitle("캘린더");
    setSize(1000, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    add(new Calendar_panel());
    setVisible(true);
  }

  public static void main(String[] args) {
    new CalendarFrame();
  }
}
