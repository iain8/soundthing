(ns soundthing.audio
  (:require [reagent.core :as reagent]))

;; need to do something about this
(def audio-state (reagent/atom 
  {:data nil
  :source nil}))

;; fetch the audio context
(defn create-context
  []
  (if js/window.AudioContext.
    (js/window.AudioContext.)
    (js/window.webkitAudioContext.)))

;; define audio context
(defonce context (create-context))

;; define audio source
(defn make-source []
  (swap! audio-state assoc :source (.createBufferSource context)))

;; decode audio, add to source and start playing (TOO MUCH FOR ONE METHOD)
(defn add-to-source [data]
  (.then (.decodeAudioData context data) 
    #(do
      (swap! audio-state assoc :data %))))

(defn start-audio []
  (do
    (make-source)
    (set! (.-buffer (@audio-state :source)) (@audio-state :data))
    (.connect (@audio-state :source) (.-destination context))
    (set! (.-loop (@audio-state :source)) true)
    (.start (@audio-state :source) 0)))

(defn stop-audio []
  (.stop (@audio-state :source) 0))
