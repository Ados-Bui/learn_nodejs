const API_BASE = "http://localhost:3000/api/notes";

function createNoteDialog(notesCollection) {
  return webix.ui({
    view: "window",
    id: "notePopup",
    head: "Note Details",
    position: "center",
    modal: true,
    body: {
      view: "form",
      id: "note_form_popup",
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
                this.getTopParentView().hide();
              },
            },
            {
              view: "button",
              value: "Save",
              css: "webix_primary",
              click: function () {
                const form = this.getFormView();
                if (form.validate()) {
                  const values = form.getValues();
                  const popup = this.getTopParentView();

                  // Chỉ dùng id nếu nó là số hợp lệ (trường hợp edit)
                  const isEdit = values.id && parseInt(values.id) > 0;
                  const method = isEdit ? "PUT" : "POST";
                  const url = isEdit
                    ? `${API_BASE}/${values.id}`
                    : API_BASE;

                  // Chỉ gửi title và content (không gửi id khi tạo mới)
                  const body = { title: values.title, content: values.content || "" };

                  fetch(url, {
                    method: method,
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(body),
                  })
                    .then((res) => {
                      if (!res.ok) {
                        return res.json().then((err) => {
                          throw new Error(
                            err.error?.message || "Server error: " + res.status
                          );
                        });
                      }
                      return res.json();
                    })
                    .then((data) => {
                      webix.message("Note saved!");
                      popup.hide();
                      // Reload lại dữ liệu từ server để bảng hiển thị đúng
                      notesCollection.clearAll();
                      notesCollection.load(API_BASE);
                    })
                    .catch((err) =>
                      webix.message({ text: "Error: " + err.message, type: "error" })
                    );
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
