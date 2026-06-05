# Proyecto 20: Terraform (Infrastructure as Code)

## ¿Qué construimos?
Definir TODA la infraestructura AWS del proyecto 19 en código (archivos .tf).
Un comando crea todo. Otro comando lo destruye.

## ¿Por qué Terraform?

```
Sin Terraform:
  1. Abrir consola AWS
  2. Clic, clic, clic (crear VPC, subnets, security groups, RDS, ECS...)
  3. 2 horas después: "listo" (pero no recuerdas qué hiciste)
  4. Necesitas otro ambiente (staging) → repetir TODO manualmente
  5. Algo se rompe → ¿qué cambió? No sabes.

Con Terraform:
  1. Escribir main.tf (5 minutos)
  2. terraform apply (Terraform crea TODO automáticamente)
  3. Necesitas staging → terraform workspace new staging → apply
  4. Algo se rompe → git log (ves exactamente qué cambió y quién)
  5. Destruir todo → terraform destroy (limpio, sin residuos)
```

## Estructura del proyecto
```
proyecto-20-terraform/
├── main.tf              ← Recursos principales (VPC, ECS, RDS)
├── variables.tf         ← Variables configurables
├── outputs.tf           ← Valores de salida (URLs, IPs)
├── terraform.tfvars     ← Valores de las variables (NO commitear passwords)
└── modules/
    ├── vpc/             ← Red privada
    ├── ecs/             ← Contenedores
    ├── rds/             ← Base de datos
    └── redis/           ← Cache
```
