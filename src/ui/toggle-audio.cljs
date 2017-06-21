(ns soundthing.ui.toggle-audio
  (:require [soundthing.data :refer [app-state]]
    [soundthing.audio :as audio]))

;; start/stop button
(defn toggle-audio []
  (if (== (@app-state :audio-playing) 0)
    (do
      (audio/start-audio)
      (swap! app-state assoc :audio-playing 1))
    (do
      (audio/stop-audio)
      (swap! app-state assoc :audio-playing 0))))

(defn button []
  [:button 
    {:on-click #(toggle-audio)
    :className "button"} "play"])
