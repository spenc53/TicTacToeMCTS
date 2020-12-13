import java.util.List;
import java.util.Scanner;

public class MCTS {
	Board board;
	Node root;

	private static double TEMP = 1.41;

	public MCTS() {
		this.board = new Board();
	}

	/**
	 * Gets the next move
	 * @return the board position of the next move
	 */
	public int getNextMove() {
		int p = board.currPlayer == Board.PLAYER_ONE ? Board.PLAYER_TWO : Board.PLAYER_ONE;
		root = new Node(null , -1, p);
		root.numVisits = 1;

		this.branch();

		Node best = root.getBestChild(1, 0);

		return best.move;
	}

	/**
	 * The actual MCTS algorithm
	 */
	public void branch() {
		// how many times to do a search
		// this can be increased to improve the estimation
		// or can be changed to allow x amount of time per move
		for (int i = 0; i < 100000; i++) {
			Node currNode = root; // start each round at the root node
			int originalPlayer = board.currPlayer; // save the original player

			// logic to find which node to search
			while (true) {
				// if the current node is the root (defined as -1 in the move), do not play it
				if (currNode.move != -1) {
					board.move(currNode.move); // updates the board with the appropriate move
				}

				// if this node has only been visited once, then it hasn't had it's children intialized
				if (currNode.numVisits == 1) {
					// populate the children with the moves that are possible from the current state
					List<Integer> moves = board.getPossibleMoves();
					int p = currNode.player == Board.PLAYER_ONE ? Board.PLAYER_TWO : Board.PLAYER_ONE; // need to have each node to know which player values to keep track of
					for (int move : moves) {
						currNode.children.add(new Node(currNode, move, p));
					}
				}

				// get the best child from the current node
				Node nextNode = currNode.getBestChild(i, TEMP);

				// need case for being at the bottom
				// if we are at the end of a game, we need to break out of the game
				if (nextNode == null) {
					break;
				}

				// if we are not at the end of a game, we set the currNode to search as the next node
				currNode = nextNode;

				// if the current node we are at is a leaf node (hasn't been visited yet), we break out of the while loop
				if (currNode.numVisits == 0) {
					break;
				}
			}

			// run random game and get the winner
			int winner = board.playRandomGame();

			// backprop the value up the tree
			while (currNode != null) {
				// important node: the update value function only updates the value if the player won or had a tie
				currNode.updateValue(winner);

				// increase the amount of visits for the selected move
				currNode.numVisits++;

				// undo all the moves made to the board
				if (currNode.move != -1) {
					board.clearSpace(currNode.move);
				}

				// continue doing this until we reach the root
				currNode = currNode.parent;
			}

			// reset the original board's player as it may not be correct as we simulated the entire game
			board.currPlayer = originalPlayer;
		}
	}


	private static Scanner scanner;

	/**
	 * USEFUL WEBSITE: https://medium.com/@quasimik/implementing-monte-carlo-tree-search-in-node-js-5f07595104df
	 *
	 * PLAY A GAME AGAINST A MCTS FOR TIC TAC TOE
	 *
	 * USER INPUT IS DEFINED AS:
	 * 1 | 2 | 3
	 * ---------
	 * 4 | 5 | 6
	 * ---------
	 * 7 | 8 | 9
	 *
	 * NOTE: The actual board is n-1 for the positions
	 * @param args no args required for this
	 */
	public static void main(String args[]) {

		MCTS computer = new MCTS();
		scanner = new Scanner(System.in);  // Create a Scanner object

		boolean humanPlayer = false;

		while (true) {
			int p1move = computer.getNextMove();
			computer.board.move(p1move);

			if (computer.board.isGameOver() || computer.board.getPossibleMoves().size() == 0) break;
			System.out.println(computer.board.toString());


			int p2move = -1;
			if (humanPlayer) {
				p2move = getUserMove(computer.board);
			} else {
				p2move = computer.getNextMove();
			}

			computer.board.move(p2move);

			if (computer.board.isGameOver() || computer.board.getPossibleMoves().size() == 0) break;
			System.out.println(computer.board.toString());
		}

		int whoWon = computer.board.getWinner();
		String winner = whoWon == 0 ? "NO ONE" : whoWon == Board.PLAYER_ONE ? "X" : "O";
		System.out.println(computer.board.toString());
		System.out.println("WINNER: " + winner);
	}

	/**
	 * Asks continues accepting input from the user until they choose a valid spot
	 * USER INPUT IS DEFINED AS:
	 * 1 | 2 | 3
	 * ---------
	 * 4 | 5 | 6
	 * ---------
	 * 7 | 8 | 9
	 *
	 * NOTE: The actual board is n-1 for the positions
	 * @param board the board the user can choose from
	 * @return the user's move
	 */
	public static int getUserMove(Board board) {
		int move = -1;
		while (!board.getPossibleMoves().contains(move)) {
			move = Integer.parseInt(scanner.nextLine());
			move--;
		}
		return move;
	}
}
