(ns book-list.views
  (:require
   [re-frame.core :as re-frame]
   [book-list.subs :as subs]
   [book-list.events :as events]
   ))

(defn submit-isbn []
  (let [isbn (.-value (.getElementById js/document "isbn"))]
    (re-frame/dispatch [::events/add-book {:id isbn}])))

(defn add-book-input
  []
  [:div.field.has-addons.has-addons-centered
   [:div.control
    [:input#isbn.input {:type "text" :placeholder "ISBN"}]]
   [:div.control
    [:button.button.is-success {:on-click submit-isbn}
     [:span.icon
      [:i.fa.fa-plus]]]]])

(defn read-icon
  [{:keys [read]}]
  (if read
    [:i.fa.fa-bookmark]
    [:i.fa.fa-bookmark-o]))

(defn like-icon
  [{:keys [liked]}]
  (if liked
    [:i.fa.fa-heart]
    [:i.fa.fa-heart-o]))

(defn like-book
  [book]
  (re-frame/dispatch [::events/update-book (update-in book [:liked] not)]))

(defn read-book
  [book]
  (re-frame/dispatch [::events/update-book (update-in book [:read] not)]))


(defn filter-list
  []
  [:div.tabs.is-toggle.is-centered
   [:ul
    [:li {:class (if @(re-frame/subscribe [::subs/read-filter-enabled]) "is-active" "not-active")}
     [:a [:span {:on-click #(re-frame/dispatch [::events/toggle-read-filter])} "Read"]]]
    [:li {:class (if @(re-frame/subscribe [::subs/liked-filter-enabled]) "is-active" "not-active")}
     [:a [:span {:on-click #(re-frame/dispatch [::events/toggle-liked-filter])} "Liked"]]]
    [:li {:class (if @(re-frame/subscribe [::subs/unread-filter-enabled]) "is-active" "not-active")}
     [:a [:span {:on-click #(re-frame/dispatch [::events/toggle-unread-filter])} "Unread"]]]]])

(defn book-card-image
  [{:keys [thumbnail]}]
  [:div.card-image
   [:figure.image
    [:img {:src thumbnail :style {:width 125 :margin-left "30%"}}]]])

(defn book-card-body
  [{:keys [title author]}]
  [:div.card-content
   [:h2 title]
   [:h4 author]])

(defn book-card
  [book]
  [:div.card
   [book-card-image book]
   [book-card-body book]
   ])

(defn book-grid []
  (let [books (re-frame/subscribe [::subs/books])]
    (fn []
      [:div.columns.is-mobile.is-multiline
       (for [book @books]
         ^{:key (:id book)}
         [:div.column.is-3-desktop.is-full-mobile
          [book-card book]])])))


;; home

(defn home-panel []
  [:div.container
   [:section.section
    [:h1.title.has-text-centered (str "Book List")]
    [:div.container.has-text-centered
     [add-book-input]]]
   [:section.section
    [:div.container
     [book-grid]]]])



;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
