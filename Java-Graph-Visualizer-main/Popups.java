import javax.swing.*;

public class Popups {

    public static void showMessage(String reason,String title,int type) {
        JOptionPane.showMessageDialog(null, reason,title,type);
    }

    public static void showError(String reason, String title) {
        JOptionPane.showMessageDialog(null, reason,title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(String reason, String title) {
        JOptionPane.showMessageDialog(null, reason,title, JOptionPane.WARNING_MESSAGE);
    }
    public static void showInfo(String reason, String title) {
        JOptionPane.showMessageDialog(null, reason,title, JOptionPane.INFORMATION_MESSAGE);
    }
}
