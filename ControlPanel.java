import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements ActionListener{
  private static int index = 1;
  private static int buttonHeight = 30;
  private static int WIDTH,HEIGHT;
  private static int buttonWidth = 180;
  public Color customGray = new Color(209,213,219);
  public Color customDarkGray = new Color(43,45,44);
  CustomPanel customPanel;
  Graph graph;
  JButton addNode; 
  JButton deleteNode;
  JButton createEdge;
  JButton saveImage;
  JButton[] buttons = new JButton[4];
  public ControlPanel(CustomPanel customPanel,Graph graph){
    this.graph = graph;
    this.customPanel = customPanel;
    ControlPanel.WIDTH = customPanel.getWidth();
    ControlPanel.HEIGHT = 75;
    addNode = new JButton("Add node"); 
    deleteNode = new JButton("Delete node");
    createEdge = new JButton("Create edge connection");
    saveImage = new JButton("Save");

    buttons[0] = addNode;
    buttons[1] = deleteNode;
    buttons[2] = createEdge;
    buttons[3] = saveImage;

    int gap = 10;

    int totalWidth = (buttonWidth * buttons.length) + (gap * (buttons.length - 1));
    int startX = (WIDTH - totalWidth) / 2;
    int y = 15;
    for(int i = 0;i<buttons.length;i++){
      JButton button = buttons[i];
      int x = startX + i * (buttonWidth + gap);

      button.setFocusable(false);
      button.setBounds(x,y,buttonWidth,buttonHeight);
      button.setBackground(customGray);
      button.setForeground(customDarkGray);
      button.addActionListener(this);
      add(button);   
    }  
  }
  @Override
  public void setBounds(int x, int y, int w, int h){
    super.setBounds(x, y, w, h);
    WIDTH = w;
    HEIGHT = h;

    int gap = 20;
    int totalWidth = (buttonWidth * buttons.length) + (gap * (buttons.length - 1));
    int startX = (WIDTH - totalWidth) / 2;
    int yPos = 15;
    for(int i = 0;i<buttons.length;i++){
      JButton button = buttons[i];
      int bx = startX + i * (buttonWidth + gap);
      if(button != null){
        button.setBounds(bx, yPos, buttonWidth, buttonHeight);
      }
    }
  }
  private void saveImage(JPanel panel){

    BufferedImage img = new BufferedImage(customPanel.getWidth(), customPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
    panel.paint(img.getGraphics());
    try{
      ImageIO.write(img,"png",new File("./Images/Screen" + index + ".png"));
      JOptionPane.showMessageDialog(customPanel, "Panel saved as image", "Image saved", JOptionPane.INFORMATION_MESSAGE);
    } catch(IllegalArgumentException e){
      e.printStackTrace();
    } catch(FileNotFoundException e){
      JOptionPane.showMessageDialog(customPanel, "File path not found", "Location not found", JOptionPane.INFORMATION_MESSAGE);
    } catch(FileAlreadyExistsException e){
      JOptionPane.showMessageDialog(customPanel, "File duplicate found", "Duplicate file", JOptionPane.INFORMATION_MESSAGE);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == addNode){
    new AddNodeFrame(graph,WIDTH,HEIGHT,customPanel,CustomPanel.size);
  }
  if(e.getSource() == createEdge){
    new CreateEdgeFrame(graph,customPanel);

  }
  if(e.getSource() == deleteNode){
    new DeleteNodeFrame(customPanel);
  }
  if(e.getSource() == saveImage){
    saveImage(customPanel);
    index++;
  }
  }
}
