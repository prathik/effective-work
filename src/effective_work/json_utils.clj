(ns effective-work.json-utils
  (:require [clojure.data.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:import (java.sql Timestamp Date)))

(use 'ring.util.response)

(defn time-writer [key value]
  (condp instance? value
    Timestamp (str (Timestamp. (.getTime value)))
    Date (str (Date. (.getTime value)))
    value))

(defn json-response [j]
  (content-type (response (json/write-str j :value-fn time-writer)) "application/json"))

