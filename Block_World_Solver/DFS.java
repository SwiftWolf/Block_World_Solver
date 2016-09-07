import java.awt.Point;
import java.util.HashSet;
import java.util.Stack;

public class DFS {
	// Set the size of the board, size x size
	public int size = 4;
	// Set the start and goals points for the blocks
	// in relation to the size of the board
	private Point aStart = new Point(size - 1, 0);
	private Point bStart = new Point(size - 1, 1);
	private Point cStart = new Point(size - 1, 2);
	
	private Point aGoal = new Point(size - 3, 1);
	private Point bGoal = new Point(size - 2, 1);
	private Point cGoal = new Point(size - 1, 1);

	public static void main(String[] args) {
		new DFS().dfs();
	}

	public void dfs() {
		// tracks the number of nodes expanded
		int moves = 0;
		// Stores the next state to explore
		Stack<Board> next = new Stack<Board>();
		// Stores the previously visited states
		HashSet<String> visited = new HashSet<String>();
		// Tracks the time it takes to run the search
		long startTime = System.currentTimeMillis();
		// Adds the start state of the board to the stack
		next.add(new Board(size, aStart, bStart, cStart, aGoal, bGoal, cGoal));
		
		// While the stack is not empty
		while (!next.isEmpty()) {
			// Increase the nodes expanded by 1
			moves++;
			// Remove the top board state from the stack
			Board board = next.pop();	
			// If the current board state is the goal state
			if (board.checkBlocks() == true) {
				// Stop timing the search
				long timeTaken = System.currentTimeMillis() - startTime;
				// Display the state of the board when the goal is found
				board.showBoard();
				System.out.println("");
				System.out.println("Depth reached: " + board.depth);
				System.out.println("Nodes expanded: " + moves);
				System.out.println("Time taken (ms): " + timeTaken);
				return;
			}
			// If the goal is not found, expand the nodes
			// Toggle the true/false for to randomise the node expansion
			for (Board b : board.expand(false)) {
				if (b != null) {
					// Checks if we have not already visited the state
					if (!visited.contains(b.toString()))
						// Adds the next possible moves to the stack
						next.push(b);
				}
			}
			// Adds the current board state to the visited states
			visited.add(board.toString());
		}
	}
}