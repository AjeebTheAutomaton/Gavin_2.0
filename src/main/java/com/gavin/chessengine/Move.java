package com.gavin.chessengine;

class Move {
	
	private long on;
	private long to;
	private long movingPiece;
	private boolean forWhite;
	private boolean isCheck;
	private int priority;
	
	private boolean whiteKingHasMovedReturn;
	private boolean whiteKRHasMovedReturn;
	private boolean whiteQRHasMovedReturn;
	private boolean blackKingHasMovedReturn;
	private boolean blackKRHasMovedReturn;
	private boolean blackQRHasMovedReturn;
	
	public long whiteKRPosition = 0b1L;
	public long whiteQRPosition = 0b10000000L;
	public long blackQRPosition = 0b1000000000000000000000000000000000000000000000000000000000000000L;
	public long blackKRPosition = 0b0000000100000000000000000000000000000000000000000000000000000000L;
	
	private long[] squareDIR;
	
	public void loadSquareDIR() {
		long a8 = 0b1000000000000000000000000000000000000000000000000000000000000000L;
		squareDIR = new long[64];
		for (int i = 0; i <= 63; i++) {
			squareDIR[i] = a8 >>> i;
		}
	}
	
	public long getSquareDIR(int i) {
		return squareDIR[i];
	}

	//Square, Rank, File DIR:
	static final long[] ranks = {0b0000000000000000000000000000000000000000000000000000000011111111L,
							0b0000000000000000000000000000000000000000000000001111111100000000L,
							0b0000000000000000000000000000000000000000111111110000000000000000L,
							0b0000000000000000000000000000000011111111000000000000000000000000L,
							0b0000000000000000000000001111111100000000000000000000000000000000L,
							0b0000000000000000111111110000000000000000000000000000000000000000L,
							0b0000000011111111000000000000000000000000000000000000000000000000L,
							0b1111111100000000000000000000000000000000000000000000000000000000L};	
	static final long[] files = {0b1000000010000000100000001000000010000000100000001000000010000000L,
							0b0100000001000000010000000100000001000000010000000100000001000000L,
							0b0010000000100000001000000010000000100000001000000010000000100000L,
							0b0001000000010000000100000001000000010000000100000001000000010000L,
							0b0000100000001000000010000000100000001000000010000000100000001000L,
							0b0000010000000100000001000000010000000100000001000000010000000100L,
							0b0000001000000010000000100000001000000010000000100000001000000010L,
							0b0000000100000001000000010000000100000001000000010000000100000001L};
	
	public String[] coordinates = {
		"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
		"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
		"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
		"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
		"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
		"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
		"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
		"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
	};
	//Constructor for Null move
	public Move() {
		this.on = 0b0L;
	}	
	
	//Colour only Constructor
	public Move(boolean forWhite, boolean isCheck, int priority) {
		this.forWhite = forWhite;
	}

	public void addCheckPriority() {
		this.priority += 1000;
	}
	
	public Move(long on, long to, long movingPiece, boolean forWhite, boolean isCheck ,int priority) {
		this.on = on;
		this.to = to;
		this.movingPiece = movingPiece;
		this.forWhite = forWhite;
		this.isCheck = isCheck;
		this.priority = priority;
	}
	
	public long getOn() {
		return on;
	}
	
	public long getTo() {
		return to;
	}
	
	public long getMovingPiece() {
		return movingPiece;
	}
	
