import javax.swing.*;

public class SignUpFrame extends JFrame {
    private JPanel main_panel;
    private JTextField Id_Input, Email_Input, SId_Input;
    private JPasswordField Pw_Input, Pw_Confirm;
    private JLabel Id, Pw, Confirm, SId, Email;
    private JButton SignUp, Cancel;

    public SignUpFrame() {
        setTitle("SignUp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 400);

        main_panel = new JPanel(null);

        JLabel Id = new JLabel("아이디");
        JLabel Pw = new JLabel("비밀번호");
        JLabel Confirm = new JLabel("비밀번호 확인");
        JLabel SId = new JLabel("학번");
        JLabel Email = new JLabel("이메일");

        JTextField Id_Input = new JTextField(10);
        JPasswordField Pw_Input = new JPasswordField(10);
        JPasswordField Pw_Confirm = new JPasswordField(10);
        JTextField SId_Input = new JTextField(10);
        JTextField Email_Input = new JTextField(10);
        JButton SignUp = new JButton("회원가입");
        JButton Cancel = new JButton("취소");
        JButton Id_Check = new JButton("중복확인");

        Id.setBounds(20, 20, 80, 30);
        Pw.setBounds(20, 80, 80, 30);
        Confirm.setBounds(20, 140, 80, 30);
        SId.setBounds(20, 200, 80, 30);
        Email.setBounds(20, 260, 80, 30);

        Id_Input.setBounds(120, 20, 160, 30);
        Pw_Input.setBounds(120, 80, 160, 30);
        Pw_Confirm.setBounds(120, 140, 160, 30);
        SId_Input.setBounds(120, 200, 160 ,30);
        Email_Input.setBounds(120, 260, 160, 30);

        SignUp.setBounds(220, 310, 100, 30);
        Cancel.setBounds(100, 310, 100, 30);
        Id_Check.setBounds(300, 20, 100, 30);

        main_panel.add(Id);
        main_panel.add(Id_Input);
        main_panel.add(Pw);
        main_panel.add(Pw_Input);
        main_panel.add(Confirm);
        main_panel.add(Pw_Confirm);
        main_panel.add(SId);
        main_panel.add(SId_Input);
        main_panel.add(Email);
        main_panel.add(Email_Input);
        main_panel.add(SignUp);
        main_panel.add(Cancel);
        main_panel.add(Id_Check);

        add(main_panel);

        setVisible(true);
    }

    public static void main(String[] args){
        new SignUpFrame();
    }
}
