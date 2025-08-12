# LightTalk

LightTalk es una aplicación Android que convierte mensajes de texto en código Morse y los transmite utilizando la linterna del dispositivo.  
Desarrollada en **Kotlin** con **Jetpack Compose** y la API de cámara, es ideal para pruebas, demostraciones o comunicación por luz.

---

## ✨ Características
- **Entrada de texto** para escribir el mensaje a transmitir.
- **Botón Enviar** para activar la linterna y emitir el mensaje en código Morse.
- **Compatibilidad** con dispositivos Android que tengan flash LED.
- **Interfaz simple** y minimalista en Jetpack Compose.
- **Permiso de cámara** solicitado en tiempo de ejecución.

---

## 📱 Requisitos
- Android 8.0 (API 26) o superior.
- Dispositivo con linterna LED.
- Android Studio **Hedgehog** o superior.
- JDK 17 o superior.

---

## 🚀 Instalación y compilación
1. Clona este repositorio o descarga el ZIP:
   ```bash
   git clone https://github.com/usuario/lighttalk.git
   ```
2. Abre la carpeta del proyecto en **Android Studio**.
3. Espera a que termine la sincronización de Gradle.
4. Conecta un dispositivo o inicia un emulador.
5. Ejecuta:
   - Menú **Run > Run 'app'**
     o  
   - Atajo **Shift + F10** (Windows/Linux) / **Control + R** (Mac).

---

## ⚙️ Permisos necesarios
En el archivo `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.CAMERA" />
```
Se solicita el permiso de cámara al iniciar la app para poder controlar la linterna.

---

## 📂 Estructura del proyecto
```
LightTalk/
├── app/                    # Código fuente principal
│   ├── src/main/java/       # Lógica en Kotlin
│   ├── src/main/res/        # Recursos (layouts, iconos, strings)
│   ├── AndroidManifest.xml
├── build.gradle.kts         # Configuración de compilación
├── settings.gradle.kts
├── README.md
└── LICENSE
```

---

## 🛠 Tecnologías usadas
- **Kotlin** como lenguaje principal.
- **Jetpack Compose** para la UI declarativa.
- **CameraManager** para controlar el flash LED.
- **Gradle** como sistema de compilación.

---

## 📄 Licencia
Este proyecto está licenciado bajo los términos de la **Apache License 2.0**.  
Consulta el archivo [LICENSE](LICENSE) para más detalles.

---

## 👤 Autor
**Ali Jose** — 2025  
