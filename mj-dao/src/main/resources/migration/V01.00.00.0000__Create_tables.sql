create table messages (
                          message_id varchar(255) not null,
                          version integer,
                          author_id varchar(255) not null,
                          recipient_id varchar(255) not null,
                          content varchar(500) not null,
                          sentAt timestamp not null,
                          primary key (message_id)
);

create table users (
                       user_id varchar(255) not null,
                       version integer,
                       email varchar(20) not null,
                       username varchar(20) not null,
                       password varchar(20) not null,
                       primary key (user_id)
);

alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

alter table messages
    add constraint FKowtlim26svclkatusptbgi7u1
        foreign key (author_id)
            references users(user_id);

alter table messages
    add constraint FKhdkwfnspwb3s60j27vpg0rpg6
        foreign key (recipient_id)
            references users(user_id);
