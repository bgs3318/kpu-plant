package kr.ac.kpu.se2018158037.tensor_test1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//카메라

public class PlantInfo2 extends Camera_activity {

    String tcname;
    ImageView imageview;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_info);
        TextView first = findViewById(R.id.first);
        imageview = findViewById(R.id.image);

        //식물이름 데이터 받아오기
        Intent intent = getIntent();
        tcname = intent.getStringExtra("value2");
        plant(tcname);
        databaseReference.child("Users").child(user.getUid()).child("recent").push().setValue(tcname);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    public void onClick(View v){
        Intent intent = new Intent(PlantInfo2.this, MainActivity.class);
        switch(v.getId()){
            case R.id.first:
                startActivity(intent);
                break;
            default:
                break;
        }
    }

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
        String url = "http://52.79.250.50/plantimage.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
        final ImageView timage = findViewById(R.id.image);
        // 서버 정보를 파싱하기 위한 변수 선언
        try {
            //Toast.makeText(PlantInfo.this, response.toString(), Toast.LENGTH_SHORT).show();
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
                String image = jsonObject.getString("image");
                int pi = getResources().getIdentifier(image,"drawable",getPackageName());
                timage.setImageResource(pi);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bplant(String fname, String femail) {
        // 1. RequestQueue 생성 및 초기화
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.123.240/Register2.php";
        // 2. Request Obejct인 StringRequest 생성

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("result", "[" + response + "]"); // 서버와의 통신 결과 확인 목적

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