import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class DeleteNodeFrame extends JFrame implements ActionListener{
  private int w = 250;
  private int h = 250;
  JLabel label;
  JTextArea areaNodeField = new JTextArea();
  JButton deleteButton;
  CustomPanel customPanel;
  public Color customDarkGray = new Color(43,45,44);
  DeleteNodeFrame(CustomPanel customPanel){
    this.customPanel = customPanel;
    label = new JLabel("<html>Enter a character to delete</html>");
    deleteButton = new JButton("Delete");

    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(w,h);
    setLocationRelativeTo(null);
    setLayout(null);
    label.setBounds(300/2 - 150, 20, 200, 45);
    label.setFont(new Font("Arial",Font.PLAIN,22));
    label.setHorizontalAlignment(JLabel.CENTER);
    areaNodeField.setBounds(300/2 - 45, 65, 80, 25);
    areaNodeField.setFont(new Font("Arial",Font.PLAIN,15));
    
    
  
    deleteButton.setBounds(w/2 - 45, 100, 80, 30);
    deleteButton.addActionListener(this);
    add(label);
    add(areaNodeField);

    deleteButton.setFocusable(false);
    deleteButton.setBackground(customDarkGray);
    deleteButton.setForeground(Color.white);
    add(deleteButton);
    setVisible(true);
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == deleteButton){
      String text = areaNodeField.getText().toUpperCase();
      char character = text.charAt(0);
      boolean deleted = customPanel.graph.deleteNode(character);
        if(text.length() != 1){
        JOptionPane.showMessageDialog(customPanel, "Please enter exactly one character", "Invalid input", JOptionPane.ERROR_MESSAGE);
        return;
      }
      
      if(!deleted){
        JOptionPane.showMessageDialog(this, "Node '" + character + "' not found", "Not found", JOptionPane.WARNING_MESSAGE);
        return;
      }

      customPanel.repaint();
      dispose();
    }
  }
}
  

