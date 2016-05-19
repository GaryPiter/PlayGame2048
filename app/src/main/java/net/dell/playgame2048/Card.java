package net.dell.playgame2048;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by dell on 2016/5/18.
 */
public class Card extends FrameLayout {

    private int num = 0;
    private TextView label;

    public Card(Context context) {
        super(context);
        label = new TextView(getContext());
        label.setBackgroundResource(R.color.colorCard);
        label.setGravity(Gravity.CENTER);
        label.setTextSize(38);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(10, 10, 0, 0);
        addView(label, lp);

        setNum(0);
    }

    /**
     * 判断两个卡片的数字是否相同
     *
     * @param card
     * @return
     */
    public boolean equals(Card card) {
        return getNum() == card.getNum();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num <= 0) {
            label.setText("");
            label.setBackgroundResource(R.color.colorCard);
        } else {
            label.setText(num + "");
            label.setBackgroundResource(R.color.colorCard_num);
        }
    }
}
