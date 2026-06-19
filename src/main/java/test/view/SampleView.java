package test.view;

import Characters.Direction;
import Characters.Sengoku;
import Items.Item;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import test.model.SampleModel;

public class SampleView {

    private final SampleModel model;
    //口の向きを記憶しておく。（初期は右向き）
    private double lastBaseAngle = 0;

    public SampleView(SampleModel model) {
        this.model = model;
    }

    public void drawStage(GraphicsContext gc) {
        int cols = model.getMap()[0].length;
        int rows = model.getMap().length;
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, cols * SampleModel.TILE_SIZE, rows * SampleModel.TILE_SIZE);
        //モデルからアイテム配列を取得
        Item[][] itemMap = model.getItemMap();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int tile = model.getMap()[row][col];
                int x = col * SampleModel.TILE_SIZE;
                int y = row * SampleModel.TILE_SIZE;
                Item item = itemMap[row][col];
                //壁の描画
                if (tile == 1) {
                    gc.setFill(Color.BLUE);
                    gc.fillRect(x + 2, y + 2, SampleModel.TILE_SIZE - 4, SampleModel.TILE_SIZE - 4);
                }
                    //Itemsフォルダに入っているPoint、Chiiを描画
                    
                    if (item != null) {
                        // PointなのかChiiなのかを詮索せず、アイテム自身に描画を丸投げする！
                        item.draw(gc, x, y, SampleModel.TILE_SIZE);
                    }
            }
        }
        
    }

    public void drawPacman(GraphicsContext gc) {
    		Sengoku sengoku = model.getSengoku();
        
        // 生存していないなら描画をスキップ
        if (sengoku == null || !sengoku.isAlive()) return;

        gc.setFill(Color.YELLOW);
        
        //  2. Sengokuの左上座標(x, y)から、描画用の中心点(pacX, pacY)を計算する
        double pacX = sengoku.getX() + SampleModel.TILE_SIZE / 2.0;
        double pacY = sengoku.getY() + SampleModel.TILE_SIZE / 2.0;
        
        double mouthAngle = model.getMouthAngle();

        //  3. Sengokuの移動方向（getDirection()）に基づいて口の向き（角度）を計算する
       
        Direction currentDir = sengoku.getDirection();

        if (currentDir != null) {
            // Direction Enum の getDX(), getDY() から向きを判定
            if (currentDir.getDX() == 1)  lastBaseAngle =0;         // 👉 右向き
            if (currentDir.getDX() == -1) lastBaseAngle =180;   // 👈 左向き
            if (currentDir.getDY() == -1) lastBaseAngle = 90;    // 👆 上向き
            if (currentDir.getDY() == 1)  lastBaseAngle =270;   // 👇 下向き
        }

        double finalStartAngle = lastBaseAngle + mouthAngle;
        //  4. 計算した中心点からパックマン（扇形）を描画
        gc.fillArc(
            pacX - SampleModel.TILE_SIZE / 2.0,
            pacY - SampleModel.TILE_SIZE / 2.0,
            SampleModel.TILE_SIZE, SampleModel.TILE_SIZE,
            finalStartAngle,
            360 - mouthAngle * 2,
            javafx.scene.shape.ArcType.ROUND
        );
    }
}
