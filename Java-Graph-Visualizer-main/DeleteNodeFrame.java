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
  private final static int WIDTH = 250,HEIGHT = 250;
  private final JLabel label;
  private final JTextArea areaNodeField;
  private final JButton deleteButton;
  private final CustomPanel customPanel;

  public DeleteNodeFrame(CustomPanel customPanel){
    this.customPanel = customPanel;
    label = new JLabel("<html>Enter a character to delete</html>");
    deleteButton = new JButton("Delete");
    areaNodeField = new JTextArea();
  }

  public DeleteNodeFrame setGui(){
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(WIDTH,HEIGHT);
    setLocationRelativeTo(null);
    setLayout(null);

    label.setBounds(300/2 - 150, 20, 200, 45);
    label.setFont(new Font("Arial",Font.PLAIN,22));
    label.setHorizontalAlignment(JLabel.CENTER);

    areaNodeField.setBounds(300/2 - 45, 65, 80, 25);
    areaNodeField.setFont(new Font("Arial",Font.PLAIN,15));



    deleteButton.setBounds(WIDTH/2 - 45, 100, 80, 30);
    deleteButton.addActionListener(this);
    add(label);
    add(areaNodeField);

    deleteButton.setFocusable(false);
    deleteButton.setBackground(Colors.customDarkGray);
    deleteButton.setForeground(Color.white);

    add(deleteButton);
    setVisible(true);

    return this;
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == deleteButton){
      String text = areaNodeField.getText().strip().toUpperCase();
      char data = text.charAt(0);
      boolean deleted = customPanel.getGraph().deleteNode(data);
        if(text.length() != 1){
          String reason = "Please enter exactly one character";
          String title = "Invalid input";
          Popups.showWarning(reason,title);
        return;
      }
      
      if(!deleted){

        String reason = String.format("Node '%s' not found",data);
        String title = "Not found";
        Popups.showError(reason,title);
        return;
      }

      customPanel.repaint();
      dispose();
    }
  }
}
  

