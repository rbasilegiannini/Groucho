package com.personal.groucho.game;

import static com.personal.groucho.game.Graphics.bufferHeight;
import static com.personal.groucho.game.Graphics.bufferWidth;
import static com.personal.groucho.game.GameWorld.physicalSize;
import static com.personal.groucho.game.GameWorld.screenSize;
import static com.personal.groucho.game.constants.Character.enemyFovInRad;
import static com.personal.groucho.game.constants.System.charDimX;
import static com.personal.groucho.game.constants.System.charDimY;
import static com.personal.groucho.game.constants.System.charScaleFactor;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.LEFT;
import static com.personal.groucho.game.controller.Orientation.RIGHT;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

public class Utils {

    public static float fromScreenToBufferX(float x) { return x/screenSize.width*bufferWidth; }
    public static float fromScreenToBufferY(float y) { return y/screenSize.height*bufferHeight; }

    public static float fromMetersToBufferX(float xLocal) {
        return ((xLocal - physicalSize.xmin) / (physicalSize.xmax - physicalSize.xmin)) * bufferWidth;
    }
    public static float fromMetersToBufferY(float yLocal) {
        return ((yLocal - physicalSize.ymin) / (physicalSize.ymax - physicalSize.ymin)) * bufferHeight;
    }

    public static float fromBufferToMetersX(float xGlobal) {
        return (xGlobal / bufferWidth) * (physicalSize.xmax - physicalSize.xmin) + physicalSize.xmin;
    }
    public static float fromBufferToMetersY(float yGlobal) {
        return (yGlobal / bufferHeight) * (physicalSize.ymax - physicalSize.ymin) + physicalSize.ymin;
    }

    public static float toBufferXLength(float x) {return x/physicalSize.width*bufferWidth;}
    public static float toBufferYLength(float y) {return y/physicalSize.height*bufferHeight;}
    public static float toMetersXLength(float x) {return x/bufferWidth * physicalSize.width;}
    public static float toMetersYLength(float y) {return y/bufferHeight * physicalSize.height;}

    public static boolean isInCircle(float centerX, float centerY, float x, float y, float distSqr) {
        float dx = x - centerX;
        float dy = y - centerY;
        float distanceSqr = dx * dx + dy * dy;
        return distanceSqr <= distSqr;
    }

    public static boolean isInTriangle(float originX, float originY, float x, float y, float dist, float rad) {
        float[] vecO = new float[]{originX - x, originY - y};
        float[] vecA = new float[]{
                (float)(originX + dist*Math.cos(rad) - x),
                (float)(originY + dist*Math.sin(rad) - y)};
        float[] vecB = new float[]{
                (float)(originX + dist*Math.cos(rad-enemyFovInRad) - x),
                (float)(originY + dist*Math.sin(rad-enemyFovInRad) - y)};

        double crossProduct1 = vecO[0] * vecA[1] - vecO[1] * vecA[0];
        double crossProduct2 = vecA[0] * vecB[1] - vecA[1] * vecB[0];
        double crossProduct3 = vecB[0] * vecO[1] - vecB[1] * vecO[0];

        return (crossProduct1 >= 0 && crossProduct2 >= 0 && crossProduct3 >= 0)
                || (crossProduct1 <= 0 && crossProduct2 <= 0 && crossProduct3 <= 0);
    }

    public static float distBetweenPos(float pos1x, float pos1y, float pos2x, float pos2y){
        float deltaX = pos2x - pos1x;
        float deltaY = pos2y - pos1y;
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public static Orientation directionBetweenGO(GameObject go, GameObject goToTurn) {
        PositionComponent posGO = (PositionComponent) go.getComponent(POSITION);
        PositionComponent posGOToTurn = (PositionComponent) goToTurn.getComponent(POSITION);

        if (posGO.posY > posGOToTurn.posY - charScaleFactor * charDimY &&
            posGO.posY < posGOToTurn.posY + charScaleFactor * charDimY
        ) {
            if (posGO.posX < posGOToTurn.posX) {
                return LEFT;
            }
            if (posGO.posX > posGOToTurn.posX) {
                return RIGHT;
            }
        }
        else {
            if (posGO.posY > posGOToTurn.posY) {
                return DOWN;
            }
            if (posGO.posY < posGOToTurn.posY) {
                return UP;
            }
        }

        return UP;
    }

    public static boolean isCloseToGO(int originX, int originY, GameObject go) {
        PositionComponent posGO = (PositionComponent) go.getComponent(POSITION);
        float distFromGOX = originX - posGO.posX;
        float distFromGOY = originY - posGO.posY;

        float distSquared = distFromGOX * distFromGOX + distFromGOY * distFromGOY;
        float thresholdSquared = 1.5f * charScaleFactor * charDimX;
        thresholdSquared *= thresholdSquared;

        return distSquared <= thresholdSquared;
    }
}
