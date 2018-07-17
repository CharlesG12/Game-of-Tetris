import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;

public class ConfigUI extends JFrame{
    private JPanel sliderPanel;
    private JTextField textField;
    private JTextField tf1,tf2;
    private JFormattedTextField width;
    private JFormattedTextField height;
    private int M, N, Size;
    private int rWidth, rHeight;
    private double S;
    private Font f = new Font("Courier", Font.BOLD,30);



    public static void main(String[] args) {

    }

    ConfigUI(LinkedList<Integer> list){
        super("Game of Tetris setting");
        setSize(800,600);
        setFont(new Font("Courier", Font.BOLD,75));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        sliderPanel = new JPanel();
        sliderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        /**  Slider for scoring factor
         *   range:	1-10
         *   Score	=	Score	+	Level	x	M
         */
        JSlider slider_M = new JSlider();
        slider_M.setPaintTicks(true);
        slider_M.setPaintLabels(true);
        slider_M.setMajorTickSpacing(1);
        slider_M.setMinorTickSpacing(1);
        slider_M.setMinimum(1);
        slider_M.setMaximum(10);
        slider_M.setValue(1);
        slider_M.setFont(f);
        addSlider(slider_M, "Scoring Factor", 1);

        /**  Slider for number	of	rows	required	for	each	Level	of	difficulty
         *   range:	20-50
         *   Score	=	Score	+	Level	x	M
         */
        JSlider slider_N = new JSlider();
        slider_N.setPaintTicks(true);
        slider_N.setPaintLabels(true);
        slider_N.setMajorTickSpacing(3);
        slider_N.setMinorTickSpacing(1);
        slider_N.setMinimum(20);
        slider_N.setMaximum(50);
        slider_N.setValue(20);
        slider_N.setFont(f);
        addSlider(slider_N, "Number of rows to level up", 2);

        /**  Slider for speed factor
         *   range:	0.1-1
         *   Score	=	Score	+	Level	x	M
         */
        JSlider slider_S = new JSlider();
        slider_S.setPaintTicks(true);
        slider_S.setPaintLabels(true);
        slider_S.setMajorTickSpacing(3);
        slider_S.setMinorTickSpacing(1);
        slider_S.setMinimum(1);
        slider_S.setMaximum(10);
        slider_S.setValue(1);
        slider_S.setFont(f);

        Dictionary<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(1, new JLabel("0.1"));
        labelTable.put(4, new JLabel("0.4"));
        labelTable.put(7, new JLabel("0.7"));
        labelTable.put(10, new JLabel("1.0"));

        slider_S.setLabelTable(labelTable);
        addSlider(slider_S, "speed factor", 3);

        /**  Slider for change square size
         *   range:	small, medium, large
         */
        JSlider slider_Size = new JSlider();
        slider_Size.setPaintTicks(true);
        slider_Size.setPaintLabels(true);
        slider_Size.setMajorTickSpacing(1);
        slider_Size.setMinorTickSpacing(1);
        slider_Size.setMinimum(1);
        slider_Size.setMaximum(3);
        slider_Size.setValue(2);
        slider_Size.setFont(f);


        Dictionary<Integer, JLabel> labelTable_2 = new Hashtable<>();
        labelTable_2.put(1, new JLabel("Small"));
        labelTable_2.put(2, new JLabel("Medium"));
        labelTable_2.put(3, new JLabel("Large"));

        slider_Size.setLabelTable(labelTable_2);
        addSlider(slider_Size, "Square Size", 4);

        /*
         **  text field for height and width of the grid
         */
        tf1 = new JTextField("10");
        tf2 = new JTextField("20");
        JPanel p = new JPanel(new java.awt.GridLayout(0, 2, 3, 8));

        p.add(tf1);
        p.add(new JLabel("width"));
        p.add(tf2);
        p.add(new JLabel("height"));
        p.setFont(f);
        sliderPanel.add(p);


        /**  button to start game
         *   range:	1-10
         *   Score	=	Score	+	Level	x	M
         */
        JButton b1 = new JButton("start");
        b1.setFont(f);


        /** Start game after press button
         * @param M, N, S
         */
        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if( M <= 0 ) { M = 1; }
                if( N <= 0 ) { N =20; }
                if( S < 0.1) { S = 0.1; }
                if( Size <= 0 ) { Size = 2; }

                GameSetting set = new GameSetting( M, N, S );
                set.changeSize(Size);

                String textFieldValue_W = tf1.getText();
                String textFieldValue_H = tf2.getText();

                rWidth = Integer.valueOf(textFieldValue_W);
                rHeight = Integer.valueOf(textFieldValue_H);

                if( rWidth < 10 ) { rWidth = 10; }
                if( rHeight < 20 ) { rHeight = 20; }

                set.changeGrid(rWidth, rHeight);

                new TetrisUI(set, list).setVisible(true);

                setVisible(false);
            }
        });

        addButton(b1, "Press Button to start game.");

        textField = new JTextField();
        add(sliderPanel, BorderLayout.CENTER);
        add(textField, BorderLayout.SOUTH);
    }

    /**
     * Adds a slider to the slider panel and hooks up the listener
     * @param s the slider
     * @param description the slider description
     */
    public void addSlider(JSlider s, String description, int v)
    {
        s.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider  slider = (JSlider ) e.getSource();

                switch(v) {
                    case 1: M = slider.getValue();
                             break;
                    case 2: N = slider.getValue();
                              break;
                    case 3: S = (double)slider.getValue()/10;
                            break;
                    case 4: Size = slider.getValue();
                        break;
                    default: break;
                }

            }
        });

        JPanel panel = new JPanel();
        panel.add(s);
        panel.add(new JLabel(description));
        sliderPanel.add(panel);
    }

    /**
     * Adds a button to the slider panel and hooks up the listener
     * @param s the button
     * @param description the button description
     */
    public void addButton(JButton b, String description)
    {
        // s.addChangeListener(listener);
        JPanel panel = new JPanel();
        panel.add(b);
        panel.add(new JLabel(description));
        sliderPanel.add(panel);
    }
}

class ButtonListener implements ActionListener {
    ButtonListener( ) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("start")) {
            System.out.println("Button has been clicked");
        }
    }
}

class GameSetting {
    int M, N, Size;
    int Width, Height;
    double S;

    GameSetting(int m, int n, double s){
        this.M = m;
        this.N = n;
        this.S = s;
    }

    void changeSize(int size) {
        this.Size = size;
    }

    void changeGrid( int w, int h) {
        this.Height = h;
        this.Width = w;
    }

}

