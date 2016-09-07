import java.awt.Point;
import java.util.*;

public class AltBoard {

	// Numerical way to refer to the direction to move the agent
	public static int UP = 0;
	public static int DOWN = 1;
	public static int LEFT = 2;
	public static int RIGHT = 3;

	// The board dimensions Size x Size
	public int size = 0;

	// The depth of the current state 
	public int depth = 0;

	// The 'container' for the cells
	public char board[][] = null;

	// The agents start (and end) positions
	public int agentX;
	public int agentY;

	// The start and goals points for the blocks
	public Point aStart;
	public Point bStart;
	public Point cStart;
	public Point aGoal;
	public Point bGoal;
	public Point cGoal;

	// Keeps track of the moves from the start state to goal state 
	public List<Integer> madeMoves = new LinkedList<Integer>();

	// Initialise and populate the board with set size  
	public AltBoard(int size, Point aStart, Point bStart, Point cStart,
			Point aGoal, Point bGoal, Point cGoal) {
		this.size = size;
		this.aStart = aStart;
		this.bStart = bStart;
		this.cStart = cStart;
		this.aGoal = aGoal;
		this.bGoal = bGoal;
		this.cGoal = cGoal;

		// Agent starts in the bottom rightmost position 
		this.agentX = size - 1;
		this.agentY = size - 1;

		// Populates the board with empty cells
		board = new char[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = '-';
			}
		}
		// Adds the agent and blocks to the board
		board[agentX][agentY] = '*';
		board[aStart.x][aStart.y] = 'A';
		board[bStart.x][bStart.y] = 'B';
		board[cStart.x][cStart.y] = 'C';
	}

	// Manhattan Distance heuristic for A* and AltA*
	public int heuristic() {
		// Takes the current depth and adds the Manhattan Distance
		// for the current blocks to their goal positions
		int h = depth;
		for (int i = 0; i < size; i++) 
			for (int j = 0; j < size; j++) 
				if ('A' == board[i][j])	
					h +=  Math.abs(aGoal.x - i) + Math.abs(aGoal.y - j);
				else if ('B' == board[i][j])
					h +=  Math.abs(bGoal.x - i) + Math.abs(bGoal.y - j);
				else if ('C' == board[i][j])
					h +=  Math.abs(cGoal.x - i) + Math.abs(cGoal.y - j);
		return h;
	}

	// Prints the current state of the board
	public void showBoard() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	// Prints the path from the start to the goal state after the search has completed
	public void printMoves(AltBoard b) {
		for(Integer i : madeMoves) {
			b.move(i);
			b.showBoard();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}

	// Checks if the blocks are in the goal state
	public boolean checkBlocks() { 
		// '*' can be removed from this to relax the goal state 
		if ('*' == board[size - 1][size - 1])
			if ('A' == board[aGoal.x][aGoal.y])
				if ('B' == board[bGoal.x][bGoal.y])
					if ('C' == board[cGoal.x][cGoal.y])
						return true;
		return false;
	}

	// Converts the current board state to a string
	public String toString() {
		String string;
		string = "";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				string = string + board[i][j];
			}
		}
		return string;
	}
	
	// Returns a list of all the possible next moves 
	public List<AltBoard> expand(boolean random) {
		List<AltBoard> expanded = new ArrayList<AltBoard>();

		expanded.add(this.doMove(UP));
		expanded.add(this.doMove(DOWN));
		expanded.add(this.doMove(LEFT));
		expanded.add(this.doMove(RIGHT));

		// To randomise the node expansion for DFS
		if (random)
			Collections.shuffle(expanded);

		return expanded;
	}
	
	// Copies the state of the board
	public AltBoard copyState() {
		AltBoard copyBoard = new AltBoard(this.size, this.aStart, this.bStart,
				this.cStart, this.aGoal, this.bGoal, this.cGoal);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				copyBoard.board[i][j] = board[i][j];
			}
		}

		copyBoard.agentX = this.agentX;
		copyBoard.agentY = this.agentY;
		copyBoard.depth = this.depth;

		for (Integer i : this.madeMoves)
			copyBoard.madeMoves.add(i);

		return copyBoard;
	}
	
	// Performs a move on the agent and puts it on a new board
	public AltBoard doMove(int i) {
		AltBoard newBoard = null;
		// New board for the move take place on
		newBoard = this.copyState();
		// Adds the move to the new board 
		if (newBoard.move(i))
			return newBoard;
		else
			return null;
	}
	
	// Moves the agent, and allows it to loops off the board
	// and on to the opposite boundary
	public boolean move(int i) {
		int destX, destY;
		switch (i) {
		case 0: // Move agent up
			if (agentX == 0) {
				destX = size - 1;
				destY = agentY;
				char temp = board[destX][destY];
				board[destX][destY] = board[agentX][agentY];
				board[agentX][agentY] = temp;
				agentX = destX;
				agentY = destY;
				madeMoves.add(i);
				depth++;
				return true;
			} else {
				destX = agentX - 1;
				destY = agentY;
				char temp = board[destX][destY];
				board[destX][destY] = board[agentX][agentY];
				board[agentX][agentY] = temp;
				agentX = destX;
				agentY = destY;
				madeMoves.add(i);
				depth++;
				return true;
			}

		case 1: // Move agent down
			if (agentX == size - 1) {
				destX = 0;
				destY = agentY;
				char temp = board[destX][destY];
				board[destX][destY] = board[agentX][agentY];
				board[agentX][agentY] = temp;
				agentX = destX;
				agentY = destY;
				madeMoves.add(i);
				depth++;
				return true;
			} else {
				destX = agentX + 1;
				destY = agentY;
				char temp = board[destX][destY];
				board[destX][destY] = board[agentX][agentY];
				board[agentX][agentY] = temp;
				agentX = destX;
				agentY = destY;
				madeMoves.add(i);
				depth++;
				return true;
			}

		case 2: // Move agent left
			if (agentY == 0) {
				destX = agentX;
				destY = size - 1;
				char temp = board[destX][destY];
				board[destX][destY] = board[agentX][agentY];
				board[agentX][agentY] = temp;
				agentX = destX;
				agentY = destY;
				madeMoves.add(i);
				depth++;
				return true;
			} else {
				destX = agentX;
				destY = agentY - 1;
				char temp = board[destX][destY];
				board[destX][destY] = board[agentX][agentY];
				board[agentX][agentY] = temp;
				agentX = destX;
				agentY = destY;
				madeMoves.add(i);
				depth++;
				return true;
			}

		case 3: // Move agent right
			if (agentY == size - 1){
				destX = agentX;
				destY = 0;
				char temp = board[destX][destY];
				board[destX][destY] = board[agentX][agentY];
				board[agentX][agentY] = temp;
				agentX = destX;
				agentY = destY;
				madeMoves.add(i);
				depth++;
				return true;
			} else {
				destX = agentX;
				destY = agentY + 1;
				char temp = board[destX][destY];
				board[destX][destY] = board[agentX][agentY];
				board[agentX][agentY] = temp;
				agentX = destX;
				agentY = destY;
				madeMoves.add(i);
				depth++;
				return true;
			}
		}
		return false;
	}
}