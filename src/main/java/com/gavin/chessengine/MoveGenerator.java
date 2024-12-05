package com.gavin.chessengine;

import java.net.URL;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class MoveGenerator {
	
	private String positionFEN;
	private Board board;

	//Rook Check Beams:
	private long whiteUpCheckBeam;
	private long whiteDownCheckBeam;
	private long whiteLeftCheckBeam;
	private long whiteRightCheckBeam;	
	private long blackUpCheckBeam;
	private long blackDownCheckBeam;
	private long blackLeftCheckBeam;
	private long blackRightCheckBeam;
	
	//Bishop Check Beams:
	private long whiteUpLeftCheckBeam;
	private long whiteUpRightCheckBeam;
	private long whiteDownLeftCheckBeam;
	private long whiteDownRightCheckBeam;	
	private long blackUpLeftCheckBeam;
	private long blackUpRightCheckBeam;
	private long blackDownLeftCheckBeam;
	private long blackDownRightCheckBeam;
	
	//EnPassant Check Beams:
	private long whiteLeftEPCheckBeam;
	private long whiteRightEPCheckBeam;
	private long blackLeftEPCheckBeam;
	private long blackRightEPCheckBeam;
	
	//Open Check Beams:
	private long[] whiteOpenCheckBeams = new long[2];
	private long[] blackOpenCheckBeams = new long[2];
	
	//Open Check Rays:
	private long[] whiteOpenCheckRays = new long[2];
	private long[] blackOpenCheckRays = new long[2];
	
	//Check Masks
	private long whiteKnightCheckMask;
	private long blackKnightCheckMask;
	private long whiteBishopCheckMask;
	private long blackBishopCheckMask;
	private long whiteRookCheckMask;
	private long blackRookCheckMask;
	
	//Attacked Squares:
	private long blackAttackedSquares;
	private long whiteAttackedSquares;
	private long whiteKnightAttacks;
	private long blackKnightAttacks;
	private long whitePawnAttacks;
	private long blackPawnAttacks;
	//Directories:
	
	//Move DIR:
	public static HashMap<Long, Long> moveBBDIR;
	public static HashMap<Long, Long> moveBBDIRWPC;
	
	//Blocked Move DIR:
	public static HashMap<String, Long> blockedWPawnMoveDIR;
	public static HashMap<String, Long> blockedBPawnMoveDIR;
	public static HashMap<String, Long> blockedBishopMoveDIR;
	public static HashMap<String, Long> blockedRookMoveDIR;
	public static HashMap<String, Long> blockedQueenMoveDIR;
	
	//Rook Check Beam DIR:
	public static HashMap<Long, Long> upCheckBeamDIR;
	public static HashMap<Long, Long> downCheckBeamDIR;
	public static HashMap<Long, Long> leftCheckBeamDIR;
	public static HashMap<Long, Long> rightCheckBeamDIR;
	
	//Blocked Rook Check Beam DIR:
	public static HashMap<String, Long> blockedUpCheckBeamDIR;
	public static HashMap<String, Long> blockedDownCheckBeamDIR;
	public static HashMap<String, Long> blockedLeftCheckBeamDIR;
	public static HashMap<String, Long> blockedRightCheckBeamDIR;
	
	//Bishop Check Beam DIR:
	public static HashMap<Long, Long> upLeftCheckBeamDIR;
	public static HashMap<Long, Long> upRightCheckBeamDIR;
	public static HashMap<Long, Long> downLeftCheckBeamDIR;
	public static HashMap<Long, Long> downRightCheckBeamDIR;
	
	//Blocked Bishop Check Beam DIR:
	public static HashMap<String, Long> blockedUpLeftCheckBeamDIR;
	public static HashMap<String, Long> blockedUpRightCheckBeamDIR;
	public static HashMap<String, Long> blockedDownLeftCheckBeamDIR;
	public static HashMap<String, Long> blockedDownRightCheckBeamDIR;
	
	//Square, Rank, File DIR:
	public static long[] squareDIR;
	public static HashMap<Long, Long> moveDIR;
	static final long[] pieces = {0b1L, 0b10L, 0b11L, 0b100L, 0b101L, 0b110L, 0b111L};
	private long[] ranks = {0b0000000000000000000000000000000000000000000000000000000011111111L,
							0b0000000000000000000000000000000000000000000000001111111100000000L,
							0b0000000000000000000000000000000000000000111111110000000000000000L,
							0b0000000000000000000000000000000011111111000000000000000000000000L,
							0b0000000000000000000000001111111100000000000000000000000000000000L,
							0b0000000000000000111111110000000000000000000000000000000000000000L,
							0b0000000011111111000000000000000000000000000000000000000000000000L,
							0b1111111100000000000000000000000000000000000000000000000000000000L};	
	private long[] files = {0b1000000010000000100000001000000010000000100000001000000010000000L,
							0b0100000001000000010000000100000001000000010000000100000001000000L,
							0b0010000000100000001000000010000000100000001000000010000000100000L,
							0b0001000000010000000100000001000000010000000100000001000000010000L,
							0b0000100000001000000010000000100000001000000010000000100000001000L,
							0b0000010000000100000001000000010000000100000001000000010000000100L,
							0b0000001000000010000000100000001000000010000000100000001000000010L,
							0b0000000100000001000000010000000100000001000000010000000100000001L};
	public static HashMap<Long, Integer> subPriorityDIR;
	
	//Castiling Masks:	
	private long whiteKingsideCastleMaskBlock = 0b00000110L;
	private long whiteQueensideCastleMaskBlock = 0b01110000L;
	private long whiteKingsideCastleMaskCheck = 0b00001110L;
	private long whiteQueensideCastleMaskCheck = 0b00111000L;
	private long blackKingsideCastleMaskBlock = 0b0000011000000000000000000000000000000000000000000000000000000000L;
	private long blackQueensideCastleMaskBlock = 0b0111000000000000000000000000000000000000000000000000000000000000L;
	private long blackKingsideCastleMaskCheck = 0b0000111000000000000000000000000000000000000000000000000000000000L;
	private long blackQueensideCastleMaskCheck = 0b0011100000000000000000000000000000000000000000000000000000000000L;
	
	public MoveGenerator(String startFEN) {
		
		//Set up board
		positionFEN = startFEN;
		board = new Board();
		board.SetBBfromFEN(startFEN);
		board.printBoard();
		board.boardToFen();
		
		//Initialize Directories
		loadSquareDIR();
		loadMoveBBDIR();
		loadMoveBBDIRWPC();
		loadBlockedWPawnMoveDIR();
		loadBlockedBPawnMoveDIR();
		loadBlockedBishopMoveDIR();
		loadBlockedRookMoveDIR();
		loadBlockedQueenMoveDIR();
		loadUpCheckBeamDIR();
		loadDownCheckBeamDIR();
		loadLeftCheckBeamDIR();
		loadRightCheckBeamDIR();
		loadBlockedUpCheckBeamDIR();
		loadBlockedDownCheckBeamDIR();
		loadBlockedLeftCheckBeamDIR();
		loadBlockedRightCheckBeamDIR();
		loadUpLeftCheckBeamDIR();
		loadUpRightCheckBeamDIR();
		loadDownLeftCheckBeamDIR();
		loadDownRightCheckBeamDIR();
		loadBlockedUpLeftCheckBeamDIR();
		loadBlockedUpRightCheckBeamDIR();
		loadBlockedDownLeftCheckBeamDIR();
		loadBlockedDownRightCheckBeamDIR();
		loadSubPriorityDIR();
	}
	
	public static void main(String[] args) {
		//Game - = new Game("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
	}
	
	public String getPositionFEN() {
		return positionFEN;
	}
	
	public Long genPPid(long position, long piece) {
		Long PPid = (3 * position) + (5 * piece);
		return PPid;
	}
	
	//Load Directory Methods
	
	public void loadSquareDIR() {
		long a8 = 0b1000000000000000000000000000000000000000000000000000000000000000L;
		squareDIR = new long[64];
		for (int i = 0; i <= 63; i++) {
			squareDIR[i] = a8 >>> i;
		}
	}
	
	public void loadMoveBBDIR() {
		moveBBDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/MoveDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					moveBBDIR.put(fileInput.nextLong(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: MoveBBDIR.txt");
		}
	}
	
	public void loadMoveBBDIRWPC() {
		moveBBDIRWPC = new HashMap<>();
		URL resource = getClass().getResource("/Directories/MoveDIRWPC.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					moveBBDIRWPC.put(fileInput.nextLong(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: MoveBBDIRWPC.txt");
		}
	}	
	
	public void loadBlockedWPawnMoveDIR() {
		blockedWPawnMoveDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedWPawnMoveDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedWPawnMoveDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedWPawnMoveDIR.txt");
		}
	}			
	
	public void loadBlockedBPawnMoveDIR() {
		blockedBPawnMoveDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedBPawnMoveDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedBPawnMoveDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedBPawnMoveDIR.txt");
		}
	}		
	
	public void loadBlockedBishopMoveDIR() {
		blockedBishopMoveDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedBishopMoveDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedBishopMoveDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedBishopMoveDIR.txt");
		}
	}
	
	public void loadBlockedRookMoveDIR() {
		blockedRookMoveDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedRookMoveDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedRookMoveDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedRookMoveDIR.txt");
		}
	}
	
	public void loadBlockedQueenMoveDIR() {
		blockedQueenMoveDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedQueenMoveDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedQueenMoveDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedQueenMoveDIR.txt");
		}
	}
	
	public void loadUpCheckBeamDIR() {
		upCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/UpCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					upCheckBeamDIR.put(fileInput.nextLong(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: UpCheckBeamDIR.txt");
		}
	}	
	
	public void loadDownCheckBeamDIR() {
		downCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/DownCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					downCheckBeamDIR.put(fileInput.nextLong(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: DownCheckBeamDIR.txt");
		}
	}	
	
	public void loadLeftCheckBeamDIR() {
		leftCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/LeftCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					leftCheckBeamDIR.put(fileInput.nextLong(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: LeftCheckBeamDIR.txt");
		}
	}
	
	public void loadRightCheckBeamDIR() {
		rightCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/RightCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					rightCheckBeamDIR.put(fileInput.nextLong(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: RightCheckBeamDIR.txt");
		}
	}
	
	public void loadBlockedUpCheckBeamDIR() {
		blockedUpCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedUpCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedUpCheckBeamDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedUpCheckBeamDIR.txt");
		}
	}
	
	public void loadBlockedDownCheckBeamDIR() {
		blockedDownCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedDownCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedDownCheckBeamDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedDownCheckBeamDIR.txt");
		}
	}
	
	public void loadBlockedLeftCheckBeamDIR() {
		blockedLeftCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedLeftCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedLeftCheckBeamDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedLeftCheckBeamDIR.txt");
		}
	}
	
	public void loadBlockedRightCheckBeamDIR() {
		blockedRightCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedRightCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedRightCheckBeamDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedRightCheckBeamDIR.txt");
		}
	}
	
	public void loadUpLeftCheckBeamDIR() {
		upLeftCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/UpLeftCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					upLeftCheckBeamDIR.put(fileInput.nextLong(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: UpLeftCheckBeamDIR.txt");
		}
	}
	
	public void loadUpRightCheckBeamDIR() {
		upRightCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/UpRightCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					upRightCheckBeamDIR.put(fileInput.nextLong(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: UpRightCheckBeamDIR.txt");
		}
	}
	
	public void loadDownLeftCheckBeamDIR() {
		downLeftCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/DownLeftCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					downLeftCheckBeamDIR.put(fileInput.nextLong(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: DownLeftCheckBeamDIR.txt");
		}
	}
	
	public void loadDownRightCheckBeamDIR() {
		downRightCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/DownRightCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					downRightCheckBeamDIR.put(fileInput.nextLong(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: DownRightCheckBeamDIR.txt");
		}
	}
	
	public void loadBlockedUpLeftCheckBeamDIR() {
		blockedUpLeftCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedUpLeftCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedUpLeftCheckBeamDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedUpLeftCheckBeamDIR.txt");
		}
	}
	
	public void loadBlockedUpRightCheckBeamDIR() {
		blockedUpRightCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedUpRightCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedUpRightCheckBeamDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedUpRightCheckBeamDIR.txt");
		}
	}
	
	public void loadBlockedDownLeftCheckBeamDIR() {
		blockedDownLeftCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedDownLeftCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedDownLeftCheckBeamDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedDownLeftCheckBeamDIR.txt");
		}
	}
	
	public void loadBlockedDownRightCheckBeamDIR() {
		blockedDownRightCheckBeamDIR = new HashMap<>();
		URL resource = getClass().getResource("/Directories/BlockedDownRightCheckBeamDIR.txt");
		if (resource != null) {
			try (Scanner fileInput = new Scanner(resource.openStream())) {
				while (fileInput.hasNext()) {
					blockedDownRightCheckBeamDIR.put(fileInput.next(), fileInput.nextLong());
				}
			}
			catch (IOException err) {
			}
		} else {
			System.out.println("Resource not found: BlockedDownRightCheckBeamDIR.txt");
		}
	}
	
	public void loadSubPriorityDIR() {
	
		subPriorityDIR = new HashMap<>();

		subPriorityDIR.put(0b1L, 0);   // a1
		subPriorityDIR.put(0b10L, 1);  // b1
		subPriorityDIR.put(0b100L, 2); // c1
		subPriorityDIR.put(0b1000L, 2); // d1
		subPriorityDIR.put(0b10000L, 2); // e1
		subPriorityDIR.put(0b100000L, 2); // f1
		subPriorityDIR.put(0b1000000L, 1); // g1
		subPriorityDIR.put(0b10000000L, 0); // h1

		subPriorityDIR.put(0b100000000L, 1); // a2
		subPriorityDIR.put(0b1000000000L, 2); // b2
		subPriorityDIR.put(0b10000000000L, 4); // c2
		subPriorityDIR.put(0b100000000000L, 4); // d2
		subPriorityDIR.put(0b1000000000000L, 4); // e2
		subPriorityDIR.put(0b10000000000000L, 4); // f2
		subPriorityDIR.put(0b100000000000000L, 2); // g2
		subPriorityDIR.put(0b1000000000000000L, 1); // h2

		subPriorityDIR.put(0b10000000000000000L, 2); // a3
		subPriorityDIR.put(0b100000000000000000L, 4); // b3
		subPriorityDIR.put(0b1000000000000000000L, 6); // c3
		subPriorityDIR.put(0b10000000000000000000L, 7); // d3
		subPriorityDIR.put(0b100000000000000000000L, 7); // e3
		subPriorityDIR.put(0b1000000000000000000000L, 6); // f3
		subPriorityDIR.put(0b10000000000000000000000L, 4); // g3
		subPriorityDIR.put(0b100000000000000000000000L, 2); // h3

		subPriorityDIR.put(0b1000000000000000000000000L, 2); // a4
		subPriorityDIR.put(0b10000000000000000000000000L, 4); // b4
		subPriorityDIR.put(0b100000000000000000000000000L, 6); // c4
		subPriorityDIR.put(0b1000000000000000000000000000L, 8); // d4
		subPriorityDIR.put(0b10000000000000000000000000000L, 8); // e4
		subPriorityDIR.put(0b100000000000000000000000000000L, 6); // f4
		subPriorityDIR.put(0b1000000000000000000000000000000L, 4); // g4
		subPriorityDIR.put(0b10000000000000000000000000000000L, 2); // h4

		subPriorityDIR.put(0b100000000000000000000000000000000L, 2); // a5
		subPriorityDIR.put(0b1000000000000000000000000000000000L, 4); // b5
		subPriorityDIR.put(0b10000000000000000000000000000000000L, 6); // c5
		subPriorityDIR.put(0b100000000000000000000000000000000000L, 8); // d5
		subPriorityDIR.put(0b1000000000000000000000000000000000000L, 8); // e5
		subPriorityDIR.put(0b10000000000000000000000000000000000000L, 6); // f5
		subPriorityDIR.put(0b100000000000000000000000000000000000000L, 4); // g5
		subPriorityDIR.put(0b1000000000000000000000000000000000000000L, 2); // h5

		subPriorityDIR.put(0b10000000000000000000000000000000000000000L, 2); // a6
		subPriorityDIR.put(0b100000000000000000000000000000000000000000L, 4); // b6
		subPriorityDIR.put(0b1000000000000000000000000000000000000000000L, 6); // c6
		subPriorityDIR.put(0b10000000000000000000000000000000000000000000L, 8); // d6
		subPriorityDIR.put(0b100000000000000000000000000000000000000000000L, 8); // e6
		subPriorityDIR.put(0b1000000000000000000000000000000000000000000000L, 6); // f6
		subPriorityDIR.put(0b10000000000000000000000000000000000000000000000L, 4); // g6
		subPriorityDIR.put(0b100000000000000000000000000000000000000000000000L, 2); // h6

		subPriorityDIR.put(0b1000000000000000000000000000000000000000000000000L, 1); // a7
		subPriorityDIR.put(0b10000000000000000000000000000000000000000000000000L, 2); // b7
		subPriorityDIR.put(0b100000000000000000000000000000000000000000000000000L, 4); // c7
		subPriorityDIR.put(0b1000000000000000000000000000000000000000000000000000L, 6); // d7
		subPriorityDIR.put(0b10000000000000000000000000000000000000000000000000000L, 6); // e7
		subPriorityDIR.put(0b100000000000000000000000000000000000000000000000000000L, 4); // f7
		subPriorityDIR.put(0b1000000000000000000000000000000000000000000000000000000L, 2); // g7
		subPriorityDIR.put(0b10000000000000000000000000000000000000000000000000000000L, 1); // h7

		subPriorityDIR.put(0b100000000000000000000000000000000000000000000000000000000L, 0); // a8
		subPriorityDIR.put(0b1000000000000000000000000000000000000000000000000000000000L, 1); // b8
		subPriorityDIR.put(0b10000000000000000000000000000000000000000000000000000000000L, 2); // c8
		subPriorityDIR.put(0b100000000000000000000000000000000000000000000000000000000000L, 2); // d8
		subPriorityDIR.put(0b1000000000000000000000000000000000000000000000000000000000000L, 2); // e8
		subPriorityDIR.put(0b10000000000000000000000000000000000000000000000000000000000000L, 2); // f8
		subPriorityDIR.put(0b100000000000000000000000000000000000000000000000000000000000000L, 1); // g8
		subPriorityDIR.put(0b1000000000000000000000000000000000000000000000000000000000000000L, 0); // h8
	}	

	//Methods for returning Blocked MoveBB
	public long getBlockerFor(long position, long piece) {
		if (piece == 0b1L) {
			return moveBBDIRWPC.get(genPPid(position, piece)) & (board.getPiecesBB() | board.getBlackEPTargetBB());
		}
		if (piece == 0b10L) {
			return moveBBDIRWPC.get(genPPid(position, piece)) & (board.getPiecesBB() | board.getWhiteEPTargetBB());
		}
		if (piece != 0b0L) {
			long blockerBB = moveBBDIRWPC.get(genPPid(position, piece)) & board.getPiecesBB();
			return removeBorder(blockerBB, position);
		}
		else {
			System.out.println("ERROR: invalid piece passed into getBlockerFor()");
			return 0b0L;
		}
	}
	
	public long getAllyBlockerFor(long position, long piece) {
		if (checkPieceIsWhite(position)) {
			long blockerBB = moveBBDIRWPC.get(genPPid(position, piece)) & board.getWhitePiecesBB();
			return removeBorder(blockerBB, position);
		}
		else {
			long blockerBB = moveBBDIRWPC.get(genPPid(position, piece)) & board.getBlackPiecesBB();
			return removeBorder(blockerBB, position);			
		}
	}
	
	public String genBPPid(long position, long blocker) {
        BigInteger bigPosition = new BigInteger(Long.toString(position));
        BigInteger bigBlocker = new BigInteger(Long.toString(blocker));
        BigInteger sum = bigBlocker.add(bigPosition);
        BigInteger product = sum.multiply(sum.add(BigInteger.ONE)).divide(BigInteger.valueOf(2));
        BigInteger result = product.add(bigPosition);
        return result.toString();		
	}
	
	/*
	public String genBPPid(long position, long blocker) {
		System.out.println("Position: " + position);
		System.out.println("Blocker: " + blocker);
		long sum = position + blocker;
		System.out.println("Sum: " + sum);
		System.out.println("MAX._VALUE: " + Long.MAX_VALUE ", MIN_VALUE: " + Long.MIN_VALUE;
		
		// Check for overflow in product computation
		if (sum >= Long.MAX_VALUE || sum <= Long.MIN_VALUE) {
			// Fallback to BigInteger if overflow is likely
			BigInteger bigPosition = BigInteger.valueOf(position);
			BigInteger bigBlocker = BigInteger.valueOf(blocker);
			BigInteger bigSum = bigBlocker.add(bigPosition);
			BigInteger bigProduct = bigSum.multiply(bigSum.add(BigInteger.ONE)).divide(BigInteger.valueOf(2));
			BigInteger bigResult = bigProduct.add(bigPosition);
			return bigResult.toString();
		}

		long product = sum * (sum + 1) / 2;
		long result = product + position;
		return Long.toString(result);
	}*/
	
	public boolean enPassantRevealsCheck(long position, boolean forWhite) {
		long leftCheckBeam;
		long rightCheckBeam;
		
		if (forWhite) {
			leftCheckBeam = whiteLeftEPCheckBeam;
			rightCheckBeam = whiteRightEPCheckBeam;
		}
		else {
			leftCheckBeam = blackLeftEPCheckBeam;
			rightCheckBeam = blackRightEPCheckBeam;			
		}
		if (Long.bitCount(board.getWhitePawnBB() & leftCheckBeam) == 1 & Long.bitCount(board.getBlackPawnBB() & leftCheckBeam) == 1 & Long.bitCount(board.getPiecesBB() & leftCheckBeam) == 3) {
			return true;
		}
		else if (Long.bitCount(board.getWhitePawnBB() & rightCheckBeam) == 1 & Long.bitCount(board.getBlackPawnBB() & rightCheckBeam) == 1 & Long.bitCount(board.getPiecesBB() & rightCheckBeam) == 3) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//<><>><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>MOVE LIST METHODS<><>><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
	public ArrayList<Move> moveBBToList(long on, long avaliableMoveBB, long piece, boolean forWhite) {
		long promotionRank;
		if (avaliableMoveBB == 0b0L) {
			return new ArrayList<>();
		}
		if (forWhite) {
			promotionRank = ranks[7];
		}
		else {
			promotionRank = ranks[0];
		}
		ArrayList<Move> moveList = new ArrayList<Move>();
		int moveCounter = 0;
		long capturedPiece;
		Move move;
		for (long square: squareDIR) {
			if (isOnBB(square, avaliableMoveBB)) {
				capturedPiece = board.getPieceOnSquare(square);
				if (capturedPiece == 0b1000L & (piece <= 0b10L)) {
					if (!enPassantRevealsCheck(on, forWhite)) {
						move = new EnPassant(on, square, piece, forWhite, false, 110);
						move.setIsCheck(getMoveIsCheck(move));
						moveList.add(move);
					}
				}
				else if (capturedPiece != 0b0L & (capturedPiece < 0b1000L)){
					if ((piece <= 0b10L) & isOnBB(square,promotionRank)) {
						//moveList.add(new PromotionCapture(on, square, piece, forWhite, 30, 0b11L, capturedPiece));
						//moveList.add(new PromotionCapture(on, square, piece, forWhite, 30, 0b100L, capturedPiece));
						//moveList.add(new PromotionCapture(on, square, piece, forWhite, 30, 0b101L, capturedPiece));
						move = new PromotionCapture(on, square, piece, forWhite, false, 300, 0b110L, capturedPiece);
						move.setIsCheck(getMoveIsCheck(move));
						moveList.add(move);
					}
					else {
						move = new Capture(on, square, piece, forWhite, false, 100 + subPriorityDIR.get(square) - subPriorityDIR.get(on), capturedPiece);
						move.setIsCheck(getMoveIsCheck(move));
						moveList.add(move);
					}
					//moveCounter++;
				}
				else {
					if ((piece <= 0b10L) & isOnBB(square,promotionRank)) {
						//moveList.add(new Promotion(on, square, piece, forWhite, 20, 0b11L));
						//moveList.add(new Promotion(on, square, piece, forWhite, 20, 0b100L));
						//moveList.add(new Promotion(on, square, piece, forWhite, 20, 0b101L));
						move = new Promotion(on, square, piece, forWhite, false, 200, 0b110L);
						move.setIsCheck(getMoveIsCheck(move));
						moveList.add(move);
					}
					else {
						move = new Move(on, square, piece, forWhite, false, subPriorityDIR.get(square) - subPriorityDIR.get(on));
						move.setIsCheck(getMoveIsCheck(move));
						moveList.add(move);
					}
					
				}
				moveCounter++;
				if (moveCounter == 27) {
					return moveList;
				}
			}
		}
		//System.out.println(moveList);
		return moveList;
	}
	
	public ArrayList<Move> genMoveList(boolean forWhite) {
		long piece;
		boolean pieceColour;
		long avaliableMoveBB;
		ArrayList<Move> moveList = new ArrayList<Move>();
		for (long square: squareDIR) {
			piece = board.getPieceOnSquare(square);
			if (piece != 0b0L) {
				pieceColour = checkPieceIsWhite(square);
				avaliableMoveBB = getLegalMoveBB(square, piece, forWhite);
				moveList.addAll(moveBBToList(square, avaliableMoveBB, piece, forWhite));
				//Castling 
				if (piece == 0b111L & pieceColour == forWhite) {
					if (checkCastlingAvaliable(forWhite, 'Q')) {
						moveList.add(new Castle('Q', forWhite, false, 90));
					}
					if (checkCastlingAvaliable(forWhite, 'K')) {
						moveList.add(new Castle('K', forWhite, false, 90));
					}
				}
				if (moveList.size() == 218) {
					return moveList;
				}
			}
		}
		Collections.sort(moveList, new MovePriorityComparator());
		return moveList;
	}

	/*public boolean getIsCheck(long on, long to, long piece, boolean forWhite) {
		
		boolean isCheck;
		
		long kingBB;
		long upCheckBlocker;
		long downCheckBlocker;
		long leftCheckBlocker;
		long rightCheckBlocker;
		
		if (forWhite) {
			kingBB = board.getWhiteKingBB();
			upCheckBlocker = getCheckRookBlockerFor('U', true);
			downCheckBlocker = getCheckRookBlockerFor('D', true);
			leftCheckBlocker = getCheckRookBlockerFor('L', true);
			rightCheckBlocker = getCheckRookBlockerFor('R', true);
		}
		else {
			kingBB = board.getBlackKingBB();
			upCheckBlocker = getCheckRookBlockerFor('U', false);
			downCheckBlocker = getCheckRookBlockerFor('D', false);
			leftCheckBlocker = getCheckRookBlockerFor('L', false);
			rightCheckBlocker = getCheckRookBlockerFor('R', false);
		}
		return isCheck;
	}*/
	
	public ArrayList<Move> genCaptureList(boolean forWhite) {
		
		long piece;
		boolean pieceColour;
		long avaliableMoveBB;
		long enemyPiecesBB;
		ArrayList<Move> captureList = new ArrayList<Move>();

		if(forWhite) {
			enemyPiecesBB = board.getBlackPiecesBB();
		}
		else {
			enemyPiecesBB = board.getWhitePiecesBB();
		}
		
		for (long square: squareDIR) {
			
			piece = board.getPieceOnSquare(square);
			
			if (piece != 0b0L) {
				pieceColour = checkPieceIsWhite(square);
				avaliableMoveBB = getLegalMoveBB(square, piece, forWhite) & enemyPiecesBB;
				captureList.addAll(moveBBToList(square, avaliableMoveBB, piece, forWhite));
			}
		}
		
		return captureList;
	}
	
	public void printMoveList(Move[] moveList) {
		for (Move move: moveList) {
			//move.printMove();
		}
	}
	
	//<><>><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>><><><><>
	//{0b1L, 0b10L, 0b11L, 0b100L, 0b101L, 0b110L, 0b111L};
	public long getPsuedoLegalMoveBB(long position, long piece) {
		long PPid = genPPid(position, piece);
		if (piece == 0b11L | piece == 0b111L) {
			return removeAlliedCaptures(moveBBDIR.get(PPid), position);
		}
		else {
			long blockerBB = getBlockerFor(position, piece);
			String BPPid = genBPPid(position, blockerBB);
			if (blockerBB == 0b0L) {
				return removeAlliedCaptures(moveBBDIR.get(PPid), position);
			}
			if (piece == 0b1L) {
				return removeAlliedCaptures(blockedWPawnMoveDIR.get(BPPid), position);
			}
			else if (piece == 0b10L) {
				return removeAlliedCaptures(blockedBPawnMoveDIR.get(BPPid), position);
			}
			else if (piece == 0b100L) {
				return removeAlliedCaptures(blockedBishopMoveDIR.get(BPPid), position);
			}
			else if (piece == 0b101L) {
				return removeAlliedCaptures(blockedRookMoveDIR.get(BPPid), position);
			}
			else if (piece == 0b110L) {
				return removeAlliedCaptures(blockedQueenMoveDIR.get(BPPid), position);
			}
			else {
				return 0b0L;
			}
		}
	}
	
	//Includes Ally Captures to be used in generating attacked squares.
	public long getPsuedoLegalWithAllyCapturesMoveBB(long position, long piece) {
		long PPid = genPPid(position, piece);
		if (piece == 0b11L | piece == 0b111L) {
			return moveBBDIR.get(PPid);
		}
		else {
			long blockerBB = getBlockerFor(position, piece);
			String BPPid = genBPPid(position, blockerBB);
			if (blockerBB == 0b0L) {
				return moveBBDIR.get(PPid);
			}
			if (piece == 0b1L) {
				return blockedWPawnMoveDIR.get(BPPid);
			}
			else if (piece == 0b10L) {
				return blockedBPawnMoveDIR.get(BPPid);
			}
			else if (piece == 0b100L) {
				return blockedBishopMoveDIR.get(BPPid);
			}
			else if (piece == 0b101L) {
				return blockedRookMoveDIR.get(BPPid);
			}
			else if (piece == 0b110L) {
				return blockedQueenMoveDIR.get(BPPid);
			}
			else {
				return 0b0L;
			}
		}
	}
	
	public long getLegalMoveBB(long position, long piece, boolean forWhite) {
		long allyPiecesBB;
		long kingBB;
		long knightAttacks;
		long pawnAttacks;
		long enemyKnightBB;
		long enemyPawnBB;
		long[] openCheckBeams;
		long[] openCheckRays;
		if (piece == 0b0L | piece == 0b1000L) {
			return 0b0L;
		}
		if (forWhite) {
			openCheckBeams = whiteOpenCheckBeams;
			openCheckRays = whiteOpenCheckRays;
			allyPiecesBB = board.getWhitePiecesBB();
			kingBB = board.getWhiteKingBB();
			enemyKnightBB = board.getBlackKnightBB();
			enemyPawnBB = board.getBlackPawnBB();
			knightAttacks = blackKnightAttacks;
			pawnAttacks = blackPawnAttacks;
		}
		else {
			openCheckBeams = blackOpenCheckBeams;
			openCheckRays = blackOpenCheckRays;
			allyPiecesBB = board.getBlackPiecesBB();
			kingBB = board.getBlackKingBB();
			enemyKnightBB = board.getWhiteKnightBB();
			enemyPawnBB = board.getWhitePawnBB();
			knightAttacks = whiteKnightAttacks;
			pawnAttacks = whitePawnAttacks;
		}
		//Double check, king must move.
		//Double slider check.
		if (openCheckBeams[0] != 0b0L & openCheckBeams[1] != 0b0L & isOnBB(position, allyPiecesBB)) {
			if (piece == 0b111L) {
				return removeIgnoreCheck(getPsuedoLegalMoveBB(position, piece), forWhite) & ~(openCheckRays[0] | openCheckRays[1]);
			}
			else {
				return 0b0L;
			}
		}
		//Double check knight and slider
		if (isOnBB(kingBB, knightAttacks) & (openCheckBeams[0] != 0b0L | openCheckBeams[1] != 0b0L) & isOnBB(position, allyPiecesBB)) {
			if (piece == 0b111L) {
				return removeIgnoreCheck(getPsuedoLegalMoveBB(position, piece), forWhite) & ~(openCheckRays[0] | openCheckRays[1]);
			}
			else {
				return 0b0L;
			}
		}
		//Knight check, king must move or capture knight.
		if (isOnBB(kingBB, knightAttacks) & isOnBB(position, allyPiecesBB)) {
			if (piece == 0b111L) {
				return removeIgnoreCheck(getPsuedoLegalMoveBB(position, piece), forWhite) & ~(openCheckRays[0] | openCheckRays[1]);
			}
			else {
				return removeSelfRevealedCheck(getPsuedoLegalMoveBB(position, piece), position, forWhite) & getCheckingKnight(forWhite);
			}
		}
		//Pawn check, king must move or capture pawn.
		if (isOnBB(kingBB, pawnAttacks) & isOnBB(position, allyPiecesBB)) {
			long[] checkingPawn = getCheckingPawn(forWhite);
			if (piece == 0b111L) {
				return removeIgnoreCheck(getPsuedoLegalMoveBB(position, piece), forWhite) & ~(openCheckRays[0]);
			}
			else if (piece <= 0b10L) {
				return removeSelfRevealedCheck(getPsuedoLegalMoveBB(position, piece), position, forWhite) & (checkingPawn[0] | checkingPawn[1]);
			}
			else {
				return removeSelfRevealedCheck(getPsuedoLegalMoveBB(position, piece), position, forWhite) & checkingPawn[0];
			}
		}
		//Single check, king moves or block check.
		if (openCheckBeams[0] != 0b0L & openCheckBeams[1] == 0b0L & isOnBB(position, allyPiecesBB)) {
			if (piece == 0b111L) {
				return removeIgnoreCheck(getPsuedoLegalMoveBB(position, piece), forWhite) & ~openCheckRays[0];
			}
			else {
				return removeSelfRevealedCheck(getPsuedoLegalMoveBB(position, piece), position, forWhite) & openCheckBeams[0];
			}
		}
		//Single check, king moves or block check.
		if (openCheckBeams[0] == 0b0L & openCheckBeams[1] != 0b0L & isOnBB(position, allyPiecesBB)) {
			if (piece == 0b111L) {
				return removeIgnoreCheck(getPsuedoLegalMoveBB(position, piece), forWhite) & ~openCheckRays[1];
			}
			else {
				return removeSelfRevealedCheck(getPsuedoLegalMoveBB(position, piece), position, forWhite) & openCheckBeams[1];
			}
		}
		//No Check.
		else {
			if (piece == 0b111L & isOnBB(position, allyPiecesBB)) {
				return removeIgnoreCheck(getPsuedoLegalMoveBB(position, piece), forWhite);
			}
			else if (isOnBB(position, allyPiecesBB)){
				long blockerBB = getBlockerFor(position, piece);
				String BPPid = genBPPid(position, blockerBB);
				return removeSelfRevealedCheck(getPsuedoLegalMoveBB(position, piece), position, forWhite);
			}
			else {
				return 0b0L;
			}
		}
	}
	
	public long getAllyBlockedMoveBB(long position, long piece) {
		long PPid = genPPid(position, piece);
		if (piece == 0b11L | piece == 0b111L) {
			return moveBBDIR.get(PPid);
		}
		else {
			long blockerBB = getAllyBlockerFor(position, piece);
			String BPPid = genBPPid(position, blockerBB);
			if (blockerBB == 0b0L) {
				return moveBBDIR.get(genPPid(position, piece));
			}
			else {
				if (piece == 0b1L) {
					return blockedWPawnMoveDIR.get(BPPid);
				}
				else if (piece == 0b10L) {
					return blockedBPawnMoveDIR.get(BPPid);
				}
				else if (piece == 0b100L) {
					return blockedBishopMoveDIR.get(BPPid);
				}
				else if (piece == 0b101L) {
					return blockedRookMoveDIR.get(BPPid);
				}
				else if (piece == 0b110L) {
					return blockedQueenMoveDIR.get(BPPid);
				}
				else {
					return 0b0L;
				}
			}
		}
	}

	//Methods for removing suedo-legal moves
	
	public boolean checkPieceIsWhite(long position) {
		if (isOnBB(position, board.getWhitePiecesBB())) {
			return true;
		}
		else { 
			return false;
		}
	}
	
	public long removeSelfRevealedCheck(long avaliableMoves, long position, boolean forWhite) {
		if (forWhite) {
			if (isOnBB(position, whiteUpCheckBeam)) {
				return avaliableMoves & whiteUpCheckBeam;
			}
			if (isOnBB(position, whiteDownCheckBeam)) {
				return avaliableMoves & whiteDownCheckBeam;
			}
			if (isOnBB(position, whiteLeftCheckBeam)) {
				return avaliableMoves & whiteLeftCheckBeam;
			}
			if (isOnBB(position, whiteRightCheckBeam)) {
				return avaliableMoves & whiteRightCheckBeam;
			}
			if (isOnBB(position, whiteUpLeftCheckBeam)) {
				return avaliableMoves & whiteUpLeftCheckBeam;
			}
			if (isOnBB(position, whiteUpRightCheckBeam)) {
				return avaliableMoves & whiteUpRightCheckBeam;
			}
			if (isOnBB(position, whiteDownLeftCheckBeam)) {
				return avaliableMoves & whiteDownLeftCheckBeam;
			}
			if (isOnBB(position, whiteDownRightCheckBeam)) {
				return avaliableMoves & whiteDownRightCheckBeam;
			}
		}
		else {
			if (isOnBB(position, blackUpCheckBeam)) {
				return avaliableMoves & blackUpCheckBeam;
			}
			if (isOnBB(position, blackDownCheckBeam)) {
				return avaliableMoves & blackDownCheckBeam;
			}
			if (isOnBB(position, blackLeftCheckBeam)) {
				return avaliableMoves & blackLeftCheckBeam;
			}
			if (isOnBB(position, blackRightCheckBeam)) {
				return avaliableMoves & blackRightCheckBeam;
			}
			if (isOnBB(position, blackUpLeftCheckBeam)) {
				return avaliableMoves & blackUpLeftCheckBeam;
			}
			if (isOnBB(position, blackUpRightCheckBeam)) {
				return avaliableMoves & blackUpRightCheckBeam;
			}
			if (isOnBB(position, blackDownLeftCheckBeam)) {
				return avaliableMoves & blackDownLeftCheckBeam;
			}
			if (isOnBB(position, blackDownRightCheckBeam)) {
				return avaliableMoves & blackDownRightCheckBeam;
			}
		}
		return avaliableMoves;
	}
	
	public long removeIgnoreCheck(long avaliableMoves, boolean forWhite) {
		if (forWhite) {
			return avaliableMoves &~ blackAttackedSquares; 
		}
		else {
			return avaliableMoves &~ whiteAttackedSquares; 
		}
	}

	public long removeAlliedCaptures(long avaliableMoves, long position) {
		if (isOnBB(position, board.getWhitePiecesBB())) {
			return avaliableMoves &~board.getWhitePiecesBB();
		}
		else if (isOnBB(position, board.getBlackPiecesBB())) {
			return avaliableMoves &~board.getBlackPiecesBB();
		}
		else {
			System.out.print("BB error: piece not of either colour!");
			return 0b0L;
		}
	}
	
	public long getCheckRookBlockerFor(char direction, boolean forWhite) {
		long rookBB;
		long queenBB;
		long kingBB;
		if (forWhite) {
			rookBB = board.getBlackRookBB();
			queenBB = board.getBlackQueenBB();
			kingBB = board.getWhiteKingBB();
		}
		else {
			rookBB = board.getWhiteRookBB();
			queenBB = board.getWhiteQueenBB();
			kingBB = board.getBlackKingBB();
		}
		switch (direction) {
			case 'U':
				return upCheckBeamDIR.get(kingBB) & (rookBB | queenBB);
			case 'D':
				return downCheckBeamDIR.get(kingBB) & (rookBB | queenBB);
			case 'L':
				return leftCheckBeamDIR.get(kingBB) & (rookBB | queenBB);
			case 'R':
				return rightCheckBeamDIR.get(kingBB) & (rookBB | queenBB);
			default:
				System.out.println("ERROR: Invalid Direction Passed!!");
				return 0b0L;
		}
	}
	
	public void setCheckMasks(boolean forWhite) {

		long kingBB;
		
		if (forWhite) {
			kingBB = board.getBlackKingBB();
			this.whiteKnightCheckMask = getPsuedoLegalWithAllyCapturesMoveBB(kingBB, 0b11L);
			this.whiteBishopCheckMask = getPsuedoLegalWithAllyCapturesMoveBB(kingBB, 0b100L);
			this.whiteRookCheckMask = getPsuedoLegalWithAllyCapturesMoveBB(kingBB, 0b101L);
		}
		else {
			kingBB = board.getWhiteKingBB();
			this.blackKnightCheckMask = getPsuedoLegalWithAllyCapturesMoveBB(kingBB, 0b11L);
			this.blackBishopCheckMask = getPsuedoLegalWithAllyCapturesMoveBB(kingBB, 0b100L);
			this.blackRookCheckMask = getPsuedoLegalWithAllyCapturesMoveBB(kingBB, 0b101L);
		}
	}
	
	//Testing Method
	public boolean getMoveIsCheck(Move move) {
		
		//Bit Boards
		long kingBB;
		long rookCheckMask;
		long bishopCheckMask;
		long rookRevealedCheckMask;
		long bishopRevealedCheckMask;
		
		//Move Variables
		long movingPiece = move.getMovingPiece();
		long on = move.getOn();
		long to = move.getTo();
		
		boolean forWhite = move.getForWhite();
		
		if (forWhite) {
			//setCheckBeamsAndRays(!forWhite);
			
			if ((movingPiece == 0b100L | movingPiece == 0b110L) & isOnBB(whiteBishopCheckMask, to)) {
				return true;
			}
			else if ((movingPiece == 0b101L | movingPiece == 0b110L) & isOnBB(whiteRookCheckMask, to)) {
				return true;
			}
			else if (movingPiece == 0b10L & isOnBB(whiteKnightCheckMask, to)) {
				return true;
			}
			else {
				if (isOnBB(on,blackUpCheckBeam & ~board.getWhitePiecesBB()) & !(isOnBB(to,blackUpCheckBeam & ~board.getWhitePiecesBB()))) {
					return true;
				}
				else if (isOnBB(on,blackUpLeftCheckBeam & ~board.getWhitePiecesBB()) & !(isOnBB(to,blackUpLeftCheckBeam & ~board.getWhitePiecesBB()))) {
					return true;
				}	
				else if (isOnBB(on,blackLeftCheckBeam & ~board.getWhitePiecesBB()) & !(isOnBB(to,blackLeftCheckBeam & ~board.getWhitePiecesBB()))) {
					return true;
				}	
				else if (isOnBB(on,blackDownLeftCheckBeam & ~board.getWhitePiecesBB()) & !(isOnBB(to,blackDownLeftCheckBeam & ~board.getWhitePiecesBB()))) {
					return true;
				}
				else if (isOnBB(on,blackDownCheckBeam & ~board.getWhitePiecesBB()) & !(isOnBB(to,blackDownCheckBeam & ~board.getWhitePiecesBB()))) {
					return true;
				}
				else if (isOnBB(on,blackDownRightCheckBeam & ~board.getWhitePiecesBB()) & !(isOnBB(to,blackDownRightCheckBeam & ~board.getWhitePiecesBB()))) {
					return true;
				}	
				else if (isOnBB(on,blackRightCheckBeam & ~board.getWhitePiecesBB()) & !(isOnBB(to,blackRightCheckBeam & ~board.getWhitePiecesBB()))) {
					return true;
				}	
				else if (isOnBB(on,blackUpRightCheckBeam & ~board.getWhitePiecesBB()) & !(isOnBB(to,blackUpRightCheckBeam & ~board.getWhitePiecesBB()))) {
					return true;
				}					
			}
		}
		else {
			if ((movingPiece == 0b100L | movingPiece == 0b110L) & isOnBB(blackBishopCheckMask, to)) {
				return true;
			}
			else if ((movingPiece == 0b101L | movingPiece == 0b110L) & isOnBB(blackRookCheckMask, to)) {
				return true;
			}
			else if (movingPiece == 0b10L & isOnBB(blackKnightCheckMask, to)) {
				return true;
			}
			else {
				if (isOnBB(on,whiteUpCheckBeam & ~board.getBlackPiecesBB()) & !(isOnBB(to,whiteUpCheckBeam & ~board.getBlackPiecesBB()))) {
					return true;
				}
				else if (isOnBB(on,whiteUpLeftCheckBeam & ~board.getBlackPiecesBB()) & !(isOnBB(to,whiteUpLeftCheckBeam & ~board.getBlackPiecesBB()))) {
					return true;
				}	
				else if (isOnBB(on,whiteLeftCheckBeam & ~board.getBlackPiecesBB()) & !(isOnBB(to, whiteLeftCheckBeam & ~board.getBlackPiecesBB()))) {
					return true;
				}	
				else if (isOnBB(on,whiteDownLeftCheckBeam & ~board.getBlackPiecesBB()) & !(isOnBB(to, whiteDownLeftCheckBeam & ~board.getBlackPiecesBB()))) {
					return true;
				}
				else if (isOnBB(on,whiteDownCheckBeam & ~board.getBlackPiecesBB()) & !(isOnBB(to, whiteDownCheckBeam & ~board.getBlackPiecesBB()))) {
					return true;
				}
				else if (isOnBB(on,whiteDownRightCheckBeam & ~board.getBlackPiecesBB()) & !(isOnBB(to, whiteDownRightCheckBeam & ~board.getBlackPiecesBB()))) {
					return true;
				}	
				else if (isOnBB(on,whiteRightCheckBeam & ~board.getBlackPiecesBB()) & !(isOnBB(to, whiteRightCheckBeam & ~board.getBlackPiecesBB()))) {
					return true;
				}	
				else if (isOnBB(on,whiteUpRightCheckBeam & ~board.getBlackPiecesBB()) & !(isOnBB(to, whiteUpRightCheckBeam & ~board.getBlackPiecesBB()))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void setRookCheckBeams(boolean forWhite) {
		long kingBB;
		long upCheckBlocker;
		long downCheckBlocker;
		long leftCheckBlocker;
		long rightCheckBlocker;
		if (forWhite) {
			kingBB = board.getWhiteKingBB();
			upCheckBlocker = getCheckRookBlockerFor('U', true);
			downCheckBlocker = getCheckRookBlockerFor('D', true);
			leftCheckBlocker = getCheckRookBlockerFor('L', true);
			rightCheckBlocker = getCheckRookBlockerFor('R', true);
			if (upCheckBlocker != 0b0L) {
				this.whiteUpCheckBeam = blockedUpCheckBeamDIR.get(genBPPid(upCheckBlocker, kingBB));
				if (Long.bitCount(whiteUpCheckBeam & ~upCheckBlocker & board.getPiecesBB()) > 1) {
					this.whiteUpCheckBeam = 0b0L;
				}
			}
			if (downCheckBlocker != 0b0L) {
				this.whiteDownCheckBeam = blockedDownCheckBeamDIR.get(genBPPid(downCheckBlocker, kingBB));
				if (Long.bitCount(whiteDownCheckBeam & ~downCheckBlocker & board.getPiecesBB()) > 1) {
					this.whiteDownCheckBeam = 0b0L;
				}
			}
			if (leftCheckBlocker != 0b0L) {
				this.whiteLeftCheckBeam = blockedLeftCheckBeamDIR.get(genBPPid(leftCheckBlocker, kingBB));
				if (Long.bitCount(whiteLeftCheckBeam & ~leftCheckBlocker & board.getPiecesBB()) == 2) {
					this.whiteLeftEPCheckBeam = blockedLeftCheckBeamDIR.get(genBPPid(leftCheckBlocker, kingBB));
				}
				if (Long.bitCount(whiteLeftCheckBeam & ~leftCheckBlocker & board.getPiecesBB()) > 1) {
					this.whiteLeftCheckBeam = 0b0L;
				}
			}
			if (rightCheckBlocker != 0b0L) {
				this.whiteRightCheckBeam = blockedRightCheckBeamDIR.get(genBPPid(rightCheckBlocker, kingBB));
				if (Long.bitCount(whiteRightCheckBeam & ~rightCheckBlocker & board.getPiecesBB()) == 2) {
					this.whiteRightEPCheckBeam = blockedRightCheckBeamDIR.get(genBPPid(rightCheckBlocker, kingBB));
				}
				if (Long.bitCount(whiteRightCheckBeam & ~rightCheckBlocker & board.getPiecesBB()) > 1) {
					this.whiteRightCheckBeam = 0b0L;
				}
			}			
		}
		else {
			//rookBB = board.getWhiteRookBB();
			kingBB = board.getBlackKingBB();
			upCheckBlocker = getCheckRookBlockerFor('U', false);
			downCheckBlocker = getCheckRookBlockerFor('D', false);
			leftCheckBlocker = getCheckRookBlockerFor('L', false);
			rightCheckBlocker = getCheckRookBlockerFor('R', false);
			if (upCheckBlocker != 0b0L) {
				this.blackUpCheckBeam = blockedUpCheckBeamDIR.get(genBPPid(upCheckBlocker, kingBB));
				if (Long.bitCount(blackUpCheckBeam & ~upCheckBlocker & board.getPiecesBB()) > 1) {
					this.blackUpCheckBeam = 0b0L;
				}
			}
			if (downCheckBlocker != 0b0L) {
				this.blackDownCheckBeam = blockedDownCheckBeamDIR.get(genBPPid(downCheckBlocker, kingBB));
				if (Long.bitCount(blackDownCheckBeam & ~downCheckBlocker & board.getPiecesBB()) > 1) {
					this.blackDownCheckBeam = 0b0L;
				}
			}
			if (leftCheckBlocker != 0b0L) {
				this.blackLeftCheckBeam = blockedLeftCheckBeamDIR.get(genBPPid(leftCheckBlocker, kingBB));
				if (Long.bitCount(blackLeftCheckBeam & ~leftCheckBlocker & board.getPiecesBB()) == 2) {
					this.blackLeftEPCheckBeam = blockedLeftCheckBeamDIR.get(genBPPid(leftCheckBlocker, kingBB));
				}
				if (Long.bitCount(blackLeftCheckBeam & ~leftCheckBlocker & board.getPiecesBB()) > 1) {
					this.blackLeftCheckBeam = 0b0L;
				}
			}
			if (rightCheckBlocker != 0b0L) {
				this.blackRightCheckBeam = blockedRightCheckBeamDIR.get(genBPPid(rightCheckBlocker, kingBB));
				if (Long.bitCount(blackRightCheckBeam & ~rightCheckBlocker & board.getPiecesBB()) == 2) {
					this.blackRightEPCheckBeam = blockedRightCheckBeamDIR.get(genBPPid(rightCheckBlocker, kingBB));
				}
				if (Long.bitCount(blackRightCheckBeam & ~rightCheckBlocker & board.getPiecesBB()) > 1) {
					this.blackRightCheckBeam = 0b0L;
				}
			}
		}
	}
	
	public void setBishopCheckBeams(boolean forWhite) {
		long kingBB;
		long upLeftCheckBlocker;
		long upRightCheckBlocker;
		long downLeftCheckBlocker;
		long downRightCheckBlocker;
		if (forWhite) {
			kingBB = board.getWhiteKingBB();
			upLeftCheckBlocker = getCheckBishopBlockerFor("UL", true);
			upRightCheckBlocker = getCheckBishopBlockerFor("UR", true);
			downLeftCheckBlocker = getCheckBishopBlockerFor("DL", true);
			downRightCheckBlocker = getCheckBishopBlockerFor("DR", true);
			if (upLeftCheckBlocker != 0b0L) {
				this.whiteUpLeftCheckBeam = blockedUpLeftCheckBeamDIR.get(genBPPid(upLeftCheckBlocker, kingBB));
				if (Long.bitCount(whiteUpLeftCheckBeam & ~upLeftCheckBlocker & board.getPiecesBB()) > 1) {
					this.whiteUpLeftCheckBeam = 0b0L;
				}
			}
			if (upRightCheckBlocker != 0b0L) {
				this.whiteUpRightCheckBeam = blockedUpRightCheckBeamDIR.get(genBPPid(upRightCheckBlocker, kingBB));
				if (Long.bitCount(whiteUpRightCheckBeam & ~upRightCheckBlocker & board.getPiecesBB()) > 1) {
					this.whiteUpRightCheckBeam = 0b0L;
				}
			}
			if (downLeftCheckBlocker != 0b0L) {
				this.whiteDownLeftCheckBeam = blockedDownLeftCheckBeamDIR.get(genBPPid(downLeftCheckBlocker, kingBB));
				if (Long.bitCount(whiteDownLeftCheckBeam & ~downLeftCheckBlocker & board.getPiecesBB()) > 1) {
					this.whiteDownLeftCheckBeam = 0b0L;
				}
			}
			if (downRightCheckBlocker != 0b0L) {
				this.whiteDownRightCheckBeam = blockedDownRightCheckBeamDIR.get(genBPPid(downRightCheckBlocker, kingBB));
				if (Long.bitCount(whiteDownRightCheckBeam & ~downRightCheckBlocker & board.getPiecesBB()) > 1) {
					this.whiteDownRightCheckBeam = 0b0L;
				}
			}			
		}
		else {
			kingBB = board.getBlackKingBB();
			upLeftCheckBlocker = getCheckBishopBlockerFor("UL", false);
			upRightCheckBlocker = getCheckBishopBlockerFor("UR", false);
			downLeftCheckBlocker = getCheckBishopBlockerFor("DL", false);
			downRightCheckBlocker = getCheckBishopBlockerFor("DR", false);
			if (upLeftCheckBlocker != 0b0L) {
				this.blackUpLeftCheckBeam = blockedUpLeftCheckBeamDIR.get(genBPPid(upLeftCheckBlocker, kingBB));
				if (Long.bitCount(blackUpLeftCheckBeam & ~upLeftCheckBlocker & board.getPiecesBB()) > 1) {
					this.blackUpLeftCheckBeam = 0b0L;
				}
			}
			if (upRightCheckBlocker != 0b0L) {
				this.blackUpRightCheckBeam = blockedUpRightCheckBeamDIR.get(genBPPid(upRightCheckBlocker, kingBB));
				if (Long.bitCount(blackUpRightCheckBeam & ~upRightCheckBlocker & board.getPiecesBB()) > 1) {
					this.blackUpRightCheckBeam = 0b0L;
				}
			}
			if (downLeftCheckBlocker != 0b0L) {
				this.blackDownLeftCheckBeam = blockedDownLeftCheckBeamDIR.get(genBPPid(downLeftCheckBlocker, kingBB));
				if (Long.bitCount(blackDownLeftCheckBeam & ~downLeftCheckBlocker & board.getPiecesBB()) > 1) {
					this.blackDownLeftCheckBeam = 0b0L;
				}
			}
			if (downRightCheckBlocker != 0b0L) {
				this.blackDownRightCheckBeam = blockedDownRightCheckBeamDIR.get(genBPPid(downRightCheckBlocker, kingBB));
				if (Long.bitCount(blackDownRightCheckBeam & ~downRightCheckBlocker & board.getPiecesBB()) > 1) {
					this.blackDownRightCheckBeam = 0b0L;
				}
			}
		}
		
	}
	
	public long getCheckBishopBlockerFor(String direction, boolean forWhite) {
		long bishopBB;
		long queenBB;
		//long pawnBB;
		long kingBB;
		if (forWhite) {
			bishopBB = board.getBlackBishopBB();
			queenBB = board.getBlackQueenBB();
			kingBB = board.getWhiteKingBB();
		}
		else {
			bishopBB = board.getWhiteBishopBB();
			queenBB = board.getWhiteQueenBB();
			kingBB = board.getBlackKingBB();
		}
		switch (direction) {
			case "UL":
				return upLeftCheckBeamDIR.get(kingBB) & (bishopBB | queenBB);
			case "UR":
				return upRightCheckBeamDIR.get(kingBB) & (bishopBB | queenBB);
			case "DL":
				return downLeftCheckBeamDIR.get(kingBB) & (bishopBB | queenBB);
			case "DR":
				return downRightCheckBeamDIR.get(kingBB) & (bishopBB | queenBB);
			default:
				System.out.println("ERROR: Invalid Direction Passed!!");
				return 0b0L;
		}
	}
	
	public long[] getCheckingPawn(boolean forWhite) {
		long[] checkingPawn = new long[2];
		if (forWhite) {
			long pawnBB = board.getBlackPawnBB();
			long kingBB = board.getWhiteKingBB();
			long kingMoveBB = moveBBDIR.get(genPPid(kingBB, 0b111L));
			if (isOnBB((kingBB << 7) & kingMoveBB, pawnBB)) {
				checkingPawn[0] = (kingBB << 7) & kingMoveBB;
				checkingPawn[1] = checkingPawn[0] << 8;
				return checkingPawn;
			}
			else if (isOnBB((kingBB << 9) & kingMoveBB, pawnBB)) {
				checkingPawn[0] =(kingBB << 9) & kingMoveBB;
				checkingPawn[1] = checkingPawn[0] << 8;
				return checkingPawn;
			}
			else {
				checkingPawn[0] = 0b0L;
				return checkingPawn;
			}
		}
		else {
			long pawnBB = board.getWhitePawnBB();
			long kingBB = board.getBlackKingBB();
			long kingMoveBB = moveBBDIR.get(genPPid(board.getBlackKingBB(), 0b111L));
			if (isOnBB((kingBB >>> 7) & kingMoveBB, pawnBB)) {
				checkingPawn[0] = (kingBB >>> 7) & kingMoveBB;
				checkingPawn[1] = checkingPawn[0] >>> 8;
				return checkingPawn;
			}
			else if (isOnBB((kingBB >>> 9) & kingMoveBB, pawnBB)) {
				checkingPawn[0] = (kingBB >>> 9) & kingMoveBB;
				checkingPawn[1] = checkingPawn[0] >>> 8;
				return checkingPawn;
			}
			else {
				checkingPawn[0] = 0b0L;
				return checkingPawn;
			}
		}
	}	
	
	public long getCheckingKnight(boolean forWhite) {
		long knightBB;
		long kingBB;
		long knightCheckMask;
		if (forWhite) {
			knightBB = board.getBlackKnightBB();
			kingBB = board.getWhiteKingBB();
			knightCheckMask = moveBBDIR.get(genPPid(kingBB, 0b11L));
		}
		else {
			knightBB = board.getWhiteKnightBB();
			kingBB = board.getBlackKingBB();
			knightCheckMask = moveBBDIR.get(genPPid(kingBB, 0b11L));
		}
		return knightCheckMask & knightBB;
	}
	
	public void setOpenCheckBeamsAndRays(boolean forWhite) {
	long pawnBB;
	long kingBB;
	long piecesBB;
	//long notBQBB;
	//long notRQBB;
	long upCheckBeam;
	long downCheckBeam;
	long leftCheckBeam;
	long rightCheckBeam;
	long upLeftCheckBeam;
	long upRightCheckBeam;
	long downLeftCheckBeam;
	long downRightCheckBeam;
	long[] openCheckBeams = new long[2];
	long[] openCheckRays = new long[2];
		if (forWhite) {
			pawnBB = board.getBlackPawnBB();
			kingBB = board.getWhiteKingBB();
			piecesBB = board.getPiecesBB();//Changed from WhitePieces to Pieces
			//notBQBB = pieces & ~(board.getBlackBishopBB() | board.getBlackQueenBB);
			//notRQBB = pieces & ~(board.getBlackRookBB() | board.getBlackQueenBB);
			upCheckBeam = whiteUpCheckBeam;
			downCheckBeam = whiteDownCheckBeam;
			leftCheckBeam = whiteLeftCheckBeam;
			rightCheckBeam = whiteRightCheckBeam;
			upLeftCheckBeam = whiteUpLeftCheckBeam;
			upRightCheckBeam = whiteUpRightCheckBeam;
			downLeftCheckBeam = whiteDownLeftCheckBeam;
			downRightCheckBeam = whiteDownRightCheckBeam;
			openCheckBeams = whiteOpenCheckBeams;
			openCheckRays = whiteOpenCheckRays;
		}
		else {
			pawnBB = board.getWhitePawnBB();
			kingBB = board.getBlackKingBB();
			piecesBB = board.getPiecesBB();//Changed from BlackPieces to Pieces
			//notBQBB = pieces & ~(board.getWhiteBishopBB() | board.getWhiteQueenBB);
			//notRQBB = pieces & ~(board.getWhiteRookBB() | board.getWhiteQueenBB);
			upCheckBeam = blackUpCheckBeam;
			downCheckBeam = blackDownCheckBeam;
			leftCheckBeam = blackLeftCheckBeam;
			rightCheckBeam = blackRightCheckBeam;
			upLeftCheckBeam = blackUpLeftCheckBeam;
			upRightCheckBeam = blackUpRightCheckBeam;
			downLeftCheckBeam = blackDownLeftCheckBeam;
			downRightCheckBeam = blackDownRightCheckBeam;
			openCheckBeams = blackOpenCheckBeams;
			openCheckRays = blackOpenCheckRays;
		}
		//if (!isOnBB(piecesBB, upCheckBeam) & upCheckBeam != 0b0L) {
		if (Long.bitCount(piecesBB & upCheckBeam) == 1) {
			openCheckBeams[0] = upCheckBeam;
			openCheckRays[0] = downCheckBeamDIR.get(kingBB);
		}
		//else if (!isOnBB(piecesBB, downCheckBeam) & downCheckBeam != 0b0L) {
		else if (Long.bitCount(piecesBB & downCheckBeam) == 1) { 
			openCheckBeams[0] = downCheckBeam;
			openCheckRays[0] = upCheckBeamDIR.get(kingBB);
		}
		//else if (!isOnBB(piecesBB, leftCheckBeam) & leftCheckBeam != 0b0L) {
		else if (Long.bitCount(piecesBB & leftCheckBeam) == 1) { 
			openCheckBeams[0] = leftCheckBeam;
			openCheckRays[0] = rightCheckBeamDIR.get(kingBB);
		}
		//else if (!isOnBB(piecesBB, rightCheckBeam) & rightCheckBeam != 0b0L) {
		else if (Long.bitCount(piecesBB & rightCheckBeam) == 1) { 
			openCheckBeams[0] = rightCheckBeam;
			openCheckRays[0] = leftCheckBeamDIR.get(kingBB);
		}
		else {
			openCheckBeams[0] = 0b0L;
			openCheckRays[0] = 0b0L;
		}
		//if (!isOnBB(piecesBB, upLeftCheckBeam) & upLeftCheckBeam != 0b0L) {
		if (Long.bitCount(piecesBB & upLeftCheckBeam) == 1) { 
			openCheckBeams[1] = upLeftCheckBeam;
			openCheckRays[1] = downRightCheckBeamDIR.get(kingBB);
		}
		//else if (!isOnBB(piecesBB, upRightCheckBeam) & upRightCheckBeam != 0b0L) {
		else if (Long.bitCount(piecesBB & upRightCheckBeam) == 1) {  
			openCheckBeams[1] = upRightCheckBeam;
			openCheckRays[1] = downLeftCheckBeamDIR.get(kingBB);
		}
		//else if (!isOnBB(piecesBB, downLeftCheckBeam) & downLeftCheckBeam != 0b0L) {
		else if (Long.bitCount(piecesBB & downLeftCheckBeam) == 1) { 
			openCheckBeams[1] = downLeftCheckBeam;
			openCheckRays[1] = upRightCheckBeamDIR.get(kingBB);
		}
		//else if (!isOnBB(piecesBB, downRightCheckBeam) & downRightCheckBeam != 0b0L) {
		else if (Long.bitCount(piecesBB & downRightCheckBeam) == 1) { 	
			openCheckBeams[1] = downRightCheckBeam;
			openCheckRays[1] = upLeftCheckBeamDIR.get(kingBB);
		}
		else {
			openCheckBeams[1] = 0b0L;
			openCheckRays[1] = 0b0L;
		}
	}
	
	public void clearAllCheckBeams(boolean forWhite) {
		if (forWhite) {
			this.whiteUpCheckBeam = 0b0L;
			this.whiteDownCheckBeam = 0b0L;
			this.whiteLeftCheckBeam = 0b0L;
			this.whiteRightCheckBeam = 0b0L;
			this.whiteUpLeftCheckBeam = 0b0L;
			this.whiteUpRightCheckBeam = 0b0L;
			this.whiteDownLeftCheckBeam = 0b0L;
			this.whiteDownRightCheckBeam = 0b0L;
			this.whiteUpLeftCheckBeam = 0b0L;
			this.whiteUpRightCheckBeam = 0b0L;
			this.whiteDownLeftCheckBeam = 0b0L;
			this.whiteDownRightCheckBeam = 0b0L;
			this.whiteLeftEPCheckBeam = 0b0L;
			this.whiteRightEPCheckBeam = 0b0L;
			for (int i = 0; i <= 1; i++) {
				this.whiteOpenCheckBeams[i] = 0b0L;
				this.whiteOpenCheckRays[i] = 0b0L;
			}
		}
		else {
			this.blackUpCheckBeam = 0b0L;
			this.blackDownCheckBeam = 0b0L;
			this.blackLeftCheckBeam = 0b0L;
			this.blackRightCheckBeam = 0b0L;
			this.blackUpLeftCheckBeam = 0b0L;
			this.blackUpRightCheckBeam = 0b0L;
			this.blackDownLeftCheckBeam = 0b0L;
			this.blackDownRightCheckBeam = 0b0L;
			this.blackUpLeftCheckBeam = 0b0L;
			this.blackUpRightCheckBeam = 0b0L;
			this.blackDownLeftCheckBeam = 0b0L;
			this.blackDownRightCheckBeam = 0b0L;
			this.blackLeftEPCheckBeam = 0b0L;
			this.blackRightEPCheckBeam = 0b0L;
			for (int i = 0; i <= 1; i++) {
				this.whiteOpenCheckBeams[i] = 0b0L;
				this.whiteOpenCheckRays[i] = 0b0L;
			}
		}
	}
	
	public void setCheckBeamsAndRays(boolean forWhite) {
		clearAllCheckBeams(forWhite);
		setBishopCheckBeams(forWhite);
		setRookCheckBeams(forWhite);
		setOpenCheckBeamsAndRays(forWhite);
		setCheckMasks(forWhite);
	}
	
	public void printCheckBeams(){
		System.out.println("whiteUpCheckBeam: ");
		printBoard(whiteUpCheckBeam);
		System.out.println("whiteDownCheckBeam: ");
		printBoard(whiteDownCheckBeam);
		System.out.println("whiteLeftCheckBeam: ");
		printBoard(whiteLeftCheckBeam);
		System.out.println("whiteRightCheckBeam: ");
		printBoard(whiteRightCheckBeam);
		
		System.out.println("whiteUpLeftCheckBeam: ");
		printBoard(whiteUpLeftCheckBeam);
		System.out.println("whiteUpRightCheckBeam: ");
		printBoard(whiteUpRightCheckBeam);
		System.out.println("whiteDownLeftCheckBeam: ");
		printBoard(whiteDownLeftCheckBeam);
		System.out.println("whiteDownRightCheckBeam: ");
		printBoard(whiteDownRightCheckBeam);
		
		System.out.println("whiteOpenBeams: ");
		printBoard(whiteOpenCheckBeams[0]);
		printBoard(whiteOpenCheckBeams[1]);
		
		System.out.println("whiteOpenRays: ");
		printBoard(whiteOpenCheckRays[0]);
		printBoard(whiteOpenCheckRays[1]);
		
		System.out.println("blackUpCheckBeam: ");
		printBoard(blackUpCheckBeam);
		System.out.println("blackDownCheckBeam: ");
		printBoard(blackDownCheckBeam);
		System.out.println("blackLeftCheckBeam: ");
		printBoard(blackLeftCheckBeam);
		System.out.println("blackRightCheckBeam: ");
		printBoard(blackRightCheckBeam);
		
		System.out.println("blackUpLeftCheckBeam: ");
		printBoard(blackUpLeftCheckBeam);
		System.out.println("blackUpRightCheckBeam: ");
		printBoard(blackUpRightCheckBeam);
		System.out.println("blackDownLeftCheckBeam: ");
		printBoard(blackDownLeftCheckBeam);
		System.out.println("blackDownRightCheckBeam: ");
		printBoard(blackDownRightCheckBeam);
		
		System.out.println("blackOpenBeams: ");
		printBoard(blackOpenCheckBeams[0]);
		printBoard(blackOpenCheckBeams[1]);
		
		System.out.println("blackOpenRays: ");
		printBoard(blackOpenCheckRays[0]);
		printBoard(blackOpenCheckRays[1]);

	}
	
	//public String checkBeamsToString(){
		//return String.format("whiteUpCheckBeam: 
	//}
	
	public long getChecks(boolean forWhite) {
		long checks = 0b0L;
		long alliedPiecesBB;
		long blockedUpCheckBeam;
		long blockedDownCheckBeam;
		long blockedLeftCheckBeam;
		long blockedRightCheckBeam;
		long blockedUpLeftCheckBeam;
		long blockedUpRightCheckBeam;
		long blockedDownLeftCheckBeam;
		long blockedDownRightCheckBeam;
		if (forWhite) {
			alliedPiecesBB = board.getWhitePiecesBB();
			blockedUpCheckBeam = whiteUpCheckBeam;
			blockedDownCheckBeam = whiteDownCheckBeam;
			blockedLeftCheckBeam = whiteLeftCheckBeam;
			blockedRightCheckBeam = whiteRightCheckBeam;	
			blockedUpLeftCheckBeam = whiteUpLeftCheckBeam;
			blockedUpRightCheckBeam = whiteUpRightCheckBeam;
			blockedDownLeftCheckBeam = whiteDownLeftCheckBeam;
			blockedDownRightCheckBeam = whiteDownRightCheckBeam;	
		}
		else {
			alliedPiecesBB = board.getBlackPiecesBB();
			blockedUpCheckBeam = blackUpCheckBeam;
			blockedDownCheckBeam = blackDownCheckBeam;
			blockedLeftCheckBeam = blackLeftCheckBeam;
			blockedRightCheckBeam = blackRightCheckBeam;
			blockedUpLeftCheckBeam = blackUpLeftCheckBeam;
			blockedUpRightCheckBeam = blackUpRightCheckBeam;
			blockedDownLeftCheckBeam = blackDownLeftCheckBeam;
			blockedDownRightCheckBeam = blackDownRightCheckBeam;			
		}
		if (blockedUpCheckBeam != 0b0L & !isOnBB(alliedPiecesBB, blockedUpCheckBeam)) {
			checks = checks | 0b10000000L;
		}
		else if (blockedDownCheckBeam != 0b0L & !isOnBB(alliedPiecesBB, blockedDownCheckBeam)) {
			checks = checks | 0b01000000L;
		}
		else if (blockedLeftCheckBeam != 0b0L & !isOnBB(alliedPiecesBB, blockedLeftCheckBeam)) {
			checks = checks | 0b00100000L;
		}
		else if (blockedRightCheckBeam != 0b0L & !isOnBB(alliedPiecesBB, blockedRightCheckBeam)) {
			checks = checks | 0b00010000L;
		}
		else {
			checks = checks;
		}
		if (blockedUpLeftCheckBeam != 0b0L & !isOnBB(alliedPiecesBB, blockedUpLeftCheckBeam)) {
			checks = checks | 0b00001000L;
		}
		else if (blockedUpRightCheckBeam != 0b0L & !isOnBB(alliedPiecesBB, blockedUpRightCheckBeam)) {
			checks = checks | 0b00000100L;
		}
		else if (blockedDownLeftCheckBeam != 0b0L & !isOnBB(alliedPiecesBB, blockedDownLeftCheckBeam)) {
			checks = checks | 0b00000010L;
		}
		else if (blockedDownRightCheckBeam != 0b0L & !isOnBB(alliedPiecesBB, blockedDownRightCheckBeam)) {
			checks = checks | 0b00000001L;
		}
		else {
			checks = checks;
		}
		return checks;
	}
	
	public void printAttackedSquares(){
		System.out.println("Whites Attacked Squares: ");
		printBoard(whiteAttackedSquares);
		System.out.println("Whites Knight Attacked Squares: ");
		printBoard(whiteKnightAttacks);
		System.out.println("Whites Pawn Attacked Squares: ");
		printBoard(whitePawnAttacks);
		System.out.println("Blacks Attacked Squares: ");
		printBoard(blackAttackedSquares);
		System.out.println("Blacks Knight Attacked Squares: ");
		printBoard(blackKnightAttacks);
		System.out.println("Blacks Pawn Attacked Squares: ");
		printBoard(blackPawnAttacks);
	}
	
	public long removeBorder(long moveBB, long position) {
		long border = 0b0L;
		if (!isOnBB(position, ranks[0])) {	
			border = border | ranks[0];
		}
		if (!isOnBB(position, ranks[7])) {	
			border = border | ranks[7];
		}
		if (!isOnBB(position, files[0])) {	
			border = border | files[0];
		}
		if (!isOnBB(position, files[7])) {	
			border = border | files[7];
		}
		return moveBB & ~border;
	}
	
	public boolean isOnBB (long BB1, long BB2) {
		if ((BB1 & BB2) != 0L) {
			return true;
		}
		return false;	
	}
	
	public void removeEPTargets(boolean forWhite) {
		if (forWhite) {
			board.setWhiteEPTargetBB(0b0L);
		}
		else {
			board.setBlackEPTargetBB(0b0L);
		}
	}	
	
	//Methods for castling.
	
	public boolean checkCastlingAvaliable(boolean forWhite, char side) {
		//Initialize castling conditions.
		boolean castlePathEmpty;
		boolean castleThroughCheck;
		long kingsideCastleMaskBlock;
		long queensideCastleMaskBlock;
		long kingsideCastleMaskCheck;
		long queensideCastleMaskCheck;
		long attackedSquares;
		boolean KRHasMoved;
		boolean QRHasMoved;
		boolean rookHasMoved;
		boolean kingHasMoved;
		
		//Set castling conditions for colour.
		if (forWhite) {
			kingsideCastleMaskBlock = whiteKingsideCastleMaskBlock;
			queensideCastleMaskBlock = whiteQueensideCastleMaskBlock;
			kingsideCastleMaskCheck = whiteKingsideCastleMaskCheck;
			queensideCastleMaskCheck = whiteQueensideCastleMaskCheck;
			attackedSquares = blackAttackedSquares;
			KRHasMoved = board.getWhiteKRHasMoved();
			QRHasMoved = board.getWhiteQRHasMoved();
			kingHasMoved = board.getWhiteKingHasMoved();
		}
		else { 
			kingsideCastleMaskBlock = blackKingsideCastleMaskBlock;
			queensideCastleMaskBlock = blackQueensideCastleMaskBlock;
			kingsideCastleMaskCheck = blackKingsideCastleMaskCheck;
			queensideCastleMaskCheck = blackQueensideCastleMaskCheck;
			attackedSquares = whiteAttackedSquares;
			KRHasMoved = board.getBlackKRHasMoved();
			QRHasMoved = board.getBlackQRHasMoved();
			kingHasMoved = board.getBlackKingHasMoved();
		}
		
		//King side.
		if (side == 'K') {
			//Check if castling path is empty.
			if (!isOnBB(board.getPiecesBB(), kingsideCastleMaskBlock)) {
				castlePathEmpty = true;
			}
			else {
				return false;
			}
			
			//Check if castling through check.
			if (!isOnBB(attackedSquares, kingsideCastleMaskCheck)) {
				castleThroughCheck = false;
			}
			else {
				return false;
			}
			
			//Check if rook has moved.
			rookHasMoved = KRHasMoved;
		}
		//Queen side.
		else {
			//Check if castling path is empty.
			if (!isOnBB(board.getPiecesBB(), queensideCastleMaskBlock)) {
				castlePathEmpty = true;
			}
			else {
				return false;
			}
			
			//Check if castling through check.
			if (!isOnBB(attackedSquares, queensideCastleMaskCheck)) {
				castleThroughCheck = false;
			}
			else {
				return false;
			}
			
			//Check if rook has moved.
			rookHasMoved = QRHasMoved;
		}
		if((!rookHasMoved) & (!kingHasMoved) & castlePathEmpty & (!castleThroughCheck)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Methods for check
	public void setWhitesAttackedSquares() {
		long attackedSquares = 0b0L;
		long knightAttacks = 0b0L;
		long pawnAttacks = 0b0L;
		long blockerBB;
		//String BPPid;
		long PPid;
		for (long position: squareDIR) {
			if (isOnBB(position, board.getWhitePawnBB())) {
				PPid = genPPid(position, 0b1L);
				attackedSquares = attackedSquares | (moveBBDIRWPC.get(PPid) & ~(position << 8 | position << 16));
				pawnAttacks = pawnAttacks | (moveBBDIRWPC.get(PPid) & ~(position << 8 | position << 16));
			}
			else if (isOnBB(position, board.getWhiteKnightBB())) {
				attackedSquares = attackedSquares | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b11L);
				knightAttacks = knightAttacks | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b11L);
			}
			else if (isOnBB(position, board.getWhiteBishopBB())) {
				attackedSquares = attackedSquares | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b100L);
			}
			else if (isOnBB(position, board.getWhiteRookBB())) {
				attackedSquares = attackedSquares | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b101L);
			}
			else if (isOnBB(position, board.getWhiteQueenBB())) {
				attackedSquares = attackedSquares | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b110L);
			}
			else if (isOnBB(position, board.getWhiteKingBB())) {
				attackedSquares = attackedSquares | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b111L);
			}
		}
		this.whitePawnAttacks = pawnAttacks;
		this.whiteKnightAttacks = knightAttacks;
		this.whiteAttackedSquares = attackedSquares;
	}
	
	public void setBlacksAttackedSquares() {
		long attackedSquares = 0b0L;
		long knightAttacks = 0b0L;
		long pawnAttacks = 0b0L;
		long blockerBB;
		//String BPPid;
		long PPid;
		for (long position: squareDIR) {
			if (isOnBB(position, board.getBlackPawnBB())) {
				PPid = genPPid(position, 0b10L);
				attackedSquares = attackedSquares | (moveBBDIRWPC.get(PPid) & ~(position >>> 8 | position >>> 16));
				pawnAttacks = pawnAttacks | (moveBBDIRWPC.get(PPid) & ~(position >>> 8 | position >>> 16));
			}
			else if (isOnBB(position, board.getBlackKnightBB())) {
				attackedSquares = attackedSquares | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b11L);
				knightAttacks = knightAttacks | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b11L);
			}
			else if (isOnBB(position, board.getBlackBishopBB())) {
				attackedSquares = attackedSquares | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b100L);
			}
			else if (isOnBB(position, board.getBlackRookBB())) {
				attackedSquares = attackedSquares | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b101L);
			}
			else if (isOnBB(position, board.getBlackQueenBB())) {
				attackedSquares = attackedSquares | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b110L);
			}
			else if (isOnBB(position, board.getBlackKingBB())) {
				attackedSquares = attackedSquares | getPsuedoLegalWithAllyCapturesMoveBB(position, 0b111L);
			}
		}
		this.blackPawnAttacks = pawnAttacks;
		this.blackKnightAttacks = knightAttacks;
		this.blackAttackedSquares = attackedSquares;
	}
	
    public void startTurn(boolean forWhite) {
        setWhitesAttackedSquares();
        setBlacksAttackedSquares();
        removeEPTargets(forWhite);
        setCheckBeamsAndRays(forWhite);
    }	
	
	//Getters and Setters
	public Board getBoard() {
		return this.board;
	}
	
	public long[] getSquareDIR() {
		return squareDIR;
	}
	
	public long getSquareDIR(int i) {
		return squareDIR[i];
	}
	
	public long getRank(int i) {
		return ranks[i];
	}
	
	public long getWhiteLeftCheckBeam() {
		return whiteLeftCheckBeam;
	}
	
	public long getWhiteRightCheckBeam() {
		return whiteRightCheckBeam;
	}

	//******FOR TESTING********

	public void printAllMoveBB(long piece) {
		long PPid;
		long moveBB;
		for (long square: squareDIR) {
				PPid = genPPid(square, piece);
		}
	}
	
	public void printMoveBBDIRWPC() {
		for (long moveBB: moveBBDIRWPC.values()) {
			if ((moveBB & ~ranks[7]) == 0b0l && moveBB != 0b0L) {
				printBoard(moveBB); 
			}
			if ((moveBB & ~ranks[0]) == 0b0l && moveBB != 0b0L) {
				printBoard(moveBB); 
			}
		}
	}
	
    private static String toFullBinaryString(long value) {
        return String.format("%64s", Long.toBinaryString(value)).replace(' ', '0');
    }
	
    private static String addSpacesBetweenCharacters(String input) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            result.append(input.charAt(i)).append(' '); // Add space after each character
        }

        return result.toString().trim(); // Remove trailing space
    }

    public static void printBoard(long BB) {
        String bString = toFullBinaryString(BB);
        String rank8 = addSpacesBetweenCharacters(bString.substring(0, 8));
        String rank7 = addSpacesBetweenCharacters(bString.substring(8, 16));
        String rank6 = addSpacesBetweenCharacters(bString.substring(16, 24));
        String rank5 = addSpacesBetweenCharacters(bString.substring(24, 32));
        String rank4 = addSpacesBetweenCharacters(bString.substring(32, 40));
        String rank3 = addSpacesBetweenCharacters(bString.substring(40, 48));
        String rank2 = addSpacesBetweenCharacters(bString.substring(48, 56));
        String rank1 = addSpacesBetweenCharacters(bString.substring(56, 64));
        System.out.printf("%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n****************\n", rank8, rank7, rank6, rank5, rank4, rank3, rank2, rank1);
    }
}