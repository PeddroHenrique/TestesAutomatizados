/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.testesautomatizados.service.impl;

import br.com.testesautomatizados.entity.Planet;
import br.com.testesautomatizados.repository.PlanetRepository;
import br.com.testesautomatizados.service.PlanetService;
import br.com.testesautomatizados.utils.QueryBuilder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pedro
 */
@Service
public class PlanetServiceImpl implements PlanetService{
    
    private PlanetRepository planetRepository;
    
    public PlanetServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }
    
    @Override
    public Planet create(Planet planet) {
        return planetRepository.save(planet);
    }

    @Override
    public Optional<Planet> getById(Long id) {
        return planetRepository.findById(id);
    }

    @Override
    public Optional<Planet> getByName(String name) {
        return planetRepository.findByName(name);
    }

    @Override
    public List<Planet> getAll(String terrain, String climate) {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(terrain, climate));
        return planetRepository.findAll(query);
    }

    @Override
    public void delete(Long id) {
        planetRepository.deleteById(id);
    }
    
}
