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
        int minDistance = gp.tileSize;
        int initialDelay = 0;

        for (int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = new OBJ_Corn();
            gp.obj[i].worldX = (int) (Math.random() * (gp.screenWidth - gp.tileSize));
            gp.obj[i].worldY = -gp.tileSize - (int) (Math.random() * gp.tileSize * 6);

            int delayFactor = i * 2000 + (int) (Math.random() * 1000) + initialDelay;
            initialDelay += 50;
            scheduleCornFall(gp.obj[i], delayFactor);
        }
    }

    private void scheduleCornFall(SuperObject corn, int delay) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> {
            corn.setFalling(true, gp);                                                                                      //flag for corn fall
        }, delay, TimeUnit.MILLISECONDS);
    }
}
