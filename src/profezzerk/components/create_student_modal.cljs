(ns profezzerk.components.create-student-modal
  (:require
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [profezzerk.actions :as a]))

(defn create-student-modal []
 (let [modal-state (r/atom false)]
   (fn []
    (js/console.log "create-student-modal rendered")
    [sa/Modal {:trigger (r/as-element [sa/Button {:color "orange"
                                                  :on-click #(reset! modal-state true)}
                                        "Create New Student"])
               :open @modal-state}
     [sa/ModalHeader "Create New Student"]
     [sa/ModalContent
      [sa/ModalDescription
       [sa/Header "Please complete new student form."]
       (let [name (r/atom nil)
             description (r/atom nil)
             handle-create #(do (reset! modal-state false)
                                (a/create-student! @name @description)
                                (.reload (.-location js/window)))
             get-value #(-> % (.-target) (.-value))]
        [sa/Form
         [sa/FormField
          [:label "First & Last Name"]
          [:input {:id "name-input"
                   :placeholder "First & Last Name"
                   :on-change #(reset! name (get-value %))}]]
         [sa/FormField
          [:label "Notes"]
          [:input {:id "description-input"
                   :placeholder "Notes"
                   :on-change #(reset! description (get-value %))}]]
         [sa/Button {:on-click #(reset! modal-state false)}
          "Cancel"]
         [sa/Button {:on-click handle-create
                     :primary true
                     :type "submit"}
          "Create"]])]]])))