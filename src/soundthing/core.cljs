(ns soundthing.core
  (:require [reagent.core :as reagent]
            [ajax.core :refer [GET]]
            [ajax.protocols :refer [-body]]
            [soundthing.audio]
            [soundthing.components.upload]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
(defonce app-state 
  (reagent/atom 
    {:audio-loaded 0
    :audio-playing 0}))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ; (swap! app-state update-in [:__figwheel_counter] inc)
)

;; toggle audio button
(defn toggle-audio []
  (if (== (@app-state :audio-playing) 0)
    (do
      (soundthing.audio.start-audio)
      (swap! app-state assoc :audio-playing 1))
    (do
      (soundthing.audio.stop-audio)
      (swap! app-state assoc :audio-playing 0))))
  
;; home component
(defn home []
  [:div
    [:h1 "soundthing"]
    (soundthing.components.upload.button)
    [:button 
      {:on-click #(toggle-audio)
      :className "button"} "play"]
    [:pre (@app-state :audio-playing)]
  ])

;; render!
(reagent/render [home]
  (. js/document (getElementById "app")))