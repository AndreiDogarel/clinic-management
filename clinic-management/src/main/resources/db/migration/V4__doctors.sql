create table if not exists doctors (
                                       id bigserial primary key,
                                       clinic_id bigint not null,
                                       first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(255) not null,
    specialization varchar(120) not null,
    phone varchar(40),
    active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint fk_doctors_clinic foreign key (clinic_id) references clinics(id)
    );

create unique index if not exists ux_doctors_email on doctors (lower(email));
create index if not exists ix_doctors_clinic on doctors (clinic_id);
create index if not exists ix_doctors_active on doctors (active);
