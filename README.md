# SSPP (Sistema de Seguimiento y Progreso de Presos)

## Descripción del Proyecto

SSPP es un sistema de seguimiento para la gestión de presos con el objetivo de apoyar en su rehabilitación a través del registro de sus sentencias, participación en talleres y generación de datos relevantes para su seguimiento. El proyecto está desarrollado en Java utilizando JavaFX para la interfaz gráfica y MySQL para la gestión de datos, con un enfoque en simplificar el uso y facilitar la visualización de la información de los presos, talleres y sentencias.

## Funcionalidades Implementadas

### 1. Registro de Presos
Permite registrar a los presos con sus datos personales, tales como nombre, apellido, fecha de nacimiento, nacionalidad, tipo de documento, número de documento, prisión de origen y su conducta.

### 2. Registro de Sentencias
Permite registrar las sentencias de forma independiente a los presos. Cada sentencia puede incluir la fecha de sentencia, el contexto del crimen, la condena en años, y comentarios adicionales del comportamiento del preso durante cada sentencia. También permite especificar si una sentencia es actual o pasada. Además, se pueden registrar los crímenes asociados a cada sentencia, detallando el crimen y la condena específica.

### 3. Registro de Talleres
Permite registrar talleres destinados a la rehabilitación de los presos, con datos como capacidad, descripción del taller, fecha de inicio y fecha de fin.

### 4. Creación de Matrículas
Permite asignar presos a talleres previamente registrados, generando una matrícula que documenta la participación del preso en el taller.

### 5. Modificación de Datos
Permite modificar o eliminar los datos de presos y talleres. Si se elimina un preso o un taller, todas las sentencias y matrículas relacionadas también se eliminan en consecuencia.

### 6. Visualización de Presos
Proporciona una vista de los presos registrados con la capacidad de aplicar filtros y ordenar los registros. Los presos se pueden filtrar por si están matriculados en talleres o no, y se puede ordenar la información por cada campo disponible. Aún no se puede visualizar los detalles de la sentencia

## Arquitectura del Proyecto
El proyecto está desarrollado bajo una arquitectura multicapa, que facilita la separación de responsabilidades y mejora el mantenimiento del código.

### Capas Principales
1. **Capa de Presentación**: Implementada utilizando JavaFX, esta capa proporciona la interfaz de usuario para interactuar con el sistema. Se encarga de mostrar las vistas de registro, modificación, y visualización de datos.

2. **Capa de Lógica de Negocio**: Contiene la lógica necesaria para gestionar el flujo del sistema, como validar los datos ingresados por los usuarios, aplicar las reglas del negocio para el registro de presos, sentencias y talleres.

3. **Capa de Acceso a Datos (DAO)**: Utiliza Java Database Connectivity (JDBC) para realizar operaciones CRUD en la base de datos MySQL. Esta capa se encarga de gestionar las conexiones y realizar las consultas necesarias para registrar, modificar y eliminar datos.

### Base de Datos
La base de datos está alojada en Clever Cloud y contiene las tablas necesarias para gestionar los registros de presos, sentencias, talleres y matrículas.

## Tecnologías Utilizadas
- **Lenguaje**: Java
- **Interfaz Gráfica**: JavaFX
- **Base de Datos**: MySQL (Alojada en Clever Cloud)
- **IDE**: Apache NetBeans
- **Gestor de Dependencias**: Maven

## Instalación y Ejecución
Para ejecutar el proyecto localmente, sigue estos pasos:
1. Clona el repositorio desde GitHub:
   ```bash
   git clone https://github.com/WilsonEspinozaAponte/Sistema-de-gestion-de-presos---SSPP
   ```
2. Abre el proyecto en Apache NetBeans.
3. Asegúrate de que Maven esté correctamente configurado y ejecuta:
   ```bash
   mvn javafx:run
   ```

## Contribución
Las contribuciones son bienvenidas. Si deseas contribuir, por favor realiza un fork del repositorio, realiza los cambios necesarios y crea un pull request para revisión, se planea que el proyecto este en constante mejora.

## Contacto
Para cualquier consulta o sugerencia, puedes contactarme a través de wilsonespinozaaponte.28@gmail.com o abrir un issue en el repositorio.

