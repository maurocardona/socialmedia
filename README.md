# Gestion de usuarios y recursos

## Contexto

Esta implementación esta basada en el artículo [Clean Architecture � Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a).

Proyecto generado con la utilidad de SpringBootInitializer y complementado con el plugin que mencionan en el artículo anterior, que nos entrega como resultado la estructura del proyecto implementando los principios de la Arquitectura Limpia y separado por módulos usando Gradle.

A continuación dejo como evidencia parte de la documentación que entrega el plugin, y que considero importante tener en cuenta antes de empezar. 

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

# Consideraciones técnicas

## Arquitectura Limpia (Clean Architecture)
Se usan los principios de la Arquitectura Limpia para hacer la aplicación sostenible y escalable en el tiempo, brindando estructura y orden al desarrollo del proyecto.

## Principios SOLID
Se intenta seguir estos principios para asegurar la calidad del código escrito, estable, limpio y escalable. 

## WebFlux
Se implementa la librería de WebFlux dadas sus capacidades de majenar las peticiones (back-pressure, non-blocking) y las ventajas que tiene frente a el Rest Template tradicional.

## MongoDB
Se implementa MongoDB por su facilidad de implementar y su flexibilidad para almancer los datos. Además de sus otras cualidades.

## Prometheus
Monitoreo de la aplicación con Prometheus (tarea sin finalizar), que considero es una de las partes mas importantes a la hora de lanzar una aplicación a un ambiente de trabajo. Así poder tomar medidas correctivas a tiempo.

## Contenerización
Me queda pendiente la generación de la imagen con Docker

## Logger
Me queda pendiente la integración con un framework de loggin, Splunk tal vez.


# Sobre la API
La aplicación brinda los siguientes servicios:

- GET /socialmedia/users -> consulta todos los usuarios del servicio externo.
- GET /socialmedia/photos -> consulta todas las fotos del servicio externo.
- GET /socialmedia/albums -> consulta todos los albumes del servicio externo.
- GET /socialmedia/comments -> consulta todos los comentarios del servicio externo.
- GET /socialmedia/user/{userId}/albums -> consulta todos los albumes de un usuario.
- GET /socialmedia/user/{userId}/photos -> consulta todas los fotos de un usuario.
- GET /socialmedia/shared-album/owner-id/{userId} -> consulta todos los albumes compartidos de un usuario
- POST /socialmedia/shared-album -> crea un nuevo album compartido
- PUT /socialmedia/shared-album/update-grant/album-id/{albumId}/user-id/{userId}/grant/{grant} -> modifica los permisos de un usuario sobre determinado album.
- GET /socialmedia/users/album/{albumId}/grant/{grant} -> consulta todos los usuarios que tienen determinado permiso sobre un album compartido

Se deja un proyecto de Insomnia Rest Client con ejemplos de consumo a los diferentes servicios en el directorio /sources del proyecto. 
