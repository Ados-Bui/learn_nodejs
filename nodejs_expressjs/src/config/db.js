const sql = require("mssql/msnodesqlv8");

const config = {
  server: "KHANHLAPTOP\\MSSQLSERVER1",
  database: "nodetest",
  driver: "msnodesqlv8",
  options: {
    trustedConnection: true,
    trustServerCertificate: true,
    encrypt: false,
  },
};

const poolPromise = new sql.ConnectionPool(config)
  .connect()
  .then(async (pool) => {
    console.log(
      "Connected to SQL Server (KHANHLAPTOP\\MSSQLSERVER1) database: nodetest",
    );

    // Check if Notes table exists and create it if it does not
    const tableCheckRequest = pool.request();
    const tableCheckResult = await tableCheckRequest.query`
      IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Notes]') AND type in (N'U'))
      CREATE TABLE [dbo].[Notes] (
        [id] INT PRIMARY KEY IDENTITY(1,1),
        [title] NVARCHAR(255) NOT NULL,
        [content] NVARCHAR(MAX),
        [createdAt] DATETIME DEFAULT GETDATE(),
        [updatedAt] DATETIME DEFAULT GETDATE()
      )
    `;
    if (tableCheckResult.rowsAffected.length > 0) {
      console.log('"Notes" table created successfully.');
    }

    return pool;
  })
  .catch((err) => {
    console.error("Database connection failed: ", err);
    process.exit(1);
  });

module.exports = {
  sql,
  poolPromise,
};
