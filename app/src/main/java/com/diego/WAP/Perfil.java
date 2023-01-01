package com.diego.WAP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Perfil extends AppCompatActivity {

    private TextView nomeUsuario, emailUsuario;
    private Button bt_deslogar;
    private EditText edit_nome, edit_email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;
    private ImageView voltar;
    private Button bt_atualizar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getSupportActionBar().hide();
        IniciarComponentes();


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, MainActivity.class);
                startActivity(intent);
            }
        });
        bt_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Perfil.this, Login.class);
                startActivity(intent);
                finish();

            }
        });
        bt_atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edit_nome.getText().toString();

                if (nome.isEmpty()){
                    nomeUsuario.setText(nome);

                    reload();

                }else
                atualizarinf(v);

            }
        });


    }
       private void atualizarinf(View v){
           String nome = edit_nome.getText().toString();
           db.collection("Usuarios").document(usuarioID).update("nome", nome);



           Snackbar snackbar = Snackbar.make(v,"Informações atualizadas", Snackbar.LENGTH_SHORT);
           snackbar.setBackgroundTint(Color.WHITE);
           snackbar.setTextColor(Color.BLACK);
           snackbar.show();
    }



       private void reload(){
           overridePendingTransition(0, 0);
           startActivity(getIntent());
           overridePendingTransition(0, 0);
           finish();
       }
    @Override
    protected void onStart() {
        super.onStart();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference =  db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    nomeUsuario.setText(documentSnapshot.getString("nome"));
                    emailUsuario.setText(email);

                }


            }
        });

    }



    public void TextViewClicked(View view) {
        ViewSwitcher switcher = findViewById(R.id.Switcher1);
        switcher.showNext(); //or switcher.showPrevious();
        TextView myTV = switcher.findViewById(R.id.text_NomeUsuario);
        myTV.setText("value");
    }
    public void TextViewClicked2(View view) {
        ViewSwitcher switcher2 =  findViewById(R.id.Switcher2);
        switcher2.showNext(); //or switcher.showPrevious();
        TextView myTV2 = switcher2.findViewById(R.id.text_EmailUsuario);
        myTV2.setText("value");
    }

    private void IniciarComponentes (){
        nomeUsuario = findViewById(R.id.text_NomeUsuario);
        emailUsuario = findViewById(R.id.text_EmailUsuario);
        bt_deslogar = findViewById(R.id.bt_deslogar);
        voltar = findViewById(R.id.voltar3);
        bt_atualizar = findViewById(R.id.bt_atualizar);
        edit_nome = findViewById(R.id.Hidden_text);
        edit_email = findViewById(R.id.Hidden_text2);


    }
}