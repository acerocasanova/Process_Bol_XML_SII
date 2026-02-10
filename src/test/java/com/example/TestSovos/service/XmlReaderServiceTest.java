package com.example.TestSovos.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.TestSovos.model.BoletaInfo;

@SpringBootTest
public class XmlReaderServiceTest {

    @Autowired
    private XmlReaderService xmlReaderService;

    private Path xmlTestFile;

    @BeforeEach
    public void setUp() throws Exception {
        // Crear archivo XML de prueba
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<boletas>\n" +
            "    <boleta>\n" +
            "        <receptor>\n" +
            "            <razonSocial>Empresa Test</razonSocial>\n" +
            "            <nombre>Juan García</nombre>\n" +
            "            <email>juan@test.com</email>\n" +
            "        </receptor>\n" +
            "        <detalle>\n" +
            "            <item>\n" +
            "                <producto>Producto A</producto>\n" +
            "                <cantidad>10</cantidad>\n" +
            "                <precio>100</precio>\n" +
            "            </item>\n" +
            "        </detalle>\n" +
            "        <descuento>\n" +
            "            <monto>50</monto>\n" +
            "        </descuento>\n" +
            "    </boleta>\n" +
            "</boletas>";

        xmlTestFile = Paths.get("test_boleta.xml");
        Files.write(xmlTestFile, xmlContent.getBytes());
    }

    @Test
    public void testReadAndLogXmlValid() {
        List<BoletaInfo> boletas = xmlReaderService.readAndLogXml(xmlTestFile);
        
        assertNotNull(boletas, "La lista de boletas no debe ser nula");
        assertEquals(1, boletas.size(), "Debe haber una boleta");
        
        BoletaInfo boleta = boletas.get(0);
        assertEquals("Empresa Test", boleta.getRazonSocial());
        assertEquals("Juan García", boleta.getNombre());
        assertEquals("juan@test.com", boleta.getEmail());
        
        // Total = (10 * 100) - 50 = 950
        assertEquals(950.0, boleta.getTotal(), 0.01);
    }

    @Test
    public void testReadAndLogXmlWithoutDeclaration() throws Exception {
        // Crear XML sin declaración
        String xmlContent = "<boletas>\n" +
            "    <boleta>\n" +
            "        <receptor>\n" +
            "            <razonSocial>Empresa 2</razonSocial>\n" +
            "            <nombre>María López</nombre>\n" +
            "            <email>maria@test.com</email>\n" +
            "        </receptor>\n" +
            "        <detalle>\n" +
            "            <item>\n" +
            "                <cantidad>5</cantidad>\n" +
            "                <precio>200</precio>\n" +
            "            </item>\n" +
            "        </detalle>\n" +
            "        <descuento>\n" +
            "            <monto>25</monto>\n" +
            "        </descuento>\n" +
            "    </boleta>\n" +
            "</boletas>";

        Path xmlTestFile2 = Paths.get("test_boleta_nodecl.xml");
        Files.write(xmlTestFile2, xmlContent.getBytes());

        List<BoletaInfo> boletas = xmlReaderService.readAndLogXml(xmlTestFile2);
        
        assertNotNull(boletas, "Debe procesar XML sin declaración");
        assertEquals(1, boletas.size());
        
        // Total = (5 * 200) - 25 = 975
        assertEquals(975.0, boletas.get(0).getTotal(), 0.01);
        
        Files.delete(xmlTestFile2);
    }

    @Test
    public void testReadAndLogXmlMultipleBoletas() throws Exception {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<boletas>\n" +
            "    <boleta>\n" +
            "        <receptor>\n" +
            "            <razonSocial>Empresa 1</razonSocial>\n" +
            "            <nombre>Cliente 1</nombre>\n" +
            "            <email>cliente1@test.com</email>\n" +
            "        </receptor>\n" +
            "        <detalle>\n" +
            "            <item>\n" +
            "                <cantidad>5</cantidad>\n" +
            "                <precio>100</precio>\n" +
            "            </item>\n" +
            "        </detalle>\n" +
            "        <descuento>\n" +
            "            <monto>10</monto>\n" +
            "        </descuento>\n" +
            "    </boleta>\n" +
            "    <boleta>\n" +
            "        <receptor>\n" +
            "            <razonSocial>Empresa 2</razonSocial>\n" +
            "            <nombre>Cliente 2</nombre>\n" +
            "            <email>cliente2@test.com</email>\n" +
            "        </receptor>\n" +
            "        <detalle>\n" +
            "            <item>\n" +
            "                <cantidad>3</cantidad>\n" +
            "                <precio>150</precio>\n" +
            "            </item>\n" +
            "        </detalle>\n" +
            "        <descuento>\n" +
            "            <monto>20</monto>\n" +
            "        </descuento>\n" +
            "    </boleta>\n" +
            "</boletas>";

        Path xmlTestFile3 = Paths.get("test_boleta_multiple.xml");
        Files.write(xmlTestFile3, xmlContent.getBytes());

        List<BoletaInfo> boletas = xmlReaderService.readAndLogXml(xmlTestFile3);
        
        assertEquals(2, boletas.size(), "Debe haber 2 boletas");
        assertEquals("Empresa 1", boletas.get(0).getRazonSocial());
        assertEquals("Empresa 2", boletas.get(1).getRazonSocial());
        
        Files.delete(xmlTestFile3);
    }
}
