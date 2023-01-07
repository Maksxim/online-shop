CREATE DATABASE order_service2;

Use order_service2;

CREATE TABLE products(
                         id int primary key auto_increment,
                         description varchar(128),
                         price double,
                         image_path varchar(100)
);

CREATE TABLE orders(
                       id int primary key auto_increment,
                       Date_Time datetime,
                       finished boolean
);

CREATE TABLE items(
                      id int primary key auto_increment,
                      product_id int,
                      price double,
                      count int,
                      order_id int not null,
                      constraint fk_orders foreign key (order_id) references orders(id)
);
