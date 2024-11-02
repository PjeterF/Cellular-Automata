package org.automata.storage;

public class WorldChunkThread implements Runnable{
    private int xBegin, xEnd;
    private World world;
    public WorldChunkThread(World world, int xBegin, int xEnd){
        this.world=world;
        this.xBegin=xBegin;
        this.xEnd=xEnd;
    }
    @Override
    public void run() {
        world.updateChunk(xBegin, xEnd);
    }
}
