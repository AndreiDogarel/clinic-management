create table if not exists clinics (
                                       id bigserial primary key,
                                       name varchar(150) not null,
    address varchar(255),
    phone varchar(40),
    active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
    );

create index if not exists ix_clinics_active on clinics (active);
