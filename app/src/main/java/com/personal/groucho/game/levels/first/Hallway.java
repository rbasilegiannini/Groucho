package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.grouchoBubble;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.levels.Room;

public class Hallway extends Room {
    public static boolean firstTime = true;
    private final FirstLevel level;
    private GameObject grouchoTrigger;
    private int playerPosX, playerPosY;
    private Orientation playerOrientation;

    public Hallway(GameWorld gameWorld, FirstLevel level) {
        super(2000, 600, gameWorld);
        this.level = level;
        Bitmap floor = Textures.firstLevelFloor;

        // Set floor
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
        Matrix m = new Matrix();
        m.postTranslate(surface.width(), surface.height());
        floorPaint.getShader().setLocalMatrix(m);
    }

    @Override
    public void init() {
        super.init();

        if (firstTime) {
            tableX = 6*cellSize;
            tableY = (int) (0.75*cellSize);
        }

        if (level.fromGrouchoRoom) {
            playerPosX = (int) (2.5*cellSize);
            playerPosY = (int) (1.7*cellSize);
            playerOrientation = UP;
        }
        else if (level.fromHall) {
            playerPosX = (int) (10.5*cellSize);
            playerPosY = cellSize;
            playerOrientation = DOWN;
        }

        makeTriggers();
        makeDecorations();
        makeFurniture();

        allocateRoom();

        firstTime = false;
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();

        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(playerOrientation);
        gameWorld.player.rest();
        setBrightness(maxBrightness);
    }

    @Override
    public void releaseRoom(){
        tableX = tablePosComp.posX;
        tableY = tablePosComp.posY;

        super.releaseRoom();
    }

    private int tableX, tableY;
    private PositionComponent tablePosComp;
    private void makeFurniture(){
        World world = gameWorld.physics.world;;
        GameObject table = GameObjectFactory.
                makeFurniture(tableX, tableY, 150, 150, 5f, world, Textures.littleTable);
        tablePosComp = (PositionComponent) table.getComponent(POSITION);
        gameObjects.add(table);
    }

    private void makeDecorations() {
        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        (int) (6*cellSize),
                        (int) (1.5*cellSize),
                        512,
                        356,
                        Textures.brownCarpet
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (6*cellSize),
                        0,
                        400,
                        210,
                        Textures.orangeCouch
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        cellSize,
                        (int) (-0.25*cellSize),
                        180,
                        250,
                        Textures.dresserWithFlower
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (7.5*cellSize),
                        (int) (3*cellSize),
                        188,
                        200,
                        Textures.windowInternal
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (10.5*cellSize),
                        (int) (3*cellSize),
                        188,
                        200,
                        Textures.windowInternal
                )));
    }

    private void makeTriggers() {
        // Door to GrouchoRoom
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (2.5*cellSize),
                        (int) (3.1*cellSize),
                        160,
                        280,
                        Textures.brownDoor
                )));
        gameObjects.add(GameObjectFactory.
                makeTrigger(
                        (int) (2.5*cellSize), (int) (3.0*cellSize),
                        160, 270,
                        gameWorld.physics.world,
                        () -> {
                            door.play(1f);
                            level.goToGrouchoRoom();
                        }));

        // Door to hall
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (10.5*cellSize),
                        (int) (-0.85*cellSize),
                        160,
                        280,
                        Textures.brownDoor
                )));
        gameObjects.add(GameObjectFactory.
                makeTrigger(
                        (int) (10.5*cellSize), (int) (-0.95*cellSize),
                        160, 270,
                        gameWorld.physics.world,
                        () -> {
                            door.play(1f);
                            level.goToHall();
                        }));

        // Little library
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (8.5*cellSize),
                        (int) (-0.25*cellSize),
                        180,
                        250,
                        Textures.littleLibrary
                )));
        gameObjects.add(GameObjectFactory.
                makeTrigger((int)(8.5*cellSize), (int) (-0.5*cellSize), 180, 128,
                        gameWorld.physics.world, () -> {
                            String sentence = gameWorld.activity.getString(R.string.groucho_level1_hallway_talk_library);
                            grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                        }));

        // Window
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (2.5*cellSize),
                        -cellSize,
                        188,
                        200,
                        Textures.windowNight
                )));
        gameObjects.add(GameObjectFactory.
                makeTrigger((int)(2.5*cellSize), (int) (-0.5*cellSize), 188, 128,
                        gameWorld.physics.world, () -> {
                            String sentence = gameWorld.activity.getString(R.string.groucho_level1_hallway_talk_window);
                            grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                        }));
    }

    private void grouchoTalk(String sentence, int posX, int posY) {
        gameWorld.hasToTalk();
        gameWorld.bubbleSpeech.setBubbleTexture(grouchoBubble);
        gameWorld.bubbleSpeech.setPosX(posX);
        gameWorld.bubbleSpeech.setPosY(posY);
        gameWorld.bubbleSpeech.setLeftAlignment();
        gameWorld.bubbleSpeech.setNormalText();
        gameWorld.bubbleSpeech.setText(sentence);
    }
}
