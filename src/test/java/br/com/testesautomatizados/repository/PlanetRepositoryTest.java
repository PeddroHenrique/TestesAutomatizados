/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.testesautomatizados.repository;

import static br.com.testesautomatizados.constants.PlanetConstants.PLANET;
import static br.com.testesautomatizados.constants.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.testesautomatizados.entity.Planet;
import br.com.testesautomatizados.utils.QueryBuilder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

/**
 *
 * @author Pedro
 */
@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public void afterEach() {
        PLANET.setId(null);
    }

    //operação_estado_retorno
    //AAA = arrange, act, assert
    @Test
    public void createPlanet_WithValidData_ReturnPlanet() {
        Planet planet = planetRepository.save(PLANET);

        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(planet.getName());
        assertThat(sut.getClimate()).isEqualTo(planet.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(planet.getTerrain());
    }

    @ParameterizedTest
    @MethodSource("providesInvalidPlanets")
    public void createPlanet_WithInvalidData_ThrowsException(Planet planet) {
        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    private static Stream<Arguments> providesInvalidPlanets() {
        return Stream.of(
                Arguments.of(new Planet(null, "climate", "terrain")),
                Arguments.of(new Planet("name", null, "terrain")),
                Arguments.of(new Planet("name", "climate", null)),
                Arguments.of(new Planet(null, null, "terrain")),
                Arguments.of(new Planet(null, "climate", null)),
                Arguments.of(new Planet("name", null, null)),
                Arguments.of(new Planet(null, null, null)),
                Arguments.of(new Planet("", "climate", "terrain")),
                Arguments.of(new Planet("name", "", "terrain")),
                Arguments.of(new Planet("name", "climate", "")),
                Arguments.of(new Planet("", "", "terrain")),
                Arguments.of(new Planet("", "climate", "")),
                Arguments.of(new Planet("name", "", "")),
                Arguments.of(new Planet("", "", ""))
        );
    }

    @Test
    public void createPlanet_WithExistingName_ThrowsException() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> optionalPlanet = planetRepository.findById(planet.getId());

        assertThat(optionalPlanet).isNotEmpty();
        assertThat(optionalPlanet.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnEmpty() {
        Optional<Planet> optionalPlanet = planetRepository.findById(1L);

        assertThat(optionalPlanet).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> optionalPlanet = planetRepository.findByName(planet.getName());

        assertThat(optionalPlanet).isNotEmpty();
        assertThat(optionalPlanet.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsNotFound() {
        Optional<Planet> optionalPlanet = planetRepository.findByName(PLANET.getName());

        assertThat(optionalPlanet).isEmpty();
    }

    @Test
    //adiciona 3 planetas ao repositório em tempo de execução
    @Sql(scripts = "/import_planets.sql")
    public void listPlanets_ReturnFilteredPlanets() {
        Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
        Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getTerrain(), TATOOINE.getClimate()));

        List<Planet> responseWithoutFilters = planetRepository.findAll(queryWithoutFilters);
        List<Planet> responseWithFilters = planetRepository.findAll(queryWithFilters);

        assertThat(responseWithoutFilters).isNotEmpty();
        assertThat(responseWithoutFilters).hasSize(3);
        assertThat(responseWithFilters).isNotEmpty();
        assertThat(responseWithFilters).hasSize(1);
        assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnNoPlanets() {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet());

        List<Planet> response = planetRepository.findAll(query);

        assertThat(response).isEmpty();
    }

    @Test
    public void deletePlanet_WithExistingId_RemovesPlanetFromDatabase() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        planetRepository.deleteById(planet.getId());

        Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());
        assertThat(removedPlanet).isNull();
    }

    @Test
    //adiciona 3 planetas ao repositório em tempo de execução
    public void deletePlanet_WithUnexistingId_DoesNothing() {
        Long unexistingId = 9999L;

        planetRepository.deleteById(unexistingId);

        assertThatCode(() -> planetRepository.deleteById(unexistingId)).doesNotThrowAnyException();
    }
}
