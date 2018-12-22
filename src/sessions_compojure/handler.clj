(ns sessions-compojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [sessions-compojure.json-utils :as sj]
            [sessions-compojure.dao :as dao]
            [sessions-compojure.request-utils :as ru]
            [clojure.tools.logging :as log]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as resp]))

(use 'ring.util.response)

(defn check-for-active-session
  [day]
  (not (empty? (dao/get-active-sessions-for-day day))))

(defn create-active-session-for-request [request]
  (log/info "creating a new session")
  (log/debug request)
  (sj/json-response (dao/insert-db-for-request (ru/get-insert-params request))))

(defn session-post-handler
  [request]
  (if-not (check-for-active-session (ru/get-start-date request))
    (create-active-session-for-request request) (str "active session present")))

(defn sessions-get-handler [day]
  (log/info "running get handler")
  (sj/json-response (dao/get-all-session-for-day day)))

(defn json-response-active-session-for-day [day]
  (sj/json-response (dao/get-active-sessions-for-day day)))

(defn set-session-state [request]
  (dao/update-session (ru/get-update-params request) (ru/get-session-id request))
  (sj/json-response {:success true}))

(defn single-session-get-handler [day session]
  (if (= session "active")
    (json-response-active-session-for-day day)
    (sj/json-response (dao/get-session-details day session))))

(defn get-all-days-response []
  (log/info "fetching all days present in the system")
  (sj/json-response (dao/get-all-days)))

(defroutes app-routes
           (GET "/days/:day/sessions/" [day] (sessions-get-handler day))
           (GET "/days/:day/sessions/:session/" [day session] (single-session-get-handler day
                                                                                         session))
           (POST "/days/:day/sessions/" request
             (session-post-handler request))
           (PUT "/days/:day/sessions/:session/" request (set-session-state request))
           (GET "/days/" [] (get-all-days-response))
           (GET "/" [] (resp/redirect "/index.html"))
           (route/resources "/")
           (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
