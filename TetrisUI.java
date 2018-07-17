import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import javax.swing.*;

//
public class TetrisUI extends JFrame{
    private static final long serialVersionUID = 1L;
    private JSlider slider_m, slider_n, slider_s;
    private JLabel lbl;
    private boolean start = true;

    public static void main(String[] args) {
       // new TetrisUI().setVisible(true);
    }

    public TetrisUI(GameSetting setting, LinkedList<Integer> list) {
        // initial the configuration
        super("Game of Tetris");

        if( setting.Size == 1 ) {
            setSize(900,800);
        }
        else if( setting.Size == 2) {
            setSize(1350,1200);
        }
        else {
            setSize(1800,1600);
        }

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        CvTetris game = new CvTetris(new GameEngine(setting.Width, setting.Height, list), setting);

        add("Center", game);

        new Thread() {
            @Override public void run() {
                while ( game.running() ) {
                    double new_speed = game.getSpeed();

                    try {
                        int intervalTime = 500 -  (int)(150 * new_speed);
                        if ( intervalTime <= 0) {
                            intervalTime = 100;
                        }
                        Thread.sleep(intervalTime);
                        game.move_down();
                    } catch ( InterruptedException e ) {}
                }
            }
        }.start();

    }
}


class CvTetris extends Canvas {
    float rWidth = 16.0F, rHeight = 20.0F, pixelSize, xp, yp;
    int centerX, centerY, notches, left, right, top, bottom, mainArea_left, mainArea_right, mainArea_top, mainArea_bottom, grid_unit;
    int level = 1, score = 0, lines = 0;

    int M ; // scoring factor( ranger: 1-10)
    int N ; // number of rows required for each Level of difficulty( range: 20-50).
    double S ; // speed factor( range: 0.1-1.0).

    boolean pause = false, isRunnig = true;
    int main_board_width, main_board_height;
    Point2D[][] board;
    public GameEngine game;

