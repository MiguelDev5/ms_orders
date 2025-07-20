# 📦 ms-order

Microservicio de gestión de pedidos desarrollado en **Spring Boot** bajo arquitectura hexagonal. Consume mensajes desde una cola de Azure, persiste órdenes en **Cosmos DB** y registra eventos de auditoría en **Azure Blob Storage**.

---

## 🧱 Arquitectura

Este microservicio sigue el patrón de **puertos y adaptadores (Hexagonal Architecture)**:

- **Entradas**: Controlador REST (`OrderController`) y consumidor de cola (`OrderQueueConsumer`).
- **Núcleo**: `OrderUseCase` como caso de uso principal.
- **Salidas**: Cosmos DB (`CosmosOrderRepository`) y Azure Blob Storage (`AzureBlobAuditAdapter`).

Comunicación asincrónica con otros servicios vía **Azure Queue Storage**.

---

## 🚀 Tecnologías utilizadas

| Tecnología         | Descripción                          |
|--------------------|--------------------------------------|
| Java 17            | Lenguaje base                        |
| Spring Boot 3      | Framework principal                  |
| Azure SDK          | Azure Queue, Blob y Cosmos DB        |
| JUnit + Mockito    | Pruebas unitarias                    |
| Docker             | Contenedor para despliegue           |
| Maven              | Gestión de dependencias              |
| Kubernetes (opcional) | Archivos en carpeta `k8s/`       |

---

## 📁 Estructura principal del proyecto
```
ms-order/
├── src/
│ ├── main/java/com/order/ms_order/
│ │ ├── application/
│ │ ├── domain/model/
│ │ ├── adapters/
│ │ ├── infrastructure/
│ │ └── MsOrderApplication.java
│ └── resources/
│ └── application.properties
├── test/
│ └── java/com/order/ms_order/
│ ├── OrderUseCaseTest.java
│ ├── OrderQueueConsumerTest.java
│ └── ...
├── Dockerfile
├── k8s/
├── pom.xml
└── README.md
```
---

## ⚙️ Configuración local

1. **Prerrequisitos**:
    - JDK 17
    - Maven 3.x
    - Docker (opcional para despliegue)
    - Cuenta de Azure con:
        - Blob Storage
        - Queue Storage
        - Cosmos DB

2. **Variables de configuración** (`application.properties`):
   
- azure.queue.connection-string=UseDevelopmentStorage=true
- azure.queue.name=order-queue
- azure.blob.connection-string=UseDevelopmentStorage=true
- azure.blob.container-name=audit-container
- azure.cosmos.uri=https://<your-cosmos-db>.documents.azure.com:443/
- azure.cosmos.key=YOUR_KEY
- azure.cosmos.database=orders-db
- azure.cosmos.container=orders

---

## ▶️ Ejecución

```bash
# Compilar el proyecto
./mvnw clean install

# Ejecutar localmente
./mvnw spring-boot:run

# Ejecutar pruebas unitarias
./mvnw test

# Construir imagen
docker build -t ms-order:latest .

# Ejecutar contenedor
docker run -p 8080:8080 --env-file .env ms-order:latest

kubectl apply -f k8s/