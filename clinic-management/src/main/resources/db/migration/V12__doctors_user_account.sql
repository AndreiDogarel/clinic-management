alter table doctors add column if not exists user_id bigint;
alter table doctors add constraint fk_doctors_user foreign key (user_id) references users(id);
create unique index if not exists ux_doctors_user on doctors(user_id) where user_id is not null;
