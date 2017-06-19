(ns soundthing.components.visualiser
  (:require [soundthing.audio :as audio]
    [soundthing.data :refer [app-state]]
    [reagent.core :as reagent]))

(def render-ctx (reagent/atom nil))

;; TODO: width and height
(defn clear-canvas []
  (.clearRect @render-ctx 0 0 300 150))

(defn render []
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
