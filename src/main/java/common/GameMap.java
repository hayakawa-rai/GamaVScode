package common;

import java.util.List;

import Characters.Direction;
import Characters.Enemy; 

public interface GameMap {
    int getStageNumber();
    double getPacX();
    double getPacY();
    int[][] getMap();
    boolean isWaitingStart();
    Direction getPlayerDirection();
    List<Enemy> getEnemies(); 
    // 指定座標が壁かどうかを判定する最小限のメソッド
    boolean isWall(int x, int y);
}