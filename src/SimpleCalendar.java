import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SimpleCalendar extends JFrame {

  private JPanel main_panel;
  private JMenuBar menuBar;
  private JMenu mn_function, mn_signOut, mn_exit;
  static JMenu mn_changeRole, mn_role;
  private JMenuItem mni_calendar, mni_bookmark;

  CardLayout card = new CardLayout();

  static boolean isAdmin = false;
  private static int group_id;

  private static String groupName;

  public SimpleCalendar(int temp) {}

  public SimpleCalendar(int group_id, String groupName) {
    this.group_id = group_id;
    this.groupName = groupName;
    new SimpleCalendar();
  }

  public SimpleCalendar() {
    setTitle("그룹 일정 관리");
    setSize(1000, 600);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    main_panel = new JPanel(card);

    // ---------------------- 메뉴바 ----------------------

    menuBar = new JMenuBar();
    menuBar.setOpaque(true);

    mn_function = new JMenu("기능");

    mn_changeRole = new JMenu("관리자 전환");
    mn_changeRole.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          if (mn_changeRole.getText().equals("일반유저로 전환")) {
            isAdmin = false;
            mn_role.setText("일반 유저");
            mn_changeRole.setText("관리자 전환");
            JOptionPane.showMessageDialog(
              null,
              "일반 유저로 변경되었습니다.",
              "변경 완료",
              JOptionPane.PLAIN_MESSAGE
            );
            return;
          }
          new GetAdminRoleFrame(group_id);
          new GetAdminRoleFrame();
        }
      }
    );

    mn_signOut = new JMenu("로그아웃");
    mn_signOut.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          isAdmin = false;
          dispose();
          new LoginFrame();
        }
      }
    );

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

    mn_role = new JMenu("일반 유저");
    mn_role.setEnabled(false);

    mni_calendar = new JMenuItem("캘린더");
    mni_bookmark = new JMenuItem("북마크");

    mni_calendar.addActionListener(new MyActionListener());
    mni_bookmark.addActionListener(new MyActionListener());

    mn_function.add(mni_calendar);
    mn_function.add(mni_bookmark);

    menuBar.add(mn_function);
    menuBar.add(mn_changeRole);
    menuBar.add(mn_signOut);
    menuBar.add(mn_exit);
    menuBar.add(mn_role);

    /////////////////////////////////////////////////

    new Calendar_panel(group_id, groupName, isAdmin);
    new Bookmark_panel(group_id, isAdmin);

    main_panel.add("calendarPan", new Calendar_panel());
    main_panel.add("bookmarkPan", new Bookmark_panel());

    ////////////////////////////////////////////////

    add(main_panel);

    setJMenuBar(menuBar);
    setVisible(true);
    requestFocus();
  }

  // --------------------- ActionListener --------------------------

  class MyActionListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      // ******************* 메뉴바 액션 리스너 *******************
      if (e.getSource() == mni_calendar) {
        card.show(main_panel, "calendarPan");
      } else if (e.getSource() == mni_bookmark) {
        card.show(main_panel, "bookmarkPan");
      }
    }
  }

  public static void main(String[] args) {
    new SimpleCalendar();
  }
}
