create table customer (
       id bigint not null auto_increment,
        email varchar(255),
        name varchar(255),
        phone varchar(255),
        primary key (id)
    ) engine=InnoDB;


    create table service_order (
       id bigint not null auto_increment,
        big_decimal decimal(19,2) not null,
        date datetime(6),
        wash_status varchar(255) not null,
        wash_type varchar(255) not null,
        primary key (id),
        customer_id bigint,
        foreign key (customer_id)
        references customer (id),
        vehicle_id bigint,
        foreign key (vehicle_id)
        references vehicle(id)
    ) engine=InnoDB;

    CREATE TABLE service_order_vehicle (
        service_order_id BIGINT,
        vehicle_id BIGINT,
        PRIMARY KEY (service_order_id, vehicle_id),
        FOREIGN KEY (service_order_id) REFERENCES service_order(id),
        FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
    )engine=InnoDB;

    
    create table vehicle (
       id bigint not null auto_increment,
        brand varchar(255) not null,
        category varchar(255) not null,
        color varchar(255) not null,
        license varchar(255),
        model varchar(255) not null,
        customer_id bigint,
        primary key (id),
        foreign key (customer_id)
        references customer (id)
    ) engine=InnoDB;