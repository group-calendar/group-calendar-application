import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import jdbc.DBConnection;

class LoginFrame extends JFrame {

  private JPanel main_panel;

  private JLabel lb_groupId;
  private JLabel lb_pw;
  private JTextField tf_groupId;
  private JTextField pf_pw;
  private JButton bt_signIn;
  private JButton bt_signUp;

  private int group_id;

  private String query, groupName;

  DBConnection dbc = new DBConnection();

  public LoginFrame() {
    setTitle("로그인");
    setSize(410, 170);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    main_panel = new JPanel(null);

    lb_groupId = new JLabel("그룹 아이디");
    lb_groupId.setBounds(6, 10, 80, 40);
    lb_groupId.setHorizontalAlignment(JLabel.CENTER);
    lb_pw = new JLabel("비밀번호");
    lb_pw.setBounds(0, 50, 80, 40);
    lb_pw.setHorizontalAlignment(JLabel.CENTER);

    tf_groupId = new JTextField();
    tf_groupId.setBounds(85, 10, 235, 40);
    pf_pw = new JPasswordField();
    pf_pw.setBounds(85, 50, 235, 40);

    // 로그인 버튼
    bt_signIn = new JButton("접속");
    bt_signIn.setBounds(320, 10, 80, 80);
    bt_signIn.setHorizontalAlignment(JButton.CENTER);

    // 회원가입 버튼
    bt_signUp = new JButton("그룹 생성");
    bt_signUp.setBounds(320, 90, 80, 40);
    bt_signUp.setHorizontalAlignment(JButton.CENTER);

    bt_signIn.addActionListener(new MyActionListener());
    bt_signUp.addActionListener(new MyActionListener());

    main_panel.add(lb_groupId);
    main_panel.add(tf_groupId);
    main_panel.add(lb_pw);
    main_panel.add(pf_pw);
    main_panel.add(bt_signIn);
    main_panel.add(bt_signUp);

    add(main_panel);

    setVisible(true);
  }

  // --------------------- ActionListener --------------------------
  class MyActionListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == bt_signIn) {
        query =
          "SELECT * FROM simple_calendar.user where groupId LIKE '" +
          tf_groupId.getText() +
          "' AND password LIKE '" +
          pf_pw.getText() +
          "'";
        System.out.println(query);
        try {
          ResultSet result = dbc.selectData(query);
          result.next();
          group_id = Integer.parseInt(result.getString(1));
          groupName = result.getString(5);
          new SimpleCalendar(group_id, groupName);
          dispose();
        } catch (SQLException error) {
          System.out.println("DB 쿼리 실행 실패");
          System.out.print("사유 : " + error.getMessage());
          JOptionPane.showMessageDialog(
            null,
            "그룹 아이디 또는 비밀번호를 잘못 입력했습니다.\n입력하신 내용을 다시 확인해주세요.",
            "로그인 실패",
            JOptionPane.ERROR_MESSAGE
          );
        }
      } else if (e.getSource() == bt_signUp) {
        dispose();
        new SignUpFrame();
      }
    }
  }

  public static void main(String[] args) {
    new LoginFrame();
  }
}
