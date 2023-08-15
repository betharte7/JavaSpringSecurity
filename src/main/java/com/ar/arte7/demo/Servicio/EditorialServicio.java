
package com.ar.arte7.demo.Servicio;

import com.ar.arte7.demo.entidades.Editorial;
import com.ar.arte7.demo.entidades.Libro;
import com.ar.arte7.demo.respositorios.EditorialRepositorio;
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
public class EditorialServicio {
    
    @Autowired
    EditorialRepositorio editorialRepositorio;
    
    @Autowired
    LibroRepositorio libroRepositorio;   
    
    @Transactional
    public void crearEditorial(String nombre) throws MiException {
        
        validar(nombre);
        
        Editorial editorial = new Editorial();
        
        editorial.setNombre(nombre);
        
        editorialRepositorio.save(editorial);
    }
    
    public List<Editorial> listaEditoriales() {
        
        List<Editorial> editoriales = new ArrayList();
        
        editoriales = editorialRepositorio.findAll();
        
        return editoriales;
           
    }
    
    public void modificarEditorial(String id, String nombre) throws MiException {
        
        validar(nombre);
        
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        
        if(respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            
            editorialRepositorio.save(editorial);
        }
    }
    
    public List<Libro> buscarPorEditorialLibro(String id) {

        List<Libro> librosFound = new ArrayList();

        librosFound = libroRepositorio.buscarPorEditorialLibro(id);

        return librosFound;
    }

    public void eliminarEditorial(String id, RedirectAttributes redirectAttributes) throws MiException {

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        try {
            if (respuesta.isPresent()) {
                Editorial editorial = respuesta.get();

                if (buscarPorEditorialLibro(editorial.getId()).isEmpty()) {

                    editorialRepositorio.delete(editorial);
                    redirectAttributes.addFlashAttribute("exito", "La editorial fue eliminada exitosamente!");

                } else {
                    throw new MiException("La editorial está asociada a un libro. No se puede borrar.");
                    
                }

            }
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            System.out.println("La editorial está asociada a un libro. No se puede borrar.");
        }
    }
    
    public Editorial getOne(String id) {
        return editorialRepositorio.getOne(id);
    }
    
    public void validar(String nombre) throws MiException {
        
        if(nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede estar vacío");
        }
    }
    
}
