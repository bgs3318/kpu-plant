package kr.ac.kpu.se2018158037.tensor_test1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//검색
public class PlantInfoActivity extends Camera_activity {

    ImageView imageview;
    String taname, temail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_info);
        TextView first = findViewById(R.id.first);


        imageview = findViewById(R.id.image);

        Intent intent = getIntent();
        taname = intent.getStringExtra("value");
        System.out.println("플랜트 액티비티"+taname);
        plant(taname);

        Intent intent2 = getIntent();
        temail = intent2.getStringExtra("value4");
        ImageButton pbook;
        pbook = findViewById(R.id.Pbookmark);
        //String femail = temail;
        //String fname=taname;
        //System.out.println("인포 이메일"+femail);

        String femail = "234@234.234";
        String fname = "튤립";
        pbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bplant(fname, femail);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

/*
    public void onClick(View v){
        Intent intent = new Intent(PlantInfoActivity.this, MainActivity.class);
        switch(v.getId()){
            case R.id.first:
                startActivity(intent);
                break;
            default:
                break;
        }
    }
 */

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.report:
                Toast.makeText(this, "보고", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.recent:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void plant(final String name) {
        // 1. RequestQueue 생성 및 초기화
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //String url = "http://192.168.121.46/plantimage.php";
        //String url = "http://115.143.180.87:8000/plantimage.php";
        String url = "http://52.79.250.50/plantimage.php";
        // 2. Request Obejct인 StringRequest 생성


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("flower_image");

        System.out.println(name);

        String presult = name + ".jpg";

        System.out.println(presult);

        String image_path = "flower_image/"+presult ; //이미지 경로 지정

        if (pathReference == null) {
            Toast.makeText(PlantInfoActivity.this, "저장소에 사진이 없습니다." ,Toast.LENGTH_SHORT).show();
        } else {
            StorageReference submitProfile = storageReference.child(image_path); //여기에 삽입
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(PlantInfoActivity.this).load(uri).into(imageview);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }



        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("222222");
                        Log.d("result", "[" + response + "]"); // 서버와의 통신 결과 확인 목적
                        showJSONList(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", "[" + error.getMessage() + "]");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                return params;
            }
        };

        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request);
    }
    private void showJSONList(String response) {
        final TextView tfeature = findViewById(R.id.tv_feature);
        final TextView tname = findViewById(R.id.tv_name);
        final TextView tmanagement = findViewById(R.id.tv_management);
        final TextView tnotice = findViewById(R.id.tv_notice);
        // 서버 정보를 파싱하기 위한 변수 선언
        try {
            JSONArray jsonArray = new JSONArray(response.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.getString("name");
                tname.setText(name);
                String feature = jsonObject.getString("feature");
                tfeature.setText(feature);
                String management = jsonObject.getString("mangement");
                tmanagement.setText(management);
                String notice = jsonObject.getString("notice");
                tnotice.setText(notice);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bplant(String fname, String femail) {
        // 1. RequestQueue 생성 및 초기화
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        System.out.println(fname);
        System.out.println(femail);
        System.out.println("blant test");
        String url = "http://115.143.180.87:8000/Register2.php";
        //String url = "http://192.168.123.240/Register2.php";
        // 2. Request Obejct인 StringRequest 생성

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("result", "[" + response + "]"); // 서버와의 통신 결과 확인 목적
                        Toast.makeText(getApplicationContext(),fname+"이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", "[" + error.getMessage() + "]");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fname", fname);
                params.put("femail", femail);
                return params;
            }
        };

        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request);
    }
}