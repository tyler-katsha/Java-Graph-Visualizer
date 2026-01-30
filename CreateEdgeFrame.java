import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class CreateEdgeFrame extends JFrame implements ActionListener{
    private int w=  250;
    private int h = 250;
    JLabel label_1;
    JLabel label_2;
    JButton createButton;
    public Color customDarkGray = new Color(43,45,44);
    JTextArea textArea1 = new JTextArea();
    JTextArea textArea2 = new JTextArea();
    Graph graph;
    CustomPanel panel;
  CreateEdgeFrame(Graph graph,CustomPanel panel){
    this.graph = graph;
    this.panel = panel;
    label_1 = new JLabel("Enter point one: ");
    label_2 = new JLabel("Enter point two: ");
    createButton = new JButton("Create");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(w,h);
    setLocationRelativeTo(null);
    setLayout(null);
      label_1.setBounds(10,10,125,30);
    label_2.setBounds(10,50,150,30);

    textArea1.setBounds(130,10,100,30);
    textArea1.setFont(new Font("Arial",Font.PLAIN,15));

    textArea2.setBounds(130,50,100,30);
    textArea2.setFont(new Font("Arial",Font.PLAIN,15));
    
    createButton.setBounds(w/2 - 40, 100, 80, 30);
    createButton.addActionListener(this);
    createButton.setFocusable(false);
    createButton.setBackground(customDarkGray);
    createButton.setForeground(Color.white);

    add(label_1);
    add(label_2);
    add(createButton);
    add(textArea1);
    add(textArea2);

    setVisible(true);
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == createButton){
      try{
        int i = Integer.parseInt(textArea1.getText().strip());
        int j = Integer.parseInt(textArea2.getText().strip());

        int iIndex = i - 1;
        int jIndex = j - 1;

        if(iIndex < 0 || jIndex < 0){
          JOptionPane.showMessageDialog(this, "Node indices must be at least 1", "Invalid value/s", JOptionPane.WARNING_MESSAGE);
          return;
        } 
        if(iIndex >= graph.nodes.size() || jIndex >= graph.nodes.size()){
          JOptionPane.showMessageDialog(this, "Out of range", "Out of Array Bounds", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if(graph.checkEdge(iIndex,jIndex) || graph.checkEdge(jIndex,iIndex)){
          JOptionPane.showMessageDialog(this, "There is an Edge connected already", "Duplicate Node", JOptionPane.ERROR_MESSAGE);
          return;
        }
        graph.addEdge(iIndex, jIndex);
        graph.print();
        panel.repaint();
        dispose();
        
      } catch(NumberFormatException e1){
        JOptionPane.showMessageDialog(this, "Invalid number format correct format explain: 1,2,3. Not 1@m,!,0_", "Invalid Format", JOptionPane.ERROR_MESSAGE);
      }
      
      
    }
  }
}
