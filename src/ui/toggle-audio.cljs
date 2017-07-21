(ns soundthing.ui.toggle-audio
  (:require [soundthing.data :refer [app-state]]
    [soundthing.audio :as audio]
    [reagent.core :as reagent]))

(def state (reagent/atom {:class ""}))

;; toggle audio button
(defn toggle-audio! []
  (if (and (some? (@app-state :audio-data)) (== (@app-state :audio-playing) 0))
    (do
      (audio/start-audio)
      (swap! app-state assoc :audio-playing 1)
      (swap! state assoc :class "active"))
    (do
      (audio/stop-audio)
      (swap! app-state assoc :audio-playing 0)
      (swap! state assoc :class ""))))

;; start/stop button
(defn button []
  [:button 
    {:on-click #(toggle-audio!)
    :class (apply str "indicator-button " (@state :class))} "play"])
