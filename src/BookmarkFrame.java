import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

class Bookmark_panel extends JPanel {

  JLabel lb_title, lb_url, lb_bookmarkImage, lb_bookmarkImage2;

  JTextField tf_title, tf_url;

  JButton bt_addBookmark, bt_deleteBookmark, bt_modifyBookmark;

  private static TableRowSorter<TableModel> sorter;
  TableCellRenderer tableRenderer;

  String header[] = { "번호", "사이트 명", "주소" };
  Object contents[][] = {
    { "1", "네이버", "https://www.naver.com/" },
    { "2", "네이버", "https://www.naver.com/" },
    {
      "3",
      "네이버",
      "https://www.naver.com/https://www.naver.com/https://www.naver.com/https://www.naver.com/https://www.naver.com/https://www.naver.com/",
    },
  };

  public Bookmark_panel() {
    setLayout(null);
    setBackground(new Color(245, 245, 245));

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
    bt_deleteBookmark = new JButton("수정");
    bt_modifyBookmark = new JButton("삭제");

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
    tableRenderer = table.getDefaultRenderer(JButton.class);
    table.setDefaultRenderer(
      JButton.class,
      new JTableButtonRenderer(tableRenderer)
    );
    // table.setPreferredScrollableViewportSize(table.getPreferredSize());

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.getColumn("번호").setPreferredWidth(50);
    table.getColumn("번호").setCellRenderer(celAlignCenter);
    table.getColumn("사이트 명").setPreferredWidth(150);
    table.getColumn("사이트 명").setCellRenderer(celAlignLeft);
    table.getColumn("주소").setPreferredWidth(390);
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

    lb_bookmarkImage.setBounds(-130, -205, 500, 500);
    lb_title.setBounds(17, 283, 80, 50);
    tf_title.setBounds(80, 292, 285, 33);
    lb_url.setBounds(30, 325, 100, 50);
    tf_url.setBounds(80, 336, 285, 33);
    bt_addBookmark.setBounds(13, 387, 352, 45);
    bt_deleteBookmark.setBounds(13, 437, 352, 45);
    bt_modifyBookmark.setBounds(13, 487, 352, 45);
    scrollPane.setBounds(380, 20, 600, 510);

    add(lb_bookmarkImage);
    add(lb_title);
    add(lb_url);
    add(tf_title);
    add(tf_url);
    add(bt_addBookmark);
    add(bt_deleteBookmark);
    add(bt_modifyBookmark);
    add(scrollPane);
  }

  class JTableButtonRenderer implements TableCellRenderer {

    private TableCellRenderer defaultRenderer;

    public JTableButtonRenderer(TableCellRenderer renderer) {
      defaultRenderer = renderer;
    }

    public Component getTableCellRendererComponent(
      JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column
    ) {
      if (value instanceof Component) return (Component) value;
      return defaultRenderer.getTableCellRendererComponent(
        table,
        value,
        isSelected,
        hasFocus,
        row,
        column
      );
    }
  }

  class JTableButtonModel extends AbstractTableModel {

    private Object[][] rows = {
      { "Button1", new JButton("Button1") },
      { "Button2", new JButton("Button2") },
      { "Button3", new JButton("Button3") },
      { "Button4", new JButton("Button4") },
    };
    private String[] columns = { "Count", "Buttons" };

    public String getColumnName(int column) {
      return columns[column];
    }

    public int getRowCount() {
      return rows.length;
    }

    public int getColumnCount() {
      return columns.length;
    }

    public Object getValueAt(int row, int column) {
      return rows[row][column];
    }

    public boolean isCellEditable(int row, int column) {
      return false;
    }

    public Class getColumnClass(int column) {
      return getValueAt(0, column).getClass();
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
