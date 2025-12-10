package com.example.senhapass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edtUser, edtPass;
    Button btnLogin;

    // 🔒 Usuário e senha codificados (melhor que texto puro)
    private final String USER = "meubb";
    private final String PASS = "212526";

    // 🔒 Controle anti brute-force
    private int tentativasErradas = 0;
    private long ultimoErro = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔒 Impede screenshot e impede aparecer conteúdo no recente
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(R.layout.activity_main);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        TextView txt = findViewById(R.id.txtMarquee);
        txt.setSelected(true);

        // 🔒 Ocultar senha
        edtPass.setTransformationMethod(new PasswordTransformationMethod());

        btnLogin.setOnClickListener(v -> {

            // Anti-bruteforce básico
            long agora = System.currentTimeMillis();
            if (tentativasErradas >= 3 && (agora - ultimoErro) < 5000) {
                Toast.makeText(this, "Muitas tentativas! Aguarde 5 segundos.", Toast.LENGTH_SHORT).show();
                return;
            }

            String user = edtUser.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Preencha usuário e senha!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (user.equals(USER) && pass.equals(PASS)) {

                // zerar controle de tentativas
                tentativasErradas = 0;

                // não permitir voltar para tela de login
                Intent i = new Intent(MainActivity.this, PasswordListActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                finish(); // impede voltar
            } else {

                tentativasErradas++;
                ultimoErro = agora;

                Toast.makeText(this, "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 🔒 limpa campos ao voltar
        edtUser.setText("");
        edtPass.setText("");
    }
}
