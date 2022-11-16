import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JPanel main_panel;
    private JLabel lb_id;
    private JLabel lb_pw;
    private JTextField tf_id;
    private JTextField tf_pw;
    private JButton bt_sign_in;
    private JButton bt_sign_up;

    public LoginFrame() {
        setTitle("로그인");
        setSize(410, 170);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        main_panel = new JPanel(null);

        lb_id = new JLabel("Username");
        lb_id.setBounds(5,10,100, 40);
        lb_id.setHorizontalAlignment(JLabel.CENTER);
        lb_pw = new JLabel("Password");
        lb_pw.setBounds(5,50,100, 40);
        lb_pw.setHorizontalAlignment(JLabel.CENTER);

        tf_id = new JTextField();
        tf_id.setBounds(105,10,200,40);
        tf_pw = new JPasswordField();
        tf_pw.setBounds(105,50,200,40);


        // 로그인 버튼
        bt_sign_in = new JButton("Sign In");
        bt_sign_in.setBounds(310, 10, 80, 80);
        bt_sign_in.setHorizontalAlignment(JButton.CENTER);

        // 회원가입 버튼
        bt_sign_up = new JButton("Sign Up");
        bt_sign_up.setBounds(310, 90, 80, 40);
        bt_sign_up.setHorizontalAlignment(JButton.CENTER);

        main_panel.add(lb_id);
        main_panel.add(tf_id);
        main_panel.add(lb_pw);
        main_panel.add(tf_pw);
        main_panel.add(bt_sign_in);
        main_panel.add(bt_sign_up);




        add(main_panel);

        setVisible(true);
    }

    public static void main(String args[]) {
        new LoginFrame();
    }
}
