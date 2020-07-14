-- :name create-book! :! :n
-- :doc add new book record
INSERT INTO books
(id, title, author, thumbnail, read)
VALUES (:id, :title, :author, :thumbnail, false)

-- :name get-book :? :1
-- :doc retrieve book record given id
SELECT * FROM books
WHERE id = :id

-- :name get-all-books :? :*
-- :doc get all book records
SELECT * FROM books
