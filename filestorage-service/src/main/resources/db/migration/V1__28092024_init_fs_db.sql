CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists files
(
    id           uuid primary key default uuid_generate_v4(),
    private_link varchar(255),
    public_link  varchar(255),
    name         varchar(255),
    created_at   timestamp        default now(),
    extension    varchar(255),
    updated_at   timestamp        default now()
)
