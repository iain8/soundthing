(ns soundthing.visualiser.freq-waveform
  (:require [reagent.core :as reagent]
    [soundthing.data :refer [app-state]]
    [cljs.core.async :refer [chan put!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def state (reagent/atom 
  {:render-ctx nil
  :canvas nil
  :length 0})) ;; TODO: remove

(def width 300)
(def height 150)

(defn clear-canvas []
  (.clearRect (@state :render-ctx) 0 0 width height))

(defn init-canvas [el]
  (swap! state assoc :canvas (aget (.-children el) 0))
  (swap! state assoc :render-ctx (.getContext (@state :canvas) "2d")))

(defn render-waveform [this]
  (go
    (let [source (@app-state :data-source)]
      (.log js/console "rendering")
      (if source
        (do
          (clear-canvas)
          (let [left-channel (.getChannelData (.-buffer source) 0)
            line-opacity (/ width (.-length left-channel))
            context (@state :render-ctx)]
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
                (when (< i (.-length left-channel))
                  (recur (inc i))
                  )))
                  (swap! app-state assoc :spectrum-loading "loaded")
                  (.restore context)))))
                  this))

; (defn do-next-thing []
;   (.log js/console "hi mom"))

; (def render-reqs (chan 2 render-waveform))
; (def finish-loading (chan 2 do-next-thing))

(defn canvas []
  (let []
    (reagent/create-class
      {:component-did-mount
        (fn [this]
          (init-canvas (reagent/dom-node this)))
      :component-will-receive-props render-waveform
        ; (fn [this]
        ;   (do
        ;     (.log js/console "pre hi mom")
        ;     (put! render-reqs this)))
      :reagent-render
        (fn []
          [:div
            [:canvas]])})))
