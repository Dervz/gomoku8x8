# Gomoku     EIGHT x EIGHT

<b> <i> See instruction of how to run the Algorithm at the bottom of this README </i> </b>

<h2> <i> About algorithm </i> </h2> 
    
 Provided algorithm is based around Minimax /Alpha – Beta strategy. Minimax implements concept of two competing players: maximizing and minimizing. First tends to maximize the overall score while the second one attempts to minimize the overall score resulting in the competition between each other. 
 Initially, the algorithm determines what colors belong to maximizing and minimizing players. Secondly, the state of the board is determined and updated after each move. The scores of the current state of the board are determined by the method called gomokuHeuristicFunction. It returns lines, rows and diagonals scores to the minimax function. Minimax function is the core function of Player140338287 class. It figures out all the potential ways of improving score for the current player and chooses the most effective one and then updates the current state of the board. 
 
   <h2> <i>How MiniMax works </i> </h2> 
 In order to determine the best potential move, minimax algorithm keeps track of the board state. The board state can be temporarily altered to try out new moves for both players, thus the strategy to get to the most beneficial state for each player can be determined. Therefore, minimax function always looks at the results of all the possible available moves (which are provided by possibleMoves method) before actually performing a move. In addition to that, minimax method also does take into consideration potential threat created by opposing player and always tries to block it. For example, if now it is maxPlayer’s turn and minPlayer currently has three entries in the row somewhere on the board, maxPlayer will try to block that row in order to decrease the benefit for minPlayer and therefore, increase its own score. This is performed by the method called maximiseScore. MaximiseScore always figures out the best spot to play for blocking the opponent’s future moves which can potentially lead to the winning condition. 
 
   <h2> <i>How scores are computed  </i> </h2> 
 Scores are computed for each entry on the board by gomokuHeuristicFunction. The scores are awarded as follows: 1 entry = 1 point, 2 entries in the row = 10 points, 3 entries in the row = 100 points, 4 entries in the row = 1,000 points and finally, 5 entries in the row = 10,000 points. It is important to mention that the scores are exponentials because 2 entries next to each other in the same row/column/diagonal are not as good as 2 entries in random places of the same row/column/diagonal. Also, gomokuHeuristicFuntion takes into account spaces between entries. For example, if certain row contains  - - X X - X - - , then it will be considered to be equally good as - - X X - X - - because if the gap is filled in, then it is a winning condition. Therefore, maxPlayer will always prevent minPlayer getting to this state by blocking the gaps and in turn, will try to create same states for itself.

 
          How to Run the Algorithm
 <p>
 <p> In order to run the algorithm follow these steps:
 <p> 1)Clone this repository in your local one (kind of obvious)
 <p> 2)cd from cmd into the directory
 <p> 3)Run GomokuReferee class (thats where the main is)
 <p> 4)Experiment with the algorithms provided for the game, where PavelAlgorithm's is MiniMax developed by myself.
 <p> 5)You can use trivial algorithms provided (Sequence & Random) or play yourself!
