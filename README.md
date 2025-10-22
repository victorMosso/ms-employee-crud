# Microservicio de Empleados (ms-employee-crud)

Este proyecto es un microservicio para la gestión de empleados (CRUD) desarrollado con Spring Boot.

## Tabla de Contenidos
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Prerrequisitos](#prerrequisitos)
- [Configuración de la Base de Datos](#configuración-de-la-base-de-datos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Documentación de la API](#documentación-de-la-api)
- [Endpoints Disponibles](#endpoints-disponibles)

## Tecnologías Utilizadas
- **Java**: `17`
- **Spring Boot**: `3.4.10`
- **Spring Cloud**: `2024.0.0`
- **Build Tool**: `Maven`
- **Base de Datos**: `MySQL`
- **Documentación API**: `SpringDoc (OpenAPI 3)`
- **Librerías**: `Lombok`, `Spring Data JPA`

## Prerrequisitos
- JDK 17 o superior.
- Maven 3.6 o superior.
- Una instancia de MySQL en ejecución.

## Configuración de la Base de Datos
1.  Asegúrese de que su servidor MySQL esté accesible en `localhost:3306`.
2.  Cree un esquema (base de datos) con el nombre `db_springboot_cloud`.
    ```sql
    CREATE DATABASE db_springboot_cloud;
    ```
3.  El servicio está configurado para conectarse con las siguientes credenciales. Si las suyas son diferentes, modifique el archivo `src/main/resources/application.properties`:
    - **URL**: `jdbc:mysql://127.0.0.1:3306/db_springboot_cloud`
    - **Usuario**: `root`
    - **Contraseña**: `admin`

    ```properties
    spring.datasource.url=jdbc:mysql://127.0.0.1:3306/db_springboot_cloud
    spring.datasource.username=root
    spring.datasource.password=admin
    ```

## Instalación y Ejecución
1.  Clone el repositorio:
    ```sh
    git clone https://github.com/victorMosso/ms-employee-crud/tree/develop
    cd ms-employee-crud
    ```
2.  Compile y ejecute el proyecto usando el wrapper de Maven:
    ```sh
    ./mvnw spring-boot:run
    ```
    En Windows, use:
    ```sh
    mvnw.cmd spring-boot:run
    ```
3.  La aplicación se iniciará en el puerto `8080`.

## Documentación de la API
Una vez que la aplicación esté en ejecución, puede acceder a la interfaz de Swagger (OpenAPI) para ver y probar los endpoints de la API.

Abra la siguiente URL en su navegador:
[http://localhost:8080/swagger-ui/index.html]

## Endpoints Disponibles
El path base para todos los endpoints es `/employees`.

| Método HTTP | Ruta                        | Descripción                               |
|-------------|-----------------------------|-------------------------------------------|
| `GET`       | `/`                         | Obtiene una lista de todos los empleados. |
| `GET`       | `/{id}`                     | Obtiene un empleado por su ID.            |
| `GET`       | `/search?name={name}`       | Busca empleados por nombre.               |
| `POST`      | `/`                         | Crea uno o más empleados nuevos.          |
| `PUT`       | `/{id}`                     | Actualiza un empleado existente.          |
| `DELETE`    | `/{id}`                     | Elimina un empleado por su ID.            |