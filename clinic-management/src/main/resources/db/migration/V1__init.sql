create table if not exists patients (
                                        id bigserial primary key,
                                        first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(255) not null,
    phone varchar(40),
    birth_date date,
    active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
    );

create unique index if not exists ux_patients_email on patients (lower(email));
