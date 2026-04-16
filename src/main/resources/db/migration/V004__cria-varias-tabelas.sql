create table payment_method (
 id bigint not null auto_increment,
 description varchar(60) not null,
 primary key (id)
) engine=InnoDB default charset=utf8;

create table grupo (
 id bigint not null auto_increment,
 name varchar(60) not null,

 primary key (id)
) engine=InnoDB default charset=utf8;

create table grupo_permission (
 grupo_id bigint not null,
 permission_id bigint not null,

 primary key (grupo_id, permission_id)
) engine=InnoDB default charset=utf8;

create table permission (
 id bigint not null auto_increment,
 description varchar(60) not null,
 name varchar(100) not null,

 primary key (id)
) engine=InnoDB default charset=utf8;

create table product (
 id bigint not null auto_increment,
 restaurant_id bigint not null,
 name varchar(80) not null,
 description text not null,
 value decimal(10,2) not null,
 active tinyint(1) not null,

 primary key (id)
) engine=InnoDB default charset=utf8;

create table restaurant (
 id bigint not null auto_increment,
 kitchen_id bigint not null,
 name varchar(80) not null,
 shipping_fee decimal(10,2) not null,
 date_update datetime not null,
 date_register datetime not null,

 address_city_id bigint,
 address_cep varchar(9),
 address_street varchar(100),
 address_number varchar(20),
 address_complement varchar(60),
 address_neighborhood varchar(60),

 primary key (id)
) engine=InnoDB default charset=utf8;

create table restaurant_payment_method (
 restaurant_id bigint not null,
 payment_method_id bigint not null,

 primary key (restaurant_id, payment_method_id)
) engine=InnoDB default charset=utf8;

create table user (
 id bigint not null auto_increment,
 name varchar(80) not null,
 email varchar(255) not null,
 password varchar(255) not null,
 date_register datetime not null,

 primary key (id)
) engine=InnoDB default charset=utf8;

create table user_grupo (
 user_id bigint not null,
 grupo_id bigint not null,

 primary key (user_id, grupo_id)
) engine=InnoDB default charset=utf8;

alter table grupo_permission add constraint fk_grupo_permission_permission
 foreign key (permission_id) references permission (id);

alter table grupo_permission add constraint fk_grupo_permission_grupo
 foreign key (grupo_id) references grupo (id);

alter table product add constraint fk_product_restaurant
 foreign key (restaurant_id) references restaurant (id);

alter table restaurant add constraint fk_restaurant_kitchen
 foreign key (kitchen_id) references kitchen (id);

alter table restaurant add constraint fk_restaurant_city
 foreign key (address_city_id) references city (id);

alter table restaurant_payment_method add constraint fk_rest_method_pay_method_pay
 foreign key (payment_method_id) references payment_method (id);

alter table restaurant_payment_method add constraint fk_rest_method_pay_restaurant
 foreign key (restaurant_id) references restaurant (id);

alter table user_grupo add constraint fk_user_grupo_grupo
 foreign key (grupo_id) references grupo (id);

alter table user_grupo add constraint fk_user_grupo_user
 foreign key (user_id) references user (id);