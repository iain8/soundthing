(ns soundthing.ui.loop-points
  (:require [reagent.core :as reagent]
    [goog.events :as events]
    [soundthing.data :refer [app-state]])
  (:import [goog.events EventType])) ;; TODO: just mousemove, mouseup

(defn get-client-rect [evt]
  (let [r (.getBoundingClientRect (.-target evt))]
    {:left (.-left r)})) ;; TODO: just left

(defn mouse-move [offset]
  (fn [evt]
    (let [x (- (- (.-clientX evt) (:x offset)) (@position :start-x))]
      (if (and (> x 0) (< x 298))
        (swap! app-state assoc :loop-start x)))))

(defn mouse-up [on-move]
  (fn me [evt]
    (events/unlisten js/window EventType.MOUSEMOVE on-move)))

(defn mouse-down [event position]
  (let [left ((get-client-rect event) :left)
        offset {:x (- (.-clientX event) left)}
        on-move (mouse-move offset)]
    (if (== (@position :start) 0)
      (swap! position assoc :start left))
    (events/listen js/window EventType.MOUSEMOVE on-move)
    (events/listen js/window EventType.MOUSEUP (mouse-up on-move))))

(defn element [type x]
  (let [position (reagent/atom {:start x})]
    (fn [] 
      [:div {:class (str "loop-point " type)
      :style {:left x}
      :on-mouse-down (fn [event] (mouse-down event position))}])))