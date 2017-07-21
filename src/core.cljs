(ns soundthing.core
  (:require [reagent.core :as reagent]
            [soundthing.audio :as audio]
            [soundthing.data :refer [app-state]]
            [soundthing.ui.toggle-audio :as toggle-audio]
            [soundthing.ui.upload :as upload]
            [soundthing.ui.waveform :as waveform]
            [soundthing.visualiser.freq-spectrum :as freq-spectrum]))

(enable-console-print!)
  
;; home component
(defn home []
  [:div
    [:h1 "soundthing"]
    [:div
      [freq-spectrum/canvas]
      [waveform/waveform]]
    [upload/button]
    [toggle-audio/button]
    [:input {:type "number"
      :max 2
      :min 0
      :step 0.1
      :value (@app-state :audio-rate)
      :on-change #(swap! app-state assoc :audio-rate (-> % .-target .-value))}]
    [:pre ":audio-loaded " (@app-state :file-name) "\n"
      ":audio-playing " (@app-state :audio-playing)]
  ])

;; render!
(defn init! []
  (reagent/render [home]
  (. js/document (getElementById "app"))))

(init!)
