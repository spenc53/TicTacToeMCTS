import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
	private static int BOARD_SIZE = 3;
	private int[][] board;
	int currPlayer;
	private Random random;

	public static int PLAYER_ONE = 1;
	public static int PLAYER_TWO = -1;

	public Board() {
		this.board = new int[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				this.board[i][j] = 0;
			}
		}
		this.currPlayer = PLAYER_ONE;
		this.random = new Random();
	}

	/**
	 * plays a random game, it does so by alternating players and playing randomly
	 * After someone wins or the game is a draw, it undos all the moves
	 * @return returns 1 for PLAYER_ONE, -1 for PLAYER_TWO, 0 for DRAW
	 */
	public int playRandomGame() {
		List<Integer> movesMade = new ArrayList<Integer>();
		List<Integer> possibleMoves = this.getPossibleMoves();
		while (possibleMoves.size() > 0 && !this.isGameOver()) {
			int move = possibleMoves.get(this.random.nextInt(possibleMoves.size()));
			this.move(move);
			possibleMoves = this.getPossibleMoves();
			movesMade.add(move);
		}

		for (int move : movesMade) {
			this.clearSpace(move);
		}

		return getWinner();
	}

	/**
	 * Get a list of empty spots on the board
	 * @return a list of integers of possible moves
	 */
	public List<Integer> getPossibleMoves() {
		List<Integer> possibleMoves = new ArrayList<Integer>();

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (this.board[i][j] == 0) {
					possibleMoves.add((i * 3) + j);
				}
			}
		}

		return possibleMoves;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (this.board[i][j] == 0) {
					sb.append('-');
				} else if(this.board[i][j] == 1)  {
					sb.append('X');
				} else if(this.board[i][j] == -1) {
					sb.append('O');
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * places the move for the current player and then updates whose turn it is
	 * Board is setup as
	 * 0 | 1 | 2
	 * ---------
	 * 4 | 5 | 6
	 * ---------
	 * 7 | 8 | 29
	 * @param move where to place the next move
	 */
	public void move(int move) {
		int i = move % 3;
		int j = move / 3;
		this.board[j][i] = currPlayer;

		this.currPlayer = currPlayer == PLAYER_ONE ? PLAYER_TWO : PLAYER_ONE;
	}

	/**
	 * clears the spot on the board
	 * Board is setup as
	 * 0 | 1 | 2
	 * ---------
	 * 4 | 5 | 6
	 * ---------
	 * 7 | 8 | 29
	 * @param move which spot on the board to clear
	 */
	public void clearSpace(int move) {
		int i = move % 3;
		int j = move / 3;
		this.board[j][i] = 0;
	}

	/**
	 * checks if the game is over
	 * @return true if game is over, otherwise false
	 */
	public boolean isGameOver(){
		return this.getPossibleMoves().size() == 0 || this.getWinner() != 0;
	}

	/**
	 * Determines if someone has won the game
	 * @return the value of the winner, 0 otherwise
	 */
	public int getWinner() {
		int countPlayerOne = 0;
		int countPlayerTwo = 0;

		// check cols
		for (int row = 0; row < BOARD_SIZE; row++) {
			countPlayerOne = 0;
			countPlayerTwo = 0;
			for (int col = 0; col < BOARD_SIZE; col++) {
				if(board[row][col] == PLAYER_TWO) countPlayerTwo++;
				else if(board[row][col] == PLAYER_ONE)countPlayerOne++;
			}
			if(countPlayerTwo == BOARD_SIZE) return PLAYER_TWO;
			if(countPlayerOne == BOARD_SIZE) return PLAYER_ONE;
		}

		// check row
		for (int col = 0; col < BOARD_SIZE; col++) {
			countPlayerOne = 0;
			countPlayerTwo = 0;
			for (int row = 0; row < BOARD_SIZE; row++) {
				if(board[row][col] == PLAYER_TWO) countPlayerTwo++;
				else if(board[row][col] == PLAYER_ONE)countPlayerOne++;
			}
			if(countPlayerTwo == BOARD_SIZE) return PLAYER_TWO;
			if(countPlayerOne == BOARD_SIZE) return PLAYER_ONE;
		}

		countPlayerOne = 0;
		countPlayerTwo = 0;

		//check diagonal right
		for (int i = 0, j= 0; i <BOARD_SIZE ; i++, j++) {
			if (board[i][j] == PLAYER_TWO) countPlayerTwo++;
			else if(board[i][j] == PLAYER_ONE) countPlayerOne++;
		}
		if(countPlayerTwo == BOARD_SIZE) return PLAYER_TWO;
		if(countPlayerOne == BOARD_SIZE) return PLAYER_ONE;

		countPlayerOne = 0;
		countPlayerTwo = 0;
		//check diagonal left
		for (int row = 0, col = BOARD_SIZE-1; row < BOARD_SIZE; row++, col--) {
			if (board[row][col] == PLAYER_TWO) countPlayerTwo++;
			else if(board[row][col] == PLAYER_ONE) countPlayerOne++;
		}
		if(countPlayerTwo == BOARD_SIZE) return PLAYER_TWO;
		if(countPlayerOne == BOARD_SIZE) return PLAYER_ONE;

		return 0;
	}
}
