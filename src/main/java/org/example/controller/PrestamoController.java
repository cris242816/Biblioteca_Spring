package org.example.controller;

import org.example.model.*;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {
    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private RevistaService revistaService;

    @Autowired
    private DVDService dvdService;

    @GetMapping
    public String listarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoService.findAll());
        return "prestamos/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        List<Libro> libros = libroService.buscarDisponibles();
        List<Revista> revistas = revistaService.buscarDisponibles();
        List<DVD> dvds = dvdService.buscarDisponibles();

        model.addAttribute("prestamo", new Prestamo());
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("libros", libros);
        model.addAttribute("revistas", revistas);
        model.addAttribute("dvds", dvds);
        return "prestamos/formulario";
    }

    @PostMapping("/nuevo")
    public String realizarPrestamo(@ModelAttribute("prestamo") Prestamo prestamo,
                                   @RequestParam(required = false) Long libroId,
                                   @RequestParam(required = false) Long revistaId,
                                   @RequestParam(required = false) Long dvdId,
                                   Model model) {
        try {
            prestamoService.realizarPrestamo(prestamo.getUsuario().getId(), libroId, revistaId, dvdId);
            return "redirect:/prestamos";
        } catch (RuntimeException e) {
            List<Usuario> usuarios = usuarioService.findAll();
            List<Libro> libros = libroService.buscarDisponibles();
            List<Revista> revistas = revistaService.buscarDisponibles();
            List<DVD> dvds = dvdService.buscarDisponibles();

            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("libros", libros);
            model.addAttribute("revistas", revistas);
            model.addAttribute("dvds", dvds);
            return "prestamos/formulario";
        }
    }

    @GetMapping("/devolver/{id}")
    public String devolverPrestamo(@PathVariable Long id) {
        prestamoService.devolverPrestamo(id);
        return "redirect:/prestamos";
    }

    @GetMapping("/usuario/{usuarioId}")
    public String listarPrestamosPorUsuario(@PathVariable Long usuarioId, Model model) {
        List<Prestamo> prestamos = prestamoService.findByUsuarioId(usuarioId);
        model.addAttribute("prestamos", prestamos);
        return "prestamos/listar";
    }

    @GetMapping("/activos")
    public String listarPrestamosActivos(Model model) {
        List<Prestamo> prestamos = prestamoService.findByDevuelto(false);
        model.addAttribute("prestamos", prestamos);
        model.addAttribute("busqueda", "Préstamos activos");
        return "prestamos/listar";
    }
}