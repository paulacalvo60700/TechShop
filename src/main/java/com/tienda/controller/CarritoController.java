package com.tienda.controller;

import com.tienda.domain.Producto;
import com.tienda.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {
    private final ProductoService productos;

    public CarritoController(ProductoService productos) { this.productos = productos; }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> obtenerCarrito(HttpSession session) {
        Object actual = session.getAttribute("carrito");
        if (actual instanceof Map<?, ?>) return (Map<Integer, Integer>) actual;
        Map<Integer, Integer> nuevo = new LinkedHashMap<>();
        session.setAttribute("carrito", nuevo);
        return nuevo;
    }

    @GetMapping({"", "/"})
    public String ver(HttpSession session, Model model) {
        List<Map<String, Object>> lineas = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        Map<Integer, Integer> carrito = obtenerCarrito(session);
        for (var entrada : carrito.entrySet()) {
            var opt = productos.getProducto(entrada.getKey());
            if (opt.isEmpty()) continue;
            Producto p = opt.get();
            int cantidad = Math.min(entrada.getValue(), Math.max(0, p.getExistencias()));
            if (cantidad == 0) continue;
            BigDecimal subtotal = p.getPrecio().multiply(BigDecimal.valueOf(cantidad));
            Map<String, Object> linea = new LinkedHashMap<>();
            linea.put("p", p);
            linea.put("cantidad", cantidad);
            linea.put("subtotal", subtotal);
            lineas.add(linea);
            total = total.add(subtotal);
        }
        model.addAttribute("lineas", lineas);
        model.addAttribute("total", total);
        return "carrito/listado";
    }

    @PostMapping("/agregar")
    public String agregar(@RequestParam Integer idProducto, HttpSession session,
            RedirectAttributes flash) {
        var item = productos.getProducto(idProducto);
        if (item.isEmpty() || !item.get().isActivo() || item.get().getExistencias() <= 0) {
            flash.addFlashAttribute("error", "Ese producto ya no está disponible.");
            return "redirect:/";
        }
        var carrito = obtenerCarrito(session);
        int anterior = carrito.getOrDefault(idProducto, 0);
        if (anterior >= item.get().getExistencias()) {
            flash.addFlashAttribute("error", "No hay más existencias de este producto.");
        } else {
            carrito.put(idProducto, anterior + 1);
            flash.addFlashAttribute("todoOk", "Agregado al carrito de demostración.");
        }
        return "redirect:/carrito";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idProducto, HttpSession session) {
        obtenerCarrito(session).remove(idProducto);
        return "redirect:/carrito";
    }

    @PostMapping("/confirmar")
    public String confirmar(HttpSession session, RedirectAttributes flash) {
        if (obtenerCarrito(session).isEmpty()) {
            flash.addFlashAttribute("error", "El carrito está vacío.");
        } else {
            obtenerCarrito(session).clear();
            flash.addFlashAttribute("todoOk", "Pedido simulado con éxito. No se procesó ningún pago ni se descontó inventario.");
        }
        return "redirect:/carrito";
    }
}
