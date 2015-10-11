

public class Board {
    private int[][] blocks;
    private int n;
    private int manhattan = -1; //cache
    private int iblank;
    private int jblank;
  //  private boolean isgoal = true; //cache

    public Board(int[][] blocks)           // construct a board from an N-by-N array of blocks
    {                                      // (where blocks[i][j] = block in row i, column j)

      iblank = -1;
      jblank = -1;
      this.n = blocks.length;
      this.blocks = new int[n][n];
      for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
          {
        //    if (blocks[i][j]!= 0 && blocks[i][j] != i*n + j + 1) isgoal = false;

            this.blocks[i][j] = blocks[i][j];
            if (blocks[i][j] == 0) {
              iblank = i;
              jblank = j;
            }
          }
    }

    public int dimension()                 // board dimension N
    {
      return n;
    }
    public int hamming()                   // number of blocks out of place
    {
      // correct value at (i, j) is i * n + j + 1
      int priority = 0;
      for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
          {
            if (i == n-1 && j == n-1) break;    // don't include the bottom right blank square
            if (blocks[i][j] != i*n + j + 1) priority++;
          }
      return priority;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
      if (manhattan == -1) {
        manhattan = 0;
        for (int i = 0; i < n; i++)
          for (int j = 0; j < n; j++)
            {
              if (blocks[i][j] != 0)
              {
                int block = blocks[i][j]-1;
                int igoal = block / n;
                int jgoal = block-(igoal*n);
                manhattan += Math.abs(i-igoal) + Math.abs(j-jgoal);
              }
            }

      }

      return manhattan;
    }



    public boolean isGoal()                // is this board the goal board?
    {
      return manhattan() == 0;
    }

    public Board twin()                    // a board that is obtained by exchanging two adjacent blocks in the same row
    {

      Board twinboard = new Board(blocks);
      int i = (iblank+1)%n ;
      swap(twinboard.blocks, i, 0, i, 1);
      return twinboard;

    }



    public boolean equals(Object y)        // does this board equal y?
    {
      if (y == this) return true;

      if (y == null) return false;

      if (y.getClass() != this.getClass()) return false;
      Board that = (Board) y;


      return this.n == that.n && tilesequal(this.blocks, that.blocks);
    }

    private boolean tilesequal(int[][] first, int[][] second)
    {
      for (int i = 0; i < n; i++)
        for (int j = 0 ; j < n; j++)
          {
            if (first[i][j] != second[i][j])
              return false;
          }
      return true;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
       Queue<Board> neighbors = new Queue<Board>();
       int i = iblank, j = jblank;

       if (i > 0) // can go left
       {
         swap(blocks, i, j, i-1, j);
         Board newboard = new Board(blocks);
         neighbors.enqueue(newboard);
         swap(blocks, i, j, i-1, j); //revert to original
       }

       if (j > 0) // can go up
       {
         swap(blocks, i, j, i, j-1);
         Board newboard = new Board(blocks);
         neighbors.enqueue(newboard);
         swap(blocks, i, j, i, j-1); //revert to original
       }

       if (i < n-1) // can go right
       {
         swap(blocks, i, j, i+1, j);
         Board newboard = new Board(blocks);
         neighbors.enqueue(newboard);
         swap(blocks, i, j, i+1, j); //revert to original
       }

       if (j < n-1) // can go up
       {
         swap(blocks, i, j, i, j+1);
         Board newboard = new Board(blocks);
         neighbors.enqueue(newboard);
         swap(blocks, i, j, i, j+1); //revert to original
       }

       return neighbors;
    }

    private void swap(int[][] board, int i0, int j0, int i1, int j1)
    {
      int tmp = board[i0][j0];
      board[i0][j0] = board[i1][j1];
      board[i1][j1] = tmp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }


    public static void main(String[] args) // unit tests (not graded)
    {
      int[][] blocks = new int[3][3];
      int val = 1;
      for (int row = 0; row < 3; row++)
        for (int col = 0; col < 3; col++)
          blocks[row][col] = val++;
      blocks[2][2] = 0;

      Board b = new Board(blocks);
      Iterable<Board> newboards = b.neighbors();
      for (Board bo : newboards)
          StdOut.println(bo.toString());
      StdOut.println(b.dimension());
      StdOut.println(b.hamming());
      StdOut.println(b.manhattan());

    }
}
