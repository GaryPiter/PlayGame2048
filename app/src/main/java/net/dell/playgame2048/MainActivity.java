package net.dell.playgame2048;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    private int score = 0;
    private TextView tvScore;
    private static MainActivity mainActivity = null;

    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvScore = (TextView) findViewById(R.id.tv_score);
        clearScore();//每次启动都有清空计分
    }

    /**
     * 清空分数
     */
    public void clearScore() {
        score = 0;
        showScore();
    }

    /**
     * 显示分数
     */
    public void showScore() {
        tvScore.setText(score + "");
    }

    /**
     * 添加分数
     */
    public void addScore(int s) {
        score += s;
        showScore();
    }
}
