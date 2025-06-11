package org.example.controller;

import org.example.model.Categoria;
import org.example.model.Libro;
import org.example.service.CategoriaService;
import org.example.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/libros")
public class LibroController {
    @Autowired
    private LibroService libroService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listarLibros(Model model) {
        List<Libro> libros = libroService.findAll();
        model.addAttribute("libros", libros);
        return "libros/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        List<Categoria> categorias = categoriaService.findAll();
        model.addAttribute("libro", new Libro());
        model.addAttribute("categorias", categorias);
        return "libros/formulario";
    }

    @PostMapping("/nuevo")
    public String guardarLibro(@Valid @ModelAttribute("libro") Libro libro,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Categoria> categorias = categoriaService.findAll();
            model.addAttribute("categorias", categorias);
            return "libros/formulario";
        }

        if (libroService.existsByIsbn(libro.getIsbn())) {
            List<Categoria> categorias = categoriaService.findAll();
            model.addAttribute("categorias", categorias);
            model.addAttribute("error", "El ISBN ya está registrado");
            return "libros/formulario";
        }

        libro.setDisponible(true);
        libroService.save(libro);
        return "redirect:/libros";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Libro> libro = libroService.findById(id);
        if (libro.isEmpty()) {
            return "redirect:/libros";
        }
        List<Categoria> categorias = categoriaService.findAll();
        model.addAttribute("libro", libro.get());
        model.addAttribute("categorias", categorias);
        return "libros/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarLibro(@PathVariable Long id,
                                  @Valid @ModelAttribute("libro") Libro libro,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Categoria> categorias = categoriaService.findAll();
            model.addAttribute("categorias", categorias);
            return "libros/formulario";
        }

        Optional<Libro> libroExistente = libroService.findById(id);
        if (libroExistente.isEmpty()) {
            return "redirect:/libros";
        }

        if (!libroExistente.get().getIsbn().equals(libro.getIsbn())) {
            if (libroService.existsByIsbn(libro.getIsbn())) {
                List<Categoria> categorias = categoriaService.findAll();
                model.addAttribute("categorias", categorias);
                model.addAttribute("error", "El ISBN ya está registrado");
                return "libros/formulario";
            }
        }

        libro.setId(id);
        libro.setDisponible(libroExistente.get().isDisponible());
        libroService.save(libro);
        return "redirect:/libros";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable Long id) {
        libroService.deleteById(id);
        return "redirect:/libros";
    }

    @GetMapping("/buscar")
    public String buscarLibros(@RequestParam(required = false) String titulo,
                               @RequestParam(required = false) String autor,
                               Model model) {
        if (titulo != null && !titulo.isEmpty()) {
            List<Libro> libros = libroService.buscarPorTitulo(titulo);
            model.addAttribute("libros", libros);
            model.addAttribute("busqueda", "Título: " + titulo);
        } else if (autor != null && !autor.isEmpty()) {
            List<Libro> libros = libroService.buscarPorAutor(autor);
            model.addAttribute("libros", libros);
            model.addAttribute("busqueda", "Autor: " + autor);
        } else {
            return "redirect:/libros";
        }
        return "libros/listar";
    }

    @GetMapping("/disponibles")
    public String listarDisponibles(Model model) {
        List<Libro> libros = libroService.buscarDisponibles();
        model.addAttribute("libros", libros);
        model.addAttribute("busqueda", "Libros disponibles");
        return "libros/listar";
    }
}