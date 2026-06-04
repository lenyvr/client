WITH idt_insert AS (
INSERT INTO identification_type (name, description)
VALUES ('DNI','Numero de identificacion nacional')
        RETURNING identification_type_id
),
gender_insert AS (
INSERT INTO gender (name, description)
VALUES ('F','Femenino')
    RETURNING gender_id
),
person_insert AS (
INSERT INTO person (first_name, last_name, age, identification_number, address, postal_code, contact_number, identification_type_id, gender_id)
SELECT 'Jose', 'Lema', 34, '1234567', 'Otavalo sn y principal', '37890', '098254785'
        , identification_type_id, gender_id
FROM idt_insert, gender_insert
    RETURNING person_id
)
INSERT INTO client (client_code, password, status, person_id)
SELECT 'C1', '1234', true, person_id
FROM person_insert
ON CONFLICT DO NOTHING;

INSERT INTO identification_type (name, description)
VALUES ('NIF','Numero de identificacion foranea')
ON CONFLICT DO NOTHING;

INSERT INTO identification_type (name, description)
VALUES ('PPT','Pasaporte')
ON CONFLICT DO NOTHING;

INSERT INTO gender (name, description)
VALUES ('M','Masculino')
ON CONFLICT DO NOTHING;


