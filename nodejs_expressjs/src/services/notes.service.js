const { poolPromise, sql } = require("../config/db");

const findAll = async (query) => {
  const pool = await poolPromise;
  const request = pool.request();
  if (query.q) {
    request.input("searchTerm", sql.NVarChar, `%${query.q}%`);
    const result = await request.query(
      "SELECT * FROM Notes WHERE title LIKE @searchTerm",
    );
    return result.recordset;
  }
  const result = await request.query("SELECT * FROM Notes");
  return result.recordset;
};

const findById = async (id) => {
  const pool = await poolPromise;
  const result = await pool
    .request()
    .input("id", sql.Int, id)
    .query("SELECT * FROM Notes WHERE id = @id");
  return result.recordset[0];
};

const create = async (noteData) => {
  const pool = await poolPromise;
  const result = await pool
    .request()
    .input("title", sql.NVarChar, noteData.title)
    .input("content", sql.NVarChar, noteData.content || "")
    .query(
      "INSERT INTO Notes (title, content) OUTPUT INSERTED.* VALUES (@title, @content)",
    );
  return result.recordset[0];
};

const update = async (id, noteData) => {
  const pool = await poolPromise;
  const result = await pool
    .request()
    .input("id", sql.Int, id)
    .input("title", sql.NVarChar, noteData.title)
    .input("content", sql.NVarChar, noteData.content)
    .input("updatedAt", sql.DateTime, new Date())
    .query(
      "UPDATE Notes SET title = @title, content = @content, updatedAt = @updatedAt WHERE id = @id; SELECT * FROM Notes WHERE id = @id",
    );
  return result.recordset[0];
};

const remove = async (id) => {
  const pool = await poolPromise;
  const result = await pool
    .request()
    .input("id", sql.Int, id)
    .query("DELETE FROM Notes WHERE id = @id");
  return result.rowsAffected[0] > 0;
};

module.exports = {
  findAll,
  findById,
  create,
  update,
  remove,
};
