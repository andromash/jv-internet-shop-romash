CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8;

CREATE TABLE `internet_shop`.`products`
(
    `product_id` BIGINT          NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(256)    NOT NULL,
    `price`      DOUBLE ZEROFILL NOT NULL,
    `deleted`    TINYINT         NULL DEFAULT 0,
    PRIMARY KEY (`product_id`)
);

CREATE TABLE internet_shop.`users`
(
    `user_id`  BIGINT(11)    NOT NULL AUTO_INCREMENT,
    `name`     VARCHAR(256)  NOT NULL,
    `login`    VARCHAR(256)  NOT NULL,
    `password` VARCHAR(256)  NOT NULL,
    `salt`     VARBINARY(16) NOT NULL,
    `deleted`  TINYINT       NULL DEFAULT 0,
    PRIMARY KEY (`user_id`)
);

CREATE TABLE internet_shop.`roles`
(
    `role_id`   BIGINT(11)   NOT NULL AUTO_INCREMENT,
    `role_name` VARCHAR(256) NOT NULL,
    `deleted`   TINYINT      NULL DEFAULT 0,
    PRIMARY KEY (`role_id`)
);

INSERT INTO `roles`(role_name)
VALUES ('ADMIN');
INSERT INTO `roles`(role_name)
VALUES ('USER');

CREATE TABLE internet_shop.`users_roles`
(
    `id`      BIGINT(11) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(11) NOT NULL,
    `role_id` BIGINT(11) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `user_roles`
        FOREIGN KEY (`user_id`)
            REFERENCES internet_shop.`users` (`user_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `role_roles`
        FOREIGN KEY (`role_id`)
            REFERENCES internet_shop.`roles` (`role_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE internet_shop.`shopping_carts`
(
    `cart_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(11) NOT NULL,
    `deleted` TINYINT    NULL DEFAULT 0,
    PRIMARY KEY (`cart_id`),
    CONSTRAINT `user_cart`
        FOREIGN KEY (`user_id`)
            REFERENCES internet_shop.`users` (`user_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE internet_shop.`shopping_carts_products`
(
    `id`         BIGINT(11) NOT NULL AUTO_INCREMENT,
    `cart_id`    BIGINT(11) NOT NULL,
    `product_id` BIGINT(11) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `cart`
        FOREIGN KEY (`cart_id`)
            REFERENCES internet_shop.`shopping_carts` (`cart_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `product`
        FOREIGN KEY (`product_id`)
            REFERENCES internet_shop.`products` (`product_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE internet_shop.`orders`
(
    `order_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
    `user_id`  BIGINT(11) NOT NULL,
    `deleted`  TINYINT    NULL DEFAULT 0,
    PRIMARY KEY (`order_id`),
    CONSTRAINT `user_order`
        FOREIGN KEY (`user_id`)
            REFERENCES internet_shop.`users` (`user_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE internet_shop.`orders_products`
(
    `id`         BIGINT(11) NOT NULL AUTO_INCREMENT,
    `order_id`   BIGINT(11) NOT NULL,
    `product_id` BIGINT(11) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `order`
        FOREIGN KEY (`order_id`)
            REFERENCES internet_shop.`orders` (`order_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `product`
        FOREIGN KEY (`product_id`)
            REFERENCES internet_shop.`products` (`product_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);
#inserting admin with password = 11111
INSERT INTO users(name, login, password, salt)
VALUES ('testAdmin', 'testadmin',
        'bf3a843c04c994ad4f481615dece5c5f6f1fd0819dec4858a033ca9d1496342d8a50b1d59ccca2fb9d60204d01f1e12a0916e9819a5f4b707957749baed0af41',
        x'7933A240F5D3C575F330FA1141ABE849');
INSERT INTO `internet_shop`.`users_roles`
(
 `user_id`,
 `role_id`)
VALUES
(1, 1);

