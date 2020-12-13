import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node {
	Node parent;
	List<Node> children;
	int move;
	int numVisits;
	private double value;
	int player;
	Random random;

	public Node(Node parent, int move, int player) {
		this.parent = parent;
		this.move = move;
		this.children = new ArrayList<Node>();
		this.numVisits = 0;
		this.value = 0;
		this.player = player;
		this.random = new Random();
	}

	/**
	 * Get the best child of the node
	 * @param n how many MCTS rounds have been done so far
	 * @param temp the exploration factor to use (0 means that we will not explore and only exploit)
	 * @return the best child
	 */
	public Node getBestChild(int n, double temp) {
		// if no children, return null
		if (this.children.size() == 0) return null;

		// set the default best value to min value
		double bestValue = Double.MIN_VALUE;
		// set the default best child to the first child
		Node bestNode = this.children.get(0);

		// loop over children
		for (Node child : children) {

			// get the child's UCB val
			double val = child.getUCB(n, temp);

			if (bestValue == val) {
				// if it's a tie, randomly choose which child to use as the best
				if (random.nextInt() % 2 == 0) {
					bestNode = child;
				}
			} else if (bestValue < val) {
				// the child has a better value, so we will use it's score and set it as the bestNode
				bestValue = val;
				bestNode = child;
			}
		}
		return bestNode;
	}

	/**
	 * update the value of this node
	 * 		if winner == this node's player:
	 * 			value is increased by 1
	 * 		else if DRAW:
	 * 			value is increased by 0
	 *
	 * we need to increase this node's value for losses otherwise it will treat
	 * draws the same as loses and will favor them equally
	 * @param winner who the winner is
	 */
	public void updateValue(int winner) {
		// if this is a winner for my node type, increment my value
		if (winner == this.player) {
			this.value++;
		} else if (winner == 0) {
			this.value += .5;
		}
	}

	/**

	 * v = value of node
	 * ni = number of times THIS node has been visited
	 * N = how many rounds have been done in the MCTS so far
	 * T = temperature
	 * forumla:
	 * (v/ni) + temp * sqrt( ln(N) / ni)
	 * @param n how simulations have been done so far
	 * @param temp exploration param, the higher it is the more likely we are to explore
	 * @return the Upper confidence bound of the node
	 */
	private double getUCB(double n, double temp) {
		if (this.numVisits == 0 && temp == 0) {
			return Double.MAX_VALUE;
		}
		return (value / (double)numVisits) + temp * Math.sqrt(Math.log(n) / numVisits);
	}
}
