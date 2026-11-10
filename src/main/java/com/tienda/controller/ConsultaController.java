package com.tienda.controller;

import com.tienda.domain.Producto;
import com.tienda.service.ProductoService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {
    private final ProductoService productos;

    public ConsultaController(ProductoService productos) {
        this.productos = productos;
    }

    @ModelAttribute("modos")
    public List<String> modos() { return List.of("derivada", "jpql", "sql"); }

    @GetMapping("/listado")
    public String listado(Model model) {
        model.addAttribute("productos", productos.getProductos(false));
        return "consultas/listado";
    }

    @PostMapping({"/consultaDerivada", "/consultaJPQL", "/consultaSQL"})
    public String consultar(@RequestParam BigDecimal precioInf,
            @RequestParam BigDecimal precioSup, jakarta.servlet.http.HttpServletRequest request,
            Model model) {
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        if (precioInf.compareTo(BigDecimal.ZERO) < 0 || precioSup.compareTo(precioInf) < 0) {
            model.addAttribute("error", "Ingresá un rango válido: 0 ≤ precio inferior ≤ precio superior.");
            model.addAttribute("productos", List.<Producto>of());
            return "consultas/listado";
        }
        String ruta = request.getServletPath();
        List<Producto> resultado;
        if (ruta.endsWith("consultaJPQL")) {
            resultado = productos.consultaJPQL(precioInf, precioSup);
            model.addAttribute("tipoConsulta", "JPQL");
        } else if (ruta.endsWith("consultaSQL")) {
            resultado = productos.consultaSQL(precioInf, precioSup);
            model.addAttribute("tipoConsulta", "SQL nativa");
        } else {
            resultado = productos.consultaDerivada(precioInf, precioSup);
            model.addAttribute("tipoConsulta", "Derivada");
        }
        model.addAttribute("productos", resultado);
        return "consultas/listado";
    }
}
