package com.tienda.controller;

import com.tienda.domain.Producto;
import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/producto")
public class ProductoController {
    private final ProductoService productos;
    private final CategoriaService categorias;

    public ProductoController(ProductoService productos, CategoriaService categorias) {
        this.productos = productos;
        this.categorias = categorias;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var lista = productos.getProductos(false);
        model.addAttribute("productos", lista);
        model.addAttribute("totalProductos", lista.size());
        return "producto/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Producto producto = new Producto();
        producto.setCategoria(new com.tienda.domain.Categoria());
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categorias.getCategorias(true));
        return "producto/formulario";
    }

    @GetMapping("/modificar/{idProducto}")
    public String modificar(@PathVariable Integer idProducto, Model model, RedirectAttributes flash) {
        var item = productos.getProducto(idProducto);
        if (item.isEmpty()) {
            flash.addFlashAttribute("error", "El producto no existe.");
            return "redirect:/producto/listado";
        }
        model.addAttribute("producto", item.get());
        model.addAttribute("categorias", categorias.getCategorias(false));
        return "producto/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto, BindingResult errores,
            @RequestParam(value="imagenFile", required=false) MultipartFile imagen, Model model,
            RedirectAttributes flash) {
        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() == null) {
            errores.rejectValue("categoria", "obligatorio", "Seleccioná una categoría.");
        }
        if (errores.hasErrors()) {
            model.addAttribute("categorias", categorias.getCategorias(false));
            return "producto/formulario";
        }
        try {
            productos.save(producto, imagen);
            flash.addFlashAttribute("todoOk", "Producto guardado correctamente.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            flash.addFlashAttribute("error", "No se pudo guardar el producto.");
        }
        return "redirect:/producto/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idProducto, RedirectAttributes flash) {
        try {
            productos.delete(idProducto);
            flash.addFlashAttribute("todoOk", "Producto eliminado correctamente.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/producto/listado";
    }
}
