package com.min.openchatiot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private EditText editTextId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextId = (EditText) findViewById(R.id.editText_id);

        //firebase 사용자 관련 처리를 위한 객체 생성
        firebaseAuth = FirebaseAuth.getInstance();

        /*
        이미 로그인된 사용자가 있는 경우
        로그인 화면을 종료하고 채팅 화면을 표시
         */
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            startChatRoom();
        }

        findViewById(R.id.button_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*firebase는 따로 중복체크를 할수있는 기능이 없기 때문에 로그인을 시도 후 로그인이 정상적으로 되었다면
                이미 계정이있다는 얘기이므로 다시 로그아웃 처리와 함께 계정이 있다는 문구 표시
                로그인이 불가능하다면 아직 계정정보가 없기 떄문에 정상적으로 사용가능하다는 문구 표시
                 */
                firebaseAuth.signInWithEmailAndPassword(editTextId.getText().toString() + "@emoticon.com", "emoticon")
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                /*
                                해당 메소드가 실행되면 정상적으로 로그인이 되었다는 의미이므로 중복체크로 생각했을 땐 중복된 아이디로 판단하면 됨.
                                 */
                                Toast.makeText(getApplicationContext(), "이미 등록된 아이디입니다.", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                /*
                                해당 메소드가 실행되면 로그인이 되지 않은 즉, 회원정보가 없는 경우이기 때문에 중복되지 않은 아이디로 판단
                                 */
                                Toast.makeText(getApplicationContext(), "사용가능합니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                firebase 로그인 처리 (중복 체크와 동일한 코드)
                 */
                firebaseAuth.signInWithEmailAndPassword(editTextId.getText().toString() + "@openchatiot.com", "emoticon")
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startChatRoom();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                /*
                                로그인이 실패했을 경우 계정정보가 있지 않다는 뜻으로 해당 정보로 바로 회원가입을 진행
                                 */
                                signUp();
                            }
                        });
            }
        });
    }

    private void signUp() {
        //firebase 회원가입 처리
        /*
        firebase 회원가입은 처리와 동시에 로그인 상태가 되기 때문에
        정상적으로 회원가입이 되었을 경우 바로 채팅화면을 표시
         */
        firebaseAuth.createUserWithEmailAndPassword(editTextId.getText().toString() + "@emoticon.com", "emoticon")
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startChatRoom();
                        }
                    }
                });
    }

    private void startChatRoom() {
        startActivity(new Intent(this, MainActivity.class));
        LoginActivity.this.finish();
    }
}