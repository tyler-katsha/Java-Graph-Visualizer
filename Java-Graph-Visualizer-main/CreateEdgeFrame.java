import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class CreateEdgeFrame extends JFrame implements ActionListener{
    private final static int WIDTH =  250,HEIGHT = 250;
    private final JLabel pointOneLabel,pointTwoLabel,weightLabel;
    private final JButton createButton;
    private final JTextField pointOneField,pointTwoField,weightField;
    private final Graph graph;
    private final CustomPanel panel;
    private final JPanel fieldPanel,bottomPanel;
  public CreateEdgeFrame(Graph graph,CustomPanel panel){
    this.graph = graph;
    this.panel = panel;
    pointOneLabel = new JLabel("Enter point one: ");
    weightLabel = new JLabel("Enter a edge weight: ");
    pointTwoLabel = new JLabel("Enter point two: ");

    createButton = new JButton("Create");

    pointOneField = new JTextField();
    pointTwoField = new JTextField();
    weightField = new JTextField();

    fieldPanel = new JPanel(new GridLayout(3,3));
    bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));


  }

  public CreateEdgeFrame setGui(){
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setSize(WIDTH,HEIGHT);
      setLocationRelativeTo(null);
      setLayout(new BorderLayout());

      pointOneField.setFont(Fonts.ARIAL_FONT);
      pointTwoField.setFont(Fonts.ARIAL_FONT);

      addToFieldPanel(pointOneLabel,pointOneField);
      addToFieldPanel(pointTwoLabel,pointTwoField);
      addToFieldPanel(weightLabel,weightField);

      createButton.addActionListener(this);
      createButton.setFocusable(false);
      createButton.setBackground(Colors.customDarkGray);
      createButton.setForeground(Color.white);

      bottomPanel.add(createButton);

      add(fieldPanel,BorderLayout.CENTER);
      add(bottomPanel,BorderLayout.SOUTH);

      setVisible(true);

      return this;
  }

  private void addToFieldPanel(JLabel lbl,JTextField txtF) {
      fieldPanel.add(lbl);
      fieldPanel.add(txtF);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == createButton){

        char x = pointOneField.getText().strip().charAt(0);
        char y = pointTwoField.getText().strip().charAt(0);
        int w;

        try {
            w = Integer.parseInt(weightField.getText().strip());
        } catch (NumberFormatException ex) {
            String title = "Invalid Format";
            String reason = ex.getMessage();
            Popups.showError(reason,title);
            return;
        }

        boolean isRealX = graph.searchBool(x);
        boolean isRealY = graph.searchBool(y);

        if(!isRealX) {
          String title = "Doesn't exist node";
          String reason = String.format("Node %s is not in the graph",x);
          Popups.showError(reason,title);
        }

        if(!isRealY) {
          String title = "Doesn't exist node";
          String reason = String.format("Node %s is not in the graph",y);
          Popups.showError(reason,title);
          return;
        }

        int iIndex = graph.search(x);
        int jIndex = graph.search(y);

        if(graph.checkEdge(iIndex,jIndex) || graph.checkEdge(jIndex,iIndex)){

          String title = "Duplicate Node";
          String reason = "There is an Edge connected already";
          Popups.showError(reason,title);
          return;
        }
        graph.addEdge(iIndex, jIndex,w);
        graph.printG();
        panel.repaint();
        dispose();
    }
  }
}