    CvTetris(GameEngine g, GameSetting setting){
        game = g;
        game.initiate();
        M = setting.M;
        N = setting.N;
        S = setting.S;
        main_board_width = setting.Width;
        main_board_height = setting.Height;

        addMouseMotionListener( new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent evt) {
                if( isRunnig ) {
                    xp = fx(evt.getX());
                    yp = fy(evt.getY());

                    if ( xp < rWidth/8 && xp > -rWidth/2 && yp < rHeight/2 && yp > -rHeight/2) {
                        if (pause != true) {
                            repaint();
                            pause = true;
                        }
                    }
                    else {
                        if( pause != false) {
                            repaint();
                            pause = false;
                        }
                    }
                }
            }
        });

        // exit when press exit

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                    xp = fx(e.getX());
                    yp = fy(e.getY());

                    if( e.getX() < mainArea_left + grid_unit * main_board_width && e.getY() < mainArea_top + grid_unit * main_board_height ){
                        if( isInsideShape(e.getX(), e.getY()) ) {
                            g.replaceShape();
                            repaint();
                        }
                    }
                    else {
                        if( e.getButton() == MouseEvent.BUTTON1) {
                            game.moveLeft();
                        }
                        if( e.getButton() == MouseEvent.BUTTON3) {
                            game.moveRight();
                        }
                        if( e.getButton() == MouseEvent.BUTTON2) {
                            game.toBottom();
                        }

                        if(xp > rWidth * 5 / 32 && xp < rWidth *5/32 + 1.5F && yp < -rHeight/2 + 1.0F && yp > -rHeight/2 ) {
                            System.exit(0);
                        }
                    }

                if( isRunnig ) {
                    repaint();
                }
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

                    notches = e.getWheelRotation();
                    if( notches > 0 ) {
                        for( int i = 0; i < notches; i++ ) {
                            System.out.println("CW");
                            game.rotateCW();
                        }
                    }
                    else {
                        for(int i = 0; i < -notches; i++) {
                            System.out.println("CCW");
                            game.rotateCCW();
                        }
                    }

                if( isRunnig ) {
                    repaint();
                }
            }
        });
    }


   /* “Point-Inside-Polygon”	test	algorithm
    * @param point coordinate in canvas: x_p, y_p
    * for each cell of the active shapes
    * if y_p is in between the top and bottom of the cell
    *    if x_p is less than left side of the cell
    *      crossing increase by two
    *    else if x_p is less than right side of the cell
    *      crossing increase by one
    *  return true if the reminder of crossing%2
    *  else return false
    */
    boolean isInsideShape(int x_p, int y_p) {
        Component c = new Component(game.active_component);
        int crossing = 0;

        for( int i = 0; i < game.active_cells.length; i++ ) {
            int X_left = mainArea_left + (c.x + game.active_cells[i].x) * grid_unit;
            int X_right = X_left + grid_unit;
            int Y_top = mainArea_top + (c.y + game.active_cells[i].y) * grid_unit;
            int Y_bottom = Y_top + grid_unit;

            if( x_p <= X_right && y_p <= Y_bottom && y_p >= Y_top) {
                if( x_p >= X_left ){
                    crossing++;
                } else{
                    crossing += 2;
                }
            }
        }

        if( crossing%2 == 1 ) {
            return true;
        }
        return false;
    }

    void initgr() {
        Dimension d = getSize();
        int maxX = d.width - 1, maxY = d.height - 1;
        pixelSize = Math.max(rWidth/maxX, rHeight/maxY);
        centerX = maxX/2; centerY = maxY/2;

        left = iX(-rWidth/2);
        right = iX(rWidth/2);
        bottom = iY(-rHeight/2);
        top = iY(rHeight/2);

        mainArea_left = left;
        mainArea_right = iX(rWidth/8);
        mainArea_bottom = bottom;
        mainArea_top = top;

        int w_unit = (mainArea_right - mainArea_left)/main_board_width;
        int h_unit = (-mainArea_top + mainArea_bottom)/main_board_height;
        grid_unit = ( w_unit > h_unit )? h_unit: w_unit;

        board = new Point2D[main_board_width][main_board_height];

        for( int w = 0; w < main_board_width; w++ ) {
            for( int h = 0; h < main_board_height; h++ ) {
                int x = mainArea_left + w * grid_unit;
                int y = mainArea_top + h* grid_unit;
                board[w][h] = new Point2D(x, y);
            }
        }
    }

    void move_down() {
        if( !pause && isRunnig) {
            game.moveDown();
            repaint();
            game.checkLine();
        }
    }

    double getSpeed(){
        return 1 + level * S;
    }

    int getScore() {
        if( game.points != lines ) {
            score += level * M;
            lines = game.points;
        }
        return score;
    }

    int getLevel() {
        if (( game.points - level * N ) >= 0) {
            level++;
        }
        return level;
    }

    boolean running() {
        if( !game.isRunning ){
            isRunnig = false;
        }
        return game.isRunning;
    }

    int iX(float x){return Math.round(centerX + x/pixelSize);}
    int iY(float y){return Math.round(centerY - y/pixelSize);}
    float fx(int x){return (x - centerX) * pixelSize;}
    float fy(int y){return (centerY - y) * pixelSize;}
    int ftoI(float i) {return Math.round(i/pixelSize);}
    float Itof(int i) {return i*pixelSize;}

    public void paint(Graphics g) {
        initgr();

        int secArea_right = right, secArea_left = iX(rWidth * 3 / 16),
            secArea_bottom = iY(rHeight * 3/10), secArea_top = top,
            secArea_width = secArea_right - secArea_left,
            secArea_height = secArea_bottom - secArea_top;

//        g.drawRect(mainArea_left, mainArea_top, mainArea_right - mainArea_left, mainArea_bottom - mainArea_top);
        g.drawRect(mainArea_left, mainArea_top, grid_unit * main_board_width, grid_unit * main_board_height);

        // draw font
        g.setFont(new Font("TimesRoman", Font.BOLD, 24));
        g.setColor(Color.black);
        g.drawString("Lines:  " + Integer.toString(game.points), secArea_left, iY(0));
        g.drawString("Level:  " + Integer.toString(getLevel()),  secArea_left, iY(rHeight / 8));
        g.drawString("Score:  " + Integer.toString(getScore()), secArea_left, iY(-rHeight / 8));

        // draw pause
        if( pause == true ) {
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.setColor(Color.black);
            g.drawString("PAUSE", iX(-rWidth / 4), iY(0));
            g.drawRect(iX(-rWidth * 9 / 32), iY(rHeight * 3/ 80), ftoI(2.5F), ftoI(1.0F));
        }

        // exit
        g.setFont(new Font("TimesRoman", Font.BOLD, 24));
        g.setColor(Color.black);
        g.drawString("Exit", secArea_left, iY(-rHeight/2 + 0.5F));
        g.drawRect(iX(rWidth * 5 / 32), iY(-rHeight/2 + 1.0F), ftoI(1.5F), ftoI(1.0F));

        // draw main area
        for( int w = 0; w < main_board_width; w ++){
            for( int h = 0; h < main_board_height; h++) {
                float x = board[w][h].x;
                float y = board[w][h].y;
                int X = iX(x), Y = iY(y), unit = grid_unit;
                Color cell_Color = game.board[w][h];

                if( cell_Color != Color.white) {
                    //g.drawRect(X, Y, unit, unit);
                    g.setColor(cell_Color);
                    g.fillRect(mainArea_left + unit * w, mainArea_top + unit * h, unit - 1, unit - 1);
                  //  g.fillRect(board[w][h].w + 1, board[w][h].h + 1, grid_unit - 1, grid_unit - 1);
                }
            }
        }

        // draw next area
        g.drawRect(secArea_left, secArea_top, secArea_width, secArea_height);
        int unit_sec_width = secArea_width / 5;
        int unit_sec_height = secArea_height / 5;

        for ( int w = 0; w < 5; w++) {
            for( int h = 0; h < 3; h++) {
                Color cell_Color = game.next_board[w][h];
                int cell_left = secArea_left + w * unit_sec_width;
                int cell_top = secArea_top + h * unit_sec_height;
                if( cell_Color != Color.white) {
                    g.setColor(cell_Color);
                    g.fillRect(cell_left, cell_top, unit_sec_width + 1, unit_sec_height + 1);
                }
            }
        }
    }
}

class Point2D
{
    float x, y;
    int w, h;
    Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    Point2D(int w, int h) {
        this.w = w;
        this.h = h;
    }
}

