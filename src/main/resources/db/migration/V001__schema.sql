CREATE TABLE incident(
    id serial NOT NULL PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(255),
    happened_on timestamp,
    origin VARCHAR(255),
    status VARCHAR(255)
);

CREATE TABLE report_item(
    incident INT,
    incident_key INT,
    type VARCHAR(255),
    date timestamp,
    description VARCHAR(255),
    FOREIGN KEY(incident) REFERENCES incident(id)
);

CREATE TABLE infrastructure_component(
    id serial NOT NULL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    description VARCHAR(255),
    check_uri VARCHAR(255),
    check_token VARCHAR(255),
    availability VARCHAR(255),
    last_checked_on timestamp
);

CREATE TABLE outage(
    infrastructure_component INT,
    infrastructure_component_key INT,
    availability VARCHAR(255),
    started_on timestamp,
    ended_on timestamp,
    FOREIGN KEY(infrastructure_component) REFERENCES infrastructure_component(id)
);
