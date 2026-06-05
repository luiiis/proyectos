# Cómo Ejecutar - Proyecto 20: Terraform

## Prerrequisitos
```bash
# Instalar Terraform
# Windows: choco install terraform
# Mac: brew install terraform
# Linux: https://developer.hashicorp.com/terraform/install

# Configurar AWS CLI
aws configure
# AWS Access Key ID: tu-access-key
# AWS Secret Access Key: tu-secret-key
# Default region: us-east-1
```

## Ejecutar
```bash
cd academia-profesional/proyecto-20-terraform

# 1. Inicializar (descarga providers)
terraform init

# 2. Ver qué va a crear (sin crear nada)
terraform plan -var="db_password=MiPassword123!"

# 3. CREAR toda la infraestructura
terraform apply -var="db_password=MiPassword123!"
# Escribe "yes" para confirmar

# 4. Ver recursos creados
terraform state list

# 5. Ver outputs (URLs, endpoints)
terraform output

# 6. DESTRUIR todo (cuando termines de practicar)
terraform destroy -var="db_password=MiPassword123!"
```

## Comandos útiles
```bash
terraform fmt          # Formatear archivos .tf
terraform validate     # Verificar sintaxis
terraform plan         # Preview de cambios
terraform apply        # Aplicar cambios
terraform destroy      # Eliminar TODO
terraform state list   # Ver recursos gestionados
terraform output       # Ver valores de salida
```

## IMPORTANTE
```
⚠️ terraform destroy ANTES de dejar de practicar
    Si no → AWS te cobra por los recursos corriendo
    Free tier tiene límites (750 horas/mes de t3.micro)
```
