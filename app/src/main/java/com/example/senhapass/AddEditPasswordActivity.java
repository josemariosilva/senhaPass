package com.example.senhapass;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditPasswordActivity extends AppCompatActivity {

    EditText edtDescricao, edtSenha;
    Button btnSalvar;

    DBHelper db;

    boolean isEdit = false;
    int editId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔒 Impede printscreen e app aparecer no multitarefas
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(R.layout.activity_add_edit_password);

        db = new DBHelper(this);

        edtDescricao = findViewById(R.id.edtDescricao);
        edtSenha = findViewById(R.id.edtSenha);
        btnSalvar = findViewById(R.id.btnSalvar);

        //==========================
        //       EDIÇÃO
        //==========================
        if (getIntent().hasExtra("id")) {
            isEdit = true;
            editId = getIntent().getIntExtra("id", -1);

            // ✔ Correção dos nomes enviados (descricao / senha)
            edtDescricao.setText(getIntent().getStringExtra("descricao"));
            edtSenha.setText(getIntent().getStringExtra("senha"));

            btnSalvar.setText("Atualizar");
        }

        //==========================
        // SALVAR / ATUALIZAR
        //==========================
        btnSalvar.setOnClickListener(v -> {
            String desc = edtDescricao.getText().toString().trim();
            String sen = edtSenha.getText().toString().trim();

            if (desc.isEmpty() || sen.isEmpty()) {
                Toast.makeText(this, "Preencha tudo!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEdit) {

                boolean ok = db.updatePassword(editId, desc, sen);

                if (ok) {
                    Toast.makeText(this, "Atualizado!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao atualizar!", Toast.LENGTH_SHORT).show();
                }

            } else {

                long result = db.addPassword(desc, sen);

                if (result != -1) {
                    Toast.makeText(this, "Salvo!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 🔒 Evita que dados fiquem na memória se o app for suspenso
        edtSenha.setText("");
    }
}
