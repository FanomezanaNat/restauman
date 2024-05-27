CREATE TABLE "ingredient_menu"(
    id INT PRIMARY KEY ,
    id_menu INT REFERENCES "menu"(id),
    id_ingredient_template INT REFERENCES "ingredient_template"(id),
    quantity double precision
);
