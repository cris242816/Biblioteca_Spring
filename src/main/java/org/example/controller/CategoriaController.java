package org.example.controller;

import org.example.model.Categoria;
import org.example.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        return "categorias/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/formulario";
    }

    @PostMapping("/nuevo")
    public String guardarCategoria(@Valid @ModelAttribute("categoria") Categoria categoria,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "categorias/formulario";
        }

        if (categoriaService.existsByNombre(categoria.getNombre())) {
            model.addAttribute("error", "La categoría ya existe");
            return "categorias/formulario";
        }

        categoriaService.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Categoria> categoria = categoriaService.findById(id);
        if (categoria.isEmpty()) {
            return "redirect:/categorias";
        }
        model.addAttribute("categoria", categoria.get());
        return "categorias/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarCategoria(@PathVariable Long id,
                                      @Valid @ModelAttribute("categoria") Categoria categoria,
                                      BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "categorias/formulario";
        }

        Optional<Categoria> categoriaExistente = categoriaService.findById(id);
        if (categoriaExistente.isEmpty()) {
            return "redirect:/categorias";
        }

        if (!categoriaExistente.get().getNombre().equals(categoria.getNombre())) {
            if (categoriaService.existsByNombre(categoria.getNombre())) {
                model.addAttribute("error", "La categoría ya existe");
                return "categorias/formulario";
            }
        }

        categoria.setId(id);
        categoriaService.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaService.deleteById(id);
        return "redirect:/categorias";
    }
}