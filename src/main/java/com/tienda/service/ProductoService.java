package com.tienda.service;

import com.tienda.domain.Categoria;
import com.tienda.domain.Producto;
import com.tienda.repository.CategoriaRepository;
import com.tienda.repository.ProductoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductoService {
    private final ProductoRepository productos;
    private final CategoriaRepository categorias;
    private final LocalImageService imagenes;

    public ProductoService(ProductoRepository productos, CategoriaRepository categorias,
                           LocalImageService imagenes) {
        this.productos = productos;
        this.categorias = categorias;
        this.imagenes = imagenes;
    }

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean soloActivos) {
        return soloActivos ? productos.findByActivoTrue() : productos.findAll();
    }

    @Transactional(readOnly = true)
    public List<Producto> getProductosActivosPorCategoria(Integer idCategoria) {
        return productos.findByCategoria_IdCategoriaAndActivoTrue(idCategoria);
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer id) {
        return productos.findById(id);
    }

    @Transactional
    public void save(Producto producto, MultipartFile imagenFile) {
        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() == null) {
            throw new IllegalArgumentException("Seleccioná una categoría para el producto.");
        }
        Categoria categoria = categorias.findById(producto.getCategoria().getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("La categoría seleccionada no existe."));
        producto.setCategoria(categoria);
        if (producto.getIdProducto() != null) {
            Producto anterior = productos.findById(producto.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("El producto no existe."));
            producto.setRutaImagen(anterior.getRutaImagen());
        }
        String rutaNueva = imagenes.guardar(imagenFile);
        if (rutaNueva != null) producto.setRutaImagen(rutaNueva);
        productos.save(producto);
    }

    @Transactional
    public void delete(Integer id) {
        if (!productos.existsById(id)) throw new IllegalArgumentException("El producto no existe.");
        try {
            productos.deleteById(id);
            productos.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("No se puede eliminar el producto porque tiene datos asociados.", ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaDerivada(BigDecimal inf, BigDecimal sup) {
        return productos.findByPrecioBetweenOrderByPrecioAsc(inf, sup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaJPQL(BigDecimal inf, BigDecimal sup) {
        return productos.consultaJPQL(inf, sup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaSQL(BigDecimal inf, BigDecimal sup) {
        return productos.consultaSQL(inf, sup);
    }
}
