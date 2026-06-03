CREATE TABLE if not exists  public.identification_type (
    identification_type_id serial NOT NULL,
    name varchar(10) NOT NULL,
    description varchar(50) NOT NULL,
    CONSTRAINT identification_type_unique UNIQUE (identification_type_id)
);

CREATE TABLE if not exists public.person (
   person_id serial NOT NULL,
   first_name varchar(20) NOT NULL,
   last_name varchar(40) NOT NULL,
   age integer NOT NULL,
   identification_number varchar NOT NULL,
   address varchar(50) NOT NULL,
   postal_code varchar(10) NOT NULL,
   contact_number varchar(15) NOT NULL,
   identification_type_id integer NOT NULL,
   CONSTRAINT person_unique UNIQUE (person_id),
   CONSTRAINT person_identification_type_fk FOREIGN KEY (identification_type_id) REFERENCES public.identification_type(identification_type_id)
);

CREATE TABLE if not exists public.client (
   person_id int4 NOT NULL,
   client_id serial4 NOT NULL,
   client_code varchar(10) NOT NULL,
   password varchar(100) NOT NULL,
   status bool NOT NULL,
   CONSTRAINT client_pk PRIMARY KEY (client_id),
   CONSTRAINT client_unique UNIQUE (client_code),
   CONSTRAINT client_person_fk FOREIGN KEY (person_id) REFERENCES public.person(person_id)
);
