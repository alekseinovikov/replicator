CREATE TABLE task_definition(
 id INT NOT NULL PRIMARY KEY,
 name VARCHAR(255) NOT NULL,
 description VARCHAR(1024) NULL,
 sync_uuid VARCHAR(36) NULL,
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS ix_task_definition_search ON task_definition(name, description);
CREATE INDEX IF NOT EXISTS ix_task_definition_sync ON task_definition(sync_uuid);


CREATE TABLE task_definition_mirror(
 id INT NOT NULL PRIMARY KEY,
 name VARCHAR(255) NOT NULL,
 description VARCHAR(1024) NULL,
 sync_uuid VARCHAR(36) NULL,
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS ix_task_definition_mirror_search ON task_definition_mirror(name, description);
CREATE INDEX IF NOT EXISTS ix_task_definition_mirror_sync ON task_definition_mirror(sync_uuid);