package com.example.senhapass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.List;

public class PasswordListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PasswordAdapter adapter;
    List<PasswordModel> listaSenhas;
    Button btnAdd;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);

        db = new DBHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carregarSenhas();

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditPasswordActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarSenhas(); // Atualiza ao voltar da tela
    }

    private void carregarSenhas() {

        listaSenhas = db.getAllPasswords();

        adapter = new PasswordAdapter(this, listaSenhas);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(item -> {
            Intent i = new Intent(PasswordListActivity.this, AddEditPasswordActivity.class);

            i.putExtra("id", item.getId());
            i.putExtra("descricao", item.getDescricao());
            i.putExtra("senha", item.getSenha());

            startActivity(i);
        });
    }



    @Override
    protected void onPause() {
        super.onPause();

        // Só fecha se o app realmente for para o background
        if (!isChangingConfigurations()) {
            if (!isFinishing()) {
                finish();
            }
        }
    }


}
