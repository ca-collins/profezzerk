(ns profezzerk.components.update-student-modal
  (:require
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [profezzerk.actions :as a]))

(defn update-student-modal [id name description fetch-data!]
 (let [modal-state (r/atom false)
       error (r/atom false)
       change-attempted (r/atom false)]
   (fn [id name description]
    (js/console.log "update-student-modal rendered")
    [sa/Modal {:trigger (r/as-element [sa/Button {:class "tiny icon"
                                                  :on-click #(reset! modal-state true)}
                                       [:i {:class "pencil alternate icon"}]])
               :open @modal-state}
     [sa/ModalHeader "Edit Student: " [:span name]]
     [sa/ModalContent
      [sa/ModalDescription
       [sa/Header "Please complete edits."]
       (let [new-name (atom nil)
             new-description (atom nil)
             ;change-attempted (atom false)
             handle-update (fn [name]
                            (do (reset! modal-state false)
                                (reset! error false)
                                (reset! change-attempted false)
                                (a/update-student! id name
                                 (if @new-description @new-description description))
                                (fetch-data!)))
             validate #(if @change-attempted
                         (if (empty? @new-name)
                             (reset! error true)
                             (handle-update @new-name))
                         (handle-update name))
             get-value #(-> % (.-target) (.-value))]
        [sa/Form
         [sa/FormField
          [:label "First & Last Name"]
          [:input {:id "name-input"
                   :placeholder "First & Last Name"
                   :default-value name
                   :on-key-press #(when (= 13 (.-charCode %))
                                    (validate)
                                    (.preventDefault %))
                   :on-change #(do (reset! new-name (get-value %))
                                   (reset! change-attempted true))}]]
         [sa/FormField
          [:label "Notes"]
          [:input {:id "description-input"
                   :placeholder "Notes"
                   :default-value description
                   :on-key-press #(when (= 13 (.-charCode %))
                                    (validate)
                                    (.preventDefault %))
                   :on-change #(reset! new-description (get-value %))}]]
         (when @error
           [sa/Message {:negative true}
            [sa/MessageHeader "Student name cannot be blank!"]])
         [sa/Button {:on-click #(do (reset! modal-state false)
                                    (reset! error false)
                                    (reset! change-attempted false))}
          "Cancel"]
         [sa/Button {:on-click validate
                     :primary true
                     :type "submit"}
          "Update Student"]])]]])))
