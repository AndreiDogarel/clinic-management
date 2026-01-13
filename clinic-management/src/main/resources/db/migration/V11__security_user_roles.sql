create table if not exists user_roles (
                                          user_id bigint not null,
                                          role varchar(30) not null,
    created_at timestamptz not null default now(),
    constraint fk_user_roles_user foreign key (user_id) references users(id) on delete cascade,
    constraint pk_user_roles primary key (user_id, role)
    );

create index if not exists ix_user_roles_role on user_roles (role);
