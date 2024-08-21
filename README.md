# EvaluacionDsegovia API de Gestión de Usuarios

## Descripción
Esta API RESTful proporciona funcionalidades para la gestión de usuarios, incluyendo registro Y autenticación de usuarios.

# Construir y Levantar los Contenedores

- **Clonar el repositorio**: `git clone https://github.com/danielSegoviaVega94/integracion.git`

Desde el directorio raíz (integracion), ejecutar los siguientes comandos:

docker-compose up --build

# Construir manualmente

Prerequisitos:

Java Development Kit (JDK) 17: Para ejecutar la aplicación Spring Boot.

- **Ejecutar el proyecto**: `mvn spring-boot:run`

### Login
- **URL**: `POST /api/auth/login`
- **Cuerpo de la solicitud**:
  ```json
  {
    "correo": "juan.perez@example.com",
    "password": "hola@22K!"
  }
  ```
- **Respuesta**:
  ```json
  {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyZDMxMDNmNi01NTdhLTQ2YjEtYTViYS0zYWQxNzRkMDAzMzMiLCJpYXQiOjE3MjQxOTc5MjQsImV4cCI6MTcyNDgwMjcyNH0.BHt83ksiHew7v4Y-kMpRRD5SsW9PZLFLCvacz2gbLkhy3Q8Kz74Z8RS7mtu2z37vwgQ6fU8TLS58EB7UT-eKYQ",
    "tokenType": "Bearer"
  }
  ```
### CreateUser
- **URL**: `POST /api/auth/signup`
- **Content-Type**: `application/json`
- **Cuerpo de la solicitud**:
  ```json
  {
    "nombre": "Juan Perez",
    "correo": "juan.perez@example.com",
    "password": "hola@22K!",
    "phones": [
        {
            "number": "123456789",
            "citycode": "1",
            "contrycode": "57"
        },
        {
            "number": "987654321",
            "citycode": "2",
            "contrycode": "57"
        }
    ]
  }
  ```
- **Respuesta**:
  ```json
  { "id": "2d3103f6-557a-46b1-a5ba-3ad174d00333",
    "name": "Juan Perez",
    "email": "juan.perez@example.com",
    "phones": [
        {
            "number": "123456789",
            "citycode": "1",
            "contrycode": "57"
        },
        {
            "number": "987654321",
            "citycode": "2",
            "contrycode": "57"
        }
    ],
    "token": "4b8974e7-085f-4e80-b15d-409502fd2cab",
    "created": "2024-08-20T19:49:23.484502",
    "modified": "2024-08-20T19:49:23.484502",
    "lastLogin": "2024-08-20T19:49:23.484502",
    "active": true
  }
  ```

## Notas
- La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula y un carácter especial.
- Se ocupo MapStruct para mapear entidades y DTO.
- Se ocupo lombok para disminuir codigo repetetivo.
- Autenticación y Autorización con JWT (JSON Web Tokens).
- Documentacion con Springdoc OpenAPI.
- Se ocupo docker para facilitar el uso.

## Diagramas
![img.png](img.png)

![img_1.png](img_1.png)
