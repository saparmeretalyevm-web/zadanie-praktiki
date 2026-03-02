import javax.swing.*;

public class MainLuxury {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LuxuryCarCenterGUI app = new LuxuryCarCenterGUI();
            app.setVisible(true);
        });
    }
}