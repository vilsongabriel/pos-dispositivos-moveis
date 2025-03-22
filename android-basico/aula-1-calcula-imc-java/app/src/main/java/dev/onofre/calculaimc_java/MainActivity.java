package dev.onofre.calculaimc_java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText altura;
    private EditText peso;
    private TextView imc;
    private Button calcular;
    private Button limpar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        altura = findViewById(R.id.altura);
        peso = findViewById(R.id.peso);
        imc = findViewById(R.id.imc);
        calcular = findViewById(R.id.calcular);
        limpar = findViewById(R.id.limpar);

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double alturaDouble = Double.parseDouble(altura.getText().toString());
                double pesoDouble = Double.parseDouble(peso.getText().toString());
                double resultImc = pesoDouble / Math.pow(alturaDouble, 2);

                imc.setText(String.valueOf(resultImc));
            }
        });

        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                altura.setText("");
                peso.setText("");
                imc.setText("0.0");
            }
        });
    }
}