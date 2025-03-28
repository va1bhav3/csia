import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class addShowMethod implements ActionListener {
   private final JFrame frame;
   private final JTextField nameField;
   private final JSpinner seasonSpinner;
   private final JSpinner episodeSpinner;
   private final JTextField platformField;
   private final String url;
   private final String user;
   private final String password;


   public addShowMethod(JFrame frame, JTextField nameField, JSpinner seasonSpinner, JSpinner episodeSpinner,
                        JTextField platformField, String url, String user, String password) {
       this.frame = frame;
       this.nameField = nameField;
       this.seasonSpinner = seasonSpinner;
       this.episodeSpinner = episodeSpinner;
       this.platformField = platformField;
       this.url = url;
       this.user = user;
       this.password = password;
   }


   @Override
   public void actionPerformed(ActionEvent e) {
       String nameFieldResult = nameField.getText();
       String platformFieldResult = platformField.getText();
       int season = (int) seasonSpinner.getValue();
       int episode = (int) episodeSpinner.getValue();


       if (nameFieldResult.isEmpty() || platformFieldResult.isEmpty()) {
           JOptionPane.showMessageDialog(frame, "Please fill out all fields.", "Error", JOptionPane.ERROR_MESSAGE);
       } else {
           try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO shows (name, season, episode, platform, date_created) VALUES (?, ?, ?, ?,?)")) {
                       LocalDate currentDate = LocalDate.now();
                       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                       String dateString = currentDate.format(formatter);
               stmt.setString(1, nameFieldResult.toUpperCase());
               stmt.setInt(2, season);
               stmt.setInt(3, episode);
               stmt.setString(4, platformFieldResult.toUpperCase());
               stmt.setString(5, dateString);
               stmt.executeUpdate();
               JOptionPane.showMessageDialog(frame, "Show added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);


               nameField.setText("");
               platformField.setText("");
               seasonSpinner.setValue(1);
               episodeSpinner.setValue(1);
           }
           catch (SQLException ex) {
              
           }
       }
   }
}
