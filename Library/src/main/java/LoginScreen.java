import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Map<String, User> users = new HashMap<>(); // Store registered users

    public LoginScreen() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("User:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 80, 80, 25);
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(190, 80, 100, 25);
        panel.add(registerButton);

        // Login button action
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("admin") && password.equals("1")) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    new BookSearchApp(username, password).setVisible(true); // Pass the username and password
                    dispose(); // Close the login window
                } else if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    new BookSearchApp(username, password).setVisible(true); // Pass the username and password
                    dispose(); // Close the login window
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationScreen(); // Open registration screen
            }
        });
    }

    private void openRegistrationScreen() {
        new RegistrationScreen(users).setVisible(true); // Pass users map to RegistrationScreen
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            LoginScreen login = new LoginScreen();
            login.setVisible(true);
        });
    }
}
