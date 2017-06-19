(ns soundthing.audio
  (:require [reagent.core :as reagent]
    [soundthing.data :refer [app-state]]))

;; fetch the audio context TODO: try/catch
(defn create-context []
  (if js/window.AudioContext.
    (js/window.AudioContext.)
    (js/window.webkitAudioContext.)))

;; define audio context TODO: into app state
(defonce context (create-context))

;; TODO: into app state
(def analyser (.createAnalyser context))

;; create a new audio source
(defn make-source []
  (do
    (swap! app-state assoc :audio-source (.createBufferSource context))
    (.connect (@app-state :audio-source) (.-destination context))
    (.connect (@app-state :audio-source) analyser)
    (set! (.-loop (@app-state :audio-source)) true)))

;; decode audio and add to state
(defn add-to-source [data]
  (.then (.decodeAudioData context data) 
    #(do
      (swap! app-state assoc :audio-data %))))

;; create a new audio node and start it
(defn start-audio []
  (do
    (make-source)
    (set! (.-buffer (@app-state :audio-source)) (@app-state :audio-data))
    (.start (@app-state :audio-source) 0)))

;; stop the node and kill it!
(defn stop-audio []
  (do
    (.stop (@app-state :audio-source) 0)
    (swap! app-state assoc :audio-source nil)))

(defn get-freq-data [freq-data]
  (.getByteFrequencyData analyser freq-data)
  freq-data)

;;TODO: why no work
(defn get-bin-count []
  (.-frequencyBinCount analyser))
