(ns book-list.routes.books
  (:require
   [book-list.middleware :as middleware]
   [book-list.db.core :as db]
   [ring.util.http-response :as response]))

(defn get-all-books [request]
  {:status 200
   :body (db/get-all-books)})

(def book-routes 
  ["/apis"
   {:middleware [middleware/wrap-formats]}
   ["/books" {:get {:handler get-all-books}}]])
