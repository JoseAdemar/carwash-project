CREATE TABLE customer (
        id BIGINT NOT NULL AUTO_INCREMENT,
        email VARCHAR(255),
        name VARCHAR(255),
        phone VARCHAR(255),
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE vehicle (
        id BIGINT NOT NULL AUTO_INCREMENT,
        brand VARCHAR(255) NOT NULL,
        category VARCHAR(255) NOT NULL,
        color VARCHAR(255) NOT NULL,
        license VARCHAR(255),
        model VARCHAR(255) NOT NULL,
        customer_id BIGINT,
        PRIMARY KEY (id),
        FOREIGN KEY (customer_id)
        REFERENCES customer (id)
    ) engine=InnoDB;

    CREATE TABLE service_order (
        id BIGINT NOT NULL AUTO_INCREMENT,
        date DATETIME,
        washStatus VARCHAR(255) NOT NULL,
        washType VARCHAR(255) NOT NULL,
        price DECIMAL(10,2),
        PRIMARY KEY (id),
        FOREIGN KEY (vehicle_id)
        REFERENCES vehicle (id)
    )engine=InnoDB;