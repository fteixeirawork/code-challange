CREATE TABLE ticket (
    id UUID PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    status VARCHAR(50),
    user_id UUID NOT NULL,
    assignee_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
