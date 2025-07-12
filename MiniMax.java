import java.io.*;
import java.util.*;

public class MiniMax {

    static int evalCount = 0;  // counter to track how many positions we evaluated

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {                                       // check for correct number of arguments
            System.out.println("invalid args");
            return;
        }

        String inputFile = args[0];                                   // input file name
        String outputFile = args[1];                                  // output file name
        int maxDepth = Integer.parseInt(args[2]);                     // how deep we search

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
            for (String nextBoard : getWhiteMoves(startBoard)) {     // generate all white moves
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
        if (depth == 0) {                                            // base case: evaluate board
            evalCount++;
            return staticEst(board);
        }

        int bestScore = isWhiteTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        List<String> nextMoves = isWhiteTurn ? getWhiteMoves(board) : new ArrayList<>(); // only white moves here
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

    public static List<String> getWhiteMoves(String board) {
        List<String> moves = new ArrayList<>();
        int w1 = Character.getNumericValue(board.charAt(0));
        int w2 = Character.getNumericValue(board.charAt(1));
        int b1 = Character.getNumericValue(board.charAt(2));
        int b2 = Character.getNumericValue(board.charAt(3));

        // Try moving white piece 1 (w1)
        if (w1 != 9) {                                              // skip if already out
            if (w1 == 8) moves.add("9" + w2 + b1 + b2);             // move off board
            else if (isFree(w1 + 1, w2, b1, b2)) moves.add((w1 + 1) + "" + w2 + b1 + b2);
            else if (isFree(w1 + 2, w2, b1, b2) || w1 + 2 == 9) {
                int newW1 = w1 + 2;
                if (w1 + 1 == b1) b1 = bumpBlack(b1, w1, w2, b2);
                if (w1 + 1 == b2) b2 = bumpBlack(b2, w1, w2, b1);
                moves.add(newW1 + "" + w2 + b1 + b2);
            } else if (isFree(w1 + 3, w2, b1, b2) || w1 + 3 == 9) moves.add((w1 + 3) + "" + w2 + b1 + b2);
            else if (isFree(w1 + 4, w2, b1, b2) || w1 + 4 == 9) moves.add((w1 + 4) + "" + w2 + b1 + b2);
            else if (isFree(w1 + 5, w2, b1, b2) || w1 + 5 == 9) moves.add((w1 + 5) + "" + w2 + b1 + b2);
        }

        // Try moving white piece 2 (w2) -- same logic
        if (w2 != 9) {
            if (w2 == 8) moves.add(w1 + "9" + b1 + b2);
            else if (isFree(w2 + 1, w1, b1, b2)) moves.add(w1 + "" + (w2 + 1) + b1 + b2);
            else if (isFree(w2 + 2, w1, b1, b2) || w2 + 2 == 9) {
                int newW2 = w2 + 2;
                if (w2 + 1 == b1) b1 = bumpBlack(b1, w1, w2, b2);
                if (w2 + 1 == b2) b2 = bumpBlack(b2, w1, w2, b1);
                moves.add(w1 + "" + newW2 + b1 + b2);
            } else if (isFree(w2 + 3, w1, b1, b2) || w2 + 3 == 9) moves.add(w1 + "" + (w2 + 3) + b1 + b2);
            else if (isFree(w2 + 4, w1, b1, b2) || w2 + 4 == 9) moves.add(w1 + "" + (w2 + 4) + b1 + b2);
            else if (isFree(w2 + 5, w1, b1, b2) || w2 + 5 == 9) moves.add(w1 + "" + (w2 + 5) + b1 + b2);
        }

        return moves;                                               // all legal moves from this board
    }

    public static int bumpBlack(int blackPiece, int w1, int w2, int otherBlack) {
        if (isFree(8, w1, w2, otherBlack)) return 8;                // bump to furthest right free spot
        if (isFree(7, w1, w2, otherBlack)) return 7;
        if (isFree(6, w1, w2, otherBlack)) return 6;
        if (isFree(5, w1, w2, otherBlack)) return 5;
        return blackPiece;                                          // if no spot free, stay put
    }

    public static boolean isFree(int pos, int w1, int w2, int b) {
        return pos != w1 && pos != w2 && pos != b;                 // check if spot is free
    }

    public static int staticEst(String board) {
        int white1 = Character.getNumericValue(board.charAt(0));
        int white2 = Character.getNumericValue(board.charAt(1));
        int black1 = Character.getNumericValue(board.charAt(2));
        int black2 = Character.getNumericValue(board.charAt(3));

        if (white1 == 9 && white2 == 9) return 100;               // white wins
        if (black1 == 0 && black2 == 0) return -100;             // black wins
        return white1 + white2 + black1 + black2 - 18;           // basic heuristic
    }
}
