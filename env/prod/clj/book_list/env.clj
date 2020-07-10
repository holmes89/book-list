(ns book-list.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[book-list started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[book-list has shut down successfully]=-"))
   :middleware identity})
