/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.testesautomatizados.controller;

import br.com.testesautomatizados.entity.Planet;
import br.com.testesautomatizados.service.PlanetService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Pedro
 */
@RestController
@RequestMapping("/planets")
public class PlanetController {
    
    private PlanetService planetService;
    
    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }
    
    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody @Valid Planet planet) {
        Planet planetCreated = planetService.create(planet);
        return new ResponseEntity(planetCreated, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Planet> getById(@PathVariable(name = "id") Long id) {
        return planetService.getById(id).map(planet -> ResponseEntity.ok(planet))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> getByName(@PathVariable(name = "name") String name) {
        return planetService.getByName(name).map(planet -> ResponseEntity.ok(planet))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Planet>> getAll(@RequestParam(name = "terrain", required = false) String terrain,
            @RequestParam(name = "climate", required = false) String climate) {
        List<Planet> planets = planetService.getAll(terrain, climate);
        return ResponseEntity.ok(planets);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        planetService.delete(id);
        return ResponseEntity.noContent().build();
                
    }
}
