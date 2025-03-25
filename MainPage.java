import javax.swing.*;
import java.awt.*;


public class MainPage {
   private static final String URL = "jdbc:mysql://localhost:3306/tv_show_tracker";
   private static final String USER = "root";
   private static final String PASSWORD = "Va1bhav@2008";


   public static void main(String[] args) {
       SwingUtilities.invokeLater(MainPage::createAndShowInterface);
   }

   private static void createAndShowInterface() {
       JFrame frame = new JFrame("TV Show Tracker");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setLayout(new BorderLayout(10, 10));
       frame.setMinimumSize(new Dimension(500, 400));
       frame.setLocationRelativeTo(null);


       JPanel titlePanel = new JPanel();
       JLabel titleLabel = new JLabel("TV SHOW TRACKER", SwingConstants.CENTER);
       titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
       titlePanel.add(titleLabel);
       frame.add(titlePanel, BorderLayout.NORTH);


       JPanel formPanel = new JPanel(new GridBagLayout());
       GridBagConstraints gbc = new GridBagConstraints();
       gbc.insets = new Insets(10, 10, 10, 10);
       gbc.anchor = GridBagConstraints.WEST;
       gbc.fill = GridBagConstraints.HORIZONTAL;


       gbc.gridx = 0;
       gbc.gridy = 0;
       formPanel.add(new JLabel("TV Show Name:"), gbc);
       gbc.gridx = 1;
       JTextField nameField = new JTextField(20);
       formPanel.add(nameField, gbc);


       gbc.gridx = 0;
       gbc.gridy = 1;
       formPanel.add(new JLabel("Most Recent Season:"), gbc);
       gbc.gridx = 1;
       JSpinner seasonSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
       formPanel.add(seasonSpinner, gbc);


       gbc.gridx = 0;
       gbc.gridy = 2;
       formPanel.add(new JLabel("Most Recent Episode:"), gbc);
       gbc.gridx = 1;
       JSpinner episodeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
       formPanel.add(episodeSpinner, gbc);


       gbc.gridx = 0;
       gbc.gridy = 3;
       formPanel.add(new JLabel("Platform Watched On:"), gbc);
       gbc.gridx = 1;
       JTextField platformField = new JTextField(20);
       formPanel.add(platformField, gbc);


       frame.add(formPanel, BorderLayout.CENTER);


       JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
       JButton addButton = new JButton("Add Entry");
       JButton showButton = new JButton("Show Log");
       JButton logButton = new JButton("Show TV Shows");
       buttonPanel.add(addButton);
       buttonPanel.add(showButton);
       buttonPanel.add(logButton);
       frame.add(buttonPanel, BorderLayout.SOUTH);


       addButton.addActionListener(new addShowMethod(frame, nameField, seasonSpinner, episodeSpinner, platformField, URL, USER, PASSWORD));
       showButton.addActionListener(new ShowLogAction());
       logButton.addActionListener(new ShowTVShowsAction(frame));


       frame.setVisible(true);
   }
}
