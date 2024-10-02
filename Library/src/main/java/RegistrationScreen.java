import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class RegistrationScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField studentIdField;

    public RegistrationScreen(Map<String, User> users) {
        setTitle("Register");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel, users);
    }

    private void placeComponents(JPanel panel, Map<String, User> users) {
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

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(10, 80, 80, 25);
        panel.add(nameLabel);

        nameField = new JTextField(20);
        nameField.setBounds(100, 80, 165, 25);
        panel.add(nameField);

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(10, 110, 80, 25);
        panel.add(studentIdLabel);

        studentIdField = new JTextField(20);
        studentIdField.setBounds(100, 110, 165, 25);
        panel.add(studentIdField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(100, 140, 100, 25);
        panel.add(registerButton);

        // Registration button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String name = nameField.getText();
                String studentId = studentIdField.getText();

                if (users.containsKey(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists!", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Save user details in the map
                    users.put(username, new User(username, password, name, studentId));
                    JOptionPane.showMessageDialog(null, "Registration Successful! You can now log in.");
                    dispose(); // Close the registration window
                }
            }
        });
    }
}
