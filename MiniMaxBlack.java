import java.io.*;
import java.util.*;

public class MiniMaxBlack {

    static int evalCount = 0;  // counter to track how many positions we evaluated

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {                                        // check for correct number of arguments
            System.out.println("needs 3 args: <inputfile.txt> <outputfile.txt> <maxDepth>");
            return;
        }

        String inputFile = args[0];                                    // input file name
        String outputFile = args[1];                                   // output file name
        int maxDepth = Integer.parseInt(args[2]);                      // max depth

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String startBoard = reader.readLine().trim();                  // read board
        reader.close();

        String bestBoard = startBoard;                                // store best board we find
        int bestScore = Integer.MAX_VALUE;                            // start at +∞ because Black minimizes

        List<String> blackNextMoves = blackMoves(startBoard);         // get Black's possible moves
        if (blackNextMoves.isEmpty()) {
            bestScore = staticEst(startBoard);                        // no moves → fallback to static eval
            evalCount++;
        } else {
            for (String nextBoard : blackNextMoves) {
                int moveScore = miniMax(nextBoard, maxDepth - 1, true);  // White's turn next (maximize)
                if (moveScore < bestScore) {                          // Black wants minimal score
                    bestScore = moveScore;
                    bestBoard = nextBoard;
                }
            }
        }

        System.out.println("Board Position: " + bestBoard);           // print best board
        System.out.println("Positions evaluated by static estimation: " + evalCount + ".");
        System.out.println("MINIMAX estimate: " + bestScore + ".");

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(bestBoard);                                      // write best board to output file
        writer.newLine();
        writer.close();
    }

    public static int miniMax(String board, int depth, boolean isWhiteTurn) {
        if (depth == 0) {                                             // base case
            evalCount++;
            return staticEst(board);
        }

        List<String> nextMoves = isWhiteTurn ? whiteMoves(board) : blackMoves(board);
        if (nextMoves.isEmpty()) {
            evalCount++;
            return staticEst(board);                                  // no moves → evaluate statically
        }

        int bestScore = isWhiteTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (String move : nextMoves) {
            int score = miniMax(move, depth - 1, !isWhiteTurn);
            if (isWhiteTurn) bestScore = Math.max(bestScore, score); // maximize White's turn
            else bestScore = Math.min(bestScore, score);             // minimize Black's turn
        }

        return bestScore;
    }

    public static List<String> whiteMoves(String board) {
        List<String> moves = new ArrayList<>();
        int w1 = Character.getNumericValue(board.charAt(0));
        int w2 = Character.getNumericValue(board.charAt(1));
        int b1 = Character.getNumericValue(board.charAt(2));
        int b2 = Character.getNumericValue(board.charAt(3));

        if (w1 != 9) {
            if (w1 == 8) moves.add("9" + w2 + b1 + b2);
            else if (w1 + 1 != w2 && w1 + 1 != b1 && w1 + 1 != b2) moves.add((w1 + 1) + "" + w2 + b1 + b2);
            else if (w1 + 2 <= 9 && (w1 + 2 != w2 && w1 + 2 != b1 && w1 + 2 != b2)) moves.add((w1 + 2) + "" + w2 + b1 + b2);
        }
        if (w2 != 9) {
            if (w2 == 8) moves.add(w1 + "9" + b1 + b2);
            else if (w2 + 1 != w1 && w2 + 1 != b1 && w2 + 1 != b2) moves.add(w1 + "" + (w2 + 1) + b1 + b2);
            else if (w2 + 2 <= 9 && (w2 + 2 != w1 && w2 + 2 != b1 && w2 + 2 != b2)) moves.add(w1 + "" + (w2 + 2) + b1 + b2);
        }

        return moves;
    }


    public static List<String> blackMoves(String board) {
        List<String> moves = new ArrayList<>();
        int w1 = Character.getNumericValue(board.charAt(0));
        int w2 = Character.getNumericValue(board.charAt(1));
        int b1 = Character.getNumericValue(board.charAt(2));
        int b2 = Character.getNumericValue(board.charAt(3));

        if (b1 != 0) {            
            if (b1 == 1) moves.add("" + w1 + w2 + "0" + b2);           
            if (b1 - 1 >= 1 && b1 - 1 != w1 && b1 - 1 != w2 && b1 - 1 != b2) moves.add("" + w1 + w2 + (b1 - 1) + b2);
            if (b1 - 2 == 0) moves.add("" + w1 + w2 + "0" + b2);
            else if (b1 - 2 >= 1 && b1 - 2 != w1 && b1 - 2 != w2 && b1 - 2 != b2) moves.add("" + w1 + w2 + (b1 - 2) + b2);
        }

        if (b2 != 0) {
            if (b2 == 1) moves.add("" + w1 + w2 + b1 + "0");            
            if (b2 - 1 >= 1 && b2 - 1 != w1 && b2 - 1 != w2 && b2 - 1 != b1) moves.add("" + w1 + w2 + b1 + (b2 - 1));            
            if (b2 - 2 == 0) moves.add("" + w1 + w2 + b1 + "0");
            else if (b2 - 2 >= 1 && b2 - 2 != w1 && b2 - 2 != w2 && b2 - 2 != b1) moves.add("" + w1 + w2 + b1 + (b2 - 2));
        }        
        return moves;
    }

    public static int staticEst(String board) {
        int white1 = Character.getNumericValue(board.charAt(0));
        int white2 = Character.getNumericValue(board.charAt(1));
        int black1 = Character.getNumericValue(board.charAt(2));
        int black2 = Character.getNumericValue(board.charAt(3));

        if (white1 == 9 || white2 == 9) return 100;               // white wins
        if (black1 == 0 || black2 == 0) return -100;             // black wins
        return white1 + white2 + black1 + black2 - 18;           
    }
}
