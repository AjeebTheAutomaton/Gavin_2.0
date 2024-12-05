package com.gavin.chessengine;

import javafx.application.Platform;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.input.KeyCode;
import java.io.File;
import java.io.FileNotFoundException;


public class FenViewer extends Application implements EngineListener{
	
	Scanner fileInput;
	
	//String startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	
	//Import and set up piece PNG's	
	Image wpI = new Image("Images//wp.png");
	Image bpI = new Image("Images//bp.png");
	Image wnI = new Image("Images//wn.png");
	Image bnI = new Image("Images//bn.png");
	Image wbI = new Image("Images//wb.png");
	Image bbI = new Image("Images//bb.png");
	Image wrI = new Image("Images//wr.png");
	Image brI = new Image("Images//br.png");
	Image wqI = new Image("Images//wq.png");
	Image bqI = new Image("Images//bq.png");
	Image wkI = new Image("Images//wk.png");
	Image bkI = new Image("Images//bk.png");	
	
	private Circle[] circles;
	//-private Rectangle[] rectangles;
	private Rectangle lastClickedSquareR;
	long selectedPieceMoveBB;
	long selectedKingCastleBB;
	private boolean waitingForMoveSelection;
	long selectedPiece;
	long on;
	boolean selectedPieceColour;
	//private long selectedPieceMoveBB;
	
	private Color returnColor;
	private StackPane a8, b8, c8, d8, e8, f8, g8, h8, a7, b7, c7, d7, e7, f7, g7, h7, a6, b6, c6, d6, e6, f6, g6, h6, a5, b5, c5, d5, e5, f5, g5, h5, a4, b4, c4, d4, e4, f4, g4, h4, a3, b3, c3, d3, e3, f3, g3, h3, a2, b2, c2, d2, e2, f2, g2, h2, a1, b1, c1, d1, e1, f1, g1, h1;
	private StackPane[] squares;
	private GridPane boardPane;
	private VBox controlPane;
	//private Board board;
	
    public void init() {
		//game = new Game(startFen);
		//game.getEngine().setEngineListener(this);
		//game.getMoveGenerator().getBoard().setBlackKingHasMoved(true);
		//game.getMoveGenerator().getBoard().setWhiteKingHasMoved(true);
		//game.getEngine().setBoardUpdateListener(() -> {Platform.runLater(this::updateBoardDisplay);});
		//game.getMoveGenerator().getBoard().setWhiteEPTargetBB(0b10000000000000000000L);
		//game.getMoveGenerator().getBoard().printBoard();
		//game.getMoveGenerator().printCheckBeams();
		//game.getMoveGenerator().printAttackedSquares();
		//game.getBoard().setBlackKingBB(0b10L);
		//game.printBoard(game.getBoard().getBlackKingBB());
		//game.setCheckBeamsAndRays(true);
		//game.setCheckBeamsAndRays(false);
		//game.setWhitesAttackedSquares();
		//game.setBlacksAttackedSquares();
		//game = new Game("8/8/8/8/8/8/4P3/K6k");

		//move.unMakeMove(game.getBoard());
		//System.out.println("White Pawn BB after un-make move");
		//game.printBoard(game.getBoard().getWhitePawnBB());
		//System.out.println("White Checks: " + game.getChecks(true));
		//System.out.println("Black Checks: " + game.getChecks(false));
    }	
	
    public static void main(String[] args) {
        launch(args);
    }	

