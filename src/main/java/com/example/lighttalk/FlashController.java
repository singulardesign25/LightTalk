package com.example.lighttalk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class FlashController {

    private final CameraManager cameraManager;
    private final String cameraIdWithFlash;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final Context context;

    public FlashController(Context context) {
        this.context = context.getApplicationContext();

        PackageManager pm = this.context.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            // No flash hardware at all
            cameraManager = null;
            cameraIdWithFlash = null;
            return;
        }

        cameraManager = (CameraManager) this.context.getSystemService(Context.CAMERA_SERVICE);
        String foundId = null;
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cc = cameraManager.getCameraCharacteristics(id);
                Boolean hasFlash = cc.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                if (hasFlash != null && hasFlash) {
                    // choose this camera id
                    foundId = id;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            // If we can't query cameras, mark as unavailable
            foundId = null;
        }
        cameraIdWithFlash = foundId;
    }

    public boolean isAvailable() {
        return cameraManager != null && cameraIdWithFlash != null;
    }

    /**
     * Envía una cadena de código morse compuesta por '.', '-' y ' ' (espacio).
     * Corre en hilo propio para no bloquear UI.
     */
    public void sendMorse(String morseCode) {
        if (!isAvailable()) {
            uiHandler.post(() ->
                Toast.makeText(context, "Flash no disponible en este dispositivo", Toast.LENGTH_SHORT).show()
            );
            return;
        }

        new Thread(() -> {
            for (int i = 0; i < morseCode.length(); i++) {
                char c = morseCode.charAt(i);
                try {
                    switch (c) {
                        case '.':
                            setTorch(true);
                            Thread.sleep(200); // punto = 200ms
                            setTorch(false);
                            Thread.sleep(200); // pausa intra-símbolo
                            break;
                        case '-':
                            setTorch(true);
                            Thread.sleep(600); // raya = 600ms
                            setTorch(false);
                            Thread.sleep(200);
                            break;
                        case ' ':
                            // pausa entre letras/espacio
                            Thread.sleep(600);
                            break;
                        default:
                            // ignorar otros caracteres
                            Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    uiHandler.post(() ->
                        Toast.makeText(context, "Envío de Morse interrumpido", Toast.LENGTH_SHORT).show()
                    );
                    return;
                } catch (Exception e) {
                    // Atrapar cualquier excepción de camera y avisar al usuario sin crashear
                    final String msg = "Error al controlar el flash: " + e.getClass().getSimpleName();
                    uiHandler.post(() -> Toast.makeText(context, msg, Toast.LENGTH_LONG).show());
                    return;
                }
            }
        }).start();
    }

    private void setTorch(boolean on) throws CameraAccessException {
        if (cameraManager == null || cameraIdWithFlash == null) return;
        cameraManager.setTorchMode(cameraIdWithFlash, on);
    }
}

