const service = require("../services/notes.service");

const getAll = async (req, res, next) => {
  try {
    const notes = await service.findAll(req.query);
    res.json({ data: notes });
  } catch (error) {
    next(error);
  }
};

const getById = async (req, res, next) => {
  try {
    const note = await service.findById(req.params.id);
    if (!note) {
      return res.status(404).json({
        error: { message: "Note not found", code: "NOT_FOUND" },
      });
    }
    res.json({ data: note });
  } catch (error) {
    next(error);
  }
};

const create = async (req, res, next) => {
  try {
    const newNote = await service.create(req.body);
    res.status(201).json({ data: newNote });
  } catch (error) {
    next(error);
  }
};

const update = async (req, res, next) => {
  try {
    const updatedNote = await service.update(req.params.id, req.body);
    if (!updatedNote) {
      return res.status(404).json({
        error: { message: "Note not found", code: "NOT_FOUND" },
      });
    }
    res.json({ data: updatedNote });
  } catch (error) {
    next(error);
  }
};

const remove = async (req, res, next) => {
  try {
    const deleted = await service.remove(req.params.id);
    if (!deleted) {
      return res.status(404).json({
        error: { message: "Note not found", code: "NOT_FOUND" },
      });
    }
    res.status(204).send();
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getAll,
  getById,
  create,
  update,
  remove,
};
