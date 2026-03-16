// eslint-disable-next-line no-unused-vars
const errorHandler = (err, req, res, next) => {
  console.error(err.stack);

  const statusCode = res.statusCode === 200 ? 500 : res.statusCode;

  res.status(statusCode).json({
    error: {
      message: err.message || "Something went wrong!",
      code: err.code || "INTERNAL_SERVER_ERROR",
    },
  });
};

module.exports = errorHandler;
