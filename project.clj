(defproject effective-work "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0"],
                 [compojure "1.6.1"],
                 [mysql/mysql-connector-java "8.0.13"],
                 [org.clojure/java.jdbc "0.7.8"],
                 [org.clojure/tools.logging "0.4.1"]
                 [org.clojure/data.json "0.2.6"],
                 [ring/ring-defaults "0.3.2"]
                 [clj-time "0.15.0"]]
  :plugins [[lein-ring "0.12.4"]
            [lein-kibit "0.1.6"]]
  :ring {:handler sessions-compojure.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
