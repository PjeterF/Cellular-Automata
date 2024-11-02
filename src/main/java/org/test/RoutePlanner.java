package org.test;


import java.util.LinkedList;

public class RoutePlanner {



    public static boolean routeExists(int fromRow, int fromColumn, int toRow, int toColumn, boolean[][] mapMatrix) {
        class Coord{
            public int x, y;
            Coord(int x, int y){
                this.x=x;
                this.y=y;
            }
        }

        LinkedList<Coord> queue=new LinkedList<>();
        boolean[][] visited= new boolean[mapMatrix.length][mapMatrix[0].length];

        queue.add(new Coord(fromColumn, fromRow));
        visited[fromRow][fromColumn]=true;



        while(queue.size()!=0){
            Coord currentCoord=queue.getLast();
            if(currentCoord.x==toColumn && currentCoord.y==toRow){
                return true;
            }

            if(currentCoord.x-1>=0){
                if(visited[currentCoord.y][currentCoord.x-1]==false && mapMatrix[currentCoord.y][currentCoord.x-1]==true){
                    visited[currentCoord.y][currentCoord.x]=true;
                    queue.add(new Coord(currentCoord.x, currentCoord.y));
                }
            }
            if(currentCoord.x+1<mapMatrix[0].length){
                if(visited[currentCoord.y][currentCoord.x+1]==false && mapMatrix[currentCoord.y][currentCoord.x+1]==true){
                    visited[currentCoord.y][currentCoord.x]=true;
                    queue.add(new Coord(currentCoord.x, currentCoord.y));
                }
            }
            if(currentCoord.y-1>=0){

            }
            if(currentCoord.y+1<mapMatrix.length){

            }
        }

        return true;
    }

    public static void main(String[] args) {
        boolean[][] mapMatrix = {
                {true,  false, false},
                {true,  true,  false},
                {false, true,  true}
        };

        System.out.println(routeExists(0, 0, 2, 2, mapMatrix));
    }
}