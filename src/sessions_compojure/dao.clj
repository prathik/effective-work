(ns sessions-compojure.dao
  (:require [clojure.java.jdbc :as j]
            [clojure.tools.logging :as log]))

(def mysql-db {:dbtype "mysql"
               :dbname "sessions"
               :user "root"
               :password "password"})

(defn get-active-sessions-for-day [day]
  (log/debug "getting active session")

  (j/query mysql-db ["select * from sessions where DAY(startDateTime) = DAY(?) and
  (endDateTime IS NULL)" day]))

(defn get-all-session-for-day  [day]
  (log/debug "fetching all sessions for the day")

  (j/query mysql-db ["select * from sessions where DAY(startDateTime) = DAY(?)" day]))

(defn get-all-days []
  (j/query mysql-db ["SELECT DISTINCT(DATE(startDateTime)) as day from sessions.sessions;"]))

(defn get-session-details [day session-id]
  (j/query mysql-db ["select * from sessions where DAY(startDateTime) = DAY(?) and id = ?" day
                     session-id]))

(defn update-session [update-values session-id]
  (log/debug "update-values: " update-values)
  (j/update! mysql-db :sessions update-values ["id = ?" session-id]))

(defn insert-db-for-request
  [insert]
  (j/insert!
    mysql-db
    :sessions
    insert))

