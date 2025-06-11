package org.example.controller;

import org.example.model.DVD;
import org.example.service.DVDService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List; // ← ESTA LÍNEA ES LA CLAVE

@Controller
@RequestMapping("/dvds")
public class DVDController {
    @Autowired
    private DVDService dvdService;

    @GetMapping
    public String listarDVDs(Model model) {
        model.addAttribute("dvds", dvdService.findAll());
        return "dvds/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("dvd", new DVD());
        return "dvds/formulario";
    }

    @PostMapping("/nuevo")
    public String guardarDVD(@Valid @ModelAttribute("dvd") DVD dvd,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "dvds/formulario";
        }

        dvd.setDisponible(true);
        dvdService.save(dvd);
        return "redirect:/dvds";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<DVD> dvd = dvdService.findById(id);
        if (dvd.isEmpty()) {
            return "redirect:/dvds";
        }
        model.addAttribute("dvd", dvd.get());
        return "dvds/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarDVD(@PathVariable Long id,
                                @Valid @ModelAttribute("dvd") DVD dvd,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "dvds/formulario";
        }

        Optional<DVD> dvdExistente = dvdService.findById(id);
        if (dvdExistente.isEmpty()) {
            return "redirect:/dvds";
        }

        dvd.setId(id);
        dvd.setDisponible(dvdExistente.get().isDisponible());
        dvdService.save(dvd);
        return "redirect:/dvds";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDVD(@PathVariable Long id) {
        dvdService.deleteById(id);
        return "redirect:/dvds";
    }

    @GetMapping("/buscar")
    public String buscarDVDs(@RequestParam(required = false) String titulo,
                             @RequestParam(required = false) String director,
                             Model model) {
        if (titulo != null && !titulo.isEmpty()) {
            List<DVD> dvds = dvdService.buscarPorTitulo(titulo);
            model.addAttribute("dvds", dvds);
            model.addAttribute("busqueda", "Título: " + titulo);
        } else if (director != null && !director.isEmpty()) {
            List<DVD> dvds = dvdService.buscarPorDirector(director);
            model.addAttribute("dvds", dvds);
            model.addAttribute("busqueda", "Director: " + director);
        } else {
            return "redirect:/dvds";
        }
        return "dvds/listar";
    }

    @GetMapping("/disponibles")
    public String listarDisponibles(Model model) {
        List<DVD> dvds = dvdService.buscarDisponibles();
        model.addAttribute("dvds", dvds);
        model.addAttribute("busqueda", "DVDs disponibles");
        return "dvds/listar";
    }
}