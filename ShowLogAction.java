import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShowLogAction implements ActionListener {
    private static final String URL = "jdbc:mysql://localhost:3306/tv_show_tracker";
    private static final String USER = "root";
    private static final String PASSWORD = "Va1bhav@2008";

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame allShowsFrame = new JFrame("TVShow Log");
        allShowsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        allShowsFrame.setLayout(new BorderLayout(10, 10));
        allShowsFrame.setSize(500, 400);
        allShowsFrame.setLocationRelativeTo(null);

        // Create a single panel for both sort controls and show list
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Header (TVShow Log)
        JLabel titleLabel = new JLabel("TVShow Log", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Sort by panel (horizontal layout with buttons)
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        sortPanel.add(new JLabel("Sort by: "));
        JButton dateButton = new JButton("Date");
        JButton platformButton = new JButton("Platform");
        JButton showNameButton = new JButton("Show Name");
        sortPanel.add(dateButton);
        sortPanel.add(platformButton);
        sortPanel.add(showNameButton);

        // Text area for the show list
        JTextArea showsList = new JTextArea();
        showsList.setEditable(false);
        showsList.setFont(new Font("Arial", Font.PLAIN, 14));

        // Create a sub-panel to hold sort controls and show list vertically
        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.add(sortPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(showsList), BorderLayout.CENTER);

        // Add the content panel to the main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add the main panel to the frame
        allShowsFrame.add(mainPanel, BorderLayout.CENTER);

        // Fetch data from the database
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform FROM shows")) {

            StringBuilder showsText = new StringBuilder();
            int index = 1;
            while (rs.next()) {
                String name = rs.getString("name");
                int season = rs.getInt("season");
                int episode = rs.getInt("episode");
                String platform = rs.getString("platform");
                showsText.append("\n").append(index).append(". ")
                        .append(name).append(" - Season ").append(season)
                        .append(", Episode ").append(episode)
                        .append(" on ").append(platform).append("\n");
                index++;
            }

            if (showsText.length() == 0) {
                showsText.append("No TV shows found in the database.");
            }
            showsList.setText(showsText.toString());

        } catch (SQLException ex) {
            showsList.setText("Error connecting to database: " + ex.getMessage());
        }

        allShowsFrame.setVisible(true);
    }
}