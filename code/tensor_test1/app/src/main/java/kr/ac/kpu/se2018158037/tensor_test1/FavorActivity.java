package kr.ac.kpu.se2018158037.tensor_test1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavorActivity extends AppCompatActivity {

    private List<User> userlist;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private ListActivity adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<User> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);
        String bname;
        Intent intent = getIntent();
        bname = intent.getStringExtra("value4");
        System.out.println("1111");
        System.out.println(bname);

        // 리스트를 생성한다.
        userlist = new ArrayList<User>();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<User>();
        arraylist.addAll(userlist);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new ListActivity(userlist, this);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
            }
        });

        //최근조회목록 클릭시 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String data = adapter.getItemid(position).toString();
                //data값 확
                //int cpos = listView.getCheckedItemPosition();
                User user = (User)parent.getItemAtPosition(position);
                String data=user.getplant_name();

                Toast.makeText(FavorActivity.this, "favor data : "+data, Toast.LENGTH_LONG);
                Intent intent = new Intent(FavorActivity.this, PlantInfoActivity.class);
                System.out.println("favor data : "+data);
                intent.putExtra("value",data);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                User user = (User)a_parent.getItemAtPosition(a_position);
                String dname=user.getplant_name();
                ldelete(dname);
                adapter.notifyDataSetChanged();
                Intent intent = getIntent();
                finish();
                startActivity(intent);

                return true;
            }
        });
        adapter.notifyDataSetChanged();
        plant(bname);
    }

    private void plant(final String femail) {
        // 1. RequestQueue 생성 및 초기화
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://115.143.180.87:8000/plantimage.php";
        // 2. Request Obejct인 StringRequest 생성

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
                params.put("femail", femail);
                return params;
            }
        };

        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request);
    }

    private void showJSONList(String response) {

        String femail, fname;

        try {
            //Toast.makeText(PlantInfo.this, response.toString(), Toast.LENGTH_SHORT).show();
            JSONArray jsonArray = new JSONArray(response.toString());
            System.out.println("더착");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                femail = jsonObject.getString("femail");

                fname = jsonObject.getString("fname");
                System.out.println(fname);
                User user = new User(femail, fname);
                userlist.add(user);
            }
            adapter.notifyDataSetChanged();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ldelete(String fname){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://115.143.180.87:8000/delete.php";
        //String url = "http://192.168.123.240/Register2.php";
        // 2. Request Obejct인 StringRequest 생성

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("result", "[" + response + "]"); // 서버와의 통신 결과 확인 목적
                        Toast.makeText(getApplicationContext(),fname+"이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };

        // 3) 생성한 StringRequest를 RequestQueue에 추가
        requestQueue.add(request);
        adapter.notifyDataSetChanged();
    }
}