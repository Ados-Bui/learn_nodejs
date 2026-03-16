const express = require("express");
const morgan = require("morgan");
const cors = require("cors");
const notesRouter = require("./routes/notes.routes");
const notFound = require("./middlewares/notFound");
const errorHandler = require("./middlewares/errorHandler");

const app = express();

// Middlewares
app.use(cors());
app.use(morgan("dev"));
app.use(express.json());

// Routes
app.get("/health", (_req, res) => res.json({ status: "ok" }));
app.use("/api/notes", notesRouter);

// Error Handling
app.use(notFound);
app.use(errorHandler);

module.exports = app;
