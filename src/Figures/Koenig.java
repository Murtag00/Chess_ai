package Figures;

import java.util.LinkedList;
import java.util.List;

import Main.*;

public class Koenig extends Figure{
	
	public Koenig(Board b,  boolean isWhite) {
		super(b);
		this.isWhite = isWhite;
		value = Integer.MAX_VALUE;
		name = "Koenig";
	}


	
	@Override
	public List<Figure> getAccessableFigures() {
		// TODO Auto-generated method stub
		return GetAccessableFigures.koenigMovement(this);
	}
	
	@Override
	public List<Figure> getAccessableFigures(Position p) {
		return GetAccessableFigures.koenigMovement(this.getBoard(),p);
	}

}
