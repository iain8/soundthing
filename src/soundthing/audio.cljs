(ns soundthing.audio
  (:require [reagent.core :as reagent]))

;; need to do something about this
(def audio-state (reagent/atom 
  {:data nil
  :source nil}))

;; fetch the audio context
(defn create-context []
  (if js/window.AudioContext.
    (js/window.AudioContext.)
    (js/window.webkitAudioContext.)))

;; define audio context
(defonce context (create-context))

;; create a new audio source
(defn make-source []
  (do
    (swap! audio-state assoc :source (.createBufferSource context))
    (.connect (@audio-state :source) (.-destination context))
    (set! (.-loop (@audio-state :source)) true)))

;; decode audio and add to state
(defn add-to-source [data]
  (.then (.decodeAudioData context data) 
    #(do
      (swap! audio-state assoc :data %))))

;; create a new audio node and start it
(defn start-audio []
  (do
    (make-source)
    (set! (.-buffer (@audio-state :source)) (@audio-state :data))
    (.start (@audio-state :source) 0)))

;; stop the node and kill it!
(defn stop-audio []
  (do
    (.stop (@audio-state :source) 0)
    (swap! audio-state assoc :source nil)))
