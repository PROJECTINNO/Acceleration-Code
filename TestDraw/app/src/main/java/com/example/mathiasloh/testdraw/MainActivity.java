package com.example.mathiasloh.testdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    RelativeLayout layout;

    public class DrawCanvasCircle extends View {
        public DrawCanvasCircle(Context mContext) {
            super(mContext);
        }
        public void onDraw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            canvas.drawColor(Color.WHITE);
            paint.setColor(Color.BLUE);
            canvas.drawCircle(600, 600, 100, paint);
            float[] test = new float[8];
            test[0] = (float) 300.0;
            test[1] = (float) 300.0;
            test[2] = (float) 600.0;
            test[3] = (float) 600.0;
            test[4] = (float) 600.0;
            test[5] = (float) 600.0;
            test[6] = (float) 600.0;
            test[7] = (float) 1700.0;

            canvas.drawLines(test, paint);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.layout);
        DrawCanvasCircle pcc = new DrawCanvasCircle (this);
        Bitmap result = Bitmap.createBitmap(5000,5000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        pcc.draw(canvas);
        layout.addView(pcc);
    }
}
