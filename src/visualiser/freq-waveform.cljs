(ns soundthing.visualiser.freq-waveform
  (:require [reagent.core :as reagent]
    [soundthing.data :refer [app-state]]))

(def state (reagent/atom 
  {:render-ctx nil
  :canvas nil
  :loading 0
  :length 0}))

(def width 300)
(def height 150)

(defn clear-canvas []
  (.clearRect (@state :render-ctx) 0 0 width height))

(defn init-canvas [el]
  (swap! state assoc :canvas (aget (.-children el) 0))
  (swap! state assoc :render-ctx (.getContext (@state :canvas) "2d")))

(defn render-waveform [this]
  (let [source (@app-state :data-source)]
    (if source
      (do
        (clear-canvas)
        (swap! state assoc :loading 0)
        (let [left-channel (.getChannelData (.-buffer source) 0)
          line-opacity (/ width (.-length left-channel))
          context (@state :render-ctx)]
          ; (swap! state assoc :length (.-length left-channel))
          (swap! state assoc :length 69)
          (.log js/console (@state :length))
          (.save context)
          (set! (.-fillStyle context) "#A8A86A")
          (set! (.-strokeStyle context) "#A8A86A")
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
              (swap! state :loading inc)
              (when (< i (.-length left-channel))
                (recur (inc i))
                )))
                ;; TODO: remove loader
                (.log js/console "done")
                (.restore context))))))

(defn canvas []
  (let []
    (reagent/create-class
      {:component-did-mount
        (fn [this]
          (.log js/console (@state :loading))
          (init-canvas (reagent/dom-node this)))
      :component-will-receive-props render-waveform
      :reagent-render
        (fn []
          [:div
            [:canvas]
            [:pre (@state :loading)]])})))
