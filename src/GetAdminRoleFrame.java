import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.*;
import jdbc.DBConnection;

class GetAdminRoleFrame extends JFrame {

  private JPanel main_panel;

  private JLabel lb_adminPassword;

  private JTextField tf_adminPassword;

  private JButton bt_getAdminRole, bt_cancel;

  private String query;

  private static int group_id, fetchedGroup_id;

  private DBConnection dbc = new DBConnection();

  SimpleCalendar sc = new SimpleCalendar(1);

  public GetAdminRoleFrame(int group_id) {
    this.group_id = group_id;
  }

  public GetAdminRoleFrame() {
    setTitle("관리자 로그인");
    setSize(300, 120);
    setLocationRelativeTo(null);

    main_panel = new JPanel();

    main_panel.setLayout(null);
    main_panel.setBackground(Color.WHITE);

    lb_adminPassword = new JLabel("관리자 암호");

    tf_adminPassword = new JPasswordField();

    bt_getAdminRole = new JButton("변경");

    bt_cancel = new JButton("취소");
    bt_cancel.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          dispose();
        }
      }
    );

    lb_adminPassword.setBounds(10, 5, 100, 50);
    tf_adminPassword.setBounds(75, 16, 215, 30);
    bt_cancel.setBounds(9, 50, 140, 33);
    bt_getAdminRole.setBounds(150, 50, 140, 33);

    bt_getAdminRole.addActionListener(new MyActionListener());

    main_panel.add(lb_adminPassword);
    main_panel.add(tf_adminPassword);
    main_panel.add(bt_cancel);
    main_panel.add(bt_getAdminRole);

    add(main_panel);

    setVisible(true);
  }

  class MyActionListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == bt_getAdminRole) {
        query =
          "select * from simple_calendar.user where group_id = " +
          group_id +
          " AND adminPassword LIKE '" +
          tf_adminPassword.getText() +
          "'";
        System.out.println(query);
        try {
          ResultSet result = dbc.selectData(query);
          result.next();
          fetchedGroup_id = Integer.parseInt(result.getString(1));
          if (group_id == fetchedGroup_id) {
            sc.isAdmin = true;
            sc.mn_changeRole.setText("일반유저로 전환");
            sc.mn_role.setText("관리자");
            JOptionPane.showMessageDialog(
              null,
              "관리자 모드로 변경되었습니다.",
              "변경 완료",
              JOptionPane.PLAIN_MESSAGE
            );
            dispose();
          }
        } catch (Exception error) {
          System.out.println("DB 쿼리 실행 실패");
          System.out.print("사유 : " + error.getMessage());
          JOptionPane.showMessageDialog(
            null,
            "관리자 암호를 잘못 입력하였습니다.\n입력하신 내용을 다시 확인해주세요.",
            "변경 실패",
            JOptionPane.ERROR_MESSAGE
          );
        }
      }
    }
  }

  public static void main(String args[]) {
    new GetAdminRoleFrame();
  }
}
