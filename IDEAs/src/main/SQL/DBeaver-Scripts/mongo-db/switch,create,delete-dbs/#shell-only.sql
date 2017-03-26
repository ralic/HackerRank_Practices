conn = new Mongo();
db = conn.getDB("npu");
show dbs;
use test;
db.movie.insert({"name":"tutorials point"});
show dbs;
db.movie.find();
db.createCollection("users");
// db.dropDatabase();
show dbs;
db.createCollection("post");
db.products.insert( [
   {
     _id: 1,
     sku: "xyz123",
     description: "hats",
     available: [ { quantity: 25, size: "S" }, { quantity: 50, size: "M" } ],
     _dummy_field: 0
   },
   {
     _id: 2,
     sku: "abc123",
     description: "socks",
     available: [ { quantity: 10, size: "L" } ],
     _dummy_field: 0
   },
   {
     _id: 3,
     sku: "ijk123",
     description: "t-shirts",
     available: [ { quantity: 30, size: "M" }, { quantity: 5, size: "L" } ],
     _dummy_field: 0
   }
] )