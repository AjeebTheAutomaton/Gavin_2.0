package com.gavin.chessengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Formatter;
import java.nio.file.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Game {

    private MoveGenerator moveGenerator;
	private Engine engine;
    private boolean isWhitesMove;
	private boolean playersMoveMade;
    /*int enPassantCount = 0;
    int castlingCount = 0;
    int captureCount = 0;
	int promotionCount = 0;
	int checkmateCount = 0;
	*/
    private int perftDepth = 5;

    public Game(String startFEN) {
        this.moveGenerator = new MoveGenerator(startFEN);
		this.engine = new Engine(moveGenerator);
        this.isWhitesMove = true; // PLAYER TO START!!
		this.playersMoveMade = false;
		
    }

    public MoveGenerator getMoveGenerator() {
        return moveGenerator;
    }
	
    public void setPlayersMoveMade(boolean playersMoveMade) {
        this.playersMoveMade = playersMoveMade;
    }	

    public void startGame() {
		/*
		boolean checkmate = false;
		while (!checkmate) {
			turn(isWhitesMove);
		}*/

		//PERFT TEST
		System.out.print("Starting Perft Test");
        long startTime = System.currentTimeMillis();
        int totalNodes = moveGenerationTest(perftDepth, isWhitesMove, true, new ArrayList<Move>());
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime; // Calculate elapsed time
		System.out.println("Total Nodes: " + totalNodes);
        System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
		float timePerNode = (float)elapsedTime/(float)totalNodes;
		System.out.println("Time per Node: " + timePerNode);
		/*
        System.out.println("En Passants: " + enPassantCount);
        System.out.println("Castles: " + castlingCount);
		System.out.println("Promotions: " + promotionCount);
        System.out.println("Captures: " + captureCount);
		System.out.println("Checkmates: " + checkmateCount);*/
    }

    public void startTurn(boolean forWhite) {
		playersMoveMade = false;
        moveGenerator.setWhitesAttackedSquares();
        moveGenerator.setBlacksAttackedSquares();
        moveGenerator.removeEPTargets(forWhite);
        moveGenerator.setCheckBeamsAndRays(forWhite);
    }

    public void switchTurn() {
        this.isWhitesMove = !isWhitesMove;
    }
	
	public void turn(boolean isWhitesMove){
		
		//boolean isEngine = !isWhitesMove;
		
		startTurn(isWhitesMove);
		
		if (!isWhitesMove) {
			System.out.println("Engines Turn");
			engine.getBestMove(5, isWhitesMove, moveGenerator.getBoard()).makeMove(moveGenerator.getBoard());
		}
		else {
			while(!playersMoveMade) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		
		switchTurn();
	}

    public boolean getIsWhitesMove() {
        return isWhitesMove;
    }
	
	public Engine getEngine() {
        return engine;
    }

    public int moveGenerationTest(int depth, boolean forWhite, boolean topLevel, List<Move> moveSequence) {
		
		if (depth == 0) {
			// Print the sequence of moves when reaching an end node
			//System.out.println("Reached end node with move sequence:");
			//for (Move move : moveSequence) {
				//System.out.print(move.moveToString() + " ");
			//}
			//System.out.println();
			return 1;
		}

		startTurn(forWhite);

		int numPositions = 0;
		ArrayList<Move> moveList = moveGenerator.genMoveList(forWhite);
		//if (topLevel) {
			//moveList = new ArrayList<>(moveList.subList(0, 1));
		//}
		Map<String, Integer> initialMoveNodes = new HashMap<>();
		
		if (moveList.size() == 0) {
			//checkmateCount ++;
			return 0;
		}

		for (Move move : moveList) {
			//System.out.println("Move: " + move.moveToString() + " made at depth: " + depth);

			// Add the current move to the sequence
			moveSequence.add(move);
			
			move.makeMove(moveGenerator.getBoard());
			
			//DEBUG
			
			// Check and count special moves
			if (move instanceof EnPassant) {
				//enPassantCount++;
				//captureCount++;
			}
			if (move instanceof Castle) {
				//castlingCount++;
			}
			if (move instanceof Capture) {
				//captureCount++;
			}
			if (move instanceof Promotion) {
				//promotionCount++;
			}
			if (move instanceof PromotionCapture) {
				//promotionCount++;
				//captureCount++;
			}

			// Recurse with a copy of the current move sequence
			List<Move> newSequence = new ArrayList<>(moveSequence);
			int nodes = moveGenerationTest(depth - 1, !forWhite, false, newSequence);
			if (topLevel) {
				initialMoveNodes.put(move.moveToString(), nodes);
			}
			numPositions += nodes;
			move.unMakeMove(moveGenerator.getBoard());
			moveSequence.remove(moveSequence.size() - 1);
		}
		if (topLevel) {
			for (Map.Entry<String, Integer> entry : initialMoveNodes.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
		}
		return numPositions;
	}
}