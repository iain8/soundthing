(ns soundthing.ui.pitch-control
  (:require [reagent.core :as reagent]
    [soundthing.data :refer [app-state]]
    [soundthing.audio :as audio]))

(defn pitch-change [event]
  (swap! app-state assoc :audio-rate (.. event -target -value))
  (audio/re-pitch))

(defn el []
  [:input {:type "number"
          :max 2
          :min 0
          :step 0.1
          :value (@app-state :audio-rate)
          :on-change pitch-change}])