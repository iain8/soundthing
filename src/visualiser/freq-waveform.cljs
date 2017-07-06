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

(defn init-canvas [canvas]
  (swap! state assoc :canvas canvas)
  (swap! state assoc :render-ctx (.getContext canvas "2d")))

(defn render-waveform [this]
  (let [source (@app-state :audio-source)]
    (if source
      (do (clear-canvas)
        (let [left-channel (.getChannelData (.-buffer source) 0)
          line-opacity (/ width (.-length left-channel))
          context (@state :render-ctx)]
          (.save context)
          (set! (.-fillStyle context) "#222")
          (set! (.-strokeStyle context) "#211")
          ; (set! (.-globalCompositeOperation context) "lighter")
          (.translate context 0 (/ height 2))
          (set! (.-globalAlpha context) 0.06)
          (loop [i 0]
            (let [x (.floor js/Math (/ (* width i) (.-length left-channel)))
              y (* (aget left-channel i) (/ height 2))]
              (.beginPath context)
              (.moveTo context x 0)
              (.lineTo context (+ x 1) y)
              (.stroke context)
              (when (< i (.-length left-channel))
                (recur (inc i))
                )))
                (.log js/console "done")
                (.restore context))))))

(defn canvas []
  (let []
    (reagent/create-class
      {:component-did-mount
        (fn [this]
          (init-canvas (reagent/dom-node this)))
      :component-will-receive-props render-waveform
      :reagent-render
        (fn []
          [:canvas])})))
