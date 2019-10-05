(ns profezzerk.components.delete-student-modal
  (:require
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [profezzerk.actions :as a]))

(defn delete-student-modal [id name]
 (let [modal-state (r/atom false)]
   (fn []
    (let [handle-create #(do (reset! modal-state false)
                             (a/delete-student! id)
                             (.reload (.-location js/window)))]
      (js/console.log "delete-student-modal rendered")
      [sa/Modal {:trigger (r/as-element [sa/Button {:class "red tiny icon"
                                                    :on-click #(reset! modal-state true)}
                                         [:i {:class "trash icon"}]])
                 :open @modal-state}
       [sa/ModalHeader "Are you sure you want to delete " [:span name] " from the roster?"]
       [sa/ModalContent
        [sa/Message {:class "warning"} "This database is NOT immutable, so this cannot be undone!"]
        [sa/ModalDescription
         [sa/Button {:on-click #(reset! modal-state false)}
          "Cancel"]
         [sa/Button {:on-click handle-create
                     :color "red"
                     :type "submit"}
          "Delete"]]]]))))
