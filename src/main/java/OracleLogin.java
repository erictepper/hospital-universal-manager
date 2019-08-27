// We need to import the java.sql package to use JDBC
import java.sql.*;

// For reading from login info file.
import java.io.*;

// To create the interface.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class OracleLogin implements ActionListener {
    // Connection to Heroku PostgreSQL
    private Connection con;

    // All other class-wide objects
    private JFrame mainFrame;
    private JTextField username;
    private JPasswordField password;

    private OracleLogin() {
        // Creates all of the interface panels
        mainFrame = new JFrame("Heroku PostgreSQL Login");
        JPanel loginPanel = new JPanel();
        JLabel oracleLabel = new JLabel("Heroku PostgreSQL Login");
        JLabel usernameLabel = new JLabel("UserID");
        JLabel passwordLabel = new JLabel("Password");
        username = new JTextField(15);
        password = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        // Sets the size of the window
        mainFrame.setSize(800, 600);
        mainFrame.setLocation(320, 150);
        mainFrame.setContentPane(loginPanel);
        loginPanel.setLayout(null);

        // Determines the layout for the interface
        oracleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 34));
        oracleLabel.setBounds(305, 150, 250, 40);
        usernameLabel.setBounds(295, 250, 80, 20);
        passwordLabel.setBounds(280, 300, 80, 20);
        username.setBounds(350, 250, 150, 20);
        password.setBounds(350, 300, 150, 20);
        password.setEchoChar('*');
        password.addActionListener(this);
        loginButton.addActionListener(this);
        loginButton.setBounds(380, 350, 80, 20);

        // Adds the different objects to the interface
        loginPanel.add(oracleLabel);
        loginPanel.add(usernameLabel);
        loginPanel.add(passwordLabel);
        loginPanel.add(username);
        loginPanel.add(password);
        loginPanel.add(loginButton);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        // anonymous inner class for closing the window
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new OracleLogin();
    }

    private boolean connect(String username, String password) {
        Properties loginInfo = new Properties();

        // Loads the confidential db-login.ini file.
        try {
            InputStream loginFilePath = getClass().getResourceAsStream("db-login.ini");
            loginInfo.load(loginFilePath);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }

        // Reads the login info from the db-login.ini file.
        String connectURL = loginInfo.getProperty("connectURL");
        username = loginInfo.getProperty("username");
        password = loginInfo.getProperty("password");

        // Attempts to connect to the Heroku PostgreSQL database.
        try {
            con = DriverManager.getConnection(connectURL, username, password);

            JOptionPane.showMessageDialog(null, "Connected to Heroku PostgreSQL!");
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Message: " + ex.getMessage());
            return false;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if ( connect(username.getText(), String.valueOf(password.getPassword())) ) {
            // if the username and password are valid,
            // remove the login window and display the Hospital Login
            mainFrame.dispose();
            new Login(con);
        }
        else {
            JOptionPane.showMessageDialog(null, "Invalid login!");
        }
    }
}
