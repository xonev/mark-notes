(ns marknotes.test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [marknotes.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'marknotes.core-test))
    0
    1))
