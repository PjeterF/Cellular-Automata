package org.test;

public class BoatMovements {
    public static boolean canTravelTo(boolean[][] gameMatrix, int fromRow, int fromColumn, int toRow, int toColumn) {
        if(fromRow<0 || toRow<0 || fromRow>=gameMatrix.length || toRow>=gameMatrix.length){
            return false;
        }
        if(fromColumn<0 || toColumn<0 || fromColumn>=gameMatrix[0].length || toColumn>=gameMatrix[0].length){
            return false;
        }

        int rowDiff=toRow-fromRow;
        int colDiff=toColumn-fromColumn;

        if(rowDiff<-1 || rowDiff>2){
            return false;
        }
        if(colDiff<-1 || colDiff>1){
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        boolean[][] gameMatrix = {
                {false, true,  true,  false, false, false},
                {true,  true,  true,  false, false, false},
                {true,  true,  true,  true,  true,  true},
                {false, true,  true,  false, true,  true},
                {false, true,  true,  true,  false, true},
                {false, false, false, false, false, false},
        };

        System.out.println(canTravelTo(gameMatrix, 3, 2, 2, 2)); // true, Valid move
        System.out.println(canTravelTo(gameMatrix, 3, 2, 3, 4)); // false, Can't travel through land
        System.out.println(canTravelTo(gameMatrix, 3, 2, 6, 2)); // false, Out of bounds
    }
}