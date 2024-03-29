(ns profezzerk.actions
  (:require
    ; [reagent.core :as r]
    [cljs.core.async :refer [<!]]
    [ajax.core :refer [GET POST DELETE ajax-request url-request-format json-response-format]]))

(def student-mgmt-server "http://localhost:8000/students")

(defn handler2 [[ok response]]
  (if ok
    ; (.log js/console
     (str response)
    ; (.error js/console
     (str response)))

(defn create-student! [name description cb]
  (ajax-request
        {:uri student-mgmt-server
         :method :post
         :params {:name name
                  :description description}
         :handler (fn [_] (cb))
         :format (url-request-format)
         :response-format (json-response-format {:keywords? true})}))

(defn update-student! [id name description cb]
 (ajax-request
       {:uri (str student-mgmt-server "/" id)
        :method :post
        :params {:_method "PUT"
                 :name name
                 :description description}
        :handler (fn [_] (cb))
        :format (url-request-format)
        :response-format (json-response-format {:keywords? true})}))

(defn delete-student! [id cb]
 (ajax-request
       {:uri (str student-mgmt-server "/" id)
        :method :post
        :params {:_method "DELETE"}
        :handler (fn [_] (cb))
        :format (url-request-format)
        :response-format (json-response-format {:keywords? true})}))
