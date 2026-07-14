class GameConfig {
    // タイルサイズ（30ピクセル）
    static get TILE_SIZE() {
        return 30;
    }
}
// 他のファイルから読み込めるようにエクスポート（ESモジュールの場合）
export default GameConfig;