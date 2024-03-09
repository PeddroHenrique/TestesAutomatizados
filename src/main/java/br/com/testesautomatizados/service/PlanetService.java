/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.testesautomatizados.service;

import br.com.testesautomatizados.entity.Planet;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Pedro
 */
public interface PlanetService {
    Planet create(Planet planet);
    Optional<Planet> getById(Long id);
    Optional<Planet> getByName(String name);
    List<Planet> getAll(String terrain,  String climate);
    void delete(Long id);
}
