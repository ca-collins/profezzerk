(ns profezzerk.components.update-student-modal
  (:require
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [profezzerk.actions :as a]))

(defn update-student-modal [id name description fetch-data!]
 (let [modal-state (r/atom false)
       error (r/atom false)
       new-name (r/atom name)
       new-description (r/atom description)]
   (fn [id name description]
    [sa/Modal {:trigger (r/as-element [sa/Button {:class "tiny icon"
                                                  :on-click #(reset! modal-state true)}
                                       [:i {:class "pencil alternate icon"}]])
               :open @modal-state}
     [sa/ModalHeader "Edit Student: " [:span name]]
     [sa/ModalContent
      [sa/ModalDescription
       [sa/Header "Please complete edits."]
       (let [handle-update (fn [name]
                            (do (reset! modal-state false)
                                (reset! error false)
                                (a/update-student! id name
                                 (if @new-description @new-description description)
                                 fetch-data!)))
             validate #(if (empty? @new-name)
                           (reset! error true)
                           (handle-update @new-name))
             get-value #(-> % (.-target) (.-value))]
        [sa/Form {:on-submit (fn [e] (.preventDefault e)
                                     (validate))}
         [sa/FormField
          [:label "First & Last Name"]
          [:input {:id "name-input"
                   :placeholder "First & Last Name"
                   :value @new-name
                   :on-change #(reset! new-name (get-value %))}]]
         [sa/FormField
          [:label "Notes"]
          [:input {:id "description-input"
                   :placeholder "Notes"
                   :default-value description
                   :on-change #(reset! new-description (get-value %))}]]
         (when @error
           [sa/Message {:negative true}
            [sa/MessageHeader "Student name cannot be blank!"]])
         [sa/Button {:type "button"
                     :on-click #(do (reset! modal-state false)
                                    (reset! error false))}
          "Cancel"]
         [sa/Button {:primary true
                     :type "submit"}
          "Update Student"]])]]])))
