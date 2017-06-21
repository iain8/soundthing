(ns soundthing.visualiser.freq-spectrum
  (:require [soundthing.audio :as audio]
    [soundthing.data :refer [app-state]]
    [reagent.core :as reagent]))

(def render-ctx (reagent/atom nil))

(def width 300)
(def height 150)

(defn clear-canvas []
  (.clearRect @render-ctx 0 0 width height))

;; frequency chart
(defn render []
  (.requestAnimationFrame js/window render)
  (clear-canvas)
  (let [bytes (audio/get-freq-data (@app-state :freq-data))
    bin-count (audio/get-bin-count)
    bar-width (- (* (/ width bin-count) 2.5) 1)] ;; not quite right
    (loop [i 0]
      (let [bar-height (or (aget bytes i) 0)]
        (set! (.-fillStyle @render-ctx) (clojure.string/join ["rgb(" (+ bar-height 100) ", 50, 50)"]))
        (.fillRect @render-ctx (* i bar-width) (- height (/ bar-height 2)) bar-width (/ bar-height 2))
        (when (< i bin-count)
          (recur (inc i)))))))

;; 96, 88, 122 for thingy

;; TODO: method for proportional adjustment of rgb color or we can use HSL via CSS3

(defn init-canvas [canvas]
  (reset! render-ctx (.getContext canvas "2d"))
  (.requestAnimationFrame js/window render))

(defn canvas []
  (let []
    (reagent/create-class
      {:component-did-mount 
        (fn [this]
          (init-canvas (reagent/dom-node this)))
      :display-name "visualisation canvas"
      :reagent-render
        (fn []
          [:canvas])})))
