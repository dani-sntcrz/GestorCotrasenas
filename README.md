# Gestor de Contraseñas Seguro

Una aplicación de consola en **Java** que permite almacenar y gestionar contraseñas de forma **segura**, aplicando técnicas de **cifrado, hashing y derivación de claves**.

---

## Funcionalidades

- Configurar una **contraseña maestra** para proteger el acceso.
- Agregar nuevas contraseñas para diferentes servicios.
- Listar y visualizar contraseñas almacenadas.
- Eliminar contraseñas de forma segura.
- Almacenamiento cifrado con AES y verificación de hash con SHA-256.

---

## Tecnologías utilizadas

- **Java 17**
- **AES (cifrado simétrico)**
- **PBKDF2WithHmacSHA256** (derivación de clave)
- **SHA-256** (hashing)
- **Git / GitHub** para control de versiones

---

## Estructura del proyecto
src/
- models/    ->  Modelo de datos PasswordEntry
- security/  ->  Hashing y criptografía
- store/     ->  Almacenamiento cifrado de contraseñas
- ui/        ->  Interfaz de consola
- Main.java  ->  Punto de entrada

---

## Ejecución

1. Clonar el repositorio:
   ```bash
   git clone git@github.com:dani-sntcrz/GestorCotrasenas.git
   cd PasswordManager
   ```
2. Compilar y ejecutar
   - Builder el proyecto desde el editor (Intellij)
   - Ejecutar el proyecto desde el archivo *Main*
3. Al iniciar por primera vez, configura tu contraseña maestra. Luego podras agregar, listar, visualizar o eliminar contraseñas.

---

## Seguridad aplicada

- Contraseña maestra protegida con hash y sal.
- Contraseñas cifradas con AES y almacenadas en passwords.encrypted.
- Derivación de clave segura con PBKDF2.
- Persistencia en archivos cifrados (sin texto plano).



---