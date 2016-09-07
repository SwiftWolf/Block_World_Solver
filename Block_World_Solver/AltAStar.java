import java.awt.Point;
import java.util.*;

public class AltAStar {
	// Set the size of the board, size x size
	private int size = 5;
	// Set the start and goals points for the blocks
	// in relation to the size of the board
	private Point aStart = new Point(size - 1, 0);
	private Point bStart = new Point(size - 1, 1);
	private Point cStart = new Point(size - 1, 2);

	private Point aGoal = new Point(size - 3, 1);
	private Point bGoal = new Point(size - 2, 1);
	private Point cGoal = new Point(size - 1, 1);


	public static void main(String[] args) {
		new AltAStar().solve();
	}
	
	// Optional, randomise the start points of the blocks
	public void randomPoints() {
		Random position = new Random();
		Boolean noOverLap = true;

		while (noOverLap) {
			this.aStart.x = position.nextInt(size - 1);
			this.aStart.y = position.nextInt(size - 1);

			this.bStart.x = position.nextInt(size - 1);
			this.bStart.y = position.nextInt(size - 1);

			this.cStart.x = position.nextInt(size - 1);
			this.cStart.y = position.nextInt(size - 1);

			// Checks that the blocks aren't on top of each other
			if (((aStart.x == bStart.x) && (aStart.y == bStart.y))
					|| ((bStart.x == cStart.x) && (bStart.y == cStart.y))
					|| ((cStart.x == aStart.x) && (cStart.y == aStart.y))) {
				noOverLap = true;
			} else
				noOverLap = false;
		}
	}
	
	// Simple comparator for the Priority Queue
	public class AltBoardComparator implements Comparator<AltBoard> {
		@Override
		public int compare(AltBoard a, AltBoard b) {
			return a.heuristic() - b.heuristic();
		}
	}

	public void solve() {
		int moves = 0;
		Comparator<AltBoard> comparator = new AltBoardComparator();
		// Stores the next states to explore, ordered according to the heuristic in Board
		PriorityQueue<AltBoard> queue = new PriorityQueue<AltBoard>(100, comparator);
		// Stores the previously visited states
		HashSet<String> visited = new HashSet<String>();

		long startTime = System.currentTimeMillis();
		
		// Randomise the start points of the blocks
		randomPoints();
		// Adds the start state of the board to the PriorityQueue
		queue.add(new AltBoard(size, aStart, bStart, cStart, aGoal, bGoal, cGoal));

		// While the PriorityQueue is not empty
		while (!queue.isEmpty()) {
			// Increase the nodes expanded by 1
			moves++;
			// Remove the top board state from the PriorityQueue

			// Get the lowest priority state.
			AltBoard altBoard = queue.poll();

			// If the current board state is the goal state
			if (altBoard.checkBlocks() == true) {
				long timeTaken = System.currentTimeMillis() - startTime;
				
				// Display the optimal path found
				altBoard.printMoves(new AltBoard(size, aStart, bStart, cStart, aGoal, bGoal, cGoal));
				System.out.println("Level reached: " + altBoard.depth);
				System.out.println("Moves taken: " + moves);
				System.out.println("Time taken (ms): " + timeTaken);
				return;
			}
			
			// If the goal is not found, expand the nodes
			for (AltBoard b : altBoard.expand(false)) {
				if (b != null) {
					// Checks if we have not already visited the state
					if (!visited.contains(b.toString()))
						// Adds the next possible moves to the PriorityQueue
						queue.add(b);
				}
			}
			// Adds the current board state to the visited states
			visited.add(altBoard.toString());
		}
	}
}