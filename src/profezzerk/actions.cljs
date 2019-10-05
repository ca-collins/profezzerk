(ns profezzerk.actions
  (:require
    ; [reagent.core :as r]
    [cljs.core.async :refer [<!]]
    [ajax.core :refer [GET POST DELETE ajax-request url-request-format json-response-format]]))

(def student-mgmt-server "http://localhost:8000/students")

(defn sanitize-data [response]
 (js->clj (.parse js/JSON response) :keywordize-keys true))

(defn handler2 [[ok response]]
  (if ok
    ; (.log js/console
     (str response)
    ; (.error js/console
     (str response)))

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

(defn delete-student! [id]
 (js/console.log "delete-student! ran")
 (ajax-request
       {:uri (str student-mgmt-server "/" id)
        :method :post
        :params {:_method "DELETE"}
        :handler handler2
        :format (url-request-format)
        :response-format (json-response-format {:keywords? true})}))