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

(.addEventListener js/window "mousemove" #(mouse-handler %))
(.addEventListener js/window "deviceorientation" #(motion-handler %))
(.addEventListener js/window "touchmove" #(touch-handler %))

(defn mouse-handler [e]
  (let [x (.-clientX e)
        y (.-clientY e)
        max-height (.-innerHeight js/window)
        volume (/ y max-height)]
    (.setValueAtTime (.-frequency vco) x 0)
    (.setValueAtTime (.-gain output) y 0)))

;; alpha is the compass direction the device is facing in degrees
;; beta  is the front-to-back tilt in degrees, where front is positive
;; gamma is the left-to-right tilt in degrees, where right is positive

(defn motion-handler [e]
  (let [alpha (.-alpha e)
        freq  (* 3 (+ 300 alpha))]
    (.setValueAtTime (.-frequency vco) freq 0)))


(defn touch-handler [e]
  (.log js/console e)
  (let [] ))


;; -- js --
;; window.addEventListener("mousemove", function() { ... })

;; (.foo bar)   ;; bar.foo()
;; (.-foo bar)  ;; bar.foo

;; vco           => OscillatorNode
;; vco.frequency => AudioParam
