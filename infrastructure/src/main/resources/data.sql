WITH idt_insert AS (
    INSERT INTO identification_type (name, description)
    VALUES ('DNI', 'Numero de identificacion nacional')
    ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name
    RETURNING identification_type_id
),
gender_insert AS (
    INSERT INTO gender (name, description)
    VALUES ('F', 'Femenino')
    ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name -- Lo mismo aquí para obtener el gender_id existente
    RETURNING gender_id
),
person_insert AS (
    INSERT INTO person (first_name, last_name, age, identification_number, address, postal_code, contact_number
                     , identification_type_id, gender_id)
    SELECT 'Jose', 'Lema', 34, '1234567', 'Otavalo sn y principal', '37890', '098254785'
          , identification_type_id, gender_id
    FROM idt_insert, gender_insert
    ON CONFLICT (identification_number) DO UPDATE SET identification_number = EXCLUDED.identification_number
    RETURNING person_id
)
INSERT INTO client (client_code, password, status, person_id)
SELECT 'C1', '1234', true, person_id
FROM person_insert
ON CONFLICT (client_code) DO NOTHING;

INSERT INTO identification_type (name, description)
VALUES ('NIF','Numero de identificacion foranea')
ON CONFLICT DO NOTHING;

INSERT INTO identification_type (name, description)
VALUES ('PPT','Pasaporte')
ON CONFLICT DO NOTHING;

INSERT INTO gender (name, description)
VALUES ('M','Masculino')
ON CONFLICT DO NOTHING;


