create table ordered (
id bigint not null auto_increment,
subtotal decimal(10,2) not null,
shipping_fee decimal(10,2) not null,
value_total decimal(10,2) not null,

restaurant_id bigint not null,
user_client_id bigint not null,
payment_method_id bigint not null,

address_city_id bigint(20) not null,
address_cep varchar(9) not null,
address_street varchar(100) not null,
address_number varchar(20) not null,
address_complement varchar(60) null,
address_neighborhood varchar(60) not null,

status varchar(10) not null,
date_creation datetime not null,
date_confirmation datetime null,
date_cancellation datetime null,
date_delivery datetime null,

primary key (id),

constraint fk_ordered_address_city foreign key (address_city_id) references city (id),
constraint fk_ordered_restaurant foreign key (restaurant_id) references restaurant (id),
constraint fk_ordered_user_client foreign key (user_client_id) references user (id),
constraint fk_ordered_payment_method foreign key (payment_method_id) references payment_method (id)
) engine=InnoDB default charset=utf8;

create table order_item (
     id bigint not null auto_increment,
     amount smallint(6) not null,
     value_unitary decimal(10,2) not null,
     value_total decimal(10,2) not null,
     observation varchar(255) null,
     ordered_id bigint not null,
     product_id bigint not null,

     primary key (id),
     unique key uk_item_order_product (ordered_id, product_id),

     constraint fk_item_ordered_ordered foreign key (ordered_id) references ordered (id),
     constraint fk_item_ordered_product foreign key (product_id) references product (id)
) engine=InnoDB default charset=utf8;