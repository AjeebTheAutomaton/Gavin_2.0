package com.gavin.chessengine;

import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.Formatter;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;


public class Engine {

    private MoveGenerator moveGenerator;
	private EngineListener engineListener;
	private int searchedNodes;

    private static final int PAWN_VALUE = 100;
    private static final int KNIGHT_VALUE = 300;
    private static final int BISHOP_VALUE = 330;
    private static final int ROOK_VALUE = 500;
    private static final int QUEEN_VALUE = 900;
	
	String filePath = "C:\\Users\\willh\\OneDrive\\Desktop\\CODING\\Gavin 2.0\\FenViewer\\EngineFENs.txt";
	Formatter fenFormatter;

	private static final int[] knightPositionTable = {
		-1,  8,  17,  26,  26,  17,   8,  -1,
		 6,  17,  28,  37,  37,  28,  17,   6,
		12,  26,  39,  46,  46,  39,  26,  12,
		17,  30,  41,  50,  50,  41,  30,  17,
		14,  26,  37,  46,  46,  37,  26,  14,
		 8,  17,  30,  37,  37,  30,  17,   8,
	   -23, -14,  -3,   3,   3,  -3, -14, -23,
	   -50, -46, -39, -32, -32, -39, -46, -50
	};
   
	private static final int[] bishopPositionTable = {
		 0,   5,  16,  22,  22,  16,   5,   0,  
		 5,  13,  17,  22,  22,  17,  13,   5,  
		16,  22,  27,  33,  33,  27,  22,  16,  
		22,  27,  33,  38,  38,  33,  27,  22,  
		13,  19,  23,  27,  27,  23,  19,  13,  
		 5,   0,  13,  19,  19,  13,   0,   5,  
		34,  45,  50,  50,  50,  50,  45,  34,  
	   -50, -45, -32, -26, -26, -32, -45, -50   
	};

	private static final int[] queenPositionTable = {
		 0,   5,  16,  22,  22,  16,   5,   0,  
		 5,  13,  17,  22,  22,  17,  13,   5,  
		16,  22,  27,  33,  33,  27,  22,  16,  
		22,  27,  33,  38,  38,  33,  27,  22,  
		13,  19,  23,  27,  27,  23,  19,  13,  
		 5,   0,  13,  19,  19,  13,   0,   5,  
		34,  20,  50,  50,  50,  50,  20,  34,  
	   -50, -45, -32, -26, -26, -32, -45, -50   
	};
	
	private static final int[] rookPositionTable = {
		 0,  0,  0,  0,  0,  0,  0,  0,
		 30,  35,  40,  50,  50,  40,  35,  30,
		 0,  0,  0,  0,  15,  15,  0,  0,
		 0,  0,  0,  0,  15,  15,  0,  0,
		 0,  0,  0,  0,  15,  15,  0,  0,
		 5,  10,  0,  0,  15,  15,  10,  5,
		 5,  5,  0,  0,  30,  30,  5,  5,
		 0,  30,  35,  50,  50,  35,  30,  0
	};
	
	private static final int[] whiteKingPositionTable = {
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 30,  40,  50,  -35,  -50,  30,  50,  30
	};

	private static final int[] blackKingPositionTable = {
		30,  40,  50,  -35,  -50,  30,  50,  30,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0,
		 0,  0,  0,  0,  0,  0,  0,  0, 
	};

    private static final int INF = 2147483647;

    public Engine(MoveGenerator moveGenerator) {
        this.moveGenerator = moveGenerator;
		try {
			fenFormatter = new Formatter(new File(filePath));
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		}
    }

	public void setEngineListener(EngineListener listener) {
        this.engineListener = listener;
    }

    public interface engineListener {
        void onEngineMove(Move move);
    }
	
	private void notifyEngineMove(Move move) {
		if (engineListener != null) {
            engineListener.onEngineMove(move);
        }
	}
	
	public void printFenListToFile(Formatter fenFormatter, ArrayList<String> fenList) {
		for(String fen: fenList) {
			fenFormatter.format("%s ", fen);
		}
		fenFormatter.format("\n");
	}
	
