package com.gavin.chessengine;

import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.nio.file.*;
import java.io.File;

public class Board {
	
	//Bit boards
	private long piecesBB;
	private long whitePiecesBB;
	private long blackPiecesBB;
	private long whiteEPTargetBB;
	private long blackEPTargetBB;
	private long whitePawnBB;
	private long blackPawnBB;
	private long whiteKnightBB;
	private long blackKnightBB;
	private long whiteBishopBB;
	private long blackBishopBB;
	private long whiteRookBB;
	private long blackRookBB;
	private long whiteQueenBB;
	private long blackQueenBB;
	private long whiteKingBB;
	private long blackKingBB;
	
	private boolean whiteKRHasMoved;
	private boolean whiteQRHasMoved;
	private boolean blackKRHasMoved;
	private boolean blackQRHasMoved;
	private boolean whiteKingHasMoved;
	private boolean blackKingHasMoved;
	
	//Directories
	/*static HashMap<Long, Long> moveBBDIR;
	static HashMap<String, Long> blockedWPawnMoveDIR;
	static HashMap<String, Long> blockedBPawnMoveDIR;
	static HashMap<String, Long> blockedBishopMoveDIR;
	static HashMap<String, Long> blockedRookMoveDIR;
	static HashMap<String, Long> blockedQueenMoveDIR;
	//static HashMap<String, Long> blockedKingMoveDIR;
	*/
	static long[] squareDIR;
	
	static HashMap<Long, Long> moveDIR;
	
	
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
	
	public Board() {
		
		//g3 
		//setBlackEPTargetBB(0b100000000000000000L);
		
		//Load Directories.
		loadSquareDIR();
		
		//Initialize Rook Has Moved.
		whiteKRHasMoved = false;
		whiteQRHasMoved = false;
		blackKRHasMoved = false;
		blackQRHasMoved = false;
		
		//Initialize King Has Moved.
		whiteKingHasMoved = false;
		blackKingHasMoved = false;
	}
	
	/*public static void main(String[] args) {
		Board board = new Board();
		board.loadSquareDIR();
		//board.loadMoveDIR();
		//System.out.println(moveDIR);
		//System.out.println("TEST");
		//printBoard(0b0000000000000000000000000001000000000000000000000000000000000000L);
		//printBoard(moveDIR.get(board.GenPPid(0b0000000000000000000000000001000000000000000000000000000000000000L, 0b110L)));
	}*/

	public long getSquare(int i) {
		return squareDIR[i];
	}
	
	public void loadSquareDIR() {
		long a8 = 0b1000000000000000000000000000000000000000000000000000000000000000L;
		squareDIR = new long[64];
		for (int i = 0; i <= 63; i++) {
			squareDIR[i] = a8 >>> i;
		}
	}


	/*public void loadBlockedKingMoveDIR() {
		blockedKingMoveDIR = new HashMap<>();
		File directory = new File("BlockedKingMoveDIR.txt");
		try {
			Scanner fileInput = new Scanner(directory);
			while (fileInput.hasNext()) {
				blockedKingMoveDIR.put(fileInput.next(), fileInput.nextLong());	
			}
			fileInput.close();
		}
		catch (FileNotFoundException err) {
		}
	}*/
	
	/*public void loadMoveListDIR() {
		moveListDIR = new HashMap<>();
		long PPid;
		long moveBB;
		File directory = new File("moveDIR.txt");
		try {
			Scanner fileInput = new Scanner(directory);
			while (fileInput.hasNext()) {
				PPid = fileInput.nextLong();
				MoveBB = fileInput.nextLong();
				moveListDIR.put(PPid, MoveBB);	
			}
			fileInput.close();
		}
		catch (FileNotFoundException err) {
		}
	}*/
	
	/*public ArrayList<long> moveBBtoList(long moveBB) {
		long SquareBB;
		Move move;
		ArrayList<Move> moveList = new ArrayList<Move>();
		for (long square: squareDIR) {
			if (isOnMask(square, moveBB)) {
				move = new Move(
				moveList.add(moveBB);
			}
		}
		return moveList;
	}*/
	
	public boolean isOnMask(long squareBB, long maskBB) {
		if ((squareBB & maskBB) != 0L) {
			return true;
		}
		return false;
	}
	
	//Bit board getters/setters
	public long getPiecesBB() {
		return piecesBB;
	}
	public long getWhitePiecesBB() {
		return whitePiecesBB;
	}
	public long getBlackPiecesBB() {
		return blackPiecesBB;
	}
	
	public long getWhiteEPTargetBB() {
		return whiteEPTargetBB;
	}
	public long getBlackEPTargetBB() {
		return blackEPTargetBB;
	}
	
