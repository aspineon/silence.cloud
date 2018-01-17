INSERT INTO status(id, name, created_at, updated_at) VALUES (1, "active", NOW(), NOW());
INSERT INTO status(id, name, created_at, updated_at) VALUES (2, "inactive", NOW(), NOW());
INSERT INTO status(id, name, created_at, updated_at) VALUES (3, "frozen", NOW(), NOW());
INSERT INTO status(id, name, created_at, updated_at) VALUES (4, "banned", NOW(), NOW());
INSERT INTO status(id, name, created_at, updated_at) VALUES (5, "blocked", NOW(), NOW());
INSERT INTO status(id, name, created_at, updated_at) VALUES (6, "online", NOW(), NOW());
INSERT INTO status(id, name, created_at, updated_at) VALUES (7, "offline", NOW(), NOW());

INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (1, "office", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (2, "partners", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (3, "counsel", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (4, "practise", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (5, "juniorAssociate", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (6, "associate", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (7, "seniorAssociate", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (8, "auditors", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (9, "marketing", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (10, "sales", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (11, "technical", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (12, "it", 1, NOW(), NOW());
INSERT INTO role (id, name, status_id, created_at, updated_at) VALUES (13, "hr", 1, NOW(), NOW());

INSERT INTO user(id, username, email, phone, password, is_admin, created_at, updated_at) VALUES (1, "John Doe", "john@doe.com", "000000000", "[B@4db0ba1c", NOW(), NOW());
INSERT INTO user(id, username, email, phone, password, is_admin, created_at, updated_at) VALUES (1, "John Smith", "john@smith.com", "111111111", "[B@4db0ba1c", 512), NOW(), NOW());