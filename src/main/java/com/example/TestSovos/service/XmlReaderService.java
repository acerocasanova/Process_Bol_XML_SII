package com.example.TestSovos.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.example.TestSovos.model.BoletaInfo;

@Service
public class XmlReaderService {

    private static final Logger logger = LoggerFactory.getLogger(XmlReaderService.class);

    public List<BoletaInfo> readAndLogXml(Path xmlFilePath) {
        List<BoletaInfo> boletas = new ArrayList<>();
        try {
            logger.info("Iniciando lectura archivo XML:", xmlFilePath.getFileName());

            // Leer el contenido del archivo
            String rawContent = new String(java.nio.file.Files.readAllBytes(xmlFilePath));

            // Limpiar espacios en blanco antes y después
            String cleanedContent = rawContent.trim();

            // Agregar declaración XML si no la tiene
            if (!cleanedContent.startsWith("<?xml")) {
                logger.warn("El archivo no contiene encabezado XML. Agregando...");
                cleanedContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + cleanedContent;
            }

            // Crear un temporal con contenido limpio
            java.nio.file.Path tempPath = java.nio.file.Files.createTempFile("xml_temp", ".xml");
            java.nio.file.Files.write(tempPath, cleanedContent.getBytes());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(tempPath.toFile());

            // Normalizar el documento
            doc.getDocumentElement().normalize();

            logger.info("Elemento incial:", doc.getDocumentElement().getNodeName());

            // Extraer datos de cada boleta
            NodeList boletaList = doc.getElementsByTagName("boleta");
            logger.info("Total de boletas encontradas:", boletaList.getLength());

            for (int i = 0; i < boletaList.getLength(); i++) {
                Element boletaElement = (Element) boletaList.item(i);

                // Extraer datos del receptor
                Element receptorElement = (Element) boletaElement.getElementsByTagName("receptor").item(0);
                String razonSocial = getElementValue(receptorElement, "razonSocial");
                String nombre = getElementValue(receptorElement, "nombre");
                String email = getElementValue(receptorElement, "email");

                // Calcular total: suma(precio*cantidad) - descuento
                double subtotal = calcularSubtotal(boletaElement);
                double descuento = Double.parseDouble(getElementValue(boletaElement, "descuento/monto"));
                double total = subtotal - descuento;

                BoletaInfo boleta = new BoletaInfo(razonSocial, nombre, email, total);
                boletas.add(boleta);
            }

            logger.info("Procesamiento completado:", boletas.size());

            // Limpiar temporal
            java.nio.file.Files.delete(tempPath);

        } catch (ParserConfigurationException e) {
            logger.error("Error de configuración del parser XML", e);
        } catch (org.xml.sax.SAXException e) {
            logger.error("Error al parsear el XML (formato inválido)", e);
        } catch (IOException e) {
            logger.error("Error al leer el archivo XML", e);
        } catch (NumberFormatException e) {
            logger.error("Error al convertir valores numéricos", e);
        }

        return boletas;
    }

    private String getElementValue(Element element, String tagName) {
        if (tagName.contains("/")) {
            String[] parts = tagName.split("/");
            Element subElement = (Element) element.getElementsByTagName(parts[0]).item(0);
            if (subElement != null) {
                return subElement.getElementsByTagName(parts[1]).item(0).getTextContent().trim();
            }
            return "";
        }

        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent().trim();
        }
        return "";
    }

    private double calcularSubtotal(Element boletaElement) {
        double subtotal = 0;
        NodeList itemList = boletaElement.getElementsByTagName("item");

        for (int i = 0; i < itemList.getLength(); i++) {
            Element itemElement = (Element) itemList.item(i);
            double cantidad = Double.parseDouble(getElementValue(itemElement, "cantidad"));
            double precio = Double.parseDouble(getElementValue(itemElement, "precio"));
            subtotal += cantidad * precio;
        }

        return subtotal;
    }
}