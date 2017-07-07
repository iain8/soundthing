(ns soundthing.ui.waveform
  (:require [reagent.core :as reagent]
    [soundthing.data :refer [app-state]]
    [soundthing.visualiser.freq-waveform :as freq-waveform]
    [soundthing.ui.indicator :as indicator]))

(defn pad-number [number]
  (if (< 9 number)
    (str number)
    (apply str "0" (str number))))

(defn seconds-to-minutes [time]
  (let [seconds (.round js/Math (mod time 60))
    minutes (.floor js/Math (/ time 60))]
      (apply str (pad-number minutes) ":" (pad-number seconds))))

(defn waveform []
  [:div {:class "waveform"}
    [freq-waveform/canvas (@app-state :data-source)]
    [indicator/element (@app-state :audio-playing)]
    [:span {:class "duration"} 
      (if (@app-state :data-source)
        (seconds-to-minutes (.. (@app-state :data-source) -buffer -duration))
        "00:00")]])
