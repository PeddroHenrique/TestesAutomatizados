/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.testesautomatizados.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.*;
import static br.com.testesautomatizados.constants.PlanetConstants.PLANET;
import static br.com.testesautomatizados.constants.PlanetConstants.PLANETS;
import static br.com.testesautomatizados.constants.PlanetConstants.TATOOINE;
import static org.hamcrest.Matchers.hasSize;
import br.com.testesautomatizados.entity.Planet;

import br.com.testesautomatizados.service.impl.PlanetServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 *
 * @author Pedro
 */
@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlanetServiceImpl planetServiceImpl;

    //operação_estado_retorno
    @Test
    public void createPlanet_withValidData_ReturnCreated() throws Exception {
        //Arrange
        when(planetServiceImpl.create(PLANET)).thenReturn(PLANET);

        //Act e Assert
        mockMvc
                .perform(
                        post("/planets").content(objectMapper.writeValueAsString(PLANET))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception {
        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        mockMvc
                .perform(
                        post("/planets").content(objectMapper.writeValueAsString(emptyPlanet))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
        mockMvc
                .perform(
                        post("/planets").content(objectMapper.writeValueAsString(invalidPlanet))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnsConflict() throws Exception {
        when(planetServiceImpl.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc
                .perform(
                        post("/planets").content(objectMapper.writeValueAsString(PLANET))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() throws Exception {
        when(planetServiceImpl.getById(1L)).thenReturn(Optional.of(PLANET));

        mockMvc
                .perform(
                        get("/planets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsNotFound() throws Exception {
        mockMvc
                .perform(
                        get("/planets/99"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
        when(planetServiceImpl.getByName(PLANET.getName())).thenReturn(Optional.of(PLANET));
        
        mockMvc
                .perform(
                        get("/planets/name/" + PLANET.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }
    
    @Test
    public void getPlanet_ByUnexistingName_ReturnsNotFound() throws Exception {
        mockMvc
                .perform(
                        get("/planets/name/" + PLANET.getName()))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void listPlanets_ReturnFilteredPlanets() throws Exception {
        when(planetServiceImpl.getAll(null, null)).thenReturn(PLANETS);
        when(planetServiceImpl.getAll(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));
        
        mockMvc
                .perform(
                        get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
        
        mockMvc
                .perform(
                        get("/planets?" + String.format("terrain=%s&climate=%s", TATOOINE.getTerrain(), TATOOINE.getClimate())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(TATOOINE));
    }
    
    @Test
    public void listPlanets_ReturnNoPlanets() throws Exception {
        when(planetServiceImpl.getAll(null, null)).thenReturn(Collections.emptyList());
        
        mockMvc
                .perform(
                        get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void removePlanet_WithExistingId_ReturnsNoContent() throws Exception {
        Long planetId = 1L;
        
        doNothing().when(planetServiceImpl).delete(planetId);
        
        mockMvc
                .perform(
                        delete("/planets/1"))
                .andExpect(status().isNoContent());
        
        verify(planetServiceImpl, times(1)).delete(planetId);
    }
    
    @Test
    public void removePlanet_withUnexistingId_ReturnsNotFound() throws Exception {
        final Long planetId = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(planetServiceImpl).delete(planetId);
        
        mockMvc
                .perform(
                        delete("/planets/" + planetId))
                .andExpect(status().isNoContent());
        
        verify(planetServiceImpl, times(1)).delete(planetId);
    }
}
