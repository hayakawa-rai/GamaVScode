// ==================================================
// Practice画面CSSをJS化
// ==================================================

const practiceContainer =
    document.getElementById("practice-container");

practiceContainer.style.position = "absolute";
practiceContainer.style.top = "0";
practiceContainer.style.left = "0";
practiceContainer.style.width = "100%";
practiceContainer.style.height = "100%";
practiceContainer.style.zIndex = "3";

practiceContainer.style.display = "flex";
practiceContainer.style.flexDirection = "column";

practiceContainer.style.justifyContent = "center";
practiceContainer.style.alignItems = "center";


// ==================================================
// タイトル
// ==================================================

const practiceTitle =
    document.getElementById("practice-title");

practiceTitle.style.position = "absolute";
practiceTitle.style.top = "20%";

practiceTitle.style.fontFamily =
    `"PixelMplus12", sans-serif`;

practiceTitle.style.fontSize =
    "clamp(30px, 4vw, 60px)";

practiceTitle.style.color = "white";

practiceTitle.style.textShadow =
    "0 0 20px rgba(0,120,220,0.8)";

practiceTitle.style.textAlign =
    "center";


// ==================================================
// ステージボタン群
// ==================================================

const stageBox =
    document.getElementById("stage-box");

stageBox.style.display = "flex";
stageBox.style.flexDirection = "column";
stageBox.style.gap = "20px";

document
    .querySelectorAll(
        "#stage-box .game-button"
    )
    .forEach(button => {

        button.style.width = "35vw";
        button.style.maxWidth = "400px";
        button.style.minWidth = "180px";

        button.style.height = "9vh";
        button.style.maxHeight = "80px";
        button.style.minHeight = "50px";
        button.style.fontSize = "30px";

    });


// ==================================================
// 戻る
// ==================================================

const backBtn =
    document.getElementById("back-btn");

backBtn.style.position = "fixed";

backBtn.style.right = "30px";
backBtn.style.bottom = "30px";

backBtn.style.width = "200px";
backBtn.style.height = "60px";

backBtn.style.zIndex = "4";


// ==================================================
// トロフィー
// ==================================================

const scoreInfo =
    document.getElementById("score-info");

scoreInfo.style.position = "fixed";

scoreInfo.style.top =
    "clamp(5px, 1vw, 15px)";

scoreInfo.style.right =
    "clamp(5px, 1vw, 15px)";

scoreInfo.style.fontSize =
    "clamp(24px, 3vw, 50px)";
scoreInfo.style.zIndex = "4";

scoreInfo.style.fontSize = "50px";

scoreInfo.style.color = "gold";

scoreInfo.style.cursor =
    "pointer";

scoreInfo.addEventListener("click", () => {

    if (tooltip.style.display === "block") {
        tooltip.style.display = "none";

    } else {

        tooltip.style.display = "block";
    }
});

// ==================================================
// ハイスコア
// ==================================================

const tooltip =
    document.getElementById(
        "highscore-tooltip"
    );

tooltip.style.display = "none";

tooltip.style.position = "absolute";

/* トロフィーの少し左下 */
tooltip.style.top = "70px";
tooltip.style.right = "20px";

tooltip.style.zIndex = "100";

tooltip.style.whiteSpace = "pre-line";

tooltip.style.backgroundColor =
    "rgba(0,0,0,0.95)";

tooltip.style.color = "white";

tooltip.style.fontFamily =
    `"PixelMplus12", sans-serif`;

/* 画面サイズに応じて可変 */
tooltip.style.fontSize =
    "clamp(18px, 2vw, 32px)";

tooltip.style.padding =
    "clamp(10px, 1vw, 20px)";

tooltip.style.position = "fixed";

tooltip.style.top =
    "clamp(50px, 8vh, 70px)";

tooltip.style.right =
    "clamp(10px, 2vw, 20px)";

tooltip.style.zIndex = "100";

tooltip.style.backgroundColor =
    "rgba(0,0,0,0.95)";

tooltip.style.color = "white";

tooltip.style.fontFamily =
    '"PixelMplus12", sans-serif';

tooltip.style.fontSize =
    "clamp(16px, 1.5vw, 32px)";

tooltip.style.padding =
    "clamp(10px, 1vw, 20px)";

tooltip.style.border =
    "2px solid gold";

tooltip.style.borderRadius =
    "8px";

tooltip.style.width =
    "clamp(150px, 35vw, 300px)";

tooltip.style.whiteSpace =
    "nowrap";

tooltip.style.boxSizing =
    "border-box";