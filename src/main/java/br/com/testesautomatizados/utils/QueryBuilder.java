/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.testesautomatizados.utils;

import br.com.testesautomatizados.entity.Planet;
import br.com.testesautomatizados.jacoco.ExcludeFromJacocoGeneratedReport;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

/**
 *
 * @author Pedro
 */
public class QueryBuilder {
    private QueryBuilder() {
        
    }
    
    public static Example<Planet> makeQuery(Planet planet) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
        return Example.of(planet, exampleMatcher);
    }
}
