package com.personal.groucho.game.levels;

import static com.personal.groucho.game.assets.Textures.dylanBubble;
import static com.personal.groucho.game.assets.Textures.grouchoBubble;
import static com.personal.groucho.game.constants.Environment.brightness;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.Environment.minBrightness;
import static com.personal.groucho.game.constants.System.cellSize;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class Room {
    protected final GameWorld gameWorld;
    protected final List<GameObject> gameObjects = new ArrayList<>();
    protected final Rect surface;
    protected final Paint floorPaint;
    protected Bitmap internalWall, externalWall;
    private final int widthGrid;
    private final int heightGrid;

    public Room(int levelDimX, int levelDimY, GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        widthGrid = levelDimX/cellSize;
        heightGrid = levelDimY/cellSize;
        surface = new Rect(0,0, widthGrid*cellSize, heightGrid*cellSize);
        floorPaint = new Paint();
    }

    public void init() {
        GameGrid.getInstance().init(widthGrid, heightGrid);
        gameObjects.clear();
        makeBorders();
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(surface, floorPaint);
    }

    private void makeBorders() {
        // Upper border
        gameObjects.addAll(GameObjectFactory.
                makeHorBorder(surface.width() / 2,
                        -cellSize,
                        surface.width(),
                        internalWall,
                        gameWorld.physics.world
                ));

        // Bottom border
        gameObjects.addAll(GameObjectFactory.
                makeHorBorder(surface.width()/2,
                        surface.height(),
                        surface.width() + cellSize,
                        externalWall,
                        gameWorld.physics.world
                ));

        // Left border
        gameObjects.add(GameObjectFactory.makeVerBorder(
                cellSize/4,
                (int) ((surface.height()/2)-(1.75f * cellSize)),
                surface.height()+(1.5f*cellSize),
                gameWorld.physics.world));

        // Right border
        gameObjects.add(GameObjectFactory.makeVerBorder(
                (int) (surface.width() + (0.74f)*cellSize),
                (int) ((surface.height()/2)-(1.75f * cellSize)),
                surface.height()+(1.5f*cellSize),
                gameWorld.physics.world));
    }

    protected void setBrightness(float intensity) {
        if (intensity >= maxBrightness) {
            brightness = maxBrightness;
            gameWorld.setPlayerVisibility(true);
        }
        else {
            brightness = minBrightness;
            gameWorld.setPlayerVisibility(false);
        }
    }

    public void releaseRoom() {
        gameWorld.goHandler.changeLevel();
        GameGrid.getInstance().releasePool();
    }

    public void allocateRoom(){
        for (GameObject go : gameObjects) {
            gameWorld.goHandler.addGameObject(go);
        }
    }

    protected void setControllerVisibility(boolean visibility) {
        gameWorld.controller.dpad.setVisibility(visibility);
        gameWorld.controller.pause.setVisibility(visibility);
        gameWorld.controller.trigger.setVisibility(visibility);
        gameWorld.controller.bulb.setVisibility(visibility);
    }

    protected void makeWallTrigger(int decCx, int decCy,
                                 int triggerX, int triggerY,
                                 int dimX, int dimY,
                                 Bitmap texture, Runnable runnable) {

        gameObjects.add((GameObjectFactory.makeWallDecoration(decCx, decCy, dimX, dimY, texture)));
        gameObjects.add(GameObjectFactory.
                makeTrigger(triggerX, triggerY, dimX, dimY, gameWorld.physics.world, runnable));
    }

    protected void grouchoTalk(String sentence, int posX, int posY) {
        gameWorld.hasToTalk();
        gameWorld.bubbleSpeech.setBubbleTexture(grouchoBubble);
        gameWorld.bubbleSpeech.setPosX(posX);
        gameWorld.bubbleSpeech.setPosY(posY);
        gameWorld.bubbleSpeech.setLeftAlignment();
        gameWorld.bubbleSpeech.setNormalText();
        gameWorld.bubbleSpeech.setText(sentence);
    }

    protected void dylanTalk(String sentence, int posX, int posY) {
        gameWorld.hasToTalk();
        gameWorld.bubbleSpeech.setBubbleTexture(dylanBubble);
        gameWorld.bubbleSpeech.setPosX(posX);
        gameWorld.bubbleSpeech.setPosY(posY);
        gameWorld.bubbleSpeech.setCenterAlignment();
        gameWorld.bubbleSpeech.setBoldText();
        gameWorld.bubbleSpeech.setText(sentence);
    }

}
