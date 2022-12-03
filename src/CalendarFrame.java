import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import jdbc.DBConnection;

class Calendar_panel extends JPanel {

  public static JPanel days_panel;

  private JLabel lb_dateTitle, lb_groupImage, lb_groupName;
  private JLabel lb_week[] = new JLabel[7];

  private JButton bt_prevMonth, bt_today, bt_nextMonth;
  public static JButton bt_days[] = new JButton[42];

  private String str_week[] = { "일", "월", "화", "수", "목", "금", "토" };
  public static String selectedDay, formatMonth;

  private static ResultSet result;

  private int xPos = 0;
  private static int k;
  private static int year;
  private static int month;
  private int week;
  private int day;
  private int dayCnt = 1, nextMonthDayCnt = 1;
  private int monthSet[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
  private int currentYear, currentMonth, currentDay, nowMonth;
  private static int group_id;

  private static boolean flag = false;
  private static boolean flag2 = false;

  private static String query;
  private static String[] date;

  private static String groupName;

  private static DBConnection dbc = new DBConnection();

  private boolean isAdmin;

  public Calendar_panel(int group_id, String groupName, boolean isAdmin) {
    this.group_id = group_id;
    this.groupName = groupName;
    this.isAdmin = isAdmin;
  }

  public Calendar_panel() {
    setLayout(null);
    setBackground(Color.WHITE);

    // ------------------ 달력을 담는 패널 정의 --------------------

    days_panel = new JPanel(new GridLayout(0, 7));
    days_panel.setBounds(0, 70, 1000, 480);

    // ---------------------------------------------------------------

    ImageIcon icon = new ImageIcon("./images/groupImage.png");
    lb_groupImage = new JLabel(icon);
    lb_groupImage.setBounds(330, -25, 200, 100);

    lb_groupName = new JLabel(groupName);
    lb_groupName.setBounds(455, -20, 200, 100);
    lb_groupName.setFont(new Font("arial", Font.BOLD, 27));

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

                  // 날짜에서 일수만 뽑아내는 코드
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

                  formatMonth = Integer.toString(month);
                  if (formatMonth.length() == 1) {
                    formatMonth = "0" + month;
                    System.out.println("테스트:" + formatMonth);
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

                  new ScheduleEventFrame(
                    group_id,
                    year,
                    formatMonth,
                    selectedDay,
                    isAdmin
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
              if (formatMonth.length() == 1) {
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

              new ScheduleEventFrame(
                group_id,
                year,
                formatMonth,
                selectedDay,
                isAdmin
              );
              new ScheduleEventFrame();
              // bt_temp.requestFocus();
              // tf_scheduleContent.setText("새로운 이벤트");
              // tf_scheduleContent.requestFocus(true);
              // INSERT INTO `simple_calendar`.`scheduleEvent` (`scheduleEvent_id`, `group_id`, `content`, `date`, `completed`) VALUES ('1', '125', '안농~~', '2022-11-01', 'true');
            }
          }
        );
      days_panel.add(bt_days[k]);
      dayCnt++;
      week++;
    }

    setScheduledEventCnt();

    bt_prevMonth = new JButton("<");
    bt_prevMonth.setBounds(830, 10, 33, 33);
    bt_prevMonth.addActionListener(new MyActionListener());

    bt_today = new JButton("오늘");
    bt_today.setBounds(860, 10, 100, 33);
    bt_today.addActionListener(new MyActionListener());

    bt_nextMonth = new JButton(">");
    bt_nextMonth.setBounds(957, 10, 33, 33);
    bt_nextMonth.addActionListener(new MyActionListener());

    add(lb_groupImage);
    add(lb_groupName);
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
        setScheduledEventCnt();
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
        setScheduledEventCnt();
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
        setScheduledEventCnt();
      }
      revalidate();
      repaint();
    }
  }

  // DB에서 로그인한 유저의 등록된 모든 일정 데이터의 수를 캘린더로 불러옴
  public static void setScheduledEventCnt() {
    System.out.println("---------------------------------------------------");
    query =
      "select modify_time, count(scheduleEvent_id) from simple_calendar.scheduleEvent where group_id = " +
      group_id +
      " group by (modify_time) order by (modify_time)";
    try {
      result = dbc.selectData(query);
      int tempMonth = month - 1;
      int tempYear = year;
      int z = 0;
      flag2 = false;
      for (k = 0; result.next(); k++) {
        System.out.println("\n\n(DB): " + result.getString(1));
        date = result.getString(1).split("-");
        System.out.println("Entered month: " + tempMonth);
        // 년도 필터
        if (
          tempYear - Integer.parseInt(date[0]) > 1 ||
          (
            tempYear - Integer.parseInt(date[0]) == 1 &&
            Integer.parseInt(date[1]) == 12 &&
            tempMonth != 0
          ) ||
          (
            tempYear - Integer.parseInt(date[0]) == 1 &&
            Integer.parseInt(date[1]) > tempMonth &&
            Integer.parseInt(date[1]) != 12
          ) ||
          (
            tempYear - Integer.parseInt(date[0]) == 1 &&
            Integer.parseInt(date[1]) < 12
          )
        ) continue;
        System.out.println("1111111111111");

        // 월 필터
        if (
          (
            Integer.parseInt(date[1]) < tempMonth &&
            Integer.parseInt(date[1]) != tempMonth &&
            tempMonth != 12
          )
        ) continue;
        System.out.println("222222222222");

        for (; z < 42; z++) {
          flag = false;
          selectedDay = "";
          // 날짜에서 일만 뽑아내는 코드
          for (int i = 0; i < bt_days[z].getText().length(); i++) {
            if (bt_days[z].getText().contains("월")) {
              if (bt_days[z].getText().charAt(i) == ' ') {
                flag = true;
                continue;
              } else if (bt_days[z].getText().charAt(i) == '일') break;
              if (flag) selectedDay += bt_days[z].getText().charAt(i);
              System.out.println("\n\n뽑아 냄(1): " + selectedDay);
            } else {
              if (bt_days[z].getText().charAt(i) == '일') break; else if (
                bt_days[z].getText().charAt(i) >= '0' &&
                bt_days[z].getText().charAt(i) <= '9'
              ) {
                selectedDay += bt_days[z].getText().charAt(i);
              }
            }
          }

          System.out.println("\n\n뽑아 냄(2): " + selectedDay);

          if (selectedDay.length() == 1) {
            selectedDay = "0" + selectedDay;
          }

          if (selectedDay.equals("01")) {
            if (flag2 == true) {
              tempMonth--;
              flag = false;
            }
            tempMonth++;
            if (tempMonth == 13) {
              tempYear++;
              tempMonth = 1;
            }
          }

          System.out.println("수정된 날짜 확인: " + tempMonth);

          // selectedDay가 1로 시작하는데, 만약 이전 달 1일에 등록된 일정이 있다면,
          // 현재 보고 있는 달 1일의 태스크 수가 나타나지 않는 문제가 있었음.
          if (
            z == 0 &&
            Integer.parseInt(selectedDay) == 1 &&
            Integer.parseInt(date[2]) == 1 &&
            tempMonth - Integer.parseInt(date[1]) == 1
          ) {
            System.out.println("에잉...??");
            flag2 = true;
            z = 0;
            break;
          }

          if (tempMonth == 0) {
            tempYear--;
            tempMonth = 12;
          }

          System.out.println(
            "아잉눈: " +
            date[1] +
            " / tempMonth: " +
            tempMonth +
            " / " +
            selectedDay
          );

          if (
            tempMonth >= Integer.parseInt(date[1]) &&
            Integer.parseInt(date[2]) < Integer.parseInt(selectedDay) &&
            tempMonth != 12 &&
            Integer.parseInt(selectedDay) != 31
          ) {
            break;
          }

          if (
            month == 1 &&
            Integer.parseInt(date[1]) == 12 &&
            z == 0 &&
            selectedDay.equals("01")
          ) {
            tempMonth--;
            break;
          }

          formatMonth = Integer.toString(tempMonth);
          if (formatMonth.length() == 1) {
            formatMonth = "0" + tempMonth;
          }

          System.out.println("DB: " + date[0] + "-" + date[1] + "-" + date[2]);
          System.out.println(
            "PC: " + year + "-" + formatMonth + "-" + selectedDay + ""
          );

          System.out.println(
            "확인: " + tempYear + "-" + formatMonth + "-" + selectedDay
          );

          if (
            Integer.parseInt(date[0]) == tempYear &&
            Integer.parseInt(date[1]) == tempMonth &&
            Integer.parseInt(date[2]) == Integer.parseInt(selectedDay)
          ) {
            System.out.println(
              "eiugherighweioughweriuoghweoiughwerouihgrewiuoghwe"
            );
            bt_days[z].setText(
                bt_days[z].getText() + " (" + result.getString(2) + ")"
              );
            z++;
            break;
          }
        }
      }
    } catch (Exception error) {
      System.out.println("DB 쿼리 실행 실패");
      System.out.print("사유 : " + error.getMessage());
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

  public void main(String[] args) {
    new CalendarFrame();
  }
}
