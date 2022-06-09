package edu.pucp.gtics.lab5_gtics_20221.repository;

import edu.pucp.gtics.lab5_gtics_20221.entity.JuegosxUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface JuegosxUsuarioRepository extends JpaRepository<JuegosxUsuario,Integer> {
    @Query(value = "Select * from juegosxusuario ju " +
            "where ju.idjuego=?1 and ju.idusuario= ?2",nativeQuery = true)
    JuegosxUsuario obtenerJuegoPorUser(int idjuego,int idusuario);
}
