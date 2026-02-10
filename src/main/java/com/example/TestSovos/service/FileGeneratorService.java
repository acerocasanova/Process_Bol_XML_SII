package com.example.TestSovos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.TestSovos.model.BoletaInfo;

@Service
public class FileGeneratorService {

    public String generarReporte(List<BoletaInfo> boletas) {
        StringBuilder contenido = new StringBuilder();
        
        // Encabezado
        contenido.append("═══════════════════════════════════════════════════════════════════\n");
        contenido.append("                    REPORTE DE BOLETAS\n");
        contenido.append("═══════════════════════════════════════════════════════════════════\n\n");
        
        // Encabezado de columnas
        contenido.append(String.format("%-30s | %-25s | %-30s | %12s\n", 
            "RAZÓN SOCIAL", "NOMBRE", "EMAIL", "TOTAL"));
        contenido.append("─────────────────────────────────────────────────────────────────────\n");
        
        // Datos
        double totalGeneral = 0;
        for (BoletaInfo boleta : boletas) {
            contenido.append(String.format("%-30s | %-25s | %-30s | $ %10.2f\n",
                truncate(boleta.getRazonSocial(), 30),
                truncate(boleta.getNombre(), 25),
                truncate(boleta.getEmail(), 30),
                boleta.getTotal()));
            totalGeneral += boleta.getTotal();
        }
        
        // Pie
        contenido.append("═════════════════════════════════════════════════════════════════════\n");
        contenido.append(String.format("%-30s | %-25s | %-30s | $ %10.2f\n", 
            "", "", "TOTAL GENERAL", totalGeneral));
        contenido.append("═════════════════════════════════════════════════════════════════════\n");
        
        return contenido.toString();
    }

    public String generarCSV(List<BoletaInfo> boletas) {
        StringBuilder contenido = new StringBuilder();
        
        // Encabezado CSV
        contenido.append("RAZÓN SOCIAL;NOMBRE;EMAIL;TOTAL\n");
        
        // Datos
        for (BoletaInfo boleta : boletas) {
            contenido.append(String.format("%s;%s;%s;%.2f\n",
                escaparCSV(boleta.getRazonSocial()),
                escaparCSV(boleta.getNombre()),
                escaparCSV(boleta.getEmail()),
                boleta.getTotal()));
        }
        
        return contenido.toString();
    }

    private String truncate(String valor, int longitud) {
        if (valor == null) return "";
        return valor.length() > longitud ? valor.substring(0, longitud) : valor;
    }

    private String escaparCSV(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
