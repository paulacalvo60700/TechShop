-- Corrección opcional de textos de ejemplo. No modifica precios, inventario, imágenes ni categorías.
-- Ejecutar en MySQL Workbench, después de creaTablas.sql. Revisá antes si editaste descripciones manualmente.
USE techshop;
START TRANSACTION;
UPDATE producto
SET detalle = CASE descripcion
    WHEN 'Monitor AOC 19' THEN 'Monitor AOC de 19 pulgadas para trabajo, estudio y uso diario.'
    WHEN 'Monitor MAC' THEN 'Pantalla para estación de trabajo, adecuada para tareas de oficina y estudio.'
    WHEN 'Monitor Flex 21' THEN 'Monitor Flex de 21 pulgadas para actividades cotidianas en computadora.'
    WHEN 'Monitor Flex 36' THEN 'Monitor Flex de 36 pulgadas con área amplia de visualización.'
    WHEN 'Teclado español everex' THEN 'Teclado Everex con distribución en español para escribir cómodamente.'
    WHEN 'Teclado fisico gamer' THEN 'Teclado orientado a videojuegos y escritura de uso diario.'
    WHEN 'Teclado usb compacto' THEN 'Teclado compacto con conexión USB, ideal para espacios reducidos.'
    WHEN 'Teclado Monitor Flex' THEN 'Teclado para estaciones de trabajo con configuraciones flexibles.'
    WHEN 'CPU Intel 7i' THEN 'Componente de procesamiento Intel para computadoras compatibles.'
    WHEN 'CPU Intel Core 5i' THEN 'Componente de procesamiento Intel Core i5 para equipos compatibles.'
    WHEN 'AMD 7500' THEN 'Componente AMD modelo 7500; verificá la compatibilidad antes de comprar.'
    WHEN 'AMD 670' THEN 'Componente AMD modelo 670; verificá la compatibilidad antes de comprar.'
    WHEN 'Samsung S22' THEN 'Teléfono Samsung S22 para llamadas, mensajes y aplicaciones cotidianas.'
    WHEN 'Motorola X23' THEN 'Teléfono Motorola X23 para comunicación y uso diario.'
    WHEN 'Nokia 5430' THEN 'Teléfono Nokia 5430 para llamadas y mensajes.'
    WHEN 'Xiami x45' THEN 'Teléfono de la línea Xiaomi para comunicación y aplicaciones diarias.'
    ELSE detalle
END
WHERE descripcion IN (
  'Monitor AOC 19',
  'Monitor MAC',
  'Monitor Flex 21',
  'Monitor Flex 36',
  'Teclado español everex',
  'Teclado fisico gamer',
  'Teclado usb compacto',
  'Teclado Monitor Flex',
  'CPU Intel 7i',
  'CPU Intel Core 5i',
  'AMD 7500',
  'AMD 670',
  'Samsung S22',
  'Motorola X23',
  'Nokia 5430',
  'Xiami x45'
);
COMMIT;
SELECT id_producto, descripcion, detalle FROM producto ORDER BY id_producto;