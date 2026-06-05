# Módulo 04: POO - Manual Técnico

## ¿Qué construimos?
Un mini sistema bancario que demuestra los 4 pilares de POO:
- **Encapsulamiento**: saldo privado, solo accesible via métodos
- **Herencia**: CuentaAhorro y CuentaCorriente heredan de Cuenta
- **Polimorfismo**: calcularInteres() se comporta diferente en cada tipo
- **Abstracción**: Cuenta es abstracta, no se puede instanciar directamente

## Cómo ejecutar
```bash
cd java-learning/04-poo/proyecto
javac *.java
java SistemaBancario
```

## Diagrama UML
```
        ┌─────────────────────┐
        │   <<abstract>>      │
        │      Cuenta         │
        ├─────────────────────┤
        │ - titular: String   │
        │ - saldo: double     │
        │ - numero: String    │
        ├─────────────────────┤
        │ + depositar(monto)  │
        │ + retirar(monto)    │
        │ + getSaldo()        │
        │ + abstract          │
        │   calcularInteres() │
        └──────────┬──────────┘
                   │
          ┌────────┴────────┐
          │                 │
┌─────────┴──────┐  ┌──────┴─────────┐
│ CuentaAhorro   │  │CuentaCorriente │
├────────────────┤  ├────────────────┤
│ - tasaInteres  │  │ - sobregiro    │
├────────────────┤  ├────────────────┤
│+calcularInteres│  │+calcularInteres│
│+aplicarInteres │  │+retirar(monto) │
└────────────────┘  └────────────────┘
```
