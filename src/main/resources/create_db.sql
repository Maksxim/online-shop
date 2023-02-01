CREATE DATABASE order_service2;

Use order_service2;

CREATE TABLE products(
                         id int primary key auto_increment,
                         product_name varchar(128),
                         description varchar(512),
                         price double,
                         image_path varchar(100)
);

CREATE TABLE orders(
                       id int primary key auto_increment,
                       Date_Time datetime,
                       finished boolean,
                       name varchar(128),
                       email varchar(128),
                       phone varchar(128),
                       address varchar(128),
                       payment_method varchar(64)
);

CREATE TABLE items(
                      id int primary key auto_increment,
                      product_id int,
                      price double,
                      count int,
                      product_name varchar(128),
                      order_id int not null,
                      constraint fk_orders foreign key (order_id) references orders(id)
);
