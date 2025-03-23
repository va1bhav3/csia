import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShowLogAction implements ActionListener {
    private static final String URL = "jdbc:mysql://localhost:3306/tv_show_tracker";
    private static final String USER = "root";
    JTextArea showsList = new JTextArea();
    private static final String PASSWORD = "Va1bhav@2008";

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame allShowsFrame = new JFrame("TVShow Log");
        allShowsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        allShowsFrame.setLayout(new BorderLayout(10, 10));
        allShowsFrame.setSize(800, 400);
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
        JButton defaultButton = new JButton("Default");

        dateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchShowsByDate();
            }
        });
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                defaultShowLog();
            }
        });
        platformButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchShowsByPlatform();
            }
        });
        showNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchShowsByName();
            }
        });
        sortPanel.add(dateButton);
        sortPanel.add(platformButton);
        sortPanel.add(showNameButton);
        sortPanel.add(defaultButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        JTextField searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(searchField.getText());
                searchShowsByName(searchField.getText());
            }
        });

        showsList.setEditable(false);
        showsList.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(sortPanel);
        topPanel.add(searchPanel);
        // Create a sub-panel to hold sort controls and show list vertically
        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(showsList), BorderLayout.CENTER);  

        // Add the content panel to the main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add the main panel to the frame
        allShowsFrame.add(mainPanel, BorderLayout.CENTER);

        defaultShowLog();

        allShowsFrame.setVisible(true);
    }

    public void defaultShowLog(){
        // Fetch data from the database
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform, date_created FROM shows")) {

            StringBuilder showsText = new StringBuilder();
            int index = 1;
            while (rs.next()) {
                String name = rs.getString("name");
                int season = rs.getInt("season");
                int episode = rs.getInt("episode");
                String platform = rs.getString("platform");
                String date_created = rs.getString("date_created");
                showsText.append("\n").append(index).append(". ")
                        .append(name).append(" - Season ").append(season)
                        .append(", Episode ").append(episode)
                        .append(" on ").append(platform).append(" on " + date_created).append("\n");
                index++;
            }

            if (showsText.length() == 0) {
                showsText.append("No TV shows found in the database.");
            }
            showsList.setText(showsText.toString());

        } catch (SQLException ex) {
            showsList.setText("Error connecting to database: " + ex.getMessage());
        }
    }

    public void searchShowsByName(String showName){
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform, date_created FROM shows WHERE name LIKE '%"+ showName + "%'")) {
                

            StringBuilder showsText = new StringBuilder();
            int index = 1;
            while (rs.next()) {
                String name = rs.getString("name");
                int season = rs.getInt("season");
                int episode = rs.getInt("episode");
                String platform = rs.getString("platform");
                String date_created = rs.getString("date_created");
                showsText.append("\n").append(index).append(". ")
                        .append(name).append(" - Season ").append(season)
                        .append(", Episode ").append(episode)
                        .append(" on ").append(platform).append(" on " + date_created).append("\n");
                index++;
            }

            if (showsText.length() == 0) {
                showsText.append("No TV shows found in the database.");
            }
            showsList.setText(showsText.toString());

        } catch (SQLException ex) {
            showsList.setText("Error connecting to database: " + ex.getMessage());
        }
    }

    public void fetchShowsByDate(){
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform, date_created FROM shows order by date_created")) {

            StringBuilder showsText = new StringBuilder();
            String lastDate = "";
            //System.out.println(11111);

            while (rs.next()) {
                System.out.println(showsText.toString() + "ghgjgjg");
                String name = rs.getString("name");
                String date = rs.getString("date_created");
                
                int season = rs.getInt("season");
                int episode = rs.getInt("episode");
                String platform = rs.getString("platform");
                

                // Print platform name only when it changes
                if (!date.equals(lastDate)) {
                    showsText.append("\n").append(date.toUpperCase()).append("\n");
                    lastDate = date;
                }

                showsText.append("\n").append("- ")
                        .append(name).append(" - Season ").append(season)
                        .append(", Episode ").append(episode)
                        .append(" on ").append(platform).append("\n");

                //showsText.append("tgjghjgjgjgj\n");
                        
            }

            if (showsText.length() == 0) {
                showsText.append("No TV shows found in the database.");
            }
            
            showsList.setText(showsText.toString());
            //System.out.println(showsList.getText());
            

        } catch (SQLException ex) {
            showsList.setText("Error fetching data: " + ex.getMessage());
        }

    }
    public void fetchShowsByPlatform(){
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform, date_created FROM shows order by platform")) {

            StringBuilder showsText = new StringBuilder();
            String lastPlatform = "";
            //System.out.println(11111);

            while (rs.next()) {
                System.out.println(showsText.toString() + "ghgjgjg");
                String name = rs.getString("name");
                String date = rs.getString("date_created");
                
                int season = rs.getInt("season");
                int episode = rs.getInt("episode");
                String platform = rs.getString("platform");
                

                // Print platform name only when it changes
                if (!platform.equals(lastPlatform)) {
                    showsText.append("\n").append(platform.toUpperCase()).append("\n");
                    lastPlatform = platform;
                }

                showsText.append("\n").append("- ")
                        .append(name).append(" - Season ").append(season)
                        .append(", Episode ").append(episode)
                        .append(" on ").append(platform + " on ").append(date).append("\n");

                //showsText.append("tgjghjgjgjgj\n");
                        
            }

            if (showsText.length() == 0) {
                showsText.append("No TV shows found in the database.");
            }
            
            showsList.setText(showsText.toString());
            //System.out.println(showsList.getText());
            

        } catch (SQLException ex) {
            showsList.setText("Error fetching data: " + ex.getMessage());
        }

    }
    public void fetchShowsByName(){
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform, date_created FROM shows order by name")) {

            StringBuilder showsText = new StringBuilder();
            String lastName = "";
            //System.out.println(11111);

            while (rs.next()) {
                System.out.println(showsText.toString() + "ghgjgjg");
                String name = rs.getString("name");
                String date = rs.getString("date_created");
                
                int season = rs.getInt("season");
                int episode = rs.getInt("episode");
                String platform = rs.getString("platform");
                

                // Print platform name only when it changes
                if (!name.equals(lastName)) {
                    showsText.append("\n").append(name.toUpperCase()).append("\n");
                    lastName =  name;
                }

                showsText.append("\n")
                        .append("- Season ").append(season)
                        .append(", Episode ").append(episode)
                        .append(" on ").append(platform + " on ").append(date).append("\n");

                //showsText.append("tgjghjgjgjgj\n");
                        
            }

            if (showsText.length() == 0) {
                showsText.append("No TV shows found in the database.");
            }
            
            showsList.setText(showsText.toString());
            //System.out.println(showsList.getText());
            

        } catch (SQLException ex) {
            showsList.setText("Error fetching data: " + ex.getMessage());
        }

    }

}