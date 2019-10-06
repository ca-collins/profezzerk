(ns profezzerk.components.create-student-modal
  (:require
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [profezzerk.actions :as a]))

(defn create-student-modal [fetch-data!]
 (let [modal-state (r/atom false)
       error (r/atom false)]
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
       (let [name (atom nil)
             description (atom nil)
             handle-create #(if (empty? @name)
                             (reset! error true)
                             (do (reset! modal-state false)
                                 (reset! error false)
                                 (a/create-student! @name (if @description @description ""))
                                 (fetch-data!)))
             get-value #(-> % (.-target) (.-value))]
        [sa/Form
         [sa/FormField {:required true}
          [:label "First & Last Name"]
          [sa/FormInput {:id "name-input"
                         :type "text"
                         :placeholder "First & Last Name"
                         :on-key-press #(when (= 13 (.-charCode %))
                                          (handle-create)
                                          (.preventDefault %))
                         :on-change #(reset! name (get-value %))}]]
         [sa/FormField
          [:label "Notes"]
          [:input {:id "description-input"
                   :placeholder "Notes"
                   :on-change #(reset! description (get-value %))
                   :on-key-press #(when (= 13 (.-charCode %))
                                    (handle-create)
                                    (.preventDefault %))}]]
         (when @error
           [sa/Message {:negative true}
            [sa/MessageHeader "Student name cannot be blank!"]])
         [sa/Button {:on-click #(do (reset! modal-state false)
                                    (reset! error false))}
          "Cancel"]
         [sa/Button {:on-click handle-create
                     :primary true
                     :type "submit"}
          "Create"]])]]])))
