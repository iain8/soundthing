(ns soundthing.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(enable-console-print!)

(println "soundthing")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

(defn toggle-audio []
  (js/alert "Hello world!"))

(defn home []
  [:div
    [:h1 "well hello there"]
    [:button {:on-click #(toggle-audio)}
      "start"]
  ])

(reagent/render-component [home]
                          (. js/document (getElementById "app")))
