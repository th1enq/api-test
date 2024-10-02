import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private String name;
    private String studentId;
    private boolean hasBorrowedBooks;

    public User(String username, String password, String name, String studentId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.studentId = studentId;
        this.hasBorrowedBooks = false; // Default to false
    }

    // Getters and setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getStudentId() { return studentId; }
    public boolean hasBorrowedBooks() { return hasBorrowedBooks; }
    public void setHasBorrowedBooks(boolean hasBorrowedBooks) { this.hasBorrowedBooks = hasBorrowedBooks; }
}
