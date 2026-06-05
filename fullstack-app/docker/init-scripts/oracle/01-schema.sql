-- ============================================================
-- Oracle XE - Schema: products_user
-- Propósito: Productos, Inventario, Ventas
-- Nota: Este script se ejecuta como el APP_USER definido en Docker
-- ============================================================

-- ==================== SECUENCIAS ====================
CREATE SEQUENCE PRODUCT_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE INVENTORY_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SALE_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SALE_DETAIL_SEQ START WITH 1 INCREMENT BY 1;

-- ==================== TABLA: products ====================
CREATE TABLE products (
    id NUMBER(19) DEFAULT PRODUCT_SEQ.NEXTVAL PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    description VARCHAR2(500),
    price NUMBER(10,2) NOT NULL,
    stock NUMBER(10) DEFAULT 0 NOT NULL,
    category VARCHAR2(50),
    sku VARCHAR2(50),
    is_active NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_active ON products(is_active);

-- ==================== TABLA: inventory_movements ====================
CREATE TABLE inventory_movements (
    id NUMBER(19) DEFAULT INVENTORY_SEQ.NEXTVAL PRIMARY KEY,
    product_id NUMBER(19) NOT NULL,
    type VARCHAR2(10) NOT NULL CHECK (type IN ('ENTRY', 'EXIT')),
    quantity NUMBER(10) NOT NULL,
    reason VARCHAR2(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR2(50),
    CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_inventory_product ON inventory_movements(product_id);
CREATE INDEX idx_inventory_type ON inventory_movements(type);

-- ==================== TABLA: sales ====================
CREATE TABLE sales (
    id NUMBER(19) DEFAULT SALE_SEQ.NEXTVAL PRIMARY KEY,
    sale_number VARCHAR2(20) NOT NULL UNIQUE,
    customer_name VARCHAR2(100),
    customer_email VARCHAR2(100),
    total_amount NUMBER(12,2) NOT NULL,
    status VARCHAR2(20) DEFAULT 'COMPLETED' CHECK (status IN ('PENDING', 'COMPLETED', 'CANCELLED')),
    notes VARCHAR2(500),
    created_by VARCHAR2(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_sales_number ON sales(sale_number);
CREATE INDEX idx_sales_status ON sales(status);
CREATE INDEX idx_sales_date ON sales(created_at);

-- ==================== TABLA: sale_details ====================
CREATE TABLE sale_details (
    id NUMBER(19) DEFAULT SALE_DETAIL_SEQ.NEXTVAL PRIMARY KEY,
    sale_id NUMBER(19) NOT NULL,
    product_id NUMBER(19) NOT NULL,
    quantity NUMBER(10) NOT NULL,
    unit_price NUMBER(10,2) NOT NULL,
    subtotal NUMBER(12,2) NOT NULL,
    CONSTRAINT fk_sale_detail_sale FOREIGN KEY (sale_id) REFERENCES sales(id),
    CONSTRAINT fk_sale_detail_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_sale_details_sale ON sale_details(sale_id);
CREATE INDEX idx_sale_details_product ON sale_details(product_id);
