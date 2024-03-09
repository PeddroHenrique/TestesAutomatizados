/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.testesautomatizados.service.impl;

import static br.com.testesautomatizados.constants.PlanetConstants.PLANET;
import static br.com.testesautomatizados.constants.PlanetConstants.INVALID_PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import br.com.testesautomatizados.entity.Planet;
import br.com.testesautomatizados.repository.PlanetRepository;
import br.com.testesautomatizados.utils.QueryBuilder;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;

/**
 *
 * @author Pedro
 */
@ExtendWith(MockitoExtension.class)
//@SpringBootTest(classes = PlanetServiceImpl.class)
public class PlanetServiceImplTest {

    //@Autowired
    @InjectMocks
    private PlanetServiceImpl planetServiceImpl;

    //@MockBean
    @Mock
    private PlanetRepository planetRepository;

    //operacao_estado_retorno
    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        //AAA
        //Arrange
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        //Act
        //sut = System Under Test
        Planet sut = planetServiceImpl.create(PLANET);

        //Assert
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> planetServiceImpl.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        when(planetRepository.findById(1L)).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetServiceImpl.getById(1L);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsEmpty() {
        when(planetRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Planet> sut = planetServiceImpl.getById(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetServiceImpl.getByName(PLANET.getName());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_WithUnexistingName_ReturnsEmpty() {
        when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.empty());

        Optional<Planet> sut = planetServiceImpl.getByName(PLANET.getName());

        assertThat(sut).isEmpty();
    }

    @Test
    public void listPlanets_WithExistingPlanets_ReturnsAllPlanets() {
        List<Planet> planets = new ArrayList() {
            {
                add(PLANET);
            }
        };
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getTerrain(), PLANET.getClimate()));
        when(planetRepository.findAll(query)).thenReturn(planets);

        List<Planet> sut = planetServiceImpl.getAll(PLANET.getTerrain(), PLANET.getClimate());

        verify(planetRepository, times(1)).findAll(any(Example.class));

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.get(0)).isEqualTo(PLANET);
        assertThat(planets.size()).isEqualTo(sut.size());
        assertThat(planets).isEqualTo(sut);
    }

    @Test
    public void listPlanets_WithUnexistingPlanets_ReturnsNoPlanets() {
        when(planetRepository.findAll(any(Example.class))).thenReturn(Collections.emptyList());

        List<Planet> sut = planetServiceImpl.getAll(PLANET.getTerrain(), PLANET.getClimate());

        verify(planetRepository, times(1)).findAll(any(Example.class));

        assertThat(sut).isEmpty();
    }
    
    @Test
    public void removePlanet_WithExistingId_DoesNotThrowAnyException() {
        assertThatCode(() -> planetServiceImpl.delete(1L)).doesNotThrowAnyException();
    }
    
    @Test
    public void removePlanet_WithUnexistingId_ThrowsException() {
        doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);
        
        assertThatThrownBy(() -> planetServiceImpl.delete(99L)).isInstanceOf(RuntimeException.class);
    }
}
