(ns soundthing.ui.loop-points
  (:require [reagent.core :as reagent]
    [goog.events :as events])
  (:import [goog.events EventType])) ;; TODO: just mousemove, mouseup

(def position (reagent/atom {:x -1
  :start-x 0}))

(defn get-client-rect [evt]
  (let [r (.getBoundingClientRect (.-target evt))]
    {:left (.-left r)})) ;; TODO: just left

(defn mouse-move [offset]
  (fn [evt]
    (let [x (- (- (.-clientX evt) (:x offset)) (@position :start-x))]
      (if (and (> x 0) (< x 298))
        (swap! position assoc :x x)))))

(defn mouse-up [on-move]
  (fn me [evt]
    (events/unlisten js/window EventType.MOUSEMOVE on-move)))

(defn mouse-down [event]
  (let [left ((get-client-rect event) :left)
        offset {:x (- (.-clientX event) left)}
        on-move (mouse-move offset)]
    (if (== (@position :start-x) 0)
      (swap! position assoc :start-x left))
    (events/listen js/window EventType.MOUSEMOVE on-move)
    (events/listen js/window EventType.MOUSEUP (mouse-up on-move))))

(defn element []
  [:div {:class "loop-point"
    :style {:left (@position :x)}
    :on-mouse-down mouse-down}])