package org.automata.storage;

import org.automata.elements.*;

import org.graphics.CanvasRenderer;
import org.utility.RandomNumberGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class World {
    final ElementBase[][] elements;
    final int width;
    final int height;
    float[] canvas;
    final CanvasRenderer canvasRenderer;

    public World(CanvasRenderer canvasRenderer){
        this.width=canvasRenderer.getWidth();
        this.height=canvasRenderer.getHeight();
        this.canvasRenderer=canvasRenderer;

        elements=new ElementBase[width][height];
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                if(x==0 || x==width-1 || y==0 || y==height-1){
                    elements[x][y]=new Stone();
                }else{
                    int i=RandomNumberGenerator.getInstance().randomInt(0, 2);
                    if(i==0){
                        elements[x][y]=new Vacuum();
                    }else if(i==1){
                        elements[x][y]=new Sand();
                    }else{
                        elements[x][y]=new Water();
                    }
                }
                canvasRenderer.setPixelData(x, y, elements[x][y].getColor());
            }
        }
    }

    public void draw(){
        canvasRenderer.draw();
    }

    public void update(){
        for(int x=0;x<width;x++) {
            for (int y = 0; y < height; y++) {
                elements[x][y].update(x, y, this);
            }
        }
    }

    public void updateChunk(int xBegin, int xEnd){
        for(int x=xBegin;x<xEnd;x++) {
            for (int y = 0; y < height; y++) {
                elements[x][y].update(x, y, this);
            }
        }
    }

    public void updateMultithreaded(int nChunks){
        int intervals[]=new int[nChunks+1];
        int chunkLength=width/nChunks;

        int limit=0;
        for(int i=0;i<nChunks;i++){
            intervals[i]=limit;
            limit=limit+chunkLength;
        }
        intervals[nChunks]=width;

        {
            ExecutorService executor=Executors.newFixedThreadPool(nChunks);
            for(int i=0;i<intervals.length-1;i=i+2){
                executor.submit(new WorldChunkThread(this, intervals[i], intervals[i+1]));
            }
            executor.shutdown();

            try {
                executor.awaitTermination(1, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
        {
            ExecutorService executor=Executors.newFixedThreadPool(nChunks);
            for(int i=1;i<intervals.length-1;i=i+2){
                executor.submit(new WorldChunkThread(this, intervals[i], intervals[i+1]));
            }
            executor.shutdown();

            try {
                executor.awaitTermination(1, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
    }

    public ElementBase getElement(int x, int y){
        if(x<0 || x>=width || y<0 || y>=height){
            throw new RuntimeException("Tried accessing element out of bounds");
        }

        return elements[x][y];
    }

    public void switchElements(int x1, int y1, int x2, int y2){
        ElementBase temp=elements[x1][y1];
        setElement(x1, y1, elements[x2][y2]);
        setElement(x2, y2, temp);
    }

    public void setElement(int x, int y, ElementBase element){
        if(x<0 || x>=width || y<0 || y>=height){
            return;
        }

        elements[x][y]=element;
        canvasRenderer.setPixelData(x, y, element.getColor());
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int canMoveDistance(int startX, int startY, int travelDistance, char direction, Predicate<ElementBase> stopCondition){
        int xOffset, yOffset;
        int sign=1;
        switch (direction){
            case 'W'->{yOffset=1; xOffset=0;}
            case 'S'->{yOffset=-1; xOffset=0; sign=-1;}
            case 'D'->{yOffset=0; xOffset=1;}
            case 'A'->{yOffset=0; xOffset=-1; sign=-1;}
            default -> {return 0;}
        }

        int result=0;
        while(travelDistance!=0){
            if(stopCondition.test(this.getElement(startX+xOffset, startY+yOffset))){
                break;
            }

            startX+=xOffset;
            startY+=yOffset;
            travelDistance--;
            result++;
        }

        return sign*result;
    }

    public Vector2D<Integer> canMoveTo(Vector2D<Integer> start, Vector2D<Integer> target, Predicate<ElementBase> stopCondition){
        Vector2D<Float> direction = new Vector2D<>((float)target.x-start.x ,(float)target.y-start.y);
        int nSteps=(int)(Math.abs(direction.x)+Math.abs(direction.y));

        if(nSteps==0){
            return start;
        }

        float xIncrement=direction.x/nSteps;
        float yIncrement=direction.y/nSteps;

        Vector2D<Float> current=new Vector2D<>((float)start.x , (float)start.y);
        Vector2D<Float> previous=new Vector2D<>((float)start.x , (float)start.y);
        for(int i=0;i<nSteps;i++){
            try{
                ElementBase currentElement=this.getElement(Math.round(current.x), Math.round(current.y));
                if(stopCondition.test(currentElement)){
                    return new Vector2D<>(Math.round(previous.x), Math.round(previous.y));
                }

                previous.x=current.x;
                previous.y=current.y;

                current.x+=xIncrement;
                current.y+=yIncrement;
            }catch (Exception e){
                return start;
            }
        }

        return target;
    }
}
