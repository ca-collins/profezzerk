(ns profezzerk.core
  (:require
    [reagent.core :as r]
    [cljs.core.async :refer [<!]]
    [ajax.core :refer [GET POST DELETE ajax-request url-request-format json-response-format]]
    [soda-ash.core :as sa]
    [profezzerk.actions :as a]))

;; COMPONENTS ==============================

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

(defn students-page []
 (let [data-atom (r/atom nil)]
  (a/fetch-data! data-atom)
  (fn []
   (js/console.log "student-page rendered")
   [:div
    [sa/Container {:text-align 'left}
     [sa/Header {:as "h1" :class "inverted"}
      [:i {:class "address book icon orange" :id "logo-icon"}]
      [:div.content
       [:span "Profe"] "zzerk"
       [:div.sub.header {:id "tagline"} "Build your own student list"]]]]
    (let [students @data-atom]
      [sa/Container {:text-align 'center
                     :class "page-content"}
       [sa/Container {:text-align 'left}
        [create-student-modal]]
       [sa/Segment {:attached 'top :color "orange"}
        [sa/Header {:size "medium" :class ""} "STUDENT ROSTER"]]
       (if (seq students)
         [sa/Table {:class "unstackable"
                    :color "orange"}
          [sa/TableHeader
           [sa/TableRow
            [sa/TableHeaderCell]
            [sa/TableHeaderCell]
            [sa/TableHeaderCell
             [sa/Header {:class "sub"} "NAME"]]
            [sa/TableHeaderCell
             [sa/Header {:class "sub"} "NOTES"]]]]
          [sa/TableBody
           (for [student students]
            [sa/TableRow {:key (:id student)}
             [sa/TableCell {:class "collapsing"}
              [delete-student-modal (:id student) (:name student)]]
             [sa/TableCell {:class "collapsing"}
              [update-student-modal (:id student) (:name student) (:description student)]]
             [sa/TableCell {:class "name-cells"} (:name student)]
             [sa/TableCell (:description student)]])]]
         [:div "No more students!"])])])))

;; Initialize app ==================================

(defn mount-root []
  (r/render [students-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