	public long getWhitePawnBB() {
		return whitePawnBB;
	}
	
	public long getBlackPawnBB() {
		return blackPawnBB;
	}
	
	public long getWhiteKnightBB() {
		return whiteKnightBB;
	}
	
	public long getBlackKnightBB() {
		return blackKnightBB;
	}
	
	public long getWhiteBishopBB() {
		return whiteBishopBB;
	}
	
	public long getBlackBishopBB() {
		return blackBishopBB;
	}
	
	public long getWhiteRookBB() {
		return whiteRookBB;
	}
	
	public long getBlackRookBB() {
		return blackRookBB;
	}
	
	public long getWhiteQueenBB() {
		return whiteQueenBB;
	}
	
	public long getBlackQueenBB() {
		return blackQueenBB;
	}
	
	public long getWhiteKingBB() {
		return whiteKingBB;
	}
	
	public long getBlackKingBB() {
		return blackKingBB;
	}
	
	public void setPiecesBB(long piecesBB) {
		this.piecesBB = piecesBB;
	}
	
	public void setWhitePiecesBB(long whitePiecesBB) {
		this.whitePiecesBB = whitePiecesBB;
	}
	
	public void setBlackPiecesBB(long blackPiecesBB) {
		this.blackPiecesBB = blackPiecesBB;
	}
	
	public void setWhiteEPTargetBB(long whiteEPTargetBB) {
		this.whiteEPTargetBB = whiteEPTargetBB;
	}
	
	public void setBlackEPTargetBB(long blackEPTargetBB) {
		this.blackEPTargetBB = blackEPTargetBB;
	}
	
	public void setWhitePawnBB(long whitePawnBB) {
		this.whitePawnBB = whitePawnBB;
	}
	
	public void setBlackPawnBB(long blackPawnBB) {
		this.blackPawnBB = blackPawnBB;
	}
	
	public void setWhiteKnightBB(long whiteKnightBB) {
		this.whiteKnightBB = whiteKnightBB;
	}
	
	public void setBlackKnightBB(long blackKnightBB) {
		this.blackKnightBB = blackKnightBB;
	}
	
	public void setWhiteBishopBB(long whiteBishopBB) {
		this.whiteBishopBB = whiteBishopBB;
	}
	
	public void setBlackBishopBB(long blackBishopBB) {
		this.blackBishopBB = blackBishopBB;
	}
	
	public void setWhiteRookBB(long whiteRookBB) {
		this.whiteRookBB = whiteRookBB;
	}
	
	public void setBlackRookBB(long blackRookBB) {
		//printBoard(blackRookBB);
		this.blackRookBB = blackRookBB;
	}
	
	public void setWhiteQueenBB(long whiteQueenBB) {
		this.whiteQueenBB = whiteQueenBB;
	}
	
	public void setBlackQueenBB(long blackQueenBB) {
		this.blackQueenBB = blackQueenBB;
	}
	
	public void setWhiteKingBB(long whiteKingBB) {
		this.whiteKingBB = whiteKingBB;
	}
	
	public void setBlackKingBB(long blackKingBB) {
		this.blackKingBB = blackKingBB;
	}
	
	//Other getters and setters.
	public boolean getWhiteKRHasMoved() {
		return whiteKRHasMoved;
	}
	
	public boolean getWhiteQRHasMoved() {
		return whiteQRHasMoved;
	}
	
	public boolean getBlackKRHasMoved() {
		return blackKRHasMoved;
	}
	
	public boolean getBlackQRHasMoved() {
		return blackQRHasMoved;
	}
	
	public boolean getWhiteKingHasMoved() {
		return whiteKingHasMoved;
	}	
	
	public boolean getBlackKingHasMoved() {
		return blackKingHasMoved;
	}

	public void setWhiteKRHasMoved(boolean whiteKRHasMoved) {
		this.whiteKRHasMoved = whiteKRHasMoved;
	}
	
	public void setWhiteQRHasMoved(boolean whiteQRHasMoved) {
		this.whiteQRHasMoved = whiteQRHasMoved;
	}
	public void setBlackKRHasMoved(boolean blackKRHasMoved) {
		this.blackKRHasMoved = blackKRHasMoved;
	}
	
	public void setBlackQRHasMoved(boolean blackQRHasMoved) {
		this.blackQRHasMoved = blackQRHasMoved;
	}
	
	public void setWhiteKingHasMoved(boolean whiteKingHasMoved) {
		this.whiteKingHasMoved = whiteKingHasMoved;
	}
	
	public void setBlackKingHasMoved(boolean blackKingHasMoved) {
		this.blackKingHasMoved = blackKingHasMoved;
	}
	
