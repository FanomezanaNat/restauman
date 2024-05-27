CREATE TABLE "ingredient_template"(
    id INT  PRIMARY KEY  ,
    name VARCHAR not null ,
    unit_price DOUBLE PRECISION NOT NULL ,
    id_unit INT REFERENCES "unit"(id)
);
