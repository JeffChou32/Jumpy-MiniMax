import java.io.*;
import java.util.*;

public class MiniMaxImproved {

    static int evalCount = 0;  // counter to track how many positions we evaluated

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {                                       // check for correct number of arguments
            System.out.println("needs 3 args: <inputfile.txt> <outputfile.txt> <maxDepth>");
            return;
        }

        String inputFile = args[0];                                   // input file name
        String outputFile = args[1];                                  // output file name
        int maxDepth = Integer.parseInt(args[2]);                     // max depth

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String startBoard = reader.readLine().trim();                 // read board
        reader.close();

        String bestBoard = startBoard;                               // to store best board we find
        int bestScore = 0;                                           // stores best score found

        if (maxDepth == 0) {                                         
            bestScore = staticEst(startBoard);
            evalCount++;
        } else {
            int highestSoFar = Integer.MIN_VALUE;                    // track highest score across moves
            for (String nextBoard : whiteMoves(startBoard)) {     // generate all white moves
                int moveScore = miniMax(nextBoard, maxDepth - 1, false); // run minimax from next position
                if (moveScore > highestSoFar) {                      // update if better
                    highestSoFar = moveScore;
                    bestBoard = nextBoard;
                }
            }
            bestScore = highestSoFar;
        }

        System.out.println("Board Position: " + bestBoard);          // print best board found
        System.out.println("Positions evaluated by static estimation: " + evalCount + ".");
        System.out.println("MINIMAX estimate: " + bestScore + ".");

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(bestBoard);                                     // write best board to output file
        writer.newLine();
        writer.close();
    }

    public static int miniMax(String board, int depth, boolean isWhiteTurn) {
        if (depth == 0) {                                            // base case
            evalCount++;
            return staticEst(board);
        }

        int bestScore = isWhiteTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        List<String> nextMoves = isWhiteTurn ? whiteMoves(board) : new ArrayList<>(); // only white moves here
        if (nextMoves.isEmpty()) {
            evalCount++;
            return staticEst(board);  // fallback to static evaluation
        }

        for (String move : nextMoves) {
            int score = miniMax(move, depth - 1, !isWhiteTurn);     // flip player turn each call
            if (isWhiteTurn) bestScore = Math.max(bestScore, score);
            else bestScore = Math.min(bestScore, score);
        }

        return bestScore;                                           // return the best found score
    }

    public static List<String> whiteMoves(String board) {
        List<String> moves = new ArrayList<>();
        int w1 = Character.getNumericValue(board.charAt(0));
        int w2 = Character.getNumericValue(board.charAt(1));
        int b1 = Character.getNumericValue(board.charAt(2));
        int b2 = Character.getNumericValue(board.charAt(3));

        // W1 move
        if (w1 != 9) {
            if (w1 == 8) {
                moves.add("9" + w2 + b1 + b2);
            } else if (w1 + 1 != b1 && w1 + 1 != b2  && w1 + 1 != w2) {
                moves.add((w1 + 1) + "" + w2 + b1 + b2);
            } else if ((w1 + 2 != b1 && w1 + 2 != b2 && w1 + 2 != w2) || w1 + 2 == 9) {
                int newW1 = w1 + 2;
                if (w1 + 1 == b1) {
                    if (8 != w1 && 8 != w2 && 8 != b2) b1 = 8;
                    else if (7 != w1 && 7 != w2 && 7 != b2) b1 = 7;
                    else if (6 != w1 && 6 != w2 && 6 != b2) b1 = 6;
                    else if (5 != w1 && 5 != w2 && 5 != b2) b1 = 5;
                }
                if (w1 + 1 == b2) {
                    if (8 != w1 && 8 != w2 && 8 != b1) b2 = 8;
                    else if (7 != w1 && 7 != w2 && 7 != b1) b2 = 7;
                    else if (6 != w1 && 6 != w2 && 6 != b1) b2 = 6;
                    else if (5 != w1 && 5 != w2 && 5 != b1) b2 = 5;
                }
                moves.add(newW1 + "" + w2 + b1 + b2);
            } else if ((w1 + 3 != w2 && w1 + 3 != b1 && w1 + 3 != b2) || w1 + 3 == 9) {
                moves.add((w1 + 3) + "" + w2 + b1 + b2);
            } else if ((w1 + 4 != w2 && w1 + 4 != b1 && w1 + 4 != b2) || w1 + 4 == 9) {
                moves.add((w1 + 4) + "" + w2 + b1 + b2);
            } else if ((w1 + 5 != w2 && w1 + 5 != b1 && w1 + 5 != b2) || w1 + 5 == 9) {
                moves.add((w1 + 5) + "" + w2 + b1 + b2);
            }
        }

        // W2 move
        if (w2 != 9) {
            if (w2 == 8) {
                moves.add(w1 + "9" + b1 + b2);
            } else if (w2 + 1 != w1 && w2 + 1 != b1 && w2 + 1 != b2) {
                moves.add(w1 + "" + (w2 + 1) + b1 + b2);
            } else if ((w2 + 2 != w1 && w2 + 2 != b1 && w2 + 2 != b2) || w2 + 2 == 9) {
                int newW2 = w2 + 2;
                if (w2 + 1 == b1) {
                    if (8 != w1 && 8 != w2 && 8 != b2) b1 = 8;
                    else if (7 != w1 && 7 != w2 && 7 != b2) b1 = 7;
                    else if (6 != w1 && 6 != w2 && 6 != b2) b1 = 6;
                    else if (5 != w1 && 5 != w2 && 5 != b2) b1 = 5;
                }
                if (w2 + 1 == b2) {
                    if (8 != w1 && 8 != w2 && 8 != b1) b2 = 8;
                    else if (7 != w1 && 7 != w2 && 7 != b1) b2 = 7;
                    else if (6 != w1 && 6 != w2 && 6 != b1) b2 = 6;
                    else if (5 != w1 && 5 != w2 && 5 != b1) b2 = 5;
                }
                moves.add(w1 + "" + newW2 + b1 + b2);
            } else if ((w2 + 3 != w1 && w2 + 3 != b1 && w2 + 3 != b2) || w2 + 3 == 9) {
                moves.add(w1 + "" + (w2 + 3) + b1 + b2);
            } else if ((w2 + 4 != w1 && w2 + 4 != b1 && w2 + 4 != b2) || w2 + 4 == 9) {
                moves.add(w1 + "" + (w2 + 4) + b1 + b2);
            } else if ((w2 + 5 != w1 && w2 + 5 != b1 && w2 + 5 != b2) || w2 + 5 == 9) {
                moves.add(w1 + "" + (w2 + 5) + b1 + b2);
            }
        }
        return moves;
    }


    public static int staticEst(String board) {
        int w1 = Character.getNumericValue(board.charAt(0));
        int w2 = Character.getNumericValue(board.charAt(1));
        int b1 = Character.getNumericValue(board.charAt(2));
        int b2 = Character.getNumericValue(board.charAt(3));

        if (w1 == 9 && w2 == 9) return 100;               // white wins
        if (b1 == 0 && b2 == 0) return -100;             // black wins
        int whiteScore = (int)Math.pow(w1, 2) + (int)Math.pow(w2, 2);
        int blackScore = (int)Math.pow(8 - b1, 2) + (int)Math.pow(8 - b2, 2);
        if (w1 != 9) whiteScore += 5;
        if (w2 != 9) whiteScore += 5;
        if (b1 != 0) blackScore += 5;
        if (b2 != 0) blackScore += 5;

        return whiteScore - blackScore;
    }       
};