	public void SetBBfromFEN(String FEN) {
		Scanner FENScanner = new Scanner(FEN);
		FENScanner.useDelimiter("");
		String piece;	
		int skip;
		long[] arrayBB = new long[12];
		for (int i = 0; i < 64; i++) {
			try {
			piece = FENScanner.next();
			//System.out.print("Piece: " + piece);
			}
			catch(Exception e) {
				System.out.print("break");
				break;
			}
			if (piece.matches("\\d")) {
				//System.out.print("DING");
				skip = Integer.parseInt(piece);
				i = i + skip - 1;					
			}
			else if (piece.equals("/")) {
				i--;
				continue;
			}
			else {
				switch(piece) {
				case "P":
					this.whitePawnBB = whitePawnBB | squareDIR[i];
					break;
				case "p":
					this.blackPawnBB = blackPawnBB | squareDIR[i];
					break;
				case "N":
					whiteKnightBB = whiteKnightBB | squareDIR[i];
					break;
				case "n":
					this.blackKnightBB = blackKnightBB | squareDIR[i];
					break;
				case "B":
					this.whiteBishopBB = whiteBishopBB | squareDIR[i];
					break;
				case "b":
					this.blackBishopBB = blackBishopBB | squareDIR[i];
					break;
				case "R":
					this.whiteRookBB = whiteRookBB | squareDIR[i];
					break;
				case "r":
					this.blackRookBB = blackRookBB | squareDIR[i];
					break;
				case "Q":
					this.whiteQueenBB = whiteQueenBB | squareDIR[i];
					break;
				case "q":
					this.blackQueenBB = blackQueenBB | squareDIR[i];
					break;			
				case "K":
					this.whiteKingBB = whiteKingBB | squareDIR[i];
					break;
				case "k":
					this.blackKingBB = blackKingBB | squareDIR[i];
					break;
				default:
				}
				whitePiecesBB = whitePawnBB | whiteKnightBB | whiteBishopBB | whiteRookBB | whiteQueenBB | whiteKingBB;
				blackPiecesBB = blackPawnBB | blackKnightBB | blackBishopBB | blackRookBB | blackQueenBB | blackKingBB;
				piecesBB = whitePiecesBB | blackPiecesBB;
			}
		}
	}
	
	public String boardToFen() {
		long start = 0b1000000000000000000000000000000000000000000000000000000000000000L;
		StringBuilder fenString = new StringBuilder(); // Use StringBuilder for efficiency
		int emptyCounter = 0; // Track empty squares in a rank

		for (int i = 0; i < 64; i++) {
			long checking = start >>> i;
			char fenChar = '\0';

			// Determine the piece on the current square
			if (isOnMask(checking, whitePawnBB)) {
				fenChar = 'P';
			} else if (isOnMask(checking, blackPawnBB)) {
				fenChar = 'p';
			} else if (isOnMask(checking, whiteKnightBB)) {
				fenChar = 'N';
			} else if (isOnMask(checking, blackKnightBB)) {
				fenChar = 'n';
			} else if (isOnMask(checking, whiteBishopBB)) {
				fenChar = 'B';
			} else if (isOnMask(checking, blackBishopBB)) {
				fenChar = 'b';
			} else if (isOnMask(checking, whiteRookBB)) {
				fenChar = 'R';
			} else if (isOnMask(checking, blackRookBB)) {
				fenChar = 'r';
			} else if (isOnMask(checking, whiteQueenBB)) {
				fenChar = 'Q';
			} else if (isOnMask(checking, blackQueenBB)) {
				fenChar = 'q';
			} else if (isOnMask(checking, whiteKingBB)) {
				fenChar = 'K';
			} else if (isOnMask(checking, blackKingBB)) {
				fenChar = 'k';
			} else {
				// Empty square
				emptyCounter++;
				// Skip appending a piece and move to the next square
				if ((i + 1) % 8 != 0) continue;
			}

			// Append empty square count if applicable
			if (emptyCounter > 0) {
				fenString.append(emptyCounter);
				emptyCounter = 0; // Reset counter
			}

			// Append the piece if not empty
			if (fenChar != '\0') {
				fenString.append(fenChar);
			}

			// End of rank: append '/' if not the last rank
			if ((i + 1) % 8 == 0 && i != 63) {
				fenString.append('/');
			}
		}

		return fenString.toString();
	}

	
	public void upDatePiecesBB() {
		this.whitePiecesBB = this.whitePawnBB |this.whiteKnightBB | this.whiteBishopBB | this.whiteRookBB | this.whiteQueenBB | this.whiteKingBB;
		this.blackPiecesBB = this.blackPawnBB | this.blackKnightBB | this.blackBishopBB | this.blackRookBB | this.blackQueenBB | this.blackKingBB;
		this.piecesBB = this.whitePiecesBB | this.blackPiecesBB;
	}

