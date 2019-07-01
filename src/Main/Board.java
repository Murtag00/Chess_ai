package Main;

import java.util.LinkedList;
import java.util.List;

import Figures.*;

public class Board {

	private Figure[][] board = new Figure[8][8];
	private boolean isWhitesTurn = true;
	private List<Move> movesPlayed = new LinkedList<Move>();

	public Board(Figure[][] b) {
		if (b.length != 8 || b[0].length != 8) {
			throw new IllegalArgumentException("Given Board does not have 8x8 entrys!");
		}
		board = b;
	}

	public Board(char[][] b) {
		if (b.length != 8 || b[0].length != 8) {
			throw new IllegalArgumentException("Given char Array does not have 8x8 entrys!");
		}
		board = setBoard_from_charArray(b);
	}

	public Board() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = new EmptyField();
			}
		}
	}

	private Figure[][] setBoard_from_charArray(char[][] b) {
		Figure[][] f = new Figure[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				f[i][j] = BoardUtils.char_toFigures(this, b[i][j], new Position(i, j));
			}
		}
		return f;
	}

	public boolean isInBounds(int b) {
		return (-1 < b) && (b < 8);
	}

	public Figure getFigure_at(Position p) {
		return getFigure_at(p.getRow(), p.getCol());
	}

	public Figure getFigure_at(int row, int col) {
		return board[row][col];
	}

	public void makeMove(Move m) {
		if (isMoveLegal(m)) {
			isWhitesTurn = !isWhitesTurn;
			makeChange(m);
		} else {
			throw new IllegalArgumentException("Move is not legal: " + m);
		}
	}

	public void takeMoveBack(Move m) {
		isWhitesTurn = !isWhitesTurn;
		if (isMoveLegal(m)) {
		} else {
			isWhitesTurn = !isWhitesTurn;
			makeChange(m);
			throw new IllegalArgumentException("Move has not been played: " + m);
		}

	}

	public boolean isMoveLegal(Move m) {

		if (isWhitesTurn != m.isWhite()) {
			return false;
		}

		List<Move> moves = m.getMovingFigure().getMoves();
		moves.addAll(specialMoves(m.getMovingFigure()));
		if (!moves.contains(m)) {
			return false;
		}

		return true;
	}
	
	public boolean hasFigure_beenMoved(int id) {
		for(Move m: movesPlayed) {
			if(m.getMovingFigure().getId() == id ) {
				return true;
			}
		}
		return false;
	}

	public List<Move> specialMoves(Figure movingFigure) {

		return new LinkedList<Move>();
	}

	protected void makeChange(Move m) {
		switch (m.getType()) {
		case EnPassant:
			board[m.toPosition().getRow() + (m.isWhite() ? -1 : 1)][m.toPosition().getCol()] = new EmptyField();
		case Normal:
		case Twice:
			board[m.toPosition().getRow()][m.toPosition().getCol()] = m.getMovingFigure();
			board[m.fromPosition().getRow()][m.fromPosition().getCol()] = new EmptyField();
			break;
		case Rochade:
			int lOrR = (m.getFigure() == 'K' ? +1 : -1);
			int row = (m.isWhite() ? 0 : 7);
			Turm turm = (Turm) getFigure_at(row, (m.getFigure() == 'K' ? 0 : 7));
			Koenig koenig = (Koenig) getFigure_at(row, 3);
			board[m.toPosition().getRow()][m.toPosition().getCol()] = koenig;
			board[m.toPosition().getRow()][m.toPosition().getCol() + lOrR] = turm;
			break;
		case BauerTo:
			Figure whateverTheBauerTurnsTo = BoardUtils.returnFigure(this, m.getFigure(), m.isWhite());
			board[m.toPosition().getRow()][m.toPosition().getCol()] = whateverTheBauerTurnsTo;
			board[m.fromPosition().getRow()][m.fromPosition().getCol()] = new EmptyField();
			break;
		default:
			throw new IllegalArgumentException("Something happend to that move");
		}
		movesPlayed.add(m);
	}

	protected void reverseChange(Move m) {
		switch (m.getType()) {
		case EnPassant:
			board[m.toPosition().getRow() + (m.isWhite() ? -1 : 1)][m.toPosition().getCol()] = m.getDefeatedFigure();
			board[m.toPosition().getRow()][m.toPosition().getCol()] = new EmptyField();
			board[m.fromPosition().getRow()][m.fromPosition().getCol()] = m.getMovingFigure();
			break;
		case BauerTo:
		case Normal:
		case Twice:
			board[m.toPosition().getRow()][m.toPosition().getCol()] = m.getDefeatedFigure();
			board[m.fromPosition().getRow()][m.fromPosition().getCol()] = m.getMovingFigure();
			break;
		case Rochade:
			int lOrR = (m.getFigure() == 'K' ? +1 : -1);
			int row = (m.isWhite() ? 0 : 7);
			Turm turm = (Turm) getFigure_at(row, (m.getFigure() == 'K' ? 0 : 7));
			Koenig koenig = (Koenig) getFigure_at(row, 3);
			board[m.fromPosition().getRow()][m.fromPosition().getCol()] = koenig;
			board[m.toPosition().getRow()][(m.getFigure() == 'K' ? 0 : 7)] = turm;
			break;
		default:
			throw new IllegalArgumentException("Something happend to that move");
		}
		movesPlayed.remove(m);
	}

	public Position getPosition_of_FigureWithId(int figureID) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j].getId() == figureID) {
					return new Position(i, j);
				}
			}
		}
		throw new IllegalArgumentException("No Figure with Id: " + figureID);
	}
	
	public boolean hasLastMove() {
		return movesPlayed.size() > 0;
	}
	
	public Move getLastMove() {
		return movesPlayed.get(movesPlayed.size()-1);
	}

	@Override
	public boolean equals(Object board) {
		if (board instanceof Board) {
			Board b = (Board) board;
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (b.getFigure_at(i, j).firstChar() != getFigure_at(i, j).firstChar()) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
}
