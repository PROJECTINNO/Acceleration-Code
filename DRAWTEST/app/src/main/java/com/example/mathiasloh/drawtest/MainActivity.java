package com.example.mathiasloh.drawtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
//    private DrawView drawView;
    private RelativeLayout layout;

//    public class DrawView extends View {
//        Paint paint = new Paint();
//
//        public DrawView(Context context) {
//            super(context);
//            paint.setColor(Color.BLACK);
//        }
//
//        @Override
//        public void onDraw(Canvas canvas) {
//            canvas.drawLine(0, 0, 20, 20, paint);
//            canvas.drawLine(20, 0, 0, 20, paint);
//        }
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        drawView = new DrawView(this);
//
//        drawView.setBackgroundColor(Color.WHITE);
//        setContentView(drawView);
    }
}
