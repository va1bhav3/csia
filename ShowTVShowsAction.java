import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowTVShowsAction implements ActionListener {
    private final JFrame frame;

    public ShowTVShowsAction(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(frame, "blank", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}

