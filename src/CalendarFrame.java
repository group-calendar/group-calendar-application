import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class Calendar_panel extends JPanel {

  private JPanel days_panel;

  private JLabel lb_dateTitle;
  private JLabel lb_week[] = new JLabel[7];

  private JButton bt_prevMonth, bt_today, bt_nextMonth;
  private JButton bt_days[] = new JButton[42];

  private String str_week[] = { "일", "월", "화", "수", "목", "금", "토" };
  private String selectedDay, formatMonth;

  private int xPos = 0, k;
  private int year, month, week, day;
  private int dayCnt = 1, nextMonthDayCnt = 1;
  private int monthSet[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
  private int currentYear, currentMonth, currentDay, nowMonth;
  private static int user_id;

  private boolean flag = false;

  public Calendar_panel(int user_id) {
    this.user_id = user_id;
  }

  public Calendar_panel() {
    setLayout(null);
    setBackground(Color.WHITE);

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

    for (k = 0; k < 7; k++) {
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

    for (k = 0; k < 42; k++) {
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
                  selectedDay = "";
                  flag = false;

                  // 클릭된 버튼 텍스트 내에서 숫자만 뽑아내는 코드
                  for (int z = 0; z < e.getActionCommand().length(); z++) {
                    if (e.getActionCommand().contains("월")) {
                      if (e.getActionCommand().charAt(z) == ' ') {
                        flag = true;
                        continue;
                      } else if (e.getActionCommand().charAt(z) == '일') break;
                      if (flag) selectedDay += e.getActionCommand().charAt(z);
                    } else {
                      if (
                        e.getActionCommand().charAt(z) == '일'
                      ) break; else if (
                        e.getActionCommand().charAt(z) >= '0' &&
                        e.getActionCommand().charAt(z) <= '9'
                      ) {
                        selectedDay += e.getActionCommand().charAt(z);
                      }
                    }
                  }

                  if (Integer.toString(month).length() == 1) {
                    formatMonth = "0" + month;
                  }

                  if (selectedDay.length() == 1) {
                    selectedDay = "0" + selectedDay;
                  }

                  System.out.println(
                    "선택된 일자(1): " +
                    year +
                    "-" +
                    formatMonth +
                    "-" +
                    selectedDay
                  );

                  new ScheduleEvent_panel(
                    user_id,
                    year,
                    formatMonth,
                    selectedDay
                  );
                  new ScheduleEventFrame();
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
              selectedDay = "";
              flag = false;

              // 클릭된 버튼 텍스트 내에서 숫자만 뽑아내는 코드
              for (int z = 0; z < e.getActionCommand().length(); z++) {
                if (e.getActionCommand().contains("월")) {
                  if (e.getActionCommand().charAt(z) == ' ') {
                    flag = true;
                    continue;
                  } else if (e.getActionCommand().charAt(z) == '일') break;
                  if (flag) selectedDay += e.getActionCommand().charAt(z);
                } else {
                  if (e.getActionCommand().charAt(z) == '일') break; else if (
                    e.getActionCommand().charAt(z) >= '0' &&
                    e.getActionCommand().charAt(z) <= '9'
                  ) {
                    selectedDay += e.getActionCommand().charAt(z);
                  }
                }
              }

              formatMonth = Integer.toString(month);
              if (Integer.toString(month).length() == 1) {
                formatMonth = "0" + month;
                System.out.println("테스트:" + formatMonth);
              }

              if (selectedDay.length() == 1) {
                selectedDay = "0" + selectedDay;
              }

              System.out.println(
                "선택된 일자(2): " +
                year +
                "-" +
                formatMonth +
                "-" +
                selectedDay
              );

              new ScheduleEvent_panel(user_id, year, formatMonth, selectedDay);
              new ScheduleEventFrame();
              // bt_temp.requestFocus();
              // tf_scheduleContent.setText("새로운 이벤트");
              // tf_scheduleContent.requestFocus(true);
              // INSERT INTO `simple_calendar`.`scheduleEvent` (`scheduleEvent_id`, `user_id`, `content`, `date`, `completed`) VALUES ('1', '125', '안농~~', '2022-11-01', 'true');
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
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    add(new Calendar_panel());
    setVisible(true);
  }

  public static void main(String[] args) {
    new CalendarFrame();
  }
}
