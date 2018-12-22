(ns effective-work.request-utils
  (:require [clojure.tools.logging :as log]
            [clojure.string :as str])
  (:import (java.text SimpleDateFormat)
           (java.sql Timestamp)))

(defn get-param [request key]
  (get (get request :params) key))

(defn error-if-blank [val]
  (if (= val "") (throw (IllegalArgumentException. "Invalid date")) val))

(defn get-date-time-from-string [strDate]
  (Timestamp. (.getTime (.parse (SimpleDateFormat. "yyyy-MM-dd HH:mm") strDate))))

(defn get-date-time [request key]
  (get-date-time-from-string (str/join " " [(get-param request :day) (get-param request key)])))

(defn get-start-date [request]
  (error-if-blank (get-date-time request :startTime)))

(defn get-end-date [request]
  (error-if-blank (get-date-time request :endTime)))

(defn get-insert-params [request]
  {:startDateTime  (get-start-date request)})

(defn param-present [request key]
  (contains? (get request :params) key))

(defn check-and-get [request key]
  (when (contains? (get request :params) key) (get-param request key)))

(defn get-session-id [request] (check-and-get request :session))

(defn get-end-date-map [request]
  (when (param-present request :endTime) {:endDateTime (get-end-date request)}))

(defn get-start-date-map [request]
  (when (param-present request :startTime) {:startDateTime (get-start-date request)}))

(defn validate-request-for-update [request]
  (when (and (not (param-present request :startTime))
           (not (param-present request :endTime)))
    (throw
      (IllegalArgumentException. "Both startTime and endTime are missing, nothing to update."))))

(defn get-update-params [request]
  (validate-request-for-update request)
  (log/debug (get-start-date-map request))
  (log/debug (get-end-date-map request))
  (merge (get-start-date-map request) (get-end-date-map request)))
