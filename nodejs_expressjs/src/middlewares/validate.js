const note = (req, res, next) => {
  const { title } = req.body;
  if (!title || title.trim() === "") {
    return res.status(400).json({
      error: { message: "Title is required", code: "VALIDATION_ERROR" },
    });
  }
  next();
};

module.exports = {
  note,
};
