CREATE KEYSPACE cqldb
WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};

USE cqldb;