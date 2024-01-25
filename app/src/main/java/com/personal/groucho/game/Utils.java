package com.personal.groucho.game;

import static com.personal.groucho.game.GameWorld.bufferHeight;
import static com.personal.groucho.game.GameWorld.bufferWidth;
import static com.personal.groucho.game.GameWorld.physicalSize;
import static com.personal.groucho.game.GameWorld.screenSize;

public class Utils {

    // Screen conversions
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

    public static float toPixelsXLength(float x) {return x/physicalSize.width*bufferWidth;}
    public static float toPixelsYLength(float y)
    {
        return y/physicalSize.height*bufferHeight;
    }
    public static float toMetersXLength(float x) {return x/bufferWidth * physicalSize.width;}
    public static float toMetersYLength(float y) {return y/bufferHeight * physicalSize.height;}

    public static boolean isInCircle(float x, float posX, float y, float posY, float distance) {
        double pointerPositionSqr = Math.pow(x - posX, 2) + Math.pow(y - posY, 2);
        return pointerPositionSqr <= distance;
    }


}
