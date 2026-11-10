-- Ejecutar como administrador del servidor MySQL en una instalación local.
-- Antes de ejecutarlo, reemplazá CAMBIAR_POR_CLAVE_MYSQL por una contraseña propia.
-- Si MySQL tiene otro usuario 'usuario_prueba'@'localhost', revisá su clave antes.
CREATE USER IF NOT EXISTS 'usuario_prueba'@'localhost' IDENTIFIED BY 'CAMBIAR_POR_CLAVE_MYSQL';
GRANT SELECT, INSERT, UPDATE, DELETE ON techshop.* TO 'usuario_prueba'@'localhost';
-- Si el usuario ya existía, CREATE USER IF NOT EXISTS no cambia su contraseña:
-- ALTER USER 'usuario_prueba'@'localhost' IDENTIFIED BY 'NUEVA_CLAVE_LOCAL';
