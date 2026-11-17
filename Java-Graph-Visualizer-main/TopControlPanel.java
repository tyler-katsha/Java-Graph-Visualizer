import javax.swing.*;
import java.awt.*;

public class TopControlPanel extends JPanel {

    private final JComboBox<GraphPreset> presetJComboBox;
    private final JComboBox<AlgorithmType> algorithmTypeJComboBox;
    private final JTextField startNodeField;
    private final CustomPanel customPanel;

    public TopControlPanel(CustomPanel customPanel) {
        this.customPanel = customPanel;
        this.algorithmTypeJComboBox = new JComboBox<>(AlgorithmType.values());
        this.presetJComboBox = new JComboBox<>(GraphPreset.values());
        this.startNodeField = new JTextField("A", 3); // Default to 'A', width of 3 columns
    }

    public TopControlPanel setGui() {

        setBackground(Colors.customDarkGray);
        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));

        JLabel presetLabel = new JLabel("Graph:");
        presetLabel.setForeground(Color.WHITE);
        presetJComboBox.addItemListener(e -> {
            GraphPreset graphPreset = (GraphPreset) presetJComboBox.getSelectedItem();
            if(graphPreset != null) {
                customPanel.loadPreset(graphPreset);
            }
        });

        add(presetLabel);
        add(presetJComboBox);
        JLabel startLabel = new JLabel("Start Node:");
        startLabel.setForeground(Color.WHITE);
        startNodeField.setHorizontalAlignment(JTextField.CENTER);

        add(startLabel);
        add(startNodeField);

        algorithmTypeJComboBox.setSelectedIndex(0);
        add(algorithmTypeJComboBox);

        JButton runBtn = new JButton("Run");
        runBtn.addActionListener(e -> executeSelectedAlgorithm());
        startNodeField.addActionListener(e -> executeSelectedAlgorithm());
        add(runBtn);

        JLabel speedLabel = new JLabel("Delay (ms):");
        speedLabel.setForeground(Color.WHITE);

        JSlider speedSlider = new JSlider(100, 1500, 300);
        speedSlider.setBackground(Colors.customDarkGray);
        speedSlider.addChangeListener(e -> customPanel.setAnimationDelay(speedSlider.getValue()));

        add(speedLabel);
        add(speedSlider);

        JButton resetBtn = new JButton("Reset Colors");
        resetBtn.addActionListener(e -> {
            algorithmTypeJComboBox.setSelectedIndex(0);
            customPanel.resetNodes();
        });
        add(resetBtn);

        setVisible(true);
        return this;
    }

    private void executeSelectedAlgorithm() {
        AlgorithmType selectedAlgo = (AlgorithmType) algorithmTypeJComboBox.getSelectedItem();

        if (selectedAlgo == null || selectedAlgo == AlgorithmType.NONE) {
            String reason = "Please select an algorithm first.";
            String title = "No Algorithm Selected";
            Popups.showError(reason, title);
            return;
        }

        String input = startNodeField.getText().strip().toUpperCase();
        if (input.isEmpty()) {
            String reason = "Please enter a starting node label (e.g., A, C, F,etc...).";
            String title = "Empty Input";
            Popups.showError(reason,title);
            return;
        }

        char targetLabel = input.charAt(0);
        int startIndex = customPanel.getGraph().search(targetLabel);

        if (startIndex == -1) {
            String reason = String.format("Node '%c' does not exist in the graph.", targetLabel);
            String title = "Invalid Node";
            Popups.showError(reason, title);
            return;
        }

        customPanel.runAlgorithm(selectedAlgo, startIndex);
    }
}