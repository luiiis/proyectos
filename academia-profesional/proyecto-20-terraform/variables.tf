variable "region" {
  description = "AWS Region"
  default     = "us-east-1"
}

variable "project" {
  description = "Nombre del proyecto (prefijo para recursos)"
  default     = "academia-erp"
}

variable "db_password" {
  description = "Password de PostgreSQL"
  sensitive   = true  # No se muestra en logs
}
