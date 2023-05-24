INSERT INTO blog.users(first_name, last_name, username, email, password, checked)
VALUES ('Rodrigo', 'Carvalho', 'duckdeveloper', 'rodrigocarvalhodev@gmail.com', '$2a$12$dX80rMOJLiUOU.wUtgoFgu5sT8MeMWhqS.SaIUhOTqQYN4a16Yhvy', true);

INSERT INTO blog.users_roles(user_id, role_id)
VALUES
    (1, 1),
    (1, 2);