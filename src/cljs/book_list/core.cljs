(ns book-list.core
  (:require
   [day8.re-frame.http-fx]
   [reagent.dom :as rdom]
   [reagent.core :as r]
   [re-frame.core :as rf]
   [goog.history.EventType :as HistoryEventType]
   [markdown.core :refer [md->html]]
   [book-list.ajax :as ajax]
   [book-list.events :as events]
   [book-list.views :as views]
   [book-list.subs :as subs]
   [reitit.core :as reitit]
   [reitit.frontend.easy :as rfe]
   [clojure.string :as string])
  (:import goog.History))




(defn navigate! [match _]
  (rf/dispatch [::events/get-books])
  (rf/dispatch  [::events/set-active-panel :home-panel]))

(def router
  (reitit/router
    [["/" {:name        :home
           :view        #'views.home-panel
           :controllers [{:start (fn [_]
                                   
                                   )}]}]]))

(defn start-router! []
  (rfe/start!
    router
    navigate!
    {}))

;; -------------------------
;; Initialize app
(defn mount-components []
  (rf/clear-subscription-cache!)
  (rdom/render [#'views/main-panel] (.getElementById js/document "app")))

(defn init! []
(start-router!)
(ajax/load-interceptors!)
(mount-components))
