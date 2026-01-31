
CREATE TABLE employee (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) UNIQUE,
                          phone VARCHAR(20),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE app_user (
                          id BIGSERIAL PRIMARY KEY,
                          username VARCHAR(255) UNIQUE NOT NULL,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(50) NOT NULL DEFAULT 'CLIENT',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE service (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         duration_minutes INT NOT NULL,
                         price DOUBLE PRECISION NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE employee_service (
                                  employee_service_id BIGSERIAL PRIMARY KEY,
                                  employee_id BIGINT REFERENCES employee(id) ON DELETE CASCADE,
                                  service_id BIGINT REFERENCES service(id) ON DELETE CASCADE
);


CREATE TABLE employee_schedule (
                                   id BIGSERIAL PRIMARY KEY,
                                   employee_id BIGINT REFERENCES employee(id) ON DELETE CASCADE,
                                   day_of_week INT NOT NULL,
                                   start_time TIME NOT NULL,
                                   end_time TIME NOT NULL,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE employee_time_off (
                                   id BIGSERIAL PRIMARY KEY,
                                   employee_id BIGINT REFERENCES employee(id) ON DELETE CASCADE,
                                   start_datetime TIMESTAMP NOT NULL,
                                   end_datetime TIMESTAMP NOT NULL,
                                   reason VARCHAR(255),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE appointment (
                             id BIGSERIAL PRIMARY KEY,
                             employee_id BIGINT NOT NULL REFERENCES employee(id) ON DELETE CASCADE,
                             service_id BIGINT NOT NULL REFERENCES service(id) ON DELETE CASCADE,
                             user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
                             start_datetime TIMESTAMP NOT NULL,
                             end_datetime TIMESTAMP NOT NULL,
                             status VARCHAR(50) DEFAULT 'SCHEDULED',
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
