(ns soundthing.data
  (:require [reagent.core :as reagent]))

;; app data
(def app-state 
  (reagent/atom 
    {:audio-data nil
    :audio-loaded ""
    :audio-playing 0
    :audio-source nil ;; TODO: gotta make this separate from playing source!
    :file-data nil
    :file-name nil
    :freq-data (js/Uint8Array. 64)}))
