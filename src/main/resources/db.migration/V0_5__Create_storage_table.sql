CREATE TABLE "storage"(
    id SERIAL PRIMARY KEY ,
    id_restaurant INT REFERENCES "restaurant"(id),
    id_ingredient_template INT REFERENCES "ingredient_template"(id),
    value DOUBLE PRECISION NOT NULL ,
    supply_date TIMESTAMP DEFAULT NOW()
);
