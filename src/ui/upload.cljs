(ns soundthing.ui.upload
  (:require 
    [cljs.core.async :refer [chan put!]]
    [reagent.core :as reagent]
    [soundthing.data :refer [app-state]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(def first-file
  (map (fn [e]
         (let [target (.-currentTarget e)
               file (-> target .-files (aget 0))]
           (set! (.-value target) "")
           file))))

(def extract-result
  (map #(-> % .-target .-result soundthing.audio.add-to-source)))

(def upload-reqs (chan 1 first-file))
(def file-reads (chan 1 extract-result))

;; step 1
(defn do-upload [event]
  (put! upload-reqs event))

(go-loop []
  (let [reader (js/FileReader.)
        file (<! upload-reqs)]
    (swap! app-state assoc :file-name (.-name file))
    (set! (.-onload reader) #(put! file-reads %))
    (.readAsArrayBuffer reader file)
    (recur)))

(go-loop []
  (swap! app-state assoc :file-data (<! file-reads))
  (recur))

(defn button []
  [:label.button "load"
    [:input.hidden
      {:type "file" :on-change do-upload}]])
