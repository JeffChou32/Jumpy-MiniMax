import java.io.*;
import java.util.*;

public class MiniMaxBlack {

    static int count = 0;  //counter to track how many positions we evaluated

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {          
            System.out.println("needs 3 args: <inputfile.txt> <outputfile.txt> <maxDepth>");
            return;
        }

        String inputFile = args[0];                                   
        String outputFile = args[1];                                  
        int maxDepth = Integer.parseInt(args[2]);                     

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String startBoard = reader.readLine().trim();                 
        reader.close();

        String bestBoard = startBoard;  
        int bestScore = Integer.MAX_VALUE;              //opposite-black is minimizing

        if (maxDepth == 0) {                                         
            bestScore = staticEst(startBoard);
            count++;
        } else {
            int lowestSoFar = Integer.MAX_VALUE;                   //track lowest score across moves
            for (String nextBoard : blackMoves(startBoard)) {       //generate all white moves
                int moveScore = MaxMin(nextBoard, maxDepth - 1);    //run minimax from next position
                if (moveScore < lowestSoFar) {                     //update if better
                    lowestSoFar = moveScore;
                    bestBoard = nextBoard;
                }
            }
            bestScore = lowestSoFar;
        }

        System.out.println("Board Position: " + bestBoard);        
        System.out.println("Positions evaluated by static estimation: " + count + ".");
        System.out.println("MINIMAX estimate: " + bestScore + ".");

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(bestBoard);                                     
        writer.newLine();
        writer.close();
    }

    public static int MaxMin(String board, int depth) {
        if (depth == 0 || isGameOver(board)) {
            count++;
            return staticEst(board);
        }
        int v = Integer.MIN_VALUE;
        for (String child : whiteMoves(board)) {
            v = Math.max(v, MinMax(child, depth - 1));
        }
        return v;
    }

    public static int MinMax(String board, int depth) {
        if (depth == 0 || isGameOver(board)) {
            count++;
            return staticEst(board);
        }
        int v = Integer.MAX_VALUE;
        for (String child : blackMoves(board)) {
            v = Math.min(v, MaxMin(child, depth - 1));
        }
        return v;
    }

    public static boolean isGameOver(String board) {
        int w1 = Character.getNumericValue(board.charAt(0));
        int w2 = Character.getNumericValue(board.charAt(1));
        int b1 = Character.getNumericValue(board.charAt(2));
        int b2 = Character.getNumericValue(board.charAt(3));
        return (w1 == 9 || w2 == 9 || b1 == 0 || b2 == 0);
    }

    public static List<String> whiteMoves(String board) {
        List<String> moves = new ArrayList<>();
        int w1 = Character.getNumericValue(board.charAt(0));
        int w2 = Character.getNumericValue(board.charAt(1));
        int b1 = Character.getNumericValue(board.charAt(2));
        int b2 = Character.getNumericValue(board.charAt(3));

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

    public static List<String> blackMoves(String board) {
        List<String> moves = new ArrayList<>();
        int w1 = Character.getNumericValue(board.charAt(0));
        int w2 = Character.getNumericValue(board.charAt(1));
        int b1 = Character.getNumericValue(board.charAt(2));
        int b2 = Character.getNumericValue(board.charAt(3));

        if (b1 != 0) {
            if (b1 == 1) {
                moves.add("" + w1 + w2 + "0" + b2);
            } else if (b1 - 1 != w1 && b1 - 1 != w2 && b1 - 1 != b2) {
                moves.add("" + w1 + w2 + (b1 - 1) + b2);
            } else if ((b1 - 2 != w1 && b1 - 2 != w2 && b1 - 2 != b2) || b1 - 2 == 0) {
                int newB1 = b1 - 2;
                if (b1 - 1 == w1) {
                    if (1 != b1 && 1 != b2 && 1 != w2) w1 = 1;
                    else if (2 != b1 && 2 != b2 && 2 != w2) w1 = 2;
                    else if (3 != b1 && 3 != b2 && 3 != w2) w1 = 3;
                    else if (4 != b1 && 4 != b2 && 4 != w2) w1 = 4;
                }
                if (b1 - 1 == w2) {
                    if (1 != b1 && 1 != b2 && 1 != w1) w2 = 1;
                    else if (2 != b1 && 2 != b2 && 2 != w1) w2 = 2;
                    else if (3 != b1 && 3 != b2 && 3 != w1) w2 = 3;
                    else if (4 != b1 && 4 != b2 && 4 != w1) w2 = 4;
                }
                moves.add("" + w1 + w2 + newB1 + b2);
            }
        }

        if (b2 != 0) {
            if (b2 == 1) {
                moves.add("" + w1 + w2 + b1 + "0");
            } else if (b2 - 1 != w1 && b2 - 1 != w2 && b2 - 1 != b1) {
                moves.add("" + w1 + w2 + b1 + (b2 - 1));
            } else if ((b2 - 2 != w1 && b2 - 2 != w2 && b2 - 2 != b1) || b2 - 2 == 0) {
                int newB2 = b2 - 2;
                if (b2 - 1 == w1) {
                    if (1 != b1 && 1 != b2 && 1 != w2) w1 = 1;
                    else if (2 != b1 && 2 != b2 && 2 != w2) w1 = 2;
                    else if (3 != b1 && 3 != b2 && 3 != w2) w1 = 3;
                    else if (4 != b1 && 4 != b2 && 4 != w2) w1 = 4;
                }
                if (b2 - 1 == w2) {
                    if (1 != b1 && 1 != b2 && 1 != w1) w2 = 1;
                    else if (2 != b1 && 2 != b2 && 2 != w1) w2 = 2;
                    else if (3 != b1 && 3 != b2 && 3 != w1) w2 = 3;
                    else if (4 != b1 && 4 != b2 && 4 != w1) w2 = 4;
                }
                moves.add("" + w1 + w2 + b1 + newB2);
            }
        }
        return moves;
    }

    public static int staticEst(String board) {
        int white1 = Character.getNumericValue(board.charAt(0));
        int white2 = Character.getNumericValue(board.charAt(1));
        int black1 = Character.getNumericValue(board.charAt(2));
        int black2 = Character.getNumericValue(board.charAt(3));

        if (white1 == 9 && white2 == 9) return 100;              //white wins
        if (black1 == 0 && black2 == 0) return -100;             //black wins
        return white1 + white2 + black1 + black2 - 18;           
    }
}