    @Override
    public void start(Stage primaryStage) {
		try {
			fileInput = new Scanner(new File("C:\\Users\\willh\\OneDrive\\Desktop\\CODING\\Gavin 2.0\\FenViewer\\EngineFENs.txt"));
		}
		catch (FileNotFoundException err) {
		}
		setBoardPane();
		squares = new StackPane[]{a8, b8, c8, d8, e8, f8, g8, h8, a7, b7, c7, d7, e7, f7, g7, h7, a6, b6, c6, d6, e6, f6, g6, h6, a5, b5, c5, d5, e5, f5, g5, h5, a4, b4, c4, d4, e4, f4, g4, h4, a3, b3, c3, d3, e3, f3, g3, h3, a2, b2, c2, d2, e2, f2, g2, h2, a1, b1, c1, d1, e1, f1, g1, h1};
		//setPieces();
		
        BorderPane root = new BorderPane();
        root.setCenter(boardPane);
		
        Scene scene = new Scene(root, 820, 820);
		
        primaryStage.setTitle("Gavin 2.0");
        primaryStage.setScene(scene);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                System.out.println("Left arrow key was pressed.");
            }
			else if (event.getCode() == KeyCode.RIGHT) {
				System.out.println("Right arrow key was pressed.");
				handleRightArrowPress(fileInput);
			}
        });		
        primaryStage.show();
		//new Thread(() -> game.startGame()).start();
    }
	
	public void handleRightArrowPress(Scanner scanner) {
		String fen = "";
		if (scanner.hasNext()) {
			fen = scanner.next();
		}
		else {
			System.out.println("End Reached");
		}
		setPieces(fen);
	}
	
	/*public void handleDarkSquareClick(Rectangle clickedSquare, boolean changeColour) {
		if (clickedSquare != lastClickedSquareR) {
			if(changeColour) {
				clickedSquare.setFill(Color.LIME);
			}
			lastClickedSquareR.setFill(returnColor);
			lastClickedSquareR = clickedSquare;
			returnColor = Color.MEDIUMSEAGREEN;
		}
	}
	
	public void handleLightSquareClick(Rectangle clickedSquare, boolean changeColour) {
		if (clickedSquare != lastClickedSquareR) {
			if(changeColour) {
				clickedSquare.setFill(Color.YELLOW);
			}
			lastClickedSquareR.setFill(returnColor);	
			lastClickedSquareR = clickedSquare;
			returnColor = Color.BISQUE;
		}
	}
	
	public void handlePieceSelectionClick(long clickedSquare, Rectangle rectangle, boolean isLightSquare) {
		System.out.println("Whites Move: " + game.getIsWhitesMove());
		if (waitingForMoveSelection) {
			handleMoveSelectionClick (clickedSquare, rectangle, isLightSquare, selectedPieceMoveBB, selectedKingCastleBB);
		}
		else {
			selectedPiece = game.getMoveGenerator().getBoard().getPieceOnSquare(clickedSquare);
			selectedPieceColour = game.getMoveGenerator().checkPieceIsWhite(clickedSquare); //might need to be moved back 
			if (selectedPiece != 0b0L & selectedPieceColour == game.getIsWhitesMove()) {
				System.out.println("piece selected");
				//Highlight selected piece
				if (isLightSquare) {
					handleLightSquareClick(rectangle, true);
				}
				else {
					handleDarkSquareClick(rectangle, true);
				}
				
				//Save selected piece info
				on = clickedSquare;
				//Here selectedPieceColour = game.checkPieceIsWhite(clickedSquare); 
				System.out.println("getLegalMove Called");
				selectedPieceMoveBB = game.getMoveGenerator().getLegalMoveBB(clickedSquare, selectedPiece, selectedPieceColour);
				if (selectedPiece == 0b111L & selectedPieceColour) {
					if (game.getMoveGenerator().checkCastlingAvaliable(true, 'Q')) {
						selectedKingCastleBB = selectedKingCastleBB | 0b00100000L;
					}
					if (game.getMoveGenerator().checkCastlingAvaliable(true, 'K')) {
						selectedKingCastleBB = selectedKingCastleBB | 0b00000010L;
					}
				}
				else if (selectedPiece == 0b111L & !selectedPieceColour) {
					if (game.getMoveGenerator().checkCastlingAvaliable(false, 'Q')) {
						selectedKingCastleBB = selectedKingCastleBB | 0b0010000000000000000000000000000000000000000000000000000000000000L;
					}
					if (game.getMoveGenerator().checkCastlingAvaliable(false, 'K')) {
						selectedKingCastleBB = selectedKingCastleBB | 0b0000001000000000000000000000000000000000000000000000000000000000L;
					}
				}
				else {
					selectedKingCastleBB = 0b0L;
				}
				//Show sleceted piece's moves
				showMoves(selectedPieceMoveBB | selectedKingCastleBB);
				
				//Tell ChessGUI to wait for move selection
				waitingForMoveSelection = true;
			}
			else {
				//Keep track of return colour
				if (isLightSquare) {
					handleLightSquareClick(rectangle, false);
				}
				else {
					handleDarkSquareClick(rectangle, false);
				}
			}
		}
	}*/
	
	/*public void handleMoveSelectionClick(long clickedSquare, Rectangle rectangle, boolean isLightSquare, long MoveBB, long CastleBB) {
		Board board = game.getMoveGenerator().getBoard();
		//Checks if legal move made.
		if (game.getMoveGenerator().isOnBB(MoveBB, clickedSquare)) {
			//Highlight legal move.
			if (isLightSquare) {
				handleLightSquareClick(rectangle, true);
			}
			else {
				handleDarkSquareClick(rectangle, true);
			}
			long capturedPiece = board.getPieceOnSquare(clickedSquare);
			System.out.println("Captured Piece: " + capturedPiece);
			//Checks if en passant made.
			if (capturedPiece == 0b1000L) {
				EnPassant enPassant = new EnPassant(on, clickedSquare, selectedPiece, selectedPieceColour);
				movePiecesEnPassant(enPassant);
				enPassant.makeMove(board);
				game.setPlayersMoveMade(true);
				waitingForMoveSelection = false;
			}
			//Checks if capture made.
			else if (capturedPiece != 0) {
				//Checks if promotion capture made.
				if (selectedPiece == 0b1L & game.getMoveGenerator().isOnBB(clickedSquare, game.getMoveGenerator().getRank(7))) {
					PromotionCapture promotionCapture = new PromotionCapture(on, clickedSquare, selectedPiece, selectedPieceColour, 0b110L, capturedPiece);
					movePieces(promotionCapture);
					promotionCapture.makeMove(board);
					game.setPlayersMoveMade(true);
					waitingForMoveSelection = false;
				}
				else if (selectedPiece == 0b10L & game.getMoveGenerator().isOnBB(clickedSquare, game.getMoveGenerator().getRank(0))) {
					PromotionCapture promotionCapture = new PromotionCapture(on, clickedSquare, selectedPiece, selectedPieceColour, 0b110L, capturedPiece);
					movePieces(promotionCapture);
					promotionCapture.makeMove(board);
					game.setPlayersMoveMade(true);
					waitingForMoveSelection = false;
				}
				//If not promotion then regular capture is made.
				else {
					System.out.println("CAPTURE MADE");
					Capture capture = new Capture(on, clickedSquare, selectedPiece, selectedPieceColour, capturedPiece);
					movePieces(capture);
					capture.makeMove(board);
					game.setPlayersMoveMade(true);
					waitingForMoveSelection = false;
				}
			}
			//Checks if non-capture promotion made.
			else if (selectedPiece == 0b1L & game.getMoveGenerator().isOnBB(clickedSquare, game.getMoveGenerator().getRank(7))) {
				Promotion promotion = new Promotion(on, clickedSquare, selectedPiece, selectedPieceColour, 0b110L);
				movePieces(promotion);
				promotion.makeMove(board);
				game.setPlayersMoveMade(true);
				waitingForMoveSelection = false;
			}
			else if (selectedPiece == 0b10L & game.getMoveGenerator().isOnBB(clickedSquare, game.getMoveGenerator().getRank(0))) {
				Promotion promotion = new Promotion(on, clickedSquare, selectedPiece, selectedPieceColour, 0b110L);
				movePieces(promotion);
				promotion.makeMove(board);
				game.setPlayersMoveMade(true);
				waitingForMoveSelection = false;
			}
			//If no special move condition is met then move made
			else {
				System.out.println("MOVE MADE");
				Move move = new Move(on, clickedSquare, selectedPiece, selectedPieceColour);
				movePieces(move);
				move.makeMove(board);
				game.setPlayersMoveMade(true);
				waitingForMoveSelection = false;
			}
			//System.out.println("Move made with piece: " + selectedPiece);
			//game.switchTurn();
			showMoves(0b0L);
		}
		//Castle Selected
		else if(game.getMoveGenerator().isOnBB(selectedKingCastleBB, clickedSquare)) {
			if(clickedSquare == 0b00100000L & selectedPieceColour) {
				Castle castle = new Castle('Q', true);
				movePiecesCastle(castle);
				castle.makeMove(board);
				game.setPlayersMoveMade(true);
			}
			else if(clickedSquare == 0b00000010L & selectedPieceColour) {
				Castle castle = new Castle('K', true);
				movePiecesCastle(castle);
				castle.makeMove(board);
				game.setPlayersMoveMade(true);
			}
			else if(clickedSquare == 0b0010000000000000000000000000000000000000000000000000000000000000L & !selectedPieceColour) {
				Castle castle = new Castle('Q', false);
				movePiecesCastle(castle);
				castle.makeMove(board);
				game.setPlayersMoveMade(true);
			}	
			else if(clickedSquare == 0b0000001000000000000000000000000000000000000000000000000000000000L & !selectedPieceColour) {
				Castle castle  = new Castle('K', false);
				movePiecesCastle(castle);
				castle.makeMove(board);
				game.setPlayersMoveMade(true);
			}	
			else {
				System.out.println("Illegal Move (Castle) selected");
			}
			waitingForMoveSelection = false;
		}
		//Illegal Move Selecetd
		else {
			System.out.println("Illegal Move selected");
			waitingForMoveSelection = false;
			showMoves(0b0L);
			handlePieceSelectionClick(clickedSquare, rectangle, isLightSquare);
		}
		
	}
	
	public void showMoves(long moveBB) {
		long start = 0b1000000000000000000000000000000000000000000000000000000000000000L;
		for (int i = 0; i <= 63; i++) {
			if (game.getMoveGenerator().isOnBB(moveBB, start >>> i)) {
				circles[i].setOpacity(0.5);
			}
			else {
				circles[i].setOpacity(0);
			}
		}
	}
	
	public void showMovesList(Move[] moveList) {
		Circle circle = new Circle(); circle.setRadius(40); circle.setFill(Color.GREY);
		long start = 0b1000000000000000000000000000000000000000000000000000000000000000L;
		long moveBB;
		//ArrayList<Circle> avaliableMoves = new ArrayList<Circle>();
		for (Move move: moveList) {
			moveBB = move.getTo();
			showMoves(moveBB);
		}
	}*/
	
	public void setBoardPane() {
		//Create StackPane for squares
		
		a8 = new StackPane(); a7 = new StackPane(); a6 = new StackPane(); a5 = new StackPane(); a4 = new StackPane(); a3 = new StackPane(); a2 = new StackPane(); a1 = new StackPane();
		b8 = new StackPane(); b7 = new StackPane(); b6 = new StackPane(); b5 = new StackPane(); b4 = new StackPane(); b3 = new StackPane(); b2 = new StackPane(); b1 = new StackPane();
		c8 = new StackPane(); c7 = new StackPane(); c6 = new StackPane(); c5 = new StackPane(); c4 = new StackPane(); c3 = new StackPane(); c2 = new StackPane(); c1 = new StackPane();
		d8 = new StackPane(); d7 = new StackPane(); d6 = new StackPane(); d5 = new StackPane(); d4 = new StackPane(); d3 = new StackPane(); d2 = new StackPane(); d1 = new StackPane();
		e8 = new StackPane(); e7 = new StackPane(); e6 = new StackPane(); e5 = new StackPane(); e4 = new StackPane(); e3 = new StackPane(); e2 = new StackPane(); e1 = new StackPane();
		f8 = new StackPane(); f7 = new StackPane(); f6 = new StackPane(); f5 = new StackPane(); f4 = new StackPane(); f3 = new StackPane(); f2 = new StackPane(); f1 = new StackPane();
		g8 = new StackPane(); g7 = new StackPane(); g6 = new StackPane(); g5 = new StackPane(); g4 = new StackPane(); g3 = new StackPane(); g2 = new StackPane(); g1 = new StackPane();
		h8 = new StackPane(); h7 = new StackPane(); h6 = new StackPane(); h5 = new StackPane(); h4 = new StackPane(); h3 = new StackPane(); h2 = new StackPane(); h1 = new StackPane();
		
		//Create Rectangles for squares
		Rectangle R = new Rectangle();
		Rectangle a8R = new Rectangle(); a8R.setWidth(100); a8R.setHeight(100); a8R.setFill(Color.BISQUE);
		Rectangle b8R = new Rectangle(); b8R.setWidth(100); b8R.setHeight(100);	b8R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle c8R = new Rectangle(); c8R.setWidth(100); c8R.setHeight(100); c8R.setFill(Color.BISQUE);
		Rectangle d8R = new Rectangle(); d8R.setWidth(100); d8R.setHeight(100); d8R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle e8R = new Rectangle(); e8R.setWidth(100);	e8R.setHeight(100); e8R.setFill(Color.BISQUE);
		Rectangle f8R = new Rectangle(); f8R.setWidth(100); f8R.setHeight(100);	f8R.setFill(Color.MEDIUMSEAGREEN);		
		Rectangle g8R = new Rectangle(); g8R.setWidth(100); g8R.setHeight(100); g8R.setFill(Color.BISQUE);
		Rectangle h8R = new Rectangle(); h8R.setWidth(100); h8R.setHeight(100); h8R.setFill(Color.MEDIUMSEAGREEN);
		
		Rectangle a7R = new Rectangle(); a7R.setWidth(100);	a7R.setHeight(100);	a7R.setFill(Color.MEDIUMSEAGREEN);	
		Rectangle b7R = new Rectangle(); b7R.setWidth(100); b7R.setHeight(100); b7R.setFill(Color.BISQUE);
		Rectangle c7R = new Rectangle(); c7R.setWidth(100); c7R.setHeight(100);	c7R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle d7R = new Rectangle(); d7R.setWidth(100);	d7R.setHeight(100); d7R.setFill(Color.BISQUE);
		Rectangle e7R = new Rectangle(); e7R.setWidth(100);	e7R.setHeight(100);	e7R.setFill(Color.MEDIUMSEAGREEN);	
		Rectangle f7R = new Rectangle(); f7R.setWidth(100); f7R.setHeight(100); f7R.setFill(Color.BISQUE);
		Rectangle g7R = new Rectangle(); g7R.setWidth(100); g7R.setHeight(100); g7R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle h7R = new Rectangle(); h7R.setWidth(100);	h7R.setHeight(100);	h7R.setFill(Color.BISQUE);
		
		Rectangle a6R = new Rectangle(); a6R.setWidth(100); a6R.setHeight(100); a6R.setFill(Color.BISQUE);
		Rectangle b6R = new Rectangle(); b6R.setWidth(100); b6R.setHeight(100); b6R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle c6R = new Rectangle(); c6R.setWidth(100); c6R.setHeight(100); c6R.setFill(Color.BISQUE);
		Rectangle d6R = new Rectangle(); d6R.setWidth(100); d6R.setHeight(100); d6R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle e6R = new Rectangle(); e6R.setWidth(100); e6R.setHeight(100); e6R.setFill(Color.BISQUE);
		Rectangle f6R = new Rectangle(); f6R.setWidth(100);	f6R.setHeight(100);	f6R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle g6R = new Rectangle(); g6R.setWidth(100); g6R.setHeight(100); g6R.setFill(Color.BISQUE);
		Rectangle h6R = new Rectangle(); h6R.setWidth(100); h6R.setHeight(100); h6R.setFill(Color.MEDIUMSEAGREEN);
			
		Rectangle a5R = new Rectangle(); a5R.setWidth(100);	a5R.setHeight(100);	a5R.setFill(Color.MEDIUMSEAGREEN);	
		Rectangle b5R = new Rectangle(); b5R.setWidth(100); b5R.setHeight(100); b5R.setFill(Color.BISQUE);
		Rectangle c5R = new Rectangle(); c5R.setWidth(100); c5R.setHeight(100);	c5R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle d5R = new Rectangle(); d5R.setWidth(100);	d5R.setHeight(100); d5R.setFill(Color.BISQUE);
		Rectangle e5R = new Rectangle(); e5R.setWidth(100);	e5R.setHeight(100);	e5R.setFill(Color.MEDIUMSEAGREEN);	
		Rectangle f5R = new Rectangle(); f5R.setWidth(100); f5R.setHeight(100); f5R.setFill(Color.BISQUE);
		Rectangle g5R = new Rectangle(); g5R.setWidth(100); g5R.setHeight(100); g5R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle h5R = new Rectangle(); h5R.setWidth(100);	h5R.setHeight(100);	h5R.setFill(Color.BISQUE);
		
		Rectangle a4R = new Rectangle(); a4R.setWidth(100); a4R.setHeight(100); a4R.setFill(Color.BISQUE);
		Rectangle b4R = new Rectangle(); b4R.setWidth(100); b4R.setHeight(100);	b4R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle c4R = new Rectangle(); c4R.setWidth(100); c4R.setHeight(100); c4R.setFill(Color.BISQUE);
		Rectangle d4R = new Rectangle(); d4R.setWidth(100); d4R.setHeight(100); d4R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle e4R = new Rectangle(); e4R.setWidth(100);	e4R.setHeight(100); e4R.setFill(Color.BISQUE);
		Rectangle f4R = new Rectangle(); f4R.setWidth(100); f4R.setHeight(100);	f4R.setFill(Color.MEDIUMSEAGREEN);		
		Rectangle g4R = new Rectangle(); g4R.setWidth(100); g4R.setHeight(100); g4R.setFill(Color.BISQUE);
		Rectangle h4R = new Rectangle(); h4R.setWidth(100); h4R.setHeight(100); h4R.setFill(Color.MEDIUMSEAGREEN);
		
		Rectangle a3R = new Rectangle(); a3R.setWidth(100);	a3R.setHeight(100);	a3R.setFill(Color.MEDIUMSEAGREEN);	
		Rectangle b3R = new Rectangle(); b3R.setWidth(100); b3R.setHeight(100); b3R.setFill(Color.BISQUE);
		Rectangle c3R = new Rectangle(); c3R.setWidth(100); c3R.setHeight(100);	c3R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle d3R = new Rectangle(); d3R.setWidth(100);	d3R.setHeight(100); d3R.setFill(Color.BISQUE);
		Rectangle e3R = new Rectangle(); e3R.setWidth(100);	e3R.setHeight(100);	e3R.setFill(Color.MEDIUMSEAGREEN);	
		Rectangle f3R = new Rectangle(); f3R.setWidth(100); f3R.setHeight(100); f3R.setFill(Color.BISQUE);
		Rectangle g3R = new Rectangle(); g3R.setWidth(100); g3R.setHeight(100); g3R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle h3R = new Rectangle(); h3R.setWidth(100);	h3R.setHeight(100);	h3R.setFill(Color.BISQUE);
		
		Rectangle a2R = new Rectangle(); a2R.setWidth(100); a2R.setHeight(100); a2R.setFill(Color.BISQUE);
		Rectangle b2R = new Rectangle(); b2R.setWidth(100); b2R.setHeight(100); b2R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle c2R = new Rectangle(); c2R.setWidth(100); c2R.setHeight(100); c2R.setFill(Color.BISQUE);
		Rectangle d2R = new Rectangle(); d2R.setWidth(100); d2R.setHeight(100); d2R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle e2R = new Rectangle(); e2R.setWidth(100); e2R.setHeight(100); e2R.setFill(Color.BISQUE);
		Rectangle f2R = new Rectangle(); f2R.setWidth(100);	f2R.setHeight(100);	f2R.setFill(Color.MEDIUMSEAGREEN);
		Rectangle g2R = new Rectangle(); g2R.setWidth(100); g2R.setHeight(100); g2R.setFill(Color.BISQUE);
		Rectangle h2R = new Rectangle(); h2R.setWidth(100); h2R.setHeight(100); h2R.setFill(Color.MEDIUMSEAGREEN);
			
		Rectangle a1R = new Rectangle(); a1R.setWidth(100);	a1R.setHeight(100);	a1R.setFill(Color.MEDIUMSEAGREEN);	
		Rectangle b1R = new Rectangle(); b1R.setWidth(100); b1R.setHeight(100); b1R.setFill(Color.BISQUE);
		Rectangle c1R = new Rectangle(); c1R.setWidth(100); c1R.setHeight(100);	c1R.setFill(Color.MEDIUMSEAGREEN);	
		Rectangle d1R = new Rectangle(); d1R.setWidth(100);	d1R.setHeight(100); d1R.setFill(Color.BISQUE);
		Rectangle e1R = new Rectangle(); e1R.setWidth(100);	e1R.setHeight(100);	e1R.setFill(Color.MEDIUMSEAGREEN);		
		Rectangle f1R = new Rectangle(); f1R.setWidth(100); f1R.setHeight(100); f1R.setFill(Color.BISQUE);
		Rectangle g1R = new Rectangle(); g1R.setWidth(100); g1R.setHeight(100); g1R.setFill(Color.MEDIUMSEAGREEN);	
		Rectangle h1R = new Rectangle(); h1R.setWidth(100);	h1R.setHeight(100);	h1R.setFill(Color.BISQUE);	

		//Create circles for squares
		
		Circle a8C = new Circle(); a8C.setRadius(45); a8C.setOpacity(0); a8C.setFill(Color.GREY);
		Circle b8C = new Circle(); b8C.setRadius(45); b8C.setOpacity(0); b8C.setFill(Color.GREY);
		Circle c8C = new Circle(); c8C.setRadius(45); c8C.setOpacity(0); c8C.setFill(Color.GREY);
		Circle d8C = new Circle(); d8C.setRadius(45); d8C.setOpacity(0); d8C.setFill(Color.GREY);
		Circle e8C = new Circle(); e8C.setRadius(45); e8C.setOpacity(0); e8C.setFill(Color.GREY);
		Circle f8C = new Circle(); f8C.setRadius(45); f8C.setOpacity(0); f8C.setFill(Color.GREY);		
		Circle g8C = new Circle(); g8C.setRadius(45); g8C.setOpacity(0); g8C.setFill(Color.GREY);
		Circle h8C = new Circle(); h8C.setRadius(45); h8C.setOpacity(0); h8C.setFill(Color.GREY);
		
		Circle a7C = new Circle(); a7C.setRadius(45); a7C.setOpacity(0); a7C.setFill(Color.GREY);	
		Circle b7C = new Circle(); b7C.setRadius(45); b7C.setOpacity(0); b7C.setFill(Color.GREY);
		Circle c7C = new Circle(); c7C.setRadius(45); c7C.setOpacity(0); c7C.setFill(Color.GREY);
		Circle d7C = new Circle(); d7C.setRadius(45); d7C.setOpacity(0); d7C.setFill(Color.GREY);
		Circle e7C = new Circle(); e7C.setRadius(45); e7C.setOpacity(0); e7C.setFill(Color.GREY);	
		Circle f7C = new Circle(); f7C.setRadius(45); f7C.setOpacity(0); f7C.setFill(Color.GREY);
		Circle g7C = new Circle(); g7C.setRadius(45); g7C.setOpacity(0); g7C.setFill(Color.GREY);
		Circle h7C = new Circle(); h7C.setRadius(45); h7C.setOpacity(0); h7C.setFill(Color.GREY);
		
		Circle a6C = new Circle(); a6C.setRadius(45); a6C.setOpacity(0); a6C.setFill(Color.GREY);
		Circle b6C = new Circle(); b6C.setRadius(45); b6C.setOpacity(0); b6C.setFill(Color.GREY);
		Circle c6C = new Circle(); c6C.setRadius(45); c6C.setOpacity(0); c6C.setFill(Color.GREY);
		Circle d6C = new Circle(); d6C.setRadius(45); d6C.setOpacity(0); d6C.setFill(Color.GREY);
		Circle e6C = new Circle(); e6C.setRadius(45); e6C.setOpacity(0); e6C.setFill(Color.GREY);
		Circle f6C = new Circle(); f6C.setRadius(45); f6C.setOpacity(0); f6C.setFill(Color.GREY);
		Circle g6C = new Circle(); g6C.setRadius(45); g6C.setOpacity(0); g6C.setFill(Color.GREY);
		Circle h6C = new Circle(); h6C.setRadius(45); h6C.setOpacity(0); h6C.setFill(Color.GREY);
			
		Circle a5C = new Circle(); a5C.setRadius(45); a5C.setOpacity(0); a5C.setFill(Color.GREY);	
		Circle b5C = new Circle(); b5C.setRadius(45); b5C.setOpacity(0); b5C.setFill(Color.GREY);
		Circle c5C = new Circle(); c5C.setRadius(45); c5C.setOpacity(0); c5C.setFill(Color.GREY);
		Circle d5C = new Circle(); d5C.setRadius(45); d5C.setOpacity(0); d5C.setFill(Color.GREY);
		Circle e5C = new Circle(); e5C.setRadius(45); e5C.setOpacity(0); e5C.setFill(Color.GREY);	
		Circle f5C = new Circle(); f5C.setRadius(45); f5C.setOpacity(0); f5C.setFill(Color.GREY);
		Circle g5C = new Circle(); g5C.setRadius(45); g5C.setOpacity(0); g5C.setFill(Color.GREY);
		Circle h5C = new Circle(); h5C.setRadius(45); h5C.setOpacity(0); h5C.setFill(Color.GREY);
		
		Circle a4C = new Circle(); a4C.setRadius(45); a4C.setOpacity(0); a4C.setFill(Color.GREY);
		Circle b4C = new Circle(); b4C.setRadius(45); b4C.setOpacity(0); b4C.setFill(Color.GREY);
		Circle c4C = new Circle(); c4C.setRadius(45); c4C.setOpacity(0); c4C.setFill(Color.GREY);
		Circle d4C = new Circle(); d4C.setRadius(45); d4C.setOpacity(0); d4C.setFill(Color.GREY);
		Circle e4C = new Circle(); e4C.setRadius(45); e4C.setOpacity(0); e4C.setFill(Color.GREY);
		Circle f4C = new Circle(); f4C.setRadius(45); f4C.setOpacity(0); f4C.setFill(Color.GREY);		
		Circle g4C = new Circle(); g4C.setRadius(45); g4C.setOpacity(0); g4C.setFill(Color.GREY);
		Circle h4C = new Circle(); h4C.setRadius(45); h4C.setOpacity(0); h4C.setFill(Color.GREY);
		
		Circle a3C = new Circle(); a3C.setRadius(45); a3C.setOpacity(0); a3C.setFill(Color.GREY);	
		Circle b3C = new Circle(); b3C.setRadius(45); b3C.setOpacity(0); b3C.setFill(Color.GREY);
		Circle c3C = new Circle(); c3C.setRadius(45); c3C.setOpacity(0); c3C.setFill(Color.GREY);
		Circle d3C = new Circle(); d3C.setRadius(45); d3C.setOpacity(0); d3C.setFill(Color.GREY);
		Circle e3C = new Circle(); e3C.setRadius(45); e3C.setOpacity(0); e3C.setFill(Color.GREY);	
		Circle f3C = new Circle(); f3C.setRadius(45); f3C.setOpacity(0); f3C.setFill(Color.GREY);
		Circle g3C = new Circle(); g3C.setRadius(45); g3C.setOpacity(0); g3C.setFill(Color.GREY);
		Circle h3C = new Circle(); h3C.setRadius(45); h3C.setOpacity(0); h3C.setFill(Color.GREY);
		
		Circle a2C = new Circle(); a2C.setRadius(45); a2C.setOpacity(0); a2C.setFill(Color.GREY);
		Circle b2C = new Circle(); b2C.setRadius(45); b2C.setOpacity(0); b2C.setFill(Color.GREY);
		Circle c2C = new Circle(); c2C.setRadius(45); c2C.setOpacity(0); c2C.setFill(Color.GREY);
		Circle d2C = new Circle(); d2C.setRadius(45); d2C.setOpacity(0); d2C.setFill(Color.GREY);
		Circle e2C = new Circle(); e2C.setRadius(45); e2C.setOpacity(0); e2C.setFill(Color.GREY);
		Circle f2C = new Circle(); f2C.setRadius(45); f2C.setOpacity(0); f2C.setFill(Color.GREY);
		Circle g2C = new Circle(); g2C.setRadius(45); g2C.setOpacity(0); g2C.setFill(Color.GREY);
		Circle h2C = new Circle(); h2C.setRadius(45); h2C.setOpacity(0); h2C.setFill(Color.GREY);
			
		Circle a1C = new Circle(); a1C.setRadius(45); a1C.setOpacity(0); a1C.setFill(Color.GREY);	
		Circle b1C = new Circle(); b1C.setRadius(45); b1C.setOpacity(0); b1C.setFill(Color.GREY);
		Circle c1C = new Circle(); c1C.setRadius(45); c1C.setOpacity(0); c1C.setFill(Color.GREY);	
		Circle d1C = new Circle(); d1C.setRadius(45); d1C.setOpacity(0); d1C.setFill(Color.GREY);
		Circle e1C = new Circle(); e1C.setRadius(45); e1C.setOpacity(0); e1C.setFill(Color.GREY);		
		Circle f1C = new Circle(); f1C.setRadius(45); f1C.setOpacity(0); f1C.setFill(Color.GREY);
		Circle g1C = new Circle(); g1C.setRadius(45); g1C.setOpacity(0); g1C.setFill(Color.GREY);	
		Circle h1C = new Circle(); h1C.setRadius(45); h1C.setOpacity(0); h1C.setFill(Color.GREY);

		//Add circles to an array
		
		circles = new Circle[]{a8C, b8C, c8C, d8C, e8C, f8C, g8C, h8C, a7C, b7C, c7C, d7C, e7C, f7C, g7C, h7C, a6C, b6C, c6C, d6C, e6C, f6C, g6C, h6C, a5C, b5C, c5C, d5C, e5C, f5C, g5C, h5C, a4C, b4C, c4C, d4C, e4C, f4C, g4C, h4C, a3C, b3C, c3C, d3C, e3C, f3C, g3C, h3C, a2C, b2C, c2C, d2C, e2C, f2C, g2C, h2C, a1C, b1C, c1C, d1C, e1C, f1C, g1C, h1C};

		//initialize return colour and last Square
		lastClickedSquareR = R;
		returnColor = Color.YELLOW;

		//add event handler for squares
		/*a8.setOnMouseClicked(event -> {System.out.println("a8 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(0), a8R, true);});
		b8.setOnMouseClicked(event -> {System.out.println("b8 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(1), b8R, false);});
		c8.setOnMouseClicked(event -> {System.out.println("c8 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(2), c8R, true);});
		d8.setOnMouseClicked(event -> {System.out.println("d8 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(3), d8R, false);});
		e8.setOnMouseClicked(event -> {System.out.println("e8 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(4), e8R, true);});
		f8.setOnMouseClicked(event -> {System.out.println("f8 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(5), f8R, false);});
		g8.setOnMouseClicked(event -> {System.out.println("g8 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(6), g8R, true);});
		h8.setOnMouseClicked(event -> {System.out.println("h8 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(7), h8R, false);});
		
		a7.setOnMouseClicked(event -> {System.out.println("a7 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(8), a7R, false);});
		b7.setOnMouseClicked(event -> {System.out.println("b7 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(9), b7R, true);});
		c7.setOnMouseClicked(event -> {System.out.println("c7 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(10), c7R, false);});
		d7.setOnMouseClicked(event -> {System.out.println("d7 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(11), d7R, true);});
		e7.setOnMouseClicked(event -> {System.out.println("e7 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(12), e7R, false);});
		f7.setOnMouseClicked(event -> {System.out.println("f7 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(13), f7R, true);});
		g7.setOnMouseClicked(event -> {System.out.println("g7 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(14), g7R, false);});
		h7.setOnMouseClicked(event -> {System.out.println("h7 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(15), h7R, true);});
		
		a6.setOnMouseClicked(event -> {System.out.println("a6 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(16), a6R, true);});
		b6.setOnMouseClicked(event -> {System.out.println("b6 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(17), b6R, false);});
		c6.setOnMouseClicked(event -> {System.out.println("c6 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(18), c6R, true);});
		d6.setOnMouseClicked(event -> {System.out.println("d6 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(19), d6R, false);});
		e6.setOnMouseClicked(event -> {System.out.println("e6 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(20), e6R, true);});
		f6.setOnMouseClicked(event -> {System.out.println("f6 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(21), f6R, false);});
		g6.setOnMouseClicked(event -> {System.out.println("g6 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(22), g6R, true);});
		h6.setOnMouseClicked(event -> {System.out.println("h6 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(23), h6R, false);});
		
		a5.setOnMouseClicked(event -> {System.out.println("a5 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(24), a5R, false);});
		b5.setOnMouseClicked(event -> {System.out.println("b5 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(25), b5R, true);});
		c5.setOnMouseClicked(event -> {System.out.println("c5 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(26), c5R, false);});
		d5.setOnMouseClicked(event -> {System.out.println("d5 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(27), d5R, true);});
		e5.setOnMouseClicked(event -> {System.out.println("e5 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(28), e5R, false);});
		f5.setOnMouseClicked(event -> {System.out.println("f5 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(29), f5R, true);});
		g5.setOnMouseClicked(event -> {System.out.println("g5 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(30), g5R, false);});
		h5.setOnMouseClicked(event -> {System.out.println("h5 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(31), h5R, true);});
		
		a4.setOnMouseClicked(event -> {System.out.println("a4 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(32), a4R, true);});
		b4.setOnMouseClicked(event -> {System.out.println("b4 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(33), b4R, false);});
		c4.setOnMouseClicked(event -> {System.out.println("c4 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(34), c4R, true);});
		d4.setOnMouseClicked(event -> {System.out.println("d4 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(35), d4R, false);});
		e4.setOnMouseClicked(event -> {System.out.println("e4 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(36), e4R, true);});
		f4.setOnMouseClicked(event -> {System.out.println("f4 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(37), f4R, false);});
		g4.setOnMouseClicked(event -> {System.out.println("g4 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(38), g4R, true);});
		h4.setOnMouseClicked(event -> {System.out.println("h4 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(39), h4R, false);});
		
		a3.setOnMouseClicked(event -> {System.out.println("a3 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(40), a3R, false);});
		b3.setOnMouseClicked(event -> {System.out.println("b3 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(41), b3R, true);});
		c3.setOnMouseClicked(event -> {System.out.println("c3 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(42), c3R, false);});
		d3.setOnMouseClicked(event -> {System.out.println("d3 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(43), d3R, true);});
		e3.setOnMouseClicked(event -> {System.out.println("e3 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(44), e3R, false);});
		f3.setOnMouseClicked(event -> {System.out.println("f3 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(45), f3R, true);});
		g3.setOnMouseClicked(event -> {System.out.println("g3 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(46), g3R, false);});
		h3.setOnMouseClicked(event -> {System.out.println("h3 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(47), h3R, true);});
		
		a2.setOnMouseClicked(event -> {System.out.println("a2 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(48), a2R, true);});
		b2.setOnMouseClicked(event -> {System.out.println("b2 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(49), b2R, false);});
		c2.setOnMouseClicked(event -> {System.out.println("c2 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(50), c2R, true);});
		d2.setOnMouseClicked(event -> {System.out.println("d2 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(51), d2R, false);});
		e2.setOnMouseClicked(event -> {System.out.println("e2 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(52), e2R, true);});
		f2.setOnMouseClicked(event -> {System.out.println("f2 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(53), f2R, false);});
		g2.setOnMouseClicked(event -> {System.out.println("g2 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(54), g2R, true);});
		h2.setOnMouseClicked(event -> {System.out.println("h2 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(55), h2R, false);});
		
		a1.setOnMouseClicked(event -> {System.out.println("a1 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(56), a1R, false);});
		b1.setOnMouseClicked(event -> {System.out.println("b1 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(57), b1R, true);});
		c1.setOnMouseClicked(event -> {System.out.println("c1 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(58), c1R, false);});
		d1.setOnMouseClicked(event -> {System.out.println("d1 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(59), d1R, true);});
		e1.setOnMouseClicked(event -> {System.out.println("e1 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(60), e1R, false);});
		f1.setOnMouseClicked(event -> {System.out.println("f1 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(61), f1R, true);});
		g1.setOnMouseClicked(event -> {System.out.println("g1 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(62), g1R, false);});
		h1.setOnMouseClicked(event -> {System.out.println("h1 clicked!"); handlePieceSelectionClick(game.getMoveGenerator().getBoard().getSquare(63), h1R, true);});
		*/
		//Add Rectangles to StackPane
		//a8.setAlignment(brV, javafx.geometry.Pos.CENTER);
		a8.getChildren().add(a8R); a7.getChildren().add(a7R); a6.getChildren().add(a6R); a5.getChildren().add(a5R); a4.getChildren().add(a4R); a3.getChildren().add(a3R); a2.getChildren().add(a2R); a1.getChildren().add(a1R);
		b8.getChildren().add(b8R); b7.getChildren().add(b7R); b6.getChildren().add(b6R); b5.getChildren().add(b5R); b4.getChildren().add(b4R); b3.getChildren().add(b3R); b2.getChildren().add(b2R); b1.getChildren().add(b1R);
		c8.getChildren().add(c8R); c7.getChildren().add(c7R); c6.getChildren().add(c6R); c5.getChildren().add(c5R); c4.getChildren().add(c4R); c3.getChildren().add(c3R); c2.getChildren().add(c2R); c1.getChildren().add(c1R);
		d8.getChildren().add(d8R); d7.getChildren().add(d7R); d6.getChildren().add(d6R); d5.getChildren().add(d5R); d4.getChildren().add(d4R); d3.getChildren().add(d3R); d2.getChildren().add(d2R); d1.getChildren().add(d1R);
		e8.getChildren().add(e8R); e7.getChildren().add(e7R); e6.getChildren().add(e6R); e5.getChildren().add(e5R); e4.getChildren().add(e4R); e3.getChildren().add(e3R); e2.getChildren().add(e2R); e1.getChildren().add(e1R);
		f8.getChildren().add(f8R); f7.getChildren().add(f7R); f6.getChildren().add(f6R); f5.getChildren().add(f5R); f4.getChildren().add(f4R); f3.getChildren().add(f3R); f2.getChildren().add(f2R); f1.getChildren().add(f1R);
		g8.getChildren().add(g8R); g7.getChildren().add(g7R); g6.getChildren().add(g6R); g5.getChildren().add(g5R); g4.getChildren().add(g4R); g3.getChildren().add(g3R); g2.getChildren().add(g2R); g1.getChildren().add(g1R);
		h8.getChildren().add(h8R); h7.getChildren().add(h7R); h6.getChildren().add(h6R); h5.getChildren().add(h5R); h4.getChildren().add(h4R); h3.getChildren().add(h3R); h2.getChildren().add(h2R); h1.getChildren().add(h1R);		
		
		//Add Circles to StackPane
		a8.getChildren().add(a8C); a7.getChildren().add(a7C); a6.getChildren().add(a6C); a5.getChildren().add(a5C); a4.getChildren().add(a4C); a3.getChildren().add(a3C); a2.getChildren().add(a2C); a1.getChildren().add(a1C);
		b8.getChildren().add(b8C); b7.getChildren().add(b7C); b6.getChildren().add(b6C); b5.getChildren().add(b5C); b4.getChildren().add(b4C); b3.getChildren().add(b3C); b2.getChildren().add(b2C); b1.getChildren().add(b1C);
		c8.getChildren().add(c8C); c7.getChildren().add(c7C); c6.getChildren().add(c6C); c5.getChildren().add(c5C); c4.getChildren().add(c4C); c3.getChildren().add(c3C); c2.getChildren().add(c2C); c1.getChildren().add(c1C);
		d8.getChildren().add(d8C); d7.getChildren().add(d7C); d6.getChildren().add(d6C); d5.getChildren().add(d5C); d4.getChildren().add(d4C); d3.getChildren().add(d3C); d2.getChildren().add(d2C); d1.getChildren().add(d1C);
		e8.getChildren().add(e8C); e7.getChildren().add(e7C); e6.getChildren().add(e6C); e5.getChildren().add(e5C); e4.getChildren().add(e4C); e3.getChildren().add(e3C); e2.getChildren().add(e2C); e1.getChildren().add(e1C);
		f8.getChildren().add(f8C); f7.getChildren().add(f7C); f6.getChildren().add(f6C); f5.getChildren().add(f5C); f4.getChildren().add(f4C); f3.getChildren().add(f3C); f2.getChildren().add(f2C); f1.getChildren().add(f1C);
		g8.getChildren().add(g8C); g7.getChildren().add(g7C); g6.getChildren().add(g6C); g5.getChildren().add(g5C); g4.getChildren().add(g4C); g3.getChildren().add(g3C); g2.getChildren().add(g2C); g1.getChildren().add(g1C);
		h8.getChildren().add(h8C); h7.getChildren().add(h7C); h6.getChildren().add(h6C); h5.getChildren().add(h5C); h4.getChildren().add(h4C); h3.getChildren().add(h3C); h2.getChildren().add(h2C); h1.getChildren().add(h1C);	
		

		//Create board GridPane
		boardPane = new GridPane();
		
		//Add squares to boardPane
		boardPane.addRow(0, new Label(""), new Label("A"), new Label("B"), new Label("C"), new Label("D"), new Label("E"), new Label("F"), new Label("G"), new Label("H"));
		boardPane.addRow(1, new Label("8"), a8, b8, c8, d8, e8, f8, g8, h8);
		boardPane.addRow(2, new Label("7"), a7, b7, c7, d7, e7, f7, g7, h7);
		boardPane.addRow(3, new Label("6"), a6, b6, c6, d6, e6, f6, g6, h6);
		boardPane.addRow(4, new Label("5"), a5, b5, c5, d5, e5, f5, g5, h5);
		boardPane.addRow(5, new Label("4"), a4, b4, c4, d4, e4, f4, g4, h4);
		boardPane.addRow(6, new Label("3"), a3, b3, c3, d3, e3, f3, g3, h3);
		boardPane.addRow(7, new Label("2"), a2, b2, c2, d2, e2, f2, g2, h2);
		boardPane.addRow(8, new Label("1"), a1, b1, c1, d1, e1, f1, g1, h1);
		
	}
	
	public void showBlackPromotion() {
		squares = new StackPane[]{a8,b8,c8,d8,e8,f8,g8,h8,a7,b7,c7,d7,e7,f7,g7,h7,a6,b6,c6,d6,e6,f6,g6,h6,a5,b5,c5,d5,e5,f5,g5,h5,a4,b4,c4,d4,e4,f4,g4,h4,a3,b3,c3,d3,e3,f3,g3,h3,a2,b2,c2,d2,e2,f2,g2,h2,a1,b1,c1,d1,e1,f1,g1,h1};
		Stage blackPromotionStage = new Stage();
        blackPromotionStage.setTitle("Promotion Selection");
		HBox blackPromotionRoot = new HBox();
		StackPane knightSP = new StackPane(); StackPane bishopSP = new StackPane(); StackPane rookSP = new StackPane(); StackPane QueenSP = new StackPane();
		Rectangle knightR = new Rectangle(); knightR.setWidth(100); knightR.setHeight(100); knightR.setFill(Color.GREY);
        Rectangle bishopR = new Rectangle(); bishopR.setWidth(100); bishopR.setHeight(100); bishopR.setFill(Color.GREY);
        Rectangle rookR = new Rectangle(); rookR.setWidth(100); rookR.setHeight(100); rookR.setFill(Color.GREY);
        Rectangle queenR = new Rectangle(); queenR.setWidth(100); queenR.setHeight(100); queenR.setFill(Color.GREY);
		Image knightI = new Image("Images//bn.png"); Image bishopI= new Image("Images//bb.png"); Image rookI = new Image("Images//br.png"); Image queenI = new Image("Images//bq.png");
		ImageView knightIV = new ImageView(knightI); knightIV.setStyle("-fx-background-color: transparent;");
		ImageView bishopIV = new ImageView(bishopI); bishopIV.setStyle("-fx-background-color: transparent;");
		ImageView rookIV = new ImageView(rookI); rookIV.setStyle("-fx-background-color: transparent;");
		ImageView queenIV = new ImageView(queenI); queenIV.setStyle("-fx-background-color: transparent;");
		knightSP.getChildren().addAll(knightR, knightIV); bishopSP.getChildren().addAll(bishopR, bishopIV); rookSP.getChildren().addAll(rookR, rookIV); QueenSP.getChildren().addAll(queenR, queenIV);
		blackPromotionRoot.getChildren().addAll( knightSP, bishopSP, rookSP, QueenSP);
		Scene blackPromotionScene = new Scene(blackPromotionRoot, 400, 100);
		knightSP.setOnMouseClicked(event -> {handlePromotionPieceClick('N'); blackPromotionStage.close(); System.out.println("Knight selected!");});
		bishopSP.setOnMouseClicked(event -> {handlePromotionPieceClick('B'); blackPromotionStage.close(); System.out.println("Bishop selected!");});
		rookSP.setOnMouseClicked(event -> {handlePromotionPieceClick('R'); blackPromotionStage.close(); System.out.println("Rook selected!");});
		QueenSP.setOnMouseClicked(event -> {handlePromotionPieceClick('Q'); blackPromotionStage.close(); System.out.println("Queen selected!");});
        blackPromotionStage.setScene(blackPromotionScene);
        blackPromotionStage.showAndWait();
	}
	
	public void showWhitePromotion() {
		Stage whitePromotionStage = new Stage();
        whitePromotionStage.setTitle("Promotion Selection");
		HBox whitePromotionRoot = new HBox();
		StackPane knightSP = new StackPane(); StackPane bishopSP = new StackPane(); StackPane rookSP = new StackPane(); StackPane QueenSP = new StackPane();
		Rectangle knightR = new Rectangle(); knightR.setWidth(100); knightR.setHeight(100); knightR.setFill(Color.GREY);
        Rectangle bishopR = new Rectangle(); bishopR.setWidth(100); bishopR.setHeight(100); bishopR.setFill(Color.GREY);
        Rectangle rookR = new Rectangle(); rookR.setWidth(100); rookR.setHeight(100); rookR.setFill(Color.GREY);
        Rectangle queenR = new Rectangle(); queenR.setWidth(100); queenR.setHeight(100); queenR.setFill(Color.GREY);
		Image knightI = new Image("Images//wn.png"); Image bishopI= new Image("Images//wb.png"); Image rookI = new Image("Images//wr.png"); Image queenI = new Image("Images//wq.png");
		ImageView knightIV = new ImageView(knightI); knightIV.setStyle("-fx-background-color: transparent;");
		ImageView bishopIV = new ImageView(bishopI); bishopIV.setStyle("-fx-background-color: transparent;");
		ImageView rookIV = new ImageView(rookI); rookIV.setStyle("-fx-background-color: transparent;");
		ImageView queenIV = new ImageView(queenI); queenIV.setStyle("-fx-background-color: transparent;");
		knightSP.getChildren().addAll(knightR, knightIV); bishopSP.getChildren().addAll(bishopR, bishopIV); rookSP.getChildren().addAll(rookR, rookIV); QueenSP.getChildren().addAll(queenR, queenIV);
		whitePromotionRoot.getChildren().addAll( knightSP, bishopSP, rookSP, QueenSP);
		Scene whitePromotionScene = new Scene(whitePromotionRoot, 400, 100);
		knightSP.setOnMouseClicked(event -> {handlePromotionPieceClick('N'); whitePromotionStage.close(); System.out.println("Knight selected!");});
		bishopSP.setOnMouseClicked(event -> {handlePromotionPieceClick('B'); whitePromotionStage.close(); System.out.println("Bishop selected!");});
		rookSP.setOnMouseClicked(event -> {handlePromotionPieceClick('R'); whitePromotionStage.close(); System.out.println("Rook selected!");});
		QueenSP.setOnMouseClicked(event -> {handlePromotionPieceClick('Q'); whitePromotionStage.close(); System.out.println("Queen selected!");});
        whitePromotionStage.setScene(whitePromotionScene);
        whitePromotionStage.showAndWait();
	}
	
	public void handlePromotionPieceClick(char selectedPiece) {
		if (selectedPiece == 'N') {
			System.out.println("Promote to knight");
		}
		else if (selectedPiece == 'B') {
			System.out.println("Promote to Bishop");
		}
		else if (selectedPiece == 'R') {
			System.out.println("Promote to Rook");
		}
		else if (selectedPiece == 'Q') {
			System.out.println("Promote to Queen");
		}	
		else {
			System.out.println("ERROR: Invalid piece for black to promote to!!");
		}
		
	}	
	
	//Updates GUI board from bit boards
	/*public void updateBoardDisplay() {
		System.out.println("Updating Board Display");
		Board board = game.getMoveGenerator().getBoard();
		ImageView piecePNG;
		long start = 0b1L;
		long checking;
		long whitePawnBB = board.getWhitePawnBB();
		long blackPawnBB = board.getBlackPawnBB();
		long whiteKnightBB = board.getWhiteKnightBB();
		long blackKnightBB = board.getBlackKnightBB();
		long whiteBishopBB = board.getWhiteBishopBB();
		long blackBishopBB = board.getBlackBishopBB();
		long whiteRookBB = board.getWhiteRookBB();
		long blackRookBB = board.getBlackRookBB();
		long whiteQueenBB = board.getWhiteQueenBB();
		long blackQueenBB = board.getBlackQueenBB();
		long whiteKingBB = board.getWhiteKingBB();
		long blackKingBB = board.getBlackKingBB();
		for (int i = 0; i < squares.length; i++) {
			checking = start << i;
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(wpI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, blackPawnBB)) {
				piecePNG = new ImageView(bpI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(wnI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(bnI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(wbI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(bbI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(wrI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(brI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(wqI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(bqI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(wkI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			if (game.getMoveGenerator().isOnBB(checking, whitePawnBB)) {
				piecePNG = new ImageView(bkI);
				piecePNG.setStyle("-fx-background-color: transparent;");
				squares[i].getChildren().add(piecePNG);
				break;				
			}
			break;
		}
	}*/
	
	public void setPieces(String fen) {
		clearBoard();
		//Read FEN from Game
		squares = new StackPane[]{a8,b8,c8,d8,e8,f8,g8,h8,a7,b7,c7,d7,e7,f7,g7,h7,a6,b6,c6,d6,e6,f6,g6,h6,a5,b5,c5,d5,e5,f5,g5,h5,a4,b4,c4,d4,e4,f4,g4,h4,a3,b3,c3,d3,e3,f3,g3,h3,a2,b2,c2,d2,e2,f2,g2,h2,a1,b1,c1,d1,e1,f1,g1,h1};
		Scanner FENScanner = new Scanner(fen);
		FENScanner.useDelimiter("");
		String piece;
		ImageView piecePNG;
		int skip;
		for (int i = 0; i < squares.length; i++) {
			try {
			piece = FENScanner.next();
			//System.out.print("Piece: " + piece);
			}
			catch(Exception e) {
				//System.out.print("break");
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
					piecePNG = new ImageView(wpI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				case "p":
					piecePNG = new ImageView(bpI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				case "N":
					piecePNG = new ImageView(wnI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				case "n":
					piecePNG = new ImageView(bnI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				case "B":
					piecePNG = new ImageView(wbI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				case "b":
					piecePNG = new ImageView(bbI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				case "R":
					piecePNG = new ImageView(wrI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				case "r":
					piecePNG = new ImageView(brI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				case "Q":
					piecePNG = new ImageView(wqI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				case "q":
					piecePNG = new ImageView(bqI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;			
				case "K":
					piecePNG = new ImageView(wkI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				case "k":
					piecePNG = new ImageView(bkI);
					piecePNG.setStyle("-fx-background-color: transparent;");
					squares[i].getChildren().add(piecePNG);
					break;
				default:
				}
			}
		}
		//Add Pieces to board
	}
	
	public void clearBoard() {
		for (StackPane square: squares) {
			square.getChildren().removeIf(node -> node instanceof ImageView);
		}
	}
	
	public void onEngineMove(Move move) {
		Platform.runLater(() -> {movePieces(move);});
	}
	
	public void movePieces(Move move) {
		
		long on = move.getOn();
		long to = move.getTo();
		long piece = move.getMovingPiece();
		boolean isWhite = move.getForWhite();
		
		squares = new StackPane[]{a8,b8,c8,d8,e8,f8,g8,h8,a7,b7,c7,d7,e7,f7,g7,h7,a6,b6,c6,d6,e6,f6,g6,h6,a5,b5,c5,d5,e5,f5,g5,h5,a4,b4,c4,d4,e4,f4,g4,h4,a3,b3,c3,d3,e3,f3,g3,h3,a2,b2,c2,d2,e2,f2,g2,h2,a1,b1,c1,d1,e1,f1,g1,h1};
		ImageView piecePNG;
		int onIndex = 63 - Long.numberOfTrailingZeros(on); 
		int toIndex = 63 - Long.numberOfTrailingZeros(to);
		int lastIndex;
		if (piece == 0b1L) {
			piecePNG = new ImageView(wpI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else if (piece == 0b10L) {
			piecePNG = new ImageView(bpI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else if (piece == 0b11L & isWhite) {
			piecePNG = new ImageView(wnI);
			piecePNG.setStyle("-fx-background-color: transparent;");
			}
		else if (piece == 0b11L & !isWhite) {
			piecePNG = new ImageView(bnI);
			piecePNG.setStyle("-fx-background-color: transparent;");
			}
		else if (piece == 0b100L & isWhite) {
			piecePNG = new ImageView(wbI);
			piecePNG.setStyle("-fx-background-color: transparent;");
			}
		else if (piece == 0b100L & !isWhite) {
			piecePNG = new ImageView(bbI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else if (piece == 0b101L & isWhite) {
			piecePNG = new ImageView(wrI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else if (piece == 0b101L & !isWhite) {
			piecePNG = new ImageView(brI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else if (piece == 0b110L & isWhite) {
			piecePNG = new ImageView(wqI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else if (piece == 0b110L & !isWhite) {
			piecePNG = new ImageView(bqI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else if (piece == 0b111L & isWhite) {
			piecePNG = new ImageView(wkI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else if (piece == 0b111L & !isWhite) {
			piecePNG = new ImageView(bkI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else {
			piecePNG = new ImageView();
		}
		squares[toIndex].getChildren().removeIf(node -> node instanceof ImageView);
		squares[toIndex].getChildren().add(piecePNG);
		squares[onIndex].getChildren().removeIf(node -> node instanceof ImageView);
	}
	
	public void movePiecesEnPassant(EnPassant enPassant) {
		
		long on = enPassant.getOn();
		long to = enPassant.getTo();
		long piece = enPassant.getMovingPiece();
		boolean isWhite = enPassant.getForWhite();
		
		squares = new StackPane[]{a8,b8,c8,d8,e8,f8,g8,h8,a7,b7,c7,d7,e7,f7,g7,h7,a6,b6,c6,d6,e6,f6,g6,h6,a5,b5,c5,d5,e5,f5,g5,h5,a4,b4,c4,d4,e4,f4,g4,h4,a3,b3,c3,d3,e3,f3,g3,h3,a2,b2,c2,d2,e2,f2,g2,h2,a1,b1,c1,d1,e1,f1,g1,h1};
		ImageView piecePNG;
		int onIndex = 63 - Long.numberOfTrailingZeros(on); 
		int toIndex = 63 - Long.numberOfTrailingZeros(to);
		int EPIndex;
		if (piece == 0b1L) {
			EPIndex = 63 - Long.numberOfTrailingZeros(to) + 8;
			piecePNG = new ImageView(wpI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else if (piece == 0b10L) {
			EPIndex = 63 - Long.numberOfTrailingZeros(to) - 8;
			piecePNG = new ImageView(bpI);
			piecePNG.setStyle("-fx-background-color: transparent;");
		}
		else {
			piecePNG = new ImageView();
			EPIndex = 0;
		}
		squares[toIndex].getChildren().removeIf(node -> node instanceof ImageView);
		squares[toIndex].getChildren().add(piecePNG);
		squares[onIndex].getChildren().removeIf(node -> node instanceof ImageView);
		squares[EPIndex].getChildren().removeIf(node -> node instanceof ImageView);
	}
	
	public void movePiecesCastle(Castle castle) {
		
		char side = castle.getSide();
		boolean isWhite = castle.getForWhite();
		
		squares = new StackPane[]{a8,b8,c8,d8,e8,f8,g8,h8,a7,b7,c7,d7,e7,f7,g7,h7,a6,b6,c6,d6,e6,f6,g6,h6,a5,b5,c5,d5,e5,f5,g5,h5,a4,b4,c4,d4,e4,f4,g4,h4,a3,b3,c3,d3,e3,f3,g3,h3,a2,b2,c2,d2,e2,f2,g2,h2,a1,b1,c1,d1,e1,f1,g1,h1};
		ImageView kingPNG;
		ImageView rookPNG;
		int kingOnIndex;
		int kingToIndex;
		int rookOnIndex;
		int rookToIndex;
		if (isWhite) {
			kingPNG = new ImageView(wkI);
			kingPNG.setStyle("-fx-background-color: transparent;");
			rookPNG = new ImageView(wrI);
			rookPNG.setStyle("-fx-background-color: transparent;");
			if(side == 'Q') {
				kingOnIndex = 60;
				kingToIndex = 58;
				rookOnIndex = 56;
				rookToIndex = 59;
			}
			else {
				kingOnIndex = 60;
				kingToIndex = 62;
				rookOnIndex = 63;
				rookToIndex = 61;
			}
		}
		else {
			kingPNG = new ImageView(bkI);
			kingPNG.setStyle("-fx-background-color: transparent;");
			rookPNG = new ImageView(brI);
			rookPNG.setStyle("-fx-background-color: transparent;");
			if(side == 'Q') {
				kingOnIndex = 4;
				kingToIndex = 2;
				rookOnIndex = 0;
				rookToIndex = 3;
			}
			else {
				kingOnIndex = 4;
				kingToIndex = 6;
				rookOnIndex = 7;
				rookToIndex = 5;
				
			}
		}
		squares[kingOnIndex].getChildren().removeIf(node -> node instanceof ImageView);
		squares[rookOnIndex].getChildren().removeIf(node -> node instanceof ImageView);
		squares[kingToIndex].getChildren().add(kingPNG);
		squares[rookToIndex].getChildren().add(rookPNG);
	}
}