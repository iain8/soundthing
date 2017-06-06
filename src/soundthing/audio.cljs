(ns soundthing.audio)

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

;; decode audio, add to source and start playing (TOO MUCH FOR ONE METHOD)
(defn add-to-source [data]
  (.then (.decodeAudioData context data) 
    #(do 
      (set! (.-buffer source) %)
      (.connect source (.-destination context))
      (set! (.-loop source) true))))

(defn start-audio []
  (.start source 0))

(defn stop-audio []
  (.stop source 0))
