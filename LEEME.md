# TechShop SC-403 — MySQL y filminas de la profesora

**Se usa MySQL como base principal; no H2, no Firebase.** Proyecto para abrir en NetBeans 28 con Java 17 y Spring Boot. Se basa en `tienda` y conserva las semanas 6, 8 y 9 (catálogo, consultas JPA/JPQL/SQL y login con roles).

## 1. La base de datos de la profesora (Semana 3)

El archivo **`db/creaTablas.sql`** deriva de `RecursosLec03/creaTablas.sql` aportado por la profesora. Incluye las **9 tablas** `categoria`, `producto`, `usuario`, `factura`, `venta`, `rol`, `usuario_rol`, `ruta`, `constante`, las claves foráneas y datos de prueba originales de categorías/productos/ventas. Se reemplazaron las claves de prueba de PayPal por `NO_CONFIGURADO`, los datos personales de muestra y se retiraron los `DROP` y comandos de creación de cuentas. **El archivo original de clase permanece en el ZIP `RecursosLec03 (3).zip` que compartiste**.

**Importante:** No ejecutes dos veces `db/creaTablas.sql` sobre la misma base. El script está destinado a una base `techshop` vacía. Si tenés una base `techshop` con datos, hacé respaldo primero; el script original de la profesora contiene `DROP DATABASE IF EXISTS techshop` (destruye los datos anteriores).

1. Instalá/iniciá MySQL Server 8.x y conectate con MySQL Workbench (o cualquier cliente compatible) usando una cuenta administradora. Workbench es el cliente: también hace falta el servidor.
2. Abrí **`db/creaTablas.sql`** y ejecutalo una sola vez. Esto crea `techshop` y carga las tablas y datos.
3. Abrí **`db/configurar_usuario_mysql.sql`**, sustituí `CAMBIAR_POR_CLAVE_MYSQL` por tu contraseña local segura y ejecutalo con permisos de administrador. Es el usuario de la aplicación, no el usuario que usás en el formulario web.
4. Copiá `config/techshop-local.properties.example` a **`config/techshop-local.properties`**. En ese último archivo reemplazá `ESCRIBIR_AQUI_LA_CLAVE_MYSQL` por la misma clave que pusiste en el paso 3. El archivo se ignora en Git.
5. En NetBeans, abrí la carpeta del proyecto `tienda`, ejecutá **Clean and Build** y después **Run**. Luego visitá `http://localhost:8086/`.

Para comprobar la carga en Workbench:

```sql
USE techshop;
SHOW TABLES;
SELECT id_categoria, descripcion FROM categoria;
SELECT id_producto, descripcion, precio FROM producto LIMIT 10;
```

## 2. Archivos de conexión

- `src/main/resources/application.properties` ahora usa `jdbc:mysql://localhost:3306/techshop` con el usuario `usuario_prueba`, sin contraseña dentro de Git.
- `spring.jpa.hibernate.ddl-auto=validate`: Hibernate comprueba el esquema, **no crea ni borra tablas**.
- El conector MySQL ya está en `pom.xml` y se retiró H2.
- No se insertan productos de demostración al arrancar: los datos vienen del script SQL.
- Si tu servidor no está en `localhost:3306`, configurá `TECHSHOP_DB_URL`.
- Si necesitás otra cuenta, configurá `TECHSHOP_DB_USER` y `TECHSHOP_DB_PASSWORD` mediante variables de entorno.

## 3. Seguridad de la Semana 9

Los usuarios de Spring Security siguen temporalmente **en memoria**, como las filminas de la semana 9. `juan`, `rebeca`, `pedro` usan la contraseña de demostración `Demo2026!`. Estos usuarios **no se autentican contra la tabla `usuario`** todavía. Los accesos y restricciones se prueban desde `login.html`.

## 4. Imágenes y advertencias

- Sin Firebase. Las imágenes nuevas se guardan en `uploads/` (carpeta ignorada por Git). Las URLs de imágenes de productos incluidas en el SQL original pueden ser externas.
- H2 y MySQL **no comparten datos automáticamente**: lo que guardaste antes usando H2 en tu Mac no se migra con este cambio. Se podrían exportar desde tu archivo local `data/techshop.mv.db` si querés recuperarlo.
- Si falla el inicio por `Communications link failure`, el servidor MySQL no es accesible. Si falla con `Access denied for user`, revisá cuenta y contraseña. Si falla con `Schema-validation: missing table`, todavía falta ejecutar el script SQL.
- No se ejecutó el script en tu Mac: este ZIP cambia el proyecto y prepara el SQL, pero vos debés correrlo en tu servidor local y probar NetBeans.

## 5. GitHub

El ZIP incluye `.git` para trabajar en la rama **`feature/sc403-mysql`**. Antes de subir, revisá los cambios en NetBeans. Nunca subás `config/techshop-local.properties` ni el archivo original de la profesora con credenciales de demostración. `master` no debe sobreescribirse ni recibir `force push`.
