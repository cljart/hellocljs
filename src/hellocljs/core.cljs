(ns hellocljs.core
  (:require [hum.core :as hum]))

(def ctx (hum/create-context))
(def vco (hum/create-osc ctx :sawtooth))
(def vcf (hum/create-biquad-filter ctx))
(def output (hum/create-gain ctx))

(hum/connect vco vcf)
(hum/connect vcf output)

(hum/start-osc vco)

(hum/connect-output output)

(hum/note-on output vco 440)

(.addEventListener js/window "mousemove" #(mouseismoving %))

;; (.foo bar)   ;; bar.foo()
;; (.-foo bar)  ;; bar.foo

(defn mouseismoving [e]
 (let [x (.-clientX e)
       y (/ (.-clientY e) (.-innerHeight js/window))]
  (.setValueAtTime (.-frequency vco) x 0)
  (.setValueAtTime (.-gain output) y 0)))
