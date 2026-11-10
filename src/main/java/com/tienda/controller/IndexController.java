package com.tienda.controller;

import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {
    private final ProductoService productos;
    private final CategoriaService categorias;

    public IndexController(ProductoService productos, CategoriaService categorias) {
        this.productos = productos;
        this.categorias = categorias;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("productos", productos.getProductos(true));
        model.addAttribute("categorias", categorias.getCategorias(true));
        return "index";
    }

    @GetMapping("/consultas/{idCategoria}")
    public String porCategoria(@PathVariable Integer idCategoria, Model model) {
        model.addAttribute("productos", productos.getProductosActivosPorCategoria(idCategoria));
        model.addAttribute("categorias", categorias.getCategorias(true));
        model.addAttribute("idCategoriaActual", idCategoria);
        return "index";
    }
}
