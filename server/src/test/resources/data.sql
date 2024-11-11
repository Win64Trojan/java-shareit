INSERT INTO users (name,email) VALUES ('Алексей','user1@mail.ru');
INSERT INTO users (name,email) VALUES ('Алена','user2@mail.ru');
INSERT INTO users (name,email) VALUES ('Миша','user3@mail.ru');
INSERT INTO users (name,email) VALUES ('Егор','user4@mail.ru');

INSERT INTO items (name,description,available,owner_id) VALUES ('Спальный мешок','Спальный мешок для похода',false,1);
INSERT INTO items (name,description,available,owner_id) VALUES ('Дрель','Дрель для ремонта',true,2);
INSERT INTO items (name,description,available,owner_id) VALUES ('Книга Овод','Книга Овод',true,2);
INSERT INTO items (name,description,available,owner_id) VALUES ('Санки','санки детские',false,4);

INSERT INTO bookngs (booker_id,item_id,status) VALUES (1,2,'WAITING');
INSERT INTO bookngs (booker_id,item_id,status,start_date,end_date) VALUES (2,1,'APPROVED','2024-10-27 20:09:19.000','2024-10-28 20:09:19.000');
INSERT INTO bookngs (booker_id,item_id,status,start_date,end_date) VALUES (3,4,'APPROVED','2024-10-27 20:09:19.000','2024-11-28 20:09:19.000');
INSERT INTO bookngs (booker_id,item_id,status,start_date,end_date) VALUES (4,1,'APPROVED','2025-10-27 20:09:19.000','2025-11-28 20:09:19.000');