(ns profezzerk.components.update-student-modal
  (:require
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [profezzerk.actions :as a]))

(defn update-student-modal [id name description]
 (let [modal-state (r/atom false)]
   (fn [id name description]
    (js/console.log "update-student-modal rendered")
    [sa/Modal {:trigger (r/as-element [sa/Button {:class "tiny icon"
                                                  :on-click #(reset! modal-state true)}
                                       [:i {:class "pencil alternate icon"}]])
               :open @modal-state}
     [sa/ModalHeader "Edit Student"]
     [sa/ModalContent
      [sa/ModalDescription
       [sa/Header "Please complete edits."]
       (let [new-name (r/atom nil)
             new-description (r/atom nil)
             handle-create #(do (reset! modal-state false)
                                (a/update-student! id @new-name @new-description)
                                (.reload (.-location js/window)))
             get-value #(-> % (.-target) (.-value))]
        [sa/Form
         [sa/FormField
          [:label "First & Last Name"]
          [:input {:id "name-input"
                   :placeholder "First & Last Name"
                   :default-value name
                   :on-change #(reset! new-name (get-value %))}]]
         [sa/FormField
          [:label "Notes"]
          [:input {:id "description-input"
                   :placeholder "Notes"
                   :default-value description
                   :on-change #(reset! new-description (get-value %))}]]
         [sa/Button {:on-click #(reset! modal-state false)}
          "Cancel"]
         [sa/Button {:on-click handle-create
                     :primary true
                     :type "submit"}
          "Update Student"]])]]])))