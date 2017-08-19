
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import java.awt.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;


class PalveAlgorithmAI extends GomokuPlayer {

    Color myColor, enemyColor; //track what color belongs to me
    int rows = GomokuBoard.ROWS;
    int cols = GomokuBoard.COLS;

    public Move chooseMove(Color[][] board, Color me) {
        while (true) {
            String[][] boardState = new String[rows][cols];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (board[r][c] == Color.BLACK) {
                        boardState[r][c] = "BLACK";
                    } else if (board[r][c] == Color.WHITE) {
                        boardState[r][c] = "WHITE";
                    } else {
                        boardState[r][c] = "EMPTY";
                    }
                }
            }
            if (me == Color.BLACK) {
                myColor = Color.WHITE;
                enemyColor = Color.BLACK;
            } else if (me == Color.WHITE) {
                myColor = Color.WHITE;
                enemyColor = Color.BLACK;
            }

            if (emptyBoard(board) == true) {
                //lineScores(board,me);
                return new Move(3, 4);

            } else {
                int[] newMove = new int[2];
                newMove = minimax(me, boardState, board);
                System.out.println("Moving to " + newMove[0] + "," + newMove[1]);
                return new Move(newMove[0], newMove[1]);

            }

        }
    } // chooseMove()

    public int[] minimax( Color me, String[][] boardState, Color[][] board) {// int[][]boardState){

        ArrayList<int[]> availableMoves = possibleMoves(boardState, me);
        int bestScore = -99999;
        int bestRow = -10;
        int bestCol = -10;
        int scores[] = new int[3];

        Color[][] newBoard;
        if (availableMoves.isEmpty()) {
            return new int[]{bestRow, bestCol};
        } else {//we gonna try new moves and compare to best scores
            for (int i = 0; i < availableMoves.size(); i++) {
                newBoard = board;
                int[] move = availableMoves.get(i);
                newBoard[move[0]][move[1]] = me;
                // System.out.println("testing move " + move[0] + " " + move[1]);
                scores = gomokuHeuristicFunction(newBoard, me, move[0], move[1]);
                newBoard[move[0]][move[1]] = null;
                if (scores.length == 2) {
                    System.out.println("I decide to move to " + scores[0] + ", " + scores[1]);
                    if (scores[0] > 7 || scores[1] > 7) {
                        System.out.println("Ooops wrong coordinates " + scores[0] + ", " + scores[1]);
                    } else {
                        return new int[]{scores[0], scores[1]};

                    }
                }
                // scores = lineScores(board,me,2,3);
                if (scores[3] > bestScore) {
                    bestScore = scores[3];

                    bestRow = move[0];
                    bestCol = move[1];

                }

            }
            System.out.println("current score is " + scores[3] + " and best possible score " + bestScore);
            System.out.println("current col score is " + scores[1]);
            System.out.println("current row score is " + scores[0]);
            System.out.println("current diag score is " + scores[2]);
            System.out.println("I decide to move to " + bestRow + ", " + bestCol);

        }
        return new int[]{bestRow, bestCol};
    }

    public int[] gomokuHeuristicFunction(Color[][] newboard, Color me, int newRow, int newCol) {
        /*this method is HEURISTIC EVALUATION.
        it assigns scores to rows, cols and diagonals
        FOR MAX PLAYER
         1entry = 1;
         2entries = 10;
         3entries = 100;
         4entries = 10000;
         5entries = 100000;
        
        FOR MIN PLAYER
            same values * -1
         */
        //set to the spot to try it out.
        Color myColor = me;
        Color enemyColor;
        if (myColor == Color.BLACK) {
            enemyColor = Color.WHITE;
        } else {
            enemyColor = Color.BLACK;
        }

        //  System.out.println(me.toString());
        int maxRowScore = 0;
        int minRowScore = 0;
        int maxColScore = 0;
        int minColScore = 0;
        int rowScore = 0;
        int colScore = 0;
        int diagScore = 0;
        int totalScore = 0;

        for (int r = 0; r < rows; r++) {

            maxRowScore = 0;
            minRowScore = 0;
            for (int c = 0; c < cols; c++) {

                if (newboard[r][c] == myColor) {
                    minRowScore = 0;

                    if (maxRowScore == 0) {
                        maxRowScore = 1;
                    } else {
                        maxRowScore *= 10;
                    }
                    rowScore += maxRowScore;
                } else if (newboard[r][c] == enemyColor) {
                    maxRowScore = 0;
                    if (minRowScore == 0) {
                        minRowScore = -1;
                    } else {
                        minRowScore *= 10;

                        //this check is to find out if enemy has got a potential for a finishing move in the NEXT turn; 
                        if (c >= 2 && c <= 5 && newboard[r][c - 1] != myColor && newboard[r][c + 1] != myColor && newboard[r][c - 2] != myColor && newboard[r][c + 2] != myColor) {

                            minRowScore *= 10;
                        } else if ((c == 1 || c == 6) && newboard[r][c - 1] != myColor && newboard[r][c + 1] != myColor) {

                            minRowScore *= 10;

                        } else {
                            minRowScore = 0;
                        }
                    }
                    rowScore += minRowScore;
                    if (minRowScore < -100) {

                        minRowScore = 0;
                        return maximiseScore(r, newboard, "ROW", me);

                    }
                } else {

                }

            }

        }

        //compute score for each column
        for (int c = 0; c < cols; c++) {

            maxColScore = 0;
            minColScore = 0;
            for (int r = 0; r < rows; r++) {

                if (newboard[r][c] == myColor) {
                    minColScore = 0;
                    if (maxColScore == 0) {
                        maxColScore = 1;
                    } else {
                        maxColScore *= 10;
                    }
                    colScore += maxColScore;
                } else if (newboard[r][c] == enemyColor) {
                    maxColScore = 0;
                    if (minColScore == 0) {
                        minColScore = -1;
                    } else {
                        minColScore *= 10;
                        if (r >= 2 && r <= 5 && newboard[r - 1][c] != myColor && newboard[r + 1][c] != myColor && newboard[r + 2][c] != myColor && newboard[r - 2][c] != myColor) {
                            minColScore *= 10;
                        } else if ((c == 1 || c == 6) && newboard[c - 1][r] != myColor && newboard[c + 1][r] != myColor) {

                            minRowScore *= 10;
                        } else {
                            minColScore = 0;
                        }
                    }
                    colScore += minColScore;
                    if (minColScore < -100) {

                        minColScore = 0;

                        return maximiseScore(c, newboard, "COL", me);

                    }
                } else {

                }

                // System.out.println(" col Scores "+ c + " are " +myColScore + " and " + enemyColScore);
            }

        }

        int[] checkDiag = new int[14];

        checkDiag[0] = leftdiagScores(4, 0, newboard, me);
        checkDiag[1] = leftdiagScores(5, 0, newboard, me);
        checkDiag[2] = leftdiagScores(6, 0, newboard, me);
        checkDiag[3] = leftdiagScores(7, 0, newboard, me);
        checkDiag[4] = leftdiagScores(7, 1, newboard, me);
        checkDiag[5] = leftdiagScores(7, 2, newboard, me);
        checkDiag[6] = leftdiagScores(7, 3, newboard, me);

        checkDiag[7] = rightdiagScores(3, 0, newboard, me);
        checkDiag[8] = rightdiagScores(2, 0, newboard, me);
        checkDiag[9] = rightdiagScores(1, 0, newboard, me);
        checkDiag[10] = rightdiagScores(0, 0, newboard, me);
        checkDiag[11] = rightdiagScores(0, 1, newboard, me);
        checkDiag[12] = rightdiagScores(0, 2, newboard, me);
        checkDiag[13] = rightdiagScores(0, 3, newboard, me);

        for (int i = 0; i < checkDiag.length; i++) {
            diagScore += checkDiag[i];
        }

   

        int[] allScores = new int[4];
        allScores[0] = rowScore;
        allScores[1] = colScore;
        allScores[2] = diagScore;
        totalScore = allScores[0] + allScores[1] + allScores[2];
        allScores[3] = totalScore;
        return allScores;
    }

    public int leftdiagScores(int Row, int Col, Color[][] board, Color me) {
        // board[newRow][newCol] = me;
        int maxDiagScore = 0;
        int minDiagScore = 0;
        int totalDiagScore = 0;
        Color maxPlayer = me;
        Color minPlayer;
        if (maxPlayer == Color.BLACK) {
            minPlayer = Color.WHITE;
        } else {
            minPlayer = Color.BLACK;
        }

        int compliment = Row + Col;

        while (Row + Col <= compliment && Row >= 0 && Col <= 7) {
            if (board[Row][Col] == maxPlayer) {
                
                minDiagScore = 0;
                
                 if (maxDiagScore == 0) {
                    maxDiagScore = 1;
                } else {
                    maxDiagScore *= 10;
                }
                totalDiagScore += maxDiagScore;
            } else if (board[Row][Col] == null) {

            } else {//if board[Row][Col] =enemyColor
                maxDiagScore = 0;
                if (minDiagScore == 0) {
                    minDiagScore = -1;
                } else {
                    minDiagScore *= 25;
                }
                totalDiagScore += minDiagScore;
            }

            // System.out.println("Diag Scores "+ Row + " " + Col  + " are " +myDiagScore + " and " + enemyDiagScore);
            Row--;
            Col++;

        }

        return totalDiagScore;
    }

    public int rightdiagScores(int Row, int Col, Color[][] board, Color me) {
        // board[newRow][newCol] = me;
        int totalDiagScore = 0;
        int maxDiagScore = 0;
        int minDiagScore = 0;
         Color maxPlayer = me;
        Color minPlayer;
        if (maxPlayer == Color.BLACK) {
            minPlayer = Color.WHITE;
        } else {
            minPlayer = Color.BLACK;
        }
        while (Row <= 7 && Col <= 7) {
            if (board[Row][Col] == maxPlayer) {
                
                minDiagScore = 0;
                
                if (maxDiagScore == 0) {
                    maxDiagScore = 1;
                } else {
                    maxDiagScore *= 10;
                }
                totalDiagScore += maxDiagScore;
            } else if (board[Row][Col] == null) {

            } else {//if board[Row][Col] !=me
                maxDiagScore = 0;
                if (minDiagScore == 0) {
                    minDiagScore = -1;
                } else {
                    minDiagScore *= 25;
                }
                totalDiagScore += minDiagScore;
            }
            // System.out.println("Diag Scores "+ Row + " " + Col  + " are " +myDiagScore + " and " + enemyDiagScore);

            Row++;
            Col++;

        }

        return totalDiagScore;
    }

    public int[] maximiseScore(int index, Color[][] board, String identifier, Color me) {
        int row = -1;
        int col = -5;
        Color maxPlayer = me;
        Color minPlayer;
        int count = 0;
        int anotherCount = 0;
        if (maxPlayer == Color.BLACK) {
            minPlayer = Color.WHITE;
        } else {
            minPlayer = Color.BLACK;
        }

        if (identifier.equals("ROW")) {

            for (int j = 0; j < rows; j++) {
                anotherCount++;
                if (board[index][j] == minPlayer) {
                    count++;
                    if (count >= 3) {

                        if (board[index][j + 1] == null && (j + 1) <= 7) {
                            row = index;
                            col = anotherCount;
                         
                            return new int[]{row, col};
                        } else if (board[index][j - count] == null && (j - count) >= 0 && board[index][j - count + 1] == minPlayer) {
                            row = index;
                            col = j - count;
                            return new int[]{row, col};
                        } else if (board[index][j - 2] == null && board[index][j - 1] == minPlayer) {
                            row = index;
                            col = j - 2;
                            return new int[]{row, col};
                        } else if (board[index][j - 1] == null) {
                            row = index;
                            col = j - 1;
                            return new int[]{row, col};
                        }

                    }
                }

            }
            return new int[]{row, col};
        } else if (identifier.equals("COL")) {

            for (int j = 0; j < cols; j++) {
                if (board[j][index] == minPlayer) {
                    count++;
                    if (count >= 3) {

                        if (board[j + 1][index] == null && (j + 1) <= 7) {
                            row = j + 1;
                            col = index;
                            return new int[]{row, col};
                        } else if (board[j - count][index] == null && (j - count) >= 0 && board[j - count + 1][index] == minPlayer) {
                            row = j - count;
                            col = index;
                            return new int[]{row, col};
                        } else if (board[j - 2][index] == null && board[j - 1][index] == minPlayer) {
                            row = j - 1;
                            col = index;
                            return new int[]{row, col};
                        } else if (board[j - 1][index] == null) {
                            row = j - 1;
                            col = index;
                            return new int[]{row, col};
                        }

                    }
                }

            }

        }

        return new int[]{row, col};
    }

   

    private ArrayList<int[]> possibleMoves(String[][] currentBoard, Color me) {
        GomokuBoard tempBoard = new GomokuBoard();
        ArrayList<int[]> allMoves = new ArrayList<int[]>();
        if (tempBoard.getWinner() != null) {
            return allMoves;
        } else {//if winner is not established yet
            //iterate over the whole board to find empty spots
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    //if spot is empty, add it to the allMoves list
                    if ("EMPTY".equals(currentBoard[r][c])) {
                        allMoves.add(new int[]{r, c});
                    }
                }
            }
        }

        return allMoves;
    }

    public boolean emptyBoard(Color[][] gomokuBoard) {
        int emptiness = 0;
        for (int i = 0; i < GomokuBoard.ROWS; i++) {
            for (int j = 0; j < GomokuBoard.COLS; j++) {
                if (gomokuBoard[i][j] == null) {
                    emptiness += 1;
                }

            }
        }
        if (emptiness == 64) {
            return true;
        }
        return false;
    }
} // class RandomPlayer
