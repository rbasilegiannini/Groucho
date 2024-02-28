package com.personal.groucho.game.levels;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.dylanBubble;
import static com.personal.groucho.game.assets.Textures.grouchoBubble;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.personal.groucho.R;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;

public class GrouchoRoom extends Level{
    private static boolean firstTime = true;
    public GrouchoRoom(GameWorld gameWorld) {
        super(gameWorld, 1000, 1000);

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(Textures.firstLevelFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init() {
        super.init();
        if (firstTime) {
            gameWorld.player.setPos(500, 500);
        }
        else {
            gameWorld.player.setPos(2*cellSize, cellSize);
        }
        setBrightness(maxBrightness);

        makeFurniture();
        makeTriggers();
        makeDecorations();

        for (GameObject go : gameObjects) {
            gameWorld.goHandler.addGameObject(go);
        }
        firstTime = false;
    }

    GameObject grouchoTrigger, dylanTrigger;

    private void handleTrigger(GameObject trigger) { gameWorld.goHandler.removeGameObject(trigger);}
    private void makeTriggers() {
        // Init level
        if (firstTime) {
            grouchoTrigger = GameObjectFactory.
                    makeTrigger(500, 550, 64, 128,
                            gameWorld, () -> {
                                String sentence = gameWorld.activity.getString(R.string.groucho_talk_room);
                                grouchoTalk(sentence, 500, 500);
                                handleTrigger(grouchoTrigger);
                            });
            gameObjects.add(grouchoTrigger);
            // Dylan Talk
            dylanTrigger = GameObjectFactory.
                    makeTrigger(400, 100, 512, 32,
                            gameWorld, () -> {
                                String sentence = gameWorld.activity.getString(R.string.dylan_talk_room);
                                dylanTalk(sentence, 600, 250);
                                handleTrigger(dylanTrigger);
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
                        gameWorld,
                        Textures.brownDoor
                )));
        gameObjects.add(GameObjectFactory.
                makeTrigger(
                        2*cellSize, (int) (-0.85*cellSize),
                        160, 270,
                        gameWorld,
                        () -> {
                            door.play(1f);
                            gameWorld.changeLevel(new Hallway(gameWorld));
                        }));
    }

    private void makeFurniture() {
        gameObjects.add((GameObjectFactory.
                makeFurniture(
                        5*cellSize,
                        (int) (3.5*cellSize),
                        250, 150,
                        5f,
                        gameWorld,
                        Textures.table)
        ));

        gameObjects.add((GameObjectFactory.
                makeFurniture(
                        5*cellSize,
                        2*cellSize,
                        280, 350,
                        100f,
                        gameWorld,
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
                        gameWorld,
                        Textures.grouchoFrame
                )));

        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (4.5*cellSize),
                        (int) (-0.5*cellSize),
                        280,
                        350,
                        gameWorld,
                        Textures.grouchoWardrobe
                )));

        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        2*cellSize,
                        (int) (2.5*cellSize),
                        512,
                        380,
                        gameWorld,
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
