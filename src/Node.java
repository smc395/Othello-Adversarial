import java.util.ArrayList;
import java.util.List;

public class Node {

	private Node parentNode; // parent
	private List<OthelloMove> moves = new ArrayList<OthelloMove>();
	private List<Node> children = new ArrayList<Node>();
	private OthelloMove actionConsequent; // action that led to this state
	private int numVisited = 0; // number of times this node has been visited
	private double averageScore; // average score found for this node
	private OthelloState boardState;
	private int score = 0;

	public Node(OthelloState s) {
		boardState = s;
		score = s.score();
		generateMoves();
	}

	// create list of children moves
	public void generateMoves() {
		List<OthelloMove> moves = boardState.generateMoves();
		if (!moves.isEmpty()) {
			this.moves = moves;
		}
	}

	public void increaseNumVisited() {
		numVisited++;
	}
	
	public void addNodeToTree(Node node){
		children.add(node);
	}
	
	public double calculateAverageScore(){
		int scoreSum = 0;
		if (children.size() == 0){
			return score;
		}
		for(Node n : children){
			scoreSum += n.getNodeScore();
		}
		double average = scoreSum / children.size();
		setAverageScore(average);
		return averageScore;
	}

	// *************** GETTERS ***************
	public Node getParentNode() {
		return parentNode;
	}

	public List<OthelloMove> getNodeMoves() {
		return moves;
	}

	public OthelloMove getActionConsequent() {
		return actionConsequent;
	}

	public int getNumVisited() {
		return numVisited;
	}

	public double getAverageScore() {
		return averageScore;
	}
	
	public OthelloState getBoardState(){
		return boardState;
	}
	
	public List<Node> getChildren(){
		return children;
	}
	
	public int getNodeScore(){
		return score;
	}
	
	// *************** SETTERS ***************
	public void setParentNode(Node pNode) {
		parentNode = pNode;
	}

	public void setChildren(List<OthelloMove> cNodes) {
		moves = cNodes;
	}

	public void setActionConsequent(OthelloMove m) {
		actionConsequent = m;
	}

	public void setNumVisited(int num) {
		numVisited = num;
	}

	public void setAverageScore(double aScore) {
		averageScore = aScore;
	}
}
