import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements ActionListener{
  private final CustomPanel customPanel;
  private final Graph graph;
  private final JButton addNodeBtn,deleteNodeBtn,createEdgeBtn,saveImageBtn;
  private final JButton[] buttons = new JButton[4];


  public ControlPanel(CustomPanel customPanel,Graph graph){
    this.graph = graph;
    this.customPanel = customPanel;
    addNodeBtn = new JButton("Add node");
    deleteNodeBtn = new JButton("Delete node");
    createEdgeBtn = new JButton("Create edge connection");
    saveImageBtn = new JButton("Save");
  }

  public ControlPanel setGui(){

    buttons[0] = addNodeBtn;
    buttons[1] = createEdgeBtn;
    buttons[2] = deleteNodeBtn;
    buttons[3] = saveImageBtn;

    for (JButton button : buttons) {
      button.setFocusable(false);
      button.setBackground(Colors.customGray);
      button.setForeground(Colors.customDarkGray);
      button.addActionListener(this);
      add(button);
    }

    return this;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == addNodeBtn){
    new AddNodeFrame(graph,customPanel).setGui();
  }
  if(e.getSource() == createEdgeBtn){
    new CreateEdgeFrame(graph,customPanel)
            .setGui();

  }
  if(e.getSource() == deleteNodeBtn){
    new DeleteNodeFrame(customPanel)
            .setGui();
  }
  if(e.getSource() == saveImageBtn){
    int w = customPanel.getWidth();
    int h = customPanel.getHeight();
    ImageService.saveImage(w,h,customPanel);
  }
  }
}
