package com.tienda.config;

import com.tienda.domain.Categoria;
import com.tienda.domain.Producto;
import com.tienda.repository.CategoriaRepository;
import com.tienda.repository.ProductoRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!mysql")
public class DatosDemo {
    // Solo inicializa un catálogo de demostración en una H2 vacía.
    @Bean
    public CommandLineRunner datosIniciales(CategoriaRepository categorias, ProductoRepository productos) {
        return args -> {
            if (categorias.count() > 0 || productos.count() > 0) return;
            Categoria computadoras = categoria(categorias, "Computadoras");
            Categoria perifericos = categoria(categorias, "Periféricos");
            Categoria accesorios = categoria(categorias, "Accesorios");
            producto(productos, computadoras, "Laptop de estudio", "Equipo para clases y programación", "385000", 6);
            producto(productos, computadoras, "Monitor 24 pulgadas", "Pantalla Full HD para escritorio", "97500", 9);
            producto(productos, perifericos, "Teclado mecánico", "Teclado USB con iluminación", "28500", 12);
            producto(productos, perifericos, "Mouse inalámbrico", "Mouse óptico de uso diario", "12000", 15);
            producto(productos, accesorios, "Soporte para laptop", "Soporte ajustable para escritorio", "17500", 8);
            producto(productos, accesorios, "Adaptador USB-C", "Adaptador multipuerto", "22900", 10);
        };
    }

    private Categoria categoria(CategoriaRepository repo, String nombre) {
        Categoria c = new Categoria();
        c.setDescripcion(nombre);
        c.setActivo(true);
        return repo.save(c);
    }

    private void producto(ProductoRepository repo, Categoria categoria, String nombre,
                          String detalle, String precio, int existencias) {
        Producto p = new Producto();
        p.setCategoria(categoria);
        p.setDescripcion(nombre);
        p.setDetalle(detalle);
        p.setPrecio(new BigDecimal(precio));
        p.setExistencias(existencias);
        p.setActivo(true);
        repo.save(p);
    }
}
