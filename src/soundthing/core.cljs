(ns soundthing.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

(defonce audio-context (js/window.AudioContext.))

(rf/reg-event-db
  :initalize
  (fn [_ _]
    {:audio-playing "false"}))

(rf/reg-event-db
  :start-audio
  (fn [db [_ playing-state]]
    (assoc db :audio-playing playing-state)))

(rf/reg-sub
  :audio-playing
  (fn [db _]
    (:audio-playing db)))

(defn toggle-audio []
  (rf/dispatch [:start-audio "true"]))

(defn audio-el []
  [:audio {:src "leadbelly.mp3"}])

(defn home []
  [:div
    [:h1 "well hello there"]
    [:button {:on-click #(toggle-audio)}
      "start"]
    [audio-el]
    [:pre (-> @(rf/subscribe [:audio-playing]))]
  ])

(defn ^:export run
  []
  (rf/dispatch-sync [:initialize])
  (reagent/render [home]
                  (js/document.getElementById "app")))
