================================================================================
                          PROYECTO TESTSOVOS
                    Portal Web de Carga y Procesamiento de XML
================================================================================

DESCRIPCIÓN
================================================================================
TestSovos es una aplicación web desarrollada con Spring Boot que permite:
- Cargar archivos XML con información de boletas
- Extrae automáticamente datos de las boletas (razón social, nombre, email, total)
- Genera reportes en formato TXT y CSV
- Descarga de datos procesados en múltiples formatos
- Suite completa de pruebas JUnit para validación de funcionalidad

La aplicación cuenta con una interfaz web intuitiva con diseño moderno en azul
y proporciona validación automática de XML con manejo de declaraciones faltantes.

================================================================================
REQUISITOS PREVIOS
================================================================================
- Java 1.8
- Maven 3.9.9
- Navegador web moderno (Chrome, Firefox, Safari, Edge)
- Sistema operativo: macOS, Linux o Windows

================================================================================
ESTRUCTURA DEL PROYECTO
================================================================================
TestSovos/
├── src/
│   ├── main/
│   │   ├── java/com/example/TestSovos/
│   │   │   ├── config/
│   │   │   │   └── AppConfig.java              (Configuración de Beans)
│   │   │   ├── controller/
│   │   │   │   └── FileUploadController.java   (Endpoints HTTP)
│   │   │   ├── model/
│   │   │   │   └── BoletaInfo.java             (Modelo de datos)
│   │   │   ├── service/
│   │   │   │   ├── XmlReaderService.java       (Lectura y parseo XML)
│   │   │   │   └── FileGeneratorService.java   (Generación de reportes)
│   │   │   └── TestSovosApplication.java       (Punto de entrada)
│   │   ├── resources/
│   │   │   ├── application.properties          (Configuración app)
│   │   │   ├── banner.txt                      (Banner de inicio)
│   │   │   ├── static/                         (Recursos estáticos)
│   │   │   └── templates/
│   │   │       └── index.html                  (Interfaz web)
│   └── test/
│       └── java/com/example/TestSovos/
│           └── service/
│               ├── XmlReaderServiceTest.java   (Tests de parseo XML)
│               ├── FileGeneratorServiceTest.java (Tests de generación)
│               └── BoletaInfoTest.java         (Tests del modelo)
├── pom.xml                                      (Descriptor Maven)
├── mvnw                                         (Maven Wrapper - macOS/Linux)
├── mvnw.cmd                                     (Maven Wrapper - Windows)
└── README.txt                                   (Este archivo)

================================================================================
COMPILACIÓN DEL PROYECTO
================================================================================

1. Navegue al directorio del proyecto:
   cd /Users/acero/Desarrollo/proyect-maven/TestSovos

2. Compile el proyecto usando Maven:
   ./mvnw clean compile

3. Para compilación y ejecución de pruebas:
   ./mvnw clean package

   NOTA: En Windows use 'mvnw.cmd' en lugar de './mvnw'

================================================================================
EJECUCIÓN DE LA APLICACIÓN
================================================================================

1. Inicie la aplicación con:
   ./mvnw spring-boot:run

2. Espere a que la aplicación se inicie (aprox. 3-5 segundos)

3. Abra su navegador web e ingrese:
   http://localhost:8080/

4. Para detener la aplicación, presione Ctrl+C en la terminal

================================================================================
EJECUCIÓN DE PRUEBAS UNITARIAS
================================================================================

Para ejecutar todos los tests:
   ./mvnw test

Para ejecutar tests en modo silencioso:
   ./mvnw test -q

Para ejecutar un test específico:
   ./mvnw test -Dtest=FileGeneratorServiceTest

Para ejecutar tests con reporte de cobertura:
   ./mvnw test -Pcoverage

RESUMEN DE TESTS:
- Total: 14 tests
- Clase BoletaInfoTest: 4 tests
- Clase FileGeneratorServiceTest: 6 tests
- Clase XmlReaderServiceTest: 3 tests
- Clase TestSovosApplicationTests: 1 test

Estado: Todos los tests PASAN!

================================================================================
FUNCIONALIDADES PRINCIPALES
================================================================================

1. CARGA DE ARCHIVOS XML
   - Soporta archivos XML con información de boletas
   - Valida y procesa archivos automáticamente
   - Maneja XML sin declaración (la agrega automáticamente)
   - Formatos aceptados: .txt, .xml (extensiones)

2. EXTRACCIÓN DE DATOS
   - Razón Social de la empresa
   - Nombre del contacto
   - Email del contacto
   - Total calculado (suma de precios × cantidades - descuentos)

3. VISUALIZACIÓN EN TABLA
   - Tabla interactiva con datos extraídos
   - Formato de moneda con 2 decimales

4. GENERACIÓN DE REPORTES
   a) Formato TXT
   b) Formato CSV

5. DESCARGA DE ARCHIVOS
   - Descargar reporte en TXT
   - Descargar datos en CSV

================================================================================
ESPECIFICACIÓN TÉCNICA
================================================================================

FRAMEWORK Y VERSIONES:
- Spring Boot: 2.7.18
- Java: 1.8
- Apache Maven: 3.9.9
- Thymeleaf: 3.x (templating)

DEPENDENCIAS CLAVE:
- spring-boot-starter-web (MVC)
- spring-boot-starter-thymeleaf (Templating)
- spring-boot-starter-test (JUnit 5)
- Java DOM Parser (parseo XML)

API ENDPOINTS:
- GET  /                    → Página principal (formulario de carga)
- POST /upload              → Procesar archivo XML cargado
- GET  /descargar/txt       → Descargar reporte TXT
- GET  /descargar/csv       → Descargar reporte CSV

ALMACENAMIENTO:
- Archivos subidos: directorio 'uploads/' (creado automáticamente)
- Datos procesados: HttpSession (sesión del navegador)
- Configuración: application.properties

================================================================================
FORMATO DEL ARCHIVO XML ESPERADO
================================================================================

Estructura básica esperada:

<?xml version="1.0" encoding="UTF-8"?>
<boletas>
  <boleta>
    <razonSocial>Nombre de la Empresa</razonSocial>
    <nombre>Nombre Contacto</nombre>
    <email>correo@empresa.com</email>
    <items>
      <item>
        <cantidad>5</cantidad>
        <precio>100.00</precio>
      </item>
      <item>
        <cantidad>3</cantidad>
        <precio>50.00</precio>
      </item>
    </items>
    <descuento>50.00</descuento>
  </boleta>
</boletas>

CÁLCULO DEL TOTAL:
Total = (cantidad1 × precio1 + cantidad2 × precio2 + ...) - descuento

================================================================================
USO DE LA APLICACIÓN
================================================================================

PASO 1: Acceder a la interfaz web
   - Abra navegador
   - Vaya a http://localhost:8080/

PASO 2: Cargar archivo XML
   - Haga clic en "Seleccionar archivo"
   - Seleccione su archivo XML
   - Haga clic en botón "Cargar archivo"

PASO 3: Ver resultados
   - La tabla se llenará automáticamente con datos extraídos
   - Visualice razón social, nombre, email y total

PASO 4: Descargar reportes
   - Botón "Descargar TXT" Descarga en formato texto
   - Botón "Descargar CSV" Descarga en formato CSV
