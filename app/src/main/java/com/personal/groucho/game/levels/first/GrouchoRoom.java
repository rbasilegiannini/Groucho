package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.dylanBubble;
import static com.personal.groucho.game.assets.Textures.grouchoBubble;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.levels.Room;

public class GrouchoRoom extends Room {
    public static boolean firstTime = true;
    private GameObject grouchoTrigger, dylanTrigger;
    private final FirstLevel level;
    private int playerPosX, playerPosY, tableX, tableY, bedX, bedY;
    private PositionComponent tablePosComp, bedPosComp;

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

        if (firstTime) {
            playerPosX = 500;
            playerPosY = 500;
            tableX = 5*cellSize;
            tableY = (int) (3.5*cellSize);
            bedX = 5*cellSize;
            bedY = 2*cellSize;
        }
        else {
            playerPosX = 2*cellSize;
            playerPosY = cellSize;
        }

        makeFurniture();
        makeTriggers();
        makeDecorations();

        allocateRoom();

        firstTime = false;
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        setBrightness(maxBrightness);
    }

    @Override
    public void releaseRoom() {
        tableX = tablePosComp.posX;
        tableY = tablePosComp.posY;
        bedX = bedPosComp.posX;
        bedY = bedPosComp.posY;

        super.releaseRoom();
    }

    private void makeTriggers() {
        // Init level
        if (firstTime) {
            grouchoTrigger = GameObjectFactory.
                    makeTrigger(500, 550, 64, 128,
                            gameWorld.physics.world, () -> {
                                String sentence = gameWorld.activity.getString(R.string.groucho_level1_bedroom_talk_init);
                                grouchoTalk(sentence, 500, 500);
                                removeTrigger(grouchoTrigger);
                            });
            gameObjects.add(grouchoTrigger);
            // Dylan Talk
            dylanTrigger = GameObjectFactory.
                    makeTrigger(400, 100, 512, 32,
                            gameWorld.physics.world, () -> {
                                String sentence = gameWorld.activity.getString(R.string.dylan_level1_bedroom_talk_init);
                                dylanTalk(sentence, 600, 250);
                                removeTrigger(dylanTrigger);
                            });
            gameObjects.add(dylanTrigger);
        }

        // Groucho's photo
        gameObjects.add(GameObjectFactory.
                makeTrigger((int) (0.75*cellSize), (int) (-0.5*cellSize), 128, 128,
                        gameWorld.physics.world, () -> {
                            String sentence = gameWorld.activity.getString(R.string.groucho_level1_bedroom_talk_photo);
                            grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                        }));

        // Wardrobe
        gameObjects.add(GameObjectFactory.
                makeTrigger((int) (4.5*cellSize), (int) (-0.5*cellSize), 280, 128,
                        gameWorld.physics.world, () -> {
                            String sentence = gameWorld.activity.getString(R.string.groucho_level1_bedroom_talk_wardrobe);
                            grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                        }));
        // Door to hallway
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
                            level.fromHall = false;
                            level.fromGrouchoRoom = true;
                            level.goToHallway();
                        }));
    }


    private void makeFurniture() {
        World world = gameWorld.physics.world;;
        GameObject table = GameObjectFactory.
                makeFurniture(tableX, tableY, 250, 150, 5f, world, Textures.table);
        tablePosComp = (PositionComponent) table.getComponent(POSITION);
        gameObjects.add(table);

        GameObject bed = GameObjectFactory.
                makeFurniture(bedX, bedY, 280, 350, 100f, world, Textures.bed
        );
        bedPosComp = (PositionComponent) bed.getComponent(POSITION);
        gameObjects.add(bed);
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
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (1.5*cellSize),
                        (int) (6.0*cellSize),
                        188,
                        200,
                        Textures.windowInternal
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (4.5*cellSize),
                        (int) (6.0*cellSize),
                        188,
                        200,
                        Textures.windowInternal
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
