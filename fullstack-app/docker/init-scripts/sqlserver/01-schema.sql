-- ============================================================
-- SQL Server - Schema: logs_db
-- Propósito: Auditoría, Logs del sistema, Notificaciones
-- ============================================================

-- Crear base de datos
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'logs_db')
BEGIN
    CREATE DATABASE logs_db;
END
GO

USE logs_db;
GO

-- ==================== TABLA: audit_logs ====================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='audit_logs' AND xtype='U')
BEGIN
    CREATE TABLE audit_logs (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        action NVARCHAR(50) NOT NULL,
        entity NVARCHAR(100) NOT NULL,
        entity_id BIGINT NULL,
        username NVARCHAR(50) NULL,
        details NVARCHAR(MAX) NULL,
        ip_address NVARCHAR(45) NULL,
        created_at DATETIME2 DEFAULT GETDATE()
    );

    CREATE INDEX idx_audit_username ON audit_logs(username);
    CREATE INDEX idx_audit_action ON audit_logs(action);
    CREATE INDEX idx_audit_entity ON audit_logs(entity, entity_id);
    CREATE INDEX idx_audit_date ON audit_logs(created_at);
END
GO

-- ==================== TABLA: system_logs ====================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='system_logs' AND xtype='U')
BEGIN
    CREATE TABLE system_logs (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        level NVARCHAR(10) NOT NULL CHECK (level IN ('INFO', 'WARN', 'ERROR', 'DEBUG')),
        service NVARCHAR(50) NOT NULL,
        message NVARCHAR(500) NOT NULL,
        stack_trace NVARCHAR(MAX) NULL,
        request_url NVARCHAR(500) NULL,
        request_method NVARCHAR(10) NULL,
        response_status INT NULL,
        duration_ms BIGINT NULL,
        username NVARCHAR(50) NULL,
        ip_address NVARCHAR(45) NULL,
        created_at DATETIME2 DEFAULT GETDATE()
    );

    CREATE INDEX idx_syslog_level ON system_logs(level);
    CREATE INDEX idx_syslog_service ON system_logs(service);
    CREATE INDEX idx_syslog_date ON system_logs(created_at);
END
GO

-- ==================== TABLA: login_attempts ====================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='login_attempts' AND xtype='U')
BEGIN
    CREATE TABLE login_attempts (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        username NVARCHAR(50) NOT NULL,
        ip_address NVARCHAR(45) NOT NULL,
        success BIT NOT NULL DEFAULT 0,
        failure_reason NVARCHAR(200) NULL,
        user_agent NVARCHAR(500) NULL,
        created_at DATETIME2 DEFAULT GETDATE()
    );

    CREATE INDEX idx_login_username ON login_attempts(username);
    CREATE INDEX idx_login_ip ON login_attempts(ip_address);
    CREATE INDEX idx_login_date ON login_attempts(created_at);
END
GO

-- ==================== TABLA: notifications ====================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='notifications' AND xtype='U')
BEGIN
    CREATE TABLE notifications (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        user_id BIGINT NOT NULL,
        title NVARCHAR(100) NOT NULL,
        message NVARCHAR(500) NOT NULL,
        type NVARCHAR(20) DEFAULT 'INFO' CHECK (type IN ('INFO', 'WARNING', 'ERROR', 'SUCCESS')),
        is_read BIT DEFAULT 0,
        read_at DATETIME2 NULL,
        created_at DATETIME2 DEFAULT GETDATE()
    );

    CREATE INDEX idx_notif_user ON notifications(user_id);
    CREATE INDEX idx_notif_read ON notifications(is_read);
    CREATE INDEX idx_notif_date ON notifications(created_at);
END
GO
