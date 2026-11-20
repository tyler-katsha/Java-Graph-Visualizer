import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.IO.print;
import static java.lang.IO.println;

public class AStarHelper {

    public static boolean isValid(int row, int col, int ROW, int COL) {
        return (row >= 0) && (row < ROW) && (col >= 0) && (col < COL);
    }

    public static boolean isUnblocked(int[][] grid,int row, int col) {
        return grid[row][col] == 1;
    }

    public static boolean isDestination(int[] dest,int row,int col){
        return row == dest[0] && col == dest[1];
    }

    public static double calculateHValue(int row,int col, int[] dest) {
        return Math.sqrt((row - dest[0]) * (row - dest[0]) + (col - dest[1]) * (col - dest[1]));
    }
    public static void tracePath(Cell[][] cellDetails, int[] dest) {
        println("The path is ");

        int row = dest[0];
        int col = dest[1];

        List<int[]> path = new ArrayList<>();
        while(!(row == cellDetails[row][col].getParentI() && col == cellDetails[row][col].getParentJ())) {
            path.add(new int[]{row,col});

            int temp_row = cellDetails[row][col].getParentI();
            int temp_col = cellDetails[row][col].getParentJ();
            row = temp_row;
            col = temp_col;

        }

        path.add(new int[]{row, col});

        Collections.reverse(path);

        path.forEach(p -> print("-> (" + p[0] + ", " + p[1] + ")"));

        println();
    }

    public static boolean processNeighbour(int i, int j, int newI, int newJ,
                                        int[][] grid, Cell[][] cellDetails,
                                        boolean[][] closedList, PriorityQueue<double[]> openList,
                                        int[] dest,int ROW,int COL) {

        if(!isValid(newI,newJ,ROW,COL)) {
            return false;
        }

        if(isDestination(dest,newI,newJ)) {
            cellDetails[newI][newJ].setParentI(i);
            cellDetails[newI][newJ].setParentJ(j);
            println("The destination cell is found");
            tracePath(cellDetails,dest);
            return true;
        }

        if(!closedList[newI][newJ] && isUnblocked(grid,newI,newJ)) {
            int moveCost = (Math.abs(newI - i) + Math.abs(newJ - j));

            double newG = cellDetails[i][j].getG() + moveCost;
            double newH = calculateHValue(newI,newJ,dest);
            double newF = newG + newH;

            if(cellDetails[newI][newJ].getF() == Double.POSITIVE_INFINITY || cellDetails[newI][newJ].getF() > newF) {
                openList.offer(new double[]{newF,newI, newJ});

                cellDetails[newI][newJ].setF(newF);
                cellDetails[newI][newJ].setG(newG);
                cellDetails[newI][newJ].setH(newH);
                cellDetails[newI][newJ].setParentI(i);
                cellDetails[newI][newJ].setParentJ(j);
            }
        }

        return false;
    }

}