	//{0b1L, 0b10L, 0b11L, 0b100L, 0b101L, 0b110L, 0b111L}
	public long getPieceOnSquare(long square) {
		if (isOnMask(square, whitePawnBB)) {
			return 0b1L;
		}
		else if (isOnMask(square, blackPawnBB)) {
			return 0b10L;
		}
		else if (isOnMask(square, whiteKnightBB) | isOnMask(square, blackKnightBB)) {
			return 0b11L;
		}
		else if (isOnMask(square, whiteBishopBB) | isOnMask(square, blackBishopBB)) {
			return 0b100L;
		}
		else if (isOnMask(square, whiteRookBB) | isOnMask(square, blackRookBB)) {
			return 0b101L;
		}
		else if (isOnMask(square, whiteQueenBB) | isOnMask(square, blackQueenBB)) {
			return 0b110L;
		}
		else if (isOnMask(square, whiteKingBB) | isOnMask(square, blackKingBB)) {
			return 0b111L;
		}
		else if (isOnMask(square, whiteEPTargetBB) | isOnMask(square, blackEPTargetBB)) {
			return 0b1000L;
		}
		else {
			return 0b0L;
		}
	}
	//Make Move Methode
	
	//******FOR TESTING******\\
	
	public Long GenPPid(long position, long piece) {
		Long PPid = (3 * position) + (5 * piece);
		return PPid;
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

	public void printBoard() {
		System.out.println("Pawns: ");
		printBoard(whitePawnBB);
		printBoard(blackPawnBB);
		System.out.println("Knights: ");
		printBoard(whiteKnightBB);
		printBoard(blackKnightBB);
		System.out.println("Bishops: ");
		printBoard(whiteBishopBB);
		printBoard(blackBishopBB);
		System.out.println("Rooks: ");
		printBoard(whiteRookBB);
		printBoard(blackRookBB);
		System.out.println("Queens: ");
		printBoard(whiteQueenBB);
		printBoard(blackQueenBB);
		System.out.println("Kings: ");
		printBoard(whiteKingBB);
		printBoard(blackKingBB);
		System.out.println("White Pieces: ");
		printBoard(whitePiecesBB);
		System.out.println("Black Pieces: ");
		printBoard(blackPiecesBB);
		System.out.println("Pieces: ");
		printBoard(piecesBB);
		System.out.println("EP Targets: ");
		printBoard(whiteEPTargetBB);
		printBoard(blackEPTargetBB);
		System.out.println("FEN: " + boardToFen());
		
	}
	
	public String boardToString() {
		return String.format("Pawns:%n%s%n%s%nKnights:%n%s%n%s%nBishops:%n%s%n%s%nRooks:%n%s%n%s%nQueens:%n%s%n%s%nKings:%n%s%n%s%nWhite Pieces:%n%s%nBlack Pieces:%n%s%nPieces:%n%s%nEP Targets:%n%s%n%s%n", boardToString(whitePawnBB), boardToString(blackPawnBB), boardToString(whiteKnightBB), boardToString(blackKnightBB), boardToString(whiteBishopBB), boardToString(blackBishopBB), boardToString(whiteRookBB), boardToString(blackRookBB), boardToString(whiteQueenBB), boardToString(blackQueenBB), boardToString(whiteKingBB), boardToString(blackKingBB), boardToString(whitePiecesBB), boardToString(blackPiecesBB), boardToString(piecesBB), boardToString(whiteEPTargetBB), boardToString(blackEPTargetBB));
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
	
	  public static String boardToString(long BB) {
        String bString = toFullBinaryString(BB);
        String rank8 = addSpacesBetweenCharacters(bString.substring(0, 8));
        String rank7 = addSpacesBetweenCharacters(bString.substring(8, 16));
        String rank6 = addSpacesBetweenCharacters(bString.substring(16, 24));
        String rank5 = addSpacesBetweenCharacters(bString.substring(24, 32));
        String rank4 = addSpacesBetweenCharacters(bString.substring(32, 40));
        String rank3 = addSpacesBetweenCharacters(bString.substring(40, 48));
        String rank2 = addSpacesBetweenCharacters(bString.substring(48, 56));
        String rank1 = addSpacesBetweenCharacters(bString.substring(56, 64));
        return String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n****************\n", rank8, rank7, rank6, rank5, rank4, rank3, rank2, rank1);
    }
}
