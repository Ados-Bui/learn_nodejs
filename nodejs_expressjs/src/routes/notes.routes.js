const router = require("express").Router();
const controller = require("../controllers/notes.controller");
const validate = require("../middlewares/validate");

router.get("/", controller.getAll);
router.get("/:id", controller.getById);
router.post("/", validate.note, controller.create);
router.put("/:id", validate.note, controller.update);
router.delete("/:id", controller.remove);

module.exports = router;
