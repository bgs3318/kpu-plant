package kr.ac.kpu.se2018158037.tensor_test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //new add2

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView recent_view;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<>();
    //new add2
    ImageButton menuRecent, menuCamera, menuGallery, menuBookmark;
    private static MainActivity mInstance;
    public static final String TAG = MainActivity.class
            .getSimpleName();
    private RequestQueue mRequestQueue;
    SearchView searchView;

    String uid;

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    List<String> uidList = new ArrayList<>();
    String user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuRecent = findViewById(R.id.menuRecent);
        menuCamera = findViewById(R.id.menuCamera);
        menuGallery = findViewById(R.id.menuGallery);
        menuBookmark = findViewById(R.id.menuBookmark);


        recent_view = (ListView) findViewById(R.id.recent_list);


        Intent intent = getIntent();
        user_email = intent.getStringExtra("value1");
        System.out.println("user_email : " + user_email);



        initDatabase();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        recent_view.setAdapter(adapter);



        mReference = mDatabase.getReference().child("Users").child(user.getUid()).child("recent");; // 변경값을 확인할 child 이름
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                uidList.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    String msg2 = messageData.getValue().toString();
                    String uidKey = messageData.getKey();
                    uidList.add(uidKey);
                    System.out.println(uidList);

                    Array.add(msg2);
                    adapter.add(msg2);
                }
                adapter.notifyDataSetChanged();
                recent_view.setSelection(adapter.getCount() - 1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        com.example.android.swipedismiss.SwipeDismissListViewTouchListener touchListener =
                new com.example.android.swipedismiss.SwipeDismissListViewTouchListener(recent_view,
                        new com.example.android.swipedismiss.SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    String data = adapter.getItem(position);

                                    adapter.remove(adapter.getItem(position));

                                    Toast.makeText(MainActivity.this, data + "position : "+position+"-> Click event", Toast.LENGTH_SHORT).show();
                                    //여기에 추가

                                    databaseReference.child("Users").child(user.getUid()).child("recent").child(uidList.get(position)).removeValue();;
                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
        recent_view.setOnTouchListener(touchListener);
        recent_view.setOnScrollListener(touchListener.makeScrollListener());

            //new add5

        //최근조회목록 클릭시 이벤트 처리
        recent_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String) adapter.getItem(position);
                //data값 확인
                Toast.makeText(MainActivity.this, "메인에서"+data + " Click event", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, PlantInfoActivity.class);
                intent.putExtra("value",data);
                intent.putExtra("value7",data);
                startActivity(intent);
            }
        });

        menuRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "현재 액티비티 입니다.", Toast.LENGTH_SHORT).show();

            }
        });

        //카메라 레이아웃
        menuCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Camera_activity.class);
                startActivity(intent);
            }
        });

        //엘범으로 이동
        menuGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), album_activity.class);
                startActivity(intent);
            }
        });

        menuBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FavorActivity.class);
                intent.putExtra("value4",user_email);
                startActivity(intent);
            }
        });
    }

    //상단검색바
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);

        //리스트 뷰 목록을 위한 어뎁터
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        recent_view.setAdapter(adapter);

        //initDatabase();


        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("식물을 검색합니다.");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {

                String sname = searchView.getQuery().toString();
                System.out.println("메인 액티비티"+sname);
                //databaseReference.child("Users").child(user.getUid()).child("recent").push().setValue(sname);

                uidList.clear(); //최근조회목록의 uid를 비워준다.


                databaseReference.child("Users").child(user.getUid()).child("recent").push().setValue(sname);
                mReference = mDatabase.getReference().child("Users").child(user.getUid()).child("recent"); // 변경값을 확인할 child 이름
                // 데이터를 불러올 child 이름
                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter.clear();

                        for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                            // child 내에 있는 데이터만큼 반복합니다.
                            String msg2 = messageData.getValue().toString();
                            Array.add(msg2);
                            adapter.add(msg2);

                        }
                        adapter.notifyDataSetChanged();
                        recent_view.setSelection(adapter.getCount() - 1);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //new add5

                Intent intent = new Intent(MainActivity.this, PlantInfoActivity.class);
                intent.putExtra("value",sname);
                startActivity(intent);

                return true;


                //new add5


            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(MainActivity.this, "검색중 : "+newText , Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.report :
                Toast.makeText(this, "보고", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.recent :
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//newadd6
    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }
    //newadd6

}

