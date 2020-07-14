(ns book-list.routes.books
  (:require
   [book-list.middleware :as middleware]
   [book-list.db.core :as db]))

(defn get-all-books []
  (db/get-all-books))

(defn book-routes []
  ["/api"
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/books"
    ["/" {:get get-all-books}]]])
