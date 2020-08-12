(ns book-list.events
  (:require
   [ajax.core :as ajax]        
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]
   [book-list.db :as db]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.controllers :as rfc]))

;;dispatchers

(rf/reg-event-db
  :common/navigate
  (fn [db [_ match]]
    (let [old-match (:common/route db)
          new-match (assoc match :controllers
                                 (rfc/apply-controllers (:controllers old-match) match))]
      (assoc db :common/route new-match))))

(rf/reg-fx
  :common/navigate-fx!
  (fn [[k & [params query]]]
    (rfe/push-state k params query)))

(rf/reg-event-fx
    :common/navigate!
  (fn [_ [_ url-key params query]]
    {:common/navigate-fx! [url-key params query]}))

(rf/reg-event-db
    ::initialize-db
  (fn [_ _]
    db/default-db))

(rf/reg-event-db
    ::set-active-panel
  (fn [db [_ active-panel]]
    (assoc db :active-panel active-panel)))

(rf/reg-event-db                   
    ::process-response             
  (fn
    [db [_ response]]
    (-> db
        (assoc :loading? false)
        (assoc :book-list (js->clj response)))))


(rf/reg-event-db                   
    ::bad-response             
  (fn
    [message]
    (js/console.log  message)))

(defn read?
  [book]
  (:read book))

(def read-filter
  (filter read?))

(defn unread?
  [book]
  (not (:read book)))

(def unread-filter
  (filter unread?))

(defn liked?
  [book]
  (:liked book))

(def liked-filter
  (filter liked?))

(rf/reg-event-db
    ::toggle-read-filter
  (fn
    [db]
    (as-> (update-in db [:read-filter-enabled?] not) d
      (if (contains? (:filters d) read-filter) 
        (update-in d [:filters] disj read-filter)
        (update-in d [:filters] conj read-filter)))))

(rf/reg-event-db
    ::toggle-unread-filter
  (fn
    [db]
    (as-> (update-in db [:unread-filter-enabled?] not) d
      (if (contains? (:filters d) unread-filter)
        (update-in d [:filters] disj unread-filter)
        (update-in d [:filters] conj unread-filter)))))

(rf/reg-event-db
    ::toggle-liked-filter
  (fn
    [db]
    (as-> (update-in db [:liked-filter-enabled?] not) d
      (if (contains? (:filters d) liked-filter)
        (update-in d [:filters] disj liked-filter)
        (update-in d [:filters] conj liked-filter)))))

(rf/reg-event-fx
    ::add-book
  (fn
    [{db :db} [_ body]]
    {:http-xhrio {:method          :post
                  :uri              "/apis/books"
                  :params body
                  :format          (ajax/json-request-format {:keywords? true})
                  :response-format (ajax/json-response-format {:keywords? true}) 
                  :on-success      [::get-books]
                  :on-failure      [::bad-response]
                  }
     :db  (assoc db :loading? true)}))

(rf/reg-event-fx
    ::update-book
  (fn
    [{db :db} [_ body]]
    {:http-xhrio {:method          :put
                  :uri              (str "/apis/books/" (get body :id))
                  :params body
                  :format          (ajax/json-request-format {:keywords? true})
                  :response-format (ajax/json-response-format {:keywords? true}) 
                  :on-success      [::get-books]
                  :on-failure      [::bad-response]
                  }
     :db  (assoc db :loading? true)}))

(rf/reg-event-fx
    ::get-books
  (fn
    [{db :db} _]
    {:http-xhrio {:method          :get
                  :uri             "/apis/books"
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true}) 
                  :on-success      [::process-response]
                  :on-failure      [::bad-response]
                  }
     :db  (assoc db :loading? true)}))

