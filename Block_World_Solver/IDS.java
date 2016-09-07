import java.awt.Point;
import java.util.Stack;

public class IDS {
	// Set the size of the board, size x size
	public int size = 4; // Doesn't work for size 5 or greater

	// Set the start and goals points for the blocks
	// in relation to the size of the board
	private Point aStart = new Point(size - 1, 0);
	private Point bStart = new Point(size - 1, 1);
	private Point cStart = new Point(size - 1, 2);

	private Point aGoal = new Point(size - 3, 1);
	private Point bGoal = new Point(size - 2, 1);
	private Point cGoal = new Point(size - 1, 1);
	
	public static void main(String[] args) {
		new IDS().ids();
	}

	// Feeds the depth into a DFS search
	public void ids() {
		int n = 0;
		while (true) {
			n++;
			// Stops the search when the solution is found
			if (dfs(n))
				return;
			System.out.println("Current depth: " + n);
		}
	}

	public boolean dfs(int limit) {
		int moves = 0;

		Stack<Board> next = new Stack<Board>();

		long startTime = System.currentTimeMillis();
		
		next.add(new Board(size, aStart, bStart, cStart, aGoal, bGoal, cGoal));

		while (!next.isEmpty()) {
			moves++;
			Board board = next.pop();

			if (board.depth > limit)
				continue;

			if (board.checkBlocks() == true) {
				long timeTaken = System.currentTimeMillis() - startTime;
				board.showBoard();
				System.out.println("");
				System.out.println("Level reached: " + board.depth);
				System.out.println("Moves taken: " + moves);
				System.out.println("Time taken (ms): " + timeTaken);
				return true;
			}

			for (Board b : board.expand(false)) {
				if (b != null) {
					next.push(b);
				}
			}
		}
		return false;
	}
}
