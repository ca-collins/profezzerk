(ns profezzerk.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [reagent.core :as r]
    [cljs.core.async :refer [<!]]
    [ajax.core :refer [GET POST DELETE ajax-request url-request-format json-response-format]]
    [soda-ash.core :as sa]))

;; CALCULATIONS ==============================
(defn sanitize-data [response]
 (js->clj (.parse js/JSON response) :keywordize-keys true))

;; ACTIONS ====================
(def student-mgmt-server "http://localhost:8000/students")

(defn fetch-data! [data-atom]
  (js/console.log "data fetched!")
  (GET student-mgmt-server
    {:handler #(reset! data-atom (sanitize-data %))
     :error-handler (fn [{:keys [status status-text]}]
                      (js/console.log status status-text))}))

(defn handler2 [[ok response]]
  (if ok
    ; (.log js/console
     (str response)
    ; (.error js/console
     (str response)))

(defn create-student! [name description]
  (ajax-request
        {:uri student-mgmt-server
         :method :post
         :params {:name name
                  :description description}
         :handler handler2
         :format (url-request-format)
         :response-format (json-response-format {:keywords? true})}))

(defn delete-student! [id]
 (js/console.log "delete-student! ran")
 (ajax-request
       {:uri (str student-mgmt-server "/" id)
        :method :post
        :params {:_method "DELETE"}
        :handler handler2
        :format (url-request-format)
        :response-format (json-response-format {:keywords? true})}))

(defn update-student! [id name description]
 (js/console.log "update-student! ran")
 (ajax-request
       {:uri (str student-mgmt-server "/" id)
        :method :post
        :params {:_method "PUT"
                 :name name
                 :description description}
        :handler handler2
        :format (url-request-format)
        :response-format (json-response-format {:keywords? true})}))

;; COMPONENTS ==============================

(defn delete-student-modal [id]
 (let [modal-state (r/atom false)]
   (fn []
    (let [handle-create #(do (reset! modal-state false)
                             (delete-student! id)
                             (.reload (.-location js/window)))]
      (js/console.log "delete-student-modal rendered")
      [sa/Modal {:trigger (r/as-element [sa/Button {:color "red" :on-click #(reset! modal-state true)} "Delete"])
                 :open @modal-state}
       [sa/ModalHeader "Are you sure you want to delete this student?"]
       [sa/ModalContent
        [sa/ModalDescription
         [sa/Button {:on-click #(reset! modal-state false)}
          "Cancel"]
         [sa/Button {:on-click handle-create
                     :color "red"
                     :type "submit"}
          "Delete"]]]]))))

(defn create-student-modal []
 (let [modal-state (r/atom false)]
   (fn []
    (js/console.log "create-student-modal rendered")
    [sa/Modal {:trigger (r/as-element [sa/Button {:primary true :on-click #(reset! modal-state true)} "Create New Student"])
               :open @modal-state}
     [sa/ModalHeader "Create New Student"]
     [sa/ModalContent
      [sa/ModalDescription
       [sa/Header "Please complete new student form."]
       (let [name (r/atom nil)
             description (r/atom nil)
             handle-create #(do (reset! modal-state false)
                                (create-student! @name @description)
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

(defn update-student-modal [id name description]
 (let [modal-state (r/atom false)]
   (fn [id name description]
    (js/console.log "update-student-modal rendered")
    [sa/Modal {:trigger (r/as-element [sa/Button {:color "gray" :on-click #(reset! modal-state true)} "Edit"])
               :open @modal-state}
     [sa/ModalHeader "Edit Student"]
     [sa/ModalContent
      [sa/ModalDescription
       [sa/Header "Please complete edits."]
       (let [new-name (r/atom nil)
             new-description (r/atom nil)
             handle-create #(do (reset! modal-state false)
                                (update-student! id @new-name @new-description)
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


(defn students-page []
 (let [data-atom (r/atom nil)]
  (fetch-data! data-atom)
  (fn []
   (js/console.log "student-page rendered")
   (let [students @data-atom]
     [sa/Container {:text-align 'center}
      [:h1 "My students"]
      [sa/Container {:text-align 'left}
       [create-student-modal]]
      (if (seq students)
        [sa/Table {:celled true}
         [sa/TableHeader
          [sa/TableRow
           [sa/TableHeaderCell]
           [sa/TableHeaderCell]
           [sa/TableHeaderCell "Name"]
           [sa/TableHeaderCell "Description"]]]
         [sa/TableBody
          (for [student students]
           [sa/TableRow {:key (:id student)}
            [sa/TableCell
             [delete-student-modal (:id student)]]
            [sa/TableCell
             [update-student-modal (:id student) (:name student) (:description student)]]
            [sa/TableCell (:name student)]
            [sa/TableCell (:description student)]])]]
        [:div "No more students!"])]))))

;; Initialize app ==================================

(defn mount-root []
  (r/render [students-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
