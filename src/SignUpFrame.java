import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import jdbc.DBConnection;

public class SignUpFrame extends JFrame {

  private JPanel main_panel;
  private JTextField tf_groupId, tf_groupName;
  private JTextField pf_pw, pf_pwConfirm, pf_adminPw, pf_adminPwConfirm;
  private JLabel lb_groupId, lb_pw, lb_pwConfirm, lb_groupName, lb_adminPw, lb_adminPwConirm;
  private JButton bt_signUp, bt_cancel, bt_idConfirm;

  private String query;

  private ResultSet selectResult;

  private int insertUpdateDeleteDataResult, peopleCnt;

  private boolean emailDuplicationCheckFlag;

  DBConnection dbc = new DBConnection();

  public SignUpFrame() {
    setTitle("그룹 생성");
    setSize(420, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    main_panel = new JPanel(null);

    lb_groupId = new JLabel("그룹 아이디");
    lb_pw = new JLabel("비밀번호");
    lb_pwConfirm = new JLabel("비밀번호 확인");
    lb_adminPw = new JLabel("관리자 암호");
    lb_adminPwConirm = new JLabel("암호 확인");
    lb_groupName = new JLabel("그룹명");

    tf_groupId = new JTextField();
    pf_pw = new JPasswordField();
    pf_pwConfirm = new JPasswordField();
    pf_adminPw = new JPasswordField();
    pf_adminPwConfirm = new JPasswordField();
    tf_groupName = new JTextField();
    bt_signUp = new JButton("그룹 생성");
    bt_cancel = new JButton("취소");
    bt_idConfirm = new JButton("중복확인");

    lb_groupId.setBounds(20, 25, 80, 30);
    lb_pw.setBounds(20, 75, 80, 30);
    lb_pwConfirm.setBounds(20, 125, 80, 30);
    lb_adminPw.setBounds(20, 175, 80, 30);
    lb_adminPwConirm.setBounds(20, 225, 80, 30);
    lb_groupName.setBounds(20, 275, 80, 30);

    tf_groupId.setBounds(100, 20, 235, 40);
    pf_pw.setBounds(100, 70, 235, 40);
    pf_pwConfirm.setBounds(100, 120, 235, 40);
    pf_adminPw.setBounds(100, 170, 235, 40);
    pf_adminPwConfirm.setBounds(100, 220, 235, 40);
    tf_groupName.setBounds(100, 270, 235, 40);

    bt_idConfirm.setBounds(337, 20, 70, 40);
    bt_cancel.setBounds(80, 320, 120, 40);
    bt_signUp.setBounds(225, 320, 120, 40);

    main_panel.add(lb_pw);
    main_panel.add(lb_pw);
    main_panel.add(pf_pw);
    main_panel.add(lb_pwConfirm);
    main_panel.add(pf_pwConfirm);
    main_panel.add(lb_groupName);
    main_panel.add(lb_adminPw);
    main_panel.add(lb_adminPwConirm);
    main_panel.add(pf_adminPw);
    main_panel.add(pf_adminPwConfirm);
    main_panel.add(lb_adminPwConirm);
    main_panel.add(tf_groupName);
    main_panel.add(lb_groupId);
    main_panel.add(tf_groupId);
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
        "select count(*) from simple_calendar.user where groupId like '" +
        tf_groupId.getText() +
        "'";
      if (e.getSource() == bt_idConfirm) {
        if (tf_groupId.getText().equals("")) {
          JOptionPane.showMessageDialog(
            null,
            "그룹 아이디를 입력해 주세요.",
            "그룹 아이디 정보 입력",
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
              "이미 사용 중인 그룹 아이디 입니다. 다시 입력해 주세요.",
              "그룹 아이디 중복",
              JOptionPane.ERROR_MESSAGE
            );
            return;
          } else {
            JOptionPane.showMessageDialog(
              null,
              "사용 가능한 그룹 아이디입니다.",
              "그룹 아이디 중복 확인",
              JOptionPane.INFORMATION_MESSAGE
            );
            emailDuplicationCheckFlag = true;
            tf_groupId.setEnabled(false);
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
            "그룹 아이디 중복 확인이 필요합니다.",
            "그룹 아이디 중복 확인 여부",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }
        if (pf_pw.getText().equals("") || tf_groupName.getText().equals("")) {
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

        if (!pf_adminPw.getText().equals(pf_adminPwConfirm.getText())) {
          JOptionPane.showMessageDialog(
            null,
            "관리자 암호가 일치하지 않습니다.",
            "암호 확인",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }

        if (pf_pw.getText().equals(pf_adminPw.getText())) {
          JOptionPane.showMessageDialog(
            null,
            "비밀번호와 관리자 암호는 동일하게\n설정할 수 없습니다.",
            "암호 설정 실패",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }

        try {
          query =
            "INSERT INTO simple_calendar.user (groupId, password, adminPassword, groupName) VALUES ('" +
            tf_groupId.getText() +
            "', '" +
            pf_pw.getText() +
            "', '" +
            pf_adminPw.getText() +
            "', '" +
            tf_groupName.getText() +
            "')";
          System.out.println(query);
          insertUpdateDeleteDataResult = dbc.insertUpdateDeleteData(query);
          System.out.println(insertUpdateDeleteDataResult);

          JOptionPane.showMessageDialog(
            null,
            "그룹을 생성하였습니다.",
            "그룹 생성 완료",
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
