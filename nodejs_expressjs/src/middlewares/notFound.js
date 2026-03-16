const notFound = (req, res, next) => {
  res.status(404).json({
    error: {
      message: `Not Found - ${req.originalUrl}`,
      code: "NOT_FOUND",
    },
  });
};

module.exports = notFound;
