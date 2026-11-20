import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class TopControlPanel extends JPanel {

    private final JComboBox<GraphPreset> presetJComboBox;
    private final JComboBox<AlgorithmType> algorithmTypeJComboBox;
    private final JTextField startNodeField;
    private final CustomPanel customPanel;

    // A* inputs
    private final JTextField srcRowField;
    private final JTextField srcColField;
    private final JTextField destRowField;
    private final JTextField destColField;

    private JPanel graphInputsPanel;
    private JPanel aStarInputsPanel;

    // 1 = open, 0 = blocked
    private final int[][] aStarGrid = {
            { 1, 0, 1, 1, 1, 1, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 1, 1, 1, 0, 1, 1 },
            { 1, 1, 1, 0, 1, 1, 0, 1, 0, 1 },
            { 0, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
            { 1, 1, 1, 0, 1, 1, 1, 0, 1, 0 },
            { 1, 0, 1, 1, 1, 1, 0, 1, 0, 0 },
            { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
            { 1, 0, 1, 1, 1, 1, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 1, 0, 0, 1 }
    };

    public TopControlPanel(CustomPanel customPanel) {
        this.customPanel = customPanel;
        this.algorithmTypeJComboBox = new JComboBox<>(AlgorithmType.values());
        this.presetJComboBox = new JComboBox<>(GraphPreset.values());
        this.startNodeField = new JTextField("A", 3);

        // Default src = {8, 0}, dest = {0, 0}
        this.srcRowField = createCoordField("8");
        this.srcColField = createCoordField("0");
        this.destRowField = createCoordField("0");
        this.destColField = createCoordField("0");
    }

    private JTextField createCoordField(String defaultVal) {
        JTextField field = new JTextField(defaultVal, 2);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.addActionListener(e -> executeSelectedAlgorithm());

        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateAStarPreview();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateAStarPreview();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateAStarPreview();
            }
        });
        return field;
    }

    public TopControlPanel setGui() {
        setBackground(Colors.customDarkGray);
        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));

        // --- Graph-mode inputs (Preset + Start Node) ---
        graphInputsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        graphInputsPanel.setOpaque(false);

        JLabel presetLabel = createLabel("Graph:");
        presetJComboBox.addItemListener(e -> {
            GraphPreset graphPreset = (GraphPreset) presetJComboBox.getSelectedItem();
            if (graphPreset != null) {
                customPanel.loadPreset(graphPreset);
            }
        });

        JLabel startLabel = createLabel("Start Node:");
        startNodeField.setHorizontalAlignment(JTextField.CENTER);
        startNodeField.addActionListener(e -> executeSelectedAlgorithm());

        graphInputsPanel.add(presetLabel);
        graphInputsPanel.add(presetJComboBox);
        graphInputsPanel.add(startLabel);
        graphInputsPanel.add(startNodeField);

        // --- A*-mode inputs (Src [r,c] + Dest [r,c]) ---
        aStarInputsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        aStarInputsPanel.setOpaque(false);
        aStarInputsPanel.setVisible(false);

        aStarInputsPanel.add(createLabel("Src (r,c):"));
        aStarInputsPanel.add(srcRowField);
        aStarInputsPanel.add(srcColField);
        aStarInputsPanel.add(createLabel("Dest (r,c):"));
        aStarInputsPanel.add(destRowField);
        aStarInputsPanel.add(destColField);

        add(graphInputsPanel);
        add(aStarInputsPanel);

        // --- Algorithm selector ---
        algorithmTypeJComboBox.setSelectedIndex(0);
        algorithmTypeJComboBox.addActionListener(e -> toggleModeInputs());
        add(algorithmTypeJComboBox);

        JButton runBtn = new JButton("Run");
        runBtn.addActionListener(e -> executeSelectedAlgorithm());
        add(runBtn);

        JLabel speedLabel = createLabel("Delay (ms):");
        JSlider speedSlider = new JSlider(100, 1500, 300);
        speedSlider.setBackground(Colors.customDarkGray);
        speedSlider.addChangeListener(e -> customPanel.setAnimationDelay(speedSlider.getValue()));

        add(speedLabel);
        add(speedSlider);

        JButton resetBtn = new JButton("Reset Colors");
        resetBtn.addActionListener(e -> {
            algorithmTypeJComboBox.setSelectedIndex(0);
            toggleModeInputs();
            customPanel.resetNodes();
        });
        add(resetBtn);

        setVisible(true);
        return this;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void updateAStarPreview(){
        if(algorithmTypeJComboBox.getSelectedItem() != AlgorithmType.ASTAR) return;

        int rs = aStarGrid.length;
        int cs = aStarGrid[0].length;

        try {
            int[] src = { Integer.parseInt(srcRowField.getText().strip()), Integer.parseInt(srcColField.getText().strip()) };
            int[] dest = { Integer.parseInt(destRowField.getText().strip()), Integer.parseInt(destColField.getText().strip())};

            if(isValidCell(src,rs,cs) && isValidCell(dest,rs,cs) && aStarGrid[src[0]][src[1]] == 1 && aStarGrid[dest[0]][dest[1]] == 1) {
                customPanel.loadAStarGrid(aStarGrid,src,dest);
            }
        } catch(NumberFormatException _) {}
    }

    private void toggleModeInputs() {
        boolean isAStar = algorithmTypeJComboBox.getSelectedItem() == AlgorithmType.ASTAR;
        graphInputsPanel.setVisible(!isAStar);
        aStarInputsPanel.setVisible(isAStar);

        if(isAStar) {

            int[] src = { Integer.parseInt(srcRowField.getText().strip()), Integer.parseInt(srcColField.getText().strip()) };
            int[] dest = { Integer.parseInt(destRowField.getText().strip()), Integer.parseInt(destColField.getText().strip())};

            customPanel.loadAStarGrid(aStarGrid,src,dest);
        } else {
            GraphPreset preset = (GraphPreset) presetJComboBox.getSelectedItem();
            if(preset != null) customPanel.loadPreset(preset);
        }
        revalidate();
        repaint();
    }

    private void executeSelectedAlgorithm() {
        AlgorithmType selectedAlgo = (AlgorithmType) algorithmTypeJComboBox.getSelectedItem();

        if (selectedAlgo == null || selectedAlgo == AlgorithmType.NONE) {
            Popups.showError("Please select an algorithm first.", "No Algorithm Selected");
            return;
        }

        if (selectedAlgo == AlgorithmType.ASTAR) {
            executeAStar();
            return;
        }

        String input = startNodeField.getText().strip().toUpperCase();
        if (input.isEmpty()) {
            Popups.showError("Please enter a starting node label (e.g., A, C, F, etc...).", "Empty Input");
            return;
        }

        char targetLabel = input.charAt(0);
        int startIndex = customPanel.getGraph().search(targetLabel);

        if (startIndex == -1) {
            Popups.showError(String.format("Node '%c' does not exist in the graph.", targetLabel), "Invalid Node");
            return;
        }

        customPanel.runAlgorithm(selectedAlgo, startIndex);
    }

    private void executeAStar() {
        int rows = aStarGrid.length;
        int cols = aStarGrid[0].length;

        try {
            int[] src = { Integer.parseInt(srcRowField.getText().strip()), Integer.parseInt(srcColField.getText().strip()) };
            int[] dest = { Integer.parseInt(destRowField.getText().strip()), Integer.parseInt(destColField.getText().strip())};

            if (!isValidCell(src, rows, cols) || !isValidCell(dest, rows, cols)) {
                Popups.showError(String.format("Coordinates must be within row [0-%d] and col [0-%d].", rows - 1, cols - 1), "Out of Bounds");
                return;
            }

            if (aStarGrid[src[0]][src[1]] == 0 || aStarGrid[dest[0]][dest[1]] == 0) {
                Popups.showError("Source or destination cell is blocked (0).", "Blocked Cell");
                return;
            }

            customPanel.runAStar(aStarGrid, src, dest,AlgorithmType.ASTAR);

        } catch (NumberFormatException ex) {
            Popups.showError("Please enter valid integer coordinates for Src and Dest.", "Invalid Coordinates");
        }
    }

    private boolean isValidCell(int[] coord, int rows, int cols) {
        return coord[0] >= 0 && coord[0] < rows && coord[1] >= 0 && coord[1] < cols;
    }
}