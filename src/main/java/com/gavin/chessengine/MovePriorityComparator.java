package com.gavin.chessengine;

import java.util.Comparator;

public class MovePriorityComparator implements Comparator<Move> {
	
	@Override
	public int compare(Move m1, Move m2) {
		return Integer.compare(m2.getPriority(), m1.getPriority());
	}

}