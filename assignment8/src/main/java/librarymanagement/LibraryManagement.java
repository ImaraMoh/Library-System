package librarymanagement;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class LibraryManagement {

    private static final String BOOKS_FILE = "books.json";
    private static final String MEMBERS_FILE = "members.json";
    private static final String LENDING_FILE = "lending.json";

    private JFrame frame;
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<LendingRecord> lendingRecords = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LibraryManagement() {
        // Load data
        loadData();

        // Show splash screen
        showSplashScreen();
    }

    private void showSplashScreen() {
        JFrame splashFrame = new JFrame();
        splashFrame.setSize(600, 400);
        splashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashFrame.setUndecorated(true);

        // Add image with title
        JLabel splashLabel = new JLabel();
        splashLabel.setLayout(new BorderLayout());

        ImageIcon splashImage = new ImageIcon("librarysystem.jpg"); // Ensure this image is in the project directory
        JLabel imageLabel = new JLabel(splashImage);
        splashLabel.add(imageLabel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Library Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE, 2, true), // Stroke with black border
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding inside the border
        ));
        splashLabel.add(titleLabel, BorderLayout.SOUTH);

        splashFrame.add(splashLabel);
        splashFrame.setVisible(true);

        // Transition to main window after 3 seconds
        Timer timer = new Timer(3000, e -> {
            splashFrame.dispose();
            showMainWindow();
        });
        timer.setRepeats(false);
        timer.start();
        splashFrame.setLocationRelativeTo(null);
    }

    private void showMainWindow() {
        frame = new JFrame("Library Management System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Librarian", createLibrarianPanel());
        tabs.addTab("Assistant", createAssistantPanel());

        frame.add(tabs, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void loadData() {
        loadBooks();
        loadMembers();
        loadLendingRecords();
    }

    private void loadBooks() {
        try {
            File file = new File(BOOKS_FILE);
            if (file.exists()) {
                books = objectMapper.readValue(file, new TypeReference<List<Book>>() {
                });
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    private void loadMembers() {
        try {
            File file = new File(MEMBERS_FILE);
            if (file.exists()) {
                members = objectMapper.readValue(file, new TypeReference<List<Member>>() {
                });
            }
        } catch (IOException e) {
            System.out.println("Error loading members: " + e.getMessage());
        }
    }

    private void loadLendingRecords() {
        try {
            File file = new File(LENDING_FILE);
            if (file.exists()) {
                lendingRecords = objectMapper.readValue(file, new TypeReference<List<LendingRecord>>() {
                });
            }
        } catch (IOException e) {
            System.out.println("Error loading lending records: " + e.getMessage());
        }
    }

    private void saveData() {
        saveBooks();
        saveMembers();
        saveLendingRecords();
    }

    private void saveBooks() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(BOOKS_FILE), books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMembers() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(MEMBERS_FILE), members);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLendingRecords() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(LENDING_FILE), lendingRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JPanel createLibrarianPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JButton addBookBtn = new JButton("Add Book");
        JButton addMemberBtn = new JButton("Add Member");
        JButton discardBookBtn = new JButton("Discard Book");
        JButton viewOverdueBtn = new JButton("View Overdue Books");

        addBookBtn.addActionListener((ActionEvent e) -> {
            // Show form to add a new book
            JTextField bookNumberField = new JTextField();
            JTextField titleField = new JTextField();
            JTextField author = new JTextField();
            JTextField editionField = new JTextField();

            Object[] message = {
                "Book Number:", bookNumberField,
                "Title:", titleField,
                "Author:", author,
                "Edition:", editionField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Add Book", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String bookNumber = bookNumberField.getText();
                    String title = titleField.getText();
                    String Author = author.getText();
                    String edition = editionField.getText();
                    books.add(new Book(bookNumber, title, Author, edition));
                    saveData();
                    JOptionPane.showMessageDialog(frame, "Book Added!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid book number. Please enter a valid number.");
                }
            }
        });

        addMemberBtn.addActionListener(e -> {
            // Show form to add a new member
            JTextField membershipNumberField = new JTextField();
            JTextField NameField = new JTextField();
            JTextField phoneNumberField = new JTextField();
            JTextField addressField = new JTextField();

            Object[] message = {
                "Membership Number:", membershipNumberField,
                "Name:", NameField,
                "Phone Number:", phoneNumberField,
                "Address:", addressField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Add Member", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String membershipNumber = membershipNumberField.getText();
                    String Name = NameField.getText();
                    String phoneNumber = phoneNumberField.getText();
                    String address = addressField.getText();

                    members.add(new Member(membershipNumber, Name, phoneNumber, address));
                    saveData();
                    JOptionPane.showMessageDialog(frame, "Member Added!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid membership number. Please enter a valid number.");
                }
            }
        });

        discardBookBtn.addActionListener(e -> {

            JDialog dialog = new JDialog(frame, "Discard Book", true);
            dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
            dialog.setSize(300, 200);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                }
            });

            int option = JOptionPane.showConfirmDialog(dialog, "Enter the book number to discard:", "Discard Book", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.CANCEL_OPTION) {
                dialog.dispose();
                return; // Exit the method if the user clicked Cancel
            }

            String bookNumber = JOptionPane.showInputDialog(dialog, "Enter Book Number to Discard:");
            books.removeIf(book -> book.getBookNumber().equals(bookNumber));
            saveData();
            JOptionPane.showMessageDialog(frame, "Book Discarded!");
        });

        viewOverdueBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(frame, "View Overdue Books", true);
            dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
            dialog.setSize(300, 200);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                }
            });

            int option = JOptionPane.showConfirmDialog(dialog, "View overdue books?", "View Overdue Books", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.CANCEL_OPTION) {
                dialog.dispose();
                return; // Exit the method if the user clicked Cancel
            }

            StringBuilder overdueBooks = new StringBuilder("Overdue Books:\n");
            lendingRecords.forEach(record -> {
                if (isOverdue(record.getReturnDate())) {
                    overdueBooks.append("Book: ").append(record.getBookNumber()).append("\n");
                }
            });
            JOptionPane.showMessageDialog(frame, overdueBooks.toString());
        });

        panel.add(addBookBtn);
        panel.add(addMemberBtn);
        panel.add(discardBookBtn);
        panel.add(viewOverdueBtn);

        return panel;
    }

    private JPanel createAssistantPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));

        JButton issueBookBtn = new JButton("Issue Book");
        JButton returnBookBtn = new JButton("Return Book");
        JButton viewMemberBooksBtn = new JButton("View Member's Books");

        issueBookBtn.addActionListener(e -> {
            JTextField bookNumberField = new JTextField();
            JTextField membershipNumberField = new JTextField();

            Object[] message = {
                "Book Number:", bookNumberField,
                "Membership Number:", membershipNumberField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Issue Book", JOptionPane.OK_CANCEL_OPTION);
            if (lendingRecords.stream().filter(record -> {
                return record.getMembershipNumber().equals(membershipNumberField);
            }).count() < 4) {
                lendingRecords.add(new LendingRecord(bookNumberField, membershipNumberField, generateReturnDate()));
                saveData();
                JOptionPane.showMessageDialog(frame, "Book Issued!");
            } else {
                JOptionPane.showMessageDialog(frame, "Limit Exceeded!");
            }

        });

        returnBookBtn.addActionListener(e -> {
            String bookNumber = JOptionPane.showInputDialog("Enter Book Number to Return:");
            lendingRecords.removeIf(record -> record.getBookNumber().equals(bookNumber));
            saveData();
            JOptionPane.showMessageDialog(frame, "Book Returned!");
        });

        viewMemberBooksBtn.addActionListener(e -> {
            String membershipNumber = JOptionPane.showInputDialog("Enter Membership Number:");
            StringBuilder booksIssued = new StringBuilder("Books Issued:\n");
            lendingRecords.forEach(record -> {
                if (record.getMembershipNumber().equals(membershipNumber)) {
                    booksIssued.append(record.getBookNumber()).append("\n");
                }
            });
            JOptionPane.showMessageDialog(frame, booksIssued.toString());
        });

        panel.add(issueBookBtn);
        panel.add(returnBookBtn);
        panel.add(viewMemberBooksBtn);

        return panel;
    }

    private boolean isOverdue(String returnDate) {
        return false; // Overdue logic can be implemented here
    }

    private String generateReturnDate() {
        return "2024-12-31"; // Temporary return date
    }

    public static void main(String[] args) {
        new LibraryManagement();
    }

    static class Book {

        private final String bookNumber;
        private final String title;
        private String author;
        private String edition;

        public Book(String bookNumber, String title, String author, String edition) {
            this.bookNumber = bookNumber;
            this.title = title;
            this.author = author;
            this.edition = edition;
        }

        public Book(String bookNumber, String title) {
            this.bookNumber = bookNumber;
            this.title = title;
        }

        public String getBookNumber() {
            return bookNumber;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getEdition() {
            return edition;
        }
    }

    static class Member {

        private final String membershipNumber;
        private String name;
        private final String phone;
        private String address;

        public Member(String membershipNumber, String name, String phone, String address) {
            this.membershipNumber = membershipNumber;
            this.name = name;
            this.phone = phone;
            this.address = address;
        }

        public Member(String membershipNumber, String phone) {
            this.membershipNumber = membershipNumber;
            this.phone = phone;
        }

        public String getMembershipNumber() {
            return membershipNumber;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }
    }

    static class LendingRecord {

        private final JTextField bookNumber;
        private final JTextField membershipNumber;
        private final String returnDate;

        public LendingRecord(JTextField bookNumberField, JTextField membershipNumberField, String returnDate) {
            this.bookNumber = bookNumberField;
            this.membershipNumber = membershipNumberField;
            this.returnDate = returnDate;
        }

        public JTextField getBookNumber() {
            return bookNumber;
        }

        public JTextField getMembershipNumber() {
            return membershipNumber;
        }

        public String getReturnDate() {
            return returnDate;
        }
    }
}
