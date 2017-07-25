(ns soundthing.data
  (:require [reagent.core :as reagent]))

;; app data
(def app-state 
  (reagent/atom 
    {:audio-data nil ;; raw audio data
    :audio-loaded "" ;; whether audio is loaded (TODO: why is this a string)
    :audio-playing 0 ;; audio playing state
    :audio-rate 1 ;; audio playback rate
    :audio-source nil ;; audio playing source!
    :data-source nil ;; source for audio data not used when playing
    :file-data nil ;; TODO: is this used?
    :file-name nil ;; name of audio file
    :freq-data (js/Uint8Array. 64) ;; TODO: should be in global state?
    :spectrum-loaded ""
    :start-time 0}))
