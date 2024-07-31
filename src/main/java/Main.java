import UI.TaskManagerGUI;

import javax.swing.SwingUtilities;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskManagerGUI().setVisible(true));
    }
}
