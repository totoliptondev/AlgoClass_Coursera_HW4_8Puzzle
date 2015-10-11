
public class Solver {
    private final Stack<Board> solution;
    private boolean isSolvable;
    private int move;

    private class SearchNode implements Comparable<SearchNode> {
      private final Board board;
      private final int move;
      private final SearchNode previous;
      private int manhattan;

      SearchNode(Board board, SearchNode previous)
      {
        this.board = board;
        this.move = (previous == null) ? 0 : previous.move + 1;
        this.previous = previous;
      }

      private int priority()
      {
        if (previous == null)
          manhattan = board.manhattan();

        else {
          manhattan = board.manhattan();


          //StdOut.println(manhattan + " and  " +board.manhattan());
        }
        return move + manhattan;
      }

      public int compareTo(SearchNode that)
      {
        if      (this.priority() > that.priority())  return 1;
        else if (this.priority() < that.priority())  return -1;
        else if (this.move       < that.move      )  return 1;
        else if (this.move       > that.move      )  return -1;
        else                                         return 0;
      }
    }

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
      solution = new Stack<Board>();
      if (initial.isGoal())
      {
        isSolvable = true;
        this.solution.push(initial);
        return;
      }

      if (initial.twin().isGoal())
      {
        isSolvable = false;
        return;
      }

      MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
      MinPQ<SearchNode> pqtwin = new MinPQ<SearchNode>();
      MinPQ<SearchNode> tmppq = pq;

      SearchNode node = new SearchNode(initial,  null);
      SearchNode nodetwin = new SearchNode(initial.twin(), null);

      pq.insert(node);
      pqtwin.insert(nodetwin);
      while (true)
      {
        node = tmppq.delMin();
        Board board = node.board;
        //StdOut.print(board.toString());

        if (board.isGoal())
        {
          isSolvable = (tmppq == pq);
          if (isSolvable)
          {
            solution.push(board);
            while (node.previous != null)
            {
              node = node.previous;
              solution.push(node.board);
            }
          }
          return;
        }

        for (Board neighbor : board.neighbors())
        {

          if (node.previous != null && neighbor.equals(node.previous.board))
            continue;  // seen this state already
          tmppq.insert(new SearchNode(neighbor, node));

        }

        tmppq = (tmppq == pq) ? pqtwin : pq;  // toggle between the two priority queues
      }


    }

    public boolean isSolvable()            // is the initial board solvable?
    {
      return isSolvable;
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
      return solution.size()-1;
    }
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {

      return isSolvable? solution : null;
    }
    public static void main(String[] args) {

      // create initial board from file
      In in = new In(args[0]);
      int N = in.readInt();
      int[][] blocks = new int[N][N];
      for (int i = 0; i < N; i++)
          for (int j = 0; j < N; j++)
              blocks[i][j] = in.readInt();
      Board initial = new Board(blocks);

      // solve the puzzle
      Solver solver = new Solver(initial);

      // print solution to standard output
      if (!solver.isSolvable())
          StdOut.println("No solution possible");
      else
      {
          StdOut.println("Minimum number of moves = " + solver.moves());
          for (Board board : solver.solution())
              StdOut.println(board);
      }
    }
}
