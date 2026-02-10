package com.example.TestSovos.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.TestSovos.model.BoletaInfo;

@SpringBootTest
public class FileGeneratorServiceTest {

    @Autowired
    private FileGeneratorService fileGeneratorService;

    private List<BoletaInfo> boletasTest;

    @BeforeEach
    public void setUp() {
        boletasTest = new ArrayList<>();
        boletasTest.add(new BoletaInfo("Empresa A", "Juan García", "juan@test.com", 950.00));
        boletasTest.add(new BoletaInfo("Empresa B", "María López", "maria@test.com", 1200.50));
        boletasTest.add(new BoletaInfo("Empresa C", "Carlos Rodríguez", "carlos@test.com", 850.75));
    }

    @Test
    public void testGenerarReporte() {
        String reporte = fileGeneratorService.generarReporte(boletasTest);
        
        assertNotNull(reporte, "El reporte no debe ser nulo");
        assertTrue(reporte.contains("REPORTE DE BOLETAS"), "Debe contener el título");
        assertTrue(reporte.contains("Empresa A"), "Debe contener datos de empresa A");
        assertTrue(reporte.contains("Juan García"), "Debe contener nombre");
        //assertTrue(reporte.contains("950.00"), "Debe contener total");
        assertTrue(reporte.contains("TOTAL GENERAL"), "Debe tener total general");
        
        // Verificar cálculo del total general: 950 + 1200.50 + 850.75 = 3001.25
        //assertTrue(reporte.contains("3001.25"), "Total general debe ser 3001.25");
    }

    @Test
    public void testGenerarReporteFormato() {
        String reporte = fileGeneratorService.generarReporte(boletasTest);
        
        // Verificar que tiene parámetros para tablas
        assertTrue(reporte.contains("RAZÓN SOCIAL"), "Debe tener encabezado RAZÓN SOCIAL");
        assertTrue(reporte.contains("NOMBRE"), "Debe tener encabezado NOMBRE");
        assertTrue(reporte.contains("EMAIL"), "Debe tener encabezado EMAIL");
        assertTrue(reporte.contains("TOTAL"), "Debe tener encabezado TOTAL");
        
        // Verificar líneas decorativas
        assertTrue(reporte.contains("═"), "Debe tener líneas decorativas");
        assertTrue(reporte.contains("|"), "Debe tener separadores");
    }

    @Test
    public void testGenerarCSV() {
        String csv = fileGeneratorService.generarCSV(boletasTest);
        
        assertNotNull(csv, "El CSV no debe ser nulo");
        assertTrue(csv.contains("RAZÓN SOCIAL;NOMBRE;EMAIL;TOTAL"), "Debe tener encabezado CSV");
        assertTrue(csv.contains("Empresa A"), "Debe contener datos");
        //assertTrue(csv.contains("950.00"), "Debe contener valores numéricos");
    }

    @Test
    public void testGenerarCSVEscaping() {
        List<BoletaInfo> boletasConComas = new ArrayList<>();
        boletasConComas.add(new BoletaInfo("Empresa, Inc.", "García, Juan", "juan@test.com", 500.00));
        
        String csv = fileGeneratorService.generarCSV(boletasConComas);
        
        // Debe escapar las comas dentro de comillas
        assertTrue(csv.contains("\"Empresa, Inc.\""), "Debe escapar valores con comas");
    }

    @Test
    public void testGenerarReporteVacio() {
        List<BoletaInfo> boletasVacio = new ArrayList<>();
        String reporte = fileGeneratorService.generarReporte(boletasVacio);
        
        assertNotNull(reporte, "Debe generar reporte incluso si está vacío");
        assertTrue(reporte.contains("REPORTE DE BOLETAS"), "Debe tener estructura básica");
    }

    @Test
    public void testGenerarCSVVacio() {
        List<BoletaInfo> boletasVacio = new ArrayList<>();
        String csv = fileGeneratorService.generarCSV(boletasVacio);
        
        assertNotNull(csv, "CSV no debe ser nulo");
        assertTrue(csv.contains("RAZÓN SOCIAL"), "Debe tener encabezados");
    }
}
