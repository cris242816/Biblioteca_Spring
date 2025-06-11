package org.example.controller;

import org.example.model.Usuario;
import org.example.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/formulario";
    }

    @PostMapping("/nuevo")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "usuarios/formulario";
        }

        if (usuarioService.existsByEmail(usuario.getEmail())) {
            model.addAttribute("error", "El email ya está registrado");
            return "usuarios/formulario";
        }

        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isEmpty()) {
            return "redirect:/usuarios";
        }
        model.addAttribute("usuario", usuario.get());
        return "usuarios/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
                                    @Valid @ModelAttribute("usuario") Usuario usuario,
                                    BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "usuarios/formulario";
        }

        Optional<Usuario> usuarioExistente = usuarioService.findById(id);
        if (usuarioExistente.isEmpty()) {
            return "redirect:/usuarios";
        }

        if (!usuarioExistente.get().getEmail().equals(usuario.getEmail())) {
            if (usuarioService.existsByEmail(usuario.getEmail())) {
                model.addAttribute("error", "El email ya está registrado");
                return "usuarios/formulario";
            }
        }

        usuario.setId(id);
        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return "redirect:/usuarios";
    }
}