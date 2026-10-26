CREATE TABLE shopping_list (
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_shopping_name UNIQUE (name)
);
