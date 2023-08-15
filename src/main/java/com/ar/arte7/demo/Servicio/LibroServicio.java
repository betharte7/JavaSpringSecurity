package com.ar.arte7.demo.Servicio;

import com.ar.arte7.demo.entidades.Autor;
import com.ar.arte7.demo.entidades.Editorial;
import com.ar.arte7.demo.entidades.Libro;
import com.ar.arte7.demo.respositorios.AutorRepositorio;
import com.ar.arte7.demo.respositorios.EditorialRepositorio;
import com.ar.arte7.demo.respositorios.LibroRepositorio;
import com.ar.arte7.demo.excepciones.MiException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {

        validar(isbn, titulo, ejemplares, idAutor, idEditorial);

        Autor autor = autorRepositorio.findById(idAutor).get();

        Editorial editorial = editorialRepositorio.findById(idEditorial).get();
        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());

        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libroRepositorio.save(libro);

    }

    public List<Libro> listarLibros() {
        List<Libro> libros = new ArrayList();

        libros = libroRepositorio.findAll();

        return libros;
    }

    public void modificarLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {

        validar(isbn, titulo, ejemplares, idAutor, idEditorial);

        Optional<Libro> respuesta = libroRepositorio.findById(isbn);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);

        Autor autor = new Autor();
        Editorial editorial = new Editorial();

        if (respuestaAutor.isPresent()) {

            autor = respuestaAutor.get();

        }

        if (respuestaEditorial.isPresent()) {

            editorial = respuestaEditorial.get();
        }

        if (respuesta.isPresent()) {

            Libro libro = respuesta.get();
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            libro.setAutor(autor);
            libro.setEditorial(editorial);

            libroRepositorio.save(libro);

        }

    }

    public void eliminarLibro(Long isbn, RedirectAttributes redirectAttributes) throws MiException {

        Optional<Libro> respuesta = libroRepositorio.findById(isbn);
        try {
            if (respuesta.isPresent()) {
                Libro libro = respuesta.get();

                libroRepositorio.delete(libro);
                redirectAttributes.addFlashAttribute("exito", "El libro fue eliminado exitosamente!");

            } else {
                throw new MiException("El libro no pudo ser eliminado.");
            }

        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            System.out.println("El libro no pudo ser eliminado.");
        }
    }

    public Libro getOne(Long isbn) {
        return libroRepositorio.getOne(isbn);
    }

    public void validar(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {

        if (isbn == null) {
            throw new MiException("El isbn no puede ser nulo");
        }
        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("El título no puede estar vacío");
        }
        if (ejemplares == null) {
            throw new MiException("Los ejemplares no pueden estar vacíos");
        }
        if (idAutor == null || idAutor.isEmpty()) {
            throw new MiException("El autor no puede estar vacío");
        }
        if (idEditorial == null || idEditorial.isEmpty()) {
            throw new MiException("La editorial no puede estar vacío");
        }

    }

}
