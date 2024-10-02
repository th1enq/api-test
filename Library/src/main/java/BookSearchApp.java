import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BookSearchApp extends JFrame {

    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private Map<String, Boolean> borrowedBooks = new HashMap<>(); // To track borrowed books
    private JTextArea userInfoArea; // To display user information
    private String userName; // To store the logged-in user's name
    private String studentId; // To store the logged-in user's student ID

    public BookSearchApp(String userName, String studentId) {
        this.userName = userName;
        this.studentId = studentId;

        setTitle("Book Search and Borrow");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UI Components
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton logoutButton = new JButton("Logout");

        // User Information Area
        userInfoArea = new JTextArea(3, 40);
        userInfoArea.setEditable(false);
        userInfoArea.setText("User: " + userName + "\nStudent ID: " + studentId + "\n\nBorrowed Books:");
        userInfoArea.setBorder(BorderFactory.createTitledBorder("User Information"));

        // Table to display books
        tableModel = new DefaultTableModel(new Object[]{"Title", "Author", "Status", "Action"}, 0);
        table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only the "Action" column is editable
            }
        };

        // Add button to the table to handle borrow/return
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter book title:"));
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(logoutButton); // Add the logout button

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(userInfoArea, BorderLayout.CENTER);
        getContentPane().add(scrollPane, BorderLayout.SOUTH);

        // Search button action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText().trim();
                if (!query.isEmpty()) {
                    searchBooks(query);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a search term.");
                }
            }
        });

        // Logout button action
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window and return to login
                dispose(); // Close the BookSearchApp window
                LoginScreen login = new LoginScreen(); // Create a new LoginScreen
                login.setVisible(true); // Show the login window
            }
        });
    }

    private void searchBooks(String query) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query.replace(" ", "%20");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                parseAndDisplayResults(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching data from Google Books API.");
        }
    }

    private void parseAndDisplayResults(String jsonResponse) {
        tableModel.setRowCount(0); // Clear previous results
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonArray items = jsonObject.getAsJsonArray("items");
        if (items != null) {
            for (JsonElement item : items) {
                JsonObject volumeInfo = item.getAsJsonObject().getAsJsonObject("volumeInfo");
                String title = volumeInfo.get("title").getAsString();
                String authors = volumeInfo.has("authors") ? volumeInfo.get("authors").toString() : "Unknown Author";

                boolean isBorrowed = borrowedBooks.getOrDefault(title, false);
                String status = isBorrowed ? "Borrowed" : "Available";
                String action = isBorrowed ? "Return" : "Borrow";

                tableModel.addRow(new Object[]{title, authors, status, action});
            }
        } else {
            JOptionPane.showMessageDialog(null, "No results found.");
        }
    }

    // ButtonRenderer Class to render buttons in the JTable
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // ButtonEditor Class to handle the click action in the JTable
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                String title = tableModel.getValueAt(row, 0).toString();
                String currentStatus = tableModel.getValueAt(row, 2).toString();

                if (currentStatus.equals("Available")) {
                    borrowedBooks.put(title, true);
                    tableModel.setValueAt("Borrowed", row, 2);
                    tableModel.setValueAt("Return", row, 3);
                    updateUserInfo(); // Update user info display
                } else {
                    borrowedBooks.put(title, false);
                    tableModel.setValueAt("Available", row, 2);
                    tableModel.setValueAt("Borrow", row, 3);
                    updateUserInfo(); // Update user info display
                }
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    // Update the user info area with borrowed books
    private void updateUserInfo() {
        StringBuilder borrowedBooksList = new StringBuilder();
        borrowedBooks.forEach((title, isBorrowed) -> {
            if (isBorrowed) {
                borrowedBooksList.append(title).append("\n");
            }
        });
        userInfoArea.setText("User: " + userName + "\nStudent ID: " + studentId + "\n\nBorrowed Books:\n" + borrowedBooksList);
    }

    // Main function to launch the Book Search App after login
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            LoginScreen login = new LoginScreen();
            login.setVisible(true);
        });
    }
}
