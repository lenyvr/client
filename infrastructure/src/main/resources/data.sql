WITH idt_insert AS (
INSERT INTO identification_type (name, description)
VALUES ('DNI','Numero de identificacion nacional')
    RETURNING identification_type_id
    ),
    person_insert AS (
INSERT INTO person (first_name, last_name, age, identification_number, address, postal_code, contact_number, identification_type_id)
SELECT 'Jose', 'Lema', 34, '1234567', 'Otavalo sn y principal', '37890', '098254785'
        , identification_type_id
FROM idt_insert
    RETURNING person_id
    )
INSERT INTO client (client_code, password, status, person_id)
SELECT 'C1', '1234', true, person_id
FROM person_insert
ON CONFLICT DO NOTHING;