(ns soundthing.audio
  (:require [cljs.core.async :as async :refer [<!]]))

;; fetch the audio context
(defn create-context
  []
  (if js/window.AudioContext.
    (js/window.AudioContext.)
    (js/window.webkitAudioContext.)))

;; define audio context
(defonce context (create-context))

;; define audio source
(def source (.createBufferSource context))

(defn error-handler [data]
  (println "error"))

;; decode some audio data
(defn decode-audio [data]
  (let [out (async/chan)]
    (.decodeAudioData context data
      (fn [err audio]
        (async/put! out (if err err audio))
        (async/close! out)))
    out))

;; TODO ugh can maybe change this back to a promise

;; add data to a bufferSource
(defn add-to-source [data]
  (let [audio (<! (decode-audio data))]
    (if (instance? js/Error audio)
      (throw audio)
      (set! (.-buffer source) data))))
  ; (catch js/Error e
  ;   (error-handler e))))


