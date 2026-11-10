package com.tienda.controller;

import com.tienda.domain.Categoria;
import com.tienda.service.CategoriaService;
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
@RequestMapping("/categoria")
public class CategoriaController {
    private final CategoriaService categorias;

    public CategoriaController(CategoriaService categorias) {
        this.categorias = categorias;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var lista = categorias.getCategorias(false);
        model.addAttribute("categorias", lista);
        model.addAttribute("totalCategorias", lista.size());
        return "categoria/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria/formulario";
    }

    @GetMapping("/modificar/{idCategoria}")
    public String modificar(@PathVariable Integer idCategoria, Model model, RedirectAttributes flash) {
        var item = categorias.getCategoria(idCategoria);
        if (item.isEmpty()) {
            flash.addFlashAttribute("error", "La categoría no existe.");
            return "redirect:/categoria/listado";
        }
        model.addAttribute("categoria", item.get());
        return "categoria/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult errores,
            @RequestParam(value="imagenFile", required=false) MultipartFile imagen, RedirectAttributes flash) {
        if (errores.hasErrors()) return "categoria/formulario";
        try {
            categorias.save(categoria, imagen);
            flash.addFlashAttribute("todoOk", "Categoría guardada correctamente.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            flash.addFlashAttribute("error", "No se pudo guardar la categoría.");
        }
        return "redirect:/categoria/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idCategoria, RedirectAttributes flash) {
        try {
            categorias.delete(idCategoria);
            flash.addFlashAttribute("todoOk", "Categoría eliminada correctamente.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/categoria/listado";
    }
}