    public int minimax(int depth, boolean forWhite, Board board, int alpha, int beta, ArrayList<String> fenStrings) {
		if (depth == 0) {
			printFenListToFile(fenFormatter, fenStrings);
			searchedNodes++;
			return evaluate(board);
            //return captureMinimax(!forWhite, board, true, 0b0L);
			//return captureMinimax(10, forWhite, board, alpha, beta, 0b0L, fenStrings);
        }
        int bestEval;
        int polarity;
        int currentEval;
        int floor;
        if (forWhite) {
			bestEval = -INF;
			moveGenerator.startTurn(forWhite);
			ArrayList<Move> moveList = moveGenerator.genMoveList(forWhite);
			if (moveList.size() == 0) {
				return -INF;
			}
			for (Move move : moveList) {
				move.makeMove(board);
				fenStrings.add(moveGenerator.getBoard().boardToFen());
				currentEval = minimax(depth - 1, !forWhite, board, alpha, beta, fenStrings);
				move.unMakeMove(board);
				fenStrings.remove(fenStrings.size() - 1);
				bestEval = Math.max(currentEval, bestEval);
				alpha = Math.max(bestEval, alpha);
				if (beta <= alpha) {
					break;
				}				
			}			
        } 
		else {
			bestEval = INF;
			moveGenerator.startTurn(forWhite);
			ArrayList<Move> moveList = moveGenerator.genMoveList(forWhite);
			if (moveList.size() == 0) {
				return INF;
			}
			for (Move move : moveList) {
				move.makeMove(board);
				fenStrings.add(moveGenerator.getBoard().boardToFen());
				currentEval = minimax(depth - 1, !forWhite, board, alpha, beta, fenStrings);
				move.unMakeMove(board);
				fenStrings.remove(fenStrings.size() - 1);				
				bestEval = Math.min(currentEval, bestEval);
				beta = Math.min(bestEval, beta);
				if (beta <= alpha) {
					break;
				}				
			}
        }
        return bestEval;
    }	
	
    public int captureMinimax(int depth, boolean forWhite, Board board, int alpha, int beta, long lastPieceCaptured, ArrayList<String> fenStrings) {
		if (depth == 0) {
			printFenListToFile(fenFormatter, fenStrings);
			return evaluate(board);
            //return captureMinimax(!forWhite, board, true, 0b0L);
			//return captureMinimax(3, forWhite, board, fenStrings, 0b0L);
        }
        int bestEval;
        int polarity;
        int currentEval;
        int floor;
        if (forWhite) {
			bestEval = alpha;
			moveGenerator.startTurn(forWhite);
			ArrayList<Move> moveList = moveGenerator.genMoveList(forWhite);
			ArrayList<Move> captureList = new ArrayList<>(moveList.stream().filter(move -> (move instanceof Capture)).collect(Collectors.toList()));
			System.out.println("Capture List Size: " + captureList.size());
			if (captureList.size() == 0) {
				System.out.println("ALL CAPTURES SEARCHED");
				printFenListToFile(fenFormatter, fenStrings);
				return evaluate(board);
			}			
			for (Move capture : captureList) {
				if (((Capture)capture).getCapturedPiece() >= lastPieceCaptured) {
					lastPieceCaptured = ((Capture)capture).getCapturedPiece();
					if (lastPieceCaptured <= 0b10L) {
						lastPieceCaptured = 0b1L;
					}
					capture.makeMove(board);
					fenStrings.add(moveGenerator.getBoard().boardToFen());
					currentEval = captureMinimax(depth - 1, !forWhite, board, alpha, beta, lastPieceCaptured, fenStrings);
					capture.unMakeMove(board);
					fenStrings.remove(fenStrings.size() - 1);
					bestEval = Math.max(currentEval, bestEval);
					alpha = Math.max(bestEval, alpha);
					if (beta <= alpha) {
						break;
					}
				}
			}	
        } 
		else {
			bestEval = beta;
			moveGenerator.startTurn(forWhite);
			ArrayList<Move> moveList = moveGenerator.genMoveList(forWhite);
			ArrayList<Move> captureList = new ArrayList<>(moveList.stream().filter(move -> (move instanceof Capture)).collect(Collectors.toList()));
			if (captureList.size() == 0) {
				printFenListToFile(fenFormatter, fenStrings);
				return evaluate(board);
			}			
			for (Move capture : captureList) {
				if (((Capture)capture).getCapturedPiece() >= lastPieceCaptured) {
					lastPieceCaptured = ((Capture)capture).getCapturedPiece();
					if (lastPieceCaptured <= 0b10L) {
						lastPieceCaptured = 0b1L;
					}
					capture.makeMove(board);
					fenStrings.add(moveGenerator.getBoard().boardToFen());
					currentEval = captureMinimax(depth - 1, !forWhite, board, alpha, beta, lastPieceCaptured, fenStrings);
					capture.unMakeMove(board);
					fenStrings.remove(fenStrings.size() - 1);
					bestEval = Math.min(currentEval, bestEval);
					alpha = Math.min(bestEval, beta);
					if (beta <= alpha) {
						break;
					}
				}
			}
        }
        return bestEval;
    }	
	
