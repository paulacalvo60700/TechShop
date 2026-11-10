package com.tienda.service;

import com.tienda.domain.Categoria;
import com.tienda.repository.CategoriaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {
    private final CategoriaRepository categorias;
    private final LocalImageService imagenes;

    public CategoriaService(CategoriaRepository categorias, LocalImageService imagenes) {
        this.categorias = categorias;
        this.imagenes = imagenes;
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean soloActivas) {
        return soloActivas ? categorias.findByActivoTrue() : categorias.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer id) {
        return categorias.findById(id);
    }

    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile) {
        if (categoria.getIdCategoria() != null) {
            Categoria anterior = categorias.findById(categoria.getIdCategoria())
                    .orElseThrow(() -> new IllegalArgumentException("La categoría no existe."));
            categoria.setRutaImagen(anterior.getRutaImagen());
        }
        String rutaNueva = imagenes.guardar(imagenFile);
        if (rutaNueva != null) categoria.setRutaImagen(rutaNueva);
        categorias.save(categoria);
    }

    @Transactional
    public void delete(Integer id) {
        if (!categorias.existsById(id)) throw new IllegalArgumentException("La categoría no existe.");
        try {
            categorias.deleteById(id);
            categorias.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("No se puede eliminar la categoría porque tiene productos asociados.", ex);
        }
    }
}
