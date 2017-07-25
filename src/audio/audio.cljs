(ns soundthing.audio
  (:require [reagent.core :as reagent]
    [soundthing.data :refer [app-state]]
    [soundthing.visualiser.freq-waveform :as waveform]))

;; fetch the audio context TODO: try/catch
(defn create-context []
  (if js/window.AudioContext.
    (js/window.AudioContext.)
    (js/window.webkitAudioContext.)))

;; define audio context
(defonce context (create-context))

(def analyser (.createAnalyser context))
(def convolver (.createConvolver context))

;; create a new audio source
(defn make-source [start-time]
  (do
    (swap! app-state assoc :audio-source (.createBufferSource context))
    (.connect (@app-state :audio-source) (.-destination context))
    (.connect (@app-state :audio-source) analyser)
    (set! (.-loop (@app-state :audio-source)) true)
    (set! (.. (@app-state :audio-source) -playbackRate -value) (@app-state :audio-rate))
    (if start-time (set! (.-loopStart (@app-state :audio-source)) start-time))))

;; decode audio and add to state
(defn add-to-source [data]
  (.then (.decodeAudioData context data) 
    #(do
      (swap! app-state assoc :audio-data %)
      (swap! app-state assoc :data-source (.createBufferSource context))
      (set! (.-buffer (@app-state :data-source)) %)
      )))

;; create a new audio node and start it
(defn start-audio [start-time]
  (do
    (make-source start-time)
    (set! (.-buffer (@app-state :audio-source)) (@app-state :audio-data))
    (swap! app-state assoc :start-time (.-currentTime context))
    (.start (@app-state :audio-source) 0 start-time)
    (.log js/console "starting audio at " start-time)))

;; stop the node and kill it!
(defn stop-audio []
  (do
    (.stop (@app-state :audio-source) 0)
    (swap! app-state assoc :audio-source nil)))

(defn get-freq-data [freq-data]
  (do
    (set! (.-fftSize analyser) 512)
    (.getByteFrequencyData analyser freq-data)
    freq-data))

(defn get-bin-count []
  (.-frequencyBinCount analyser))

;; TODO: only do this if the sound is playing
;; TODO: use this in start/stop
;; TODO: factor in duration (put duration into app state)
(defn re-pitch []
  (let [position (* 
    (mod (.-currentTime context) (.. (@app-state :audio-source) -buffer -duration)) 
    (@app-state :audio-rate))]
        (stop-audio)
        (start-audio position)))
