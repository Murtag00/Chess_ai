package Main;

import Figures.*;

public class Move {
	private Position fromPosition;
	private Position toPosition;
	private Figure movingFigure;
	private Figure defeatedFigure;
	private MoveTyp type;
	private char figure;


	public Move(Board b, Position start, Position end) {
		fromPosition = start;
		toPosition = end;
		movingFigure = b.getFigure_at(start);
		defeatedFigure = b.getFigure_at(end);
		type = MoveTyp.Normal;
		figure = '-';
		errorHandling(movingFigure, defeatedFigure, start, end);
	}


	public Move(Board b, Position start, Position end, MoveTyp kind,char f) {
		fromPosition = start;
		toPosition = end;
		movingFigure = b.getFigure_at(start);
		defeatedFigure = b.getFigure_at(end);
		type =  kind;
		figure = f;
		errorHandling(movingFigure, defeatedFigure, start, end);
	}
	
	
	public Move(Figure movingFigure,Figure defeatedFigure, Position start, Position end) {
		fromPosition = start;
		toPosition = end;
		this.movingFigure = movingFigure;
		this.defeatedFigure = defeatedFigure;
		type = MoveTyp.Normal;
		figure = '-';
		errorHandling(movingFigure, defeatedFigure, start, end);
	}
	
	public Move(Figure movingFigure,Figure defeatedFigure, Position start, Position end,MoveTyp kind,char f) {
		fromPosition = start;
		toPosition = end;
		this.movingFigure = movingFigure;
		this.defeatedFigure = defeatedFigure;
		type = kind;
		figure = f;
		errorHandling(movingFigure, defeatedFigure, start, end);
	}
	
	private void errorHandling(Figure movingFigure,Figure defeatedFigure, Position start, Position end ) {
		if(start.equals(end)) {
			throw new IllegalArgumentException("Start and End position of the move are the same");
		}
		if(movingFigure instanceof EmptyField) {
			throw new IllegalArgumentException("An EmptyField can not make a move");
		}
	
	}
	
	public Position fromPostion() {
		return fromPosition;
	}
	
	public Position toPostion() {
		return toPosition;
	}

	public Figure getMovingFigure() {
		return movingFigure;
	}
	
	public Figure getDefeatedFigure() {
		return defeatedFigure;
	}
	
	public boolean isWhite( ) {
		return movingFigure.isWhite();
	}
	
	@Override
	public boolean equals(Object move) {
		if(move instanceof Move) {
			Move m =(Move) move;
			if(m.fromPosition.equals(fromPosition)&& m.toPosition.equals(toPosition) 
					&& movingFigure.equals(m.getMovingFigure())&& defeatedFigure.equals(defeatedFigure)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "from: "+fromPosition+" to: "+toPosition;
	}

	public MoveTyp getType() {
		return type;
	}

	public char getFigure() {
		return figure;
	}

}
