(ns book-list.routes.books
  (:require
   [book-list.middleware :as middleware]
   [book-list.db.core :as db]
   [ring.util.http-response :as response]
   [cheshire.core :as ch]))

(defn book-from-google-api [isbn]
  (-> 
    (slurp (str "https://www.googleapis.com/books/v1/volumes?q=isbn%3D" isbn))
    ch/parse-string
    (get "items")
    first
    (get "volumeInfo")))

(defn lookup-book [isbn]  
  (let [b (book-from-google-api isbn)]
    {
     :id isbn
     :title (get b "title")
     :author (first (get b "authors"))
     :thumbnail (get-in b ["imageLinks" "thumbnail"])
     }))

(defn get-all-books [request]
  {:status 200
   :body (db/get-all-books)})

(defn insert-book [request]
  (let [b (lookup-book (clojure.string/replace (clojure.string/trim (get-in request [:body-params :id])) "-" ""))]
    (db/create-book! b)
    (assoc {:status 201} :body b)))

(def book-routes 
  ["/apis"
   {:middleware [middleware/wrap-formats]}
   ["/books" {:get {:handler get-all-books}
              :post {:handler insert-book}}]])
