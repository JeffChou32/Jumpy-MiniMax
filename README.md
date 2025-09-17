# Minimax Game Solver

This project implements a basic Minimax algorithm (with and without alpha-beta pruning) to evaluate a simplified two-player game where white and black pieces race toward opposite ends of a linear board. The board state is represented as a 4-digit string.

## Board Encoding

Each board state is encoded as a string of 4 digits:
- `w1`, `w2`: Positions of the two white pieces (0–9; 9 = off board)
- `b1`, `b2`: Positions of the two black pieces (0–9; 0 = off board)

Example: `"2456"` means white pieces are at positions 2 and 4, and black at 5 and 6.

## Features

- `whiteMoves(String board)`: Generates all legal white moves.
- `blackMoves(String board)`: Generates all legal black moves.
- `staticEst(String board)`: Heuristic evaluation function using squared distance from goal.
- `minimaxWhite / minimaxBlack`: Recursive Minimax implementations.
- `isTerminal(String board)`: Checks if the game is over.

## How to Run

Compile and run using any Java IDE or command line:
```bash
javac Minimax.java
java Minimax
