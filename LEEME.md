# TechShop SC-403 — Versión alineada con las filminas

Proyecto Maven para abrir en NetBeans. **Incluye historial Git en la rama `feature/sc403-filminas`, basada en el proyecto original. No se ha subido nada a GitHub.**

## Correspondencia con las clases

- **Semana 6:** `IndexController.java`, `templates/index.html` y `templates/consultas/fragmentos.html` (pestañas `tabs` y tarjetas `tarjetas`). Filtros por categoría en `/consultas/{idCategoria}`.
- **Semana 8:** `ConsultaController.java`, `ProductoRepository.java` y `ProductoService.java`. Tres consultas por rangos de precios (derivada, JPQL, SQL nativo); formularios en `consultas/fragmentos.html`.
- **Semana 9:** `SecurityConfig.java`, `login.html` y `general/fragmentos.html`. Login/logout, roles y `sec:authorize` para menú y acciones de edición. El reto final (limitar botones de productos solo ADMIN) está implementado en `producto/listado.html`.

La vista usa Bootstrap con encabezado verde, navegación simple, pestañas de categorías y tarjetas como el TechShop de las filminas. **No reproduce literalmente las capturas**, pero sigue su organización académica y ejemplos.

## Base de datos e imágenes

- **Sin Firebase.** Las imágenes se guardan dentro de la carpeta local `uploads/` (máximo 2MB).
- Sin requerir MySQL para la práctica: base de datos **H2 local persistente** en `data/techshop.mv.db`; se genera automáticamente al iniciar.
- Datos de ejemplo se cargan solo si la base está vacía.
- Se deja la opción para MySQL en `application-mysql.properties`, pero no está activa.
- El carrito es demostrativo: no procesa pagos ni reduce existencias.

## Iniciar con NetBeans 28

1. **File > Open Project** y elegir la carpeta `TechShop` dentro de este ZIP descomprimido.
2. `Clean and Build` para descargar dependencias; luego `Run Project`.
3. Abrir `http://localhost:8086/`.
4. Si ya hay otra aplicación en 8086, detenela antes de ejecutar.

## Credenciales de demostración (solo desarrollo)

| Usuario | Contraseña | Rol | Acceso |
|---|---|---|---|
| `juan` | `Demo2026!` | `ADMIN` | Administración, consultas y catálogo. CRUD completo. |
| `rebeca` | `Demo2026!` | `VENDEDOR` | Consulta de categorías/productos sin modificaciones. |
| `pedro` | `Demo2026!` | `USUARIO` | Inicio y carrito de demostración. |

Cambiá la contraseña de prueba antes de desplegar la aplicación en un servidor público. El login es un ejemplo de clase con usuarios en memoria.

## GitHub

Esta copia **sí** contiene `.git` y la rama `feature/sc403-filminas` con un commit local, preparada para subir mediante NetBeans/terminal. `origin` apunta a `https://github.com/paulacalvo60700/TechShop.git`. No se ha hecho ningún push.

1. Hacé respaldo de tu carpeta `tienda` sin el indicador Git. Cerrá esa copia en NetBeans.
2. Extraé este ZIP en una carpeta nueva (no mezcles las carpetas) y abrí esa carpeta como proyecto Maven.
3. Verificá `Clean and Build`, `Run Project` y `http://localhost:8086`.
4. Para subir los cambios: en NetBeans, botón derecho en proyecto > `Git` o `Team` > `Remote` > `Push` y seleccionar la rama `feature/sc403-filminas`. O con terminal desde esa carpeta: `git push -u origin feature/sc403-filminas`.
5. En GitHub creá un pull request hacia `master` **solo cuando hayas probado que funciona**. No hagás `force push` ni reemplaces `master` directamente.

Si no aparece el indicador de rama en NetBeans, verificá que abriste exactamente la carpeta raíz que contiene `.git` (en Finder, los archivos con punto suelen estar ocultos).
