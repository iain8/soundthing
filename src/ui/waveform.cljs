(ns soundthing.ui.waveform
  (:require [reagent.core :as reagent]
    [soundthing.data :refer [app-state]]
    [soundthing.visualiser.freq-waveform :as freq-waveform]
    [soundthing.ui.indicator :as indicator]
    [soundthing.ui.loop-points :as loop-points]))

(defn pad-number [number]
  (if (< 9 number)
    (str number)
    (str "0" (str number))))

(defn seconds->minutes [time]
  (let [seconds (.round js/Math (/ (mod time 60) (@app-state :audio-rate)))
    minutes (.floor js/Math (/ time 60))]
      (str (pad-number minutes) ":" (pad-number seconds))))

(defn waveform []
  [:div {:class (clojure.string/join " " ["waveform" (@app-state :spectrum-loading)])}
    [:div {:class "loading-blind"}
      [:img {:src "/img/716.gif"}]]
    [freq-waveform/canvas (@app-state :data-source)]
    [indicator/element (@app-state :audio-playing)]
    [loop-points/element]
    [:span {:class "duration"} 
      (if (@app-state :data-source)
        (seconds->minutes (.. (@app-state :data-source) -buffer -duration))
        "00:00")]])
