import java.util.List;

public class OthelloMinimaxPlayer extends OthelloPlayer {

	private int depth;

	public OthelloMinimaxPlayer(int depth) {
		this.depth = depth;
	}

	@Override
	public OthelloMove getMove(OthelloState state) {

		// Generate the list of moves that opponent can move:
		List<OthelloMove> moves = state.generateMoves();

		// Array of scores after running minimax for each move
		int[] moveScores = new int[moves.size()];

		if (!moves.isEmpty()) {
			// For each move possible from the current state, run miniMax
			for (int i = 0; i < moves.size(); i++) {
				if (state.nextPlayerToMove == 1) {
					moveScores[i] = miniMax(depth, state, false);
				} else {
					moveScores[i] = miniMax(depth, state, true);
				}
			}

			/*
			 * Loop through all the scores of the moves to find the best move
			 * for the respective player
			 */
			int bestMoveIndex = 0;
			int currentBest = 0;
			for (int k = 0; k < moveScores.length; k++) {

				// Player X wants the smallest move value possible
				if (state.nextPlayerToMove == 1) {
					if (moveScores[k] < currentBest) {
						currentBest = moveScores[k];
						bestMoveIndex = k;
					}
				} else {
					// Player O wants the largest move value possible
					if (moveScores[k] > currentBest) {
						currentBest = moveScores[k];
						bestMoveIndex = k;
					}
				}
			}
			return moves.get(bestMoveIndex);
		}

		// If there are no possible moves, just return "pass":
		return null;
	}

	public int miniMax(int depth, OthelloState state, boolean maxPlayer) {
		/*
		 * If we've reached the max depth to look ahead or reached a terminal
		 * state, return the leaf state's score
		 */
		if (depth == 0 || state.gameOver()) {
			return state.score();
		}
		if (maxPlayer) {
			int bestScore = -1000000;
			// generate all the moves the state has
			List<OthelloMove> moves = state.generateMoves();
			for (OthelloMove m : moves) {
				OthelloState newState = state.applyMoveCloning(m);
				int v = miniMax(depth - 1, newState, false);
				bestScore = max(bestScore, v);
			}
			return bestScore;
		} else {
			int bestScore = 1000000;
			List<OthelloMove> moves = state.generateMoves();
			for (OthelloMove m : moves) {
				OthelloState newState = state.applyMoveCloning(m);
				int v = miniMax(depth - 1, newState, true);
				bestScore = min(bestScore, v);
			}
			return bestScore;
		}

	}

	private int min(int bestScore, int v) {
		if (v < bestScore) {
			return v;
		}
		return bestScore;
	}

	private int max(int bestScore, int v) {
		if (v > bestScore) {
			return v;
		}
		return bestScore;
	}

}// end OthelloMinimaxPlayer class
