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

       sortHelperMethods SHM = new sortHelperMethods(showsList, true);


       JPanel mainPanel = new JPanel(new BorderLayout(10, 10));


       JLabel titleLabel = new JLabel("TVShow Log", SwingConstants.CENTER);
       titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
       mainPanel.add(titleLabel, BorderLayout.NORTH);


       JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
       sortPanel.add(new JLabel("Sort by: "));
       JButton dateButton = new JButton("Date");
       JButton platformButton = new JButton("Platform");
       JButton showNameButton = new JButton("Show Name");
       JButton defaultButton = new JButton("Default");


       dateButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               //sortShowsByDate();
               SHM.retrieveDataCategorize("date_created");
           }
       });
       defaultButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               SHM.searchDataBase("");
           }
       });
       platformButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               //sortShowsByPlatform();
               SHM.retrieveDataCategorize("platform");
           }
       });
       showNameButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
            SHM.retrieveDataCategorize("name");
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
       JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
       contentPanel.add(topPanel, BorderLayout.NORTH);
       contentPanel.add(new JScrollPane(showsList), BorderLayout.CENTER); 


       mainPanel.add(contentPanel, BorderLayout.CENTER);


       allShowsFrame.add(mainPanel, BorderLayout.CENTER);


       SHM.searchDataBase("");


       allShowsFrame.setVisible(true);
   }


   public void defaultShowLog(){
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
            ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform, date_created FROM shows WHERE name LIKE '%"+ showName + "%'" + "or platform like '%" + showName + "%'" )) {
              


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

//    public String formatReturnMethod(String sortByName , String name, int season, int episode, String platform, String date){
//     if (sortByName.equals("date_created")){
//         // choose sort by date
//         return String.format("\n- %s - Season %d, Episode %d on %s\n", name, season, episode, platform);
//     } if (sortByName.equals("platform")){
//         //choose sort by platformj
//         return String.format("\n- %s - Season %d, Episode %d on %s\n",name , season, episode, date);
//     }
//     if (sortByName.equals("name")){
//         return String.format("\n- Season %d, Episode %d on %s on %s\n", season, episode, platform, date);
//     } else {
//         return "";
//     }
//    }
//    public void testretrieveDataCategorize( String sortByName){
//     try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform, date_created FROM shows order by " + sortByName)) {


//            StringBuilder showsText = new StringBuilder();
//            String lastDate = "";


//            while (rs.next()) {
//                //System.out.println(showsText.toString() + "ghgjgjg");
//                String name = rs.getString("name");
//                String date = rs.getString("date_created");
              
//                int season = rs.getInt("season");
//                int episode = rs.getInt("episode");
//                String platform = rs.getString("platform");
              


//                if (!rs.getString(sortByName).equals(lastDate)) {
//                    showsText.append("\n").append(rs.getString(sortByName).toUpperCase()).append("\n");
//                    lastDate = rs.getString(sortByName);
//                }


//                String result = formatReturnMethod(sortByName, name, season, episode, platform, date);
//                showsText.append(result);

                      
//            }


//            if (showsText.length() == 0) {
//                showsText.append("No TV shows found in the database.");
//            }
          
//            showsList.setText(showsText.toString());
          


//        } catch (SQLException ex) {
//            showsList.setText("Error fetching data: " + ex.getMessage());
//        }

//    }

//    public void sortShowsByDate(){
//        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform, date_created FROM shows order by date_created")) {


//            StringBuilder showsText = new StringBuilder();
//            String lastDate = "";


//            while (rs.next()) {
//                System.out.println(showsText.toString() + "ghgjgjg");
//                String name = rs.getString("name");
//                String date = rs.getString("date_created");
              
//                int season = rs.getInt("season");
//                int episode = rs.getInt("episode");
//                String platform = rs.getString("platform");
              


//                if (!date.equals(lastDate)) {
//                    showsText.append("\n").append(date.toUpperCase()).append("\n");
//                    lastDate = date;
//                }


//                showsText.append("\n").append("- ")
//                        .append(name).append(" - Season ").append(season)
//                        .append(", Episode ").append(episode)
//                        .append(" on ").append(platform).append("\n");
                      
//            }


//            if (showsText.length() == 0) {
//                showsText.append("No TV shows found in the database.");
//            }
          
//            showsList.setText(showsText.toString());
          


//        } catch (SQLException ex) {
//            showsList.setText("Error fetching data: " + ex.getMessage());
//        }


//    }
//    public void sortShowsByPlatform(){
//        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform, date_created FROM shows order by platform")) {


//            StringBuilder showsText = new StringBuilder();
//            String lastPlatform = "";


//            while (rs.next()) {
//                System.out.println(showsText.toString() + "ghgjgjg");
//                String name = rs.getString("name");
//                String date = rs.getString("date_created");
              
//                int season = rs.getInt("season");
//                int episode = rs.getInt("episode");
//                String platform = rs.getString("platform");
              


//                if (!platform.equals(lastPlatform)) {
//                    showsText.append("\n").append(platform.toUpperCase()).append("\n");
//                    lastPlatform = platform;
//                }


//                showsText.append("\n").append("- ")
//                        .append(name).append(" - Season ").append(season)
//                        .append(", Episode ").append(episode)
//                        .append(" on ").append(platform + " on ").append(date).append("\n");


                      
//            }


//            if (showsText.length() == 0) {
//                showsText.append("No TV shows found in the database.");
//            }
          
//            showsList.setText(showsText.toString());
          


//        } catch (SQLException ex) {
//            showsList.setText("Error fetching data: " + ex.getMessage());
//        }


//    }
//    public void sortShowsByName(){
//        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery("SELECT name, season, episode, platform, date_created FROM shows order by name")) {


//            StringBuilder showsText = new StringBuilder();
//            String lastName = "";


//            while (rs.next()) {
//                System.out.println(showsText.toString() + "ghgjgjg");
//                String name = rs.getString("name");
//                String date = rs.getString("date_created");
              
//                int season = rs.getInt("season");
//                int episode = rs.getInt("episode");
//                String platform = rs.getString("platform");
              


//                if (!name.equals(lastName)) {
//                    showsText.append("\n").append(name.toUpperCase()).append("\n");
//                    lastName =  name;
//                }


//                showsText.append("\n")
//                        .append("- Season ").append(season)
//                        .append(", Episode ").append(episode)
//                        .append(" on ").append(platform + " on ").append(date).append("\n");


                      
//            }


//            if (showsText.length() == 0) {
//                showsText.append("No TV shows found in the database.");
//            }
          
//            showsList.setText(showsText.toString());
          


//        } catch (SQLException ex) {
//            showsList.setText("Error fetching data: " + ex.getMessage());
//        }


//    }


}