	/*public int captureMinimax(int depth, boolean forWhite, Board board, ArrayList<String> fenStrings, long lastPieceCaptured) {
			
			int bestEval;
			int polarity;
			int currentEval;
			int floor;
			
			if (forWhite) {
				floor = -INF;
				polarity = 1;
			} else {
				floor = INF;
				polarity = -1;
			}
			if (depth == 0) {
				printFenListToFile(fenFormatter, fenStrings);
				//System.out.println("Evaluation Returned Due to Capture Depth Limit Reached");
				return evaluate(board);
				//return captureMinimax(!forWhite, board, true, 0b0L);
			}
			bestEval = floor;
			moveGenerator.startTurn(forWhite);
			ArrayList<Move> moveList = moveGenerator.genMoveList(forWhite);
			ArrayList<Move> captureList = new ArrayList<>(moveList.stream().filter(move -> (move instanceof Capture)).collect(Collectors.toList()));
			if (captureList.size() == 0) {
				//System.out.println("Evaluation Returned Due to No More Captures");
				return evaluate(board);
			}
			for (Move capture : captureList) {
				if (((Capture)capture).getCapturedPiece() >= lastPieceCaptured) {
					lastPieceCaptured = ((Capture)capture).getCapturedPiece();
					if (lastPieceCaptured <= 0b10L) {
						lastPieceCaptured = 0b1L;
					}
					capture.makeMove(board);
					fenStrings.add(moveGenerator.getBoard().boardToFen());
					currentEval = captureMinimax(depth - 1, !forWhite, board, fenStrings, lastPieceCaptured);
					if (polarity * currentEval > polarity * bestEval) {
						bestEval = currentEval;
					}
					capture.unMakeMove(board);
					fenStrings.remove(fenStrings.size() - 1);
				}
			}
			return bestEval;
	}*/	

    /*public int captureMinimax(boolean forWhite, Board board, boolean topLevel, long exchangeSquare) {
		int bestEval;
		int polarity;
		int currentEval;
		int floor;

		if (forWhite) {
			floor = -INF;
			polarity = 1;
		} else {
			floor = INF;
			polarity = -1;
		}
		bestEval = floor;
		moveGenerator.startTurn(forWhite);
		ArrayList<Move> moveList = moveGenerator.genMoveList(forWhite);
		if (moveList.size() == 0) {
			//System.out.println("No moves available, returning floor: " + floor);
			return floor;
		}

		ArrayList<Move> captureList = new ArrayList<>(moveList.stream()
			.filter(move -> (move instanceof Capture))
			.collect(Collectors.toList()));

		if (captureList.isEmpty()) {
			int eval = evaluate(board);
			//System.out.println("No captures available, returning evaluation: " + eval);
			return eval;
		}

		if(topLevel) {
			for (Move capture : captureList) {
				//System.out.println("Top-level capture: " + capture.moveToString());
				capture.makeMove(board);
				currentEval = captureMinimax(!forWhite, board, false, capture.getTo());
				//System.out.println("Evaluation for capture " + capture.moveToString() + ": " + currentEval);
				if (polarity * currentEval > polarity * bestEval) {
					bestEval = currentEval;
				}
				capture.unMakeMove(board);
			}
		} else {
			ArrayList<Move> filteredCaptureList = new ArrayList<>(captureList.stream()
				.filter(move -> ((move instanceof Capture) && 
								(((Capture) move).getMovingPiece() <= ((Capture) move).getCapturedPiece() || 
								 move.getTo() == exchangeSquare)))
				.collect(Collectors.toList()));
			if (filteredCaptureList.isEmpty()) {
				int eval = evaluate(board);
				//System.out.println("No filtered captures available, returning evaluation: " + eval);
				return eval;
			}		
			for (Move capture : filteredCaptureList) {
				//System.out.println("Filtered capture: " + capture.moveToString());
				capture.makeMove(board);
				currentEval = captureMinimax(!forWhite, board, false, capture.getTo());
				//System.out.println("Evaluation for filtered capture " + capture.moveToString() + ": " + currentEval);
				if (polarity * currentEval > polarity * bestEval) {
					bestEval = currentEval;
				}
				capture.unMakeMove(board);
			}
		}

		//System.out.println("Returning bestEval: " + bestEval);
		return bestEval;
	}*/

