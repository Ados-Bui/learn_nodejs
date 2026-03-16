const express = require("express");
const app = express();

app.get("/api", (req, res) => {
  res.json({
    server: "API SERVER 2",
  });
});

app.listen(3000, () => {
  console.log("API2 running");
});