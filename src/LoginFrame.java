import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import jdbc.DBConnection;

class LoginFrame extends JFrame {

  private JPanel main_panel;

  private JLabel lb_id;
  private JLabel lb_pw;
  private JTextField tf_id;
  private JTextField tf_pw;
  private JButton bt_sign_in;
  private JButton bt_sign_up;

  DBConnection dbc = new DBConnection();

  public LoginFrame() {
    setTitle("로그인");
    setSize(410, 170);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    main_panel = new JPanel(null);

    lb_id = new JLabel("사용자명");
    lb_id.setBounds(0, 10, 80, 40);
    lb_id.setHorizontalAlignment(JLabel.CENTER);
    lb_pw = new JLabel("비밀번호");
    lb_pw.setBounds(0, 50, 80, 40);
    lb_pw.setHorizontalAlignment(JLabel.CENTER);

    tf_id = new JTextField();
    tf_id.setBounds(85, 10, 235, 40);
    tf_pw = new JPasswordField();
    tf_pw.setBounds(85, 50, 235, 40);

    // 로그인 버튼
    bt_sign_in = new JButton("로그인");
    bt_sign_in.setBounds(320, 10, 80, 80);
    bt_sign_in.setHorizontalAlignment(JButton.CENTER);

    // 회원가입 버튼
    bt_sign_up = new JButton("회원가입");
    bt_sign_up.setBounds(320, 90, 80, 40);
    bt_sign_up.setHorizontalAlignment(JButton.CENTER);

    bt_sign_in.addActionListener(new MyActionListener());
    bt_sign_up.addActionListener(new MyActionListener());

    main_panel.add(lb_id);
    main_panel.add(tf_id);
    main_panel.add(lb_pw);
    main_panel.add(tf_pw);
    main_panel.add(bt_sign_in);
    main_panel.add(bt_sign_up);

    add(main_panel);

    setVisible(true);
  }

  // --------------------- ActionListener --------------------------
  class MyActionListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == bt_sign_in) {
        String query =
          "SELECT * FROM simple_calendar.user where id LIKE '" +
          tf_id.getText() +
          "' AND password LIKE '" +
          tf_pw.getText() +
          "'";
        System.out.println(query);
        try {
          ResultSet result = dbc.selectData(query);
          do {
            result.next();
            System.out.println(
              result.getString(1) +
              "  " +
              result.getString(2) +
              "  " +
              result.getString(3) +
              "  " +
              result.getString(4) +
              "  " +
              result.getString(5)
            );
          } while (result.next());
          new SimpleCalendar();
          dispose();
        } catch (SQLException error) {
          System.out.println("DB 쿼리 실행 실패");
          System.out.print("사유 : " + error.getMessage());
          JOptionPane.showMessageDialog(
            null,
            "아이디 또는 비밀번호를 잘못 입력했습니다.\n입력하신 내용을 다시 확인해주세요.",
            "로그인 실패",
            JOptionPane.ERROR_MESSAGE
          );
        }
      } else if (e.getSource() == bt_sign_up) {
        dispose();
        new SignUpFrame();
      }
    }
  }

  public static void main(String[] args) {
    new LoginFrame();
  }
}
