package net.dell.playgame2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏界面的主布局
 * Created by dell on 2016/5/17.
 */
public class GameView extends GridLayout {

    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoint = new ArrayList<>();

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    /**
     * 初始化
     */
    private void initGameView() {
        setColumnCount(4);//四行
        setOnTouchListener(new OnTouchListener() {
            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        //如果是斜着滑动时，判断用户意图是上还是下
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -4) {
//                                Log.w("点击方向---", "向左边");
                                swipeLeft();
                            } else if (offsetX > 4) {
//                                Log.w("点击方向---", "向右边");
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -4) {
//                                Log.w("点击方向---", "向上边");
                                swipeUp();
                            } else if (offsetY > 4) {
//                                Log.w("点击方向---", "向下边");
                                swipeDown();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 获取布局宽高
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("onSizeChanged", "----------onSizeChanged");
        //求出最小值，留出10dp的空隙，分4列
        int cardWidth = (Math.min(w, h) - 10) / 4;
        //添加卡片
        addcards(cardWidth, cardWidth);
        startGame();
    }

    /**
     * 开始游戏
     */
    private void startGame() {
//        MainActivity.getMainActivity().clearScore();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        //添加两个随机数
        addRondonNum();
        addRondonNum();
    }

    /**
     * 添加卡片
     *
     * @param width
     * @param height
     */
    private void addcards(int width, int height) {
        Card c;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, width, height);
                cardsMap[x][y] = c;//记录二维数组
            }
        }
    }

    /**
     * 添加随机数
     */
    private void addRondonNum() {
        emptyPoint.clear();
        //遍历所有的位置
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoint.add(new Point(x, y));
                }
            }
        }
        Point p = emptyPoint.remove((int) (Math.random() * emptyPoint.size()));
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    /**
     * 滑动方向：左边
     */
    private void swipeLeft() {
        boolean isflag = false;//有合并就 添加一个随机数

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                //从当前位置往右边遍历
                for (int i = x + 1; i < 4; i++) {
                    //获取到有值
                    if (cardsMap[i][y].getNum() > 0) {
                        // 1.当前位置值=0,进行移动
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[i][y].getNum());
                            cardsMap[i][y].setNum(0);
                            x--;
                            isflag = true;
                        } else if (cardsMap[x][y].equals(cardsMap[i][y])) {
                            // 2.当前位置和获取到的值相同
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[i][y].setNum(0);
                            //计分
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            isflag = true;
                        }
                        break;
                    }
                }
            }
        }
        if (isflag) {
            addRondonNum();
            checkComplete();
        }
    }

    /**
     * 滑动方向：右边
     */
    private void swipeRight() {
        boolean isflag = false;//有合并就 添加一个随机数
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                //从当前位置往右边遍历
                for (int i = x - 1; i >= 0; i--) {
                    //获取到有值
                    if (cardsMap[i][y].getNum() > 0) {
                        // 1.当前位置值=0,进行移动
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[i][y].getNum());
                            cardsMap[i][y].setNum(0);
                            x++;
                            isflag = true;
                        } else if (cardsMap[x][y].equals(cardsMap[i][y])) {
                            // 2.当前位置和获取到的值相同
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[i][y].setNum(0);
                            //计分
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            isflag = true;
                        }
                        break;
                    }
                }
            }
        }
        if (isflag) {
            addRondonNum();
            checkComplete();
        }
    }

    /**
     * 滑动方向：上边
     */
    private void swipeUp() {
        boolean isflag = false;//有合并就 添加一个随机数
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                //从当前位置往右边遍历
                for (int i = y + 1; i < 4; i++) {
                    //获取到有值
                    if (cardsMap[x][i].getNum() > 0) {
                        // 1.当前位置值=0,进行移动
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][i].getNum());
                            cardsMap[x][i].setNum(0);
                            y--;
                            isflag = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][i])) {
                            // 2.当前位置和获取到的值相同
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][i].setNum(0);
                            //计分
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            isflag = true;
                        }
                        break;
                    }
                }
            }
        }
        if (isflag) {
            addRondonNum();
            checkComplete();
        }
    }

    /**
     * 滑动方向：下边
     */
    private void swipeDown() {
        boolean isflag = false;//有合并就 添加一个随机数
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                //从当前位置往右边遍历
                for (int i = y - 1; i >= 0; i--) {
                    //获取到有值
                    if (cardsMap[x][i].getNum() > 0) {
                        // 1.当前位置值=0,进行移动
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][i].getNum());
                            cardsMap[x][i].setNum(0);
                            y++;
                            isflag = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][i])) {
                            // 2.当前位置和获取到的值相同
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][i].setNum(0);
                            //计分
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            isflag = true;
                        }
                        break;
                    }
                }
            }
        }
        if (isflag) {
            addRondonNum();
            checkComplete();
        }
    }

    /**
     * 判断结束游戏
     */
    private void checkComplete() {
        boolean isComplete = true;//表示游戏结束
        //标签：表示跳出整个循环
        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() == 0 ||  //判断当前有空位置
                        (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y])) || //判断左边的数
                        (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y])) || //判断右边的数
                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1])) || //判断上边的数
                        (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) { //判断下边的数
                    isComplete = false;
                    break ALL;
                }
            }
        }
        if (isComplete) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Sorry~")
                    .setMessage("游戏结束了！")
                    .setPositiveButton("再来一次", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //清空计分重新开始
                            MainActivity.getMainActivity().clearScore();
                            startGame();
                        }
                    }).setCancelable(false).show();//点击空白区域不消失
        }
    }//end

}