    public Move getBestMove(int depth, boolean forWhite, Board board) {
		long startTime = System.currentTimeMillis();
        int currentMoveEval;
        int bestEval;
        int polarity;
		searchedNodes = 0;
        Random random = new Random();
        Move bestMove = new Move();
		
		ArrayList<String> fenStrings = new ArrayList<>();
		fenStrings.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");

        if (forWhite) {
            bestEval = -INF;
            polarity = 1;
        } else {
            bestEval = INF;
            polarity = -1;
        }

        ArrayList<Move> moveList = moveGenerator.genMoveList(forWhite);
        System.out.println("Move List Size: " + moveList.size());

        for (Move move : moveList) {
			fenStrings.add(moveGenerator.getBoard().boardToFen());
            move.makeMove(board);
			fenStrings.add(moveGenerator.getBoard().boardToFen());
            currentMoveEval = minimax(depth - 1, !forWhite, board, -INF, INF, fenStrings);
            System.out.println("Move: " + move.moveToString() + ", Evaluation: " + currentMoveEval);
            if (polarity * currentMoveEval > polarity * bestEval) {
                bestEval = currentMoveEval;
                bestMove = move;
            } else if (currentMoveEval == bestEval & random.nextBoolean()) {
                bestEval = currentMoveEval;
                bestMove = move;
            }
            move.unMakeMove(board);
			fenStrings.remove(fenStrings.size() - 1);
        }
		notifyEngineMove(bestMove);
		long endTime = System.currentTimeMillis();
		System.out.println("ENGINE PLAYED: " + bestMove.moveToString());
		System.out.println("Elapsed Time: " + (endTime - startTime));
		System.out.println("Nodes Searched: " + searchedNodes);
		searchedNodes = 0;
        return bestMove;
    }

    public int evaluate(Board board) {
		return evaluateMaterial(board) + evaluatePositioning(board);
    }

    public int evaluateMaterial(Board board) {
        int whiteMaterial = 0;
        int blackMaterial = 0;

        whiteMaterial += Long.bitCount(board.getWhitePawnBB()) * PAWN_VALUE;
        whiteMaterial += Long.bitCount(board.getWhiteKnightBB()) * KNIGHT_VALUE;
        whiteMaterial += Long.bitCount(board.getWhiteBishopBB()) * BISHOP_VALUE;
        whiteMaterial += Long.bitCount(board.getWhiteRookBB()) * ROOK_VALUE;
        whiteMaterial += Long.bitCount(board.getWhiteQueenBB()) * QUEEN_VALUE;

        blackMaterial += Long.bitCount(board.getBlackPawnBB()) * PAWN_VALUE;
        blackMaterial += Long.bitCount(board.getBlackKnightBB()) * KNIGHT_VALUE;
        blackMaterial += Long.bitCount(board.getBlackBishopBB()) * BISHOP_VALUE;
        blackMaterial += Long.bitCount(board.getBlackRookBB()) * ROOK_VALUE;
        blackMaterial += Long.bitCount(board.getBlackQueenBB()) * QUEEN_VALUE;

        return whiteMaterial - blackMaterial;
    }
	
	public int evaluatePositioning(Board board) {
		
		int whitePositioning = 0;
		int blackPositioning = 0;
		
		long[] squares = moveGenerator.getSquareDIR();
		long square;
		
		for (int i = 0; i <= 63; i++) {
			square = squares[i];
			if(moveGenerator.isOnBB(square, board.getWhiteKnightBB())) {
				whitePositioning += knightPositionTable[i];
			}
			else if (moveGenerator.isOnBB(square, board.getWhiteBishopBB())) {
				whitePositioning += bishopPositionTable[i];
			}
			else if (moveGenerator.isOnBB(square, board.getWhiteRookBB())) {
				whitePositioning += rookPositionTable[i];
			}
			else if (moveGenerator.isOnBB(square, board.getWhiteQueenBB())) {
				whitePositioning += queenPositionTable[i];
			}
			else if (moveGenerator.isOnBB(square, board.getWhiteKingBB())) {
				whitePositioning += whiteKingPositionTable[i];
			}
			else {
				//System.out.println("Piece positioning table unavaliable");
			}
			if(moveGenerator.isOnBB(square, board.getBlackKnightBB())) {
				blackPositioning += knightPositionTable[63 - i];
			}
			else if (moveGenerator.isOnBB(square, board.getBlackBishopBB())) {
				blackPositioning += bishopPositionTable[63 - i];
			}
			else if (moveGenerator.isOnBB(square, board.getBlackRookBB())) {
				blackPositioning += rookPositionTable[63 - i];
			}
			else if (moveGenerator.isOnBB(square, board.getBlackQueenBB())) {
				blackPositioning += queenPositionTable[63 - i];
			}
			else if (moveGenerator.isOnBB(square, board.getBlackKingBB())) {
				whitePositioning += blackKingPositionTable[63 - i];
			}
			else {
				//System.out.println("Piece positioning table unavaliable");
			}
		}
		return whitePositioning - blackPositioning;
	}

    public void playMove(int depth, boolean forWhite) {
        System.out.println("playMove() CALLED With forWhite = " + forWhite);
        Board board = moveGenerator.getBoard();
        Move move = getBestMove(depth, forWhite, board);
        System.out.println(move.moveToString());
        move.makeMove(board);
    }
}