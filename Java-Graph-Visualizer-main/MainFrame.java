import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private final TopControlPanel topControlPanel;
    private final CustomPanel customPanel;
    private final ControlPanel controlPanel;


    public MainFrame(CustomPanel customPanel, ControlPanel controlPanel, TopControlPanel topControlPanel) {

        this.topControlPanel = topControlPanel;
        this.controlPanel = controlPanel;
        this.customPanel = customPanel;


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
