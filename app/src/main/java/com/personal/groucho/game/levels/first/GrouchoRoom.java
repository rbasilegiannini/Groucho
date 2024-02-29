package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.dylanBubble;
import static com.personal.groucho.game.assets.Textures.grouchoBubble;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.levels.Room;

public class GrouchoRoom extends Room {
    public static boolean firstTime = true;
    private GameObject grouchoTrigger, dylanTrigger;
    private final FirstLevel level;

    public GrouchoRoom(GameWorld gameWorld, FirstLevel level) {
        super(1000, 1000, gameWorld);
        this.level = level;
        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(Textures.firstLevelFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init() {
        super.init();

        makeFurniture();
        makeTriggers();
        makeDecorations();

        allocateRoom();

        firstTime = false;
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        if (firstTime) {
            gameWorld.player.setPos(500, 500);
        }
        else {
            gameWorld.player.setPos(2*cellSize, cellSize);
        }
        setBrightness(maxBrightness);
    }

    private void makeTriggers() {
        // Init level
        if (firstTime) {
            grouchoTrigger = GameObjectFactory.
                    makeTrigger(500, 550, 64, 128,
                            gameWorld.physics.world, () -> {
                                String sentence = gameWorld.activity.getString(R.string.groucho_talk_room);
                                grouchoTalk(sentence, 500, 500);
                                removeTrigger(grouchoTrigger);
                            });
            gameObjects.add(grouchoTrigger);
            // Dylan Talk
            dylanTrigger = GameObjectFactory.
                    makeTrigger(400, 100, 512, 32,
                            gameWorld.physics.world, () -> {
                                String sentence = gameWorld.activity.getString(R.string.dylan_talk_room);
                                dylanTalk(sentence, 600, 250);
                                removeTrigger(dylanTrigger);
                            });
            gameObjects.add(dylanTrigger);
        }

        // Door
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        2*cellSize,
                        (int) (-0.85*cellSize),
                        160,
                        280,
                        Textures.brownDoor
                )));
        gameObjects.add(GameObjectFactory.
                makeTrigger(
                        2*cellSize, (int) (-0.85*cellSize),
                        160, 270,
                        gameWorld.physics.world,
                        () -> {
                            door.play(1f);
                            level.goToHallway();
                        }));
    }

    private void removeTrigger(GameObject trigger) { gameWorld.goHandler.removeGameObject(trigger);}

    private void makeFurniture() {
        gameObjects.add((GameObjectFactory.
                makeFurniture(
                        5*cellSize,
                        (int) (3.5*cellSize),
                        250, 150,
                        5f,
                        gameWorld.physics.world,
                        Textures.table)
        ));

        gameObjects.add((GameObjectFactory.
                makeFurniture(
                        5*cellSize,
                        2*cellSize,
                        280, 350,
                        100f,
                        gameWorld.physics.world,
                        Textures.bed)
        ));
    }

    private void makeDecorations() {
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (0.75*cellSize),
                        (int) (-cellSize),
                        128,
                        128,
                        Textures.grouchoFrame
                )));

        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (4.5*cellSize),
                        (int) (-0.5*cellSize),
                        280,
                        350,
                        Textures.grouchoWardrobe
                )));

        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        2*cellSize,
                        (int) (2.5*cellSize),
                        512,
                        380,
                        Textures.redCarpet
                )));
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

    private void dylanTalk(String sentence, int posX, int posY) {
        gameWorld.hasToTalk();
        gameWorld.bubbleSpeech.setBubbleTexture(dylanBubble);
        gameWorld.bubbleSpeech.setPosX(posX);
        gameWorld.bubbleSpeech.setPosY(posY);
        gameWorld.bubbleSpeech.setCenterAlignment();
        gameWorld.bubbleSpeech.setBoldText();
        gameWorld.bubbleSpeech.setText(sentence);
    }
}
