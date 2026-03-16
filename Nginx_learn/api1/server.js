const express = require("express");
const app = express();

app.get("/api", (req, res) => {
  res.json({
    server: "API SERVER 1",
  });
});

app.listen(3000, () => {
  console.log("API1 running");
});
