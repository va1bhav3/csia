
import java.sql.*;

import javax.swing.JTextArea;

public class sortHelperMethods {
    private static final String URL = "jdbc:mysql://localhost:3306/tv_show_tracker";
   private static final String USER = "root";
   JTextArea showsList ;//= new JTextArea();
   private static final String PASSWORD = "Va1bhav@2008";
   boolean isTVLog;

   public sortHelperMethods(JTextArea showsList, boolean isTVLog){
    this.showsList = showsList;
    this.isTVLog = isTVLog;
   }

   public String formatReturnMethod(String sortByName, String name, String platform){
    if (sortByName.equals("platform")){
        return  String.format(" - %s\n", name);
    }
    else if (sortByName.equals("name")){
        return String.format("\n- %s on %s\n", name, platform);
    } else if (sortByName.equals("default")){
        return String.format(" %s on %s\n", name, platform);

    }
    return "";
   }

   public String formatReturnMethod(String sortByName , String name, int season, int episode, String platform, String date){

    if (sortByName.equals("date_created")){
        // choose sort by date
        return String.format("\n- %s - Season %d, Episode %d on %s\n", name, season, episode, platform);
    } if (sortByName.equals("platform")){
       
        //choose sort by platformj
        return String.format("\n- %s - Season %d, Episode %d on %s\n",name , season, episode, date);
    }
    if (sortByName.equals("name")){
       
        return String.format("\n- Season %d, Episode %d on %s on %s\n", season, episode, platform, date);
    } else if (sortByName.equals("default")){
        return String.format(" %s - Season %d, Episode %d on %s on %s\n", name, season, episode, platform, date);
    }
    else {
        return "";
    }
   }

      public void retrieveDataCategorize( String sortByName){
        String query = "";

        // Determine which SQL query to run based on the sort parameter
        if (!this.isTVLog) {
            query = "SELECT distinct name, platform FROM shows ORDER BY " + sortByName;
        } else {
            query = "SELECT name, season, episode, platform, date_created FROM shows ORDER by " + sortByName;
        }
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {


           StringBuilder showsText = new StringBuilder();
           String lastDate = "";


           while (rs.next()) {
              
                if (this.isTVLog){
                    String name = rs.getString("name");
                    String date = rs.getString("date_created");
                    
                    int season = rs.getInt("season");
                    int episode = rs.getInt("episode");
                    String platform = rs.getString("platform");
                    


                    if (!rs.getString(sortByName).equals(lastDate)) {
                        showsText.append("\n").append(rs.getString(sortByName).toUpperCase()).append("\n");
                        lastDate = rs.getString(sortByName);
                    }


                    String result = formatReturnMethod(sortByName, name, season, episode, platform, date);
                    showsText.append(result);

                } else {
                    String name = rs.getString("name");
                    String platform = rs.getString("platform");

                    String toCompare = rs.getString(sortByName);
                    if (!this.isTVLog && sortByName.equals("name")){
                        toCompare = toCompare.substring(0,1);
                    }

                    // If the first letter of the name changes, start a new section
                    if (!toCompare.equals(lastDate)) {
                        showsText.append("\n").append(toCompare.toUpperCase()).append("\n");
                        lastDate = toCompare;
                        if (!this.isTVLog && sortByName.equals(("name"))){
                            lastDate = lastDate.substring(0,1);
                        }
                    }

                    showsText.append(formatReturnMethod(sortByName, name, platform));
                }
               

                      
           }


           if (showsText.length() == 0) {
               showsText.append("No TV shows found in the database.");
           }
          
           showsList.setText(showsText.toString());
          


       } catch (SQLException ex) {
           showsList.setText("Error fetching data: " + ex.getMessage());
       }

   }

   public void searchDataBase(String searchName){
    String query = "";
    if (this.isTVLog){
        query = "SELECT name, season, episode, platform, date_created FROM shows WHERE name LIKE '%"+ searchName + "%'" + "or platform like '%" + searchName + "%'";
    } else {
        query = "SELECT DISTINCT name, platform FROM shows WHERE name LIKE '%" + searchName + "%' OR platform LIKE '%" + searchName + "%'";

    }
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {


           StringBuilder showsText = new StringBuilder();
           int index = 1;
           while (rs.next()) {
               String name = rs.getString("name");
               int season = 0;// = rs.getInt("season");
               int episode = 0;// = rs.getInt("episode");
               String date_created = "";//= rs.getString("date_created");
               if (isTVLog){
                season = rs.getInt("season");
               episode = rs.getInt("episode");
               date_created = rs.getString("date_created");
               }
               String platform = rs.getString("platform");
               
               showsText.append("\n" + index + ". ");

               if (isTVLog){
                showsText.append(formatReturnMethod("default", name, season, episode, platform, date_created));
               } else {
                showsText.append(formatReturnMethod("default", name, platform));
               }
               
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

    
}
