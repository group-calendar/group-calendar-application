import java.awt.CardLayout;
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

public class SimpleCalendar extends JFrame {

  JPanel main_panel;
  JMenuBar menuBar;
  JMenu mn_function, mn_exit;
  JMenuItem mni_calendar, mni_bookmark;

  CardLayout card = new CardLayout();

  private static int user_id;

  public SimpleCalendar(int user_id) {
    this.user_id = user_id;
    new SimpleCalendar();
  }

  public SimpleCalendar() {
    setTitle("Simple Calendar");
    setSize(1000, 600);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    main_panel = new JPanel(card);

    System.out.println("아잉눈: " + user_id);

    // ---------------------- 메뉴바 ----------------------

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

    mni_calendar = new JMenuItem("캘린더");
    mni_bookmark = new JMenuItem("북마크");

    mni_calendar.addActionListener(new MyActionListener());
    mni_bookmark.addActionListener(new MyActionListener());

    mn_function.add(mni_calendar);
    mn_function.add(mni_bookmark);

    menuBar.add(mn_function);
    menuBar.add(mn_exit);

    /////////////////////////////////////////////////

    new Bookmark_panel(user_id);

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
