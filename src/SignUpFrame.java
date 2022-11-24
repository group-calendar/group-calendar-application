import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import jdbc.DBConnection;

public class SignUpFrame extends JFrame {

  private JPanel main_panel;
  private JTextField tf_email, tf_username;
  private JTextField pf_pw, pf_pwConfirm;
  private JLabel lb_email, lb_pw, lb_pwConfirm, lb_username;
  private JButton bt_signUp, bt_cancel, bt_idConfirm;

  private String query;

  private ResultSet selectResult;

  private int insertUpdateDeleteDataResult, peopleCnt;

  private boolean emailDuplicationCheckFlag;

  DBConnection dbc = new DBConnection();

  public SignUpFrame() {
    setTitle("회원가입");
    setSize(420, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    main_panel = new JPanel(null);

    lb_email = new JLabel("이메일");
    lb_pw = new JLabel("비밀번호");
    lb_pwConfirm = new JLabel("비밀번호 확인");
    lb_username = new JLabel("이름");

    tf_email = new JTextField(10);
    pf_pw = new JPasswordField(10);
    pf_pwConfirm = new JPasswordField(10);
    tf_username = new JTextField(10);
    bt_signUp = new JButton("회원가입");
    bt_cancel = new JButton("취소");
    bt_idConfirm = new JButton("중복확인");

    lb_email.setBounds(20, 25, 80, 30);
    lb_pw.setBounds(20, 75, 80, 30);
    lb_pwConfirm.setBounds(20, 125, 80, 30);
    lb_username.setBounds(20, 175, 80, 30);

    tf_email.setBounds(100, 20, 235, 40);
    pf_pw.setBounds(100, 70, 235, 40);
    pf_pwConfirm.setBounds(100, 120, 235, 40);
    tf_username.setBounds(100, 170, 235, 40);

    bt_idConfirm.setBounds(337, 20, 70, 40);
    bt_cancel.setBounds(80, 220, 120, 40);
    bt_signUp.setBounds(225, 220, 120, 40);

    main_panel.add(lb_pw);
    main_panel.add(lb_pw);
    main_panel.add(pf_pw);
    main_panel.add(lb_pwConfirm);
    main_panel.add(pf_pwConfirm);
    main_panel.add(lb_username);
    main_panel.add(tf_username);
    main_panel.add(lb_email);
    main_panel.add(tf_email);
    main_panel.add(bt_idConfirm);
    main_panel.add(bt_signUp);
    main_panel.add(bt_cancel);

    add(main_panel);

    bt_idConfirm.addActionListener(new MyActionListener());
    bt_cancel.addActionListener(new MyActionListener());
    bt_signUp.addActionListener(new MyActionListener());

    setVisible(true);
  }

  // --------------------- ActionListener --------------------------
  class MyActionListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      query =
        "select count(*) from simple_calendar.user where email like '" +
        tf_email.getText() +
        "'";
      if (e.getSource() == bt_idConfirm) {
        if (tf_email.getText().equals("")) {
          JOptionPane.showMessageDialog(
            null,
            "이메일을 입력해 주세요.",
            "이메일 정보 입력",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }
        try {
          selectResult = dbc.selectData(query);
          selectResult.next();
          peopleCnt = Integer.parseInt(selectResult.getString(1));
          if (peopleCnt == 1) {
            JOptionPane.showMessageDialog(
              null,
              "이미 사용 중인 이메일입니다. 다시 입력해 주세요.",
              "이메일 정보 중복",
              JOptionPane.ERROR_MESSAGE
            );
            return;
          } else {
            JOptionPane.showMessageDialog(
              null,
              "사용 가능한 이메일입니다.",
              "이메일 중복 확인",
              JOptionPane.INFORMATION_MESSAGE
            );
            emailDuplicationCheckFlag = true;
            tf_email.setEnabled(false);
          }
        } catch (Exception error) {
          System.out.println("DB 쿼리 실행 실패");
          System.out.print("사유 : " + error.getMessage());
        }
      } else if (e.getSource() == bt_cancel) {
        dispose();
        new LoginFrame();
      } else if (e.getSource() == bt_signUp) {
        if (!emailDuplicationCheckFlag) {
          JOptionPane.showMessageDialog(
            null,
            "이메일 중복 확인이 필요합니다.",
            "이메일 중복 확인 여부",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }
        if (pf_pw.getText().equals("") || tf_username.getText().equals("")) {
          JOptionPane.showMessageDialog(
            null,
            "모든 사용자 정보를 입력해주세요.",
            "사용자 정보 확인",
            JOptionPane.ERROR_MESSAGE
          );
        }

        if (!pf_pw.getText().equals(pf_pwConfirm.getText())) {
          JOptionPane.showMessageDialog(
            null,
            "비밀번호가 일치하지 않습니다.",
            "비밀번호 확인",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }

        try {
          query =
            "INSERT INTO simple_calendar.user (email, password, username) VALUES ('" +
            tf_email.getText() +
            "', '" +
            pf_pw.getText() +
            "', '" +
            tf_username.getText() +
            "')";
          System.out.println(query);
          insertUpdateDeleteDataResult = dbc.insertUpdateDeleteData(query);
          System.out.println(insertUpdateDeleteDataResult);

          JOptionPane.showMessageDialog(
            null,
            "회원가입을 완료하였습니다.",
            "회원가입 완료",
            JOptionPane.INFORMATION_MESSAGE
          );

          new LoginFrame();
          dispose();
        } catch (Exception error) {
          System.out.println("DB 쿼리 실행 실패");
          System.out.print("사유 : " + error.getMessage());
          JOptionPane.showMessageDialog(
            null,
            "회원가입에 실패하였습니다.\n입력q하신 내용을 다시 확인해주세요.",
            "회원가입 실패",
            JOptionPane.ERROR_MESSAGE
          );
        }
      }
    }
  }

  public static void main(String[] args) {
    new SignUpFrame();
  }
}
