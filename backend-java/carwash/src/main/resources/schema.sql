create table customer (
        id bigint not null auto_increment,
        email varchar(255),
        name varchar(255),
        phone varchar(255),
        primary key (id)
    ) engine=InnoDB;

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

    create table service_order (
        id bigint not null auto_increment,
        date DATETIME,
        washStatus varchar(255) not null,
        washType varchar(255) not null,
        price DECIMAL(10,2),
        primary key (id),
        foreign key (customer_id)
        references customer (id),
        foreign key (vehicle_id)
        references vehicle (id)
    )engine=InnoDB;