(ns soundthing.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [ajax.protocols :refer [-body]]
            [soundthing.audio]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ; (swap! app-state update-in [:__figwheel_counter] inc)
)

(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:audio-loaded false
    :audio-playing "false"
    :source "nope"})) ;; hope we can replace this value

;; get audio data
(rf/reg-event-fx
   :start-audio
   (fn [{:keys [db]} [_ a]]
       {:http-xhrio {:method :get
               :uri    "leadbelly.mp3"
               :response-format {:content-type "audio/mpeg" :description "MP3 audio file" :read -body :type :arraybuffer}
               :on-success  [:process-response]
               :on-fail     [:bad-response]}
        :db   (assoc db :audio-loaded true)}))

;; process audio data
(rf/reg-event-db
  :process-response
  (fn [db [_ result]]
  (soundthing.audio.decode-audio result)))

(rf/reg-event-db
  :bad-response
  (fn [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text))))

(rf/reg-sub
  :audio-playing
  (fn [db _]
    (:audio-playing db)))

(defn toggle-audio []
  (rf/dispatch [:start-audio "true"]))

(defn home []
  [:div
    [:h1 "well hello there"]
    [:button {:on-click #(toggle-audio)} "load"]
    [:pre (-> @(rf/subscribe [:audio-playing]))]
  ])

;; does not want to run... why though
; (defn ^:export run
;   []
;   ;; (rf/dispatch-sync [:initialize])
;   (println "farts")
;   (reagent/render [home]
;                   (. js/document (getElementById "app"))))

(rf/dispatch-sync [:initialize])
  (reagent/render [home]
                  (. js/document (getElementById "app")))