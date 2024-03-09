/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.testesautomatizados.repository;

import br.com.testesautomatizados.entity.Planet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Pedro
 */
public interface PlanetRepository extends JpaRepository<Planet, Long>{
    
    @Query("SELECT p FROM Planet p WHERE p.name = :name")
    Optional<Planet> findByName(@Param("name") String name);
}
