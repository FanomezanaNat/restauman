CREATE TYPE  movement_type AS ENUM('Income','Outcome');

CREATE TABLE "movement"(
    id SERIAL PRIMARY KEY ,
    id_ingredient_menu INT REFERENCES ingredient_menu(id),
    id_restaurant INT REFERENCES restaurant(id),
    movement_datetime TIMESTAMP DEFAULT NOW(),
    type movement_type,
    quantity DOUBLE PRECISION
);
