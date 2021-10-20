CREATE TABLE IF NOT EXISTS price
(
    id         LONG        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    currency   VARCHAR(50) NOT NULL,
    price      DECIMAL     NOT NULL,
    vehicle_id LONG        NOT NULL
);