package com.example.lighttalk;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private FlashController flashController;
    private static final int CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editTextMessage);
        Button buttonSend = findViewById(R.id.buttonSend);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            initFlash();
        }

        buttonSend.setOnClickListener(v -> {
            String text = editText.getText().toString();

            if (text.isEmpty()) {
                Toast.makeText(this, "Escribe un mensaje primero", Toast.LENGTH_SHORT).show();
                return;
            }

            if (flashController == null || !flashController.isAvailable()) {
                Toast.makeText(this, "Flash no disponible en este dispositivo", Toast.LENGTH_SHORT).show();
                return;
            }

            String morse = MorseEncoder.toMorse(text);
            flashController.sendMorse(morse);
        });
    }

    private void initFlash() {
        // Si FlashController lanza alguna excepción, la capturamos genéricamente
        try {
            flashController = new FlashController(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error inicializando el flash", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initFlash();
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
        }
    }
}
