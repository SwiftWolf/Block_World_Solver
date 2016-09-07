import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {
	// Set the size of the board, size x size
	public int size = 5;
	// Set the start and goals points for the blocks
	// in relation to the size of the board
	private Point aStart = new Point(size - 1, 0);
	private Point bStart = new Point(size - 1, 1);
	private Point cStart = new Point(size - 1, 2);

	private Point aGoal = new Point(size - 3, 1);
	private Point bGoal = new Point(size - 2, 1);
	private Point cGoal = new Point(size - 1, 1);

	public static void main(String[] args) {
		new BFS().bfs();
	}

	public void bfs() {
		// tracks the number of nodes expanded
		int moves = 0;
		
		// Stores the next state to explore
		Queue<Board> queue = new LinkedList<Board>();
		// Stores the previously visited states
		HashSet<String> visited = new HashSet<String>();
		// Tracks the time it takes to run the search
		long startTime = System.currentTimeMillis();
		// Adds the start state of the board to the queue
		queue.add(new Board(size, aStart, bStart, cStart, aGoal, bGoal, cGoal));
		
		// While the queue is not empty
		while (!queue.isEmpty()) {
			// Increase the nodes expanded by 1
			moves++;
			// Remove the top board state from the queue
			Board board = queue.remove();
		
			// If the current board state is the goal state
			if (board.checkBlocks() == true) {
				long timeTaken = System.currentTimeMillis() - startTime;
				// Display the optimal path found
				board.printMoves(new Board(size, aStart, bStart, cStart, aGoal,bGoal, cGoal));
	//			board.showBoard();
				System.out.println("");
				System.out.println("Depth reached: " + board.depth);
				System.out.println("Nodes expanded: " + moves);
				System.out.println("Time taken (ms): " + timeTaken);
				break;
			}
			// If the goal is not found, expand the nodes
			for (Board b : board.expand(false)) {
				if (b != null) {
					// Checks if we have not already visited the state
					if (!visited.contains(b.toString()))
						// Adds the next possible moves to the queue
						queue.add(b);
				}
			}
			// Adds the current board state to the visited states
			visited.add(board.toString());
		}
	}
}