package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.CharacterFactory.getWolf;
import static com.personal.groucho.game.assets.Textures.bigTable;
import static com.personal.groucho.game.assets.Textures.box;
import static com.personal.groucho.game.assets.Textures.brownCarpet;
import static com.personal.groucho.game.assets.Textures.chairDown;
import static com.personal.groucho.game.assets.Textures.chairLeft;
import static com.personal.groucho.game.assets.Textures.chairRight;
import static com.personal.groucho.game.assets.Textures.chairUp;
import static com.personal.groucho.game.assets.Textures.dresserRight;
import static com.personal.groucho.game.assets.Textures.fridge;
import static com.personal.groucho.game.assets.Textures.greenCouchRight;
import static com.personal.groucho.game.assets.Textures.kitchen;
import static com.personal.groucho.game.assets.Textures.lamp;
import static com.personal.groucho.game.assets.Textures.library;
import static com.personal.groucho.game.assets.Textures.lightWoodFloor;
import static com.personal.groucho.game.assets.Textures.littleGreenCarpetVer;
import static com.personal.groucho.game.assets.Textures.littleTable;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.controller.Orientation.LEFT;
import static com.personal.groucho.game.controller.states.StateName.IDLE;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.levels.first.KitchenEvents.entryHallDoorEvent;
import static com.personal.groucho.game.levels.first.KitchenEvents.firstTimeInRoomEvent;
import static com.personal.groucho.game.levels.first.KitchenEvents.wolfDeath;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.levels.Room;

public class Kitchen extends Room {
    protected final FirstLevel level;
    protected int playerPosX, playerPosY;

    public Kitchen(GameWorld gameWorld, FirstLevel level) {
        super(1650, 1650, gameWorld);
        this.internalWall = Textures.greenWall;
        this.externalWall = Textures.woodWall;
        this.level = level;

        setFloor(lightWoodFloor, 128, 128);
    }

    @Override
    public void init() {
        super.init();
        playerPosX = (int)(9.1*unit);
        playerPosY = 4*unit;

        if (firstTime){
            firstTimeInRoomEvent(this);
        }

        makeFurniture();
        makeTriggers();
        makeDecorations();
        makeEnemies();
        makeHealth();

        allocateRoom();
    }

    @Override
    public void allocateRoom() {
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(LEFT);
        setBrightness(maxBrightness);
    }

    private void makeFurniture() {
        World world = gameWorld.physics.world;

        gameObjects.add(GameObjectFactory.
                makeStaticFurniture(
                        (int) (1.7*unit), (int)(0.5*unit),
                        550, 400,
                        world, kitchen)
        );
        addDynamicFurn((int)(9.4*unit), 7*unit, 150, 380, 25f, greenCouchRight);
        addDynamicFurn((int)(9.4*unit), (int)(5.5*unit), 150, 150, 5f, littleTable);
        addDynamicFurn((int)(9.6*unit), (int)(1.5*unit), 90, 400, 35f, dresserRight);
        addDynamicFurn((int)(3.1*unit), (int)(6.7*unit), 250, 360, 35f, bigTable);
        addDynamicFurn((int)(2.1*unit), (int)(6.7*unit), 90, 150, 5f, chairLeft);
        addDynamicFurn((int)(4.1*unit), (int)(6.7*unit), 90, 150, 5f, chairRight);
        addDynamicFurn((int)(3.1*unit), (int)(5.3*unit), 90, 150, 5f, chairUp);
        addDynamicFurn((int)(2.9*unit), (int)(7.7*unit), 90, 110, 5f, chairDown);
        addDynamicFurn((int)(7.8*unit), (int)(7.8*unit), 110, 110, 5f, box);
        addDynamicFurn((int)(7.4*unit), (int)(8.1*unit), 60, 60, 5f, box);
    }

    private void makeTriggers(){
        addFloorTrigger((int)(9.6*unit), 4*unit, (int)(10.3*unit), 4*unit,
                90, 150, littleGreenCarpetVer, ()->entryHallDoorEvent(this));
    }

    private void makeDecorations() {
        addWallDec((int)(9.3*unit), (int)(-0.5*unit), 100, 356, lamp);
        addWallDec(7*unit, (int)(-0.5*unit), 400, 400, library);
        addWallDec(4*unit, (int)(-0.5*unit), 150, 400, fridge);
        addFloorDec((int)(2.3*unit), (int)(2.1*unit), 512, 356, brownCarpet);
    }

    GameObject wolf, keyGO;
    private void makeEnemies() {
        wolf = GameObjectFactory.makeEnemy(6*unit, 4*unit, LEFT, getWolf(), IDLE, gameWorld);
        gameObjects.add(wolf);
    }

    private void makeHealth() {
        World world = gameWorld.physics.world;
        gameObjects.add(GameObjectFactory.makeHealth((int) (4.8*unit), unit/2, world));
        gameObjects.add(GameObjectFactory.makeHealth((int) (5.4*unit), unit/2, world));
        gameObjects.add(GameObjectFactory.makeHealth(8*unit, (int) (8.1*unit), world));
    }

    @Override
    public void handleDeath() {
        PositionComponent posComp = (PositionComponent) wolf.getComponent(POSITION);
        keyGO = GameObjectFactory.makeTrigger(posComp.posX, posComp.posY, 60, 60,
                gameWorld.physics.world, ()->wolfDeath(this));
        gameWorld.goHandler.addGameObject(keyGO);
    }
}
