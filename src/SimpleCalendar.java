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
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class SimpleCalendar extends JFrame {

  JFrame jf_scheduleEvent;

  JPanel main_panel, days_panel, scheduleEvent_panel;
  JMenuBar menuBar;
  JMenu mn_function, mn_exit;
  JMenuItem mni_bookmark;

  JLabel lb_dateTitle;
  JLabel lb_week[] = new JLabel[7];

  JButton bt_prevMonth, bt_today, bt_nextMonth;
  JButton bt_days[] = new JButton[42];

  JTextField tf_scheduleContent;

  String str_week[] = { "일", "월", "화", "수", "목", "금", "토" };

  int xPos = 0;
  int year, month, week, day;
  int dayCnt = 1, nextMonthDayCnt = 1;
  int monthSet[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
  int currentYear, currentMonth, currentDay;

  public SimpleCalendar() {
    setTitle("Simple Calendar");
    setSize(1000, 600);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //---------------- 일정 등록 관련 JFrame ---------------
    jf_scheduleEvent = new JFrame();
    jf_scheduleEvent.setSize(350, 250);
    jf_scheduleEvent.setLocationRelativeTo(null);
    jf_scheduleEvent.setTitle("일정 등록");

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
    tf_scheduleContent.setBounds(10, 10, 200, 30);

    scheduleEvent_panel.add(tf_scheduleContent);

    jf_scheduleEvent.add(scheduleEvent_panel);

    //-----------------------------------------------

    main_panel = new JPanel(null);
    main_panel.setBackground(Color.WHITE);
    days_panel = new JPanel(new GridLayout(0, 7));
    days_panel.setBounds(0, 70, 1000, 480);

    menuBar = new JMenuBar();

    mn_function = new JMenu("기능");
    mn_exit = new JMenu("종료");
    mn_exit.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          // 종료 메뉴 클릭시 프로그램 종료
          System.exit(1);
        }
      }
    );
    mni_bookmark = new JMenuItem("북마크 추가");

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
      main_panel.add(lb_week[k]);
    }

    //
    year = currentYear;
    month = currentMonth;

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
        if (bt_days[k].getText().contains("" + currentDay)) {
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
    //

    bt_prevMonth = new JButton("<");
    bt_prevMonth.setBounds(830, 10, 33, 33);
    bt_prevMonth.addActionListener(new MyActionListener());

    bt_today = new JButton("오늘");
    bt_today.setBounds(860, 10, 100, 33);
    bt_today.addActionListener(new MyActionListener());

    bt_nextMonth = new JButton(">");
    bt_nextMonth.setBounds(957, 10, 33, 33);
    bt_nextMonth.addActionListener(new MyActionListener());

    mn_function.add(mni_bookmark);

    menuBar.add(mn_function);
    menuBar.add(mn_exit);

    main_panel.add(lb_dateTitle);
    main_panel.add(bt_prevMonth);
    main_panel.add(bt_today);
    main_panel.add(bt_nextMonth);

    main_panel.add(days_panel);
    add(main_panel);

    setJMenuBar(menuBar);
    setVisible(true);
    requestFocus();
  }

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
      main_panel.revalidate();
      main_panel.repaint();
    }
  }

  public static void main(String[] args) {
    new SimpleCalendar();
  }
}
