(ns soundthing.components.visualiser
  (:require [soundthing.audio :as audio]
    [soundthing.data :refer [app-state]]
    [reagent.core :as reagent]))

(def render-ctx (reagent/atom nil))

(def width 300)
(def height 150)

;; TODO: width and height
(defn clear-canvas []
  (.clearRect @render-ctx 0 0 width height))

(defn render []
  (.requestAnimationFrame js/window render)
  (clear-canvas)
  (let [bytes (audio/get-freq-data (@app-state :freq-data))
    bin-count (audio/get-bin-count)]
    (set! (.-font @render-ctx) "16px serif")
    (loop [i 0]
      (let [val (aget bytes i)]
        (set! (.-fillStyle @render-ctx) "rgb(255,255,255)")
        (.fillText @render-ctx val (+ 10 (* i 30)) 50)
        (when (< i bin-count)
          (recur (inc i)))))))

;; improved render function!
(defn render_mk2 []
  (.requestAnimationFrame js/window render_mk2)
  (clear-canvas)
  (let [bytes (audio/get-freq-data (@app-state :freq-data))
    bin-count (audio/get-bin-count)
    bar-width (- (* (/ bin-count width) 2.5) 1)]
    (loop [i 0]
      (let [bar-height (or (aget bytes i) 100)]
        (set! (.-fillStyle @render-ctx) (clojure.string/join ["rgb(" (+ bar-height 100) ", 50, 50)"]))
        (.fillRect @render-ctx (* i bar-width) (- height (/ bar-height 2)) bar-width (/ bar-height 2))
        (when (< i bin-count)
          (recur (inc i)))))))

(defn init-canvas [canvas]
  (reset! render-ctx (.getContext canvas "2d"))
  (.requestAnimationFrame js/window render_mk2))

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
