(ns soundthing.ui.waveform
  (:require [reagent.core :as reagent]
    [soundthing.data :refer [app-state]]
    [soundthing.visualiser.freq-waveform :as freq-waveform]
    [soundthing.ui.indicator :as indicator]))

(defn waveform []
  [:div {:class "waveform"}
    [freq-waveform/canvas (@app-state :audio-source)]
    [indicator/element (@app-state :audio-playing)]])
