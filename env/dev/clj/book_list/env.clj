(ns book-list.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [book-list.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[book-list started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[book-list has shut down successfully]=-"))
   :middleware wrap-dev})
