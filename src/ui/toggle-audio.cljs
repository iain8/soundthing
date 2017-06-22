(ns soundthing.ui.toggle-audio
  (:require [soundthing.data :refer [app-state]]
    [soundthing.audio :as audio]
    [reagent.core :as reagent]))

(def state (reagent/atom {:class "indicator-button"}))

;; toggle audio button TODO: remove side-effect
(defn toggle-audio []
  (when (some? (@app-state :audio-data))
    (if (== (@app-state :audio-playing) 0)
      (do
        (audio/start-audio)
        (swap! app-state assoc :audio-playing 1)
        (swap! state assoc :class "indicator-button active"))
      (do
        (audio/stop-audio)
        (swap! app-state assoc :audio-playing 0)
        (swap! state assoc :class "indicator-button")))))

;; start/stop button
(defn button []
  [:button 
    {:on-click #(toggle-audio)
    :class (@state :class)} "play"])
