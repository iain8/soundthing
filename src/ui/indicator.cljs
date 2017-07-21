(ns soundthing.ui.indicator
  (:require [reagent.core :as reagent]
    [soundthing.data :refer [app-state]]))

(def state (reagent/atom 
  {:dom-node nil 
  :class "indicator"
  :duration 0}))

(defn toggle []
  (if (@app-state :audio-source)
    (swap! state assoc :duration (.. (@app-state :audio-source) -buffer -duration)))
  (if (== (@app-state :audio-playing) 1)
    (swap! state assoc :class "indicator start")
    (swap! state assoc :class "indicator")))

(defn element []
  (let []
    (reagent/create-class 
      {:component-did-mount
        (fn [this]
          (swap! state assoc :dom-node (reagent/dom-node this)))
      :component-will-receive-props toggle
      :reagent-render
        (fn []
          [:div {:class (@state :class)
            :style {:animation-duration (apply str (/ (@state :duration) (@app-state :audio-rate)) "s")}}])})))
