DROP TABLE IF EXISTS Nodes;
DROP TABLE IF EXISTS Edges;

create table Nodes(

 nodeId int auto_increment primary key,
 name varchar(200) unique not null,
 level int CHECK ( level > 0 AND LEVEL <=3),
 holdingweight int
);


create table Edges(

 edge_id int auto_increment primary key,
 parent_node_id int,
 child_node_id int,
 edge_level int,
 holding_quantity int,
 foreign key (parent_node_id) references Nodes(nodeId),
 foreign key (child_node_id) references Nodes(nodeId)
);



