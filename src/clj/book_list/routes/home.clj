(ns book-list.routes.home
  (:require
   [book-list.layout :as layout]
   [book-list.db.core :as db]
   [clojure.java.io :as io]
   [book-list.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html"))


(def home-routes
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ])

