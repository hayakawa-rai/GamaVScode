package test.view;

import Characters.Direction;
import Characters.Sengoku;
import Items.Item;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font; // ★フォント設定のためにインポートを追加
import javafx.scene.text.FontWeight; // ★太字にするためにインポートを追加
import test.model.SampleModel;

public class SampleView {

    private final SampleModel model;
    // 口の向きを記憶しておく。（初期は右向き）
    private double lastBaseAngle = 0;

    public SampleView(SampleModel model) {
        this.model = model;
    }

    public void drawStage(GraphicsContext gc) {
        int cols = model.getMap()[0].length;
        int rows = model.getMap().length;
        
        // Scoreを表示させるための宣言
        double stageWidth = cols * SampleModel.TILE_SIZE;
        double stageHeight = rows * SampleModel.TILE_SIZE;
        
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, stageWidth, stageHeight);
        
        // モデルからアイテム配列を取得
        Item[][] itemMap = model.getItemMap();

        // 1. マップとアイテムのループ描画
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int tile = model.getMap()[row][col];
                int x = col * SampleModel.TILE_SIZE;
                int y = row * SampleModel.TILE_SIZE;
                Item item = itemMap[row][col];
                
                // 壁の描画
                if (tile == 1) {
                    gc.setFill(Color.BLUE);
                    gc.fillRect(x + 2, y + 2, SampleModel.TILE_SIZE - 4, SampleModel.TILE_SIZE - 4);
                }
                
                // Itemsフォルダに入っているPoint、Chiiを描画
                if (item != null) {
                    // PointなのかChiiなのかを詮索せず、アイテム自身に描画を丸投げする！
                    item.draw(gc, x, y, SampleModel.TILE_SIZE);
                }
            }
        } // 👈 【バグ修正】2重ループの閉じカッコを正しい位置へ移動させました！

        // 2. スコアを表示するためのコード（マップ描画の外側で1回だけ実行）
        Sengoku sengoku = model.getSengoku();
        if (sengoku != null) {
            // スコア用のテキストを作成
            String scoreText = "SCORE: " + sengoku.getScore();
            
            // 文字のデザインを設定 (フォント名, 太さ, サイズ)
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            
            // 文字の色を白に設定
            gc.setFill(Color.WHITE);
            
            // 文字の位置を「右下」に計算（右端と下端から少し内側）
            double textX = stageWidth - 140; // 文字幅を考慮して右端から引く
            double textY = stageHeight - 20; // 下端から少し上げる
            
            // 描画
            gc.fillText(scoreText, textX, textY);
        }
    } // 👈 drawStage メソッドの正しい閉じカッコ

    public void drawPacman(GraphicsContext gc) {
        Sengoku sengoku = model.getSengoku();
        
        // 生存していないなら描画をスキップ
        if (sengoku == null || !sengoku.isAlive()) return;

        gc.setFill(Color.YELLOW);
        
        // Sengokuの左上座標(x, y)から、描画用の中心点(pacX, pacY)を計算する
        double pacX = sengoku.getX() + SampleModel.TILE_SIZE / 2.0;
        double pacY = sengoku.getY() + SampleModel.TILE_SIZE / 2.0;
        
        double mouthAngle = model.getMouthAngle();

        // Sengokuの移動方向（getDirection()）に基づいて口の向き（角度）を計算する
        Direction currentDir = sengoku.getDirection();

        if (currentDir != null) {
            // Direction Enum の getDX(), getDY() から向きを判定
            if (currentDir.getDX() == 1)  lastBaseAngle = 0;    // 👉 右向き
            if (currentDir.getDX() == -1) lastBaseAngle = 180;  // 👈 左向き
            if (currentDir.getDY() == -1) lastBaseAngle = 90;   // 👆 上向き
            if (currentDir.getDY() == 1)  lastBaseAngle = 270;  // 👇 下向き
        }

        double finalStartAngle = lastBaseAngle + mouthAngle;
        
        // 計算した中心点からパックマン（扇形）を描画
        gc.fillArc(
            pacX - SampleModel.TILE_SIZE / 2.0,
            pacY - SampleModel.TILE_SIZE / 2.0,
            SampleModel.TILE_SIZE, SampleModel.TILE_SIZE,
            finalStartAngle,
            360 - mouthAngle * 2,
            javafx.scene.shape.ArcType.ROUND
        );
    }
    
    public void setupEnemyView(javafx.scene.image.ImageView enemyImageView) {
        // 敵の画像をステージのタイルサイズ（30x30）に引き伸ばしてぴったり合わせる
        enemyImageView.setFitWidth(SampleModel.TILE_SIZE);
        enemyImageView.setFitHeight(SampleModel.TILE_SIZE);
        enemyImageView.setPreserveRatio(true);
    }
}
