import java.util.List;
import java.util.Random;

public class OthelloMCTSPlayer extends OthelloPlayer {

	@Override
	public OthelloMove getMove(OthelloState state) {
		return monteCarloTreeSearch(state, 100000);
	}

	public OthelloMove monteCarloTreeSearch(OthelloState boardState, int iterations) {
		Node root = new Node(boardState);
		for (int i = 0; i < iterations; i++) {
			Node node = treePolicy(root);
			if (node != null) {
				Node node2 = defaultPolicy(node);
				double node2score = score(node2);
				backup(node, node2score);
			}
		}

		Node bestChild = bestChild(root);
		if (bestChild != null) {
			return bestChild.getActionConsequent();
		}
		// no possible moves, return null
		return null;
	}

	private Node bestChild(Node node) {
		OthelloState boardState = node.getBoardState();
		if (boardState.nextPlayerToMove == 0) {
			// return child with best average max score
			int bestNodeIndex = 0;
			double bestAvgScore = -5.0;
			List<Node> children = node.getChildren();

			// if we have not reached a terminal node
			if (!children.isEmpty()) {
				for (int i = 0; i < children.size(); i++) {
					double childAvgScore = children.get(i).calculateAverageScore();
					if (childAvgScore > bestAvgScore) {
						bestAvgScore = childAvgScore;
						bestNodeIndex = i;
					}
				}
				return children.get(bestNodeIndex);
			}

			// if there is terminal node, return null
			return null;

		} else {
			// return child with best average min score
			int bestNodeIndex = 0;
			double bestAvgScore = 0.0;
			List<Node> children = node.getChildren();

			// if we have not reached a terminal node
			if (!children.isEmpty()) {
				for (int i = 0; i < children.size(); i++) {
					double childAvgScore = children.get(i).calculateAverageScore();
					if (childAvgScore < bestAvgScore) {
						bestAvgScore = childAvgScore;
						bestNodeIndex = i;
					}
				}
				return children.get(bestNodeIndex);
			}
			// if there is terminal node, return null
			return null;
		}
	}

	private Node treePolicy(Node node) {
		List<OthelloMove> moves = node.getNodeMoves();
		// if node still has any moves that are not in the tree
		if (!moves.isEmpty()) {
			OthelloMove move = moves.remove(0);
			// generate child node
			OthelloState childState = node.getBoardState().applyMoveCloning(move);
			Node newNode = new Node(childState);
			newNode.setParentNode(node);
			newNode.setActionConsequent(move);
			node.addNodeToTree(newNode);
			return newNode;
		}
		// if node is not terminal, but all its children are in the tree
		else if (moves.isEmpty() && !node.getChildren().isEmpty()) {
			Node nodetmp;
			Random rnd = new Random();
			int r = rnd.nextInt(10);
			
			if (r < 9) {
				// 90% of the time
				nodetmp = bestChild(node);
			} else {
				// 10% of the time choose a random child node
				int r2 = rnd.nextInt(node.getChildren().size());
				nodetmp = node.getChildren().get(r2);
			}
			return treePolicy(nodetmp);
		}
		// else node is a terminal node then return node
		return node;
	}

	private Node defaultPolicy(Node node) {
		List<Node> children = node.getChildren();
		// if we have not reached a terminal node, pick a child node at random to go down the tree
		if (!children.isEmpty()) {
			Random rnd = new Random();
			int r = rnd.nextInt(children.size());
			Node nodetmp = children.get(r);
			return defaultPolicy(nodetmp);
		}
		return node;
	}
	
	private double score(Node node2){
		return node2.calculateAverageScore();
	}

	private void backup(Node node, double score) {
		node.increaseNumVisited();
		node.setAverageScore(score);
		if (node.getParentNode() != null) {
			backup(node.getParentNode(), score);
		}
	}
}
