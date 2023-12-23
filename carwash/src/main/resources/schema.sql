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
        customer_id bigint,
        primary key (id),
        foreign key (customer_id)
        references customer (id)
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