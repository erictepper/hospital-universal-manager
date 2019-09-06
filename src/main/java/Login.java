// We need to import the java.sql package to use JDBC
import java.sql.*;

// For reading from login info file.
import java.io.*;
import java.util.*;

// To create the interface.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login implements ActionListener {
    // Connection to Oracle
    private Connection con;

    // All other class-wide objects
    private JFrame mainFrame;
    private JComboBox<String> userType;
    private JTextField username;
    private JPasswordField password;


    Login(Connection inputConnection) {
        // Creates all of the interface panels
        mainFrame = new JFrame("User Login");
        JPanel loginPanel = new JPanel();
        JLabel loginLabel = new JLabel("User Login");
        JLabel userTypeLabel = new JLabel("Account Type");
        JLabel usernameLabel = new JLabel("UserID");
        JLabel passwordLabel = new JLabel("Password");
        String[] userCategories = {"Patient", "Receptionist", "Staff"};
        userType = new JComboBox<>(userCategories);
        username = new JTextField(15);
        password = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        // Sets the size of the window
        mainFrame.setSize(800, 600);
        mainFrame.setLocation(320, 150);
        mainFrame.setContentPane(loginPanel);
        loginPanel.setLayout(null);

        // Determines the layout for the interface
        loginLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 34));
        loginLabel.setBounds(305, 160, 200, 40);
        userTypeLabel.setBounds(250,230,160,20);
        usernameLabel.setBounds(295, 260, 80, 20);
        passwordLabel.setBounds(280, 290, 80, 20);
        userType.setBounds(350, 230, 150, 25);
        username.setBounds(350, 260, 150, 20);
        password.setBounds(350, 290, 150, 20);
        password.setEchoChar('*');
        password.addActionListener(this);
        loginButton.setBounds(380, 320, 80, 20);
        loginButton.addActionListener(this);

        // Adds the different objects to the interface
        loginPanel.add(loginLabel);
        loginPanel.add(userTypeLabel);
        loginPanel.add(usernameLabel);
        loginPanel.add(passwordLabel);
        loginPanel.add(userType);
        loginPanel.add(username);
        loginPanel.add(password);
        loginPanel.add(loginButton);

        // Makes the interface visible
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        // Use the input connection if it is valid, otherwise create a new connection.
        try {
            if (inputConnection != null && inputConnection.isValid(0)) {
                this.con = inputConnection;
            } else if (!connect()) {
                JOptionPane.showMessageDialog(null, "Connection to Heroku database failed!");
                System.out.println("Connection to Heroku database failed!");
                System.exit(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Message: " + e.getMessage());
            System.out.println("Message: " + e.getMessage());
            System.exit(1);
        }

        // anonymous inner class for closing the window
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    con.close();
                }
                catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Message: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        new Login(null);
    }

    private boolean connect() {
        Properties loginInfo = new Properties();

        // Loads the confidential db-login.ini file.
        try {
            InputStream loginFileStream = getClass().getResourceAsStream("db-login.ini");
            loginInfo.load(loginFileStream);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "The file containing " +
                "the Heroku database login info (db-login.ini) was not found in the resources folder.");
            return false;
        }

        // Reads the login info from the db-login.ini file.
        String connectURL = loginInfo.getProperty("connectURL");
        String username = loginInfo.getProperty("username");
        String password = loginInfo.getProperty("password");

        // Attempts to connect to the Heroku PostgreSQL database.
        try {
            con = DriverManager.getConnection(connectURL, username, password);
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Message: " + ex.toString());
            return false;
        }
    }

    private String login(String userType, String username, String password) {
        PreparedStatement getLoginInfoForUser;
        ResultSet resultLoginInfo;

        if (userType.equals("Patient")) {
            try {
                getLoginInfoForUser = con.prepareStatement("SELECT PatientPassword FROM Patient WHERE " +
                    "PatientIDNumber = ?");
                getLoginInfoForUser.setString(1, username);
                resultLoginInfo = getLoginInfoForUser.executeQuery();

                // Checks to see if the user does not exist
                if (!resultLoginInfo.next()) {
                    JOptionPane.showMessageDialog(null, "UserID does not exist!");
                    getLoginInfoForUser.close();
                    return "false";
                }
                // If the user exists, check the input password against the password in the database
                else {
                    String databasePassword = resultLoginInfo.getString("PatientPassword");
                    getLoginInfoForUser.close();
                    if (password.equals(databasePassword)) {
                        return "patient";
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Incorrect password!");
                        return "false";
                    }
                }
            }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Message: " + ex.getMessage());
                return "false";
            }
        }

        if (userType.equals("Receptionist")) {
            try {
                getLoginInfoForUser = con.prepareStatement("SELECT EmploymentPosition, StaffPassword " +
                    "FROM HospitalStaff WHERE StaffIDNumber = ?");
                getLoginInfoForUser.setString(1, username);
                resultLoginInfo = getLoginInfoForUser.executeQuery();

                if (!resultLoginInfo.next()) {
                    JOptionPane.showMessageDialog(null, "UserID does not exist!");
                    getLoginInfoForUser.close();
                    return "false";
                }
                else {
                    String employmentPosition = resultLoginInfo.getString("EmploymentPosition");

                    String databasePassword = resultLoginInfo.getString("StaffPassword");
                    getLoginInfoForUser.close();
                    if ((password.equals(databasePassword))&&(employmentPosition.equals("Receptionist"))) {
                        return "receptionist";
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Incorrect password!");
                        return "false";
                    }
                }
            }
            catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());
                return "false";
            }
        }

        else {
            try {

                getLoginInfoForUser = con.prepareStatement("SELECT EmploymentPosition, StaffPassword " +
                    "FROM HospitalStaff WHERE StaffIDNumber = ?");
                getLoginInfoForUser.setString(1, username);
                resultLoginInfo = getLoginInfoForUser.executeQuery();

                if (!resultLoginInfo.next()) {
                    JOptionPane.showMessageDialog(null, "UserID does not exist!");
                    getLoginInfoForUser.close();
                    return "false";
                }
                else {
                    String employmentPosition = resultLoginInfo.getString("EmploymentPosition");

                    String databasePassword = resultLoginInfo.getString("StaffPassword");
                    getLoginInfoForUser.close();
                    if ((password.equals(databasePassword))&&(!employmentPosition.equals("Receptionist"))) {
                        return "staff";
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Incorrect password!");
                        return "false";
                    }
                }
            }
            catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());
                return "false";
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        switch (login((String) userType.getSelectedItem(), username.getText(),
                String.valueOf(password.getPassword()))) {
            case "patient":
                mainFrame.dispose();
                new PatientView(con, username.getText());
                break;
            case "receptionist":
                mainFrame.dispose();
                new RecepView(con, username.getText());
                break;
            case "staff":
                mainFrame.dispose();
                new StaffView(con, username.getText());
                break;
        }
    }
}