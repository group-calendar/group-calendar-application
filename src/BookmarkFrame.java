import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jdbc.DBConnection;

class Bookmark_panel extends JPanel {

  private JLabel lb_title, lb_url, lb_bookmarkImage;

  private JTextField tf_title, tf_url;

  private JButton bt_addBookmark, bt_deleteBookmark, bt_modifyBookmark, bt_websiteOpen;

  private TableRowSorter<TableModel> sorter;

  private ResultSet result;

  private DefaultTableModel model;

  private String query;

  private String header[] = { "번호", "사이트 명", "주소" };
  private Object contents[][];

  private JTable table;

  private int k, peopleCnt, row, insertUpdateDeleteDataResult;

  private static int group_id;

  private boolean isAdmin;

  DBConnection dbc = new DBConnection();

  public Bookmark_panel(int group_id, boolean isAdmin) {
    this.group_id = group_id;
    this.isAdmin = isAdmin;
  }

  public Bookmark_panel() {
    setLayout(null);
    setBackground(new Color(245, 245, 245));

    // 유저의 북마크 데이터들을 DB에서 불러옴
    updatingtableData();

    ImageIcon icon = new ImageIcon("./images/bookmark.png");
    lb_bookmarkImage = new JLabel(icon);

    lb_title = new JLabel("사이트 명");
    lb_title.setFont(new Font("arial", Font.PLAIN, 13));

    lb_url = new JLabel("주소");
    lb_url.setFont(new Font("arial", Font.PLAIN, 13));

    tf_title = new JTextField();
    tf_title.setBorder(
      BorderFactory.createLineBorder(new Color(155, 179, 255))
    );
    tf_url = new JTextField();
    tf_url.setBorder(BorderFactory.createLineBorder(new Color(155, 179, 255)));

    bt_addBookmark = new JButton("추가");
    bt_modifyBookmark = new JButton("수정");
    bt_deleteBookmark = new JButton("삭제");
    bt_websiteOpen = new JButton("이동");

    model =
      new DefaultTableModel(contents, header) {
        @Override
        public Class<?> getColumnClass(int column) {
          switch (column) {
            case 0:
              return Integer.class;
            case 1:
              return String.class;
            case 2:
              return String.class;
            default:
              return String.class;
          }
        }
      };
    DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
    celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
    DefaultTableCellRenderer celAlignLeft = new DefaultTableCellRenderer();
    celAlignLeft.setHorizontalAlignment(JLabel.LEFT);
    table = new JTable(model);

    table.getTableHeader().setFont(new Font("arial", Font.PLAIN, 15));

    // 테이블 내 데이터 행 셀의 높이 지정
    table.setRowHeight(25);
    // table.setPreferredScrollableViewportSize(table.getPreferredSize());

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.getColumn("번호").setPreferredWidth(50);
    table.getColumn("번호").setCellRenderer(celAlignCenter);
    table.getColumn("사이트 명").setPreferredWidth(150);
    table.getColumn("사이트 명").setCellRenderer(celAlignLeft);
    table.getColumn("주소").setPreferredWidth(401);
    table.getColumn("주소").setCellRenderer(celAlignLeft);
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
    lb_bookmarkImage.setBounds(-65, -130, 500, 500);

    lb_title.setBounds(17, 238, 80, 50);
    tf_title.setBounds(80, 245, 285, 33);
    lb_url.setBounds(30, 279, 100, 50);
    tf_url.setBounds(80, 287, 285, 33);
    bt_addBookmark.setBounds(13, 336, 352, 45);
    bt_deleteBookmark.setBounds(13, 387, 352, 45);
    bt_modifyBookmark.setBounds(13, 437, 352, 45);
    bt_websiteOpen.setBounds(13, 487, 352, 45);
    scrollPane.setBounds(380, 20, 600, 510);
    bt_addBookmark.addActionListener(new MyActionListener());

    bt_modifyBookmark.addActionListener(new MyActionListener());
    bt_deleteBookmark.addActionListener(new MyActionListener());
    bt_websiteOpen.addActionListener(new MyActionListener());
    add(lb_bookmarkImage);

    add(lb_title);
    add(lb_url);
    add(tf_title);
    add(tf_url);
    add(bt_addBookmark);
    add(bt_modifyBookmark);
    add(bt_deleteBookmark);
    add(bt_websiteOpen);
    add(scrollPane);
  }

