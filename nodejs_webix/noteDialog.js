function createNoteDialog(notesCollection) {
  return webix.ui({
    view: "window",
    id: "notePopup",
    head: "Note Details",
    position: "center",
    modal: true,
    body: {
      view: "form",
      id: "note_form_popup", // Use a unique ID for the form inside the popup
      width: 400,
      elements: [
        { view: "text", label: "Title", name: "title", required: true },
        {
          view: "textarea",
          label: "Content",
          name: "content",
          height: 100,
        },
        {
          margin: 5,
          cols: [
            {},
            {
              view: "button",
              value: "Cancel",
              click: function () {
                this.getTopParentView().hide(); // Close the popup
              },
            },
            {
              view: "button",
              value: "Save",
              css: "webix_primary",
              click: function () {
                const form = this.getFormView(); // Correctly get the form within the popup
                if (form.validate()) {
                  notesCollection
                    .save(form.getValues())
                    .then(() => {
                      webix.message("Note saved!");
                      this.getTopParentView().hide();
                    })
                    .fail(() => {
                      webix.message({
                        type: "error",
                        text: "Error saving note.",
                      });
                    });
                }
              },
            },
          ],
        },
      ],
      rules: {
        title: webix.rules.isNotEmpty,
      },
    },
  });
}
