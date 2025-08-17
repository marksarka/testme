const express = require("express");
const { MongoClient, ObjectId, ServerApiVersion } = require("mongodb");
const mongoSanitize = require("mongo-sanitize");

const app = express();
const url = "mongodb://localhost:27017";
const dbName = "testdb";

// Setup MongoDB connection
let db;
MongoClient.connect(url, { useUnifiedTopology: true }, (err, client) => {
  if (err) throw err;
  console.log("Connected to MongoDB");
  db = client.db(dbName);
});

// Another way to connect to MongoDB
const client = new MongoClient(url, { serverApi: ServerApiVersion.v1 });

app.get("/fruit/add/:name", async (req, res) => {
  const fruit = req.params.name;

  try {
    const myDB = client.db("myDB");
    const myColl = myDB.collection("fruits");

    // ok: mongodb-express
    await myColl.insertMany([
      { _id: 1, name: fruit, qty: 5, rating: 3 },
      { _id: 2, name: fruit, qty: 7, rating: 1, color: "yellow" },
    ]);

    res.send("Fruits added successfully!");
  } catch (err) {
    res.status(500).send("Error adding fruits: " + err.message);
  }
});

app.get("/fruit/update-color/:id/:color", async (req, res) => {
  const fruitId = req.body.id;
  const newColor = req.query.color;

  try {
    await client.withSession(async (session) => {
      const myDB = client.db("myDB");
      const myColl = myDB.collection("fruits");

      session.startTransaction(); // Start a transaction

      // Vulnerable to NoSQL injection due to unvalidated input
      // proruleid: mongodb-express
      const updateResult = await myColl.updateOne(
        { _id: fruitId }, // Potentially vulnerable query parameter
        { $set: { color: newColor } }, // Setting color with user-supplied value
        { session },
      );

      // Additional queries vulnerable to NoSQL injection

      // Vulnerable to NoSQL injection
      // proruleid: mongodb-express
      const aggregateResult = await myColl
        .aggregate([
          { $match: { _id: fruitId } },
          { $addFields: { color: newColor } },
        ])
        .toArray();

      // Vulnerable to NoSQL injection
      // proruleid: mongodb-express
      const bulkWriteResult = await myColl.bulkWrite(
        [
          {
            updateOne: {
              filter: { _id: fruitId },
              update: { $set: { color: newColor } },
            },
          },
          {
            updateOne: {
              filter: { _id: fruitId },
              update: { $set: { lastUpdated: new Date() } },
            },
          },
        ],
        { session },
      );

      // Vulnerable to NoSQL injection
      // proruleid: mongodb-express
      const updateManyResult = await myColl.updateMany(
        { _id: fruitId },
        { $set: { color: newColor } },
        { session },
      );

      // Vulnerable to NoSQL injection
      // proruleid: mongodb-express
      const findAndModifyResult = await myColl.findOneAndUpdate(
        { _id: fruitId },
        { $set: { color: newColor } },
        { returnOriginal: false, session },
      );

      res.send("Fruit color updated and additional queries executed!");
    });
  } catch (err) {
    res.status(500).send("Error during fruit update: " + err.message);
  }
});

app.get("/fruit/update-color/:id/:color", async (req, res) => {
  const fruitId = req.query.id;
  const newColor = req.body.color;

  try {
    await client.withSession(async (session) => {
      const myDB = client.db("myDB");
      const myColl = myDB.collection("fruits");

      session.startTransaction(); // Start a transaction

      // Vulnerable to NoSQL injection due to unvalidated input
      // proruleid: mongodb-express
      const updateResult = await myColl.updateOne(
        { _id: fruitId }, // Potentially vulnerable query parameter
        { $set: { color: newColor } }, // Setting color with user-supplied value
        { session },
      );
    });
  } catch (err) {
    res.status(500).send("Error during fruit update: " + err.message);
  }
});

// Vulnerable route
app.get("/user/:id", (req, res) => {
  const userId = req.query.id;

  // Vulnerable to NoSQL injection
  const query = { _id: userId };

  // proruleid: mongodb-express
  db.collection("users").findOne(query, (err, user) => {
    res.json(user);
  });
});

app.get("/user2/:id", async (req, res) => {
  const userId = req.query.id;

  try {
    const safeUserId = new ObjectId(userId); // Convert to ObjectId
    const safeQuery = { _id: safeUserId };

    // ok: mongodb-express
    const user = await db.collection("users").findOne(safeQuery);

    // ok: mongodb-express
    const user2 = await db
      .collection("users")
      .find({ $where: `this.userId === '${userId}'` });
    res.json(user);
  } catch (err) {
    res.status(400).send("Invalid ID format");
  }

  const sanitizedUserId = mongoSanitize(userId);
  const sanitizedQuery = { _id: sanitizedUserId };

  // ok: mongodb-express
  const sanitizedUser = await db.collection("users").findOne(sanitizedQuery);
  res.json(sanitizedUser);
});

app.listen(4000, () => console.log("Server running on port 4000"));
