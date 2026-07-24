# Cómo Ejecutar — Proyecto 7: Arquitectura Hexagonal

## Prerrequisitos
- MySQL con BD `productos_db` (del nivel 3)
- Java 21 + Maven

## Ejecutar
```cmd
cd nivel-07-arquitectura/proyecto/backend
mvn spring-boot:run
```

## Verificar que funciona igual que nivel 3
```bash
# Los mismos endpoints:
curl http://localhost:8080/api/productos
curl http://localhost:8080/api/productos/1
curl "http://localhost:8080/api/productos/buscar?nombre=laptop"
curl http://localhost:8080/api/productos/categoria/1
```

## ¿Qué cambió?
Misma funcionalidad, diferente organización interna:

```
ANTES (nivel 3 — por capas):        DESPUÉS (nivel 7 — hexagonal):
controller/                          infrastructure/adapter/in/rest/
service/                             application/usecase/
mapper/                              infrastructure/adapter/out/persistence/
model/                               domain/model/
```

## Estructura Hexagonal
```
src/main/java/com/softwarelee/productos/
├── Application.java
├── domain/                              ← DOMINIO (Java puro, sin frameworks)
│   ├── model/
│   │   └── Producto.java               ← Entidad + reglas de negocio
│   ├── ports/
│   │   ├── in/                          ← Puertos de ENTRADA (interfaces de casos de uso)
│   │   │   ├── CrearProductoUseCase.java
│   │   │   └── ConsultarProductoUseCase.java
│   │   └── out/                         ← Puertos de SALIDA (interfaces de persistencia)
│   │       └── ProductoRepository.java
│   └── exception/
│       └── ProductoNotFoundException.java
├── application/                         ← APLICACIÓN (implementa los casos de uso)
│   └── usecase/
│       ├── CrearProductoUseCaseImpl.java
│       └── ConsultarProductoUseCaseImpl.java
└── infrastructure/                      ← INFRAESTRUCTURA (frameworks, BD, HTTP)
    └── adapter/
        ├── in/rest/
        │   └── ProductoRestAdapter.java     ← Controller
        └── out/persistence/
            ├── ProductoMyBatisAdapter.java   ← Implementa el Repository
            └── ProductoPersistenceMapper.java ← MyBatis Mapper
```

## Regla de oro
El dominio NO importa nada de Spring ni MyBatis:
```bash
# Este comando debe dar 0 resultados:
grep -r "import org.springframework" src/main/java/com/softwarelee/productos/domain/
grep -r "import org.apache.ibatis" src/main/java/com/softwarelee/productos/domain/
```
