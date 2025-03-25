import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.sql.*;




public class ShowTVShowsAction implements ActionListener {
   private static final String URL = "jdbc:mysql://localhost:3306/tv_show_tracker";
   private static final String USER = "root";
   private static final String PASSWORD = "Va1bhav@2008";
   JTextArea showsList = new JTextArea();


   public ShowTVShowsAction(JFrame frame) {
   }


   @Override
   public void actionPerformed(ActionEvent e) {
               JFrame allShowsFrame = new JFrame("TVShow Names");
       allShowsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       allShowsFrame.setLayout(new BorderLayout(10, 10));
       allShowsFrame.setSize(800, 400);
       allShowsFrame.setLocationRelativeTo(null);


       JPanel mainPanel = new JPanel(new BorderLayout(10, 10));


       JLabel titleLabel = new JLabel("TVShow Names", SwingConstants.CENTER);
       titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
       mainPanel.add(titleLabel, BorderLayout.NORTH);


       JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
       sortPanel.add(new JLabel("Sort by: "));
      
       JButton platformButton = new JButton("Platform");
       JButton alphaButton = new JButton("Alphabetical");
       JButton defaultButton = new JButton("Default");
       JTextField searchText = new JTextField(10);
       searchText.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               System.out.println(searchText.getText());
               searchShowsByName(searchText.getText());
           }
       });


       platformButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               sortShowsByPlatform();
           }
       });


       alphaButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e){
               sortShowsAlphabetically();
           }
       });


       defaultButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e){
               sortShowsDefault();
           }
       });
      
      
       sortPanel.add(platformButton);
       sortPanel.add(alphaButton);
       sortPanel.add(defaultButton);
       sortPanel.add(new JLabel("Search: "));
       sortPanel.add(searchText);


      


      
       showsList.setEditable(false);
       showsList.setFont(new Font("Arial", Font.PLAIN, 14));


       JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
       contentPanel.add(sortPanel, BorderLayout.NORTH);
       contentPanel.add(new JScrollPane(showsList), BorderLayout.CENTER);


       mainPanel.add(contentPanel, BorderLayout.CENTER);


       allShowsFrame.add(mainPanel, BorderLayout.CENTER);


       sortShowsDefault();


       allShowsFrame.setVisible(true);
   }


   private void searchShowsByName(String showName){
       try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT name, platform FROM shows WHERE name LIKE '%" + showName + "%'")) {


           StringBuilder showsText = new StringBuilder();
           int index = 1;
           while (rs.next()) {
               String name = rs.getString("name");
               String platform = rs.getString("platform");
               showsText.append("\n").append(index).append(". ")
                       .append(name)
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


   }


   private void sortShowsDefault(){
       try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT distinct name, platform FROM shows")) {


           StringBuilder showsText = new StringBuilder();
           int index = 1;
           while (rs.next()) {
               String name = rs.getString("name");
               String platform = rs.getString("platform");
               showsText.append("\n").append(index).append(". ")
                       .append(name)
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
   }


   private void sortShowsByPlatform() {
       try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT distinct name, platform FROM shows ORDER BY platform")) {


           StringBuilder showsText = new StringBuilder();
           String lastPlatform = "";


           while (rs.next()) {
               String name = rs.getString("name");
               String platform = rs.getString("platform");


               if (!platform.equals(lastPlatform)) {
                   showsText.append("\n").append(platform.toUpperCase()).append("\n");
                   lastPlatform = platform;
               }


               showsText.append(" - ").append(name).append("\n");
           }


           if (showsText.length() == 0) {
               showsText.append("No TV shows found in the database.");
           }
           showsList.setText(showsText.toString());


       } catch (SQLException ex) {
           showsList.setText("Error fetching data: " + ex.getMessage());
       }
   }


   private void sortShowsAlphabetically() {
       try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT distinct name, platform FROM shows ORDER BY name")) {


           StringBuilder showsText = new StringBuilder();
           String lastName = "";
          


           while (rs.next()) {
               String name = rs.getString("name");
               String platform = rs.getString("platform");


               if (!name.substring(0,1).equals(lastName)) {
                   showsText.append("\n").append(name.substring(0,1).toUpperCase()).append("\n");
                   lastName = name.substring(0,1);
               }


               showsText.append("\n").append("- ")
                       .append(name)
                       .append(" on ").append(platform).append("\n");


           }


           if (showsText.length() == 0) {
               showsText.append("No TV shows found in the database.");
           }
           showsList.setText(showsText.toString());


       } catch (SQLException ex) {
           showsList.setText("Error fetching data: " + ex.getMessage());
       }
   }
}
