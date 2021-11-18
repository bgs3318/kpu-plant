package kr.ac.kpu.se2018158037.tensor_test1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class album_activity extends Camera_activity {


    ImageView album_imageView;
    TextView album_textView;
    Button album_detectButton, album_open;
    private static final int FROM_ALBUM = 1;

    private static final String MODEL_PATH = "model2.tflite";
    private static final boolean QUANT = true;
    private static final String LABEL_PATH = "labels2.txt"; //labels
    private static final int INPUT_SIZE = 224;

    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_show);

        album_imageView = findViewById(R.id.album_image);

        album_detectButton = findViewById(R.id.album_detect);
        album_open = findViewById(R.id.album_open);

        album_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");                      // 이미지만
                intent.setAction(Intent.ACTION_GET_CONTENT);    // 카메라(ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, FROM_ALBUM);
            }
        });

        initTensorFlowAndLoadModel();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 카메라를 다루지 않기 때문에 앨범 상수에 대해서 성공한 경우에 대해서만 처리
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != FROM_ALBUM || resultCode != RESULT_OK)
            return;
        try {
            int batchNum = 0;
            InputStream buf = getContentResolver().openInputStream(data.getData());
            Bitmap bitmap = BitmapFactory.decodeStream(buf);
            bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

            buf.close();

            //이미지 뷰에 선택한 사진 띄우기
            album_imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            album_imageView.setImageBitmap(bitmap);

            //이미지 결과를 텍스트뷰에 띄우기
            final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

            album_detectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cname = results.toString();
                    String result = cname.substring(1, cname.length()-1);
                    System.out.println(result);
                    System.out.println(cname);
                    Intent intent = new Intent(album_activity.this, PlantInfo2.class);
                    intent.putExtra("value2",result);
                    startActivity(intent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }



}
