package org.example.controller;

import org.example.model.Revista;
import org.example.service.RevistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List; // ← ESTA LÍNEA ES LA CLAVE
import java.util.Optional;

@Controller
@RequestMapping("/revistas")
public class RevistaController {
    @Autowired
    private RevistaService revistaService;

    @GetMapping
    public String listarRevistas(Model model) {
        model.addAttribute("revistas", revistaService.findAll());
        return "revistas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("revista", new Revista());
        return "revistas/formulario";
    }

    @PostMapping("/nuevo")
    public String guardarRevista(@Valid @ModelAttribute("revista") Revista revista,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "revistas/formulario";
        }

        if (revistaService.existsByIssn(revista.getIssn())) {
            model.addAttribute("error", "El ISSN ya está registrado");
            return "revistas/formulario";
        }

        revista.setDisponible(true);
        revistaService.save(revista);
        return "redirect:/revistas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Revista> revista = revistaService.findById(id);
        if (revista.isEmpty()) {
            return "redirect:/revistas";
        }
        model.addAttribute("revista", revista.get());
        return "revistas/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarRevista(@PathVariable Long id,
                                    @Valid @ModelAttribute("revista") Revista revista,
                                    BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "revistas/formulario";
        }

        Optional<Revista> revistaExistente = revistaService.findById(id);
        if (revistaExistente.isEmpty()) {
            return "redirect:/revistas";
        }

        if (!revistaExistente.get().getIssn().equals(revista.getIssn())) {
            if (revistaService.existsByIssn(revista.getIssn())) {
                model.addAttribute("error", "El ISSN ya está registrado");
                return "revistas/formulario";
            }
        }

        revista.setId(id);
        revista.setDisponible(revistaExistente.get().isDisponible());
        revistaService.save(revista);
        return "redirect:/revistas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRevista(@PathVariable Long id) {
        revistaService.deleteById(id);
        return "redirect:/revistas";
    }

    @GetMapping("/buscar")
    public String buscarRevistas(@RequestParam String titulo, Model model) {
        List<Revista> revistas = revistaService.buscarPorTitulo(titulo);
        model.addAttribute("revistas", revistas);
        model.addAttribute("busqueda", "Título: " + titulo);
        return "revistas/listar";
    }

    @GetMapping("/disponibles")
    public String listarDisponibles(Model model) {
        List<Revista> revistas = revistaService.buscarDisponibles();
        model.addAttribute("revistas", revistas);
        model.addAttribute("busqueda", "Revistas disponibles");
        return "revistas/listar";
    }
}
