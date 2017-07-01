(ns soundthing.core
  (:require [reagent.core :as reagent]
            [soundthing.data :refer [app-state]]
            [soundthing.ui.toggle-audio :as toggle-audio]
            [soundthing.audio :as audio]
            [soundthing.ui.upload :as upload]
            [soundthing.visualiser.freq-spectrum :as freq-spectrum]
            [soundthing.visualiser.freq-waveform :as freq-waveform]))

(enable-console-print!)
  
;; home component
(defn home []
  [:div
    [:h1 "soundthing"]
    [:div
      [freq-spectrum/canvas]
      [freq-waveform/canvas]]
    [upload/button]
    [toggle-audio/button]
    [:pre ":audio-loaded " (@app-state :file-name) "\n"
      ":audio-playing " (@app-state :audio-playing)]
  ])

;; render!
(defn init! []
  (reagent/render [home]
  (. js/document (getElementById "app"))))

(init!)
