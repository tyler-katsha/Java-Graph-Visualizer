import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private static MainFrame INSTANCE;

    private static TopControlPanel topControlPanel;
    private static CustomPanel customPanel;
    private static ControlPanel controlPanel;


    public MainFrame(CustomPanel customPanel, ControlPanel controlPanel, TopControlPanel topControlPanel) {

        MainFrame.topControlPanel = topControlPanel;
        MainFrame.controlPanel = controlPanel;
        MainFrame.customPanel = customPanel;


    }

    public MainFrame setGui(){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());


        add(topControlPanel,BorderLayout.NORTH);
        add(customPanel,BorderLayout.CENTER);
        add(controlPanel,BorderLayout.SOUTH);
        pack();

        setVisible(true);

        return this;
    }
}
