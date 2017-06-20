(ns soundthing.core
  (:require [reagent.core :as reagent]
            [ajax.core :refer [GET]]
            [ajax.protocols :refer [-body]]
            [soundthing.data :refer [app-state]]
            [soundthing.audio :as audio]
            [soundthing.components.upload :as upload]
            [soundthing.components.visualiser :as visualiser]))

(enable-console-print!)

;; toggle audio button
(defn toggle-audio []
  (if (== (@app-state :audio-playing) 0)
    (do
      (audio/start-audio)
      (swap! app-state assoc :audio-playing 1))
    (do
      (audio/stop-audio)
      (swap! app-state assoc :audio-playing 0))))
  
;; home component
(defn home []
  [:div
    [:h1 "soundthing"]
    [:div
      [visualiser/canvas]]
    [upload/button]
    [:button 
      {:on-click #(toggle-audio)
      :className "button"} "play"]
    [:pre ":audio-loaded " (@app-state :file-name) "\n"
      ":audio-playing " (@app-state :audio-playing)]
  ])

;; render!
(defn init! []
  (reagent/render [home]
  (. js/document (getElementById "app"))))

(init!)