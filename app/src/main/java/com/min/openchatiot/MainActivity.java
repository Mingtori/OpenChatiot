package com.min.openchatiot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity{
    private String roomName;
    private FirebaseAuth firebaseAuth;

    private String id;
    private TextView textViewId;

    private DatabaseReference reference;

    private EditText editText;
    private LinearLayout layoutEdit;

    private RecyclerView recyclerView;
    private MessageAdapter adapter;

    private InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        roomName = intent.getStringExtra("chatroom");

        //키보드 show, hide 기능에 필요한 객체 생성
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        //firebase 사용자 정보 및 데이터베이스 사용을 위한 객체 생성
        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        textViewId = findViewById(R.id.textView_id);
        editText = findViewById(R.id.editText_message);
        layoutEdit =  findViewById(R.id.layout_edit);


        id = firebaseUser.getEmail().split("@")[0];
        textViewId.setText(id);

        //채팅 현형 리스트 구현
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MessageAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                로그아웃 버튼을 통해 firebase 로그아웃을 진행 후
                채팅화면을 닫고 로그인 화면을 표시
                 */
                firebaseAuth.signOut(); //firebase 로그아웃
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                MainActivity.this.finish();
            }
        });

        findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                firebase 데이터베이스 내의 chat 테이블에 Message 데이터클래스 자체를 삽입
                이럴 경우 나중에 데이터를 가져올 때 class자체로 다시 가져올 수 있어 관리가 용이
                 */
                Message message = new Message(id, new SimpleDateFormat("yyyy-MM-dd").format(new Date()), new SimpleDateFormat("ahh:mm").format(new Date()));
                message.setText(editText.getText().toString());

                //firebase 데이터베이스 내부 chat 테이블로 데이터 전송
                reference.child(roomName).child("chat").push().setValue(message);
                editText.setText("");
            }
        });


        //chat이라는 테이블 내에 데이터에 대한 리스너
        reference.child(roomName).child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                /*
                최초 1회는 해당 테이블 내의 모든 데이터를 가져오며
                이후 해당 테이블 내에 데이터가 삽입될 때마다 이벤트가 실행된다.
                 */
                //추가가 된 경우만 리스트에 추가
                // 최초 1회 chat테이블의 모든 데이터를 가져온다
                Message message = dataSnapshot.getValue(Message.class); //삽입된 데이터를 가져온 후 클래스로 변환

                if(message != null) { //가져온 데이터가 Message 데이터 클래스로 제대로 변환된 경우
                    if(message.getId().equals(id)) { //가져온 데이터의 ID와 현재 접속된 사용자의 ID가 같은 경우 본인 글로 판단
                        message.setWho("me");
                    }
                    else {
                        message.setWho("you"); //위와 반대로 판단
                    }

                    adapter.addMessage(message); //recyclerview에 추가
                    recyclerView.smoothScrollToPosition(adapter.getItemCount()); //추가가 되고나서 제일 아래에 있는 View로 스크롤
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
}
