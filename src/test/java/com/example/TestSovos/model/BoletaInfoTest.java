package com.example.TestSovos.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class BoletaInfoTest {

    @Test
    public void testCrearBoletaInfo() {
        BoletaInfo boleta = new BoletaInfo("Sovos", "Soledad", "soledad@example.com", 1500.00);
        
        assertNotNull(boleta, "La boleta no debe ser nula");
        assertEquals("Sovos", boleta.getRazonSocial());
        assertEquals("Soledad", boleta.getNombre());
        assertEquals("soledad@example.com", boleta.getEmail());
        assertEquals(1500.00, boleta.getTotal());
    }

    @Test
    public void testSettersBoletaInfo() {
        BoletaInfo boleta = new BoletaInfo("Empresa A", "Juan", "juan@test.com", 1000.00);
        
        boleta.setRazonSocial("Empresa B");
        boleta.setNombre("María");
        boleta.setEmail("maria@test.com");
        boleta.setTotal(2000.00);
        
        assertEquals("Empresa B", boleta.getRazonSocial());
        assertEquals("María", boleta.getNombre());
        assertEquals("maria@test.com", boleta.getEmail());
        assertEquals(2000.00, boleta.getTotal());
    }

    @Test
    public void testBoletaInfoConValoresNulos() {
        BoletaInfo boleta = new BoletaInfo(null, null, null, 0.0);
        
        assertNull(boleta.getRazonSocial());
        assertNull(boleta.getNombre());
        assertNull(boleta.getEmail());
        assertEquals(0.0, boleta.getTotal());
    }

    @Test
    public void testBoletaInfoConValoresDecimales() {
        double total = 1234.567;
        BoletaInfo boleta = new BoletaInfo("Test", "Test", "test@test.com", total);
        
        assertEquals(total, boleta.getTotal());
    }
}
