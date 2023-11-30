package main;

import object.OBJ_Corn;
import object.SuperObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter (GamePanel gp) {
        this.gp = gp;
    }

    public void setObject () {
        initializeCornObject ();
    }

    private void initializeCornObject() {
        int minDistance = gp.tileSize * 2;                                                                              //adjust the minimum distance as needed
        int previousX = -minDistance;                                                                                   //initial previousX value to ensure the first corn is spaced properly
        int initialDelay = 0;

        for (int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = new OBJ_Corn();
            gp.obj[i].worldX = (int) (previousX + minDistance + Math.random() * (gp.screenWidth - previousX - minDistance));
            gp.obj[i].worldY = -gp.tileSize - (int) (Math.random() * gp.tileSize * 3) -1 * 50;                          //vertical level of corn
            previousX = gp.obj[i].worldX;

            int delayFactor = i * 2000 + (int) (Math.random() * 1000) + initialDelay;                                   //make corns all at different times
            initialDelay += 100;
            scheduleCornFall(gp.obj[i], delayFactor);
        }
    }

    private void scheduleCornFall(SuperObject corn, int delay) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> {
            corn.setFalling(true);                                                                                      //flag for corn fall
        }, delay, TimeUnit.MILLISECONDS);
    }
}
