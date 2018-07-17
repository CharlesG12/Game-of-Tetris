import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import javax.swing.*;


public class ShapesUI extends JFrame {
    Font f = new Font("Courier", Font.BOLD,30);
    LinkedList<Integer> indexs = new LinkedList<Integer>();

    public ShapesUI() {
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        addButton();

        for( int i = 1; i <= 8; i++ ){
            generateShape(i);
        }
    }

    public static void main(String[] args) {
 //       new ShapesUI().setVisible(true);
    }

    /*
    * add shapes position to the linkedlist if selected.
     */
    public void generateShape(int number) {
        JCheckBox checkBox = new JCheckBox("Not Selected");
        checkBox.setSelected(false);

        // Set default icon for checkbox
        checkBox.setIcon(new ImageIcon(this.getClass().getResource("/images/s_" + Integer.toString(number) + ".png")));

        // add checbox ItemLister
        checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
               if(checkBox.isSelected()){
                   checkBox.setText("Selected");

                   indexs.add(number);
               }
               else{
                   checkBox.setText("Not Selected");
                   indexs.remove(number);
               }
            }
        });
        checkBox.setFont(f);
        getContentPane().add(checkBox);
    }

    /*
    * pass the linkedlist to ConfigUI
     */
    private void addButton() {
        JButton b1 = new JButton("next");
        b1.setFont(f);

        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                new ConfigUI(indexs).setVisible(true);

                setVisible(false);
            }
        });

        getContentPane().add(b1);

    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ShapesUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
