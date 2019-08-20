package com.google.developer.bugmaster.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.developer.bugmaster.R;

//TODO: This class should be used in the insect list to display danger level
@SuppressLint("AppCompatCustomView")
public class DangerLevelView extends TextView {

    public DangerLevelView(Context context) {
        super(context);
    }

    public DangerLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setDangerLevel(int dangerLevel) {
        //TODO: Update the view appropriately based on the level input
        String[] colorArray = getContext().getResources().getStringArray(R.array.dangerColors);
        GradientDrawable ovalShape = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.background_danger);
        if (dangerLevel == 1){
            ovalShape.setColor(Color.parseColor(colorArray[0]));
        } else if (dangerLevel == 2){
            ovalShape.setColor(Color.parseColor(colorArray[1]));
        } else if (dangerLevel == 3){
            ovalShape.setColor(Color.parseColor(colorArray[2]));
        } else if (dangerLevel == 4){
            ovalShape.setColor(Color.parseColor(colorArray[3]));
        } else if (dangerLevel == 5){
            ovalShape.setColor(Color.parseColor(colorArray[4]));
        } else if (dangerLevel == 6){
            ovalShape.setColor(Color.parseColor(colorArray[5]));
        } else if (dangerLevel == 7){
            ovalShape.setColor(Color.parseColor(colorArray[6]));
        } else if (dangerLevel == 8){
            ovalShape.setColor(Color.parseColor(colorArray[7]));
        } else if (dangerLevel == 9){
            ovalShape.setColor(Color.parseColor(colorArray[8]));
        }else if (dangerLevel == 10){
            ovalShape.setColor(Color.parseColor(colorArray[9]));
        }
    }

    public int getDangerLevel() {
        //TODO: Report the current level back as an integer
        return -1;
    }
}
