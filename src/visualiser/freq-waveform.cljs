(ns soundthing.visualiser.freq-waveform
  (:require [reagent.core :as reagent]
    [soundthing.data :refer [app-state]]))

(def state (reagent/atom 
  {:render-ctx nil
  :canvas nil}))

(def width 300)
(def height 150)

(defn clear-canvas []
  (.clearRect (@state :render-ctx) 0 0 width height))

;; TODO: call this when data is available!
(defn render []
  (clear-canvas)
  (let [left-channel (.-buffer (@app-state :audio-source))
    line-opacity (/ width left-channel)]
    (.save (@state :render-ctx))
    ))

(defn init-canvas [canvas]
  (swap! state assoc :canvas canvas)
  (swap! state assoc :render-ctx (.getContext canvas "2d")))

(defn canvas []
  (let []
    (reagent/create-class
      {:component-did-mount
        (fn [this]
          (init-canvas (reagent/dom-node this)))
      :reagent-render
        (fn []
          [:canvas])})))
