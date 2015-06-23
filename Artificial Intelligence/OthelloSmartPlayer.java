/*
	AI for a computer-controlled othello player
*/

/*
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
*/
package cs380.othello;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
/*
@author Bruce Johnson
*/
public class OthelloSmartPlayer extends OthelloPlayer
{
    public OthelloMove getMove(OthelloState state)
    {
        int iterations = 10000;
        OthelloMove smartestMove = mcts(state,iterations);
        return smartestMove;
    }
	
    public int eval(OthelloState state,boolean p1)
    {
        int score = 0;
        int PLAYER1 = 0;
        int PLAYER2 = 1;
        int boardSize = 8;
        int board[][] = state.board;
        int check_vert = 0;
        int check_hor = 0;
        int player;
        int p1_score = 0;
		
        for(int i = 0; i < boardSize; i++)
	{
	    for(int j = 0; j < boardSize; j++)
	    {
	        for (int p = 0; p < 2; p++)
		{
		    player = (p == 0) ? PLAYER1 : else player = PLAYER2;
		    if (board[i][j] == player)
		    {
		        /*
			check to see if there are pieces in any of the corners. since corner pieces cannot be reversed, they are
			very valuable. also check for pieces adjacent to corner pieces. these pieces cannot be reversed either, so they
			are valuable as well.
			*/
		        if (i == 0 && j == 0 && board[i][j] == player) {
		            check_vert = 1; check_hor = 1; score += 10;
		        }
		        else if (i == 0 && j == 7 && board[i][j] == player) {
		            check_vert = 1; check_hor = -1; score += 10;
		        }
		        else if (i == 7 && j == 0 && board[i][j] == player) {
		            check_vert = -1; check_hor = 1; score += 10;
		        }
		        else if (i == 7 && j == 7 && board[i][j] == player) {
		            check_vert = -1; check_hor = -1; score += 10;
		        }
		        else score++;
						
		        if (check_vert == 1) {
		            for (int k = i; k < boardSize; k = k++) {
		                score += adj2corner(player,board,k,j);
			    }
			}
			else if (check_vert == -1) {
			    for (int k = i; k > 0; k = k--) {
			        score += adj2corner(player,board,k,j);
			    }
			}
			
			if (check_vert == 1) {
			    for (int k = j; k < boardSize; k = k++) {
			        score += adj2corner(player,board,k,j);
			    }
			}
			else if (check_vert == -1) {
			    for (int k = j; k > 0; k = k--) {
			        score += adj2corner(player,board,k,j);
			    }
			}
		    }
					
		    if (p == 0) { p1_score = score; }
		}
	    }
       }
        /*
        Normally we search for the solution that gives the greatest difference between the number of smart player and opponent pieces.
        However, the code below will utilize the solution that allows the smart player to win in the least number of moves.
        For example, we might be able to the win the game in a few turns, but opt for a solution that will give the highest amount of points
        instead. The code belows searches the board to see if the opponent has any pieces present. If they don't, then we give this move a high
        score because it is an automatic win.
        */
        for(int i = 0; i < boardSize;i++)
	{
            for(int j = 0;j < boardSize;j++)
	    {
                if ((p2 && board[i][j] == PLAYER1) || board[i][j] == PLAYER2)  {
		    return p1_score - score;
                }
            }
            return 1000;
        }
        return 1;
    }
    
    public int adj2corner(int PLAYER1,int board[][],int k,int j)
    {
	int r = (board[k][j] == PLAYER1) ? 2 : 0; 
        return r;
    }
	
    public OthelloMove mcts(OthelloState board,int iterations)
    {
        OthelloNode root;
        OthelloState node2;
        int node2Score = 0;
        root = createNode(board);
        List<OthelloNode> children = new ArrayList<OthelloNode>(root.getChildren());
		
        for (int i = 0; i < iterations; i++)
	{
            OthelloNode node = treePolicy(root,children);
            if (node != null) {
                node2 = defaultPolicy(node);
                node2Score = node2.score();
                node.backup(node2Score);
            }
        }
        OthelloNode m = bestChild(root);
        return m.getMove();
    }
	
    public OthelloNode treePolicy(OthelloNode node,List<OthelloNode> children)
    {
        if (children.size() > 0) {
            OthelloNode c = children.get(0);
            children.remove(0);
            return c;
        }
        else if (node.getChildren().size() == 0) { return node; }
        else if (children.size() == 0 && node.getChildren().size() > 0) {
            Random rand = new Random();
            if (rand.nextInt(100) <= 90) {
                return bestChild(node);
            }
            else {
                List<OthelloNode> child = node.getChildren();
                int r = child.size();
                if (r != 0) {
                    return child.get(rand.nextInt(r));
                }
                else return child.get(0);
            }
        }
        return node;
    }
	
    public OthelloState defaultPolicy(OthelloNode node)
    {
        OthelloState clone = node.getState();
        boolean stop = false;
		
        while (!clone.gameOver() && !stop) {
            List<OthelloMove> moves = clone.generateMoves();
            Random rand = new Random();
            int r = moves.size();
            if (r != 0) {
                clone = clone.applyMoveCloning(moves.get(rand.nextInt(r)));
            }
            else stop = true;
        }
        return clone;
    }
	
    public OthelloNode createNode(OthelloState board)
    {
        OthelloNode t = new OthelloNode();
        List<OthelloMove> moves = board.generateMoves();
        t.setChildren(moves,t,board);
        t.setState(board);
        List<OthelloNode> children = t.getChildren();
        return t;
    }
	
    public OthelloNode bestChild(OthelloNode node)
    {
        List<OthelloNode> children = node.getChildren();
        OthelloNode max;
        try {
            max = children.get(0);
        }
        catch (Exception e) {
            max = null;
        }
        for (int i = 1; i < children.size(); i++)  {
            if (p1) {
                if (children.get(i).getAvgScore() > max.getAvgScore()) {
                    max = children.get(i);
                }
            }
            else {
                if (children.get(i).getAvgScore() < max.getAvgScore()) {
                    max = children.get(i);
                }
            }
        }
        return max;
    }
    /*
    Performs the Minimax algorithm
    p1 = player1
    p2 = player2
    OSM = OthelloSmartestMove class
    */
    public OSM minimax(OthelloState state,int depth,boolean maximizingPlayer,boolean p1, boolean p2)
    {
        List<OthelloMove> moves = state.generateMoves();
        OSM smartestMove = new OSM();
        if (maximizingPlayer)  {
            if (p1) smartestMove.setBV(-99999);
            else smartestMove.setBV(99999);
        }
        else {
            if (p1) smartestMove.setBV(99999);
            else smartestMove.setBV(-99999);
        }
        if (moves.size() == 0 || depth == 0) {
            OSM o = new OSM();
            o.setBV(state.score());
            return o;
        }
        for (int i = 0; i < moves.size(); i++) {
            OthelloState clone = state.applyMoveCloning(moves.get(i));
            int val = minimax(clone,depth - 1,!maximizingPlayer,p1,p2).getBV();
            if (maximizingPlayer)  {
                //Adapt the algorithm according to whether the player is X or O
                if ((p1 && val > smartestMove.getBV()) || (p2 && val < smartestMove.getBV())) {
                    smartestMove.setBV(val);
                    smartestMove.setMove(moves.get(i));
                }
            }
            else {
                if ((p1 && val < smartestMove.getBV()) || (p2 && val > smartestMove.getBV())) {
                    smartestMove.setBV(val);
                    smartestMove.setMove(moves.get(i));
                }
            }
        }
        return smartestMove;
    }
}
