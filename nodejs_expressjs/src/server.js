const app = require("./app");
const { poolPromise } = require("./config/db");

const PORT = process.env.PORT || 3000;

poolPromise.then(() => {
  app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
  });
}).catch(err => {
  console.error("Failed to start server due to database connection error:", err);
});