	public boolean getForWhite() {
		return forWhite;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(boolean isCheck) {
		this.isCheck = isCheck;
		if (isCheck) {
			addCheckPriority();
		}
	}
	
	public boolean getWhiteKRHasMovedReturn() {
		return whiteKRHasMovedReturn;
	}
	
	public boolean getWhiteQRHasMovedReturn() {
		return whiteQRHasMovedReturn;
	}
	
	public boolean getWhiteKingHasMovedReturn() {
		return whiteKingHasMovedReturn;
	}
	
	public boolean getBlackKRHasMovedReturn() {
		return blackKRHasMovedReturn;
	}
	
	public boolean getBlackQRHasMovedReturn() {
		return blackQRHasMovedReturn;
	}
	
	public boolean getBlackKingHasMovedReturn() {
		return blackKingHasMovedReturn;
	}
	
	public void setWhiteKRHasMovedReturn(boolean whiteKRHasMovedReturn) {
		this.whiteKRHasMovedReturn = whiteKRHasMovedReturn;
	}
	
	public void setWhiteQRHasMovedReturn(boolean whiteQRHasMovedReturn) {
		this.whiteQRHasMovedReturn = whiteQRHasMovedReturn;
	}
	
	public void setWhiteKingHasMovedReturn(boolean whiteKingHasMovedReturn) {
		this.whiteKingHasMovedReturn = whiteKingHasMovedReturn;
	}
	
	public void setBlackKRHasMovedReturn(boolean blackKRHasMovedReturn) {
		this.blackKRHasMovedReturn = blackKRHasMovedReturn;
	}
	
	public void setBlackQRHasMovedReturn(boolean blackQRHasMovedReturn) {
		this.blackQRHasMovedReturn = blackQRHasMovedReturn;
	}
	
	public void setBlackKingHasMovedReturn(boolean blackKingHasMovedReturn) {
		this.blackKingHasMovedReturn = blackKingHasMovedReturn;
	}
	
	//{0b1L, 0b10L, 0b11L, 0b100L, 0b101L, 0b110L, 0b111L};	
	public void makeMoveWithoutUpdate(Board board) {

		long pieceBB;
		
		this.whiteKingHasMovedReturn = board.getWhiteKingHasMoved();
        this.whiteKRHasMovedReturn = board.getWhiteKRHasMoved();
		this.whiteQRHasMovedReturn = board.getWhiteQRHasMoved();
		
		this.blackKingHasMovedReturn = board.getBlackKingHasMoved();
		this.blackKRHasMovedReturn = board.getBlackKRHasMoved();
		this.blackQRHasMovedReturn = board.getBlackQRHasMoved();
				
		if (movingPiece == 0b1L) {
			pieceBB = board.getWhitePawnBB();
			board.setWhitePawnBB(pieceBB & ~on | to);
			if (to == on << 16) {
				board.setWhiteEPTargetBB(on << 8);
			}
		}
		else if (movingPiece == 0b10L) {
			pieceBB = board.getBlackPawnBB();
			board.setBlackPawnBB(pieceBB & ~on | to);
			if (to == on >>> 16) {
				board.setBlackEPTargetBB(on >>> 8);
			}
		}
		else if (movingPiece == 0b11L & forWhite) {
			pieceBB = board.getWhiteKnightBB();
			board.setWhiteKnightBB(pieceBB & ~on | to);
		}
		else if (movingPiece == 0b11L & !forWhite) {
			pieceBB = board.getBlackKnightBB();
			board.setBlackKnightBB(pieceBB & ~on | to);
		}
		else if (movingPiece == 0b100L & forWhite) {
			pieceBB = board.getWhiteBishopBB();
			board.setWhiteBishopBB(pieceBB & ~on | to);
		}
		else if (movingPiece == 0b100L & !forWhite) {
			pieceBB = board.getBlackBishopBB();
			board.setBlackBishopBB(pieceBB & ~on | to);
		}
		else if (movingPiece == 0b101L & forWhite) {
			pieceBB = board.getWhiteRookBB();
			board.setWhiteRookBB(pieceBB & ~on | to);
			if (isOnBB(whiteQRPosition, on)) {
				board.setWhiteQRHasMoved(true);
			}
			else if (isOnBB(whiteKRPosition, on)) {
				board.setWhiteKRHasMoved(true);
			}	
		}
		else if (movingPiece == 0b101L & !forWhite) {
			pieceBB = board.getBlackRookBB();
			board.setBlackRookBB(pieceBB & ~on | to);
			if (isOnBB(blackQRPosition, on)) {
				board.setBlackQRHasMoved(true);
			}
			else if (isOnBB(blackKRPosition, on)) {
				board.setBlackKRHasMoved(true);
			}
		}
		else if (movingPiece == 0b110L & forWhite) {
			pieceBB = board.getWhiteQueenBB();
			board.setWhiteQueenBB(pieceBB & ~on | to);	
		}
		else if (movingPiece == 0b110L & !forWhite) {
			pieceBB = board.getBlackQueenBB();
			board.setBlackQueenBB(pieceBB & ~on | to);
		}
		else if (movingPiece == 0b111L & forWhite) {
			pieceBB = board.getWhiteKingBB();
			board.setWhiteKingBB(pieceBB & ~on | to);
			board.setWhiteKingHasMoved(true);
		}
		else if (movingPiece == 0b111L & !forWhite) {
			pieceBB = board.getBlackKingBB();
			board.setBlackKingBB(pieceBB & ~on | to);
			board.setBlackKingHasMoved(true);
		}
		else {
			System.out.println("ERROR: movingPiece or forWhite is an unsupported value!");
			System.out.println("movingPiece:" + movingPiece);
		}
	}
	
	public void makeMove(Board board) {
		makeMoveWithoutUpdate(board);
		board.upDatePiecesBB();
		//System.out.println(moveToString() + "MADE");
	}
	
	public void unMakeMoveWithoutUpdate(Board board) {
		long pieceBB;
		
		if (movingPiece == 0b1L) {
			pieceBB = board.getWhitePawnBB();
			board.setWhitePawnBB(pieceBB & ~to | on);
			board.setWhiteEPTargetBB(0b0L);
		}
		else if (movingPiece == 0b10L) {
			pieceBB = board.getBlackPawnBB();
			board.setBlackPawnBB(pieceBB & ~to | on);
			board.setBlackEPTargetBB(0b0L);
		}
		else if (movingPiece == 0b11L & forWhite) {
			pieceBB = board.getWhiteKnightBB();
			board.setWhiteKnightBB(pieceBB & ~to | on);
		}
		else if (movingPiece == 0b11L & !forWhite) {
			pieceBB = board.getBlackKnightBB();
			board.setBlackKnightBB(pieceBB & ~to | on);
		}
		else if (movingPiece == 0b100L & forWhite) {
			pieceBB = board.getWhiteBishopBB();
			board.setWhiteBishopBB(pieceBB & ~to | on);
		}
		else if (movingPiece == 0b100L & !forWhite) {
			pieceBB = board.getBlackBishopBB();
			board.setBlackBishopBB(pieceBB & ~to | on);
		}
		else if (movingPiece == 0b101L & forWhite) {
			pieceBB = board.getWhiteRookBB();
			board.setWhiteRookBB(pieceBB & ~to | on);
			board.setWhiteKRHasMoved(whiteKRHasMovedReturn);
			board.setWhiteQRHasMoved(whiteQRHasMovedReturn);
		}
		else if (movingPiece == 0b101L & !forWhite) {
			pieceBB = board.getBlackRookBB();
			board.setBlackRookBB(pieceBB & ~to | on);
			board.setBlackKRHasMoved(blackKRHasMovedReturn);
			board.setBlackQRHasMoved(blackQRHasMovedReturn);
		}
		else if (movingPiece == 0b110L & forWhite) {
			pieceBB = board.getWhiteQueenBB();
			board.setWhiteQueenBB(pieceBB & ~to | on);
		}
		else if (movingPiece == 0b110L & !forWhite) {
			pieceBB = board.getBlackQueenBB();
			board.setBlackQueenBB(pieceBB & ~to | on);
		}
		else if (movingPiece == 0b111L & forWhite) {
			pieceBB = board.getWhiteKingBB();
			board.setWhiteKingBB(pieceBB & ~to | on);
			board.setWhiteKingHasMoved(whiteKingHasMovedReturn);
		}
		else if (movingPiece == 0b111L & !forWhite) {
			pieceBB = board.getBlackKingBB();
			board.setBlackKingBB(pieceBB & ~to | on);
			board.setBlackKingHasMoved(blackKingHasMovedReturn);
		}
		else {
			System.out.println("ERROR: movingPiece or forWhite is an unsupported value!");
		}
	}	
	
	public void unMakeMove(Board board) {
		unMakeMoveWithoutUpdate(board);
		board.upDatePiecesBB();
		//System.out.println(moveToString() + "UNMADE");
	}
	
	public boolean isOnBB (long BB1, long BB2) {
		if ((BB1 & BB2) != 0L) {
			return true;
		}
		return false;	
	}

	public String moveToString() {
		String colour;
		String piece;
		String onC = "X";
		String toC = "X";
		
		for (int i = 0; i <= 63; i++) {
			if (on == 0b1000000000000000000000000000000000000000000000000000000000000000L >>> i) {
				onC = coordinates[i];
			}
			if (to == 0b1000000000000000000000000000000000000000000000000000000000000000L >>> i) {
				toC = coordinates[i];
			}
		}
		if (movingPiece == 0b1L) {
			colour = "White";
			piece = "Pawn";
		}
		else if (movingPiece == 0b10L) {
			colour = "Black";
			piece = "Pawn";
		}
		else if (movingPiece == 0b11L & forWhite) {
			colour = "White";
			piece = "Knight";
		}
		else if (movingPiece == 0b11L & !forWhite) {
			colour = "Black";
			piece = "Knight";
		}
		else if (movingPiece == 0b100L & forWhite) {
			colour = "White";
			piece = "Bishop";	
		}
		else if (movingPiece == 0b100L & !forWhite) {
			colour = "Black";
			piece = "Bishop";
		}
		else if (movingPiece == 0b101L & forWhite) {
			colour = "White";
			piece = "Rook";
		}
		else if (movingPiece == 0b101L & !forWhite) {
			colour = "Black";
			piece = "Rook";
		}
		else if (movingPiece == 0b110L & forWhite) {
			colour = "White";
			piece = "Queen";
		}
		else if (movingPiece == 0b110L & !forWhite) {
			colour = "Black";
			piece = "Queen";
		}
		else if (movingPiece == 0b111L & forWhite) {
			colour = "White";
			piece = "King";		
		}
		else if (movingPiece == 0b111L & !forWhite) {
			colour = "Black";
			piece = "King";
		}
		else {
			System.out.println("ERROR: movingPiece or forWhite is an unsupported value!");
			System.out.println("movingPiece: " + movingPiece + ", forWhite: " + forWhite);
			colour = "X";
			piece = "X";
		}
		//return "(Move: " + colour + " " + piece  + ": " + onC + "-" + toC + ")";
		return onC + toC;
	}
	
	//For Testing
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

class Capture extends Move {
	
	private long capturedPiece;
	
	public Capture(long on, long to, long movingPiece, boolean forWhite, boolean isCheck, int priority, long capturedPiece) {
		super(on, to, movingPiece, forWhite, isCheck, 0);
		this.capturedPiece = capturedPiece;
		int capturedPiecePriority;
		if (capturedPiece <= 0b10L) {
			capturedPiecePriority = 2;
		}
		else {
			capturedPiecePriority = (int)capturedPiece;
		}
		setPriority((10*capturedPiecePriority - 10*(int)movingPiece) + priority);
	}
	
	public long getCapturedPiece() {
		return capturedPiece;
	}
	
	@Override
	public void makeMove(Board board) {
		super.makeMoveWithoutUpdate(board);
		long capturedPieceBB;
		if (this.capturedPiece == 0b1L) {
			capturedPieceBB = board.getWhitePawnBB();
			board.setWhitePawnBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b10L) {
			capturedPieceBB = board.getBlackPawnBB();
			board.setBlackPawnBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b11L & !getForWhite()) {
			capturedPieceBB = board.getWhiteKnightBB();
			board.setWhiteKnightBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b11L & getForWhite()) {
			capturedPieceBB = board.getBlackKnightBB();
			board.setBlackKnightBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b100L & !getForWhite()) {
			capturedPieceBB = board.getWhiteBishopBB();
			board.setWhiteBishopBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b100L & getForWhite()) {
			capturedPieceBB = board.getBlackBishopBB();
			board.setBlackBishopBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b101L & !getForWhite()) {
			capturedPieceBB = board.getWhiteRookBB();
			board.setWhiteRookBB(capturedPieceBB & ~getTo());
			if (isOnBB(whiteQRPosition, getTo())) {
				board.setWhiteQRHasMoved(true);
			}
			if (isOnBB(whiteKRPosition, getTo())) {
				board.setWhiteKRHasMoved(true);
			}	
		}
		else if (this.capturedPiece == 0b101L & getForWhite()) {
			capturedPieceBB = board.getBlackRookBB();
			board.setBlackRookBB(capturedPieceBB & ~getTo());		
			if (isOnBB(blackQRPosition, getTo())) {
				board.setBlackQRHasMoved(true);
			}
			if (isOnBB(blackKRPosition, getTo())) {
				board.setBlackKRHasMoved(true);
			}
		}
		else if (this.capturedPiece == 0b110L & !getForWhite()) {
			capturedPieceBB = board.getWhiteQueenBB();
			board.setWhiteQueenBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b110L & getForWhite()) {
			capturedPieceBB = board.getBlackQueenBB();
			board.setBlackQueenBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b111L & !getForWhite()) {
			System.out.println("King capture attempted!!");
			System.out.println("Move: " + moveToString());
			
		}
		else if (this.capturedPiece == 0b111L & getForWhite()) {
			System.out.println("King capture attempted!!");
			System.out.println("Move: " + moveToString());
		}
		else {
			System.out.println("ERROR: capturedPiece or forWhite is an unsupported value!");
		}
		board.upDatePiecesBB();
	}
	
	@Override
	public void unMakeMove(Board board) {
		super.unMakeMoveWithoutUpdate(board);
		long capturedPieceBB;
		if (this.capturedPiece == 0b1L) {
			capturedPieceBB = board.getWhitePawnBB();
			board.setWhitePawnBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b10L) {
			capturedPieceBB = board.getBlackPawnBB();
			board.setBlackPawnBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b11L & !getForWhite()) {
			capturedPieceBB = board.getWhiteKnightBB();
			board.setWhiteKnightBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b11L & getForWhite()) {
			capturedPieceBB = board.getBlackKnightBB();
			board.setBlackKnightBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b100L & !getForWhite()) {
			capturedPieceBB = board.getWhiteBishopBB();
			board.setWhiteBishopBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b100L & getForWhite()) {
			capturedPieceBB = board.getBlackBishopBB();
			board.setBlackBishopBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b101L & !getForWhite()) {
			capturedPieceBB = board.getWhiteRookBB();
			board.setWhiteRookBB(capturedPieceBB | getTo());
			board.setWhiteKRHasMoved(getWhiteKRHasMovedReturn());
			board.setWhiteQRHasMoved(getWhiteQRHasMovedReturn());
		}
		else if (this.capturedPiece == 0b101L & getForWhite()) {
			capturedPieceBB = board.getBlackRookBB();
			board.setBlackRookBB(capturedPieceBB | getTo());
			board.setBlackKRHasMoved(getBlackKRHasMovedReturn());
			board.setBlackQRHasMoved(getBlackQRHasMovedReturn());
		}
		else if (this.capturedPiece == 0b110L & !getForWhite()) {
			capturedPieceBB = board.getWhiteQueenBB();
			board.setWhiteQueenBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b110L & getForWhite()) {
			capturedPieceBB = board.getBlackQueenBB();
			board.setBlackQueenBB(capturedPieceBB | getTo());
		}
		else {
			System.out.println("ERROR: capturedPiece or forWhite is an unsupported value!");
			capturedPieceBB = 0b0L;
		}
		board.upDatePiecesBB();
	}
	
	/*@Override
	public String moveToString() {
		return "Capture: " + super.moveToString();
	}*/
	
	
}

class Promotion extends Move {
	
	private long promoteTo;
	
	public long getPromoteTo() {
		return promoteTo;
	}
	
	public Promotion(long on, long to, long movingPiece, boolean forWhite, boolean isCheck, int priority, long promoteTo) {
		super(on, to, movingPiece, forWhite, isCheck, priority);
		this.promoteTo = promoteTo;
	}
	
	@Override
	public void makeMoveWithoutUpdate(Board board) {	
		
		long pieceBB;
		long promotePieceBB;
		
		if (getMovingPiece() == 0b1L) {
			pieceBB = board.getWhitePawnBB();
			board.setWhitePawnBB(pieceBB & ~getOn());
		}
		else if (getMovingPiece() == 0b10L) {
			pieceBB = board.getBlackPawnBB();
			board.setBlackPawnBB(pieceBB & ~getOn());
		}
		else {
			System.out.println("ERROR: movingPiece or forWhite is an unsupported value!");
		}
		if (promoteTo == 0b11L & getForWhite()) {
			promotePieceBB = board.getWhiteKnightBB();
			board.setWhiteKnightBB(promotePieceBB | getTo());
		}
		else if (promoteTo == 0b11L & !getForWhite()) {
			promotePieceBB = board.getBlackKnightBB();
			board.setBlackKnightBB(promotePieceBB | getTo());
		}
		else if (promoteTo == 0b100L & getForWhite()) {
			promotePieceBB = board.getWhiteBishopBB();
			board.setWhiteBishopBB(promotePieceBB | getTo());
		}
		else if (promoteTo == 0b100L & !getForWhite()) {
			promotePieceBB = board.getBlackBishopBB();
			board.setBlackBishopBB(promotePieceBB | getTo());
		}
		else if (promoteTo == 0b101L & getForWhite()) {
			promotePieceBB = board.getWhiteRookBB();
			board.setWhiteRookBB(promotePieceBB | getTo());
		}
		else if (promoteTo == 0b101L & !getForWhite()) {
			promotePieceBB = board.getBlackRookBB();
			board.setBlackRookBB(promotePieceBB | getTo());
		}
		else if (promoteTo == 0b110L & getForWhite()) {
			promotePieceBB = board.getWhiteQueenBB();
			board.setWhiteQueenBB(promotePieceBB | getTo());
		}
		else if (promoteTo == 0b110L & !getForWhite()) {
			promotePieceBB = board.getBlackQueenBB();
			board.setBlackQueenBB(promotePieceBB | getTo());
		}
		else {
			System.out.println("ERROR: promoteTo or forWhite is an unsupported value!");
		}
	}
	
	@Override
	public void makeMove(Board board) {
		makeMoveWithoutUpdate(board);
		board.upDatePiecesBB();
	}
	
	@Override
	public void unMakeMoveWithoutUpdate(Board board) {
		
		long pieceBB;
		long promotePieceBB;
		
		if (getMovingPiece() == 0b1L) {
			pieceBB = board.getWhitePawnBB();
			board.setWhitePawnBB(pieceBB | getOn());
		}
		else if (getMovingPiece() == 0b10L) {
			pieceBB = board.getBlackPawnBB();
			board.setBlackPawnBB(pieceBB | getOn());
		}
		else {
			System.out.println("ERROR: movingPiece or forWhite is an unsupported value!");
		}
		if (promoteTo == 0b11L & getForWhite()) {
			promotePieceBB = board.getWhiteKnightBB();
			board.setWhiteKnightBB(promotePieceBB & ~getTo());
		}
		else if (promoteTo == 0b11L & !getForWhite()) {
			promotePieceBB = board.getBlackKnightBB();
			board.setBlackKnightBB(promotePieceBB & ~getTo());
		}
		else if (promoteTo == 0b100L & getForWhite()) {
			promotePieceBB = board.getWhiteBishopBB();
			board.setWhiteBishopBB(promotePieceBB & ~getTo());
		}
		else if (promoteTo == 0b100L & !getForWhite()) {
			promotePieceBB = board.getBlackBishopBB();
			board.setBlackBishopBB(promotePieceBB & ~getTo());
		}
		else if (promoteTo == 0b101L & getForWhite()) {
			promotePieceBB = board.getWhiteRookBB();
			board.setWhiteRookBB(promotePieceBB & ~getTo());
		}
		else if (promoteTo == 0b101L & !getForWhite()) {
			promotePieceBB = board.getBlackRookBB();
			board.setBlackRookBB(promotePieceBB & ~getTo());
		}
		else if (promoteTo == 0b110L & getForWhite()) {
			promotePieceBB = board.getWhiteQueenBB();
			board.setWhiteQueenBB(promotePieceBB & ~getTo());
		}
		else if (promoteTo == 0b110L & !getForWhite()) {
			promotePieceBB = board.getBlackQueenBB();
			board.setBlackQueenBB(promotePieceBB & ~getTo());
		}
		else {
			System.out.println("ERROR: promoteTo or forWhite is an unsupported value!");
		}
	}
	
	@Override
	public void unMakeMove(Board board) {
		unMakeMoveWithoutUpdate(board);
		board.upDatePiecesBB();
	}
	
	@Override
	public String moveToString() {
		String promotionChar;
		if (promoteTo == 0b11L) {
			promotionChar = "n";
		}
		else if (promoteTo == 0b100L) {
			promotionChar = "b";
		}
		else if (promoteTo == 0b101L) {
			promotionChar = "r";
		}
		else if (promoteTo == 0b110L) {
			promotionChar = "q";
		}
		else {
			System.out.println("ERROR: promoteTo is an unsupported value!");
			promotionChar = "unsupported value!";
		}
		//return "Promotion: " + super.moveToString() + promotionChar;
		return super.moveToString() + promotionChar;
	}	
}

class PromotionCapture extends Promotion {
	
	private long capturedPiece;
	
	public PromotionCapture(long on, long to, long movingPiece, boolean forWhite, boolean isCheck, int priority, long promoteTo, long capturedPiece) {
		super(on, to, movingPiece, forWhite, isCheck, priority, promoteTo);
		this.capturedPiece = capturedPiece;
	}
	
	@Override
	public void makeMove(Board board) {
		
		long pieceBB;
		long promotePieceBB;
		long capturedPieceBB;
		
        this.setWhiteKRHasMovedReturn(board.getWhiteKRHasMoved());
		this.setWhiteQRHasMovedReturn(board.getWhiteQRHasMoved());
		
		this.setBlackKRHasMovedReturn(board.getBlackKRHasMoved());
		this.setBlackQRHasMovedReturn(board.getBlackQRHasMoved());			
		
		super.makeMoveWithoutUpdate(board);
		if (this.capturedPiece == 0b1L) {
			capturedPieceBB = board.getWhitePawnBB();
			board.setWhitePawnBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b10L) {
			capturedPieceBB = board.getBlackPawnBB();
			board.setBlackPawnBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b11L & !getForWhite()) {
			capturedPieceBB = board.getWhiteKnightBB();
			board.setWhiteKnightBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b11L & getForWhite()) {
			capturedPieceBB = board.getBlackKnightBB();
			board.setBlackKnightBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b100L & !getForWhite()) {
			capturedPieceBB = board.getWhiteBishopBB();
			board.setWhiteBishopBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b100L & getForWhite()) {
			capturedPieceBB = board.getBlackBishopBB();
			board.setBlackBishopBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b101L & !getForWhite()) {
			capturedPieceBB = board.getWhiteRookBB();
			board.setWhiteRookBB(capturedPieceBB & ~getTo());
			if (isOnBB(whiteQRPosition, getTo())) {
				board.setWhiteQRHasMoved(true);
			}
			if (isOnBB(whiteKRPosition, getTo())) {
				board.setWhiteKRHasMoved(true);
			}	
		}
		else if (this.capturedPiece == 0b101L & getForWhite()) {
			capturedPieceBB = board.getBlackRookBB();
			board.setBlackRookBB(capturedPieceBB & ~getTo());		
			if (isOnBB(blackQRPosition, getTo())) {
				board.setBlackQRHasMoved(true);
			}
			if (isOnBB(blackKRPosition, getTo())) {
				board.setBlackKRHasMoved(true);
			}
		}
		else if (this.capturedPiece == 0b110L & !getForWhite()) {
			capturedPieceBB = board.getWhiteQueenBB();
			board.setWhiteQueenBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b110L & getForWhite()) {
			capturedPieceBB = board.getBlackQueenBB();
			board.setBlackQueenBB(capturedPieceBB & ~getTo());
		}
		else if (this.capturedPiece == 0b111L & !getForWhite()) {
			System.out.println("King capture attempted!!");
			System.out.println("Move: " + moveToString());
			
		}
		else if (this.capturedPiece == 0b111L & getForWhite()) {
			System.out.println("King capture attempted!!");
			System.out.println("Move: " + moveToString());
		}
		else {
			System.out.println("ERROR: capturedPiece or forWhite is an unsupported value!");
		}

		board.upDatePiecesBB();
	}
	
	@Override
	public void unMakeMove(Board board) {
		
		long pieceBB;
		long promotePieceBB;
		long capturedPieceBB;
		
		super.unMakeMoveWithoutUpdate(board);
		if (this.capturedPiece == 0b1L) {
			capturedPieceBB = board.getWhitePawnBB();
			board.setWhitePawnBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b10L) {
			capturedPieceBB = board.getBlackPawnBB();
			board.setBlackPawnBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b11L & !getForWhite()) {
			capturedPieceBB = board.getWhiteKnightBB();
			board.setWhiteKnightBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b11L & getForWhite()) {
			capturedPieceBB = board.getBlackKnightBB();
			board.setBlackKnightBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b100L & !getForWhite()) {
			capturedPieceBB = board.getWhiteBishopBB();
			board.setWhiteBishopBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b100L & getForWhite()) {
			capturedPieceBB = board.getBlackBishopBB();
			board.setBlackBishopBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b101L & !getForWhite()) {
			capturedPieceBB = board.getWhiteRookBB();
			board.setWhiteRookBB(capturedPieceBB | getTo());
			board.setWhiteKRHasMoved(getWhiteKRHasMovedReturn());
			board.setWhiteQRHasMoved(getWhiteQRHasMovedReturn());
		}
		else if (this.capturedPiece == 0b101L & getForWhite()) {
			capturedPieceBB = board.getBlackRookBB();
			board.setBlackRookBB(capturedPieceBB | getTo());
			board.setBlackKRHasMoved(getBlackKRHasMovedReturn());
			board.setBlackQRHasMoved(getBlackQRHasMovedReturn());
		}
		else if (this.capturedPiece == 0b110L & !getForWhite()) {
			capturedPieceBB = board.getWhiteQueenBB();
			board.setWhiteQueenBB(capturedPieceBB | getTo());
		}
		else if (this.capturedPiece == 0b110L & getForWhite()) {
			capturedPieceBB = board.getBlackQueenBB();
			board.setBlackQueenBB(capturedPieceBB | getTo());
		}
		board.upDatePiecesBB();
	}	
}	

class EnPassant extends Move {
	
	public EnPassant(long on, long to, long movingPiece, boolean forWhite, boolean isCheck, int priority) {
		super(on, to, movingPiece, forWhite, isCheck, priority);
	}
	
	@Override
	public void makeMove(Board board) {
		long pieceBB;
		long capturedPawnBB;
		if (getMovingPiece() == 0b1L) {
			pieceBB = board.getWhitePawnBB();
			capturedPawnBB = board.getBlackPawnBB();
			board.setBlackEPTargetBB(0b0L);
			board.setWhitePawnBB(pieceBB & ~getOn() | getTo());
			board.setBlackPawnBB(capturedPawnBB & ~(getTo() >>> 8));
		}
		else if (getMovingPiece() == 0b10L) {
			pieceBB = board.getBlackPawnBB();
			capturedPawnBB = board.getWhitePawnBB();
			board.setWhiteEPTargetBB(0b0L);
			board.setBlackPawnBB(pieceBB & ~getOn() | getTo());
			board.setWhitePawnBB(capturedPawnBB & ~(getTo() << 8));
		}
		else {
			System.out.println("ERROR: movingPiece or forWhite is an unsupported value!");
		}
		board.upDatePiecesBB();
	}
	
	@Override
	public void unMakeMove(Board board) {
		long pieceBB;
		long capturedPawnBB;
		if (getMovingPiece() == 0b1L) {
			pieceBB = board.getWhitePawnBB();
			capturedPawnBB = board.getBlackPawnBB();
			board.setBlackEPTargetBB(getTo());
			board.setWhitePawnBB(pieceBB & ~getTo() | getOn());
			board.setBlackPawnBB(capturedPawnBB | (getTo() >>> 8));	
		}
		else if (getMovingPiece() == 0b10L) {
			pieceBB = board.getBlackPawnBB();
			capturedPawnBB = board.getWhitePawnBB();
			board.setWhiteEPTargetBB(getTo());
			board.setBlackPawnBB(pieceBB & ~getTo() | getOn());
			board.setWhitePawnBB(capturedPawnBB | (getTo() << 8));	
		}
		else {
			System.out.println("ERROR: movingPiece or forWhite is an unsupported value!");
		}
		board.upDatePiecesBB();
	}
	
	/*@Override
	public String moveToString() {
		return "En Passant: " + super.moveToString();
	}	*/
}

class Castle extends Move{
	
	private char side;
	//private boolean forWhite;
	
	public Castle(char side, boolean forWhite, boolean isCheck, int priority) {
		super(forWhite, isCheck, priority);
		this.side = side;
	}
	
	public char getSide() {
		return side;
	}
	
	
	@Override
	public void makeMove(Board board) {
		
		//setWhiteKingHasMovedReturn(board.getWhiteKingHasMoved());
		//setBlackKingHasMovedReturn(board.getBlackKingHasMoved());
		
		if (getForWhite()) {
			if (side == 'Q') {
				board.setWhiteKingBB(board.getWhiteKingBB() << 2);
				board.setWhiteRookBB(board.getWhiteRookBB() & ~whiteQRPosition | (whiteQRPosition  >>> 3));
			}
			else if (side == 'K') {
				board.setWhiteKingBB(board.getWhiteKingBB() >>> 2);	
				board.setWhiteRookBB(board.getWhiteRookBB() & ~whiteKRPosition | (whiteKRPosition << 2));
			}
			else {
				System.out.println("ERROR: Invalid side passed into Castle.MakeMove()");
			}
			board.setWhiteKingHasMoved(true);
		}
		else {
			if (side == 'Q') {
				board.setBlackKingBB(board.getBlackKingBB() << 2);
				board.setBlackRookBB(board.getBlackRookBB() & ~blackQRPosition | (blackQRPosition >>> 3));
			}
			else if (side == 'K') {
				board.setBlackKingBB(board.getBlackKingBB() >>> 2);	
				board.setBlackRookBB(board.getBlackRookBB() & ~blackKRPosition | (blackKRPosition << 2));
			}
			else {
				System.out.println("ERROR: Invalid side passed into Castle.MakeMove()");
			}
			board.setBlackKingHasMoved(true);
		}		
		board.upDatePiecesBB();
	}
	
	@Override
	public void unMakeMove(Board board) {
		if (getForWhite()) {
			if (side == 'Q') {
				board.setWhiteKingBB(board.getWhiteKingBB() >>> 2);
				board.setWhiteRookBB((board.getWhiteRookBB() | whiteQRPosition) & ~(whiteQRPosition  >>> 3));
			}
			else if (side == 'K') {
				board.setWhiteKingBB(board.getWhiteKingBB() << 2);	
				board.setWhiteRookBB((board.getWhiteRookBB() | whiteKRPosition) & ~(whiteKRPosition << 2));
			}
			else {
				System.out.println("ERROR: Invalid side passed into Castle.MakeMove()");
			}
			board.setWhiteKingHasMoved(false);
		}
		else {
			if (side == 'Q') {
				board.setBlackKingBB(board.getBlackKingBB() >>> 2);
				board.setBlackRookBB((board.getBlackRookBB() | blackQRPosition) & ~(blackQRPosition >>> 3));
			}
			else if (side == 'K') {
				board.setBlackKingBB(board.getBlackKingBB() << 2);	
				board.setBlackRookBB((board.getBlackRookBB() | blackKRPosition) & ~(blackKRPosition << 2));
			}
			else {
				System.out.println("ERROR: Invalid side passed into Castle.MakeMove()");
			}
			board.setBlackKingHasMoved(false);
		}	
		board.upDatePiecesBB();
	}
	
	@Override
	public String moveToString() {
		if (getForWhite()) {
			if (side == 'Q') {
				//return "(Castle: e1-c1)";
				return "e1c1";
			}
			else if (side == 'K') {
				//return "(Castle: e1-g1)";
				return "e1g1";
			}
			else {
				System.out.println("ERROR: Invalid side passed into Castle.MakeMove()");
			}
		}
		else {
			if (side == 'Q') {
				//return "(Castle: e8-c8)";
				return "e8c8";
			}
			else if (side == 'K') {
				//return "(Castle: e8-g8)";
				return "e8g8";
			}
			else {
				System.out.println("ERROR: Invalid side passed into Castle.MakeMove()");
			}
		}
		return "Castle to string error";
	}	
}

 