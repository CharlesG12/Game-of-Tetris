import java.util.LinkedList;
import java.util.Random;
import java.awt.Color;

public class GameEngine {
    private int width, height;
    private int xdiff = 0, ydiff= 0;
    public int points = 0;
    public Color[][] board;
    private Color[][] activeBoard;
    private Color[][] fixedBoard;
    public Color[][] next_board = new Color[5][3];
    private Component next_component;
    public Component active_component;
    public boolean isRunning = true;
    public Coordinate[] active_cells;
    private LinkedList<Integer> extraShapes;

    private Color[] colors = {Color.black, Color.red, Color.blue, Color.yellow, Color.green, Color.pink};

    public Coordinate[][][] shapes = {
            { // L
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(1, -1)},
                    {new Coordinate(0,0), new Coordinate(0, -1), new Coordinate(0, 1), new Coordinate(1, 1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1,0), new Coordinate(-1,1)},
                    {new Coordinate(0,0), new Coordinate(0, 1), new Coordinate(0, -1), new Coordinate(-1, -1)},
            },

            { // mirror L
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(-1, -1)},
                    {new Coordinate(0,0), new Coordinate(0, -1), new Coordinate(0, 1), new Coordinate(1, -1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(1, 1)},
                    {new Coordinate(0,0), new Coordinate(0, -1), new Coordinate(0, 1), new Coordinate(-1, 1)},
            },

