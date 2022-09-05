import javax.swing.JFrame;

public class SimulationRunner {

  public static void main(String[] args) {
    JFrame f = new JFrame("Put Frame Title Here"); 
    GraphPanel p = new GraphPanel();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.addMouseListener(p);
    f.addMouseMotionListener(p);
    f.addKeyListener(p);
    f.add(p);
    f.pack();
    f.setVisible(true);
  }
}