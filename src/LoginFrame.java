import javax.swing.*;

public class LoginFrame extends JFrame {

    private JPanel main_panel;

    private JButton bt_test;

    public LoginFrame() {
        setTitle("로그인");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        main_panel = new JPanel(null);

        bt_test = new JButton("버튼");
        bt_test.setBounds(20, 20, 100, 70);

        main_panel.add(bt_test);

        add(main_panel);

        setVisible(true);
    }

    public static void main(String args[]) {
        new LoginFrame();
    }
}