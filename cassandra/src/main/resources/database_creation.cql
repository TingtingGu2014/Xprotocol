CREATE KEYSPACE IF NOT EXISTS xprotocol WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

use xprotocol;

CREATE TABLE IF NOT EXISTS xprotocol.user_details (

    user_details_id timeuuid,

    user_id bigint,

    address text,

    state varchar,

    city varchar,

    zipcode varchar,

    PRIMARY KEY (user_details_id)

) ;

CREATE TABLE IF NOT EXISTS xprotocol.user_protocol (

    user_protocol_uuid timeuuid,

    user_uuid timeuuid,

    project_uuids set<timeuuid>,

    project_titles set<varchar>,

    title varchar,

    body text,

    files set<varchar>,

    comments map<varchar, text>,

    versions set<varchar>,

    keywords set<varchar>,

    PRIMARY KEY (user_uuid, user_protocol_uuid)

) WITH CLUSTERING ORDER BY (user_protocol_uuid DESC); 


CREATE TABLE IF NOT EXISTS xprotocol.user_project (

    user_project_uuid timeuuid,

    user_uuid timeuuid,

    protocol_uuids set<timeuuid>,

    protocol_titles set<varchar>,

    title varchar,

    description text,

    keywords set<varchar>,

    PRIMARY KEY (user_uuid, user_project_uuid)

) WITH CLUSTERING ORDER BY (user_project_uuid DESC); 


CREATE TABLE IF NOT EXISTS xprotocol.comment (

    comment_uuid timeuuid,

    user_uuid timeuuid,

    path varchar,

    content text,

    protocol_uuid timeuuid,

    protocol_user_uuid timeuuid,

    protocol_title varchar,

    PRIMARY KEY (user_uuid, comment_uuid)

) WITH CLUSTERING ORDER BY (comment_uuid DESC);


CREATE TABLE IF NOT EXISTS xprotocol.protocol_to_user (

    protocol_uuid timeuuid,

    user_uuid timeuuid,

    title text,

    PRIMARY KEY (protocol_uuid)

); 


CREATE TABLE IF NOT EXISTS xprotocol.project_by_user (

    user_project_uuid timeuuid,

    user_uuid timeuuid,

    title text,

    PRIMARY KEY (user_project_uuid)

);


CREATE TABLE IF NOT EXISTS xprotocol.comment_by_user (

    user_comment_uuid timeuuid,

    user_uuid timeuuid,

    path text,

    PRIMARY KEY (user_comment_uuid)

); 