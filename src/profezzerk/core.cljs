(ns profezzerk.core
  (:require
    [reagent.core :as r]
    [cljs.core.async :refer [<!]]
    [ajax.core :refer [GET POST DELETE ajax-request url-request-format json-response-format]]
    [soda-ash.core :as sa]
    [profezzerk.actions :as a]
    [profezzerk.components.delete-student-modal :refer [delete-student-modal]]
    [profezzerk.components.create-student-modal :refer [create-student-modal]]
    [profezzerk.components.update-student-modal :refer [update-student-modal]]))

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
