(ns profezzerk.components.delete-student-modal
  (:require
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [profezzerk.actions :as a]))

(defn delete-student-modal [id name fetch-data!]
 (let [modal-state (r/atom false)]
   (fn []
    (let [handle-delete #(do (reset! modal-state false)
                             (a/delete-student! id fetch-data!))]
      [sa/Modal {:trigger (r/as-element [sa/Button {:class "red tiny icon"
                                                    :on-click #(reset! modal-state true)}
                                         [:i {:class "trash icon"}]])
                 :open @modal-state}
       [sa/ModalHeader "Are you sure you want to delete " [:span name] " from the roster?"]
       [sa/ModalContent
        [sa/Message {:class "warning"} "This database is NOT immutable, so this cannot be undone! ;)"]
        [sa/ModalDescription
         [sa/Form {:on-submit (fn [e] (.preventDefault e)
                                      (handle-delete))}
          [sa/Button {:type "button"
                      :on-click #(reset! modal-state false)}
           "Cancel"]
          [sa/Button {:color "red"
                      :type "submit"}
           "Delete"]]]]]))))
