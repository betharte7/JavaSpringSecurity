package com.ar.arte7.demo.controladores;

import com.ar.arte7.demo.Servicio.UsuarioServicio;
import com.ar.arte7.demo.entidades.Usuario;
import com.ar.arte7.demo.excepciones.MiException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class adminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "panel.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);

        return "usuario_list";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id) {
        usuarioServicio.cambiarRol(id);

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuario/modificar/{id}")
    public String modificarUsuario(@PathVariable String id, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(id));

        return "usuario_modificar.html";
    }

    @PostMapping("/usuario/modificar/{id}")
    public String modificar(MultipartFile archivo, @PathVariable String idUsuario, String nombre, String email, String password, String password2, ModelMap modelo) {
        try {
            usuarioServicio.actualizar(archivo, idUsuario, nombre, email, password, password2);
            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "usuario_modificar.html";
        }

    }

    @GetMapping("/usuario/borrar/{id}")
    public String borrar(@PathVariable String id, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(id));

        return "usuario_borrar.html";
    }

    @PostMapping("/usuario/borrar/{id}")
    public String borrarUsuario(@PathVariable String id, ModelMap modelo) {
        try {
            usuarioServicio.deleteUserById(id);
            modelo.put("exito", "Usuario eliminado correctamente!");
            List<Usuario> usuarios = usuarioServicio.listarUsuarios();
            modelo.addAttribute("usuarios", usuarios);
            return "usuario_list.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            List<Usuario> usuarios = usuarioServicio.listarUsuarios();
            modelo.addAttribute("usuarios", usuarios);
            return "usuario_list.html";
        }

    }

}