  // --------------------- ActionListener --------------------------
  class MyActionListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == bt_addBookmark) {
        if (tf_title.getText().equals("") || tf_url.getText().equals("")) {
          JOptionPane.showMessageDialog(
            null,
            "사이트 명 혹은 주소를 모두 입력해 주세요.",
            "추가 실패",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }
        query =
          "INSERT INTO `simple_calendar`.`bookmark` (`group_id`, `title`, `url`) VALUES (" +
          group_id +
          ", '" +
          tf_title.getText() +
          "', '" +
          tf_url.getText() +
          "')";
        System.out.println(query);
        try {
          insertUpdateDeleteDataResult = dbc.insertUpdateDeleteData(query);
          System.out.println(insertUpdateDeleteDataResult);
          Object[] newRow = { ++k, tf_title.getText(), tf_url.getText() };
          model.addRow(newRow);
          updatingtableData();
          tf_title.setText("");
          tf_url.setText("");
        } catch (Exception error) {
          System.out.println("DB 쿼리 실행 실패");
          System.out.print("사유 : " + error.getMessage());
        }
      } else if (e.getSource() == bt_deleteBookmark) {
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
          "DELETE FROM `simple_calendar`.`bookmark` WHERE (`bookmark_id` = " +
          contents[row][3] +
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
      } else if (e.getSource() == bt_modifyBookmark) {
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

        query =
          "UPDATE simple_calendar.bookmark SET title = '" +
          table.getValueAt(row, 1) +
          "', url = '" +
          table.getValueAt(row, 2) +
          "' WHERE (bookmark_id = " +
          contents[row][3] +
          ")";
        System.out.println(query);
        try {
          insertUpdateDeleteDataResult = dbc.insertUpdateDeleteData(query);
        } catch (Exception error) {
          System.out.println("DB 쿼리 실행 실패");
          System.out.print("사유 : " + error.getMessage());
        }
      } else if (e.getSource() == bt_websiteOpen) {
        try {
          row = table.getSelectedRow();
          if (row == -1) {
            JOptionPane.showMessageDialog(
              null,
              "이동할 사이트를 선택해 주세요.",
              "이동 실패",
              JOptionPane.ERROR_MESSAGE
            );
            return;
          }

          Desktop
            .getDesktop()
            .browse(new URI(table.getValueAt(row, 2).toString()));
        } catch (Exception error) {
          System.out.println(error);
          JOptionPane.showMessageDialog(
            null,
            "올바른 홈페이지 주소가 아닙니다.\n올바른 주소로 다시 수정해 주세요.",
            "URL 오류",
            JOptionPane.ERROR_MESSAGE
          );
        }
      }
    }
  }

  void updatingtableData() {
    try {
      query =
        "select count(*) from simple_calendar.bookmark where group_id = " +
        group_id;

      System.out.println(query);
      result = dbc.selectData(query);
      result.next();
      peopleCnt = Integer.parseInt(result.getString(1));
      contents = new Object[peopleCnt][4];

      query =
        "SELECT title, url, bookmark_id FROM simple_calendar.bookmark WHERE group_id = " +
        group_id;
      System.out.println(query);
      result = dbc.selectData(query);
      for (k = 0; result.next(); k++) {
        contents[k][0] = (k + 1); // 북마크 순서 번호( 1 ~ result.next()까지 )
        contents[k][1] = result.getString(1); // 사이트 제목
        contents[k][2] = result.getString(2); // 사이트 주소
        contents[k][3] = result.getString(3); // bookmark_id (PK, 기본키)
      }
    } catch (SQLException error) {
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

public class BookmarkFrame extends JFrame {

  public BookmarkFrame() {
    setTitle("북마크");
    setSize(1000, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    add(new Bookmark_panel());
    setVisible(true);
  }

  public static void main(String[] args) {
    new BookmarkFrame();
  }
}
