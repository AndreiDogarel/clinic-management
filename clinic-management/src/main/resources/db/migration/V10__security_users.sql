create table if not exists users (
                                     id bigserial primary key,
                                     email varchar(255) not null,
    password_hash varchar(255) not null,
    active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
    );

create unique index if not exists ux_users_email on users (lower(email));