            { // square
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(-1, -1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(-1, -1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(-1, -1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(-1, -1)},
            },
            { // T
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(0, -1)},
                    {new Coordinate(0,0), new Coordinate(1, 0), new Coordinate(0, -1), new Coordinate(0, 1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(0, 1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(0, 1), new Coordinate(0, -1)},
            },
            { // S
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(0, -1), new Coordinate(1, -1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(0, 1), new Coordinate(-1, -1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(0, -1), new Coordinate(1, -1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(0, 1), new Coordinate(-1, -1)},
            },
            { // mirror S
                    {new Coordinate(0,0), new Coordinate(1, 0), new Coordinate(0, -1), new Coordinate(-1, -1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(-1, 1), new Coordinate(0, -1)},
                    {new Coordinate(0,0), new Coordinate(1, 0), new Coordinate(0, -1), new Coordinate(-1, -1)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(-1, 1), new Coordinate(0, -1)},
            },
            { // -
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(2, 0)},
                    {new Coordinate(0,0), new Coordinate(0, 1), new Coordinate(0, -1), new Coordinate(0, -2)},
                    {new Coordinate(0,0), new Coordinate(-1, 0), new Coordinate(1, 0), new Coordinate(2, 0)},
                    {new Coordinate(0,0), new Coordinate(0, 1), new Coordinate(0, -1), new Coordinate(0, -2)},
            },

            /*
            * new shapes
             */
            { //
                    {new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(0, -1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, -1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(0, 1), new Coordinate(0, 0)},
            },
            { //
                    {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(-1, -1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, -1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(1, 1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(-1, 1), new Coordinate(0, 0)},
            },
            { //
                    {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(-1, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, -1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(-1, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, -1), new Coordinate(0, 0)},
            },
            { //
                    {new Coordinate(0, 0), new Coordinate(1, -1), new Coordinate(0, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(0, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, -1), new Coordinate(0, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(0, 0), new Coordinate(0, 0)},
            },
            { //
                    {new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, 0)},
            },
            { //
                    {new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(-1, 1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(-1, -1), new Coordinate(-1, 1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, -1), new Coordinate(-1, -1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, -1), new Coordinate(1, 1), new Coordinate(0, 0)},
            },
            { //
                    {new Coordinate(0, 0), new Coordinate(0, 0), new Coordinate(0, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(0, 0), new Coordinate(0, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(0, 0), new Coordinate(0, 0), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(0, 0), new Coordinate(0, 0), new Coordinate(0, 0)},
            },
            { //
                    {new Coordinate(0, 0), new Coordinate(1, -1), new Coordinate(-1, 1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(-1, -1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, -1), new Coordinate(-1, 1), new Coordinate(0, 0)},
                    {new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(-1, -1), new Coordinate(0, 0)},
            },
    };

    GameEngine(int width, int height,  LinkedList<Integer> list){
        this.width = width;
        this.height = height;
        this.extraShapes = list;
    }

    GameEngine() {
        this.width = 9;
        this.height = 19;
    }

    public void initiate() {
        newPiece();
        fixedBoard = generateEmptyBoard();
        activeBoard = generateEmptyBoard();
        board = generateEmptyBoard();
        mergeBoards();
        newPiece();
        active_next_component();
    }

    public Color[][] generateEmptyBoard() {
        Color[][] newBoard = new Color[width][height];
        for( int i = 0; i < width; i++ ) {
            for( int j = 0; j < height; j++ ) {
                newBoard[i][j] = Color.white;
            }
        }
        return newBoard;
    }

    public Color[][] generateEmptyNextBoard() {
        Color[][] newBoard = new Color[5][3];
        for( int i = 0; i < 5; i++ ) {
            for( int j = 0; j < 3; j++ ) {
                newBoard[i][j] = Color.white;
            }
        }
        return newBoard;
    }


    public void active_next_component() {
        //newPiece();
        active_component = new Component(next_component);
        active_cells = shapes[active_component.shape][active_component.rotation];
        //active_component = new Component(next_component);
        newPiece();
    }

    // merge active board wth fixed board
    public void copyBoard(Color[][] subject, Color[][] object) {
        for( int i = 0; i < width; i++ ) {
            for( int j = 0; j < height; j++ ) {
                subject[i][j] = object[i][j];
            }
        }
    }

    /*
     * replace active_component with next_component,
     * generate new value for next_component.
     */
    public void replaceShape() {
        active_component.shape = next_component.shape;
        active_component.rotation = next_component.rotation;
        newPiece();

    }

    // move active component position on the board.
    public void update_active_board() {
        boolean illegalMove = false;
        int s = active_component.shape, r = active_component.rotation;

        // check if the new position out of bound
        Coordinate[] cells = shapes[s][r];
        for( int i = 0; i < cells.length; i++ ) {
            int newX = cells[i].x + active_component.x + xdiff;
            int newY = cells[i].y + active_component.y + ydiff;

            if( newX < 0 || newX >= width ) {
                illegalMove = true;
            }
            if( newY >= height) {
                System.out.println("reach bottom");

                illegalMove = true;

                mergeBoards();
                copyBoard(fixedBoard, board);

                active_next_component();
            }
        }
        System.out.println(illegalMove);

        // update the position if not out of bound
        if(!illegalMove) {
            activeBoard = generateEmptyBoard();
            for( int i = 0; i < cells.length; i++ ) {
                int newX = cells[i].x + active_component.x + xdiff;
                int newY = cells[i].y + active_component.y + ydiff;
                if( newY >= 0 && newY < height && newX >= 0 && newX <width ) {
                    activeBoard[newX][newY] = active_component.color;
                }
            }
            active_component.x += xdiff;
            active_component.y += ydiff;
        }
        else{
            System.out.println("Move out of bound!");
        }
        xdiff = 0;
        ydiff = 0;
    }


    /*
    * Merge active_component with the fixed components on the final board
    * Game terminates when it reached the top of the board
    */
    public boolean mergeBoards() {
        boolean legal = true;
        Color[][] newBoard = new Color[width][height];
        for( int j = 0; j < height; j++ ) {
            for( int i = 0; i < width; i++ ) {
                Color a = activeBoard[i][j];
                Color f = fixedBoard[i][j];

                if( a == f && a == Color.white) {
                    newBoard[i][j] = Color.white;
                }
                else if ( a != Color.white && f != Color.white ) {
                    return !legal;
                }
                else if ( a != Color.white ) {
                    newBoard[i][j] = a;
                }
                else {
                    newBoard[i][j] = f;
                }


                if( j == 0 && fixedBoard[i][0] != Color.white ) {
                    isRunning = false;
                }
            }
        }
        board = newBoard;

        return legal;
    }

    public void update_fixed_board(Component c) {
        Color[][] new_fixBoard = new Color[width][height];
        copyBoard(new_fixBoard, fixedBoard);

        int s = c.shape, r = c.rotation;
        Coordinate[] cells = shapes[s][r];
        for( int i = 0; i < cells.length; i++ ) {
            int X = cells[i].x;
            int Y = cells[i].y;
            if( X < width && X >= 0 && Y < height && Y >=0 ) {
                fixedBoard[X][Y] = c.color;
            }
        }
    }

    public void checkLine() {
        boolean line_empty = true;
        int n = 0;
        for( int y = height-1; y > 0; y-- ) {
            boolean line_full = true;
            for( int x = width-1; x > 0; x-- ) {
                if( fixedBoard[x][y] == Color.white ) {
                    line_full = false;
                }
                else {
                    line_empty = false;
                }
            }

            if( line_empty ) {
                points += 1.5 * n;
                return;
            }

            if( line_full ) {
                n++;
                for( int t = y; t > 0; t -- ) {
                    for( int x = width-1; x > 0; x--) {
                        if ( t - 1 >= 0 ) {
                            fixedBoard[x][t] = fixedBoard[x][t-1];
                        }
                        else {
                            fixedBoard[x][t] = Color.white;
                        }
                    }
                }
            }
        }
        points += 1.5 * n;
        return;
    }

    public void newPiece() {
        Random rand = new Random();
        int num_of_extra_shapes = extraShapes.size();

        int shape = rand.nextInt(7 + num_of_extra_shapes);
        if( shape >= 7 ) {
            int pos_in_list = shape - 7;
            shape = 7 + extraShapes.get(pos_in_list);
        }

        int cl = rand.nextInt(5);

        next_component = new Component(width/3, -1, shape, colors[cl], 0);
        update_next_board();
    }

    public void update_next_board() {
        int s = next_component.shape, r = next_component.rotation;

        next_board = generateEmptyNextBoard();
        Coordinate[] cells = shapes[s][r];
        for( int i = 0; i < cells.length; i++ ) {
            int X = cells[i].x + 2;
            int Y = cells[i].y + 2;
            next_board[X][Y] = next_component.color;
        }
    }

    public void rotateCCW() {
        int r = active_component.rotation;
        int rotation;
        r --;
        if ( r < 0 ) {
            rotation =  4 - Math.abs(r) % 4;
        }else {
            rotation = r % 4;
        }

        active_component.rotation = rotation;
        update_active_board();
        mergeBoards();
        System.out.println("CCW" + rotation);
    }

    public void rotateCW() {
        int r = active_component.rotation;
        r ++;
        int rotation = r % 4;
        active_component.rotation = rotation;
        update_active_board();
        mergeBoards();
        System.out.println("CW " + rotation);
    }

    public void moveDown() {
        ydiff = 1;
        update_active_board();
        if(mergeBoards() == false){
            System.out.println("move down is collision");

            mergeBoards();
            copyBoard(fixedBoard, board);

            active_next_component();
        }
    }

    public void toBottom() {
        moveDown();
        if( mergeBoards()){
            toBottom();
        }
    }

    public void moveLeft() {
        xdiff = -1;
        update_active_board();
        mergeBoards();
    }

    public void moveRight() {
        xdiff = 1;
        update_active_board();
        mergeBoards();
    }

}

//class Component {
//    int x, y, shape, rotation;
//    Color color;
//    Component(int x, int y, int shape, Color color, int r) {
//        this.x = x;
//        this.y = y;
//        this.rotation = r;
//        this.shape = shape;
//        this.color = color;
//    }
//
//    public Component(Component c) {
//        this(c.x, c.y, c.shape, c.color, c.rotation);
//    }
//}

//class Coordinate {
//    int x, y;
//    Coordinate( int x, int y) {
//        this.x = x;
//        this.y = y;
//    }
//}

