package com.ar.arte7.demo.respositorios;

import com.ar.arte7.demo.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Long> {
    
    
    @Query("SELECT l FROM Libro l WHERE l.autor.id = :autor_id")
    public List<Libro> buscarPorAutorLibro(@Param("autor_id")String autor_id);
    
    @Query("SELECT l FROM Libro l WHERE l.editorial.id = :editorial_id")
    public List<Libro> buscarPorEditorialLibro(@Param("editorial_id")String editorial_id);
    
}
