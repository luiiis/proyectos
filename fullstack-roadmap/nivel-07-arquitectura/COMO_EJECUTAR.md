# Cómo Ejecutar — Nivel 7: Arquitectura Hexagonal

## Prerrequisitos
- Nivel 5-6 completado (CRUD + seguridad + correo)
- Este nivel REORGANIZA el código existente (no agrega funcionalidades nuevas)

## Ejecutar
```cmd
cd nivel-07-arquitectura/proyecto/backend
mvn spring-boot:run
```

## ¿Qué cambió respecto al nivel anterior?
Mismo resultado, diferente organización interna:

```
ANTES (por capas):               DESPUÉS (hexagonal):
controller/                      infrastructure/adapter/in/rest/
service/                         application/usecase/
repository/                      infrastructure/adapter/out/persistence/
model/                           domain/model/
dto/                             application/dto/
```

## Verificar que funciona igual
```bash
# Los mismos endpoints siguen funcionando:
curl http://localhost:8080/api/productos
curl -X POST http://localhost:8080/api/auth/login -d '{"username":"admin","password":"Admin123!"}'
```

## Comando para verificar dependencias
El dominio NO debe importar nada de Spring:
```bash
# Buscar imports de Spring en domain/ (debe dar 0 resultados)
grep -r "import org.springframework" src/main/java/com/softwarelee/productos/domain/
# Si encuentra algo → el dominio está contaminado → corregir
```
