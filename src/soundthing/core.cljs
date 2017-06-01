(ns soundthing.core
  (:require [reagent.core :as reagent]
            [ajax.core :refer [GET]]
            [ajax.protocols :refer [-body]]
            [soundthing.audio]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state 
  (reagent/atom 
    {:audio-playing "no"}))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ; (swap! app-state update-in [:__figwheel_counter] inc)
)



(defn handler [data]
  (do
    (soundthing.audio.add-to-source data)
    (swap! app-state assoc :audio-playing "yes")))

;; TODO: make better
(defn error-handler [data]
  (println "error"))

(defn toggle-audio [] 
  (GET "loop.wav"
    {:response-format {:content-type "audio/wav" :description "Wave audio file" :read -body :type :arraybuffer}
    :handler handler
    :error-handler error-handler }))

(defn home []
  [:div
    [:h1 "well hello there"]
    [:button {:on-click #(toggle-audio)} "load"]
    [:pre (@app-state :audio-playing)]
  ])

;; does not want to run... why though
; (defn ^:export run
;   []
;   ;; (rf/dispatch-sync [:initialize])
;   (println "farts")
;   (reagent/render [home]
;                   (. js/document (getElementById "app"))))

(reagent/render [home]
  (. js/document (getElementById "app")))