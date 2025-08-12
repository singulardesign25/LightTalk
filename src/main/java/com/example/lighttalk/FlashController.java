package com.example.lighttalk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Looper;

public class FlashController {

    private final CameraManager cameraManager;
    private final String cameraId;

    public FlashController(Context context) throws CameraAccessException {
        PackageManager pm = context.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            throw new RuntimeException("El dispositivo no tiene flash");
        }
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        cameraId = cameraManager.getCameraIdList()[0];
    }

    public void sendMorse(String morseCode) {
        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            for (char c : morseCode.toCharArray()) {
                try {
                    switch (c) {
                        case '.':
                            toggleFlash(true);
                            Thread.sleep(200); // punto = 200ms
                            toggleFlash(false);
                            Thread.sleep(200); // pausa entre señales
                            break;
                        case '-':
                            toggleFlash(true);
                            Thread.sleep(600); // raya = 600ms
                            toggleFlash(false);
                            Thread.sleep(200);
                            break;
                        case ' ':
                            Thread.sleep(600); // pausa entre letras
                            break;
                    }
                } catch (InterruptedException e) {
                    handler.post(() -> {
                        throw new RuntimeException("Error enviando morse", e);
                    });
                }
            }
        }).start();
    }

    private void toggleFlash(boolean status) {
        try {
            cameraManager.setTorchMode(cameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
