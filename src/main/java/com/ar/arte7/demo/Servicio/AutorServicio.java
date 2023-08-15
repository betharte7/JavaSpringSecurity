package com.ar.arte7.demo.Servicio;

import com.ar.arte7.demo.entidades.Autor;
import com.ar.arte7.demo.entidades.Libro;
import com.ar.arte7.demo.respositorios.AutorRepositorio;
import com.ar.arte7.demo.excepciones.MiException;
import com.ar.arte7.demo.respositorios.LibroRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class AutorServicio {

    @Autowired
    AutorRepositorio autorRepositorio;

    @Autowired
    LibroRepositorio libroRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws MiException {

        validar(nombre);

        Autor autor = new Autor();

        autor.setNombre(nombre);

        autorRepositorio.save(autor);
    }

    public List<Autor> listaAutores() {

        List<Autor> autores = new ArrayList();

        autores = autorRepositorio.findAll();

        return autores;
    }

    public void modificarAutor(String nombre, String id) throws MiException {

        validar(nombre);

        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();

            autor.setNombre(nombre);

            autorRepositorio.save(autor);

        }
    }

    public List<Libro> buscarLibroxIdAutor(String id) {

        List<Libro> librosFound = new ArrayList();

        librosFound = libroRepositorio.buscarPorAutorLibro(id);

        return librosFound;
    }
    
    public void eliminarAutor(String id, RedirectAttributes redirectAttributes) throws MiException {

        Optional<Autor> respuesta = autorRepositorio.findById(id);
        try {
            if (respuesta.isPresent()) {
                Autor autor = respuesta.get();

                if (buscarLibroxIdAutor(autor.getId()).isEmpty()) {

                    autorRepositorio.delete(autor);
                    redirectAttributes.addFlashAttribute("exito", "El autor fue eliminado exitosamente!");

                } else {
                    throw new MiException("El autor está asociado a un libro. No se puede borrar.");
                }

            }
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            System.out.println("El autor está asociado a un libro. No se puede borrar.");
        }
    }

//    public void eliminarAutor(String id, ModelMap modelo) throws MiException {
//
//        Optional<Autor> respuesta = autorRepositorio.findById(id);
//        try {
//            if (respuesta.isPresent()) {
//                Autor autor = respuesta.get();
//
//                if (buscarLibroxIdAutor(autor.getId()).isEmpty()) {
//
//                    autorRepositorio.delete(autor);
//                    modelo.put("exito", "El autor fue eliminado exitosamente!");
//
//                } else {
//                    throw new MiException("El autor está asociado a un libro. No se puede borrar.");
//                }
//
//            }
//        } catch (MiException ex) {
//            modelo.put("error", ex.getMessage());
//            System.out.println("El autor está asociado a un libro. No se puede borrar.");
//        }
//    }

    public Autor getOne(String id) {
        return autorRepositorio.getOne(id);
    }

    public void validar(String nombre) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede estar vacío");
        }
    }

}
