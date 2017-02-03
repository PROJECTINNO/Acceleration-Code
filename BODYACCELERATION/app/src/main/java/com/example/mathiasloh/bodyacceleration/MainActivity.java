package com.example.mathiasloh.bodyacceleration;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.opencsv.CSVWriter;

@SuppressWarnings("ALL")
public class MainActivity extends Activity implements SensorEventListener, OnClickListener{
    private SensorManager sensorManager;
    private Button btnStart, btnAcceleration, btnUpload;
    private boolean started = false;
    private LinearLayout layout;
    private GraphicalView mChart;
    float[] mGravf = null;
    float[] mMagnetf = null;
    private AccelData sensorData;
    private ArrayList<Double> accx;
    private ArrayList<Double> accy;
    private ArrayList<Double> accz;
    private ArrayList<Double> vel_x;
    private ArrayList<Double> vel_y;
    private ArrayList<Double> vel_z;
    private ArrayList<Double> treated_data_x;
    private ArrayList<Double> treated_data_y;
    private ArrayList<Double> treated_data_z;
    private Canvas canvas = new Canvas();
    private Paint paint = new Paint();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (LinearLayout) findViewById(R.id.chart_container);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorData = new AccelData();
        verifyStoragePermissions(this);
        accx = new ArrayList<Double>();
        accy = new ArrayList<Double>();
        accz = new ArrayList<Double>();

        btnStart = (Button) findViewById(R.id.btnStart);
        btnAcceleration = (Button) findViewById(R.id.btnAcceleration);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnStart.setOnClickListener(this);
        btnAcceleration.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnAcceleration.setEnabled(false);
        if (sensorData == null || sensorData.getX().size() == 0) {
            btnUpload.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (started == true) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }


