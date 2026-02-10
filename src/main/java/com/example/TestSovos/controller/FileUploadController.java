package com.example.TestSovos.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.TestSovos.model.BoletaInfo;
import com.example.TestSovos.service.FileGeneratorService;
import com.example.TestSovos.service.XmlReaderService;

//import jakarta.servlet.http.HttpSession;

@Controller
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads";

    @Autowired
    private XmlReaderService xmlReaderService;

    @Autowired
    private FileGeneratorService fileGeneratorService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model, HttpSession session) {
        try {
            if (file.isEmpty()) {
                model.addAttribute("message", "Por favor selecciona un archivo");
                return "index";
            }

            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            // Guardar archivo
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // Si es XML, procesarlo con el servicio
            List<BoletaInfo> boletas = xmlReaderService.readAndLogXml(filePath);
            model.addAttribute("xmlMessage", boletas);

            // Guardar en sesión para posteriores descargas
            session.setAttribute("boletasExtraidas", boletas);

            return "index";
        } catch (IOException e) {
            model.addAttribute("message", "Error al cargar el archivo: " + e.getMessage());
            return "index";
        }
    }

    @GetMapping("/descargar/txt")
    public ResponseEntity<String> descargarTxt(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<BoletaInfo> boletas = (List<BoletaInfo>) session.getAttribute("boletasExtraidas");

        if (boletas == null || boletas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        String contenido = fileGeneratorService.generarReporte(boletas);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_boletas.txt\"");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8");

        return ResponseEntity.ok()
                .headers(headers)
                .body(contenido);
    }

    @GetMapping("/descargar/csv")
    public ResponseEntity<String> descargarCsv(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<BoletaInfo> boletas = (List<BoletaInfo>) session.getAttribute("boletasExtraidas");

        if (boletas == null || boletas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        String contenido = fileGeneratorService.generarCSV(boletas);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_boletas.csv\"");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

        return ResponseEntity.ok()
                .headers(headers)
                .body(contenido);
    }
}