    @Override
    public void onSensorChanged(SensorEvent event) {

        // Gets the value of the sensor that has been changed
        if (started) {
            if ((mGravf != null) && (mMagnetf != null) && (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)) {
                long timestamp = System.currentTimeMillis();
                float[] deviceRelativeAcceleration = new float[4];
                deviceRelativeAcceleration[0] = event.values[0];
                deviceRelativeAcceleration[1] = event.values[1];
                deviceRelativeAcceleration[2] = event.values[2];
                deviceRelativeAcceleration[3] = 0;


                // If we want relative acceleration
                sensorData.addX(deviceRelativeAcceleration[0]);
                sensorData.addY(deviceRelativeAcceleration[1]);
                sensorData.addZ(deviceRelativeAcceleration[2]);
                sensorData.addTimestamp(timestamp);

                // If we want absolute acceleration
//                float[] R = new float[16], I = new float[16], earthAcc = new float[16];
//                SensorManager.getRotationMatrix(R, I, mGravf, mMagnetf);
//                float[] inv = new float[16];
//                android.opengl.Matrix.invertM(inv, 0, R, 0);
//                android.opengl.Matrix.multiplyMV(earthAcc, 0, inv, 0, deviceRelativeAcceleration, 0);
//                sensorData.addX(earthAcc[0]);
//                sensorData.addY(earthAcc[1]);
//                sensorData.addZ(earthAcc[2]);
//                sensorData.addTimestamp(timestamp);
            }
            else if (event.sensor.getType() == Sensor.TYPE_GRAVITY){
                mGravf = event.values;
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                mMagnetf = event.values;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                btnStart.setEnabled(false);
                btnAcceleration.setEnabled(true);
                btnUpload.setEnabled(true);
                sensorData = new AccelData();
                accx = new ArrayList<Double>();
                accy = new ArrayList<Double>();
                accz = new ArrayList<Double>();

                started = true;
                Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);

                Sensor grav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
                sensorManager.registerListener(this, grav, SensorManager.SENSOR_DELAY_GAME);

                Sensor mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                sensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_GAME);

                break;
            case R.id.btnAcceleration:
                btnStart.setEnabled(true);
                btnAcceleration.setEnabled(false);
                btnUpload.setEnabled(true);
                started = false;
                sensorManager.unregisterListener(this);
                layout.removeAllViews();
                openAcceleration();

                break;
            case R.id.btnUpload:
                btnStart.setEnabled(true);
                btnAcceleration.setEnabled(true);
                btnUpload.setEnabled(false);
                started = false;
                sensorManager.unregisterListener(this);
                layout.removeAllViews();
                try {
                    saveDataToCSV();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void openAcceleration() {
        if (sensorData != null || sensorData.getX().size() > 0) {
            long t = sensorData.getTimestamp().get(0);

            // --------- Setup MultiRenderer ----------------------------------- //
            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setXLabels(0);
            for (int i=1; i<100; i++){
                multiRenderer.addXTextLabel(1000*i, Integer.toString(i));
            }
            multiRenderer.setYLabels(6);
            multiRenderer.setLabelsColor(Color.BLACK);
            multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
            multiRenderer.setChartTitle("Time vs Acceleration (x,y,z)");
            multiRenderer.setXTitle("Sensor Data (s)");
            multiRenderer.setYTitle("Values of Acceleration (m.s^-2)");
            multiRenderer.setShowCustomTextGrid(true);
            multiRenderer.setLegendTextSize(20);
            multiRenderer.setLabelsTextSize(20);
            multiRenderer.setAxisTitleTextSize(20);
            multiRenderer.setChartTitleTextSize(40);
            multiRenderer.setFitLegend(true);
            multiRenderer.setLegendHeight(20);
            multiRenderer.setZoomButtonsVisible(false);
            multiRenderer.setXAxisMax(5000);
            multiRenderer.setYAxisMax(5);
            multiRenderer.setYLabelsColor(0, Color.BLACK);
            multiRenderer.setXLabelsColor(Color.BLACK);
            multiRenderer.setScale(3);
            // --------- End Setup MultiRenderer --------------------------------//


            // --------- Raw Acceleration Data ---------------------------------//
            XYSeries xSeries = new XYSeries("X");
            XYSeries ySeries = new XYSeries("Y");
            XYSeries zSeries = new XYSeries("Z");

            for (int i = 0; i < sensorData.getX().size();i++){
                xSeries.add(sensorData.getTimestamp().get(i) - t, sensorData.getX().get(i));
                ySeries.add(sensorData.getTimestamp().get(i) - t, sensorData.getY().get(i));
                zSeries.add(sensorData.getTimestamp().get(i) - t, sensorData.getZ().get(i));
            }

            dataset.addSeries(xSeries);
            dataset.addSeries(ySeries);
            dataset.addSeries(zSeries);

            XYSeriesRenderer xRenderer = new XYSeriesRenderer();
            xRenderer.setColor(Color.RED);
            xRenderer.setPointStyle(PointStyle.POINT);
            xRenderer.setFillPoints(true);
            xRenderer.setLineWidth(1);
            xRenderer.setDisplayChartValues(false);

            XYSeriesRenderer yRenderer = new XYSeriesRenderer();
            yRenderer.setColor(Color.GREEN);
            yRenderer.setPointStyle(PointStyle.POINT);
            yRenderer.setFillPoints(true);
            yRenderer.setLineWidth(1);
            yRenderer.setDisplayChartValues(false);

            XYSeriesRenderer zRenderer = new XYSeriesRenderer();
            zRenderer.setColor(Color.BLUE);
            zRenderer.setPointStyle(PointStyle.POINT);
            zRenderer.setFillPoints(true);
            zRenderer.setLineWidth(1);
            zRenderer.setDisplayChartValues(false);

            multiRenderer.addSeriesRenderer(xRenderer);
            multiRenderer.addSeriesRenderer(yRenderer);
            multiRenderer.addSeriesRenderer(zRenderer);
            // --------- Raw Acceleration Data ---------------------------------//


            // --------- Smooth Acceleration Data -----------------------------//
            XYSeries accxSeries = new XYSeries("accxAverage");
            XYSeries accySeries = new XYSeries("accyAverage");
            XYSeries acczSeries = new XYSeries("acczAverage");

            accx = TestAlgorithms.calculateAverage(sensorData.getX());
            accy = TestAlgorithms.calculateAverage(sensorData.getY());
            accz = TestAlgorithms.calculateAverage(sensorData.getZ());


            for (int i = 0;i < sensorData.getX().size();i++){
                accxSeries.add(sensorData.getTimestamp().get(i) -t, accx.get(i));
                accySeries.add(sensorData.getTimestamp().get(i) -t, accy.get(i));
                acczSeries.add(sensorData.getTimestamp().get(i) -t, accz.get(i));
            }


            dataset.addSeries(accxSeries);
            dataset.addSeries(accySeries);
            dataset.addSeries(acczSeries);


            XYSeriesRenderer accxRenderer = new XYSeriesRenderer();
            accxRenderer.setColor(Color.BLACK);
            accxRenderer.setPointStyle(PointStyle.POINT);
            accxRenderer.setFillPoints(true);
            accxRenderer.setLineWidth(1);
            accxRenderer.setDisplayChartValues(false);

            XYSeriesRenderer accyRenderer = new XYSeriesRenderer();
            accyRenderer.setColor(Color.CYAN);
            accyRenderer.setPointStyle(PointStyle.POINT);
            accyRenderer.setFillPoints(true);
            accyRenderer.setLineWidth(1);
            accyRenderer.setDisplayChartValues(false);

            XYSeriesRenderer acczRenderer = new XYSeriesRenderer();
            acczRenderer.setColor(Color.MAGENTA);
            acczRenderer.setPointStyle(PointStyle.POINT);
            acczRenderer.setFillPoints(true);
            acczRenderer.setLineWidth(1);
            acczRenderer.setDisplayChartValues(false);

            multiRenderer.addSeriesRenderer(accxRenderer);
            multiRenderer.addSeriesRenderer(accyRenderer);
            multiRenderer.addSeriesRenderer(acczRenderer);
            // --------- Smooth Acceleration Data -----------------------------//


            // --------- Plotting the graphs ---------------------------------//
            mChart = ChartFactory.getLineChartView(getBaseContext(), dataset,
                    multiRenderer);

            layout.addView(mChart);
            // --------- End Plotting the graphs -----------------------------//
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void openComparison() {
        if (sensorData != null || sensorData.getX().size() > 0) {
            // --------- Setup MultiRenderer ----------------------------------- //
            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setXLabels(6);

            multiRenderer.setYLabels(6);
            multiRenderer.setLabelsColor(Color.BLACK);
            multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
            multiRenderer.setChartTitle("x Acceleration vs y Acceleration");
            multiRenderer.setXTitle("x Acceleration (m.s^-2)");
            multiRenderer.setYTitle("y Acceleration (m.s^-2)");
            multiRenderer.setShowCustomTextGrid(true);
            multiRenderer.setLegendTextSize(20);
            multiRenderer.setLabelsTextSize(20);
            multiRenderer.setAxisTitleTextSize(20);
            multiRenderer.setChartTitleTextSize(40);
            multiRenderer.setFitLegend(true);
            multiRenderer.setLegendHeight(20);
            multiRenderer.setZoomButtonsVisible(false);
            multiRenderer.setXAxisMax(5);
            multiRenderer.setYAxisMax(5);
            multiRenderer.setYLabelsColor(0, Color.BLACK);
            multiRenderer.setXLabelsColor(Color.BLACK);
            multiRenderer.setScale(3);
            multiRenderer.setPointSize(40);

            // --------- End Setup MultiRenderer --------------------------------//

            // --------- Raw Acceleration Data ---------------------------------//
            XYSeries xySeries = new XYSeries("raw x-y");


            for (int i = 0; i < sensorData.getX().size(); i++) {
                xySeries.add(sensorData.getX().get(i), sensorData.getY().get(i));
            }

            dataset.addSeries(xySeries);

            XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
            xyRenderer.setColor(Color.RED);
            xyRenderer.setPointStyle(PointStyle.POINT);
            xyRenderer.setFillPoints(true);
            xyRenderer.setLineWidth(1);
            xyRenderer.setDisplayChartValues(false);


            multiRenderer.addSeriesRenderer(xyRenderer);
            // --------- Raw Acceleration Data ---------------------------------//


            // --------- Smooth Acceleration Data -----------------------------//
            XYSeries accxySeries = new XYSeries("accxAverage");

            accx = new ArrayList<Double>();
            accy = new ArrayList<Double>();
            accx = TestAlgorithms.calculateAverage(sensorData.getX());
            accy = TestAlgorithms.calculateAverage(sensorData.getY());


            for (int i = 0; i < sensorData.getX().size(); i++) {
                accxySeries.add(accx.get(i), accy.get(i));
            }


            dataset.addSeries(accxySeries);

            XYSeriesRenderer accxyRenderer = new XYSeriesRenderer();
            accxyRenderer.setColor(Color.BLACK);
            accxyRenderer.setPointStyle(PointStyle.POINT);
            accxyRenderer.setFillPoints(true);
            accxyRenderer.setLineWidth(1);
            accxyRenderer.setDisplayChartValues(false);


            multiRenderer.addSeriesRenderer(accxyRenderer);
            // --------- Smooth Acceleration Data -----------------------------//

            // --------- Plotting the graphs ---------------------------------//
//            mChart = ChartFactory.getLineChartView(getBaseContext(), dataset,
//                    multiRenderer);
//
//            layout.addView(mChart);
            // --------- End Plotting the graphs -----------------------------//


            // --------- Plotting the drawing ---------------------------------//
            ArrayList<Float> points = new ArrayList<Float>();
            for(int i = 0; i < accx.size()-1; i+=2){
                points.add(Float.parseFloat(Double.toString(accx.get(i))));
                points.add(Float.parseFloat(Double.toString(accy.get(i))));
                points.add(Float.parseFloat(Double.toString(accx.get(i+1))));
                points.add(Float.parseFloat(Double.toString(accy.get(i+1))));
            }

            float res[] = new float[accx.size()*2];
            for(int i = 0; i<points.size();i++){
                res[i] = points.get(i);
                System.out.println(res[i]);
            }
            paint = new Paint();
            paint.setColor(Color.parseColor("#CD5C5C"));
            paint.setStyle(Paint.Style.STROKE);
            Bitmap bg = Bitmap.createBitmap(1000,1000, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bg);
            canvas.drawCircle(500,500,200,paint);
            canvas.drawLines(res ,paint);
//            mDrawing = new MyView(this, res);
            layout.setBackground(new BitmapDrawable(getResources(), bg));

            // --------- End Plotting the drawing -----------------------------//
        }
    }
    private void saveDataToCSV() throws IOException {
        String baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String fileName = "AccelerationData.csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);

        CSVWriter writer;

        if (f.exists() && !f.isDirectory()){
            f.delete();
            f = new File(filePath);
            writer = new CSVWriter(new FileWriter(filePath));
        } else {
            writer = new CSVWriter(new FileWriter(filePath));
        }

        String[] data = "X,Y,Z".split(",");
        writer.writeNext(data);

        for (int i = 0; i < sensorData.getX().size(); i++){
            String[] entry = {Double.toString(sensorData.getX().get(i)),
                    Double.toString(sensorData.getY().get(i)), Double.toString(sensorData.getZ().get(i))};
            writer.writeNext(entry);
        }

        writer.close();
    }
}
